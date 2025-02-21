package com.tmnhat.Event.Service.service;

import com.tmnhat.Event.Service.model.Event;
import com.tmnhat.Event.Service.repository.EventDAO;

import java.sql.SQLException;
import java.util.List;

public interface EventService {
    void addEvent(Event event);
    List<Event> getAllEvents();
    Event getEventById(Long id);
    void updateEvent(Long id, Event event);
    void deleteEvent(Long id);
    List<Event> getAllHotEvents();
}
