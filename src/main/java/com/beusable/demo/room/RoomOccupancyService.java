package com.beusable.demo.room;

import com.beusable.demo.guest.GuestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Comparator.reverseOrder;

/**
 * Service for room occupancy related operations.
 *
 * @author Branko Ostojic (bostojic@boothub.io)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoomOccupancyService {

    private final GuestService guestService;
    private final RoomBookingProperties roomBookingProperties;

    /**
     * Estimates rooms occupancy and earnings based on the potential guests and provided availability per room category.
     *
     * @param availablePremiumRoomsCount Available premium rooms count.
     * @param availableEconomyRoomsCount Available economy rooms count.
     * @return Room occupancy estimate.
     */
    public RoomOccupancyDetails estimateRoomOccupancy(Integer availablePremiumRoomsCount,
                                                      Integer availableEconomyRoomsCount) {
        log.info("Estimating room occupancy (availablePremiumRoomsCount: {}, availableEconomyRoomsCount: {})",
                availablePremiumRoomsCount, availablePremiumRoomsCount);
        final var premiumRoomReservationsQueue = new PriorityQueue<Double>(reverseOrder());
        final var economyRoomReservationsQueue = new PriorityQueue<Double>(reverseOrder());

        this.populateRoomReservationQueues(premiumRoomReservationsQueue, economyRoomReservationsQueue);
        this.upgradeApplicableReservations(premiumRoomReservationsQueue, economyRoomReservationsQueue,
                availablePremiumRoomsCount, availableEconomyRoomsCount);

        var premiumRoomReservations = handleReservations(premiumRoomReservationsQueue, availablePremiumRoomsCount);
        log.info("Estimated premium room reservations (reservations: {})", premiumRoomReservations);
        var economyRoomReservations = handleReservations(economyRoomReservationsQueue, availableEconomyRoomsCount);
        log.info("Estimated economy room reservations (reservations: {})", economyRoomReservations);

        return RoomOccupancyDetails.builder()
                .premium(RoomOccupancy.builder()
                        .roomsOccupied(premiumRoomReservations.size())
                        .price(premiumRoomReservations.stream().mapToDouble(it -> it).sum())
                        .build())
                .economy(RoomOccupancy.builder()
                        .roomsOccupied(economyRoomReservations.size())
                        .price(economyRoomReservations.stream().mapToDouble(it -> it).sum())
                        .build())
                .build();
    }

    /**
     * Populates room reservation queues based on potential guests and price (room) category.
     *
     * @param premiumRoomReservationsQueue Premium room reservations queue.
     * @param economyRoomReservationsQueue Economy room reservations queue.
     */
    private void populateRoomReservationQueues(PriorityQueue<Double> premiumRoomReservationsQueue,
                                               PriorityQueue<Double> economyRoomReservationsQueue) {
        log.info("Populating room reservations queues.");
        guestService.getPotentialGuests().forEach(it -> {
            if (isPremiumRoomPrice(it)) {
                premiumRoomReservationsQueue.add(it);
            } else {
                economyRoomReservationsQueue.add(it);
            }
        });
    }

    /**
     * Handles reservations based on provided reservations queue and available rooms count.
     * This method assumes that reservations are ordered by descending price in the queue.
     *
     * @param reservationsQueue        Reservations queue.
     * @param totalAvailableRoomsCount Total available rooms count.
     * @return Reservations list.
     */
    private ArrayList<Double> handleReservations(final PriorityQueue<Double> reservationsQueue,
                                                 final Integer totalAvailableRoomsCount) {
        var roomReservations = new ArrayList<Double>();
        var remainingAvailableRooms = new AtomicInteger(totalAvailableRoomsCount);
        while (!reservationsQueue.isEmpty() && remainingAvailableRooms.get() > 0) {
            roomReservations.add(reservationsQueue.poll());
            remainingAvailableRooms.decrementAndGet();
        }

        return roomReservations;
    }

    /**
     * Upgrades economy into premium category reservation if applicable.
     * This method will calculate potential available premium rooms after all premium reservations are satisfied
     * and promote economy reservations with highest offered amount only if there are no more free economy rooms.
     *
     * @param premiumRoomReservationsQueue Premium room reservations queue.
     * @param economyRoomReservationsQueue Economy room reservations queue.
     * @param availablePremiumRoomsCount   Available premium rooms count.
     * @param availableEconomyRoomsCount   Available economy rooms count.
     */
    private void upgradeApplicableReservations(final PriorityQueue<Double> premiumRoomReservationsQueue,
                                               final PriorityQueue<Double> economyRoomReservationsQueue,
                                               final Integer availablePremiumRoomsCount,
                                               final Integer availableEconomyRoomsCount) {
        log.info("Upgrading applicable reservations (premiumRoomReservations: {}, economyRoomReservations: {}," +
                        " availablePremiumRoomsCount: {}, availableEconomyRoomsCount: {})",
                premiumRoomReservationsQueue.size(), economyRoomReservationsQueue.size(),
                availablePremiumRoomsCount, availableEconomyRoomsCount);
        int applicableForUpgrade = 0;
        int sparePremiumRooms = Math.max(availablePremiumRoomsCount - premiumRoomReservationsQueue.size(), 0);
        if (economyRoomReservationsQueue.size() > availableEconomyRoomsCount && sparePremiumRooms > 0) {
            applicableForUpgrade = Math.min(
                    sparePremiumRooms, economyRoomReservationsQueue.size() - availableEconomyRoomsCount);
        }

        log.info("Found {} reservations applicable for upgrade.", applicableForUpgrade);

        while (applicableForUpgrade > 0) {
            premiumRoomReservationsQueue.add(economyRoomReservationsQueue.poll());
            --applicableForUpgrade;
        }
    }

    /**
     * Determines if offered amount is enough to be considered as premium room price.
     *
     * @param amount Offered amount.
     * @return Premium room price indicator.
     */
    private boolean isPremiumRoomPrice(Double amount) {
        return amount >= roomBookingProperties.getPremiumPriceMargin();
    }

}
