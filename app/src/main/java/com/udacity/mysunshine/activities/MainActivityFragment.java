package com.udacity.mysunshine.activities;

import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.udacity.mysunshine.R;
import com.udacity.mysunshine.params.WeatherParams;
import com.udacity.mysunshine.pojos.WeatherAPIResponse;
import com.udacity.mysunshine.pojos.WeatherDay;
import com.udacity.mysunshine.tasks.FetchWeatherTask;
import com.udacity.mysunshine.utils.FormatUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ArrayAdapter mArrayAdapter;
    List<String> weekData;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String[] dataArray = {
                "Today - Sunny 88/63",
                "Tomorrow - Foggy 70/46",
                "Weds - Cloudy 72/63",
                "Thurs - Warmer 90/70",
                "Fri - Foggy 70/46",
                "Sat - Dead 00/00"
        };
        weekData = new ArrayList<>(Arrays.asList(dataArray));

        mArrayAdapter = new ArrayAdapter<>(
                getActivity(), R.layout.list_item_forecast, R.id.textview_forcaste_item, weekData);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        if (mArrayAdapter!=null) {
            listView.setAdapter((ArrayAdapter) mArrayAdapter);
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            WeatherParams wp = new WeatherParams();
            wp.setCnt("7"); wp.setMode("json"); wp.setQuery("94568");
            wp.setUnits("F");
            FetchWeatherTask weatherTask = new FetchWeatherTask(this);
            weatherTask.execute(wp);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setFetchWeatherTaskValues(WeatherAPIResponse response) {
        String prettyDate="";
        DecimalFormat df = new DecimalFormat("##");
        if (response!=null) {
            weekData = new ArrayList<>(response.getList().size());
            for (WeatherDay wd : response.getList()) {
                if (wd.getDt()!=null) {
                    prettyDate = FormatUtil.formatDate(new Date(new Long(wd.getDt())*1000), getString(R.string.date_format));
                }
                weekData.add(prettyDate + " " + wd.getWeather().getMain() + " " +
                        df.format(new Float(wd.getTemp().getMin())) + "/" +
                        df.format(new Float(wd.getTemp().getMax()))
                );
            }
            mArrayAdapter.clear();
            mArrayAdapter.addAll(weekData);
        }
    }
}
