package com.example.wb.aidltest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
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
        mBookList.add(new Book(1,"西游记"));
        mBookList.add(new Book(2,"水浒传"));
        new Thread(new ServiceWork()).start();
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

    private class ServiceWork implements Runnable{

        @Override
        public void run() {
            while (!mIsServiceDestroy.get()){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId,"new Book#" + bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void onNewBookArrived(Book newBook) throws RemoteException{
        mBookList.add(newBook);
        for (int i=0;i<mBookListener.size();i++){
            IOnNewBookArrviedListener listener = mBookListener.get(i);
            listener.addNewBookArrived(newBook);
        }
    }

}
