package me.thrasher.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.logging.Level;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.java.Log;

//http://localhost:8080/urldecode/%7B%22179397559%22%3A%22false%22%2C%22179790633%22%3A%22direct%22%2C%22180070639%22%3A%22gc%22%7D
@Log
@Singleton
public class UrldecodeServlet extends HttpServlet {
	private static final long serialVersionUID = -1;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String code = getCode(req);
		code = URLDecoder.decode(code, "UTF-8");// support other encodings?
		log.log(Level.INFO, code);

		PrintWriter out = res.getWriter();
		out.print(code);
	}

	private String getCode(HttpServletRequest req) {
		StringBuilder code = new StringBuilder();

		String pathinfo = req.getPathInfo();
		if (pathinfo != null && pathinfo.length() > 1) {
			// strip leading /
			code.append(pathinfo.substring(1));
		}

		String queryString = req.getQueryString();
		if (queryString != null) {
			code.append("?");
			code.append(queryString);
		}

		return code.toString();
	}
}
