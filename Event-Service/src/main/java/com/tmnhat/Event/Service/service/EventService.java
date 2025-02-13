package com.tmnhat.Event.Service.service;

import com.tmnhat.Event.Service.model.Event;
import com.tmnhat.Event.Service.reponsitory.EventDAO;

import java.sql.SQLException;
import java.util.List;

public class EventService {
    private final EventDAO eventDAO=new EventDAO();
    public void addEvent(Event event) throws SQLException {
        eventDAO.addEvent(event);
    }
    public List<Event> getAllEvents() throws SQLException{
        return eventDAO.getAllEvents();
    }
    public Event getEventById(Long id) throws SQLException {
        return eventDAO.getEventById(id);
    }
    public void updateEvent(Long id,Event event) throws SQLException {
        eventDAO.updateEvent(id,event);
    }
    public void deleteEvent(Long id) throws SQLException {
        eventDAO.deleteEvent(id);
    }
}
