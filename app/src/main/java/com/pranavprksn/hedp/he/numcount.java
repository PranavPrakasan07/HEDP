package com.pranavprksn.hedp.he;

import java.math.BigInteger;

public class numcount {

    public BigInteger numCounter(BigInteger n) {

        BigInteger count = BigInteger.ZERO;
        System.out.println("No. of digits: ");

        while (!n.equals(BigInteger.ZERO)) {
            n = n.divide(BigInteger.TEN);
            count = count.add(BigInteger.ONE);
        }

        return count;
    }
}