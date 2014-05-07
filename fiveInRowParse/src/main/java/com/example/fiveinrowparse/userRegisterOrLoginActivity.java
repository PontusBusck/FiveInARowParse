package com.example.fiveinrowparse;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class userRegisterOrLoginActivity extends Activity {

    private LoginFragment mLoginFragment;
    private RegisterFragment mRegisterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_register_or_login);


        mLoginFragment = new LoginFragment();
        mRegisterFragment = new RegisterFragment();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, mLoginFragment).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_register_or_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void registerUser(View view) {
        EditText usernameField = (EditText) findViewById(R.id.username_textfield);
        EditText passwordField = (EditText) findViewById(R.id.password_textfield);

        if(usernameField.getText() != null && passwordField.getText() != null) {
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();

            if(!username.equals("") && !password.equals("")) {
                ParseUser user = new ParseUser();
                user.setUsername(usernameField.getText().toString());
                user.setPassword(usernameField.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(userRegisterOrLoginActivity.this, "User created!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(userRegisterOrLoginActivity.this, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }else{
                Toast.makeText(this, "You must choose both a password and a username!", Toast.LENGTH_SHORT).show();
            }

        } else{
            Toast.makeText(this, "You must choose both a password and a username!", Toast.LENGTH_SHORT).show();
        }
    }

    public void loginUser(View view) {
        EditText usernameField = (EditText) findViewById(R.id.username_login_textfield);
        EditText passwordField = (EditText) findViewById(R.id.password_login_textfield);

        if(usernameField.getText() != null && passwordField.getText() != null) {
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();

            if(!username.equals("") && !password.equals("")) {
                ParseUser.logInInBackground(usernameField.getText().toString(), passwordField.getText().toString(), new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                            installation.put("userId", ParseUser.getCurrentUser().getObjectId());
                            installation.saveInBackground();
                            Toast.makeText(userRegisterOrLoginActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(userRegisterOrLoginActivity.this, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else{
                Toast.makeText(this, "You must enter both a password and a username!", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(this, "You must enter both a password and a username!", Toast.LENGTH_SHORT).show();

        }
    }

    public void goToRegisterUser(View view) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, mRegisterFragment).commit();

    }

    public static class LoginFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.login_fragment, container, false);
        }
    }

    public static class RegisterFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.register_fragment, container, false);
        }
    }

}
