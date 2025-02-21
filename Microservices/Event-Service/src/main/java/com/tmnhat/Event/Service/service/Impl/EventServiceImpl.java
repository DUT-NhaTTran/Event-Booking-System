package com.tmnhat.Event.Service.service.Impl;

import com.tmnhat.Event.Service.cache.EventCacheService;
import com.tmnhat.Event.Service.model.Event;
import com.tmnhat.Event.Service.repository.EventDAO;
import com.tmnhat.Event.Service.service.EventService;
import com.tmnhat.common.exception.DatabaseException;
import com.tmnhat.common.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventServiceImpl implements EventService {
    private final EventDAO eventDAO=new EventDAO();
    private final EventCacheService eventCacheService= new EventCacheService();

    @Override
    public void addEvent(Event event) {
        try {
            Long eventId = eventDAO.addEventAndReturnId(event); // Thêm sự kiện vào DB
            event.setId(eventId); // Gán ID sau khi lưu

            // Nếu là hot event, cache vào Redis
            if (event.getIsHotEvent()) {
                eventCacheService.cacheHotEvent(event);
            }
        } catch (Exception e) {
            throw new DatabaseException("Error adding event: " + e.getMessage());
        }
    }

    @Override
    public List<Event> getAllEvents() {
        try {
            List<Event> events = new ArrayList<>();
            events.addAll(eventCacheService.getAllHotEvents()); // Lấy sự kiện hot từ cache
            events.addAll(eventDAO.getAllEvents()); // Lấy từ DB
            return events;
        } catch (Exception e) {
            throw new DatabaseException("Error retrieving events: " + e.getMessage());
        }
    }

    @Override
    public Event getEventById(Long id) {

            Optional<Event> cachedEvent = eventCacheService.getHotEvent(id);
            if (cachedEvent.isPresent()) {
                return cachedEvent.get();
            }

            Event event = eventDAO.getEventById(id);
            if (event == null) {
                throw new ResourceNotFoundException("Event with ID " + id + " not found");
            }

            return event;

    }

    @Override
    public void updateEvent(Long id, Event event) {
        try {
            Event existingEvent = getEventById(id);
            if (existingEvent == null) {
                throw new ResourceNotFoundException("Event with ID " + id + " not found");
            }

            event.setId(id);

            if (event.getIsHotEvent()) {
                eventCacheService.cacheHotEvent(event);
            }
            eventDAO.updateEvent(id, event);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Event with ID " + id + " not found");
        } catch (Exception e) {
            throw new DatabaseException("Error updating event with ID " + id + ": " + e.getMessage());
        }
    }

    @Override
    public void deleteEvent(Long id) {
        try {
            Optional<Event> cachedEvent = eventCacheService.getHotEvent(id);
            if (cachedEvent.isPresent()) {
                eventCacheService.removeHotEvent(id);
            } else {
                Event existingEvent = getEventById(id);
                eventDAO.deleteEvent(id);
            }
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Event with ID " + id + " not found");
        } catch (Exception e) {
            throw new DatabaseException("Error deleting event with ID " + id + ": " + e.getMessage());
        }
    }

    @Override
    public List<Event> getAllHotEvents() {
        return eventCacheService.getAllHotEvents();
    }
}
