package com.beusable.demo.room;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Room occupancy details model.
 *
 * @author Branko Ostojic (bostojic@boothub.io)
 */
@Getter
@Builder
@ToString
public class RoomOccupancyDetails {
    private final RoomOccupancy premium;
    private final RoomOccupancy economy;
}
