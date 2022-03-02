package com.controller.webcontent;

import java.net.*;
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
			URLConnection urlConnection = url.openConnection(); // creating a urlconnection object

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

}
