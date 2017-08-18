package com.example.wb.aidltest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Zhangchen on 2017/8/18.
 */

public class BookManagerService extends Service{

    private static final String TAG = "BMS";

    private AtomicBoolean mIsServiceDestroy = new AtomicBoolean(false);

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<Book>();
    private CopyOnWriteArrayList<IOnNewBookArrviedListener> mBookListener = new CopyOnWriteArrayList<IOnNewBookArrviedListener>();

    private Binder mBinder = new IBookManager.Stub(){

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registListener(IOnNewBookArrviedListener listener) throws RemoteException {
            if (!mBookListener.contains(listener)){
                mBookListener.add(listener);
            }else {
                Log.d("服务端","already exists");
            }
            Log.d("服务端","not found ,can not regist");
        }

        @Override
        public void unregistListener(IOnNewBookArrviedListener listener) throws RemoteException {
            if (mBookListener.contains(listener)){
                mBookListener.remove(listener);
                Log.d("服务端","unregister listener succed");
            }else {
                Log.d("服务端","not found ,can not regist");
            }
            Log.d("服务端","unregisterListener,current size :" + mBookListener.size());
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book("1","Amdroid"));
        mBookList.add(new Book("2","IOS"));
        handler.sendEmptyMessageDelayed(1,1000);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestroy.set(true);
        super.onDestroy();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    while (!mIsServiceDestroy.get()){
                        Book book = new Book("3","水浒传");
                        mBookList.add(book);
                        for (int i=0;i<mBookListener.size();i++) {
                            IOnNewBookArrviedListener listener = mBookListener.get(i);
                            try {
                                listener.addNewBookArrived(book);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    handler.sendEmptyMessageDelayed(1,1000);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };
}
