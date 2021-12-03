package com.app.medicalqrcodeinventorysystem.equipments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.medicalqrcodeinventorysystem.R;
import com.app.medicalqrcodeinventorysystem.adapters.EquipmentAdapter;
import com.app.medicalqrcodeinventorysystem.dto.EquipmentDTO;
import com.app.medicalqrcodeinventorysystem.environment.Environment;
import com.app.medicalqrcodeinventorysystem.scanner.QRCodeScannerActivity;
import com.app.medicalqrcodeinventorysystem.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipmentListActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_list);

        sessionManager = new SessionManager(getApplicationContext());

        EquipmentListRetrievalTask equipmentListRetrievalTask = new EquipmentListRetrievalTask(getApplicationContext(), "");
        equipmentListRetrievalTask.execute();
    }

    public void scan(View view) {
        Intent scanActivity = new Intent(this, QRCodeScannerActivity.class);
        startActivity(scanActivity);
    }

    public void logout(View view) {
        sessionManager.logoutUser();
    }

    public class EquipmentListRetrievalTask extends AsyncTask<String, Void, Void> {

        private Context context;

        private String filter;

        public EquipmentListRetrievalTask(Context context, String filter) {
            this.context = context;
            this.filter = filter;
        }

        @Override
        protected Void doInBackground(String... strings) {
            // TODO add search
            String postUrl = Environment.ROOT_PATH + "/api/v1/getAllEquipments";
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, postUrl,
                    response -> {
                        Log.i("EquipmentListRetrievalTask", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            List<EquipmentDTO> equipmentData = new ArrayList<>();
                            RecyclerView recyclerView = findViewById(R.id.equipmentRecyclerView);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));

                            for( int index = 0; index < jsonArray.length(); index++ ) {
                                JSONObject equipmentObject = jsonArray.getJSONObject(index);
                                equipmentData.add( new EquipmentDTO( equipmentObject.getInt("equipment_id")+"",
                                                                    equipmentObject.getString("model_number"),
                                                                    equipmentObject.getString("equipment_type"),
                                                                    equipmentObject.getString("designation_name"),
                                                                    equipmentObject.getString("status") ) );
                            }

                            EquipmentAdapter equipmentAdapter = new EquipmentAdapter(equipmentData, EquipmentListActivity.this);
                            recyclerView.setAdapter(equipmentAdapter);
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