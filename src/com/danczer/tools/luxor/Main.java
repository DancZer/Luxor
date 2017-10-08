package com.danczer.tools.luxor;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("Invalid arguments.");
            return;
        }

        String url = args[0];
        //Path filePath = Paths.get("");
        String filePathStr = "C:\\Temp\\" + "luxor.csv";

        System.out.println("Downloading...");
        try {
            Download(url, filePathStr);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        File file = new File(filePathStr);

        if (!file.exists()) {
            System.out.println("File not found" + file.getAbsolutePath() + "!");
            return;
        }

        int year = Integer.parseInt(args[1]);
        int week = Integer.parseInt(args[2]);

        CsvReader reader = new CsvReader(file.toPath());

        System.out.println("Reading...");
        try {
            reader.ReadWeek(year, week);
            reader.PrintValues();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void Download(String from, String toFile) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        // Create a new trust manager that trust all certificates
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());


        File file = new File(toFile);

        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException("Can not delete file!");
            }
        }

        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                throw new IOException("Can not create directories!");
            }
        }

        URL website = new URL(from);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(file, true);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        //fos.close();
    }
}
