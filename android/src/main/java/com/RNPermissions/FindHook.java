//package com.xyd.xydlib.utils;
package com.RNPermissions;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Declaration:
 * 检测native hook框架：Cydia Substrate、Xposed
 * Created by cc
 * Created time 2017/12/21
 */
public class FindHook {
    private static boolean findHookAppName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> applicationInfoList = packageManager
                .getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo applicationInfo : applicationInfoList) {
            if (applicationInfo.packageName.equals("de.robv.android.xposed.installer")) {
//                LogUtils.error("Xposed found on the system.");
                return true;
            }
            if (applicationInfo.packageName.equals("com.saurik.substrate")) {
//                LogUtils.error("Substrate found on the system.");
                return true;
            }
        }
        return false;
    }

    private static boolean findHookAppFile() {
        try {
            Set<String> libraries = new HashSet<String>();
            String mapsFilename = "/proc/" + android.os.Process.myPid() + "/maps";
            BufferedReader reader = new BufferedReader(new FileReader(mapsFilename));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.endsWith(".so") || line.endsWith(".jar")) {
                    int n = line.lastIndexOf(" ");
                    libraries.add(line.substring(n + 1));
                }
            }
            reader.close();
            for (String library : libraries) {
                if (library.contains("com.saurik.substrate")) {
//                    LogUtils.error("Substrate shared object found: " + library);
                    return true;
                }
                if (library.contains("XposedBridge.jar")) {
//                    LogUtils.error("Xposed JAR found: " + library);
                    return true;
                }
            }
        } catch (Exception e) {
//            LogUtils.error(e.toString());
        }
        return false;
    }

    // 1. 如果存在Xposed框架hook
    // （1）在dalvik.system.NativeStart.main方法后出现de.robv.android.xposed.XposedBridge.main调用
    // （2）如果Xposed hook了调用栈里的一个方法，
    // 还会有de.robv.android.xposed.XposedBridge.handleHookedMethod
    // 和de.robv.android.xposed.XposedBridge.invokeOriginalMethodNative调用

    // 2. 如果存在Substrate框架hook
    // （1）dalvik.system.NativeStart.main调用后会出现2次com.android.internal.os.ZygoteInit.main，而不是一次。
    // （2）如果Substrate hook了调用栈里的一个方法，
    // 还会出现com.saurik.substrate.MS$2.invoked，com.saurik.substrate.MS$MethodPointer.invoke还有跟Substrate扩展相关的方法

    private static boolean findHookStack() {
        try {
            throw new Exception("findhook");
        } catch (Exception e) {

            // 读取栈信息
            // for(StackTraceElement stackTraceElement : e.getStackTrace()) {
            // Log.wtf("HookDetection", stackTraceElement.getClassName() + "->"+
            // stackTraceElement.getMethodName());
            // }

            int zygoteInitCallCount = 0;
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                if (stackTraceElement.getClassName().equals("com.android.internal.os.ZygoteInit")) {
                    zygoteInitCallCount++;
                    if (zygoteInitCallCount == 2) {
//                        LogUtils.error("Substrate is active on the device.");
                        return true;
                    }
                }
                if (stackTraceElement.getClassName().equals("com.saurik.substrate.MS$2")
                        && stackTraceElement.getMethodName().equals("invoked")) {
//                    LogUtils.error("A method on the stack trace has been hooked using Substrate.");
                    return true;
                }
                if (stackTraceElement.getClassName().equals("de.robv.android.xposed.XposedBridge")
                        && stackTraceElement.getMethodName().equals("main")) {
//                    LogUtils.error("Xposed is active on the device.");
                    return true;
                }
                if (stackTraceElement.getClassName().equals("de.robv.android.xposed.XposedBridge")
                        && stackTraceElement.getMethodName().equals("handleHookedMethod")) {
//                    LogUtils.error("A method on the stack trace has been hooked using Xposed.");
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isHook(Context context) {
        if (findHookAppName(context) || findHookAppFile() || findHookStack()) {
            return true;
        }
        return false;
    }
}
