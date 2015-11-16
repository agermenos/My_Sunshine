package com.udacity.mysunshine.utils;

import com.udacity.mysunshine.pojos.City;
import com.udacity.mysunshine.pojos.Temperature;
import com.udacity.mysunshine.pojos.Weather;
import com.udacity.mysunshine.pojos.WeatherAPIResponse;
import com.udacity.mysunshine.pojos.WeatherDay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alejandro on 11/15/15.
 */
public class WeatherParser {

    /* The date/time conversion code is going to be moved outside the asynctask later,
     * so for convenience we're breaking it out into its own method now.
     */
    private String getReadableDateString(long time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDateFormat.format(time);
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        // For presentation, assume the user doesn't care about tenths of a degree.
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }

    public WeatherAPIResponse parse(String jsonResponse) throws JSONException {

        WeatherAPIResponse response = new WeatherAPIResponse();
        JSONObject forecastJson = new JSONObject(jsonResponse);
        response.setCnt(forecastJson.getString("cnt"));
        response.setCod(forecastJson.getString("cod"));
        response.setCity(parseCity(forecastJson.getJSONObject("city")));
        List<WeatherDay> days = new ArrayList<>();

        JSONArray list = forecastJson.getJSONArray("list");
        for (int k=0; k<list.length(); k++) {
            JSONObject jsonWeatherDay = list.getJSONObject(k);
            days.add(parseWeatherDay(jsonWeatherDay));
        }
        response.setList(days);
        return response;
    }

    private City parseCity(JSONObject jsonCity) throws JSONException {
        City city = new City();
        city.setCountry(jsonCity.getString("country"));
        city.setName(jsonCity.getString("name"));
        city.setId(jsonCity.getString("id"));
        return city;
    }

    private WeatherDay parseWeatherDay(JSONObject jsonWeatherDay) throws JSONException {
        WeatherDay day =new WeatherDay();
        day.setClouds(jsonWeatherDay.getString("clouds"));
        day.setDeg(jsonWeatherDay.getString("deg"));
        day.setDt(jsonWeatherDay.getString("dt"));
        day.setHumidity(jsonWeatherDay.getString("humidity"));
        day.setPressure(jsonWeatherDay.getString("pressure"));
        day.setSpeed(jsonWeatherDay.getString("speed"));
        day.setTemp(parseTemp(jsonWeatherDay.getJSONObject("temp")));
        JSONArray weathers = jsonWeatherDay.getJSONArray("weather");
        day.setWeather(parseWeather(weathers.getJSONObject(0)));
        return day;
    }

    private Weather parseWeather(JSONObject weatherJson) throws JSONException {
        Weather weather=new Weather();
        weather.setDescription(weatherJson.getString("description"));
        weather.setIcon(weatherJson.getString("icon"));
        weather.setId(weatherJson.getString("id"));
        weather.setMain(weatherJson.getString("main"));
        return weather;
    }

    private Temperature parseTemp(JSONObject jsonTemp) throws JSONException {
        Temperature temp = new Temperature();
        temp.setDay(jsonTemp.getString("day"));
        temp.setEve(jsonTemp.getString("eve"));
        temp.setMax(jsonTemp.getString("max"));
        temp.setMin(jsonTemp.getString("min"));
        temp.setMorn(jsonTemp.getString("morn"));
        temp.setNight(jsonTemp.getString("night"));
        return temp;
    }
}
