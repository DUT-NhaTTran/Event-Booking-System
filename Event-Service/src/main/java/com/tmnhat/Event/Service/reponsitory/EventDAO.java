package com.tmnhat.Event.Service.reponsitory;

import com.tmnhat.Event.Service.config.DatabaseConnection;
import com.tmnhat.Event.Service.config.SQLPropertyLoader;
import com.tmnhat.Event.Service.model.Event;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    public void addEvent(Event event) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQLPropertyLoader.getQuery("insertEvent"))) {

            stmt.setString(1, event.getEventName());
            stmt.setString(2, event.getDescription());
            stmt.setString(3, event.getLocation());

            // Kiểm tra null trước khi chuyển thành Timestamp
            stmt.setTimestamp(4, event.getDate() != null ? Timestamp.valueOf(event.getDate()) : null);
            stmt.setInt(5, event.getAvailableTickets());
            stmt.setDouble(6, event.getTicketPrice());
            stmt.setBoolean(7, event.getIsHotEvent());

            // Kiểm tra null trước khi set createdAt và updatedAt
            LocalDateTime now = LocalDateTime.now();
            stmt.setTimestamp(8, event.getCreatedAt() != null ? Timestamp.valueOf(event.getCreatedAt()) : Timestamp.valueOf(now));
            stmt.setTimestamp(9, event.getUpdateAt() != null ? Timestamp.valueOf(event.getUpdateAt()) : Timestamp.valueOf(now));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEvent(Long id, Event event) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQLPropertyLoader.getQuery("updateEvent"))) {
            stmt.setString(1, event.getEventName());
            stmt.setString(2, event.getDescription());
            stmt.setString(3, event.getLocation());
            stmt.setTimestamp(4, java.sql.Timestamp.valueOf(event.getDate()));
            stmt.setInt(5, event.getAvailableTickets());
            stmt.setDouble(6, event.getTicketPrice());
            stmt.setBoolean(7, event.getIsHotEvent());
            stmt.setTimestamp(8, java.sql.Timestamp.valueOf(event.getCreatedAt()));
            stmt.setTimestamp(9, java.sql.Timestamp.valueOf(event.getCreatedAt()));
            stmt.setLong(10, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Event getEventById(Long id) throws SQLException {
        Event getEvent = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQLPropertyLoader.getQuery("getEventById"))) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                getEvent = new Event(
                        rs.getLong("id"),
                        rs.getString("event_name"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getTimestamp("date").toLocalDateTime(),
                        rs.getInt("available_tickets"),
                        rs.getDouble("ticket_price"),
                        rs.getBoolean("is_hot_event"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                );
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getEvent;
    }

    public List<Event> getAllEvents() throws SQLException {
        List<Event> eventList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQLPropertyLoader.getQuery("getAllEvents"))) {


            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Event event = new Event(
                            rs.getLong("id"),
                            rs.getString("event_name"),
                            rs.getString("description"),
                            rs.getString("location"),
                            rs.getTimestamp("date").toLocalDateTime(),
                            rs.getInt("available_tickets"),
                            rs.getDouble("ticket_price"),
                            rs.getBoolean("is_hot_event"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("updated_at").toLocalDateTime()
                    );
                    eventList.add(event);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi lấy dữ liệu sự kiện từ PostgreSQL", e);
        }

        return eventList;
    }

    public void deleteEvent(Long id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQLPropertyLoader.getQuery("deleteEvent"))) {
            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
