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
import com.app.medicalqrcodeinventorysystem.dto.UserDTO;
import com.app.medicalqrcodeinventorysystem.environment.Environment;
import com.app.medicalqrcodeinventorysystem.equipments.EquipmentListActivity;
import com.app.medicalqrcodeinventorysystem.landing.LandingActivity;
import com.app.medicalqrcodeinventorysystem.session.SessionManager;
import com.app.medicalqrcodeinventorysystem.user.SignUpActivity;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView errorMessageTextView;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(getApplicationContext());
        errorMessageTextView = findViewById(R.id.errorLoginTextView);
    }

    public void login( View view ) {
        EditText loginText = findViewById(R.id.loginTxt);
        EditText passwordText = findViewById(R.id.passwordTxt);

        validateLogin( loginText.getText().toString(), passwordText.getText().toString() );

    }

    private void validateLogin(final String email, final String password) {
        String postUrl = Environment.ROOT_PATH + "/api/v1/validate-employee-login";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,
                response -> {
                    Log.i("LoginResponse", response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if( jsonResponse.getBoolean("success") ) {
                            errorMessageTextView.setText("");
//                            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();

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

                            // TODO user info
                            String userId = jsonResponse.getString("user_id");
                            UserDTO userDTO = new UserDTO(userId);
                            sessionManager.createLoginSession( userDTO );

                            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            Intent landingPageIntent = new Intent(getApplicationContext(), EquipmentListActivity.class);
                            startActivity(landingPageIntent);
                        }else {
                            errorMessageTextView.setText("Invalid email or password.");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, Throwable::printStackTrace){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("email", email);
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