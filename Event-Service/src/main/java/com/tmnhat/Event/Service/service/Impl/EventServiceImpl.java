package com.tmnhat.Event.Service.service.Impl;

import com.tmnhat.Event.Service.model.Event;
import com.tmnhat.Event.Service.repository.EventDAO;
import com.tmnhat.Event.Service.service.EventService;

import java.sql.SQLException;
import java.util.List;

public class EventServiceImpl implements EventService {
    private final EventDAO eventDAO;

    public EventServiceImpl() {
        this.eventDAO = new EventDAO(); // Tạo instance của DAO
    }

    @Override
    public void addEvent(Event event) throws SQLException {
        eventDAO.addEvent(event);
    }

    @Override
    public List<Event> getAllEvents() throws SQLException {
        return eventDAO.getAllEvents();
    }

    @Override
    public Event getEventById(Long id) throws SQLException {
        return eventDAO.getEventById(id);
    }

    @Override
    public void updateEvent(Long id, Event event) throws SQLException {
        eventDAO.updateEvent(id, event);
    }

    @Override
    public void deleteEvent(Long id) throws SQLException {
        eventDAO.deleteEvent(id);
    }
}
