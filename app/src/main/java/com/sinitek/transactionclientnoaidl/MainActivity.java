package com.sinitek.transactionclientnoaidl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    IBinder mBinder;
    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.activity_main_operate_tv);
        Intent intent = new Intent("com.sinitek.noaidl.myservice");
        intent.setPackage("com.sinitek.transactionservernoaidl");
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        initEvent();
    }

    private void initEvent() {
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRcvSnd();
            }
        });
    }

    public void mRcvSnd() {

        if (mBinder == null) {
            return;
        }
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        int _code = (int) (Math.random() * 6) % 2;
        int _result;
        try {
            _data.writeInterfaceToken("NoAidlService");//客户端标识
            _data.writeInt(6);
            _data.writeInt(6);
            mBinder.transact(_code, _data, _reply, 0);
            _reply.readException();
            _result = _reply.readInt();
            Toast.makeText(this, "收到回复：" + _result, Toast.LENGTH_SHORT).show();

        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }
}