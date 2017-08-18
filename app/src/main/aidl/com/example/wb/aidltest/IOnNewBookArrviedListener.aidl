// IOnNewBookArrviedListener.aidl
package com.example.wb.aidltest;

// Declare any non-default types here with import statements
import com.example.wb.aidltest.Book;
interface IOnNewBookArrviedListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void addNewBookArrived(in Book book);
}
