package com.tmnhat.ticketservice.service.Impl;

import com.tmnhat.ticketservice.model.Ticket;
import com.tmnhat.ticketservice.repository.TicketDAO;
import com.tmnhat.ticketservice.service.TicketService;

import java.sql.SQLException;
import java.util.List;

public class TicketServiceImpl implements TicketService {
    private final TicketDAO ticketDAO=new TicketDAO();

    @Override
    public void addTicket(Ticket ticket) throws SQLException {
        ticketDAO.addTicket(ticket);
    }

    @Override
    public void updateTicket(Long id,Ticket ticket) throws SQLException {
        ticketDAO.updateTicket(id,ticket);
    }

    @Override
    public List<Ticket> getAllTickets() throws SQLException {
        return ticketDAO.getAllTickets();
    }

    @Override
    public Ticket getTicketById(Long id) throws SQLException {
        return ticketDAO.getTicketById(id);
    }
}
