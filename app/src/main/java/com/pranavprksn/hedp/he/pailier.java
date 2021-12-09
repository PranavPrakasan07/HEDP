package com.pranavprksn.hedp.he;

import android.util.Log;

import java.math.BigInteger;
import java.util.Random;

public class pailier {
    private static final String FILENAME = "encryption key file";
    static numcount numb;
    private BigInteger p, q, lambda;
    public BigInteger n;

    public BigInteger nsquare;
    private BigInteger g;
    private int bitLength;

    public pailier(int bitLengthVal, int certainty) {
        KeyGeneration(bitLengthVal, certainty);
    }

    public pailier() {

    }

    public BigInteger[] getPQ() {
        BigInteger[] res = new BigInteger[4];
        res[0] = new BigInteger("" + p);
        res[1] = new BigInteger("" + q);
        return res;
    }

    public void KeyGeneration(int bitLengthVal, int certainty) {
        bitLength = bitLengthVal;

        p = new BigInteger(bitLength / 2, certainty, new Random());
        q = new BigInteger(bitLength / 2, certainty, new Random());

        n = p.multiply(q);
        nsquare = n.multiply(n);

        g = new BigInteger("2");
        lambda = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)).divide(
                p.subtract(BigInteger.ONE).gcd(q.subtract(BigInteger.ONE)));

        if (g.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).gcd(n).intValue() != 1) {
            Log.d("Message", "g is not good. Choose g again.");
            System.exit(1);
        }
    }

    public void KeyGeneration(int bitLengthVal, int certainty, BigInteger a, BigInteger b) {
        bitLength = bitLengthVal;

        p = a;
        q = b;

        n = p.multiply(q);
        nsquare = n.multiply(n);

        g = new BigInteger("2");
        lambda = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)).divide(
                p.subtract(BigInteger.ONE).gcd(q.subtract(BigInteger.ONE)));

        if (g.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).gcd(n).intValue() != 1) {
            Log.d("Message", "g is not good. Choose g again.");
            System.exit(1);
        }
    }

    public void test(){
        BigInteger b = encryptStringParallel("", BigInteger.ONE);
    }

    public BigInteger Encryption(BigInteger m, BigInteger r) {
        return g.modPow(m, nsquare).multiply(r.modPow(n, nsquare)).mod(nsquare);
    }


    public BigInteger Decryption(BigInteger c) {
        BigInteger u = g.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).modInverse(n);
        return c.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).multiply(u).mod(n);
    }

    public BigInteger EncrypStr(String st, BigInteger r) {
        int temp = st.charAt(0);
        BigInteger num = new BigInteger(String.valueOf(temp));

        for (int i = 1; i < st.length(); i++) {
            temp = st.charAt(i);
            num = num.multiply(BigInteger.valueOf(1000)).add(BigInteger.valueOf(temp));
        }
        BigInteger enc = Encryption(num, r);
        return enc;
    }


    public BigInteger encryptStringParallel(String st, BigInteger r) {

        int splitLength = st.length() / 4 ;

        ThreadSplit s1 = new ThreadSplit(st.substring(0,splitLength), 1);
        ThreadSplit s2 = new ThreadSplit(st.substring(splitLength+1,2*splitLength), 2);
        ThreadSplit s3 = new ThreadSplit(st.substring(2*splitLength+1,3*splitLength), 3);
        ThreadSplit s4 = new ThreadSplit(st.substring(3*splitLength+1), 4);

        Thread t1 = new Thread(s1);
        Thread t2 = new Thread(s2);
        Thread t3 = new Thread(s3);
        Thread t4 = new Thread(s4);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            Log.d("TAG", e.toString());
        }finally {
            Log.d("TAG", "End");
        }

        int temp = st.charAt(0);
        // System.out.println(temp);
        BigInteger num = new BigInteger(String.valueOf(temp));

        for (int i = 1; i < st.length(); i++) {

            temp = st.charAt(i);
            // System.out.println(temp);

            //num = num.multiply(BigInteger.valueOf(1000)).add(BigInteger.valueOf(temp));
            // System.out.println("num:" + num);

        }
        // System.out.println("num:" + num);

        System.out.println("end of loop");

        return BigInteger.ONE;//encryption(num, r);
    }

    public String DecrpyStr(BigInteger num) {
        BigInteger num1 = Decryption(num);
        Log.d("SecondBig", String.valueOf(num1));
        int strc = num1.toString().length();
        Log.d("strc length", String.valueOf(strc));

        String m = num1.toString();
        Log.d("m string", m);

        if (strc % 3 != 0) {
            m = "0" + m;
        }

        StringBuilder strd = new StringBuilder();

        for (int i = 0; i < m.length(); i += 3) {
            strd.append((char) (Integer.parseInt(m.substring(i, i + 3))));
            Log.d("Process", String.valueOf((strd)));
        }
        return strd.toString();
    }
}
	
