package org.fca.processors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class LanguageTranslationTextRussian extends LanguageTranslationText {

    private static final String translate_api = "https://script.google.com/macros/s/AKfycbw4upkbVcDr07foHAwqDiUQct4ouFi76l0ZSKAP72lolUtJjJU/exec?q=%s&langFrom=%s&langTo=en";

    private String language;

    public LanguageTranslationTextRussian() {
        super("ru");
    }

    @Override
    protected String translate(String value, String language) {

        StringBuffer translation = new StringBuffer();

        while (value.length() > 500) {
            int e = 500;
            while (Character.isLetter(value.charAt(e))) e--;
            translation.append(translateChunk(value.substring(0,e),language));
            value = value.substring(e);
        }

        translation.append(translateChunk(value,language));
        return translation.toString();

    }

    private String translateChunk(String value, String language) {

        try {

            URL url = new URL(String.format(translate_api, URLEncoder.encode(value,"UTF-8"),language));

            StringBuilder response = new StringBuilder();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader stream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = stream.readLine()) != null) {
                response.append(inputLine);
            }
            stream.close();

            return response.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

}
