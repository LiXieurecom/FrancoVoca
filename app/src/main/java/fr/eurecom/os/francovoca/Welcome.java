package fr.eurecom.os.francovoca;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

public class Welcome extends AppCompatActivity {

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */

    private static final int UI_ANIMATION_DELAY = 300;
    private static final int LOGIN = 0;
    private static final int SIGNUP = 1;
    private static final int SUCCESS = 2;
    private static final int SHOWTIME = 2500;
    private static final int FADE_TIME = 500;

    private View mContentView;
    private View mControlsView;
    private View mLogin;
    private View mSignup;
    private View mBoard;
    private boolean mVisible;
    private TextView information;
    private Client client;
    private LoginUtil loginService;
    private EditText login_edit_email;
    private EditText login_edit_password;
    private EditText signup_edit_email;
    private EditText signup_edit_password;
    private EditText signup_edit_password_confirmation;
    private EditText signup_edit_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        client = new Client();
        mVisible = true;
        mControlsView = findViewById(R.id.content_controller);
        mContentView = findViewById(R.id.fullscreen_content);
        mLogin = findViewById(R.id.login_div);
        mSignup = findViewById(R.id.signup_div);
        mBoard = findViewById(R.id.information_board);
        information = (TextView) findViewById(R.id.information);
        mBoard.setVisibility(View.INVISIBLE);
        mSignup.setVisibility(View.INVISIBLE);
        mLogin.setVisibility(View.INVISIBLE);
        login_edit_email = (EditText) findViewById(R.id.login_email_edit);
        login_edit_password = (EditText) findViewById(R.id.login_password_edit);
        signup_edit_email = (EditText) findViewById(R.id.signup_email_edit);
        signup_edit_password = (EditText) findViewById(R.id.signup_password_edit);
        signup_edit_password_confirmation = (EditText) findViewById(R.id.signup_password_confirmation_edit);
        signup_edit_username = (EditText) findViewById(R.id.signup_username_edit);
        loginService = new LoginUtil(loginHandler);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        final Button login = (Button) findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = login_edit_email.getText().toString();
                String password = login_edit_password.getText().toString();
                if(!LoginUtil.isEmail(email)){
                    login_edit_email.setText("");
                    login_edit_password.setText("");
                    delayedInformation("You need to enter a valid E-mail Address!", LOGIN, SHOWTIME);
                } else {
                    if (!LoginUtil.isPassword(password)){
                        login_edit_password.setText("");
                        delayedInformation("You need to enter a valid Password!", LOGIN, SHOWTIME);
                    } else {
                        client.setInformation(email, password);
                        showInformation("Connecting Server ... ...");
                        loginService.setRequest(client, LoginUtil.LOGINSERVICE);
                        Thread loginThread = new Thread(loginService);
                        loginThread.start();
                    }
                }
            }
        });
        Button signup = (Button) findViewById(R.id.signup_button);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHideAnimation(mLogin, FADE_TIME);
                setShowAnimation(mSignup, FADE_TIME);
            }
        });
        Button confirmation = (Button) findViewById(R.id.confirmation_button);
        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = signup_edit_email.getText().toString();
                String password = signup_edit_password.getText().toString();
                String password_confirmation = signup_edit_password_confirmation.getText().toString();
                String username = signup_edit_username.getText().toString();
                if (!LoginUtil.isEmail(email)){
                    signup_edit_email.setText("");
                    signup_edit_password.setText("");
                    signup_edit_password_confirmation.setText("");
                    signup_edit_username.setText("");
                    delayedInformation("You need to enter a valid E-mail Address!\nA valid E-mail should contain @", SIGNUP, SHOWTIME);
                } else {
                    if (!LoginUtil.isPassword(password) || !LoginUtil.isPassword(password_confirmation)){
                        signup_edit_password.setText("");
                        signup_edit_password_confirmation.setText("");
                        signup_edit_username.setText("");
                        delayedInformation("You need to enter a valid Password!\nA valid Password should have a length from 4 to 10", SIGNUP, SHOWTIME);
                    } else {
                        if (!password.equals(password_confirmation)){
                            signup_edit_password.setText("");
                            signup_edit_password_confirmation.setText("");
                            signup_edit_username.setText("");
                            delayedInformation("Your confirmation password should be identical with your password", SIGNUP, SHOWTIME);
                        } else {
                            if (!LoginUtil.isUsername(username)){
                                signup_edit_password.setText("");
                                signup_edit_password_confirmation.setText("");
                                signup_edit_username.setText("");
                                delayedInformation("Your Username should have a length from 3 to 10", SIGNUP, SHOWTIME);
                            } else {
                                client.setInformation(email, password);
                                client.setUsername(username);
                                showInformation("Connecting Server ... ...");
                                loginService.setRequest(client, LoginUtil.SIGNUPSERVICE);
                                Thread loginThread = new Thread(loginService);
                                loginThread.start();
                            }
                        }
                    }
                }
            }
        });
        Button signup_back = (Button) findViewById(R.id.signup_back_button);
        signup_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHideAnimation(mSignup, FADE_TIME);
                setShowAnimation(mLogin, FADE_TIME);
            }
        });
    }

    Handler loginHandler = new Handler() {
        public void handleMessage(Message msg){
            boolean isLogin = msg.getData().getBoolean("Login");
            boolean isSignup = msg.getData().getBoolean("Signup");
            String status = msg.getData().getString("Status");
            if(isLogin) {
                switch (status) {
                    case LoginUtil.SUCCESS_LOGIN:
                        login_edit_email.setText("");
                        login_edit_password.setText("");
                        delayedInformation("Authentication Successful!!\nWelcome~~\nDear "+client.getUsername(), SUCCESS, SHOWTIME);
                        break;
                    case LoginUtil.WRONG_PASSWORD:
                        login_edit_password.setText("");
                        delayedInformation("Wrong Password!!\nPlease check your E-mail address and your password.", LOGIN, SHOWTIME);
                        break;
                    case LoginUtil.NO_EMAIL:
                        login_edit_email.setText("");
                        login_edit_password.setText("");
                        delayedInformation("There is no such E-mail Address!!\nIf you haven't signed up, please click the button on the right.", LOGIN, SHOWTIME);
                }
            }
            if (isSignup) {
                switch (status){
                    case LoginUtil.SUCCESS_LOGIN:
                        signup_edit_email.setText("");
                        signup_edit_password.setText("");
                        signup_edit_password_confirmation.setText("");
                        signup_edit_username.setText("");
                        delayedInformation("Sign Up Successfully!\nCongradulations!", LOGIN, SHOWTIME);
                        break;
                    case LoginUtil.SAME_EMAIL:
                        signup_edit_email.setText("");
                        signup_edit_password.setText("");
                        signup_edit_password_confirmation.setText("");
                        signup_edit_username.setText("");
                        delayedInformation("E-mail Address already exists...\nPlease change another E-mail Address", SIGNUP, SHOWTIME);
                        break;
                }
            }
        }
    };

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLogin.setVisibility(View.INVISIBLE);
        mSignup.setVisibility(View.INVISIBLE);
        mBoard.setVisibility(View.INVISIBLE);
        hide();
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        mControlsView.setVisibility(View.INVISIBLE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            mControlsView.setVisibility(View.VISIBLE);
            setShowAnimation(mLogin, FADE_TIME);
        }
    };

    private final Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private final Handler mInformationHandler = new Handler();
    private final Runnable mInformationRunable_Login = new Runnable() {
        @Override
        public void run() {
            Log.i("Login:", "Start");
            mSignup.setVisibility(View.INVISIBLE);
            setHideAnimation(mBoard, FADE_TIME);
            setShowAnimation(mLogin, FADE_TIME);
        }
    };
    private final Runnable mInformationRunable_Signup = new Runnable() {
        @Override
        public void run() {
            mLogin.setVisibility(View.INVISIBLE);
            setHideAnimation(mBoard, FADE_TIME);
            setShowAnimation(mSignup, FADE_TIME);
        }
    };
    private final Runnable mInformationRunable_Success = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(), DashBoard.class);
            intent.putExtra("Client", client);
            startActivity(intent);
        }
    };

    private void delayedInformation(String message, int category, int delayMillis) {
        if (mLogin.getVisibility() == View.VISIBLE){
            setHideAnimation(mLogin, FADE_TIME);
        }
        if (mSignup.getVisibility() == View.VISIBLE){
            setHideAnimation(mSignup, FADE_TIME);
        }
        information.setText(message);
        if (mBoard.getVisibility() == View.INVISIBLE){
            setShowAnimation(mBoard, FADE_TIME);
        }
        mInformationHandler.removeCallbacks(mInformationRunable_Signup);
        mInformationHandler.removeCallbacks(mInformationRunable_Login);
        mInformationHandler.removeCallbacks(mInformationRunable_Success);
        switch (category) {
            case LOGIN:
                mInformationHandler.postDelayed(mInformationRunable_Login, delayMillis);
                break;
            case SIGNUP:
                mInformationHandler.postDelayed(mInformationRunable_Signup, delayMillis);
                break;
            case SUCCESS:
                mInformationHandler.postDelayed(mInformationRunable_Success, delayMillis);
        }
    }

    private void showInformation (String message) {
        information.setText(message);
        if (mLogin.getVisibility() == View.VISIBLE){
            mLogin.setVisibility(View.INVISIBLE);
        }
        if (mSignup.getVisibility() == View.VISIBLE){
            mSignup.setVisibility(View.INVISIBLE);
        }
        if (mBoard.getVisibility() == View.INVISIBLE){
            mBoard.setVisibility(View.VISIBLE);
        }
    }

    private AlphaAnimation mHideAnimation = null;
    private AlphaAnimation mShowAnimation= null;
    private void setHideAnimation( final View view, int duration ){
        if( null == view || duration < 0 ){
            return;
        }
        if( null != mHideAnimation ){
            mHideAnimation.cancel( );
        }
        mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        mHideAnimation.setDuration(duration);
        mHideAnimation.setFillAfter(true);
        mHideAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation( mHideAnimation );
    }
    private void setShowAnimation( final View view, int duration ){
        if( null == view || duration < 0 ){
            return;
        }
        if( null != mShowAnimation ){
            mShowAnimation.cancel( );
        }
        mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        mShowAnimation.setDuration(duration);
        mShowAnimation.setFillAfter(true);
        mShowAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation( mShowAnimation );
    }
}
