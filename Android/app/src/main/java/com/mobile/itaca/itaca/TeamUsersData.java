package com.mobile.itaca.itaca;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by alonshmueli on 04/05/2016.
 */

class UserForTeamData {
    @SerializedName("sum(minutes)")
    private Float sum;
    @SerializedName("UserCommit")
    private Float UserCommit;
    private String username;

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

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}

public class TeamUsersData {
    private String sucess;
    private ArrayList<UserForTeamData> usersData = new ArrayList<>();;

    // parse dictionary: {"sucess":"true","0":{"id":"1","username":"ronshra","TeamName":"rrrrrr","UserCommit":"240","sum(minutes)":"197","sum(minutes)\/L.`UserCommit`":"0.8208"}}
    @SerializedName("0")
    private UserForTeamData userData0;

    @SerializedName("1")
    private UserForTeamData userData1;

    @SerializedName("2")
    private UserForTeamData userData2;

    @SerializedName("3")
    private UserForTeamData userData3;

    @SerializedName("4")
    private UserForTeamData userData4;

    @SerializedName("5")
    private UserForTeamData userData5;

    @SerializedName("6")
    private UserForTeamData userData6;

    @SerializedName("7")
    private UserForTeamData userData7;

    @SerializedName("8")
    private UserForTeamData userData8;

    @SerializedName("9")
    private UserForTeamData userData9;

    @SerializedName("10")
    private UserForTeamData userData10;

    @SerializedName("11")
    private UserForTeamData userData11;

    @SerializedName("12")
    private UserForTeamData userData12;

    public void genetaeUsesData() {
        if (userData0 != null) {
            usersData.add(userData0);
        }
        if (userData1 != null) {
            usersData.add(userData1);
        }
        if (userData2 != null) {
            usersData.add(userData2);
        }
        if (userData3 != null) {
            usersData.add(userData3);
        }
        if (userData4 != null) {
            usersData.add(userData4);
        }
        if (userData5 != null) {
            usersData.add(userData5);
        }
        if (userData6 != null) {
            usersData.add(userData6);
        }
        if (userData7 != null) {
            usersData.add(userData7);
        }
        if (userData8 != null) {
            usersData.add(userData8);
        }
        if (userData9 != null) {
            usersData.add(userData9);
        }
        if (userData10 != null) {
            usersData.add(userData10);
        }
        if (userData11 != null) {
            usersData.add(userData11);
        }
        if (userData12 != null) {
            usersData.add(userData12);
        }
    }

    public String getSucess() {
        return sucess;
    }
    public void setSucess(String sucess) {
        this.sucess = sucess;
    }

    public ArrayList<UserForTeamData> getUsersData() {
        return usersData;
    }
}
