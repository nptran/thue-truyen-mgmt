package com.ptit.thuetruyenmgmt.controller;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Integer> ints = new ArrayList<>();
        ints.add(1);
        ints.add(2);
        ints.add(3);
        int counter = 0;
        for (Integer i : ints) {
            counter++;
            System.out.println(i);
        }
        System.out.println(counter);
    }
}
