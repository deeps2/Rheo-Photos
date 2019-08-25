package com.example.rheophotos.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.example.rheophotos.base.MyApplication;
import com.example.rheophotos.model.SearchResponse;
import com.google.gson.Gson;

public class AppUtils {

    private static final Gson gson = new Gson();

    public static boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
            return null != connectivityManager.getActiveNetworkInfo() && connectivityManager.getActiveNetworkInfo().isConnected();
        else
            return false;
    }

    public static void changeStatusBarColor(Activity activity, int color) {
        if (activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, color));
        }
    }

    public static void showKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);

    }

    public static void hideKeyboard(Activity context) {
        View view = context.getWindow().getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null && imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void setImageViewHeight(ImageView imageView, SearchResponse.Results result, int width) {
        int imageWidth = result.getThumbnailMeta().getWidth();
        int imageHeight = result.getThumbnailMeta().getHeight();
        float aspectRatio = (float) imageWidth / imageHeight;
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = (int) (width / aspectRatio);
        imageView.setLayoutParams(layoutParams);
    }

    @SuppressWarnings("unchecked")
    public static Object getObject(String responseString, Class objectClass) {
        try {
            return gson.fromJson(responseString, objectClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getSerialisedStringFromObject(Object obj) {
        try {
            return gson.toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Drawable getAppDrawable(int resID, Context context) {
        return ContextCompat.getDrawable(context, resID);
    }

    public static String getAppString(int resID, Context context) {
        return context.getResources().getString(resID);
    }

}
