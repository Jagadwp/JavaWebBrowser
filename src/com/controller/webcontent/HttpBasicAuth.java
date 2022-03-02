package com.controller.webcontent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class HttpBasicAuth {

	public static void main(String[] args) {

		try {
			URL url = new URL("http://bit.ly/BerkasCalonKetuaHMTC");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			
			String auth = "jagad@mail.com" + ":" + "testt";
			String encoding = Base64.getEncoder().encodeToString(auth.getBytes());
			connection.setRequestProperty("Authorization", "Basic " + encoding);

			int status = connection.getResponseCode();
			System.out.println("Request URL ... " + url);
			boolean redirect = false;

			// normally, 3xx is redirect
			if (status >= 300 && status < 400) redirect = true;
			
			System.out.println("Response Code ... " + status);

			if (redirect) {

				// get redirect url from "location" header field
				String newUrl = connection.getHeaderField("Location");
				// get the cookie if need, for login
				String cookies = connection.getHeaderField("Set-Cookie");

				// open the new connnection again
				connection = (HttpURLConnection) new URL(newUrl).openConnection();
				connection.setRequestProperty("Cookie", cookies);
				connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
				connection.addRequestProperty("User-Agent", "Mozilla");
				connection.addRequestProperty("Referer", "google.com");

				System.out.println("Redirect to URL : " + newUrl);

			}

			InputStream content = (InputStream) connection.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(content));
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}