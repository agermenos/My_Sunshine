package com.udacity.mysunshine.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.mysunshine.R;
import com.udacity.mysunshine.pojos.Weather;
import com.udacity.mysunshine.pojos.WeatherAPIResponse;
import com.udacity.mysunshine.pojos.WeatherDay;
import com.udacity.mysunshine.utils.FormatUtil;
import com.udacity.mysunshine.utils.WeatherParser;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    WeatherAPIResponse weatherBean;
    @Bind(R.id.textDate)
    TextView textDate;
    @Bind(R.id.textMain)
    TextView textMain;
    @Bind(R.id.textMax)
    TextView textMax;
    @Bind(R.id.textMin)
    TextView textMin;
    @Bind(R.id.textToday)
    TextView textToday;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        String jsonResponse = getActivity().getIntent().getStringExtra("JSON_RESPONSE");
        int position = getActivity().getIntent().getIntExtra("POSITION", 0);
        WeatherParser weatherParser=new WeatherParser();
        try {
            ButterKnife.bind(this, rootView);
            weatherBean = weatherParser.parse(jsonResponse);
            WeatherDay selectedDay = weatherBean.getList().get(position);
            textDate.setText(FormatUtil.getPrettyDate(selectedDay.getDt().toString(), getString(R.string.date_format)));
            textMain.setText(selectedDay.getWeather().getMain());
            textMax.setText(selectedDay.getTemp().getMax());
            textMin.setText(selectedDay.getTemp().getMin());
        }
        catch (Throwable e) {
            Log.e("DetailActivityFragment", e.getMessage());
        }
        return rootView;
    }

}
