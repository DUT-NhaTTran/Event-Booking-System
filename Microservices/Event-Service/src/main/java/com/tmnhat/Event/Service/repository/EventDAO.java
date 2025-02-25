package com.tmnhat.Event.Service.repository;

import com.tmnhat.Event.Service.model.Event;
import com.tmnhat.common.exception.DatabaseException;
import com.tmnhat.common.exception.ResourceNotFoundException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Component
public class EventDAO extends BaseDAO {

    public Long addEventAndReturnId(Event event) {
        String addEventSQL = """
                INSERT INTO events 
                (event_name, description, location, date, available_tickets, ticket_price, is_hot_event, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id
                """;

        try {
            System.out.println("INSERTING into DB: " + event.getEventName());
            return executeQuery(addEventSQL, stmt -> {
                stmt.setString(1, event.getEventName());
                stmt.setString(2, event.getDescription());
                stmt.setString(3, event.getLocation());
                stmt.setTimestamp(4, Timestamp.valueOf(event.getDate()));
                stmt.setInt(5, event.getAvailableTickets());
                stmt.setDouble(6, event.getTicketPrice());
                stmt.setBoolean(7, event.getIsHotEvent());
                LocalDateTime now = LocalDateTime.now();
                stmt.setTimestamp(8, event.getCreatedAt() != null ? Timestamp.valueOf(event.getCreatedAt()) : Timestamp.valueOf(now));
                stmt.setTimestamp(9, event.getUpdatedAt() != null ? Timestamp.valueOf(event.getUpdatedAt()) : Timestamp.valueOf(now));


                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getLong("id");
                }
                throw new DatabaseException("Failed to insert event");
            });
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting event: " + e.getMessage());
        }
    }

    public void updateEvent(Long id, Event event) {
        String updateEvent = "UPDATE events SET event_name = ?, description = ?, location = ?, date = ?, available_tickets = ?, ticket_price = ?, is_hot_event = ?, created_at = ? ,updated_at = ? WHERE id = ?";

        int rowsAffected = executeUpdate(updateEvent, stmt -> {
            stmt.setString(1, event.getEventName());
            stmt.setString(2, event.getDescription());
            stmt.setString(3, event.getLocation());
            stmt.setTimestamp(4, event.getDate() != null ? Timestamp.valueOf(event.getDate()) : null);
            stmt.setInt(5, event.getAvailableTickets());
            stmt.setDouble(6, event.getTicketPrice());
            stmt.setBoolean(7, event.getIsHotEvent());
            LocalDateTime now = LocalDateTime.now();
            stmt.setTimestamp(8, event.getCreatedAt() != null ? Timestamp.valueOf(event.getCreatedAt()) : Timestamp.valueOf(now));
            stmt.setTimestamp(9, event.getUpdatedAt() != null ? Timestamp.valueOf(event.getUpdatedAt()) : Timestamp.valueOf(now));
            stmt.setLong(10, id);
        });

        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("Event with ID " + id + " not found");
        }
    }

    public Event getEventById(Long id) {
        String query = "SELECT * FROM events WHERE id = ?";

        try {
            return executeQuery(query, stmt -> {
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new Event.Builder()
                            .id(rs.getLong("id"))
                            .eventName(rs.getString("event_name"))
                            .description(rs.getString("description"))
                            .location(rs.getString("location"))
                            .date(rs.getTimestamp("date").toLocalDateTime())
                            .availableTickets(rs.getInt("available_tickets"))
                            .ticketPrice(rs.getDouble("ticket_price"))
                            .isHotEvent(rs.getBoolean("is_hot_event"))
                            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                            .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                            .build();
                }
                return null;
            });
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching event with ID " + id + ": " + e.getMessage());
        }
    }


    public List<Event> getAllEvents() {
        String getAllEvents = "SELECT * FROM events";

        try {
            return executeQuery(getAllEvents, stmt -> {
                ResultSet rs = stmt.executeQuery();
                List<Event> eventList = new ArrayList<>();

                while (rs.next()) {
                    Event event = new Event.Builder()
                            .id(rs.getLong("id"))
                            .eventName(rs.getString("event_name"))
                            .description(rs.getString("description"))
                            .location(rs.getString("location"))
                            .date(rs.getTimestamp("date").toLocalDateTime())
                            .availableTickets(rs.getInt("available_tickets"))
                            .ticketPrice(rs.getDouble("ticket_price"))
                            .isHotEvent(rs.getBoolean("is_hot_event"))
                            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                            .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                            .build();
                    eventList.add(event);
                }

                return eventList;
            });
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all events: " + e.getMessage());
        }
    }

    public void deleteEvent(Long id) {
        String query = "DELETE FROM events WHERE id = ?";

        int rowsAffected = executeUpdate(query, stmt -> stmt.setLong(1, id));
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("Event with ID " + id + " not found");
        }
    }

    public void updateEventTickets(Long eventId, int ticketCount) {
        String query = "UPDATE events SET available_tickets = available_tickets - ? WHERE id = ?";

        int rowsAffected = executeUpdate(query, stmt -> {
            stmt.setInt(1, ticketCount);
            stmt.setLong(2, eventId);
        });

        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("Event with ID " + eventId + " not found or tickets not updated");
        }
    }
}
