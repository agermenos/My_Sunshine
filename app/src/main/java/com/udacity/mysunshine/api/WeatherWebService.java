package com.udacity.mysunshine.api;

import android.util.Log;

import com.udacity.mysunshine.params.WeatherParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Alejandro on 11/15/15.
 */
public class WeatherWebService {
    private final String logTag="WeatherWebService";

    public String getJSONWeatherByParameters(WeatherParams params){
        WeatherAPI weatherAPI=new WeatherAPI();
        weatherAPI.api(params.getApi()).cnt(params.getCnt())
                .mode(params.getMode()).type(params.getType())
                .query(params.getQuery());
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        try {
            URL url = new URL(weatherAPI.build());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(logTag, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } catch(Exception e) {
            Log.e(logTag, "Error ", e);
        }    finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(logTag, "Error closing stream", e);
                }
            }
        }
        return forecastJsonStr;
    }
}
