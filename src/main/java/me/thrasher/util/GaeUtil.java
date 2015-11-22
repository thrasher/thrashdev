package me.thrasher.util;

import java.util.Date;
import java.util.logging.Level;

import lombok.extern.java.Log;

import com.google.appengine.api.utils.SystemProperty;
import com.google.appengine.api.utils.SystemProperty.Environment.Value;

@Log
public class GaeUtil {
	private String prodHost;
	private String prodVersion;

	public GaeUtil(String prodHost, String prodVersion) {
		this.prodHost = prodHost;
		this.prodVersion = prodVersion;
	}

	public String getHostPort() {
		log.log(Level.FINE, "SystemProperty.applicationVersion.get(): "
				+ getAppengineWebXmlVersion());
		if (SystemProperty.environment.value() == Value.Production) {
			if (prodVersion.equals(getAppengineWebXmlVersion())) {
				return prodHost;
			}
			return buildHost(SystemProperty.applicationId.get(),
					getAppengineWebXmlVersion());
		}
		return "localhost:8080";
	}

	/**
	 * GAE adds a timestamp to the GAE version number reported by the API, which
	 * must be removed to verify the version is what's expected.
	 * 
	 * @return the version number from the application-web.xml file
	 */
	String getAppengineWebXmlVersion() {
		// 2.386137556593235133
		if (SystemProperty.applicationVersion.get() != null) {
			return SystemProperty.applicationVersion.get().split("\\.")[0];
		}
		return null;
	}

	/**
	 * build hostname to app version like http://2-dot-appname.appspot.com/
	 * 
	 * @param appId
	 * @param appVersion
	 */
	public static String buildHost(String appId, String appVersion) {
		StringBuilder sb = new StringBuilder();
		sb.append(appVersion);
		sb.append("-dot-"); // NOTE: not '.' so https: wildcard cert works
		sb.append(appId);
		sb.append(".appspot.com");
		return sb.toString();
	}

	/**
	 * Checks using the security manager.
	 * 
	 * @return true if on GAE Dev or Prod
	 */
	public static boolean isGoogleAppEngine() {
		SecurityManager sm = System.getSecurityManager();
		return sm != null
				&& (sm.getClass().getName().startsWith("com.google.app"));
	}

	/**
	 * Unpack the GAE version data, which also has the date info.
	 * 
	 * @see https://code.google.com/p/googleappengine/issues/detail?id=5788
	 * @return date the GAE app was uploaded
	 */
	public static Date getUploadDate() {
		String appVer = SystemProperty.applicationVersion.get();
		appVer = appVer.substring(appVer.lastIndexOf(".") + 1);
		Long timestamp = Long.parseLong(appVer);
		return new Date(timestamp / (2 << 27) * 1000);
	}
}
