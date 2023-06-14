package model.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Event;
import model.ModelException;
import model.User;

public class MySQLEventDAO implements EventDAO {

	@Override
	public boolean save(Event event) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlInsert = "INSERT INTO events VALUES (DEFAULT, ?, ?, ?, ?, ?, ?);";
		
		db.prepareStatement(sqlInsert);
		
		db.setString(1, event.getEventName());
		db.setDate(2, event.getDateStart() == null ? new Date() : event.getDateStart());
			
		if (event.getDateEnd() == null)
			db.setNullDate(3);
		else db.setDate(3, event.getDateEnd());
		
		db.setString(4, event.getLocation());
		db.setString(5, event.getDescription());
		db.setInt(6, event.getUser().getId());
		
		return db.executeUpdate() > 0;	
	}

	@Override
	public boolean update(Event event) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlUpdate = "UPDATE events "
				+ " SET event_name = ?, "
				+ " date_start = ?, "
				+ " date_end = ?, "
				+ " location = ?, "
				+ " description = ?, "
				+ " user_id = ? "
				+ " WHERE id = ?; "; 
		
		db.prepareStatement(sqlUpdate);
		
		db.setString(1, event.getEventName());
		
		db.setDate(2, event.getDateStart() == null ? new Date() : event.getDateStart());
		
		if (event.getDateEnd() == null)
			db.setNullDate(3);
		else db.setDate(3, event.getDateEnd());
		
		db.setString(4, event.getLocation());
		db.setString(5, event.getDescription());
		db.setInt(6, event.getUser().getId());
		db.setInt(7, event.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean delete(Event event) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlDelete = " DELETE FROM events "
		         + " WHERE id = ?;";

		db.prepareStatement(sqlDelete);		
		db.setInt(1, event.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public List<Event> listAll() throws ModelException {
		DBHandler db = new DBHandler();
		
		List<Event> events = new ArrayList<Event>();
			
		// Declara uma instrução SQL
		String sqlQuery = " SELECT e.id as 'event_id', e.*, u.* \n"
				+ " FROM events e \n"
				+ " INNER JOIN users u \n"
				+ " ON e.user_id = u.id;";
		
		db.createStatement();
	
		db.executeQuery(sqlQuery);

		while (db.next()) {
			User user = new User(db.getInt("user_id"));
			user.setName(db.getString("nome"));
			user.setGender(db.getString("sexo"));
			user.setEmail(db.getString("email"));
			
			Event event = new Event(db.getInt("event_id"));
			event.setEventName(db.getString("event_name"));
			event.setDateStart(db.getDate("date_start"));
			event.setDateEnd(db.getDate("date_end"));
			event.setLocation(db.getString("location"));
			event.setDescription(db.getString("description"));
			event.setUser(user);
			
			events.add(event);
		}
		
		return events;
	}

	@Override
	public Event findById(int id) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sql = "SELECT * FROM events WHERE id = ?;";
		
		db.prepareStatement(sql);
		db.setInt(1, id);
		db.executeQuery();
		
		Event e = null;
		while (db.next()) {
			e = new Event(id);
			e.setEventName(db.getString("event_name"));
			e.setDateStart(db.getDate("date_start"));
			e.setDateEnd(db.getDate("date_end"));
			e.setLocation(db.getString("location"));
			e.setDescription(db.getString("description"));
			
			UserDAO userDAO = DAOFactory.createDAO(UserDAO.class); 
			User user = userDAO.findById(db.getInt("user_id"));
			e.setUser(user);
			
			break;
		}
		
		return e;
	}
}
