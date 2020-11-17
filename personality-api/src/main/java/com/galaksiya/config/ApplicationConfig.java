package com.galaksiya.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ApplicationConfig {

	private static final String WEB = "com.galaksiya.web";
	private static final String PORT = "port";
	private static final String HOST = "host";
	private static final String MIN_THREADS = "minThreads";
	private static final String MAX_THREADS = "maxThreads";
	private static final String IDLE_TIMEOUT = "idleTimeout";
	private static final String CORS_ENABLED = "corsEnabled";
	private static final String CORS_ORIGIN_URL = "corsOriginUrl";
	private static final String HIBERNATE = "com.galaksiya.hibernate";
	public static final String SCHEMA = "schema";

	/**
	 * Singleton instance of this class.
	 */
	private static ApplicationConfig instance;

	/**
	 * {@link Config} instance for reading configuration file.
	 */
	private Config config;

	/**
	 * Default private constructor for creating singleton instance.
	 */
	private ApplicationConfig() {
		config = ConfigFactory.load();
	}

	/**
	 * Getter for the singleton instance of this class.
	 */
	public static ApplicationConfig getInstance() {
		if (instance == null) {
			instance = new ApplicationConfig();
		}
		return instance;
	}

	/**
	 * Finds the <i>com.galaksiya.web.baseUrl</i> configuration. If not found, set default "*".
	 *
	 * @return the found baseUrl configuration as boolean.
	 */
	public String getCorsOriginUrl() {
		String url = "*";
		Config config = getConfig(WEB);
		if (config.hasPath(CORS_ORIGIN_URL)) {
			url = config.getString(CORS_ORIGIN_URL);
		}
		return url;
	}

	public int getIdleTimeout() {
		return getConfig(WEB).getInt(IDLE_TIMEOUT);
	}

	public int getMaxThreadCount() {
		return getConfig(WEB).getInt(MAX_THREADS);
	}

	public int getMinThreadCount() {
		return getConfig(WEB).getInt(MIN_THREADS);
	}

	public String getHibernateSchema() {
		return getConfig(HIBERNATE).getString(SCHEMA);
	}

	public int getWebPort() {
		return getConfig(WEB).getInt(PORT);
	}

	public String getWebHost() {
		return getConfig(WEB).getString(HOST);
	}

	public boolean isCorsEnabled() {
		return getConfig(WEB).getBoolean(CORS_ENABLED);
	}

	private Config getConfig(String config) {
		return this.config.getConfig(config);
	}

	public Config getConfig() {
		return this.config;
	}

}
