package me.thrasher.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;

import lombok.extern.java.Log;

@Log
public class UrlContentCache {
	// SimpleNotificationService-d6d679a1d18e95c2f9ffcf11f4f9e198.pem
	private HashMap<String, MapEntry> mCache = new HashMap<String, MapEntry>();
	private static final long MAX_AGE = 1000 * 60 * 60 * 24; // 24 hours

	public UrlContentCache() {
	}

	public void flushAll() {
		mCache.clear();
	}

	public void flush(String url) {
		mCache.remove(url);
	}

	public Set<String> getUrls() {
		return mCache.keySet();
	}

	public void putBytes(String url, byte[] bytes) {
		MapEntry entry = new MapEntry(System.currentTimeMillis(), bytes);
		mCache.put(url, entry);
	}

	public byte[] getBytes(URL url) {
		return getBytes(url.toExternalForm());
	}

	public byte[] getBytes(String url) {
		// determine when the file was last modified on disk
		MapEntry entry = mCache.get(url);

		if (entry != null) {
			// if older than threshold, refresh the data
			if (System.currentTimeMillis() - entry.getLastModified() > MAX_AGE) {
				// remove the entry reference
				entry = null;
				mCache.remove(url);
			}
		}

		// create a new entry in the cache if necessary
		if (entry == null) {
			try {
				// save to the cache
				byte[] b = load(url);
				if (b != null)
					putBytes(url, b);
				entry = mCache.get(url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return entry == null ? null : entry.getBytes();
	}

	private static byte[] load(String url) throws IOException {
		URL u = new URL(url);
		URLConnection con = u.openConnection();

		InputStream in = null;
		ByteArrayOutputStream out = null;

		if (con instanceof HttpURLConnection) {
			HttpURLConnection connection = (HttpURLConnection) con;
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				in = new BufferedInputStream(con.getInputStream());
				out = new ByteArrayOutputStream();
			} else {
				log.log(Level.WARNING, "Error " + connection.getResponseCode()
						+ " getting url  " + u.toExternalForm());
				return null;
			}
		} else {
			in = new BufferedInputStream(con.getInputStream());
			out = new ByteArrayOutputStream();
		}

		int b = 0;
		while ((b = in.read()) != -1) {
			out.write(b);
		}

		in.close();
		out.close();

		return out.toByteArray();
	}

	/**
	 * This class represents a value in the cache Map.
	 */
	private class MapEntry {
		private long lastModified; // when the file was modified
		private byte[] bytes;

		MapEntry(long lastModified, byte[] bytes) {
			this.lastModified = lastModified;
			this.bytes = bytes;
		}

		public long getLastModified() {
			return this.lastModified;
		}

		public byte[] getBytes() {
			return this.bytes;
		}
	}
}
