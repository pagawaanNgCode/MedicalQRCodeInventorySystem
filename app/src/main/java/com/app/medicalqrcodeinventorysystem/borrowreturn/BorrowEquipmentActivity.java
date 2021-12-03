package com.app.medicalqrcodeinventorysystem.borrowreturn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.medicalqrcodeinventorysystem.R;
import com.app.medicalqrcodeinventorysystem.environment.Environment;
import com.app.medicalqrcodeinventorysystem.session.SessionManager;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BorrowEquipmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView statusTxtView;

    private TextView equipmentTypeTxtView;

    private TextView modelNumberTxtView;

    private TextView defaultDesignationTxtView;

    private EditText purposeTxtView;

    private Spinner borrowDesignationSpinner;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_equipment);
        sessionManager = new SessionManager(getApplicationContext());
        modelNumberTxtView = findViewById(R.id.modelNumberTxtView);
        statusTxtView = findViewById(R.id.statusTxtView);
        equipmentTypeTxtView = findViewById(R.id.equipmentTypeTxtView);
        defaultDesignationTxtView = findViewById(R.id.defaultDesignationTxtView);
        purposeTxtView = findViewById(R.id.purposeTxtView);
        borrowDesignationSpinner = findViewById(R.id.borrowDesignationSpinner);
        borrowDesignationSpinner.setOnItemSelectedListener( this );

        modelNumberTxtView.setText( getIntent().getStringExtra("model_number") );
        equipmentTypeTxtView.setText( getIntent().getStringExtra("equipment_type") );
        statusTxtView.setText( "Available" );
        defaultDesignationTxtView.setText( getIntent().getStringExtra("designation_name") );

        GetDesignationsTask getDesignationsTask = new GetDesignationsTask(this, getIntent().getStringExtra("designation_name") );
        getDesignationsTask.execute();

        if( getSupportActionBar() != null ) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void borrow(View view) {
        BorrowEquipmentTask borrowEquipmentTask = new BorrowEquipmentTask(getApplicationContext(),
               getIntent().getStringExtra("equipment_id"),
                (String)borrowDesignationSpinner.getSelectedItem(), purposeTxtView.getText().toString() );
        Log.i("borrow equipmentId", getIntent().getStringExtra("equipment_id"));
        Log.i("borrow designation", (String)borrowDesignationSpinner.getSelectedItem());
        Log.i("borrow purpose", purposeTxtView.getText().toString());
        Log.i("borrow userId", sessionManager.getUserId());
        borrowEquipmentTask.execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class BorrowEquipmentTask extends AsyncTask<String, Void, Void> {

        private Context context;

        private final String equipmentID;

        private String borrowDesignation;

        private String purpose;

        public BorrowEquipmentTask(Context context, final String equipmentID, String borrowDesignation, String purpose) {
            this.context = context;
            this.equipmentID = equipmentID;
            this.borrowDesignation = borrowDesignation;
            this.purpose = purpose;
        } //·

        @Override
        protected Void doInBackground(String... strings) {

            String getUrl = Environment.ROOT_PATH + "/api/v1/borrow-equipment";
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, getUrl,
                    response -> {
                        Log.i("BorrowEquipmentTask", response);

                        int id = Integer.parseInt(response);

                        if( id > 0 ) {
                            Intent borrowLandingActivity = new Intent(context, BorrowLandingActivity.class);
                            borrowLandingActivity.putExtra("equipment_id", equipmentID);
                            startActivity(borrowLandingActivity);
                        }

                    }, Throwable::printStackTrace){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<>();
                    params.put("to_designation", borrowDesignation);
                    params.put("equipment_id", equipmentID);
                    params.put("user_id", sessionManager.getUserId());
                    params.put("purpose", purpose);
                    return params;
                }
            };

            requestQueue.add(stringRequest);

            return null;
        }
    }

    public class GetDesignationsTask extends AsyncTask<String, Void, Void> {

        private Context context;

        private String currentDesignation;

        public GetDesignationsTask(Context context, String currentDesignation) {
            this.context = context;
            this.currentDesignation = currentDesignation;
        } //·

        @Override
        protected Void doInBackground(String... strings) {
            String getUrl = Environment.ROOT_PATH + "/api/v1/getDesignations?current=" + currentDesignation;
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, getUrl,
                    response -> {
                        Log.i("GetDesignationsTask", response);
                        try {
                            JSONArray designationsArray = new JSONArray(response);
                            List<String> designations = new ArrayList<>();

                            for( int i = 0; i < designationsArray.length(); i++ ) {
                                if( !((JSONObject)designationsArray.get(i)).getString("designation_name").equalsIgnoreCase( currentDesignation ) ) {
                                    designations.add( ((JSONObject)designationsArray.get(i)).getString("designation_name") );
                                }
                            }

                            ArrayAdapter<String> designationsAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, designations);
                            designationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            borrowDesignationSpinner.setAdapter(designationsAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }, Throwable::printStackTrace){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<>();

                    return params;
                }
            };

            requestQueue.add(stringRequest);

            return null;
        }
    }
}