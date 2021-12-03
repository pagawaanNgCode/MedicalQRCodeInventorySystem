package com.app.medicalqrcodeinventorysystem.dto;

import org.json.JSONException;
import org.json.JSONObject;

public class UserDTO {

    private String userId;

    private String email;

    private String firstName;

    private String lastName;

    private String mobilePhone;

    public UserDTO(String userId) {
        this.userId = userId;
    }

    public UserDTO(String userId, String email, String firstName, String lastName, String mobilePhone) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobilePhone = mobilePhone;
    }

    public UserDTO(JSONObject userInfo ) throws JSONException {
        this( userInfo.getString("user_id"), userInfo.getString("email"), userInfo.getString("first_name"), userInfo.getString("last_name"),
                userInfo.getString("mobile_phone") );
    }

    public static String getUserIdKey() {
        return "userId";
    }

    public static String getEmailKey() {
        return "email";
    }

    public static String getFirstNameKey() {
        return "firstName";
    }

    public static String getLastNameKey() {
        return "lastName";
    }

    public static String getmobilePhoneKey() {
        return "mobilePhone";
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }



    @Override
    public String toString() {
        return "UserDTO{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                '}';
    }
}
