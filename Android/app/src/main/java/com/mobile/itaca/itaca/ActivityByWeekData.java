package com.mobile.itaca.itaca;

//import java.util.HashMap;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alonshmueli on 01/05/2016.
 */
class WeekData {
    @SerializedName("sum(minutes)")
    private Float sum;
    private Float UserCommit;

    public Float getSum() {
        return sum;
    }
    public void setSum(Float sum) {
        this.sum = sum;
    }

    public Float getUserCommit() {
        return UserCommit;
    }
    public void setUserCommit(Float UserCommit) {
        this.UserCommit = UserCommit;
    }
}

public class ActivityByWeekData {
    private String sucess;
    // TODO:: parse dictionary: {"sucess":"true","0":{"count(*)":"1","sum(minutes)":"120","UserCommit":"240","Week":"18"}}
    @SerializedName("0")
    private WeekData weekData;

    public String getSucess() {
        return sucess;
    }
    public void setSucess(String sucess) {
        this.sucess = sucess;
    }

    public WeekData getWeekData() {
        return weekData;
    }
    public void setWeekData(WeekData weekData) {
        this.weekData = weekData;
    }
}
