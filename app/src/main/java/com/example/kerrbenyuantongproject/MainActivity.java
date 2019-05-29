package com.example.kerrbenyuantongproject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.TransformerConfigurationException;

import fragment.MapFragment;
import fragment.TCP;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public LocationClient locationClient = null;
    private TextView positionText = null;
    public Fragment fragment = null;
    public String content = "0";
    private FloatingActionButton button = null;
    TCP tcp ;
    Socket socket;
    String lastContent ="0";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPermission();//获取权限
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        fragment = new MapFragment();
        replaceFragment(fragment);
        button = (FloatingActionButton)findViewById(R.id.button);
        button.setOnClickListener(this);
        tcp = new TCP();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{socket = new Socket("193.112.52.121",9997);}catch (Exception e){e.printStackTrace();}
            }
        }).start();


    }

    public void initPermission(){
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }


        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }else {

        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    for (int result:grantResults){
                        if (result!= PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须有权限才可运行此程序",Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }
                    }
                }else {
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
                default:
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.map_fragment,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                //悬浮按钮 点击进入QR扫描
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                tcp.sendMsg("p",socket);
                startActivityForResult(intent, 111);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==111&&resultCode==RESULT_OK){
            content = data.getStringExtra(Constant.CODED_CONTENT);
            Log.w( "onActivityResult: ", "扫描到+"+content);
            if(content!=lastContent){//和上一次扫的箱号不同时发送此num
                        Log.w( "onActivityResult: ", "扫描到+"+content);
                        tcp.sendMsg(content,socket);
                        lastContent = content;
                        Toast.makeText(this,"打开"+content+"箱门",Toast.LENGTH_LONG).show();
            }
        }
    }


}
