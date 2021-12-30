package com.app.medicalqrcodeinventorysystem.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.medicalqrcodeinventorysystem.environment.Environment;
import com.app.medicalqrcodeinventorysystem.MainActivity;
import com.app.medicalqrcodeinventorysystem.R;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private TextView errorMessageTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        errorMessageTxtView = findViewById(R.id.errorMessageTxtView);

        if( getSupportActionBar() != null ) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void register(View view) {
        CheckBox agreeBox = findViewById(R.id.agreeCBox);
        if( !agreeBox.isChecked() ) {
            Toast.makeText(this, "Please agree to the terms and conditions.", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText employeeEmailTxt = findViewById(R.id.employeeIdTxt);
        EditText firstName = findViewById(R.id.firstNameTxt);
        EditText lastNameTxt = findViewById(R.id.lastNameTxt);
        EditText passwordText = findViewById(R.id.passwordSignUpTxt);

        signUp(employeeEmailTxt.getEditableText().toString(), firstName.getEditableText().toString(), lastNameTxt.getEditableText().toString(),
                passwordText.getEditableText().toString());

    }

    private void signUp( final String email, final String firstName, final String lastName, final String password ) {
        String postUrl = Environment.ROOT_PATH + "/api/v1/employee-sign-up";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,
                response -> {
                    Log.i("SignUpResponse", response);


                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean success = responseObject.getBoolean("success");

                        if( success ) {
                            Toast.makeText(this, "Sign Up Successful!", Toast.LENGTH_SHORT).show();
                            redirectToLogin();
                        }else {
                            errorMessageTxtView.setText(responseObject.getString("errors"));
                            Toast.makeText(this, responseObject.getString("errors"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }, Throwable::printStackTrace){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("email", email);
                params.put("first_name", firstName);
                params.put("last_name", lastName);
                params.put("password", new String(Hex.encodeHex(DigestUtils.sha1(password))) );
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void redirectToLogin() {
        Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(loginIntent);
    }
}