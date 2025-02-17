package com.tmnhat.Event.Service.service;

import com.tmnhat.Event.Service.model.Event;
import com.tmnhat.Event.Service.repository.EventDAO;

import java.sql.SQLException;
import java.util.List;

public interface EventService {
    void addEvent(Event event) throws SQLException;
    List<Event> getAllEvents() throws SQLException;
    Event getEventById(Long id) throws SQLException;
    void updateEvent(Long id, Event event) throws SQLException;
    void deleteEvent(Long id) throws SQLException;
}
