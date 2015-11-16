package com.udacity.mysunshine.api;

/**
 * Created by Alejandro on 11/14/15.
 */
public class WeatherAPI {
    public static String BASE="http://api.openweathermap.org/data/";
    private static String POSTFIX="/forecast/";
    private String type="daily";
    private String mode="json";
    private String cnt = "7";
    private String version = "2.5";
    private String units="metric";
    private String api ="f52ae2a42e6502848dade6d8f9ea08d0";
    private String query="94043,US";
    // "http://api.openweathermap.org/data/2.5/forecast/daily?q=94568,US&mode=json&units=metric&cnt=7&appid=";
    public WeatherAPI type(String type) {
        if (type!=null) this.type=type;
        return this;
    }
    public WeatherAPI mode(String mode) {
        if (mode!=null) this.mode=mode;
        return this;
    }
    public WeatherAPI cnt(String cnt) {
        if (cnt!=null) this.cnt=cnt;
        return this;
    }
    public WeatherAPI version(String version){
        if (version!=null) this.version=version;
        return this;
    }
    public WeatherAPI api(String api){
        if (api!=null) this.api=api;
        return this;
    }
    public WeatherAPI query(String query){
        if (query!=null) this.query=query;
        return this;
    }
    public String build(){
        StringBuilder stringBuilder=new StringBuilder(BASE);
        stringBuilder.append(this.version);
        stringBuilder.append(POSTFIX);
        stringBuilder.append(this.type).append("/");
        stringBuilder.append("?q=").append(query);
        stringBuilder.append("&mode=").append(mode);
        stringBuilder.append("&units=").append(units);
        stringBuilder.append("&cnt=").append(cnt);
        stringBuilder.append("&appid=").append(api);
        return stringBuilder.toString();
    }
    public static void main(String args[]){
        WeatherAPI weatherAPI=new WeatherAPI();
        weatherAPI.query("94568,US").cnt("4").api("f52ae2a42e6502848dade6d8f9ea08d0").mode("xml");
        System.out.println(weatherAPI.build());
    }
}
