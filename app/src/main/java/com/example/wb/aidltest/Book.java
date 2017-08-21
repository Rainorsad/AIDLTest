package com.example.wb.aidltest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zhangchen on 2017/8/18.
 */

public class Book implements Parcelable{
    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", user='" + user + '\'' +
                '}';
    }

    private int name;
    private String user;

    public Book(int name, String user) {
        this.name = name;
        this.user = user;
    }

    protected Book(Parcel in) {
        name = in.readInt();
        user = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(name);
        dest.writeString(user);
    }
}
