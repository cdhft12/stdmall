package com.std.stdmall.randomHexGenerator;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RandomHexGenerator {
    public static String generateSecureRandomHex(int byteLength) {
        SecureRandom secureRandom = new SecureRandom(); // 암호학적으로 강력한 난수 생성기
        byte[] randomBytes = new byte[byteLength];
        secureRandom.nextBytes(randomBytes); // 지정된 길이만큼 난수 바이트 생성

        // 바이트 배열을 16진수 문자열로 변환
        // BigInteger를 사용하여 부호 없는 16진수 문자열로 변환
        // `+ 1`을 하는 이유는 BigInteger가 0부터 시작하는 바이트 배열을 처리할 때
        // 선행 0이 생략될 수 있기 때문에, 항상 양수로 처리하기 위함입니다.
        String hexString = new BigInteger(1, randomBytes).toString(16);

        // 생성된 16진수 문자열의 길이가 원하는 바이트 길이 * 2 (16진수 한 자리가 4비트)와
        // 일치하도록 앞에 0을 채웁니다.
        // 예를 들어, 1바이트 0x0A는 "a"로 변환될 수 있으므로 "0a"로 맞춰야 합니다.
        int expectedLength = byteLength * 2;
        while (hexString.length() < expectedLength) {
            hexString = "0" + hexString;
        }

        return hexString;
    }
    public static void main(String[] args) {
        int byteLength = 32; // openssl rand -hex 32와 동일하게 32바이트
        String hexSecret = generateSecureRandomHex(byteLength);
        System.out.println("Generated Hex Secret (" + byteLength * 8 + " bits): " + hexSecret);
        System.out.println("Length: " + hexSecret.length());

        // JWT secret은 보통 Base64로 인코딩된 문자열을 사용하므로,
        // 필요에 따라 Base64 인코딩을 적용할 수도 있습니다.
        // byte[] decodedBytes = new BigInteger(hexSecret, 16).toByteArray(); // 16진수를 바이트로 다시 변환 (부호 문제 주의)
        // String base64Secret = Base64.getEncoder().encodeToString(randomBytes); // 위에서 생성한 randomBytes를 직접 인코딩하는 것이 더 정확
        // System.out.println("Base64 Encoded Secret: " + base64Secret);
    }
}
