package me.thrasher;

import java.util.logging.Level;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.ServletContextEvent;

import lombok.extern.java.Log;
import me.thrasher.util.GaeUtil;
import me.thrasher.web.UnimplementedServlet;
import me.thrasher.web.UrldecodeServlet;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

@Log
public class GuiceConfig extends GuiceServletContextListener {
	public static final String HOST_PORT = "hostPort";

	public static class AppServletModule extends ServletModule {
		@Override
		protected void configureServlets() {

			// unimplemented cases
			serve("/urldecode/*").with(UrldecodeServlet.class);
			serve("/urlencode").with(UnimplementedServlet.class);

		}
	}

	public static class AppModule extends AbstractModule {
		@Override
		protected void configure() {
		}

		@Provides
		@Singleton
		@Named(HOST_PORT)
		String getGaeUtil() {
			GaeUtil util = new GaeUtil(Constants.PROD_HOST, Constants.PROD_VERSION);
			return util.getHostPort();
		}
	}

	/**
	 * Logs the time required to initialize Guice
	 */
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		long time = System.currentTimeMillis();

		super.contextInitialized(servletContextEvent);

		long millis = System.currentTimeMillis() - time;
		log.log(Level.INFO, "Guice initialization took " + millis + " millis");
	}

	@Override
	protected Injector getInjector() {
		// ShiroAopModule is required to use @Requires* annotations
		return Guice.createInjector(//
				new AppModule(), //
				new AppServletModule());
	}
}
