package com.parkwoodrx.fastrx.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class PasswordGenerator {

	private final String SALT = "%$*!@&^@&*(@__)(";

	public String generatePassword() {
		// A strong password has Cap_chars, Lower_chars,
		// numeric value and symbols. So we are using all of
		// them to generate our password
		int passwordLength = 8;
		String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String Small_chars = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";
		String values = Capital_chars + Small_chars + numbers;

		// Using random method
		Random rndm_method = new Random();

		char[] password = new char[passwordLength];

		for (int i = 0; i < passwordLength; i++) {
			// Use of charAt() method : to get character value
			// Use of nextInt() as it is scanning the value as int
			password[i] = values.charAt(rndm_method.nextInt(values.length()));

		}
		String passwordString = String.valueOf(password);
		return passwordString;
	}

	public String generateHash(String input) throws NoSuchAlgorithmException {
		input = SALT + input;
		StringBuilder hash = new StringBuilder();

		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			byte[] hashedBytes = sha.digest(input.getBytes());
			char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
			for (int idx = 0; idx < hashedBytes.length; ++idx) {
				byte b = hashedBytes[idx];
				hash.append(digits[(b & 0xf0) >> 4]);
				hash.append(digits[b & 0x0f]);
			}
		} catch (NoSuchAlgorithmException e) {
			throw e;
		}

		return hash.toString();
	}

	public String getEncodedString(String string) {
		if (string == null) {
			return null;
		} else {
			return new String(Base64.getEncoder().encode(string.getBytes(StandardCharsets.UTF_8)));
		}
	}

	public String getDecodedString(String string) {
		if (string == null) {
			return null;
		} else {
			return new String(Base64.getDecoder().decode(string));
		}
	}

}
