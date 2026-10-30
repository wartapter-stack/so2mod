package com.so2.mod;

import android.app.Activity;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class MainActivity extends Activity {

    static final int REQ_OVERLAY = 1;
    static final int REQ_VPN     = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Settings.canDrawOverlays(this)) {
            Intent i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivityForResult(i, REQ_OVERLAY);
        } else {
            requestVpn();
        }
    }

    void requestVpn() {
        Intent i = VpnService.prepare(this);
        if (i != null) {
            startActivityForResult(i, REQ_VPN);
        } else {
            startServices();
        }
    }

    void startServices() {
        startService(new Intent(this, OverlayService.class));
        startService(new Intent(this, ProxyVpnService.class));
        Toast.makeText(this, "Мод активирован", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        if (req == REQ_OVERLAY) {
            requestVpn();
        } else if (req == REQ_VPN) {
            if (res == RESULT_OK) startServices();
        }
    }
}
