package com.udacity.mysunshine.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.udacity.mysunshine.activities.MainActivityFragment;
import com.udacity.mysunshine.api.WeatherWebService;
import com.udacity.mysunshine.params.WeatherParams;
import com.udacity.mysunshine.pojos.WeatherAPIResponse;
import com.udacity.mysunshine.utils.WeatherParser;


/**
 * Created by Alejandro on 11/15/15.
 */
public class FetchWeatherTask extends AsyncTask<WeatherParams, Void, WeatherAPIResponse> {
    private final static String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    private MainActivityFragment caller=null;

    public FetchWeatherTask(MainActivityFragment caller){
        super();
        this.caller=caller;
    }

    @Override
    protected WeatherAPIResponse doInBackground(WeatherParams... params) {
        try {
            WeatherWebService wws = new WeatherWebService();
            String jsonResponse = wws.getJSONWeatherByParameters(params[0]);
            WeatherParser parser = new WeatherParser();
            return parser.parse(jsonResponse);
        }
        catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(WeatherAPIResponse response) {
        super.onPostExecute(response);
        caller.setFetchWeatherTaskValues(response);
    }
}
