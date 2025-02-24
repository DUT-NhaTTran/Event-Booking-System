package com.tmnhat.ticketservice.controller;

import com.tmnhat.ticketservice.model.Ticket;
import com.tmnhat.ticketservice.service.Impl.TicketServiceImpl;
import com.tmnhat.ticketservice.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(){
        this.ticketService= new TicketServiceImpl();
    }

//    @PostMapping()
//    public ResponseEntity<String> addTicket(@RequestBody Ticket ticket) throws SQLException {
//        ticketService.addTicket(ticket);
//        return ResponseEntity.ok("Ticket added successfully");
//    }
    @PostMapping
    public ResponseEntity<String> addTicket(@RequestBody Ticket ticket) {
        System.out.println("📌 Received Ticket: " + ticket);

        if (ticket.getIsSold() == null) {
            return ResponseEntity.badRequest().body("isSold không được để trống!");
        }

        try {
            ticketService.addTicket(ticket);
            return ResponseEntity.ok("✅ Thêm vé thành công!");
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Lỗi hệ thống khi thêm vé.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTicket(@PathVariable("id") Long id,@RequestBody Ticket ticket) throws SQLException {
        ticketService.updateTicket(id,ticket);
        return ResponseEntity.ok("Ticket updated successfully");
    }
    @GetMapping()
    public ResponseEntity<List<Ticket>> getAllTickets() throws SQLException {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable("id") Long id) throws SQLException {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }
}
