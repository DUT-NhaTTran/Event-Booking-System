package com.tmnhat.ticketservice.service;

import com.tmnhat.ticketservice.model.Ticket;

import java.sql.SQLException;
import java.util.List;

public interface TicketService {
    void addTicket(Ticket ticket) throws SQLException;
    void updateTicket(Long id,Ticket ticket) throws SQLException;
    List<Ticket> getAllTickets() throws SQLException;
    Ticket getTicketById(Long id) throws SQLException;
}
