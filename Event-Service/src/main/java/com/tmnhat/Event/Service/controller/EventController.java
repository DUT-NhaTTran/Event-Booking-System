package com.tmnhat.Event.Service.controller;

import com.tmnhat.Event.Service.model.Event;
import com.tmnhat.Event.Service.service.EventService;
import com.tmnhat.Event.Service.service.Impl.EventServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;
    // Nếu đổi các phương thức, chỉ cần đổi class impl
    public EventController() {
        this.eventService = new EventServiceImpl(); // Khởi tạo service
    }

    @GetMapping()
    public ResponseEntity<List<Event>> getAllEvents() {
        try {
            List<Event> events = eventService.getAllEvents();
            return ResponseEntity.ok(events);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable("id") Long id) throws SQLException {
        Event event = eventService.getEventById(id);
        if (event != null) {
            return ResponseEntity.ok(event);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public ResponseEntity<String> addEvent(@RequestBody Event event) throws SQLException {
        eventService.addEvent(event);
        return ResponseEntity.ok("Thêm thành công");
    }
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateEvent(@PathVariable("id") Long id,@RequestBody Event event) throws SQLException {
        eventService.updateEvent(id,event);
        return ResponseEntity.ok("Cập nhật thành công");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable("id") Long id) throws SQLException {
        eventService.deleteEvent(id);
        return ResponseEntity.ok(" Sự kiện đã được xóa thành công!");
    }


}
