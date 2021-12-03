package com.app.medicalqrcodeinventorysystem.scanner;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.medicalqrcodeinventorysystem.R;
import com.app.medicalqrcodeinventorysystem.borrowreturn.BorrowEquipmentActivity;
import com.app.medicalqrcodeinventorysystem.borrowreturn.ReturnEquipmentActivity;
import com.app.medicalqrcodeinventorysystem.dto.EquipmentDTO;
import com.app.medicalqrcodeinventorysystem.environment.Environment;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QRCodeScannerActivity extends AppCompatActivity {
    private CodeScanner codeScanner;

//    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code_scanner_activity);

//        sessionManager = new SessionManager(getApplicationContext());

        if( getSupportActionBar() != null ) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        }


        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this, scannerView);
        codeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
//            Toast.makeText(QRCodeScannerActivity.this, result.getText(), Toast.LENGTH_SHORT).show();

            Log.i("Scanning result", result.getText());
            final String url =  Environment.ROOT_PATH + "/api/v1/getEquipment?qrCodeString=" + result.getText();
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {
                        Log.i("GetEquipment", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            if( jsonArray.length() > 0 ) {
                                JSONObject equipmentObject = (JSONObject)jsonArray.get(0);

                                EquipmentDTO equipmentDTO = new EquipmentDTO(equipmentObject.getString("equipment_id"),
                                        equipmentObject.getString("model_number"),
                                        equipmentObject.getString("equipment_type"), equipmentObject.getString("designation_name") );

                                Log.i("GetEquipment", Objects.isNull(equipmentObject.getInt("status_id"))+"");
                                if( equipmentObject.getInt("status_id") == 1 ) {
                                    Intent borrowIntent = new Intent(getApplicationContext(), BorrowEquipmentActivity.class);
                                    borrowIntent.putExtra("equipment_id", equipmentDTO.getEquipmentId());
                                    borrowIntent.putExtra("model_number", equipmentDTO.getModelNumber());
                                    borrowIntent.putExtra("equipment_type", equipmentDTO.getEquipmentType());
                                    borrowIntent.putExtra("designation_name", equipmentDTO.getDesignationName());
                                    startActivityForResult(borrowIntent, 100);
                                }else {

                                    GetBorrowedEquipmentTask getBorrowedEquipmentTask = new GetBorrowedEquipmentTask(getApplicationContext(), equipmentDTO);
                                    getBorrowedEquipmentTask.execute();
                                }

                            }else {
                                Toast.makeText(this, "Invalid QR Code/Data", Toast.LENGTH_SHORT).show();;
                            }

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


        }));

        codeScanner.startPreview();
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public class GetBorrowedEquipmentTask extends AsyncTask<String, Void, Void> {

        private Context context;

        private EquipmentDTO equipmentDTO;



        public GetBorrowedEquipmentTask(Context context, EquipmentDTO equipmentDTO) {
            this.context = context;
            this.equipmentDTO = equipmentDTO;
        } //·

        @Override
        protected Void doInBackground(String... strings) {
            String getUrl = Environment.ROOT_PATH + "/api/v1/getBorrowedEquipment?equipmentId=" + equipmentDTO.getEquipmentId();
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, getUrl,
                    response -> {
                        Log.i("GetBorrowedEquipmentTask", response);
                        try {
                            JSONArray borrowedEquipmentArray = new JSONArray(response);

                            if( borrowedEquipmentArray.length() > 0 ) {
                                JSONObject borrowedEquipmentObject = (JSONObject)borrowedEquipmentArray.get(0);

                                Intent returnEquipmentIntent = new Intent(context, ReturnEquipmentActivity.class);
                                returnEquipmentIntent.putExtra("equipment_id", equipmentDTO.getEquipmentId());
                                returnEquipmentIntent.putExtra("model_number", equipmentDTO.getModelNumber());
                                returnEquipmentIntent.putExtra("equipment_type", equipmentDTO.getEquipmentType());
                                returnEquipmentIntent.putExtra("current_designation", borrowedEquipmentObject.getString("current_designation"));
                                returnEquipmentIntent.putExtra("return_designation", equipmentDTO.getDesignationName());
                                returnEquipmentIntent.putExtra("purpose", borrowedEquipmentObject.getString("purpose"));
                                startActivity(returnEquipmentIntent);

                            }else {
                                Toast.makeText(context, "No existing borrowed equipment for equipment ID " + equipmentDTO.getEquipmentId(), Toast.LENGTH_SHORT).show();;
                            }

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


    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
//        codeScanner.startPreview();
        Log.i("OnPause", "On paused");
    }

}
