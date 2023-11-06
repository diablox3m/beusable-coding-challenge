package com.beusable.demo.guest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for guest related operations.
 *
 * @author Branko Ostojic (bostojic@boothub.io)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GuestService {

    // In reality this would come from proper repository
    private static final List<Double> guests = List.of(23d, 45d, 155d, 374d, 22d, 99.99d, 100d, 101d, 115d, 209d);

    /**
     * Returns list of all potential guests.
     *
     * @return Potential guests list.
     */
    public List<Double> getPotentialGuests() {
        log.debug("Getting list of all potential guests");
        return guests;
    }

}
