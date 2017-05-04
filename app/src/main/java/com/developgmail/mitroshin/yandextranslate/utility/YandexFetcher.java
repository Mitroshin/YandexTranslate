package com.developgmail.mitroshin.yandextranslate.utility;

import android.net.Uri;
import android.util.Log;

import com.developgmail.mitroshin.yandextranslate.gson.GsonDetermineLanguage;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class YandexFetcher {
    private static final String TAG = "YandexFetcher";
    private static final String API_KEY = "trnsl.1.1.20170409T220612Z.39bd7f8838bf263b.80dd81cbca10d22872c640dd733fe291e4047fb6";

    private static final int OPERATION_WAS_SUCCESSFUL = 200;
    private static final int INVALID_API_KEY = 401;
    private static final int API_KEY_LOCKED = 402;
    private static final int DAILY_LIMIT_EXCEEDED = 404;
    private static final int MAXIMUM_TEXT_SIZE_EXCEEDED = 413;
    private static final int TEXT_CAN_NOT_BE_TRANSLATED = 422;
    private static final int TRANSLATION_DIRECTION_IS_NOT_SUPPORTED = 501;


    public String determineLanguageOfText(String text) {
        try {
            return tryToDetermineLanguageOfText(text);
        } catch (IOException ioe) {
            Log.e(TAG, "Error while requesting language definition: ", ioe);
        }
        return null;
    }

    private String tryToDetermineLanguageOfText(String text) throws IOException {
        String queryDetectLanguage = getQueryToDetermineLanguageOfText(text);
        String jsonDetectLanguage = getUrlString(queryDetectLanguage);
        Gson gson = new Gson();
        GsonDetermineLanguage gsonDetermineLanguage = gson.fromJson(jsonDetectLanguage, GsonDetermineLanguage.class);
        int code = gsonDetermineLanguage.getCode();
        if (code == OPERATION_WAS_SUCCESSFUL) {
            return gsonDetermineLanguage.getLang();
        } else {
            return notificationByCode(code);
        }
    }

    private String getQueryToDetermineLanguageOfText(String text) {
        return Uri.parse("https://translate.yandex.net/api/v1.5/tr.json/detect? ")
                .buildUpon()
                .appendQueryParameter("key", API_KEY)
                .appendQueryParameter("text", text)
                .build().toString();
    }

    private String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    private byte[] getUrlBytes(String urlString) {
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
            return tryToGetByteGroupByConnection(connection);
        } catch (IOException e) {
            Log.e(TAG, "Error reading data: " + e);
        } finally {
            connection.disconnect();
        }
        return null;
    }

    private byte[] tryToGetByteGroupByConnection(HttpURLConnection connection) throws IOException {
        InputStream inputStream = connection.getInputStream();
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException(connection.getResponseMessage());
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int bytesRead = 0;
        byte[] buffer = new byte[1024];
        while ((bytesRead = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, bytesRead);
        }
        return outputStream.toByteArray();
    }

    //TODO судя по тому, что изменение ключа не повлекло за собой уведомления о неправильном ключе - фиг знает как обрабатывать эту ситуацию.
    // Возникает ситуация возникновения null при получении connection. Надо думать.
    private String notificationByCode(int code) {
        switch (code) {
            case INVALID_API_KEY:
                return "Invalid API key";
            case API_KEY_LOCKED:
                return "API key locked";
            case DAILY_LIMIT_EXCEEDED:
                return "It exceeded the daily limit on the amount of translated text";
            case MAXIMUM_TEXT_SIZE_EXCEEDED:
                return "Maximum text size exceeded";
            case TEXT_CAN_NOT_BE_TRANSLATED:
                return "Text can not be translated";
            case TRANSLATION_DIRECTION_IS_NOT_SUPPORTED:
                return "Specified translation direction is not supported";
        }
        return null;
    }
}
