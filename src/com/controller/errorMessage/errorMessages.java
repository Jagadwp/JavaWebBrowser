package com.controller.errorMessage;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class errorMessages {
	public static int getUrl(String theUrl) {
		int code = 200;
		try {
			// Open the URLConnection for reading
			URL u = new URL(theUrl);
			HttpURLConnection uc = (HttpURLConnection) u.openConnection();
			code = uc.getResponseCode();
			String response = uc.getResponseMessage();

			if (code == 404) {
				System.out.println("HTTP/1.x " + code + " " + response);
				System.out.println(
						"It may indicate a bad link, a document that has moved with no forwarding address, a mistyped URL, or something similar\r\n");
			} else if (code == 200) {
				System.out.println("HTTP/1.x " + code + " " + response);
				System.out.println("You’re Good To Go!\r\n");
			} else if (code == 206) {
				System.out.println("HTTP/1.x " + code + " " + response);
				System.out.println(
						"The server has returned the part of the resource the client requested using the byte range extension to HTTP, rather than the whole document.\r\n");
			} else if (code == 401) {
				System.out.println("HTTP/1.x " + code + " " + response);
				System.out.println(
						"Authorization, generally a username and password, is required to access this page\r\n");
			} else if (code == 403) {
				System.out.println("HTTP/1.x " + code + " " + response);
				System.out
						.println("The server understood the request, but is deliberately refusing to process it.\r\n");
			} else if (code == 301) {
				System.out.println("HTTP/1.x " + code + " " + response);
				System.out.println("Page Moved Permanently!\r\n");
			} else if (code == 302) {
				System.out.println("HTTP/1.x " + code + " " + response);
				System.out.println("Moved Temporarily!\r\n");
			} else if (code == 500) {
				System.out.println("HTTP/1.x " + code + " " + response);
				System.out.println("An unexpected condition occurredthat the server does not know how to handle.\r\n");
			} else if (code == 300) {
				System.out.println("HTTP/1.x " + code + " " + response);
				System.out.println("Redirecting\r\n");
			}

			System.out.println();
			try (InputStream in = new BufferedInputStream(uc.getInputStream())) {
				// chain the InputStream to a Reader
				Reader r = new InputStreamReader(in);
			}
		} catch (MalformedURLException ex) {
			System.err.println(theUrl + " is not a parseable URL");
		} catch (IOException ex) {
			System.err.println(ex);
		}

		return code;
	}

}
