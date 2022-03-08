package com.controller.webcontent;

import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class UrlConnectionReader {
	public static String getUrlContents(String theUrl) {
		StringBuilder content = new StringBuilder();
		StringBuilder links = new StringBuilder();

		// Use try and catch to avoid the exceptions
		try {
			URL url = new URL(theUrl); // creating a url object
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

			//Redirector
			int status = urlConnection.getResponseCode();
			System.out.println("Request URL ... " + url);
			boolean redirect = false;

			// normally, 3xx is redirect
			if (status >= 300 && status < 400) redirect = true;
			System.out.println("Response Code ... " + status);

			if (redirect) {

				// get redirect url from "location" header field
				String newUrl = urlConnection.getHeaderField("Location");
				// get the cookie if need, for login
				String cookies = urlConnection.getHeaderField("Set-Cookie");

				// open the new connnection again
				urlConnection = (HttpURLConnection) new URL(newUrl).openConnection();
				urlConnection.setRequestProperty("Cookie", cookies);
				urlConnection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
				urlConnection.addRequestProperty("User-Agent", "Mozilla");
				urlConnection.addRequestProperty("Referer", "google.com");

				System.out.println("Redirect to URL : " + newUrl);

			}

			// wrapping the urlconnection in a bufferedreader
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
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

	public static void UrlContent(String uriInput) {
        try {
            Socket socket = new Socket(uriInput, 80);
            String protocols = "GET / HTTP/1.1\r\nHost: " + uriInput + "\r\n\r\n ";
            System.out.println(protocols);
 
            BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
 
            bos.write(protocols.getBytes());
            bos.flush();
 
            int bufferSize = 100;
            byte[] bResp = new byte[bufferSize];
            int c = bis.read(bResp);
            String resp = "";
 
            while(c != -1) {
                resp += (new String(bResp));
                bResp = new byte[bufferSize];
                c = bis.read(bResp);
 
            }
            System.out.println(resp);
            // urlNow += resp;
 
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(UrlConnectionReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
