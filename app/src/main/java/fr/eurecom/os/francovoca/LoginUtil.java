package fr.eurecom.os.francovoca;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.CharBuffer;

/**
 * Created by lxllx on 2016/1/5.
 */
public class LoginUtil implements Runnable{

    private static final int MINIMUM_PASSWORD_LENGTH = 4;
    private static final int MAXIMUM_PASSWORD_LENGTH = 10;
    private static final int MINIMUM_USERNAME_LENGTH = 3;
    private static final int MAXIMUM_USERNAME_LENGTH = 10;
    public static final int LOGINSERVICE = 100;
    public static final int SIGNUPSERVICE = 200;
    private final String url = "http://1-dot-centralserver-1181.appspot.com/centralserver";
    public static final String SUCCESS_LOGIN = "success_login";
    public static final String WRONG_PASSWORD = "wrong_password";
    public static final String NO_EMAIL = "no_email";
    public static final String SAME_EMAIL = "same_email";
    private String status;
    private Handler handler;
    private int type;
    private Client client;

    public LoginUtil (Handler handler) {
        this.handler = handler;
    }

    public void setRequest (Client client, int type) {
        this.client = client;
        this.type = type;
    }

    public static boolean isEmail(String email) {
        if(email.contains("@")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isPassword (String password){
        if(password.length() > MINIMUM_PASSWORD_LENGTH  && password.length() <= MAXIMUM_PASSWORD_LENGTH) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isUsername (String username) {
        if(username.length() > MINIMUM_USERNAME_LENGTH && username.length() < MAXIMUM_USERNAME_LENGTH) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void run() {
        if(this.type == LOGINSERVICE) {
            JSONObject response = null;
            try {
                URL url = new URL(this.url);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                String postParameters = converToPara("login");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                con.setRequestProperty("Charset", "utf-8");
                con.setFixedLengthStreamingMode(postParameters.getBytes().length);
                con.connect();
                PrintWriter out = new PrintWriter(con.getOutputStream());
                Log.i("postData:", postParameters);
                out.print(postParameters);
                out.close();
                InputStreamReader in = new InputStreamReader(con.getInputStream(),"UTF-8");
                Reader reader = in;
                String jsonString = readAll(in);
                Log.i("Json:", jsonString);
                response = new JSONObject(jsonString);
                con.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                switch (response.getString("type")){
                    case "Success":
                        client.setUsername(response.getString("username"));
                        client.setExperience(response.getLong("experience"));
                        this.status = SUCCESS_LOGIN;
                        break;
                    case "Wrong":
                        this.status = WRONG_PASSWORD;
                        break;
                    case "No":
                        this.status = NO_EMAIL;
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("Status", this.status);
            bundle.putBoolean("Login", true);
            bundle.putBoolean("Singup", false);
            message.setData(bundle);
            handler.sendMessage(message);
        }

        if (this.type == SIGNUPSERVICE) {
            JSONObject response = null;
            try {
                URL url = new URL(this.url);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                String postParameters = converToPara("signup");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                con.setRequestProperty("Charset", "utf-8");
                con.setFixedLengthStreamingMode(postParameters.getBytes().length);
                con.connect();
                PrintWriter out = new PrintWriter(con.getOutputStream());
                Log.i("postData:", postParameters);
                out.print(postParameters);
                out.close();
                InputStreamReader in = new InputStreamReader(con.getInputStream(),"UTF-8");
                Reader reader = in;
                String jsonString = readAll(in);
                Log.i("Json:", jsonString);
                response = new JSONObject(jsonString);
                con.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                switch (response.getString("type")){
                    case "Success":
                        this.status = SUCCESS_LOGIN;
                        break;
                    case "Failure":
                        this.status = SAME_EMAIL;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("Status", this.status);
            bundle.putBoolean("Login", false);
            bundle.putBoolean("Signup", true);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }

    private String converToPara(String request_type){
        if(request_type.equals("login")){
            return "request_type=login&"+"email="+client.getEmail()+"&"+"password"+"="+client.getPassword();
        } else {
            if (request_type.equals("signup")){
                return "request_type=signup&email="+client.getEmail()+"&password="+client.getPassword()+"&username="+client.getUsername();
            } else {
                return "";
            }
        }
    }

    private String readAll(Reader reader) throws IOException {
        StringBuilder builder = new StringBuilder(4096);
        for (CharBuffer buf = CharBuffer.allocate(512); (reader.read(buf)) > -1; buf
                .clear()) {
            builder.append(buf.flip());
        }
        return builder.toString();
    }
}
