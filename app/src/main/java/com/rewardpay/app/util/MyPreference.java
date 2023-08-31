package com.rewardpay.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MyPreference {

    private static SharedPreferences getPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static SharedPreferences.Editor getEdit(Context context) {
        return getPreference(context).edit();
    }

    public static void saveIdToken(Context context, int userId, String securityToken) {
        SharedPreferences.Editor edit = getEdit(context);
        edit.putInt("USER_ID", userId);
        edit.putString("SECURITY_TOKEN", securityToken);
        edit.apply();
        edit.commit();
    }

    public static void saveUpdatedName(Context context, String name) {
        SharedPreferences.Editor edit = getEdit(context);
        edit.putString("UPDATED_NAME", name);
        edit.apply();
        edit.commit();
    }

    public static void saveAppOpenDetails(Context context, String socialEmail, Boolean force_update, String SocialImgUrl, String socialName, String appUrl) {
        SharedPreferences.Editor edit = getEdit(context);
        edit.putString("SOCIAL_EMAIL", socialEmail);
        edit.putBoolean("FORCE_UPDATE", force_update);
        edit.putString("SOCIAL_IMAGE", SocialImgUrl);
        edit.putString("SOCIAL_NAME", socialName);
        edit.putString("RATE_US", appUrl);
        edit.apply();
        edit.commit();
    }

    public static String getUpdatedName(Context context) {
        SharedPreferences preferences = getPreference(context);
        return preferences.getString("UPDATED_NAME", "");
    }
    public static String getUserName(Context context) {
        SharedPreferences preferences = getPreference(context);
        return preferences.getString("SOCIAL_NAME", "");
    }

    public static String getUserEmail(Context context) {
        SharedPreferences preferences = getPreference(context);
        return preferences.getString("SOCIAL_EMAIL", "");
    }

    public static String getUserImage(Context context) {
        SharedPreferences preferences = getPreference(context);
        return preferences.getString("SOCIAL_IMAGE", "");
    }

    public static String getAppUpdate(Context context) {
        SharedPreferences preferences = getPreference(context);
        return preferences.getString("RATE_US", "");
    }

    public static int getUserId(Context context) {
        SharedPreferences preferences = getPreference(context);
        return preferences.getInt("USER_ID", -1);
    }

    public static String getSecurityToken(Context context) {
        SharedPreferences preferences = getPreference(context);
        return preferences.getString("SECURITY_TOKEN", "");
    }

}
