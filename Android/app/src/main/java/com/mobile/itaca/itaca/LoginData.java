package com.mobile.itaca.itaca;

/**
 * Created by alonshmueli on 19/04/2016.
 */
public class LoginData {
    private String sucess;
    private String session_id;
    private String username;
    private String password;
    private String CompanName;
    private String id;
    private String error;

    public String getSucess() {
        return sucess;
    }
    public void setSucess(String sucess) {
        this.sucess = sucess;
    }

    public String getSession_id() {
        return session_id;
    }
    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompanName() {
        return CompanName;
    }
    public void setCompanName(String CompanName) {
        this.CompanName = CompanName;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
}
