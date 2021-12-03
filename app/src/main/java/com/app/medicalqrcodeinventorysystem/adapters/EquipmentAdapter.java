package com.app.medicalqrcodeinventorysystem.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.medicalqrcodeinventorysystem.R;
import com.app.medicalqrcodeinventorysystem.borrowreturn.ReturnEquipmentActivity;
import com.app.medicalqrcodeinventorysystem.dto.EquipmentDTO;
import com.app.medicalqrcodeinventorysystem.environment.Environment;
import com.app.medicalqrcodeinventorysystem.equipments.EquipmentListActivity;
import com.app.medicalqrcodeinventorysystem.equipments.ViewEquipmentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder> {

    private List<EquipmentDTO> equipmentData;
    private Context context;

    public EquipmentAdapter(List<EquipmentDTO> equipmentData, EquipmentListActivity activity) {
        this.equipmentData = equipmentData;
        this.context = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.equipment_list, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final EquipmentDTO equipmentElement = equipmentData.get(position);

        int equipmentId = Integer.parseInt(equipmentElement.getEquipmentId());
        holder.equipmentModelNumber.setText(equipmentElement.getModelNumber());
        holder.equipmentType.setText(equipmentElement.getEquipmentType());
        holder.equipmentStatus.setText( equipmentElement.getStatus() );
//        holder.currentDesignation.setText(equipmentElement.getDesignationName());
//        holder.borrowedBy.setText( equipmentElement.getBorrowedBy() );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO make equipment page
//                Toast.makeText(context, marketElement.getMarketName() + " : " + marketElement.getMarketId(), Toast.LENGTH_SHORT).show();

                Log.i("onClick status", equipmentElement.getStatus());

                Intent viewEquipmentIntent = new Intent(context, ViewEquipmentActivity.class);
                viewEquipmentIntent.putExtra("equipment_id", equipmentElement.getEquipmentId());
                viewEquipmentIntent.putExtra("model_number", equipmentElement.getModelNumber());
                viewEquipmentIntent.putExtra("equipment_type", equipmentElement.getEquipmentType());
                viewEquipmentIntent.putExtra("status", equipmentElement.getStatus());
                viewEquipmentIntent.putExtra("current_designation", equipmentElement.getDesignationName());

                if( "In-Use".equalsIgnoreCase( equipmentElement.getStatus() ) ) {
                    GetBorrowedEquipmentTask getBorrowedEquipmentTask = new GetBorrowedEquipmentTask( context, equipmentElement );
                    getBorrowedEquipmentTask.execute();
                }else {
                    context.startActivity( viewEquipmentIntent );
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return equipmentData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView equipmentModelNumber;
        TextView equipmentType;
        TextView equipmentStatus;
//        TextView currentDesignation;
//        TextView borrowedBy;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            equipmentModelNumber = itemView.findViewById(R.id.boxModelNumber);
            equipmentType = itemView.findViewById(R.id.boxEquipmentType);
            equipmentStatus = itemView.findViewById(R.id.boxStatus);
//            currentDesignation = itemView.findViewById(R.id.boxCurrentDesignation);
//            borrowedBy = itemView.findViewById(R.id.boxBorrowedBy);
        }
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
                        Log.i("GetBorrowedEquipmentTask_Adapter", response);
                        try {
                            JSONArray borrowedEquipmentArray = new JSONArray(response);

                            if( borrowedEquipmentArray.length() > 0 ) {
                                JSONObject borrowedEquipmentObject = (JSONObject)borrowedEquipmentArray.get(0);

                                Intent viewEquipmentIntent = new Intent(context, ViewEquipmentActivity.class);
                                viewEquipmentIntent.putExtra("equipment_id", equipmentDTO.getEquipmentId());
                                viewEquipmentIntent.putExtra("model_number", equipmentDTO.getModelNumber());
                                viewEquipmentIntent.putExtra("equipment_type", equipmentDTO.getEquipmentType());
                                viewEquipmentIntent.putExtra("status", equipmentDTO.getStatus());
                                viewEquipmentIntent.putExtra("current_designation", borrowedEquipmentObject.getString("current_designation"));
                                viewEquipmentIntent.putExtra("purpose", borrowedEquipmentObject.getString("purpose"));
                                viewEquipmentIntent.putExtra("date_borrowed", borrowedEquipmentObject.getString("date_borrowed"));
                                viewEquipmentIntent.putExtra("borrowed_by", borrowedEquipmentObject.getString("borrowed_by"));

                                context.startActivity( viewEquipmentIntent );

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


}
