package com.mycompany.plugins.example;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

import java.util.Collections;
import java.util.List;

@CapacitorPlugin(
        name = "EsimReader",
        permissions = {
                @Permission(alias = "readPhoneState",   strings = { Manifest.permission.READ_PHONE_STATE }),
                @Permission(alias = "readPhoneNumbers", strings = { Manifest.permission.READ_PHONE_NUMBERS })
        }
)
public class EsimReaderPlugin extends Plugin {

    // -------- hasEsim --------
    @PluginMethod
    public void hasEsim(PluginCall call) {
        boolean supported = false;
        try {
            TelephonyManager tm = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // compileSdk может быть < 28, поэтому только рефлексия
                Object res = TelephonyManager.class.getMethod("isEuiccSupported").invoke(tm);
                if (res instanceof Boolean) supported = (Boolean) res;
            }
        } catch (Throwable ignored) {
            supported = false;
        }
        JSObject obj = new JSObject();
        obj.put("supported", supported);
        call.resolve(obj);
    }

    // -------- getEsimInfo --------
    @PluginMethod
    public void getEsimInfo(PluginCall call) {
        // Нужен READ_PHONE_STATE
        if (!hasPermission(Manifest.permission.READ_PHONE_STATE)) {
            requestPermissionForAlias("readPhoneState", call, "onPhoneStatePermission");
            return;
        }

        JSArray profiles = new JSArray();
        boolean hasEmbedded = false;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                SubscriptionManager sm = (SubscriptionManager) getContext().getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
                List<SubscriptionInfo> list = safeActiveSubscriptions(sm);
                for (SubscriptionInfo info : list) {
                    JSObject item = new JSObject();
                    item.put("carrierName", cs(info.getCarrierName()));
                    item.put("displayName", cs(info.getDisplayName()));
                    item.put("iccId", info.getIccId());
                    item.put("mcc", info.getMcc());
                    item.put("mnc", info.getMnc());
                    item.put("simSlotIndex", info.getSimSlotIndex());
                    item.put("subscriptionId", info.getSubscriptionId());

                    // Номер отдаём только если есть READ_PHONE_NUMBERS (иначе на 13+ может быть SecurityException)
                    if (hasPermission(Manifest.permission.READ_PHONE_NUMBERS)) {
                        try { item.put("number", info.getNumber()); } catch (Throwable ignored) {}
                    } else {
                        item.put("number", (String) null);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        boolean embedded = false;
                        try { embedded = info.isEmbedded(); } catch (Throwable ignored) {}
                        item.put("isEmbedded", embedded);
                        if (embedded) hasEmbedded = true;
                    }

                    profiles.put(item);
                }
            }
        } catch (SecurityException se) {
            JSObject err = new JSObject();
            err.put("error", "SecurityException: " + se.getMessage());
            call.resolve(err);
            return;
        } catch (Throwable ignored) {
            // глотаем любые OEM-странности
        }

        JSObject result = new JSObject();
        result.put("profiles", profiles);
        result.put("esimSupported", hasEmbedded);        // наличие eSIM-профиля (Android 10+)
        result.put("profilesCount", profiles.length());
        call.resolve(result);
    }

    @PermissionCallback
    private void onPhoneStatePermission(PluginCall call) {
        if (getPermissionState("readPhoneState") == com.getcapacitor.PermissionState.GRANTED) {
            // Параллельно запросим номер (не обязателен)
            if (!hasPermission(Manifest.permission.READ_PHONE_NUMBERS)) {
                requestPermissionForAlias("readPhoneNumbers", call, "onPhoneNumbersPermission");
                return;
            }
            getEsimInfo(call);
        } else {
            call.reject("READ_PHONE_STATE permission denied");
        }
    }

    @PermissionCallback
    private void onPhoneNumbersPermission(PluginCall call) {
        // Если дали — вернём номер; если нет — тоже ок, просто без номера
        getEsimInfo(call);
    }

    // ---------- helpers ----------
    @Override
    public boolean hasPermission(String perm) {
        return ActivityCompat.checkSelfPermission(getContext(), perm) == PackageManager.PERMISSION_GRANTED;
    }


    private static List<SubscriptionInfo> safeActiveSubscriptions(SubscriptionManager sm) {
        if (sm == null) return Collections.emptyList();
        try {
            List<SubscriptionInfo> list = sm.getActiveSubscriptionInfoList();
            return list != null ? list : Collections.emptyList();
        } catch (Throwable t) {
            return Collections.emptyList();
        }
    }

    private static String cs(CharSequence cs) {
        return cs != null ? cs.toString() : null;
    }
}
