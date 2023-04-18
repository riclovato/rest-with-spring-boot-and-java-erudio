package com.ricklovato.erudio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.HashMap;
import java.util.Map;


@SpringBootApplication
public class Startup {

	public static void main(String[] args) {
		SpringApplication.run(Startup.class, args);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		String result1 = passwordEncoder.encode("admin123");
		String result2 = passwordEncoder.encode("admin234");
		System.out.println("My hash result1 " + result1);
		System.out.println("My hash result2 " + result2);

}}