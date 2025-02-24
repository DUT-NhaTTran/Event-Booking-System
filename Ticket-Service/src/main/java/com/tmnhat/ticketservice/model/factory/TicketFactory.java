package com.tmnhat.ticketservice.model.factory;

import com.tmnhat.ticketservice.model.StandardTicket;
import com.tmnhat.ticketservice.model.Ticket;
import com.tmnhat.ticketservice.model.TicketType;
import com.tmnhat.ticketservice.model.VipTicket;

import java.time.LocalDateTime;

public class TicketFactory {
    public static Ticket createTicket(TicketType type, Long eventId, Double basePrice, Boolean isSold, LocalDateTime createdAt) {
        if (type == TicketType.VIP) {
                return new VipTicket(eventId, basePrice, isSold, createdAt);
        }
        else return new StandardTicket(eventId, basePrice, isSold, createdAt);
    }
}