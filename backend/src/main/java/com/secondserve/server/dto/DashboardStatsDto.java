package com.secondserve.server.dto;

import java.math.BigDecimal;

/**
 * A Data Transfer Object used to send the calculated statistics for the
 * Hotel Manager's dashboard from the server to the client.
 */
public class DashboardStatsDto {

    private BigDecimal totalDonatedThisWeek;
    private BigDecimal totalLoggedThisWeek;
    private String hotelCode;

    /**
     * A no-argument constructor is required for libraries like Jackson
     * to deserialize JSON back into this Java object.
     */
    public DashboardStatsDto() {
    }

    // --- Getters and Setters ---
    // These methods allow other parts of the application (like the service and
    // the JSON converter) to access and set the private fields.

    public BigDecimal getTotalDonatedThisWeek() {
        return totalDonatedThisWeek;
    }

    public void setTotalDonatedThisWeek(BigDecimal totalDonatedThisWeek) {
        this.totalDonatedThisWeek = totalDonatedThisWeek;
    }

    public BigDecimal getTotalLoggedThisWeek() {
        return totalLoggedThisWeek;
    }

    public void setTotalLoggedThisWeek(BigDecimal totalLoggedThisWeek) {
        this.totalLoggedThisWeek = totalLoggedThisWeek;
    }

    public String getHotelCode() {
        return hotelCode;
    }

    public void setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
    }
}