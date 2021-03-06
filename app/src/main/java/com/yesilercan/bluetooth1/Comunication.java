package com.yesilercan.bluetooth1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class Comunication extends AppCompatActivity {

    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;

    BluetoothSocket btSocket = null;
    BluetoothHidDevice remoteDevice;
    BluetoothServerSocket mmServer;

    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    Button LedOn,LedOff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunication);

        Intent newint = getIntent();
        address = newint.getStringExtra(MainActivity.EXTRA_ADRESS);

        LedOn = findViewById(R.id.led_on);
        LedOff = findViewById(R.id.led_off);

        LedOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btSocket!=null){
                    try {
                        btSocket.getOutputStream().write("1".toString().getBytes());
                    }
                    catch (IOException e){

                    }
                }
            }
        });

        LedOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btSocket!=null){
                    try {
                        btSocket.getOutputStream().write("2".toString().getBytes());
                    }
                    catch (IOException e){

                    }
                }
            }
        });

        new BTbaglan().execute();
    }



    private void Disconnect(){
        if(btSocket!=null){
            try {
                btSocket.close();
            } catch (IOException e){
                // msg("Error");
            }
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Disconnect();
    }

    private class BTbaglan extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(Comunication.this, "Baglan??yor...", "L??tfen Bekleyin");
        }


        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice cihaz = myBluetooth.getRemoteDevice(address);
                    btSocket = cihaz.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!ConnectSuccess) {
                // msg("Baglant?? Hatas??, L??tfen Tekrar Deneyin");
                Toast.makeText(getApplicationContext(),"Ba??lant?? Hatas?? Tekrar Deneyin",Toast.LENGTH_SHORT).show();
                finish();
            } else {
                //   msg("Baglant?? Basar??l??");
                Toast.makeText(getApplicationContext(),"Ba??lant?? Ba??ar??l??",Toast.LENGTH_SHORT).show();

                isBtConnected = true;
            }
            progress.dismiss();
        }

    }

}
