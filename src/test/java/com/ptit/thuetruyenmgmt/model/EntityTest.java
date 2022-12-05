package com.ptit.thuetruyenmgmt.model;


public interface EntityTest {

    /**
     * GIVEN
     */
    void initData() throws NoSuchFieldException, IllegalAccessException;


    void testFullArgConstructor() throws IllegalAccessException;


    void testBuilder() throws IllegalAccessException;


    void testSetters() throws IllegalAccessException;


    void testGetters() throws IllegalAccessException;


    void testToString() throws IllegalAccessException;


    void testEquals() throws NoSuchFieldException, IllegalAccessException;


    void testHashCode() throws IllegalAccessException;

}
