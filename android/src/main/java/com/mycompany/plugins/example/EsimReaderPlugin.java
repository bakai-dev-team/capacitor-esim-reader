import android.os.Build;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.PluginMethod;

import org.json.JSONObject;

@CapacitorPlugin(name = "EsimReader")
public class EsimReaderPlugin extends Plugin {

    @PluginMethod
    public void hasEsim(PluginCall call) {
        TelephonyManager telephonyManager = (TelephonyManager)
                getContext().getSystemService(getContext().TELEPHONY_SERVICE);

        boolean hasEsim = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            hasEsim = telephonyManager != null && telephonyManager.isEuiccSupported();
        }

        JSObject result = new JSObject();
        result.put("supported", hasEsim);
        call.resolve(result);
    }

    @PluginMethod
    public void getEsimInfo(PluginCall call) {
        String esimInfo = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SubscriptionManager sm = (SubscriptionManager)
                    getContext().getSystemService(getContext().TELEPHONY_SUBSCRIPTION_SERVICE);
            if (sm != null) {
                esimInfo = sm.getActiveSubscriptionInfoList().toString();
            }
        }

        JSObject result = new JSObject();
        result.put("info", esimInfo);
        call.resolve(result);
    }
}
