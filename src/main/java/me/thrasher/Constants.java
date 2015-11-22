package me.thrasher;

public class Constants {

	/**
	 * The production hostname that serves the app. This should be where the app
	 * lives when deployed, not the development/stage/canary value.
	 */
	public static final String PROD_HOST = "thrashdev.appspot.com";

	/**
	 * The version of the app that is live in production for users. This can be
	 * used at runtime to check if we are live, or a development version.
	 */
	public static final String PROD_VERSION = "1";

}
