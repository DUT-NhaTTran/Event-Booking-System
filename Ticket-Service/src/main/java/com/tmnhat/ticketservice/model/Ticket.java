package com.tmnhat.ticketservice.model;

import java.time.LocalDateTime;

public class Ticket {
    private final Long id;
    private final Long eventId;
    private final String type;
    private final Double price;
    private final Boolean isSold;
    private final LocalDateTime createdAt;

    // Constructor sử dụng Builder Pattern
    protected Ticket(Builder builder) {
        this.id = builder.id;
        this.eventId = builder.eventId;
        this.type = builder.type;
        this.price = builder.price;
        this.isSold = builder.isSold;
        this.createdAt = builder.createdAt;
    }


    // Getters (Không có Setters để đảm bảo tính bất biến)
    public Long getId() {
        return id;
    }

    public Long getEventId() {
        return eventId;
    }

    public String getType() {
        return type;
    }

    public Double getPrice() {
        return price;
    }

    public Boolean getIsSold() {
        return isSold;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Builder Pattern
    public static class Builder {
        private Long id;
        private Long eventId;
        private String type;
        private Double price;
        private Boolean isSold;
        private LocalDateTime createdAt;

        public Builder() {}

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder eventId(Long eventId) {
            this.eventId = eventId;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder price(Double price) {
            this.price = price;
            return this;
        }

        public Builder isSold(Boolean isSold) {
            this.isSold = isSold;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Ticket build() {
            return new Ticket(this);
        }
    }
}
