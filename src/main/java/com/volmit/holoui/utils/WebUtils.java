package com.volmit.holoui.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class WebUtils {

    public static JsonElement getJson(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        if(con.getResponseCode() != 200)
            throw new IOException("Failed to retrieve JSON data from \"" + url + "\": " + con.getResponseCode());
        try(BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            StringBuilder buffer = new StringBuilder();
            String line;
            while((line = in.readLine()) != null)
                buffer.append(line);
            return JsonParser.parseString(buffer.toString());
        }
    }

    public static void downloadFile(String url, File target) throws IOException {
        FileUtils.copyURLToFile(new URL(url), target);
    }
}
