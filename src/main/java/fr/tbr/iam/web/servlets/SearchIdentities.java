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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fr.tbr.iam.web.context.ContextListener;
import fr.tbr.iamcore.datamodel.Identity;
import fr.tbr.iamcore.services.IdentityDAO;
import fr.tbr.iamcore.services.exception.IMCoreException;
/**
 * This servlet searches identities 
 * @author Dattaprasad
 *
 */
@WebServlet("/searchIdentities")
public class SearchIdentities extends HttpServlet {
	private final Logger logger = Logger.getLogger(SearchIdentities.class
			.getName());
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	private IdentityDAO identityDAO;
	
	public SearchIdentities() {
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
				config.getServletContext());
	}
	
	/**
	 * Displays search page.
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		TilesContainer container = TilesAccess.getContainer(request
				.getSession().getServletContext());
		container.render("searchIdentities", request, response);
	}

	/**
	 * Performs search and displays search result
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String birthDateStr = request.getParameter("birthDate");
		Identity identity = new Identity(firstName, lastName, email);

		// Parse birth date
		if (birthDateStr != null && !birthDateStr.isEmpty()) {
			Date birthDate = null;
			try {
				birthDate = dateFormatter.parse(birthDateStr);
			} catch (ParseException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
				response.sendError(500);
				return;
			}
			identity.setBirthDate(birthDate);
		}
		for (String attribute : (ArrayList<String>) request.getServletContext().getAttribute("attributeNamesList")) {
			
			String attaibuteValue = request.getParameter(attribute);
			if (attaibuteValue!= null && !attaibuteValue.isEmpty()) {
				identity.getAttributes().put(attribute, attaibuteValue);
			}
		}
		
		try {
			List<Identity> identityList = identityDAO.search(identity);
			request.setAttribute("identityList", identityList);
			request.setAttribute("attributes", (ArrayList<String>) request.getServletContext().getAttribute("attributeNamesList"));
		} catch (IMCoreException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			response.sendError(500);
		}
		TilesContainer container = TilesAccess.getContainer(request
				.getSession().getServletContext());
		container.render("searchIdentitiesResult", request, response);
	}
}
