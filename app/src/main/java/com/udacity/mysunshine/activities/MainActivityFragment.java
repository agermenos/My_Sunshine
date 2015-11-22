package com.udacity.mysunshine.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ArrayAdapter mArrayAdapter;
    List<String> weekData;
    String jsonResponse;

    public MainActivityFragment() {
    }

    @Override
    public void onStart(){
        super.onStart();
        updateWeather();
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

        weekData = new ArrayList<>();
        mArrayAdapter = new ArrayAdapter<>(
                getActivity(), R.layout.list_item_forecast, R.id.textview_forcaste_item, weekData);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        if (mArrayAdapter!=null) {
            listView.setAdapter((ArrayAdapter) mArrayAdapter);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(view, weekData.get(position) + " clicked!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("JSON_RESPONSE", jsonResponse)
                        .putExtra("POSITION", position);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);

    }

    private void updateWeather(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String locationPref = sharedPref.getString(getString(R.string.pref_zip_code_key),
                getString(R.string.pref_default_display_zip_code));
        String tempPref = sharedPref.getString(getString(R.string.pref_units_key),
                getString(R.string.pref_units_default));
        WeatherParams wp = new WeatherParams();
        wp.setCnt("7"); wp.setMode("json"); wp.setQuery(locationPref);
        wp.setUnits(tempPref);
        FetchWeatherTask weatherTask = new FetchWeatherTask(this);
        weatherTask.execute(wp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Callback method for AsyncTask
    public void setFetchWeatherTaskValues(WeatherAPIResponse response, String jsonResponse) {
        String prettyDate="";
        this.jsonResponse = jsonResponse;
        DecimalFormat df = new DecimalFormat("##");
        if (response!=null) {
            weekData = new ArrayList<>(response.getList().size());
            for (WeatherDay wd : response.getList()) {
                if (wd.getDt()!=null) {
                    prettyDate = FormatUtil.getPrettyDate(wd.getDt().toString(), getString(R.string.date_format));
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
