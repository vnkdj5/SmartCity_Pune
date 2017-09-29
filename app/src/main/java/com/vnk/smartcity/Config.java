package com.vnk.smartcity;

/**
 * Created by root on 11/9/17.
 */

public class Config {

    //URL to our login.php file
    public static final String SERVER_URL = "http://192.168.43.208/smartcity";
    public static final String LOGIN_URL = SERVER_URL + "/login.php";
    public static  final String REG_URL=SERVER_URL+"/regnew.php";
    public static final String GET_COMPLAINTS_URL = SERVER_URL + "/get_complaints.php";
    public static final String GET_COMPLAINTS_OFFICER_URL = SERVER_URL + "/get_complaints_officer.php";
    public static final String UPDATE_STATUS_URL = SERVER_URL + "/update_status.php";
    public static final String GET_COMPLAINTS_IDS = SERVER_URL + "/get_complaint_ids.php";
    public static final String GET_COMPLAINT = SERVER_URL + "/get_complaint.php";

    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

    //Keys for Sharedpreferences111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //login type
    public static final String USER_TYPE = "usertype";

    //This would be used to store the username of current logged in user
    public static final String USER_EMAIL_SHARED_PREF = "username";

    public static final String KEY_USER_NAME="name";
    public static final String KEY_USER_ADDRESS="address";
    public static final String KEY_USER_MOBILE="mobile";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";


    //-----------------COMPALINSTS TAGS
    public static final String TAG_IMAGE_URL = "image_url";
    public static final String TAG_CAT_ID = "category";
    public static final String TAG_SUB_CAT = "subcategory";
    public static final String TAG_COMPLAINT_ID = "complaint_id";
    public static final String TAG_ADDRESS = "address";
    public static final String TAG_DESCRIPTION = "description";
    public static final String TAG_STATUS = "status";
    public static final String TAG_SUBMIT_DATE = "submit_date";

}