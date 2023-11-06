package com.beusable.demo.guest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GuestServiceTest {

    @InjectMocks
    private GuestService guestService;

    @Test
    @DisplayName("All potential guests returned")
    void allPotentialGuestsReturned() {
        List<Double> potentialGuests = guestService.getPotentialGuests();

        assertThat(potentialGuests).containsExactly(23d, 45d, 155d, 374d, 22d, 99.99d, 100d, 101d, 115d, 209d);
    }
}