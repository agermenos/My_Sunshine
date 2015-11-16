package com.udacity.mysunshine.pojos;

import java.util.List;

/**
 * Created by Alejandro on 11/15/15.
 */
public class WeatherAPIResponse {
    private String cod;
    private String message;
    private City city;
    private String cnt;
    private List<WeatherDay> list;

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public List<WeatherDay> getList() {
        return list;
    }

    public void setList(List<WeatherDay> list) {
        this.list = list;
    }
}
