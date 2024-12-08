package com.example.projectfix;

import android.app.Activity;

public class BottomNavigationHelper {
    public static void setBottomNavigationVisibility(Activity activity, boolean isVisible) {
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).setBottomNavigationVisibility(isVisible);
        }
    }
}
