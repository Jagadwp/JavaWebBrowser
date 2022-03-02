package com.controller.webcontent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpBasicAuth {

	public static String getUrlContents(String theUrl, String method, String credential) {
		StringBuilder content = new StringBuilder();
		StringBuilder links = new StringBuilder();

		try {
			URL url = new URL(theUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);
			connection.setDoOutput(true);
			
			String auth = credential;
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

			// wrapping the urlconnection in a bufferedreader
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			// reading from the urlconnection using the bufferedreader

			while ((line = bufferedReader.readLine()) != null) {
				content.append(line + "\n");
				
				String lineTemp = line.toString();
				if (lineTemp.contains("<a href=\"http")) {
					lineTemp = extractLink(lineTemp);
					links.append(lineTemp + "\n");
				}
			}
			content.append("\nClickable Links list:\n");
			content.append(links);

			bufferedReader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return content.toString();
	}

	public static String extractLink(String str){
  
		StringBuilder url = new StringBuilder();
  
        String regex
            = "\\b((?:https?|ftp|file|http):"
              + "//[-a-zA-Z0-9+&@#/%?="
              + "~_|!:, .;]*[-a-zA-Z0-9+"
              + "&@#/%=~_|])";
  
        // Compile the Regular Expression
        Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
  
        // Find the match between string
        // and the regular expression
        Matcher m = p.matcher(str);
  
        // Find the next subsequence of
        // the input subsequence that
        // find the pattern
        while (m.find()) {
  
            // Find the substring from the
            // first index of match result
            // to the last index of match
            // result and add in the list
			url.append(str.substring(m.start(0), m.end(0)));
        }

		return url.toString();
    }

}