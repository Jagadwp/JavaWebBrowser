package com.view;

import com.controller.*;
import com.controller.webcontent.UrlConnectionReader;
import com.controller.download.download;

import java.net.*;
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

		switch (featureNumber) {
		case 1:
			System.out.println("Enter URI:");
			String uri = sc.nextLine();
			String messages = errorMessages.getUrl(uri);
			String output = UrlConnectionReader.getUrlContents(uri);
			System.out.println("Web Content:\n" + messages +"\n" + output);
//			showClickableLinksList();
			break;
		case 2:
			System.out.println("Tuesday");
			break;
		case 3:
			String downloadLink= sc.nextLine();
			//Input Direktori kemana file harus disimpan 
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

	static void welcomeText() {
		System.out.println("Welcome to Simple Web Browser App\n");
		System.out.println("Features:");
		System.out
				.println("1. Open Web Page \n" + "2. Open Web Page with Basic Auth\n" + "3. Download Web Contents\\n");
		System.out.println("Enter the feature's number to try the feature:");
	}
}