package com.RNPermissions;

import android.content.Context;

import java.io.DataOutputStream;
import java.io.File;

/**
 * Declaration:
 * Created by cc
 * Created time 2017/12/21
 */
public class CheckSystemRoot {
    /**
     * 检查手机是否拥有root权限
     *
     * @return
     */
    private static boolean isRoot() {
        boolean root = false;
        try {
            if ((!new File("/system/bin/su").exists())
                    && (!new File("/system/xbin/su").exists())) {
                root = false;
            } else {
                root = true;
            }

        } catch (Exception e) {
        }

        return root;
    }


    /**
     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
     *
     * @param context 命令： String apkRoot="chmod 777 "+getPackageCodePath();
     * @return 应用程序是/否获取Root权限
     */
    private static boolean rootCommand(Context context) {
        String command = "chmod 777 " + context.getPackageCodePath();
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
//            LogUtils.error("ROOT REE" + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
//        LogUtils.error("Root SUC ");
        return true;
    }


    public static boolean checkDeviceDebuggable(){
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
//            LogUtils.error("buildTags="+buildTags);
            return true;
        }
        return false;
    }

    public static boolean checkSuperuserApk(){
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
//                LogUtils.error("/system/app/Superuser.apk exist");
                return true;
            }
        } catch (Exception e) { }
        return false;
    }

    public static boolean checkRoot(Context context) {
        if (checkDeviceDebuggable()
                || checkSuperuserApk()
                || isRoot()
                || rootCommand(context)) {
            return true;
        }
        return false;
    }
}
