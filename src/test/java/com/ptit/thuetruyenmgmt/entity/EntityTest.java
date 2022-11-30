package com.ptit.thuetruyenmgmt.entity;


interface EntityTest {

    /**
     * GIVEN
     */
    void initData() throws NoSuchFieldException;


    void testFullArgConstructor() throws IllegalAccessException;


    void testBuilder() throws IllegalAccessException;


    void testSetters() throws IllegalAccessException;


    void testGetters() throws IllegalAccessException;


    void testToString() throws IllegalAccessException;


    void testEquals() throws NoSuchFieldException, IllegalAccessException;


    void testHashCode();

}
