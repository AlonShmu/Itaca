package com.mobile.itaca.itaca;

import com.google.gson.annotations.SerializedName;

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

    public String getSucess() {
        return sucess;
    }
    public void setSucess(String sucess) {
        this.sucess = sucess;
    }

    public UserForTeamData getUserData0() {
        return userData0;
    }
    public void setUserData0(UserForTeamData userData0) {
        this.userData0 = userData0;
    }

    public UserForTeamData getUserData1() {
        return userData1;
    }
    public void setUserData1(UserForTeamData userData1) {
        this.userData1 = userData1;
    }

    public UserForTeamData getUserData2() {
        return userData2;
    }
    public void setUserData2(UserForTeamData userData2) {
        this.userData2 = userData2;
    }

    public UserForTeamData getUserData3() {
        return userData3;
    }
    public void setUserData3(UserForTeamData userData3) {
        this.userData3 = userData3;
    }

    public UserForTeamData getUserData4() {
        return userData4;
    }
    public void setUserData4(UserForTeamData userData4) {
        this.userData4 = userData4;
    }

    public UserForTeamData getUserData5() {
        return userData5;
    }
    public void setUserData5(UserForTeamData userData5) {
        this.userData5 = userData5;
    }

    public UserForTeamData getUserData6() {
        return userData6;
    }
    public void setUserData6(UserForTeamData userData6) {
        this.userData6 = userData6;
    }

    public UserForTeamData getUserData7() {
        return userData7;
    }
    public void setUserData7(UserForTeamData userData7) {
        this.userData7 = userData7;
    }
}
