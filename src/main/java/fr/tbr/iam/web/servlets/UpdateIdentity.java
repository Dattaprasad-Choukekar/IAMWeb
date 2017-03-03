package fr.tbr.iam.web.servlets;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
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
 * Servlet implementation for updating identity
 */
@WebServlet("/updateIdentity")
public class UpdateIdentity extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	private final Logger logger = Logger.getLogger(UpdateIdentity.class
			.getName());
	@Autowired
	private IdentityDAO identityDAO;

	/**
	 * Default constructor.
	 */
	public UpdateIdentity() {
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
				config.getServletContext());
	}

	/**
	 * Dispalys form for updating identity
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String idStr = request.getParameter("id");
		int id = -1;
		boolean isValidId = false;

		if (idStr != null && !idStr.isEmpty()) {
			try {
				id = Integer.parseInt(idStr);
			} catch (NumberFormatException ex) {
				logger.warning("Invalid id : " + ex);
			}
			isValidId = true;
		}
		if (isValidId) {
			try {
				Identity identity = identityDAO.get(id);
				if (identity == null) {
					isValidId = false;
					logger.log(Level.SEVERE,
							"Identity with id not found. Id : " + id);
				}
				request.setAttribute("identity", identity);
			} catch (IMCoreException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
				isValidId = false;
			}
		}

		if (isValidId) {
			TilesContainer container = TilesAccess.getContainer(request
					.getSession().getServletContext());
			container.render("updateIdentity", request, response);
		} else {
			response.sendError(500);
		}

	}

	/**
	 * Updates identity in persistent storage and redirect user to list page.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String idStr = request.getParameter("identityId");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String birthDateStr = request.getParameter("birthDate");
		Identity identity = new Identity(firstName, lastName, email);
		identity.setId(Integer.parseInt(idStr));

		// Parse bith date
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

		for (String attribute : (ArrayList<String>) request.getServletContext()
				.getAttribute("attributeNamesList")) {

			String attaibuteValue = request.getParameter(attribute);
			if (attaibuteValue != null && !attaibuteValue.isEmpty()) {
				identity.getAttributes().put(attribute, attaibuteValue);
			}
		}

		boolean isUpdated = false;
		try {
			isUpdated = identityDAO.update(identity);
		} catch (IMCoreException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		if (!isUpdated) {
			response.sendError(500);
			return;
		}
		request.setAttribute("resultId", Integer.parseInt(idStr));
		response.sendRedirect(request.getContextPath() + "/listIdentities");
		/*
		 * RequestDispatcher rd =
		 * request.getServletContext().getRequestDispatcher("/listIdentities");
		 * rd.forward(request, response);
		 */
	}
}
