package com.std.stdmall.randomHexGenerator;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.math.BigInteger;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

public class RandomHexGenerator {
        public static void main(String[] args) {
        Key secureKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String encodedSecureKey = Base64.getEncoder().encodeToString(secureKey.getEncoded());
        System.out.println("Generated Secure HS512 Key (Base64 Encoded): " + encodedSecureKey);
        System.out.println("Generated Secure HS512 Key Length (bytes): " + secureKey.getEncoded().length);
        System.out.println("Generated Secure HS512 Key Length (bits): " + (secureKey.getEncoded().length * 8));
    }
}
