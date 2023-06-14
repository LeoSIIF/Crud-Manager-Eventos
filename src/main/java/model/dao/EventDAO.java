package model.dao;

import java.util.List;

import model.ModelException;
import model.Event;

public interface EventDAO {
	boolean save(Event event) throws ModelException;
	boolean update(Event event) throws ModelException;
	boolean delete(Event event) throws ModelException;
	List<Event> listAll() throws ModelException;
	Event findById(int id) throws ModelException;
}