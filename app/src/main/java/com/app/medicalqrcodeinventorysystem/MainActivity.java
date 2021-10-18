package com.app.medicalqrcodeinventorysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.medicalqrcodeinventorysystem.user.SignUpActivity;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView errorMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        errorMessageTextView = findViewById(R.id.errorLoginTextView);
    }

    public void login( View view ) {
        EditText loginText = findViewById(R.id.loginTxt);
        EditText passwordText = findViewById(R.id.passwordTxt);

        validateLogin( loginText.getText().toString(), passwordText.getText().toString() );

    }

    private void validateLogin(final String employeeId, final String password) {
        String postUrl = Environment.ROOT_PATH + "/api/v1/validate-employee-login";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,
                response -> {
                    Log.i("LoginResponse", response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if( jsonResponse.getBoolean("success") ) {
                            errorMessageTextView.setText("");
                            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();

//                            JSONObject userInfo = jsonResponse.getJSONObject("user_info");
//                            UserDTO userDTO = new UserDTO(userInfo);
//
//                            Intent landingPageIntent;
//                            if( jsonResponse.getBoolean("has_address_info" ) ) {
//                                JSONObject addressInfo = jsonResponse.getJSONObject("address_info");
//                                userDTO.setAddress( new AddressDTO(addressInfo) );
//                                landingPageIntent = new Intent(getApplicationContext(), DashboardActivity.class);
//                            }else {
//                                userDTO.setAddress(null);
//                                landingPageIntent = new Intent(getApplicationContext(), ConfirmAddressActivity.class);
//                            }
//
//                            sessionManager.createLoginSession(userDTO);
//                            startActivity(landingPageIntent);
                        }else {
                            errorMessageTextView.setText("Invalid username or password.");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, Throwable::printStackTrace){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("employee_id", employeeId);
                params.put("password", new String(Hex.encodeHex(DigestUtils.sha1(password))));
                return params;
            }
        };

        requestQueue.add(stringRequest);

    }

    public void signUp( View view ) {
        Intent signUpIntent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(signUpIntent);
    }
}