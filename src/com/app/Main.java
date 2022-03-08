package com.app;

import com.service.*;
import com.controller.download.download;

import java.util.Scanner;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {
	public static String root;
	public static String[] paths;
	public static String header;

	public static void main(String[] args) {
		welcomeText();
		Scanner sc = new Scanner(System.in);

		int featureNumber = 0;
		try {
			featureNumber = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		String url;

		switch (featureNumber) {
			case 1:
				System.out.println("Enter URL:");
				url = sc.nextLine();

				String response = Web.makeNormalRequest(url, "");
				String statusCode = WebUtil.getStatusCode(header);
				String statusMessage = WebUtil.getStatusMessage(header);

				showOutput(response, statusCode, statusMessage);

				break;
			case 2:

				break;
			case 3:
				System.out.println("Enter URL:");
				url = sc.nextLine();

				System.out.print("Download Filename\n>");
				String filename = sc.nextLine();

				try {
					URL urlObject = new URL(url);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				int s = Web.download(url, filename);

				if (s == 1)
					System.out.println("--Download Success--");
				else
					System.out.println("--Download Failed--");

				break;
			case 4:
				String downloadLink = sc.nextLine();
				// Input Direktori kemana file harus disimpan
				String dir = sc.nextLine();
				File out = new File(dir);
				new Thread(new download(downloadLink, out)).start();
				// System.out.println("Wednesday");
				break;
			default:
				System.out.println("Wrong input");
		}

		sc.close();

	}

	private static void welcomeText() {
		System.out.println("Welcome to Simple Web Browser App\n");
		System.out.println("Features:");
		System.out.println(
				"1. Open Web Page \n" + "2. Open Web Page with Basic Auth\n" + "3. Login\n" + "4. Download File\n");
		System.out.println("Enter the feature's number to try the feature:");
	}

	private static void showOutput(String response, String statusCode, String statusMessage) {
		if (statusCode.charAt(0) == '3') {
			System.out.println("You got message as > " + statusMessage);
			System.out.println("Status Code: " + statusCode);
			System.out.println("Redirecting...");
			response = Web.redirect(header);
			statusCode = WebUtil.getStatusCode(header);
			statusMessage = WebUtil.getStatusMessage(header);
			System.out.println(response);
		} else if (statusCode.charAt(0) == '4') {
			System.out.println("You got an error with message: " + statusMessage);
			System.out.println("Status Code: " + statusCode);
			System.out.println("Aborting Connection...");
		} else {
			System.out.println(response);
		}

		boolean abort = false;
		Scanner scanner = new Scanner(System.in);

		while (!abort) {
			System.out.println("What would you like to do?");
			System.out.println("1. Show Clickable Links");
			System.out.println("2. Show Status Code and Message");
			System.out.println("3. Make another request URL");
			System.out.println("Choice > ");

			String choice = scanner.nextLine();

			if (choice.equals("1")) {
				WebUtil.showClickable(response);
			} else if (choice.equals("2")) {
				System.out.println("Response Status Code: " + statusCode);
				System.out.println("Response Message: " + statusMessage);
			} else if (choice.equals("3")) {
				abort = true;
			}
		}

		scanner.close();

	}
}