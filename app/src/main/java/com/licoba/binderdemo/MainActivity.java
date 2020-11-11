package com.licoba.binderdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.licoba.binderserver.ICat;

public class MainActivity extends AppCompatActivity {
    private String TAG = "BinderDemo";
    private ICat catService;
    TextView tipText;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "服务连接成功");
            tipText.setText("服务已连接");

            catService = ICat.Stub.asInterface(service); //远程服务返回的是service的代理
//            catService = (ICat) service; //本地服务应该是这个在

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "服务已断开");
            catService = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        EditText editText = findViewById(R.id.editText);
        EditText editText2 = findViewById(R.id.editText2);
        tipText = findViewById(R.id.tipText);
        Intent intent = new Intent();
        // Service里的menifest定义的action  com.licoba.binderserver.action.AIDL_SERCICE
        intent.setAction("com.licoba.binderserver.action.AIDL_SERCICE");
        // service的包名 com.licoba.binderserver
        intent.setPackage("com.licoba.binderserver");
        tipText.setText("正在绑定服务");
        Log.e(TAG, "开始绑定服务");
        bindService(intent, conn, BIND_AUTO_CREATE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    editText.setText(catService.getColor());
                    editText2.setText(catService.getWeight() + " kg");
                } catch (Exception e) {
                    tipText.setText("未获取到Service，检查服务端程序是否启动");
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        tipText.setText("取消绑定服务");
        Log.e(TAG, "取消绑定服务");
        super.onDestroy();
        this.unbindService(conn);
    }
}