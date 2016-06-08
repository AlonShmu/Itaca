package com.mobile.itaca.itaca;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alonshmueli on 04/05/2016.
 */

class TeamActivityWeekData {
    @SerializedName("sum(minutes)")
    private Float sum;
    @SerializedName("sum(`UserCommit`)")
    private Float UserCommit;
    private String teamname;

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

    public String getTeamname() {
        return teamname;
    }
    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }
}

public class TeamActivityByWeekData {
    private String sucess;
    // parse dictionary: {"sucess":"true","0":{"count(*)":"1","sum(minutes)":"120","sum(`UserCommit`)":"240","teamname":"rrrrrr","companname":"OptimalPlus"}}
    @SerializedName("0")
    private TeamActivityWeekData weekData0;

    @SerializedName("1")
    private TeamActivityWeekData weekData1;

    @SerializedName("2")
    private TeamActivityWeekData weekData2;

    @SerializedName("3")
    private TeamActivityWeekData weekData3;

    @SerializedName("4")
    private TeamActivityWeekData weekData4;

    @SerializedName("5")
    private TeamActivityWeekData weekData5;

    @SerializedName("6")
    private TeamActivityWeekData weekData6;

    public String getSucess() {
        return sucess;
    }
    public void setSucess(String sucess) {
        this.sucess = sucess;
    }

    public TeamActivityWeekData getWeekData0() {
        return weekData0;
    }
    public void setWeekData0(TeamActivityWeekData weekData0) {
        this.weekData0 = weekData0;
    }

    public TeamActivityWeekData getWeekData1() {
        return weekData1;
    }
    public void setWeekData1(TeamActivityWeekData weekData1) {
        this.weekData1 = weekData1;
    }

    public TeamActivityWeekData getWeekData2() {
        return weekData2;
    }
    public void setWeekData2(TeamActivityWeekData weekData2) {
        this.weekData2 = weekData2;
    }

    public TeamActivityWeekData getWeekData3() {
        return weekData3;
    }
    public void setWeekData3(TeamActivityWeekData weekData3) {
        this.weekData3 = weekData3;
    }

    public TeamActivityWeekData getWeekData4() {
        return weekData4;
    }
    public void setWeekData4(TeamActivityWeekData weekData4) {
        this.weekData4 = weekData4;
    }

    public TeamActivityWeekData getWeekData5() {
        return weekData5;
    }
    public void setWeekData5(TeamActivityWeekData weekData5) {
        this.weekData5 = weekData5;
    }

    public TeamActivityWeekData getWeekData6() {
        return weekData6;
    }
    public void setWeekData6(TeamActivityWeekData weekData6) {
        this.weekData6 = weekData6;
    }
}
