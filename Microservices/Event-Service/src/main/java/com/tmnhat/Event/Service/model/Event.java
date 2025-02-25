package com.tmnhat.Event.Service.model;

import java.time.LocalDateTime;

public class Event {
    private Long id;
    private String eventName;
    private String description;
    private String location;
    private LocalDateTime date;
    private Integer availableTickets;
    private Double ticketPrice;
    private Boolean isHotEvent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Event(Builder builder) {
        this.id = builder.id;
        this.eventName = builder.eventName;
        this.description = builder.description;
        this.location = builder.location;
        this.date = builder.date;
        this.availableTickets = builder.availableTickets;
        this.ticketPrice = builder.ticketPrice;
        this.isHotEvent = builder.isHotEvent;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }
    private Event(){

    }

    // Getters
    public Long getId() { return id; }
    public String getEventName() { return eventName; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public LocalDateTime getDate() { return date; }
    public Integer getAvailableTickets() { return availableTickets; }
    public Double getTicketPrice() { return ticketPrice; }
    public Boolean getIsHotEvent() { return isHotEvent; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    //Setters
    public void setId(Long id) {
        this.id = id;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setAvailableTickets(Integer availableTickets) {
        this.availableTickets = availableTickets;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public void setHotEvent(Boolean hotEvent) {
        isHotEvent = hotEvent;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    // Builder Class
    public static class Builder {
        private Long id;
        private String eventName;
        private String description;
        private String location;
        private LocalDateTime date=LocalDateTime.now();
        private Integer availableTickets;
        private Double ticketPrice;
        private Boolean isHotEvent;
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime updatedAt = LocalDateTime.now();

        public Builder() {}

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder eventName(String eventName) {
            this.eventName = eventName;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder date(LocalDateTime date) {
            this.date = date != null ? date : LocalDateTime.now();
            return this;
        }

        public Builder availableTickets(Integer availableTickets) {
            this.availableTickets = availableTickets;
            return this;
        }

        public Builder ticketPrice(Double ticketPrice) {
            this.ticketPrice = ticketPrice;
            return this;
        }

        public Builder isHotEvent(Boolean isHotEvent) {
            this.isHotEvent = isHotEvent;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt != null ? updatedAt : LocalDateTime.now();
            return this;
        }

        public Event build() {
            return new Event(this);
        }
    }
}
