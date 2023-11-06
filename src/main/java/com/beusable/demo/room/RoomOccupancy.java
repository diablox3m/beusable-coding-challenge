package com.beusable.demo.room;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Room occupancy model.
 *
 * @author Branko Ostojic (bostojic@boothub.io)
 */
@Getter
@Builder
@ToString
public class RoomOccupancy {
    private final Integer roomsOccupied;
    private final Double price;
}
