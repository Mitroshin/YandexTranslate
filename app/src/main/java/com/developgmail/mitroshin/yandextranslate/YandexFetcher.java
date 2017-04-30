package com.developgmail.mitroshin.yandextranslate;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class YandexFetcher {
    public static final String TAG = "YandexFetcher";
    public static final String API_KEY = "trnsl.1.1.20170409T220612Z.39bd7f8838bf263b.80dd81cbca10d22872c640dd733fe291e4047fb6";

    public byte[] getUrlBytes(String urlString) {
        HttpURLConnection connection = getConnectionByURL(urlString);
        return getByteGroupByConnection(connection);
    }

    private HttpURLConnection getConnectionByURL(String urlString) {
        try {
            return tryToGetConnectionByUrl(urlString);
        } catch (IOException ioe) {
            Log.e(TAG, "Connection not established : " + ioe);
        }
        return null;
    }

    private HttpURLConnection tryToGetConnectionByUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return connection;
        } else {
            throw new IOException(connection.getResponseMessage() + ": with " + urlString);
        }
    }

    private byte[] getByteGroupByConnection(HttpURLConnection connection) {
        try {
            tryToGetByteGroupByConnection(connection);
        } catch (IOException e) {
            Log.e(TAG, "Error reading data: " + e);
        } finally {
            connection.disconnect();
        }
        return null;
    }

    private byte[] tryToGetByteGroupByConnection(HttpURLConnection connection) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = connection.getInputStream();
        int bytesRead = 0;
        byte[] buffer = new byte[1024];
        while ((bytesRead = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, bytesRead);
        }
        return outputStream.toByteArray();
    }












    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }
}
