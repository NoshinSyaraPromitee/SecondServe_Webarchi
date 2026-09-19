package com.secondserve.server.dto;

import java.math.BigDecimal;

public class DashboardStatsDto {

    private BigDecimal totalDonatedThisWeek;
    private BigDecimal totalLoggedThisWeek;
    private String hotelCode;


    public DashboardStatsDto() {
    }

    // --- Getters and Setters ---


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