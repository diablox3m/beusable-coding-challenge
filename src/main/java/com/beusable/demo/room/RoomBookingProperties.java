package com.beusable.demo.room;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "room.booking")
public class RoomBookingProperties {

    private final Double premiumPriceMargin;
}
