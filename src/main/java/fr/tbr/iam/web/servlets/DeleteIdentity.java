package fr.tbr.iam.web.servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fr.tbr.iam.web.context.ContextListener;
import fr.tbr.iamcore.datamodel.Identity;
import fr.tbr.iamcore.services.IdentityDAO;
import fr.tbr.iamcore.services.exception.IMCoreException;

/**
 * Servlet for deleting identity
 */
@WebServlet("/deleteIdentity")
public class DeleteIdentity extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Logger logger = Logger.getLogger(DeleteIdentity.class
			.getName());

	@Autowired
	private IdentityDAO identityDAO;

	/**
	 * Default constructor.
	 */
	public DeleteIdentity() {
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
				config.getServletContext());
	}

	/**
	 * Deletes the identity with id equal to param 'id' from persistant storage
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
				} else {
					if (!identityDAO.delete(identity)) {
						isValidId = false;
					}
				}
			} catch (IMCoreException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
				isValidId = false;
			}
		}
		if (!isValidId) {
			response.sendError(500);
		} else {
			response.sendRedirect(request.getContextPath() + "/listIdentities");
		}
	}
}
