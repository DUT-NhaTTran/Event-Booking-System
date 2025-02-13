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

    //Constructor
    public Event(Long id, String eventName, String description, String location, LocalDateTime date, Integer availableTickets, Double ticketPrice, Boolean isHotEvent, LocalDateTime createdAt, LocalDateTime updateAt) {
        this.id = id;
        this.eventName = eventName;
        this.description = description;
        this.location = location;
        this.date = date;
        this.availableTickets = availableTickets;
        this.ticketPrice = ticketPrice;
        this.isHotEvent = isHotEvent;
        this.createdAt = createdAt;
        this.updatedAt = updateAt;
    }
    public Event(){

    }
    //Getter , Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(Integer availableTickets) {
        this.availableTickets = availableTickets;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Boolean getIsHotEvent() {
        return isHotEvent;
    }

    public void setIsHotEvent(Boolean hotEvent) {
        isHotEvent = hotEvent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdateAt() {
        return updatedAt;
    }

    public void setUpdateAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


}
