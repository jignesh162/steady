package com.steady.nifty.strategy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncryptPassword {
    public static void main(String[] args) throws IOException {
        System.out.println("Enter password and press enter:");
        BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode(obj.readLine()));
    }
}
