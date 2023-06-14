package controller;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Company;
import model.Event;
import model.ModelException;
import model.User;
import model.dao.EventDAO;
import model.dao.UserDAO;
import model.dao.DAOFactory;

@WebServlet(urlPatterns = {"/events", "/event/form", "/event/update", "/event/insert", "/event/delete"})
public class EventsController extends HttpServlet{
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getRequestURI();

        switch (action) {
            case "/crud-manager/event/form": {
                CommonsController.listUsers(req);
                req.setAttribute("action", "insert");
                ControllerUtil.forward(req, resp, "/form-event.jsp");
                break;
                }
            case "/crud-manager/event/update": {
            	
                String idStr = req.getParameter("eventId");
                int idEvent = Integer.parseInt(idStr);
                
                EventDAO dao = DAOFactory.createDAO(EventDAO.class);
                
                Event event = null;
                try {
                    event = dao.findById(idEvent);
                } catch (ModelException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                CommonsController.listUsers(req);
                req.setAttribute("action", "update");
                req.setAttribute("event", event);
                ControllerUtil.forward(req, resp, "/form-event.jsp");
                break;
            }
            
            default:
                
                listEvents(req);
                
                ControllerUtil.transferSessionMessagesToRequest(req);
                
                ControllerUtil.forward(req, resp, "/events.jsp");
        }
    }
	
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getRequestURI();

        switch (action) {
            case "/crud-manager/event/insert":
                insertEvent(req, resp);
                break;
            case "/crud-manager/event/delete":
                deleteEvent(req, resp);
                break;
            case "/crud-manager/event/update":
                updateEvent(req, resp);
                break;
            default:
                System.out.println("URL inválida " + action);
        }

        ControllerUtil.redirect(resp, req.getContextPath() + "/events");
    }
	
	private void updateEvent(HttpServletRequest req, HttpServletResponse resp) {
		String eventIdStr = req.getParameter("eventId");
		String eventName = req.getParameter("event_name");
		String dateStart = req.getParameter("date_start");
		String dateEnd = req.getParameter("date_end");
		String location = req.getParameter("location");
		String description = req.getParameter("description");
		Integer userId = Integer.parseInt(req.getParameter("user_id"));
		
		Event event = new Event(Integer.parseInt(eventIdStr));
		event.setEventName(eventName);
		event.setDateStart(ControllerUtil.formatDate(dateStart));
		event.setDateEnd(ControllerUtil.formatDate(dateEnd));
		event.setLocation(location);
		event.setDescription(description);
		event.setUser(new User(userId));
		
		EventDAO dao = DAOFactory.createDAO(EventDAO.class);
		
		try {	
			
			if (dao.update(event)) {
				ControllerUtil.sucessMessage(req, "Evento '" + event.getEventName() + "' atualizado com sucesso.");
			}
			else {
				ControllerUtil.errorMessage(req, "Evento '" + event.getEventName() + "' não pode ser atualizado.");
			}
				
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	
	private void listEvents(HttpServletRequest req) {
		EventDAO dao = DAOFactory.createDAO(EventDAO.class);
		
		List<Event> events = null;
		try {
			events = dao.listAll();
		} catch (ModelException e) {
			// Log no servidor
			e.printStackTrace();
		}
		
		if (events != null)
			req.setAttribute("events", events);
	}

	private void deleteEvent(HttpServletRequest req, HttpServletResponse resp) {
		String eventIdParameter = req.getParameter("id");
		
		int eventId = Integer.parseInt(eventIdParameter);
		
		EventDAO dao = DAOFactory.createDAO(EventDAO.class);
		
		try {
			Event event = dao.findById(eventId);
			
			if (event == null)
				throw new ModelException("Evento não encontrado para deleção.");
			
			if (dao.delete(event)) {
				ControllerUtil.sucessMessage(req, "Evento '" + 
						event.getEventName() + "' deletado com sucesso.");
			}
			else {
				ControllerUtil.errorMessage(req, "Evento '" + 
						event.getEventName() + "' não pode ser deletado. "
								+ "Há dados relacionados ao Evento.");
			}
		} catch (ModelException e) {
			// log no servidor
			if (e.getCause() instanceof 
					SQLIntegrityConstraintViolationException) {
				ControllerUtil.errorMessage(req, e.getMessage());
			}
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void insertEvent(HttpServletRequest req, HttpServletResponse resp) {
		String eventName = req.getParameter("event_name");
		String dateStart = req.getParameter("date_start");
		String dateEnd = req.getParameter("date_end");
		String location = req.getParameter("location");
		String description = req.getParameter("description");
		Integer userId = Integer.parseInt(req.getParameter("user_id"));
		
		Event event = new Event();
		event.setEventName(eventName);
		event.setDateStart(ControllerUtil.formatDate(dateStart));
		event.setDateEnd(ControllerUtil.formatDate(dateEnd));
		event.setLocation(location);
		event.setDescription(description);
		event.setUser(new User(userId));
		
		EventDAO dao = DAOFactory.createDAO(EventDAO.class);
	
		try {
			if (dao.save(event)) {
				ControllerUtil.sucessMessage(req, "Evento '" + event.getEventName() 
				+ "' salvo com sucesso.");
			}
			else {
				ControllerUtil.errorMessage(req, "Evento '" + event.getEventName()
				+ "' não pode ser salvo.");
			}
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}
	
}
