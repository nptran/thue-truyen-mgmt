package com.ptit.thuetruyenmgmt.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Test {
    public static void main(String[] args) {
        List<Integer> ids = IntStream.range(3, 6).mapToObj(i -> i).collect(Collectors.toList());
        for (Integer i : ids) {
            System.out.println(i);
        }
    }
}
