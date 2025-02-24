package com.tmnhat.ticketservice.repository;

import com.tmnhat.ticketservice.model.Ticket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO extends BaseDAO {

    public void addTicket(Ticket ticket) throws SQLException {
        String addTicket = "INSERT INTO ticket (event_id,type,price,is_sold,created_at) VALUES (?,?,?,?,?)";
        executeUpdate(addTicket, stmt -> {
            stmt.setLong(1, ticket.getEventId());
            stmt.setString(2, ticket.getType());
            stmt.setDouble(3, ticket.getPrice());
            stmt.setBoolean(4, ticket.getIsSold());
            stmt.setTimestamp(5, ticket.getCreatedAt() != null ? Timestamp.valueOf(ticket.getCreatedAt()) : null);

        });
    }

    public void updateTicket(Long id, Ticket ticket) throws SQLException {
        String updateTicket = "UPDATE Ticket SET event_id=?,type=?,price=?,is_sold=?,created_at=? WHERE id=?";
        executeUpdate(updateTicket, stmt -> {
            stmt.setLong(1, ticket.getEventId());
            stmt.setString(2, ticket.getType());
            stmt.setDouble(3, ticket.getPrice());
            stmt.setBoolean(4, ticket.getIsSold());
            stmt.setTimestamp(5, ticket.getCreatedAt() != null ? Timestamp.valueOf(ticket.getCreatedAt()) : null);
            stmt.setLong(6, id);

        });
    }

    public List<Ticket> getAllTickets() throws SQLException {
        String getAllTickets = "SELECT * FROM Ticket";
        return executeQuery(getAllTickets, stmt -> {
            List<Ticket> ticketList = new ArrayList<>();
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Ticket ticket = new Ticket.Builder()
                        .id(rs.getLong("id"))
                        .eventId(rs.getLong("event_id"))
                        .type(rs.getString("type"))
                        .price(rs.getDouble("price"))
                        .isSold(rs.getBoolean("is_sold"))
                        .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                        .build();
                ticketList.add(ticket);
            }
            return ticketList;
        });
    }

    public Ticket getTicketById(Long id) throws SQLException {
        String getTicketById = "SELECT * FROM Ticket WHERE id=?";
        return executeQuery(getTicketById, stmt -> {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Ticket.Builder()
                        .id(rs.getLong("id"))
                        .eventId(rs.getLong("event_id"))
                        .type(rs.getString("type"))
                        .price(rs.getDouble("price"))
                        .isSold(rs.getBoolean("is_sold"))
                        .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                        .build();
            }
            return null;
        });
    }

}
