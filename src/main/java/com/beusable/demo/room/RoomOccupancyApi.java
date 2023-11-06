package com.beusable.demo.room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RoomOccupancyApi {

    private final RoomOccupancyService roomOccupancyService;

    @GetMapping("api/rooms/occupancy")
    public RoomOccupancyDetails estimateRoomOccupancy(@RequestParam("premium")
                                                      Integer premium,
                                                      @RequestParam("economy")
                                                      Integer economy) {
        log.info("Estimating rooms occupancy (premium)");

        var roomOccupancyDetails = roomOccupancyService.estimateRoomOccupancy(premium, economy);
        log.info("Finished rooms occupancy estimate (details: {})", roomOccupancyDetails);

        return roomOccupancyDetails;
    }
}
