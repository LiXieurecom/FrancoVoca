package fr.eurecom.os.francovoca;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.CharBuffer;

/**
 * Created by lxllx on 2016/1/5.
 */

public class Client implements Serializable{
    private String email;
    private String password;
    private String username;
    private long experience;

    public Client() {
    }

    public void setInformation(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public long getExperience() { return this.experience; }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {return this.password;}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setExperience(long experience) {this.experience = experience;}

}
