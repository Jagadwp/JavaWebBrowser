package com.view;

import com.controller.webcontent.HttpBasicAuth;
import com.controller.webcontent.UrlConnectionReader;
import com.controller.download.download;
import com.controller.errorMessage.errorMessages;

import java.util.Scanner;
import java.io.*;

public class Main {
	public static void main(String[] args) {
		welcomeText();
		Scanner sc = new Scanner(System.in);
		int featureNumber = 0;

		try {
			featureNumber = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		String uri;
		int status;

		switch (featureNumber) {
			case 1:
				System.out.println("Enter URI:");
				uri = sc.nextLine();
				status = errorMessages.getUrl(uri);

				if (status >= 200 && status < 400) {
					String output = UrlConnectionReader.getUrlContents(uri);
					System.out.println("\nWeb Content:\n" + output);
				}

				break;
			case 2:
				System.out.println("Enter URI:");
				uri = sc.nextLine();
				
				System.out.println("Choose HTTP Method:");
				String method = sc.nextLine();
				
				System.out.println("Credential(user:pass) :");
				String credential = sc.nextLine();

				status = errorMessages.getUrl(uri);

				if (status >= 200 && status < 400) {
					String output = HttpBasicAuth.getUrlContents(uri, method, credential);;
					System.out.println("\nWeb Content:\n" + output);
				}

				break;
			case 3:
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
		System.out.println("1. Open Web Page \n" + "2. Open Web Page with Basic Auth\n" + "3. Download Web Contents\n");
		System.out.println("Enter the feature's number to try the feature:");
	}
}