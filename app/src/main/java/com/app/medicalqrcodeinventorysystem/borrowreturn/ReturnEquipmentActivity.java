package com.app.medicalqrcodeinventorysystem.borrowreturn;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.medicalqrcodeinventorysystem.R;
import com.app.medicalqrcodeinventorysystem.environment.Environment;
import com.app.medicalqrcodeinventorysystem.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReturnEquipmentActivity extends AppCompatActivity {

    private TextView statusTxtView;

    private TextView equipmentTypeTxtView;

    private TextView modelNumberTxtView;

    private TextView currentDesignationTxtView;

    private TextView returnDesignationTxtView;

    private TextView purposeTxtView;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_equipment);
        sessionManager = new SessionManager(getApplicationContext());
        modelNumberTxtView = findViewById(R.id.modelNumberReturnTxtView);
        statusTxtView = findViewById(R.id.statusReturnTxtView);
        equipmentTypeTxtView = findViewById(R.id.equipmentTypeReturnTxtView);
        currentDesignationTxtView = findViewById(R.id.currentDesignationTxtView);
        returnDesignationTxtView = findViewById(R.id.returnDesignationTxtView);
        purposeTxtView = findViewById(R.id.borrowPurposeTxtView);

        // TODO add borrowed by
        modelNumberTxtView.setText( getIntent().getStringExtra("model_number") );
        equipmentTypeTxtView.setText( getIntent().getStringExtra("equipment_type") );
        statusTxtView.setText( "In-Use" );
        currentDesignationTxtView.setText( getIntent().getStringExtra("current_designation") );
        returnDesignationTxtView.setText( getIntent().getStringExtra("return_designation") );
        purposeTxtView.setText( getIntent().getStringExtra("purpose") );

        if( getSupportActionBar() != null ) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void returnEquipment(View view) {
        ReturnEquipmentTask returnEquipmentTask = new ReturnEquipmentTask(getApplicationContext(), getIntent().getStringExtra("equipment_id"));
        returnEquipmentTask.execute();
    }


    public class ReturnEquipmentTask extends AsyncTask<String, Void, Void> {

        private Context context;

        private final String equipmentID;

        public ReturnEquipmentTask(Context context, final String equipmentID) {
            this.context = context;
            this.equipmentID = equipmentID;
        } //·

        @Override
        protected Void doInBackground(String... strings) {

            String getUrl = Environment.ROOT_PATH + "/api/v1/return-equipment";
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, getUrl,
                    response -> {
                        Log.i("ReturnEquipmentTask", response);
                        try {
                            JSONObject returnEquipmentObject = new JSONObject(response);

                            if( returnEquipmentObject.getBoolean("success") ) {
                                Intent returnLandingActivity = new Intent(context, ReturnLandingActivity.class);
                                returnLandingActivity.putExtra("equipment_id", equipmentID);
                                startActivity(returnLandingActivity);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }, Throwable::printStackTrace){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<>();
                    params.put("equipment_id", equipmentID);
                    params.put("user_id", sessionManager.getUserId());
                    return params;
                }
            };

            requestQueue.add(stringRequest);

            return null;
        }
    }



}