//package com.tmnhat.Event.Service.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.tmnhat.Event.Service.model.Event;
//import com.tmnhat.Event.Service.service.EventService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(EventController.class)
//@ExtendWith(SpringExtension.class)
//// Chỉ test Controller
//public class EventControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc; // Giả lập HTTP Request
//
//    @MockBean
//    private EventService eventService; // Mock EventService
//
//    @Autowired
//    private ObjectMapper objectMapper; // Chuyển đổi Object <-> JSON
//
//    private Event testEvent;
//
//    @BeforeEach
//    void setUp() {
//        testEvent = new Event.Builder()
//                .id(1L)
//                .eventName("Test Event")
//                .description("Test Description")
//                .location("Test Location")
//                .date(LocalDateTime.now())
//                .availableTickets(50)
//                .ticketPrice(100.0)
//                .isHotEvent(true)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//    }
//
//    // Test API GET /events
//    @Test
//    public void givenEventsExist_whenGetAllEvents_thenReturnEvents() throws Exception {
//        List<Event> events = Arrays.asList(testEvent, testEvent);
//        when(eventService.getAllEvents()).thenReturn(events);
//
//        mockMvc.perform(get("/events"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data", hasSize(2))) // Kiểm tra có 2 sự kiện trả về
//                .andExpect(jsonPath("$.data[0].eventName").value("Test Event"));
//    }
//
//    // 🟢 Test API GET /events/{id}
//    @Test
//    public void givenEventExists_whenGetEventById_thenReturnEvent() throws Exception {
//        when(eventService.getEventById(1L)).thenReturn(testEvent);
//
//        mockMvc.perform(get("/events/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.eventName").value("Test Event"));
//    }
//
//    // 🔴 Test API GET /events/{id} khi không tìm thấy sự kiện
//    @Test
//    public void givenEventNotFound_whenGetEventById_thenReturnNotFound() throws Exception {
//        when(eventService.getEventById(anyLong())).thenThrow(new RuntimeException("Event not found"));
//
//        mockMvc.perform(get("/events/99"))
//                .andExpect(status().isInternalServerError()) // Trả về lỗi 500
//                .andExpect(jsonPath("$.message").value("Event not found"));
//    }
//
//    // 🟢 Test API POST /events (Thêm sự kiện)
//    @Test
//    public void givenValidEvent_whenAddEvent_thenReturnSuccess() throws Exception {
//        doNothing().when(eventService).addEvent(any(Event.class));
//
//        mockMvc.perform(post("/events")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(testEvent)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.eventName").value("Test Event"));
//    }
//
//    // 🔴 Test API POST /events với dữ liệu không hợp lệ
//    @Test
//    public void givenInvalidEvent_whenAddEvent_thenReturnBadRequest() throws Exception {
//        Event invalidEvent = new Event.Builder().build(); // Không có dữ liệu
//
//        mockMvc.perform(post("/events")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidEvent)))
//                .andExpect(status().isInternalServerError());
//    }
//
//    // 🟢 Test API PATCH /events/{id} (Cập nhật sự kiện)
//    @Test
//    public void givenValidEvent_whenUpdateEvent_thenReturnSuccess() throws Exception {
//        doNothing().when(eventService).updateEvent(anyLong(), any(Event.class));
//
//        mockMvc.perform(patch("/events/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(testEvent)))
//                .andExpect(status().isOk());
//    }
//
//    // 🔴 Test API PATCH /events/{id} khi không tìm thấy sự kiện
//    @Test
//    public void givenEventNotFound_whenUpdateEvent_thenReturnNotFound() throws Exception {
//        doThrow(new RuntimeException("Event not found"))
//                .when(eventService).updateEvent(anyLong(), any(Event.class));
//
//        mockMvc.perform(patch("/events/99")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(testEvent)))
//                .andExpect(status().isInternalServerError())
//                .andExpect(jsonPath("$.message").value("Event not found"));
//    }
//
//    // 🟢 Test API DELETE /events/{id} (Xóa sự kiện)
//    @Test
//    public void givenEventExists_whenDeleteEvent_thenReturnSuccess() throws Exception {
//        doNothing().when(eventService).deleteEvent(1L);
//
//        mockMvc.perform(delete("/events/1"))
//                .andExpect(status().isOk());
//    }
//
//    // 🔴 Test API DELETE /events/{id} khi không tìm thấy sự kiện
//    @Test
//    public void givenEventNotFound_whenDeleteEvent_thenReturnNotFound() throws Exception {
//        doThrow(new RuntimeException("Event not found"))
//                .when(eventService).deleteEvent(anyLong());
//
//        mockMvc.perform(delete("/events/99"))
//                .andExpect(status().isInternalServerError())
//                .andExpect(jsonPath("$.message").value("Event not found"));
//    }
//}
