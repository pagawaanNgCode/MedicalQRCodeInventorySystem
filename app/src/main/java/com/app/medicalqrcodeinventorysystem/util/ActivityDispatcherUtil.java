package com.app.medicalqrcodeinventorysystem.util;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.app.medicalqrcodeinventorysystem.borrowreturn.BorrowEquipmentActivity;
import com.app.medicalqrcodeinventorysystem.dto.EquipmentDTO;

public class ActivityDispatcherUtil  extends AppCompatActivity {

    public static void goToWithEquipment(View view, EquipmentDTO equipmentDTO, Context context, Class<? extends AppCompatActivity> clazz) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra("equipment_id", equipmentDTO.getEquipmentId());
        intent.putExtra("model_number", equipmentDTO.getModelNumber());
        intent.putExtra("equipment_type", equipmentDTO.getEquipmentType());
        intent.putExtra("designation_name", equipmentDTO.getDesignationName());
        context.startActivity( intent );
    }
}
