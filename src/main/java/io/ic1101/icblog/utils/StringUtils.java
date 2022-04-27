package io.ic1101.icblog.utils;

import java.util.Random;

public class StringUtils {

    public static String generateRandomPassword(int length) {
        String chars = "0123456789" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz" + "!@#$%&";

        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            stringBuilder.append(chars.charAt(random.nextInt(chars.length())));
        }

        return stringBuilder.toString();
    }
}
