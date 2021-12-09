package com.pranavprksn.hedp.he;

import java.math.BigInteger;
import java.util.HashMap;

public class ThreadSplit implements Runnable{

    private String inputText;
    private int threadNumber = 0;

    HashMap<Integer,BigInteger> map=new HashMap<>();

    ThreadSplit(String inputText, int number){
        this.inputText = inputText;
        this.threadNumber = number;
    }

    @Override
    public void run() {

        int temp = inputText.charAt(0);

        BigInteger num = new BigInteger(String.valueOf(temp));

        for (int i = 1; i < inputText.length(); i++) {

            temp = inputText.charAt(i);

            num = num.multiply(BigInteger.valueOf(1000)).add(BigInteger.valueOf(temp));
        }

        map.put(this.threadNumber, num);

    }
}
