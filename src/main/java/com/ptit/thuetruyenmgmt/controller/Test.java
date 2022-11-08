package com.ptit.thuetruyenmgmt.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        LocalDateTime from = LocalDateTime.of(2022,11,7,23,10);
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(from, now);

        long hours = duration.toHours();
        System.out.println(hours);
        System.out.println((double) 13/24);
        double day = (double) 13/24;
        double amount = 500;
        double total = amount * day;
        System.out.println(total);
    }
}
