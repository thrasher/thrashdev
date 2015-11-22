package me.thrasher.web;

import java.io.IOException;
import java.util.logging.Level;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.java.Log;
import me.thrasher.util.RequestUtil;

@Log
@Singleton
public class UnimplementedServlet extends HttpServlet {
	private static final long serialVersionUID = -1;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		log.log(Level.FINE, RequestUtil.getActualUrl(req));

		// get headers
		RequestUtil.getHeadersAsMap(req);
		RequestUtil.getParametersAsMap(req);

		res.sendError(HttpServletResponse.SC_NO_CONTENT);
	}

}
