package com.epam.training.dao.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Encoder;

public class MyEncoder {
	public static String encode(String pwd) {
		try {
			MessageDigest md = MessageDigest.getInstance("Encoder");
			byte[] digest = md.digest(pwd.getBytes());
			Encoder encoder = Base64.getEncoder();
			// System.out.println(encoder.encode(digest).toString());
			return encoder.encode(digest).toString();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

}
