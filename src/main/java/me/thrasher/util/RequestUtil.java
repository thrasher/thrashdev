package me.thrasher.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.java.Log;

@Log
public class RequestUtil {
	public static Map<String, String> getHeadersAsMap(HttpServletRequest req) {
		Map<String, String> map = new TreeMap<String, String>();
		@SuppressWarnings("rawtypes")
		Enumeration en = req.getHeaderNames();
		// enumerate through the keys and extract the values
		// from the keys!
		while (en.hasMoreElements()) {
			String name = (String) en.nextElement();
			String value = req.getHeader(name);
			map.put(name, value);
		}
		if (log.isLoggable(Level.FINE)) {
			StringBuilder sb = new StringBuilder();
			for (String key : map.keySet()) {
				if (sb.length() > 0) {
					sb.append('\n');
				}
				sb.append(key);
				sb.append(" = ");
				sb.append(map.get(key));
			}
			log.log(Level.FINE, sb.toString());
		}
		return map;
	}

	public static Map<String, String> getParametersAsMap(HttpServletRequest req) {
		Map<String, String> map = new TreeMap<String, String>();
		@SuppressWarnings("rawtypes")
		Enumeration en = req.getParameterNames();
		// enumerate through the keys and extract the values
		// from the keys!
		while (en.hasMoreElements()) {
			String name = (String) en.nextElement();
			String value = req.getParameter(name);
			map.put(name, value);
		}
		if (log.isLoggable(Level.FINE)) {
			StringBuilder sb = new StringBuilder();
			for (String key : map.keySet()) {
				if (sb.length() > 0) {
					sb.append('\n');
				}
				sb.append(key);
				sb.append(" = ");
				sb.append(map.get(key));
			}
			log.log(Level.FINE, sb.toString());
		}
		return map;
	}

	/**
	 * Stupid java doesn't come with a URL builder.
	 */
	public static String buildURL(String base, Map<String, Object> params) {
		if (params == null || params.isEmpty())
			return base;
		else
			return base + "?" + buildQueryString(params);
	}

	/**
	 * Create a query string
	 */
	public static String buildQueryString(Map<String, Object> params) {
		StringBuilder bld = new StringBuilder();

		boolean afterFirst = false;
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (afterFirst)
				bld.append("&");
			else
				afterFirst = true;

			bld.append(urlEncode(entry.getKey()));
			bld.append("=");
			bld.append(urlEncode(entry.getValue()));
		}

		return bld.toString();
	}

	/**
	 * Parse a query string.
	 */
	public static LinkedHashMap<String, String> parseQueryString(
			String queryString) {
		LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();

		String[] pairs = queryString.split("&");

		for (String pairStr : pairs) {
			String[] pair = pairStr.split("=");
			result.put(urlDecode(pair[0]), urlDecode(pair[1]));
		}

		return result;
	}

	/**
	 * An interface to URLEncoder.encode() that isn't inane
	 */
	public static String urlEncode(Object value) {
		try {
			return URLEncoder.encode(value.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * An interface to URLDecoder.decode() that isn't inane
	 */
	public static String urlDecode(Object value) {
		try {
			return URLDecoder.decode(value.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Generates a url from the request that is good for sending off with
	 * redirects
	 */
	public static String getActualUrl(HttpServletRequest request) {
		StringBuilder url = new StringBuilder(request.getRequestURL());

		String queryString = request.getQueryString();
		if (queryString != null) {
			url.append("?");
			url.append(queryString);
		}

		return url.toString();
	}
}
