package com.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.app.*;

public class Web {
	public static String makeNormalRequest(String url, String additional) {

		String pattern, host = "", path = "";
		String res = "";

		if (url.contains("http")) {
			pattern = "(http://|https://)([^/]*)(.*)";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(url);
			if (m.find()) {
				host = m.group(2);
				path = m.group(3);
				if (!url.contains("/"))
					path = "/";
			}
		} else {
			pattern = "([^/]*)(.*)";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(url);
			if (m.find()) {
				host = m.group(1);
				path = m.group(2);
				if (!url.contains("/"))
					path = "/";
			}
		}

		Main.root = host;
		Main.paths = path.split("/", 0);

		String req = "GET " + path + " HTTP/1.1\r\n" + "Host: " + host
				+ additional + "\r\n\r\n";

		try {
			Socket sock = new Socket(host, 80);

			BufferedInputStream bis = new BufferedInputStream(sock.getInputStream());
			BufferedOutputStream bos = new BufferedOutputStream(sock.getOutputStream());

			bos.write(req.getBytes());
			bos.flush();

			byte[] bRes = new byte[1];
			int c = bis.read(bRes, 0, 1);
			int contentLength = 0;
			boolean isBody = false;

			while (c != -1) {
				res += (new String(bRes));

				if (res.contains("\r\n\r\n") && isBody == false) {
					contentLength = WebUtil.getContentLength(res);
					isBody = true;
					Main.header = res;
					res = "";
				}

				if (isBody) {
					contentLength -= 1;
				}

				// System.out.print(new String(bRes));
				c = bis.read(bRes, 0, 1);

				if (isBody && contentLength == 0) {
					break;
				}
			}
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return res;
	}

	public static String redirect(String response) {
		String pattern = "(Location:) (.*)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(response);

		String newUrl = "";
		if (m.find()) {
			newUrl = m.group(2);
		}

		String absolutePath = "";
		if (!newUrl.contains("http")) {
			absolutePath = Main.root + "/";
		}

		if (Main.paths.length > 1) {
			for (int i = 0; i < Main.paths.length - 1; i++) {
				absolutePath += (Main.paths[i] + "/");
			}
		}

		absolutePath += newUrl;

		String cookie = "";
		if (WebUtil.isThereCookie(Main.header)) {
			cookie = "\r\nCookie: " + WebUtil.getCookie(Main.header);
		}

		return makeNormalRequest(absolutePath, cookie);
	}

	public static int download(String url, String fileName) {

		String pattern, host = "", path = "";
		String res = "";

		if (url.contains("http")) {
			pattern = "(http://|https://)([^/]*)(.*)";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(url);
			if (m.find()) {
				host = m.group(2);
				path = m.group(3);
				if (!url.contains("/"))
					path = "/";
			}
		} else {
			pattern = "([^/]*)(.*)";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(url);
			if (m.find()) {
				host = m.group(1);
				path = m.group(2);
				if (!url.contains("/"))
					path = "/";
			}
		}

		Main.root = host;
		Main.paths = path.split("/", 0);

		String req = "GET " + path + " HTTP/1.1\r\n" + "Host: " + host
				+ "\r\n\r\n";

		try {
			Socket sock = new Socket(host, 80);

			BufferedInputStream bis = new BufferedInputStream(sock.getInputStream());
			BufferedOutputStream bos = new BufferedOutputStream(sock.getOutputStream());
			FileOutputStream fos = new FileOutputStream(fileName);

			bos.write(req.getBytes());
			bos.flush();

			byte[] bRes = new byte[1];
			int c = bis.read(bRes, 0, 1);
			int contentLength = 0;
			boolean isBody = false;

			while (c != -1) {
				res += (new String(bRes));

				if (isBody) {
					fos.write(bRes);
				}

				if (res.contains("\r\n\r\n") && isBody == false) {
					contentLength = WebUtil.getContentLength(res);
					isBody = true;
					Main.header = res;
					res = "";
				}

				if (isBody) {
					contentLength -= 1;
				}

				// System.out.print(new String(bRes));
				c = bis.read(bRes, 0, 1);

				if (isBody && contentLength == 0) {
					break;
				}
			}
			sock.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	public static String makeBasicAuthRequest(String url, String id, String password) {
		//
		String pattern, host = "", path = "";
		String res = "";

		if (url.contains("http")) {
			pattern = "(http://|https://)([^/]*)(.*)";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(url);
			if (m.find()) {
				host = m.group(2);
				path = m.group(3);
			}
		} else {
			pattern = "([^/]*)(.*)";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(url);
			if (m.find()) {
				host = m.group(1);
				path = m.group(2);
				if (!url.contains("/"))
					path = "/";
			}
		}

		Main.root = host;
		Main.paths = path.split("/", 0);

		// Make Base64
		String idpass = id + ":" + password;
		String encoded = Base64.getEncoder().encodeToString(idpass.getBytes());

		String req = "GET " + path + " HTTP/1.1\r\n" + "Host: " + host + "\r\n"
				+ "Authorization: Basic " + encoded + "\r\n\r\n";

		try {
			Socket sock = new Socket(host, 80);

			BufferedInputStream bis = new BufferedInputStream(sock.getInputStream());
			BufferedOutputStream bos = new BufferedOutputStream(sock.getOutputStream());

			bos.write(req.getBytes());
			bos.flush();

			byte[] bRes = new byte[1];
			int c = bis.read(bRes, 0, 1);
			int contentLength = 0;
			boolean isBody = false;

			while (c != -1) {
				res += (new String(bRes));

				if (res.contains("\r\n\r\n") && isBody == false) {
					// System.out.println("duh"+res);
					contentLength = WebUtil.getContentLength(res);
					// System.out.println(contentLength);
					isBody = true;
					Main.header = res;
					res = "";
				}

				if (isBody) {
					contentLength -= 1;
				}

				// System.out.print(new String(bRes));
				c = bis.read(bRes, 0, 1);

				if (isBody && contentLength == 0) {
					break;
				}
			}
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return res;
	}

	public static String makeLoginRequest(String url, String key1, String key2) {
		//
		String pattern, host = "", path = "";
		String res = "";

		if (url.contains("http")) {
			pattern = "(http://|https://)([^/]*)(.*)";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(url);
			if (m.find()) {
				host = m.group(2);
				path = m.group(3);
			}
		} else {
			pattern = "([^/]*)(.*)";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(url);
			if (m.find()) {
				host = m.group(1);
				path = m.group(2);
				if (!url.contains("/"))
					path = "/";
			}
		}

		Main.root = host;
		Main.paths = path.split("/", 0);

		try {
			key1 = URLEncoder.encode(key1, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String payload = "email=" + key1 + "&password=" + key2 + "&submit=";
		String req = "POST " + path + " HTTP/1.1\r\n" + "Host: " + host
				+ "\r\nContent-Type: application/x-www-form-urlencoded\r\n"
				+ "Content-Length: " + payload.length() + "\r\n\r\n" + payload;
		// System.out.println(req);
		try {
			Socket sock = new Socket(host, 80);

			BufferedInputStream bis = new BufferedInputStream(sock.getInputStream());
			BufferedOutputStream bos = new BufferedOutputStream(sock.getOutputStream());

			bos.write(req.getBytes());
			bos.flush();

			byte[] bRes = new byte[1];
			int c = bis.read(bRes, 0, 1);
			int contentLength = 0;
			boolean isBody = false;

			while (c != -1) {
				res += (new String(bRes));

				if (res.contains("\r\n\r\n") && isBody == false) {
					contentLength = WebUtil.getContentLength(res);
					isBody = true;
					Main.header = res;
					res = "";
				}

				if (isBody) {
					contentLength -= 1;
				}

				// System.out.print(new String(bRes));
				c = bis.read(bRes, 0, 1);

				if (isBody && contentLength == 0) {
					break;
				}
			}
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return res;
	}

}
