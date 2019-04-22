package com.albertech.inputdemo.chatoperator.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 权限工具
 */
public class PermissionUtil {

    public static final int ALL_GRANTED = 0;
    public static final int NEED_ASK = 1;
    public static final int DENIED_NEVER_ASK = 2;
    public static final int ERROR = 3;

    public static int checkPermission(Activity activity, String[] peimissions) {
        if (activity == null) {
            return ERROR;
        } else if (peimissions == null) {
            return ERROR;
        } else {
            List<String> deniedPermissionList = new ArrayList<>();
            for (String permission : peimissions) {
                if (!TextUtils.isEmpty(permission)) {
                    if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                        deniedPermissionList.add(permission);
                    }
                }
            }
            if (deniedPermissionList.size() > 0) {
                int neverAskCount = 0;
                for (String permission : deniedPermissionList) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                        ActivityCompat.requestPermissions(activity, peimissions, 13);
                    } else {
                        neverAskCount++;
                    }
                }
                if (neverAskCount > 0) {
                    return DENIED_NEVER_ASK;
                } else {
                    return NEED_ASK;
                }
            } else {
                return ALL_GRANTED;
            }
        }
    }

}
