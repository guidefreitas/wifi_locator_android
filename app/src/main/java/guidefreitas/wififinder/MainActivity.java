package guidefreitas.wififinder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    WifiManager mainWifi;
    WifiReceiver receiverWifi;

    private final Handler handler = new Handler();
    ArrayList<WifiInfo> wifis = new ArrayList<WifiInfo>();

    private TextView textView;
    private EditText roomEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        receiverWifi = new WifiReceiver();
        registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        if(mainWifi.isWifiEnabled()==false)
        {
            mainWifi.setWifiEnabled(true);
        }

        doInback();

        textView = (TextView) findViewById(R.id.textView);
        roomEt = (EditText) findViewById(R.id.roomEt);

        Button btSave = (Button) findViewById(R.id.btSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveWifiData();
            }
        });
    }

    public void doInback()
    {
        handler.postDelayed(new Runnable() {

            @Override
            public void run()
            {
                mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

                receiverWifi = new WifiReceiver();
                registerReceiver(receiverWifi, new IntentFilter(
                        WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                mainWifi.startScan();
                doInback();
            }
        }, 1000);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Refresh");
        return super.onCreateOptionsMenu(menu);}

    @Override
    public void onClick(View v) {
        final ActionBar bar = getSupportActionBar();
        int flags = 0;
        switch (v.getId()) {

        }
    }

    @Override
    protected void onPause()
    {
        unregisterReceiver(receiverWifi);
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setTextView(String msg){
        System.out.println(msg);
        textView.setText(msg);
    }

    public void saveWifiData(){
        String filename = "wifi_data.txt";
        String root = Environment.getExternalStorageDirectory().toString();
        String filePath = root + "/";
        System.out.println("File: " + filePath + filename);
        File file = new File(filePath, filename);

        String room = roomEt.getText().toString();
        String point = UUID.randomUUID().toString();

        FileOutputStream stream;
        PrintWriter writer;
        try {
            stream = new FileOutputStream(file, true);
            writer = new PrintWriter(stream);
            String prefix = room + "," + point + ",";
            for(WifiInfo info : wifis){
                String data = prefix + info.toPipe();
                writer.println(data);
            }
            writer.close();
            Toast.makeText(this, "Added successfully", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class WifiReceiver extends BroadcastReceiver
    {
        public void onReceive(Context c, Intent intent)
        {

            wifis.clear();

            List<ScanResult> wifiList;
            wifiList = mainWifi.getScanResults();
            for(int i = 0; i < wifiList.size(); i++)
            {
                ScanResult wifi =  wifiList.get(i);
                WifiInfo info = new WifiInfo();
                info.setBssid(wifi.BSSID);
                info.setSignal(wifi.level);
                info.setSsid(wifi.SSID);
                info.setFrequency(wifi.frequency);
                wifis.add(info);
            }

            setTextView("Access points found: " + wifis.size());


        }
    }
}


