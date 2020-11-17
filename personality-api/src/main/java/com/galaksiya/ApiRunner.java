package com.galaksiya;

import com.galaksiya.db.DatabaseConnector;
import com.galaksiya.logger.GLogger;
import com.galaksiya.utils.RestUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;

public class ApiRunner {

	/**
	 * Logger instance.
	 */
	private static final GLogger logger = new GLogger(ApiRunner.class);

	public static void main(String[] args) {
		try {
			DatabaseConnector.getInstance();
			Server server = RestUtils.createServer();
			WebAppContext root = RestUtils.createWebAppContext();
			server.setHandler(root);
			ServerConnector connector = RestUtils.createConnector(server);
			server.start();
			logger.info("started on port: %s", connector.getPort());
			server.join();
		} catch (Exception e) {
			logger.fatal("unexpected error occurred during startup", e);
		}
	}

}
