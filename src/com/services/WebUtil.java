package com.services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WebUtil {
	 public static String getStatusCode(String response) {
        String statusCode = "";
        
        String pattern = "(HTTP....) (\\w+) (.*)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(response);
        if(m.find()) {
            statusCode = m.group(2);
        }
        return statusCode;
    }
    
    public static String getStatusMessage(String response) {
        String msg = "";
        
        String pattern = "(HTTP....) (\\w+) (.*)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(response);
        if(m.find()) {
            msg = m.group(3);
        }
        return msg;
    }
    
    public static int getContentLength(String response) {
    	String pattern = "Content-Length: ([^\n]*)";
    	Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(response);
		
		if (m.find()) {
			return Integer.parseInt(m.group(1).trim());
		}
		return 0;
    }
	
	public static List<String> showClickable(String response) {
		String pattern = "<a.*(href=\"|href=\')([^\"\']*)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(response);
		
		List<String> clickable = new ArrayList<String>();
		while(m.find()) {
			if (m.group(2).startsWith("#"))
				continue;
			clickable.add(m.group(2));
			System.out.println(m.group(2));
		}
		return clickable;
	}

	public static boolean isThereCookie(String response) {
		String pattern = "Set-Cookie: ";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(response); 
		if (m.find()) {
			return true;
		}
		return false;
	}

	public static String getCookie(String response) {
		String pattern = "Set-Cookie: (.*)(PHPSESSID=[^\\s]+)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(response); 
		if (m.find()) {
			return m.group(2);
		}
		return "";
	}

}
