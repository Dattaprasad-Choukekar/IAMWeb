package fr.tbr.iam.web.servlets;

import java.io.IOException;
import java.util.ArrayList;
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
 * Servlet for listing identities
 * @author Dattaprasad
 *
 */
@WebServlet("/listIdentities")
public class ListIdentities extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = Logger.getLogger(ListIdentities.class
			.getName());
	
	@Autowired
	private IdentityDAO identityDAO;
	
	public ListIdentities() {

	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
				config.getServletContext());
	}

	/**
	 * Lists all identities.
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			IdentityDAO identityDAO = (IdentityDAO) request.getServletContext()
					.getAttribute(ContextListener.DAO);
			List<Identity> identityList = identityDAO.getAllEntities();
			request.setAttribute("identityList", identityList);
			request.setAttribute("attributes", (ArrayList<String>) request.getServletContext().getAttribute("attributeNamesList"));
		} catch (IMCoreException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			response.sendError(500);
		}

		TilesContainer container = TilesAccess.getContainer(request
				.getSession().getServletContext());
		container.render("listIdentities", request, response);

	}
	
	/**
	 * Lists all identities.
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			List<Identity> identityList = identityDAO.getAllEntities();
			request.setAttribute("identityList", identityList);
		} catch (IMCoreException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			response.sendError(500);
		}

		TilesContainer container = TilesAccess.getContainer(request
				.getSession().getServletContext());
		container.render("listIdentities", request, response);

	}
}
