package com.udacity.mysunshine.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.mysunshine.R;
import com.udacity.mysunshine.pojos.WeatherAPIResponse;
import com.udacity.mysunshine.pojos.WeatherDay;
import com.udacity.mysunshine.utils.FormatUtil;
import com.udacity.mysunshine.utils.WeatherParser;


import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private static final String LOG_TAG = "DetailActivityFragment";
    String mShareText;
    private final String FORECAST_SHARE_HASHTAG="#ShareMySunshineWeather";
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
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detail_fragment, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_item_share);

        ShareActionProvider mShareActionProvider=
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        
        if (mShareActionProvider!=null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
        else {
            Log.d(LOG_TAG, "Share Action Provider is null!");
        }
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
            mShareText = buildText(selectedDay);
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

    private String buildText(WeatherDay selectedDay) {
        StringBuilder builder= new StringBuilder();
        builder.append(FormatUtil.getPrettyDate(selectedDay.getDt().toString(), "MMM, dd"));
        builder.append(" ").append("Weatehr is ").append(selectedDay.getWeather().getMain());
        builder.append(" Max ").append(selectedDay.getTemp().getMax()).append("\u00b0");
        builder.append("/ Min ").append(selectedDay.getTemp().getMin()).append("\u00b0");
        return builder.toString();
    }

    private Intent createShareForecastIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.putExtra(Intent.EXTRA_TEXT, mShareText + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }

}
