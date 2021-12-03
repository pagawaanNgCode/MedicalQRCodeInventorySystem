package com.app.medicalqrcodeinventorysystem.session;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.app.medicalqrcodeinventorysystem.MainActivity;
import com.app.medicalqrcodeinventorysystem.dto.UserDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@SuppressLint("LogNotTimber")
public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREF_NAME = "PNPQRSystem";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)

    public static final String KEY_NAME = "username";

    public static final String KEY_BUILDING = "building";

    private static final String PRODUCT_ORDERS = "product_orders";

    // Constructor
    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */

    public void createLoginSession(UserDTO userDTO) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // Storing name in pref
//        editor.putString(UserDTO.getEmailKey(), userDTO.getEmail());

        editor.putString(UserDTO.getUserIdKey(), userDTO.getUserId());

//        editor.putString(UserDTO.getFirstNameKey(), userDTO.getFirstName());
//
//        editor.putString(UserDTO.getLastNameKey(), userDTO.getLastName());
//
//        editor.putString(UserDTO.getmobilePhoneKey(), userDTO.getMobilePhone());


        // commit changes
        editor.commit();
    }

    public void updateUserDetails( UserDTO userDTO ) {
        final String email = pref.getString(UserDTO.getEmailKey(), null);
        final String firstName = pref.getString(UserDTO.getFirstNameKey(), null);
        final String lastName = pref.getString(UserDTO.getLastNameKey(), null);
        final String mobile = pref.getString(UserDTO.getmobilePhoneKey(), null);

        if( Objects.nonNull(email) || Objects.nonNull(firstName) ||
                Objects.nonNull(lastName) || Objects.nonNull(mobile) ) {
            if( Objects.nonNull(email) ) {
                editor.remove(UserDTO.getEmailKey());
            }

            if( Objects.nonNull(firstName) ) {
                editor.remove(UserDTO.getFirstNameKey());
            }

            if( Objects.nonNull(lastName) ) {
                editor.remove(UserDTO.getLastNameKey());
            }

            if( Objects.nonNull(mobile) ) {
                editor.remove(UserDTO.getmobilePhoneKey());
            }

            editor.commit();
        }

        editor.putString(UserDTO.getEmailKey(), userDTO.getEmail());
        editor.putString(UserDTO.getFirstNameKey(), userDTO.getFirstName());
        editor.putString(UserDTO.getLastNameKey(), userDTO.getLastName());
        editor.putString(UserDTO.getmobilePhoneKey(), userDTO.getMobilePhone());

        editor.commit();
    }



    /**
     * Check login method wil check user login status If false it will redirect
     * user to login page Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    /**
     * Get stored session data
     */
    public UserDTO getUserDetails() {
        UserDTO userDTO = new UserDTO(pref.getString(UserDTO.getUserIdKey(), null), pref.getString(UserDTO.getEmailKey(), null),
                pref.getString(UserDTO.getFirstNameKey(), null), pref.getString(UserDTO.getLastNameKey(), null),
                pref.getString(UserDTO.getmobilePhoneKey(), null)
                );
        return userDTO ;
    }

    public String getUserId() {
        return pref.getString(UserDTO.getUserIdKey(), null);
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}