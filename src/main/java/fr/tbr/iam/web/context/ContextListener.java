package fr.tbr.iam.web.context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fr.tbr.iam.web.model.Attribute;
import fr.tbr.iam.web.model.Attributes;
import fr.tbr.iamcore.datamodel.Identity;
import fr.tbr.iamcore.services.IdentityDAO;
import fr.tbr.iamcore.services.exception.IMCoreException;
import fr.tbr.iamcore.services.impl.dbstore.IdentityDaoDbStoreImpl;
import fr.tbr.iamcore.services.impl.filestore.IdentityDAOFileStoreImpl;

/**
 * This context listener performs initialization tasks, when the application is
 * started.
 * 
 * @author Dattaprasad
 *
 */
@WebListener
public class ContextListener implements ServletContextListener {
	private final Logger logger = Logger.getLogger(ContextListener.class
			.getName());

	public static final String DAO = "DAO";
	public static final String DB_CONFIG_PATH = "dbConfigPath";

	@Autowired
	private IdentityDAO identityDAO;

	@Autowired
	private SessionFactory sessionFactory;

	public void contextDestroyed(ServletContextEvent arg0) {

	}

	/**
	 * Initializes DAO implementation from Spring container. If
	 * loadIdentitiesFromTxtFile variable is specified in web.xml, it loads the
	 * identities from specified file.
	 * 
	 */
	public void contextInitialized(ServletContextEvent contextEvent) {

		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
				contextEvent.getServletContext());

		ServletContext context = contextEvent.getServletContext();
		try {
			String dropAndCreateTable = context
					.getInitParameter("dropAndCreateTable");

			if (dropAndCreateTable != null
					&& identityDAO instanceof IdentityDaoDbStoreImpl
					&& (dropAndCreateTable.startsWith("y") || dropAndCreateTable
							.startsWith("Y"))) {
				// TODO

			}

			// if loadIdentitiesFromTxtFile variable is specified in web.xml
			// then load identities from specified file
			String loadIdentitiesFromTxtFile = context
					.getInitParameter("loadIdentitiesFromTxtFile");
			if (loadIdentitiesFromTxtFile != null) {
				String loadIdentitiesFromTxtFilePath = context
						.getRealPath(loadIdentitiesFromTxtFile);
				IdentityDAO identityDAOTemp = new IdentityDAOFileStoreImpl(
						loadIdentitiesFromTxtFilePath);
				for (Identity identity : identityDAOTemp.getAllEntities()) {
					identityDAO.save(identity);
				}
			}

			initializeIdentityAttributes(contextEvent);

		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error while initialting application context.", e);
			throw new RuntimeException(
					"Error while initialting  application context.", e);
		}
		context.setAttribute(DAO, identityDAO);
	}

	private void initializeIdentityAttributes(ServletContextEvent contextEvent) {
		List<Attribute> attributeList = readAttributeFile();
		StringBuffer attributeTemplate = new StringBuffer();
		StringBuffer attributeTemplateForSearch = new StringBuffer();
		ArrayList<String> attributeNames = new ArrayList<String>();
		if (attributeList != null && !attributeList.isEmpty()) {
			InputStream configStream = contextEvent.getServletContext()
					.getResourceAsStream("/WEB-INF/template.jsp");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					configStream));
			StringBuffer fileData = new StringBuffer();
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					fileData.append(line);
				}

			} catch (IOException e) {
				throw new RuntimeException(
						"Error while reading attribute template.", e);
			}

			for (Attribute atrb : attributeList) {
				String result = fileData.toString().replaceAll("token_type",
						atrb.getType().toString());
				result = result.replaceAll("token", atrb.getName());
				attributeTemplateForSearch.append(result.replaceAll(
						"isTokenMandatory", ""));
				if (atrb.getIsMandatory()) {
					result = result.replaceAll("isTokenMandatory", "required");
				} else {
					result = result.replaceAll("isTokenMandatory", "");
				}

				attributeTemplate.append(result);

				attributeNames.add(atrb.getName());
			}
			contextEvent.getServletContext().setAttribute("attributeList",
					attributeList);
			contextEvent.getServletContext().setAttribute("attributeNamesList",
					attributeNames);

		}

		String path = contextEvent.getServletContext().getRealPath(
				"WEB-INF/jsp/attributetemplate.jsp");
		try {
			FileWriter fw = new FileWriter(path);
			fw.write(attributeTemplate.toString());
			fw.flush();
			fw.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while wrtting attribute file.", e);
			throw new RuntimeException("Error while wrtting attribute file.", e);
		}

		path = contextEvent.getServletContext().getRealPath(
				"WEB-INF/jsp/attributetemplate_for_search.jsp");
		try {
			FileWriter fw = new FileWriter(path);
			fw.write(attributeTemplateForSearch.toString());
			fw.flush();
			fw.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while wrtting attribute file.", e);
			throw new RuntimeException("Error while wrtting attribute file.", e);
		}

		updateAttributeForPersistedIdentities(attributeNames);
	}

	public List<Attribute> readAttributeFile() {
		List<Attribute> attributeList = new ArrayList<Attribute>();

		String file = this.getClass().getClassLoader()
				.getResource("attributes-configuration.xml").getFile();
		JAXBContext jaxbContext = null;
		try {
			this.getClass().getClassLoader()
					.getResourceAsStream("SomeTextFile.txt");
			jaxbContext = JAXBContext.newInstance(Attributes.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Attributes attributes = (Attributes) jaxbUnmarshaller
					.unmarshal(new File(file));

			attributeList.addAll(attributes.getAttributes());

		} catch (JAXBException e) {
			logger.log(Level.SEVERE, "Error while wrtting attribute file.", e);
			throw new RuntimeException("Error while wrtting attribute file.", e);
		}
		return attributeList;
	}

	private void updateAttributeForPersistedIdentities(
			List<String> newAttributeNames) {

		try {
			for (Identity identity : identityDAO.getAllEntities()) {
				if (identity.getAttributes() != null
						&& !identity.getAttributes().isEmpty()) {

					Map<String, String> attributeMap = new HashMap<String, String>(
							identity.getAttributes());
					Iterator<String> it = attributeMap.keySet().iterator();
					while (it.hasNext()) {
						String attribute = it.next();
						if (!newAttributeNames.contains(attribute)) {
							identity.getAttributes().remove(attribute);
						}
					}
				}
				if (!identityDAO.update(identity)) {
					throw new RuntimeException(
							"Error while updating attribute in Identity.");
				}
			}
		} catch (IMCoreException e) {
			throw new RuntimeException(
					"Error while updating attribute in Identity.", e);
		}

	}
}
