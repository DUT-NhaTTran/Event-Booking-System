package com.tmnhat.Event.Service.validation;

import com.tmnhat.Event.Service.model.Event;
import com.tmnhat.common.exception.BadRequestException;

public class EventValidator {

    public static void validateEvent(Event event) {
        if (event == null) {
            throw new BadRequestException("Event data is required");
        }
        if (event.getEventName() == null || event.getEventName().trim().isEmpty()) {
            throw new BadRequestException("Event name cannot be empty");
        }
        if (event.getDescription() == null || event.getDescription().trim().isEmpty()) {
            throw new BadRequestException("Event description cannot be empty");
        }
        if (event.getLocation() == null || event.getLocation().trim().isEmpty()) {
            throw new BadRequestException("Event location cannot be empty");
        }
        if (event.getDate() == null) {
            throw new BadRequestException("Event date is required");
        }
        if (event.getAvailableTickets() < 0) {
            throw new BadRequestException("Available tickets cannot be negative");
        }
        if (event.getTicketPrice() < 0) {
            throw new BadRequestException("Ticket price cannot be negative");
        }
    }

    // Kiểm tra ID khi lấy, cập nhật hoặc xóa sự kiện
    public static void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid event ID"+id);
        }
    }
}
