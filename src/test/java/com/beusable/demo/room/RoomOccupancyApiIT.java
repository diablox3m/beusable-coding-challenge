package com.beusable.demo.room;

import com.beusable.demo.SpringTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@DisplayName("RoomOccupancyApi")
class RoomOccupancyApiIT extends SpringTestBase {

    @Test
    @DisplayName("Premium room count not provided")
    void premiumRoomsCountNotProvided() {
        given()
                .when()
                .get(url("api/rooms/occupancy?economy=3"))
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Economy room count not provided")
    void economyRoomsCountNotProvided() {
        given()
                .when()
                .get(url("api/rooms/occupancy?premium=3"))
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Negative room numbers considered as 0")
    void negativeRoomNumbers() {
        given()
                .when()
                .get(url("api/rooms/occupancy?premium=-2&economy=-1"))
                .then()
                .log().all()
                .body("premium.roomsOccupied", is(0))
                .body("premium.price", is(0f))
                .body("economy.roomsOccupied", is(0))
                .body("economy.price", is(0f));
    }

    @Test
    @DisplayName("Test case 1")
    void testCase1() {
        given()
                .when()
                .get(url("api/rooms/occupancy?premium=3&economy=3"))
                .then()
                .log().all()
                .body("premium.roomsOccupied", is(3))
                .body("premium.price", is(738f))
                .body("economy.roomsOccupied", is(3))
                .body("economy.price", is(167.99f));
    }

    @Test
    @DisplayName("Test case 2")
    void testCase2() {
        given()
                .when()
                .get(url("api/rooms/occupancy?premium=7&economy=5"))
                .then()
                .log().all()
                .body("premium.roomsOccupied", is(6))
                .body("premium.price", is(1054f))
                .body("economy.roomsOccupied", is(4))
                .body("economy.price", is(189.99f));
    }

    @Test
    @DisplayName("Test case 3")
    void testCase3() {
        given()
                .when()
                .get(url("api/rooms/occupancy?premium=2&economy=7"))
                .then()
                .log().all()
                .body("premium.roomsOccupied", is(2))
                .body("premium.price", is(583f))
                .body("economy.roomsOccupied", is(4))
                .body("economy.price", is(189.99f));
    }

    @Test
    @DisplayName("Test case 4")
    void testCase4() {
        given()
                .when()
                .get(url("api/rooms/occupancy?premium=7&economy=1"))
                .then()
                .log().all()
                .body("premium.roomsOccupied", is(7))
                .body("premium.price", is(1153.99f))
                .body("economy.roomsOccupied", is(1))
                .body("economy.price", is(45.0f));
    }
}