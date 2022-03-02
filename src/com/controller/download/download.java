package com.controller.download;

import java.net.*;
import java.io.*;

public class download implements Runnable {
	
	String link;
	File out;
	
	public download(String link, File out) {
		this.link = link;
		this.out  =out;
	}
	
	@Override
	public void run() {
		try {
			URL url = new URL(link);
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			double fileSize = (double)http.getContentLengthLong();
			BufferedInputStream in = new BufferedInputStream(http.getInputStream());
			FileOutputStream fos = new FileOutputStream(this.out);
			BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
			byte[] buffer = new byte[1024];
			double downloaded = 0.00;
			int read = 0;
			double percentDownloaded = 0.00;
			while((read-in.read(buffer, 0, 1024))>=0) {
				bout.write(buffer, 0, read);
				downloaded += read;
				percentDownloaded = (downloaded*100)/fileSize;
				String percent = String.format("%.4f", percentDownloaded);
				System.out.println("Downloaded "+percent+"% of a file");
				
			}
			bout.close();
			in.close();
			System.out.println("Download Complete");
			
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}


	public static void DownloadWebPage(String webpage) {
		try {

			// Create URL object
			URL url = new URL(webpage);
			BufferedReader readr = new BufferedReader(new InputStreamReader(url.openStream()));

			// Enter filename in which you want to download
			BufferedWriter writer = new BufferedWriter(new FileWriter("Download.pdf"));

			// read each line from stream till end
			String line;
			while ((line = readr.readLine()) != null) {
				writer.write(line);
			}

			readr.close();
			writer.close();
			System.out.println("Successfully Downloaded.");
		}

		// Exceptions
		catch (MalformedURLException mue) {
			System.out.println("Malformed URL Exception raised");
		} catch (IOException ie) {
			System.out.println("IOException raised");
		}
	}

	public static void main(String args[]) throws IOException {
		String url = "https://www.geeksforgeeks.org/";
		DownloadWebPage(url);
	}
}

// public class Download {

	
// }
