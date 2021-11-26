package com.pranavprksn.hedp.he;

import java.math.BigInteger;
import java.util.Random;

public class HED {
    String FILENAME = "encryption key location";
    static pailier paillier;
    file fl;
    static BigInteger r;

    public HED() {

        fl = new file();

        paillier = new pailier();
        if (fl.check(FILENAME)) {
            BigInteger[] rValue = fl.getKey(FILENAME);
            paillier.KeyGeneration(512, 62, rValue[0], rValue[1]);
            r = rValue[2];
        } else {
            r = new BigInteger(512, new Random());
            paillier.KeyGeneration(512, 62);
            BigInteger[] temp;
            temp = paillier.getPQ();
            temp[2] = new BigInteger("" + r);
            temp[3] = new BigInteger("" + 100 + (int) (Math.random() * (10000 - 100 + 1)));
            fl.fileWrite(temp, FILENAME);
        }
    }

    public static String encryptHE(String input) {

        BigInteger ena = paillier.EncrypStr(input, r);

        String ena_string = ena.toString();

        return ena_string;
    }

    public static String decryptHE(String input) {

        String dec = paillier.DecrpyStr(new BigInteger(input));

        return dec;
    }
}
