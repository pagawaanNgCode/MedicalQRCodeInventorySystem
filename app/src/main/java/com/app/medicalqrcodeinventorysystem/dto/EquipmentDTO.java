package com.app.medicalqrcodeinventorysystem.dto;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class EquipmentDTO {

    private String equipmentId;

    private String modelNumber;

    private String equipmentType;

    private String designationName;

    private String status;

    private LocalDateTime dateBorrowed;

    private LocalDateTime dateReturned;

    private String borrowedBy;

    public EquipmentDTO(String equipmentId, String modelNumber, String equipmentType, String designationName) {
        this.equipmentId = equipmentId;
        this.modelNumber = modelNumber;
        this.equipmentType = equipmentType;
        this.designationName = designationName;
    }

    public EquipmentDTO(String equipmentId, String modelNumber, String equipmentType, String designationName, String status) {
        this.equipmentId = equipmentId;
        this.modelNumber = modelNumber;
        this.equipmentType = equipmentType;
        this.designationName = designationName;
        this.status = status;
    }

    public EquipmentDTO(String equipmentId, String modelNumber, String equipmentType, String designationName, LocalDateTime dateBorrowed, LocalDateTime dateReturned) {
        this.equipmentId = equipmentId;
        this.modelNumber = modelNumber;
        this.equipmentType = equipmentType;
        this.designationName = designationName;
        this.dateBorrowed = dateBorrowed;
        this.dateReturned = dateReturned;
    }

    public EquipmentDTO(String equipmentId, String modelNumber, String equipmentType, String designationName, LocalDateTime dateBorrowed, LocalDateTime dateReturned, String borrowedBy) {
        this.equipmentId = equipmentId;
        this.modelNumber = modelNumber;
        this.equipmentType = equipmentType;
        this.designationName = designationName;
        this.dateBorrowed = dateBorrowed;
        this.dateReturned = dateReturned;
        this.borrowedBy = borrowedBy;
    }

    public EquipmentDTO(String equipmentId, String modelNumber, String equipmentType, String designationName, String status, LocalDateTime dateBorrowed, String borrowedBy) {
        this.equipmentId = equipmentId;
        this.modelNumber = modelNumber;
        this.equipmentType = equipmentType;
        this.designationName = designationName;
        this.status = status;
        this.dateBorrowed = dateBorrowed;
        this.borrowedBy = borrowedBy;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public LocalDateTime getDateBorrowed() {
        return dateBorrowed;
    }

    public void setDateBorrowed(LocalDateTime dateBorrowed) {
        this.dateBorrowed = dateBorrowed;
    }

    public LocalDateTime getDateReturned() {
        return dateReturned;
    }

    public void setDateReturned(LocalDateTime dateReturned) {
        this.dateReturned = dateReturned;
    }

    public String getBorrowedBy() {
        return borrowedBy;
    }

    public void setBorrowedBy(String borrowedBy) {
        this.borrowedBy = borrowedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
