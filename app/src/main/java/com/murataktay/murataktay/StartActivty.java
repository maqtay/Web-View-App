package com.murataktay.murataktay;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StartActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_activty);

        Thread thread = new Thread(){
            @Override
            public void run(){
                try{
                    synchronized (this){
                        wait(2000);
                    }
                }catch (InterruptedException ignored){

                }finally {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        if(InternetCtrl()){
            thread.start();
        }else{
            AlertDialog.Builder alert = new AlertDialog.Builder(StartActivty.this);
            alert.setTitle("İnternet Bağlantısı Sorunu!");
            alert.setMessage("İnternet bağlantısı yok. Lütfen kontrol edip tekrar deneyin!");
            alert.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.setCancelable(false);
            alert.show();
        }
    }
    public boolean InternetCtrl(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isAvailable() && manager.getActiveNetworkInfo().isConnected();
    }
}