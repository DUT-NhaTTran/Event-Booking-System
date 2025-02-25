package com.tmnhat.Event.Service.service.Impl;

import com.tmnhat.Event.Service.cache.EventCacheService;
import com.tmnhat.Event.Service.config.TestDatabaseConfig;
import com.tmnhat.Event.Service.model.Event;
import com.tmnhat.Event.Service.repository.EventDAO;
import com.tmnhat.common.exception.DatabaseException;
import com.tmnhat.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Import(TestDatabaseConfig.class) // Chỉ sử dụng H2 database trong test
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EventServiceImplTest {

    @Autowired
    private EventDAO eventDAO;

    @Autowired
    private EventCacheService eventCacheService;

    @Autowired
    private EventServiceImpl eventService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    @Transactional
    public void clearDatabase() {
        System.out.println("🔹 Clearing H2 database before each test...");
        jdbcTemplate.execute("DELETE FROM events"); // Xóa dữ liệu
        jdbcTemplate.execute("ALTER TABLE events ALTER COLUMN id RESTART WITH 1"); // Reset ID
    }

    private Event createEvent(boolean isHotEvent) {
        return new Event.Builder()
                .eventName("Test Event")
                .description("description")
                .location("location")
                .date(LocalDateTime.now())
                .availableTickets(100)
                .ticketPrice(99.9)
                .isHotEvent(isHotEvent)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    public void givenHotEvent_whenAddEvent_thenEventIsPersistedAndCached() {
        // Given
        Event event = createEvent(true);

        // When
        eventService.addEvent(event);

        // Then
        assertNotNull(event.getId(), "Event ID should be assigned after saving");

        // Kiểm tra rằng event đã được lưu vào database H2
        Event persistedEvent = eventDAO.getEventById(event.getId());
        assertNotNull(persistedEvent, "Persisted event should not be null");
        assertEquals("Test Event", persistedEvent.getEventName(), "Event name should match");

        // Kiểm tra nếu là hot event, nó sẽ được cache
        Optional<Event> cachedEvent = eventCacheService.getHotEvent(event.getId());
        assertTrue(cachedEvent.isPresent());
    }

    @Test
    public void givenNotHotEvent_whenAddEvent_thenEventIsPersistedWithoutCaching() {
        // Given
        Event event = createEvent(false);

        // When
        eventService.addEvent(event);

        // Then
        assertNotNull(event.getId());
        assertFalse(eventCacheService.getHotEvent(event.getId()).isPresent());
    }

    @Test
    public void givenAvailableEvents_whenGetAllEvents_thenReturnCompleteEventList() {
        // Given - Đếm số lượng sự kiện trước khi thêm mới
        int initialCount = eventService.getAllEvents().size();

        eventService.addEvent(createEvent(true));
        eventService.addEvent(createEvent(false));

        // When - Lấy số lượng sự kiện sau khi thêm
        List<Event> result = eventService.getAllEvents();

        // Debug: In ra danh sách sự kiện để kiểm tra
        System.out.println("Total events before: " + initialCount);
        System.out.println("Total events after adding: " + result.size());

        for (Event e : result) {
            System.out.println("Event ID: " + e.getId() + ", Name: " + e.getEventName());
        }

        // Then - Kiểm tra tổng số lượng sự kiện sau khi thêm
        assertEquals(initialCount + 2, result.size());
    }

    @Test
    public void givenEventExists_whenGetEventById_thenReturnEvent() {
        // Given
        Event event = createEvent(false);
        eventService.addEvent(event);

        // When
        Event result = eventService.getEventById(event.getId());

        // Then
        assertNotNull(result);
        assertEquals(event.getId(), result.getId());
    }

    @Test
    public void givenEventNotExists_whenGetEventById_thenThrowResourceNotFoundException() {
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> eventService.getEventById(999L));
    }

    @Test
    public void givenHotEvent_whenUpdateEvent_thenPersistAndCacheUpdatedEvent() {
        // Given
        Event event = createEvent(true);
        eventService.addEvent(event);

        Event updatedEvent = createEvent(true);
        updatedEvent.setEventName("Updated Name");

        // When
        eventService.updateEvent(event.getId(), updatedEvent);

        // Then
        Event persistedEvent = eventDAO.getEventById(event.getId());
        assertEquals("Updated Name", persistedEvent.getEventName());
        assertTrue(eventCacheService.getHotEvent(event.getId()).isPresent());
    }

    @Test
    public void givenNotHotEvent_whenUpdateEvent_thenPersistWithoutCaching() {
        // Given
        Event event = createEvent(false);
        eventService.addEvent(event);

        Event updatedEvent = createEvent(false);
        updatedEvent.setEventName("Updated Name");

        // When
        eventService.updateEvent(event.getId(), updatedEvent);

        // Then
        Event persistedEvent = eventDAO.getEventById(event.getId());
        assertEquals("Updated Name", persistedEvent.getEventName());
        assertFalse(eventCacheService.getHotEvent(event.getId()).isPresent());
    }

    @Test
    public void givenEventNotFound_whenUpdateEvent_thenThrowResourceNotFoundException() {
        // Given
        Event updatedEvent = createEvent(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> eventService.updateEvent(999L, updatedEvent));
    }

    @Test
    public void givenCachedEvent_whenDeleteEvent_thenRemoveFromCacheAndDB() {
        // Given
        Event event = createEvent(true);
        eventService.addEvent(event);

        // When
        eventService.deleteEvent(event.getId());

        // Then
        assertThrows(ResourceNotFoundException.class, () -> eventService.getEventById(event.getId()));
        assertFalse(eventCacheService.getHotEvent(event.getId()).isPresent());
    }

    @Test
    public void givenNotCachedEvent_whenDeleteEvent_thenRemoveOnlyFromDB() {
        // Given
        Event event = createEvent(false);
        eventService.addEvent(event);

        // When
        eventService.deleteEvent(event.getId());

        // Then
        assertThrows(ResourceNotFoundException.class, () -> eventService.getEventById(event.getId()));
    }
}
