// IBookManager.aidl
package com.example.wb.aidltest;

// Declare any non-default types here with import statements
import com.example.wb.aidltest.Book;
import com.example.wb.aidltest.IOnNewBookArrviedListener;
interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    List<Book> getBookList();
    void addBook(in Book book);
    void registListener(IOnNewBookArrviedListener listener);
    void unregistListener(IOnNewBookArrviedListener listener);
}
