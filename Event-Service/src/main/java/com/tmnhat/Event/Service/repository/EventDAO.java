package com.tmnhat.Event.Service.repository;

import com.tmnhat.Event.Service.model.Event;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventDAO extends BaseDAO {

    public void addEvent(Event event) throws SQLException {
        String addEvent = "INSERT INTO events (event_name, description, location, date, available_tickets, ticket_price, is_hot_event, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)\n";
        executeUpdate(addEvent, stmt -> {
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
        });

    }

    public void updateEvent(Long id, Event event) throws SQLException {
        String updateEvent = "UPDATE events SET event_name = ?, description = ?, location = ?, date = ?, available_tickets = ?, ticket_price = ?, is_hot_event = ?, created_at = ? ,updated_at = ? WHERE id = ?\n";
        executeUpdate(updateEvent, stmt -> {

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
    }

    public Event getEventById(Long id) throws SQLException {
        String query = "SELECT * FROM events WHERE id = ?";

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
    }


    public List<Event> getAllEvents() throws SQLException {
        String getAllEvents = "SELECT * FROM events";
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
    }

    public void deleteEvent(Long id) throws SQLException {
        String query = "DELETE FROM events WHERE id = ?";
        executeUpdate(query, stmt -> stmt.setLong(1, id));
    }

}
