// BookAidlInterface.aidl
package com.dai.utils.aidl;


// Declare any non-default types here with import statements
import com.dai.utils.aidl.Book;

interface BookAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     List<Book> getBooks();
     String getName();
     void addBook(in Book book);
}