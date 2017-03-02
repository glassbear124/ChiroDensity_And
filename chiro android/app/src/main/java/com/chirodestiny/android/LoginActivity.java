package com.chirodestiny.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chirodestiny.android.data.PodcastCatData;
import com.chirodestiny.android.net.GetDataTask;
import com.chirodestiny.android.net.LoginRequestTask;
import com.chirodestiny.android.net.LoginTask;
import com.chirodestiny.android.net.RequestTask;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.TimeZone;

/**
 * Created by Administrator on 2016-09-05.
 */
public class LoginActivity extends Activity {

    ImageView loginBut;
    EditText  emailTxt, passwordTxt;
    TextView txtSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);

        loginBut = (ImageView)findViewById(R.id.login_but);
        emailTxt = (EditText) findViewById(R.id.emailTxt);
        passwordTxt = (EditText)findViewById(R.id.passwordTxt);
        txtSignup = (TextView)findViewById(R.id.signUp);

        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://app.chirodestiny.com/appapis/signup.php"));
                startActivity(browserIntent);
            }
        });

        emailTxt.setText(""); passwordTxt.setText("");

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Log","Token : " + refreshedToken);

        if ( refreshedToken == null )
            refreshedToken = "";
        final String finalRefreshedToken = refreshedToken;
        loginBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailTxt.getText().toString();
                String password = passwordTxt.getText().toString();

                new LoginTask(getApplicationContext(), email, password, finalRefreshedToken,  new LoginRequestTask.onExecuteListener(){
                    @Override
                    public void onExecute(JSONObject result) {
                        if ( result != null)
                        {
                            Log.d("Log", "Login Response : " + result.toString());
                            try {
                                if ( result.getString("isValid").equals("True")) {

                                    Common.userId = result.getString("userid");
                                    Common.status = result.getString("status");

                                    SharedPreferences p = getApplicationContext().getSharedPreferences("pref", 0);
                                    SharedPreferences.Editor ed = p.edit();
                                    ed.putString("userID", Common.userId);
                                    ed.putString("status", Common.status);
                                    ed.commit();

                                    Intent intent = new Intent(LoginActivity.this, SelectMenu.class);
                                    startActivity(intent);

                                    new GetDataTask(LoginActivity.this, "timezone", new RequestTask.onExecuteListener(){
                                        @Override
                                        public void onExecute(JSONArray result)
                                        {
                                        }
                                    }).execute();

                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), result.getString("Message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                }).execute();
            }
        });
    }
}
