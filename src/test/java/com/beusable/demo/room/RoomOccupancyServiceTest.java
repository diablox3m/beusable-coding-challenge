package com.beusable.demo.room;

import com.beusable.demo.guest.GuestService;
import com.beusable.demo.room.RoomBookingProperties;
import com.beusable.demo.room.RoomOccupancyDetails;
import com.beusable.demo.room.RoomOccupancyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomOccupancyServiceTest {

    private static final List<Double> potentialGuests = of(23d, 45d, 155d, 374d, 22d, 99.99d, 100d, 101d, 115d, 209d);

    @InjectMocks
    private RoomOccupancyService roomOccupancyService;
    @Mock
    private GuestService guestService;
    @Mock
    private RoomBookingProperties roomBookingProperties;

    @BeforeEach
    public void setup() {
        when(roomBookingProperties.getPremiumPriceMargin()).thenReturn(100d);
        when(guestService.getPotentialGuests()).thenReturn(potentialGuests);
    }

    @Test
    @DisplayName("Test 1")
    void test1() {
        RoomOccupancyDetails roomOccupancyDetails = roomOccupancyService.estimateRoomOccupancy(3, 3);

        assertThat(roomOccupancyDetails.getPremium().getRoomsOccupied()).isEqualTo(3);
        assertThat(roomOccupancyDetails.getPremium().getPrice()).isEqualTo(738d);
        assertThat(roomOccupancyDetails.getEconomy().getRoomsOccupied()).isEqualTo(3);
        assertThat(roomOccupancyDetails.getEconomy().getPrice()).isEqualTo(167.99d);
    }

    @Test
    @DisplayName("Test 2")
    void test2() {
        RoomOccupancyDetails roomOccupancyDetails = roomOccupancyService.estimateRoomOccupancy(7, 5);

        assertThat(roomOccupancyDetails.getPremium().getRoomsOccupied()).isEqualTo(6);
        assertThat(roomOccupancyDetails.getPremium().getPrice()).isEqualTo(1054d);
        assertThat(roomOccupancyDetails.getEconomy().getRoomsOccupied()).isEqualTo(4);
        assertThat(roomOccupancyDetails.getEconomy().getPrice()).isEqualTo(189.99d);
    }

    @Test
    @DisplayName("Test 3")
    void test3() {
        RoomOccupancyDetails roomOccupancyDetails = roomOccupancyService.estimateRoomOccupancy(2, 7);

        assertThat(roomOccupancyDetails.getPremium().getRoomsOccupied()).isEqualTo(2);
        assertThat(roomOccupancyDetails.getPremium().getPrice()).isEqualTo(583d);
        assertThat(roomOccupancyDetails.getEconomy().getRoomsOccupied()).isEqualTo(4);
        assertThat(roomOccupancyDetails.getEconomy().getPrice()).isEqualTo(189.99d);
    }

    @Test
    @DisplayName("Test 4")
    void test4() {
        RoomOccupancyDetails roomOccupancyDetails = roomOccupancyService.estimateRoomOccupancy(7, 1);

        assertThat(roomOccupancyDetails.getPremium().getRoomsOccupied()).isEqualTo(7);
        assertThat(roomOccupancyDetails.getPremium().getPrice()).isEqualTo(1153.99d);
        assertThat(roomOccupancyDetails.getEconomy().getRoomsOccupied()).isEqualTo(1);
        assertThat(roomOccupancyDetails.getEconomy().getPrice()).isEqualTo(45d);
    }

}
