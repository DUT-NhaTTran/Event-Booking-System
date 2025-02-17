package com.tmnhat.bookingservice.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Booking {
    private Long id;
    private String bookingName;
    private Long eventId;
    private Long userId;


    private int ticketCount;
    private String status;
    private LocalDateTime createdAt;

    private Booking(Builder builder) {
        this.id = builder.id;
        this.bookingName = builder.bookingName;
        this.eventId = builder.eventId;
        this.userId = builder.userId;
        this.ticketCount = builder.ticketCount;
        this.status = builder.status;
        this.createdAt = builder.createdAt;
    }

    private Booking() {

    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getBookingName() {
        return bookingName;
    }

    public Long getEventId() {
        return eventId;
    }

    public Long getUserId() {
        return userId;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    //Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setBookingName(String bookingName) {
        this.bookingName = bookingName;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Builder Class
    public static class Builder {
        private Long id;
        private String bookingName;
        private Long eventId;
        private Long userId;
        private int ticketCount;
        private String status;
        private LocalDateTime createdAt;

        public Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder bookingName(String bookingName) {
            this.bookingName = bookingName;
            return this;
        }

        public Builder eventId(Long eventId) {
            this.eventId = eventId;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder ticketCount(int ticketCount) {
            this.ticketCount = ticketCount;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Booking build() {
            return new Booking(this);
        }
    }
}
