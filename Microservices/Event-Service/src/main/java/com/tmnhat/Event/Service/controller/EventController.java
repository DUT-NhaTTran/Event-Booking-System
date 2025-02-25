package com.tmnhat.Event.Service.controller;

import com.tmnhat.Event.Service.model.Event;
import com.tmnhat.Event.Service.service.EventService;
import com.tmnhat.Event.Service.service.Impl.EventServiceImpl;
import com.tmnhat.Event.Service.validation.EventValidator;
import com.tmnhat.common.payload.ResponseDataAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventServiceImpl eventService;
    @Autowired
    public EventController(EventServiceImpl eventService) {
        this.eventService = eventService;
    }

    // Lấy danh sách tất cả sự kiện
    @GetMapping()
    public ResponseEntity<ResponseDataAPI> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(events));
    }

    // Lấy danh sách sự kiện hot
    @GetMapping("/hot-events")
    public ResponseEntity<ResponseDataAPI> getAllHotEvents() {
        List<Event> hotEvents = eventService.getAllHotEvents();
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(hotEvents));
    }

    // Lấy thông tin sự kiện theo ID (Kiểm tra ID hợp lệ)
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDataAPI> getEventById(@PathVariable("id") Long id) {
        EventValidator.validateId(id); // Kiểm tra ID hợp lệ
        Event event = eventService.getEventById(id);
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(event));
    }

    //Thêm sự kiện mới (Kiểm tra dữ liệu hợp lệ)
    @PostMapping()
    public ResponseEntity<ResponseDataAPI> addEvent(@RequestBody Event event) {
        EventValidator.validateEvent(event); // Kiểm tra dữ liệu đầu vào
        eventService.addEvent(event);
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(event));
    }

    // Cập nhật thông tin sự kiện (Kiểm tra ID & dữ liệu hợp lệ)
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDataAPI> updateEvent(@PathVariable("id") Long id, @RequestBody Event event) {
        EventValidator.validateId(id);     // Kiểm tra ID hợp lệ
        EventValidator.validateEvent(event); // Kiểm tra dữ liệu đầu vào
        eventService.updateEvent(id, event);
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMetaAndData());
    }

    // Xóa sự kiện theo ID (Kiểm tra ID hợp lệ)
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDataAPI> deleteEvent(@PathVariable("id") Long id) {
        EventValidator.validateId(id); // Kiểm tra ID hợp lệ
        eventService.deleteEvent(id);
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMetaAndData());
    }
}
