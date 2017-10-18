package com.tuturu.spark.mangareader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import layout.FileExplorerRow;

public class MainActivity extends AppCompatActivity
                          implements FileExplorerRow.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFolders();
    }

    private void checkPermission(){
        int perm1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int perm2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (perm1 != PackageManager.PERMISSION_GRANTED || perm2 != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                 Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
    }

    private void loadFolders(){

        checkPermission();

        File storDir = new File(Environment.getExternalStorageDirectory().getPath());

        File[] files = storDir.listFiles();

        for (File file : files){

            FileExplorerRow row = new FileExplorerRow();

            if (file.isDirectory()){
                row.init(FileExplorerRow.ItemType.Folder, file.getName(), file.getPath());
            } else {
                row.init(FileExplorerRow.ItemType.File, file.getName(), file.getPath());
            }

            getSupportFragmentManager().beginTransaction().add(R.id.filesList, row, file.getName()).commit();
        }
    }

    @Override
    public void onFragmentInteraction() {

    }
}