package com.brsrker.emerald.jwt.auth.util;

public class Constant {
    public final static String EMAIL_VAL_REGEX = "^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$";
    public final static String PASSWORD_VAL_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";
}
