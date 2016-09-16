package org.qwan.energy.utils;

import android.app.Application;
import android.content.Context;


public class AppGlobals extends Application {

    public static final String KEY_ER_USERID = "userid";
    public static final String KEY_ER_MEMBERID = "memberid";
    public static final String KEY_ER_SESSIONID = "sessionid";
    public static final String KEY_ER_EMAIL = "email";

    private static Context sContext;
    public static final String USER_LOGIN_KEY = "user_login_key";
    public static final String KEY_USERNAME = "username ";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USER_ID = "user_id";

    //public static final String BASE_URL = "http://tode.ca/tode_rest_php/";
    // office ip: 172.16.21.57
    // home ip: 192.168.0.15
    public static final String BASE_URL = "http://192.168.0.15:1718/Brazuca/";
    //http://tode.ca/tode_rest_php/r_sa_user_select.php?username=raja&password=test
    //http://localhost:1718/Brazuca/energy/login
    //http://172.16.21.57:1718/Brazuca/energy/login

    public static final String LOGIN_URL = String.format("%senergy/login?", BASE_URL);
    public static final String GET_USERINFO_URL = String.format("%s"+"energy/get/userinfo?", BASE_URL);
    public static final String ADD_CREDIT_URL = String.format("%s"+"energy/add/credit?", BASE_URL);
    public static final String SEND_IMAGES_URL = String.format("%s"+"r_sa_add_food_entry.php", BASE_URL);

    public static final String REGISTER_URL = String.format("%sr_sa_registeruser.php", BASE_URL);
    public static final String NO_INTERNET_TITLE = "Info";
    public static final String NO_INTERNET_MESSAGE = "Internet not available, Cross check your " +
            "internet connectivity and try again";
    public static final String SUCCESS_TITLE = "Success";
    public static final String SUCCESS_MESSAGE = "Please login with your credentials";
    public static final String USER_EXIST = "Info";
    public static final String USER_EXIST_MESSAGE = "User already exist \nPlease select another username";
    public static boolean sRegisterStatus = false;
    public static final String GET_IMAGES_URL = "http://tode.ca/tode_rest_php" +
            "/r_sa_show_food_entries.php?userid=";
    public static final String IMAGES_LOCATION = "http://tode.ca/tode_rest_php/food_uploads/";


    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
}
