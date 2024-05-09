package com.lzf.androidid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import org.w3c.dom.Text;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // Android Id
    public String getAndroidId(Context context) {
        return "\r\n\t\t" + Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID) + "\r\n\t\t" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    // SERIAL
    private String getSerial() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Build.getSerial();
        } else {
            return Build.SERIAL;
        }
    }

    // Mac地址
    private String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) {
                    continue;
                }
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Mac地址-作废
    private String getLocalMac(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String result = "\r\n\t\tMacAddress：" + info.getMacAddress();
        result += "\r\n\t\tBSSID：" + info.getBSSID();
        result += "\r\n\t\tIpAddress：" + info.getIpAddress();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            result += "\r\n\t\tPasspointFqdn：" + info.getPasspointFqdn();
        }
        result += "\r\n\t\tSSID：" + info.getSSID();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            result += "\r\n\t\tPasspointProviderFriendlyName：" + info.getPasspointProviderFriendlyName();
        }
        if (Build.VERSION.SDK_INT >= 33) {
            result += "\r\n\t\tApMloLinkId：" + info.getApMloLinkId();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            result += "\r\n\t\tWifiStandard：" + info.getWifiStandard();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            result += "\r\n\t\tTxLinkSpeedMbps：" + info.getTxLinkSpeedMbps();
        }
        result += "\r\n\t\tSupplicantState：" + info.getSupplicantState();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            result += "\r\n\t\tSubscriptionId：" + info.getSubscriptionId();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            result += "\r\n\t\tRxLinkSpeedMbps：" + info.getRxLinkSpeedMbps();
        }
        result += "\r\n\t\tRssi：" + info.getRssi();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            result += "\r\n\t\tMaxSupportedTxLinkSpeedMbps：" + info.getMaxSupportedTxLinkSpeedMbps();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            result += "\r\n\t\tMaxSupportedRxLinkSpeedMbps：" + info.getMaxSupportedRxLinkSpeedMbps();
        }
        result += "\r\n\t\tNetworkId：" + info.getNetworkId();
        result += "\r\n\t\tLinkSpeed：" + info.getLinkSpeed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            result += "\r\n\t\tApplicableRedactions：" + info.getApplicableRedactions();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            result += "\r\n\t\tCurrentSecurityType：" + info.getCurrentSecurityType();
        }
        result += "\r\n\t\tFrequency：" + info.getFrequency();
        result += "\r\n\t\tHiddenSSID：" + info.getHiddenSSID();
        return result;
    }

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView txtOne = findViewById(R.id.txtOne);
        String result = "";
        result += "\r\n\tAndroidId：" + getAndroidId(getApplicationContext());
        result += "\r\n\tMac：" + getMacAddress();
        result += "\r\n\tMac：" + getLocalMac(getApplicationContext());
        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        result += "\r\n\tTelephony：";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            result += "\r\n\t\tIMEI：" + telManager.getMeid(0);
            result += "\r\n\t\tSimSerialNumber：" + telManager.getSimSerialNumber();
            result += "\r\n\t\tIMSI：" + telManager.getSubscriberId();
            result += "\r\n\t\tDeviceId：" + telManager.getDeviceId();
            //硬件序列号
            result += "\r\n\tSerialNumber：" + getSerial();
        }
        txtOne.setText(result);
    }
}
