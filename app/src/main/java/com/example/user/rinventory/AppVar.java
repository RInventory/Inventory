package com.example.user.rinventory;

/**
 * Created by ASUS on 5/8/2018.
 */

public class AppVar {

    //URL to our login.php file, url bisa diganti sesuai dengan alamat server kita
    public static final String LOGIN_URL = "http://192.168.43.206/login.php";

    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_EMAIL = "username";
    public static final String KEY_PASSWORD = "password";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

}