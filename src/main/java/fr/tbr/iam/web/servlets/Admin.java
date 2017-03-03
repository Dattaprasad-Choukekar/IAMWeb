package fr.tbr.iam.web.servlets;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fr.tbr.iam.web.context.ContextListener;
import fr.tbr.iam.web.model.Attribute;
import fr.tbr.iam.web.model.Attribute.AttributeType;
import fr.tbr.iamcore.datamodel.Identity;
import fr.tbr.iamcore.services.IdentityDAO;
import fr.tbr.iamcore.services.exception.IMCoreException;

/**
 * Servlet implementation for Creating identity
 */
@WebServlet("/admin")
public class Admin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	private final Logger logger = Logger.getLogger(Admin.class
			.getName());

	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * Default constructor.
	 */
	public Admin() {
		
		
		
	}
	
	public void init(ServletConfig config) throws ServletException {
			super.init(config);
	    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
	      config.getServletContext());
	  }

	/**
	 * Presents create identity form to user.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		Session session = sessionFactory.openSession();
		List<Attribute> list = session.createCriteria(Attribute.class).setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).list();
		session.close();
		
		Attribute atrb = new Attribute();
		atrb.setId(1);
		atrb.setName("City");
		atrb.setType(AttributeType.TEXT);
		list.add(atrb);
		atrb = new Attribute();
		atrb.setId(2);
		atrb.setName("Age");
		atrb.setType(AttributeType.TEXT);
		list.add(atrb);
		
		request.setAttribute("attributeList", list);
		TilesContainer container = TilesAccess.getContainer(request
				.getSession().getServletContext());
		container.render("admin", request, response);
	}

	/**
	 * Saves identity to persistent storage and redirects to list identity.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String birthDateStr = request.getParameter("birthDate");
		Identity identity = new Identity(firstName, lastName, email);
		Date date = null;
		if (birthDateStr != null && !birthDateStr.isEmpty()) {
			try {
				date = dateFormatter.parse(birthDateStr);
			} catch (ParseException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
				response.sendError(500);
				return;
			}
			identity.setBirthDate(date);
		}
		
		for (String attribute : (ArrayList<String>) request.getServletContext().getAttribute("attributeNamesList")) {
			
			String attaibuteValue = request.getParameter(attribute);
			if (attaibuteValue!= null && !attaibuteValue.isEmpty()) {
				identity.getAttributes().put(attribute, attaibuteValue);
			}
		}
		

		IdentityDAO identityDAO = (IdentityDAO) request.getServletContext()
				.getAttribute(ContextListener.DAO);
		try {
			identityDAO.save(identity);
		} catch (IMCoreException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			response.sendError(500);
			return;
		}
		response.sendRedirect(request.getContextPath() + "/listIdentities");
	}

}
