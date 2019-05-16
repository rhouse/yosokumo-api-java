// Base64Test.java  -  Test the Base64 class with JUnit

package com.yosokumo.core;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;


public class Base64Test
{
    private static String bytesToBase64(byte[] bytes)
    {
        String str = Base64.encodeBytes(bytes);
        byte [] b  = Base64.decodeString(str);
        assertTrue(Arrays.equals(bytes, b));

        String strXXX = Base64XXX.encodeBytes(bytes);
        assertTrue(strXXX.equals(str));

        try
        {
            byte[] bXXX = Base64XXX.decode( strXXX );
            assertTrue(Arrays.equals(bXXX, bytes));
        }
        catch( java.io.IOException e )
        {
            assertTrue(false);
        }

        return str;
    }

    @Test
    public void variousLengths()
    {
        byte [] b0 = {};
        byte [] b1 = {22};
        byte [] b2 = {22, 33};
        byte [] b3 = {22, 33, 44};
        byte [] b4 = {22, 33, 44, 55};
        byte [] b5 = {22, 33, 44, 55, 66};
        byte [] b6 = {22, 33, 44, 55, 66, 77};
        byte [] b7 = {22, 33, 44, 55, 66, 88};
        byte [] b8 = { 0, 16, -125, 16, 81, -128};
        byte [] b9 = {16, 81};
        byte [] b10= {16, 81,    0};
        byte [] b11= {16, 81,   64};
        byte [] b12= {16, 81, -128};
        byte [] b13= {16, 81,  -64};
        byte [] b14 = {0, 16};
        byte [] b15 = {0};

        bytesToBase64(b0);
        bytesToBase64(b1);
        bytesToBase64(b2);
        bytesToBase64(b3);
        bytesToBase64(b4);
        bytesToBase64(b5);
        bytesToBase64(b6);
        bytesToBase64(b7);
        bytesToBase64(b8);
        bytesToBase64(b9);
        bytesToBase64(b10);
        bytesToBase64(b11);
        bytesToBase64(b12);
        bytesToBase64(b13);
        bytesToBase64(b14);
        bytesToBase64(b15);
    }

    @Test
    public void allBitPatterns()
    {
        byte [] bytes = new byte [3*256];

        int k = 0;

        for (int i = 0;  i < 256;  ++i)
        {
            bytes[k++] = (byte)i;
            bytes[k++] = (byte)(i+1);
            bytes[k++] = (byte)(i+2);
        }

        assertTrue(k == 3*256);

        String s = bytesToBase64(bytes);

        assertTrue(s.length() == 4*256);

        // Now check that every possible base64 char appears in s

        boolean [] sawChar = new boolean [64];

        for (int i = 0;  i < 64;  ++i)
            sawChar[i] = false;

        for (int i = 0;  i < 4*256;  ++i)
            sawChar[Base64.decode64(s.charAt(i))] = true;

        for (int i = 0;  i < 64;  ++i)
            assertTrue(sawChar[i]);
    }

    @Test
    public void randomBytes()
    {
        int numTests = 100;
        int maxLen = 2048;

        Random r = new Random();

        for (int i = 0;  i < numTests;  ++i)
        {
            int len = r.nextInt(maxLen);

            byte [] bytes = new byte [len];
            r.nextBytes(bytes);

            bytesToBase64(bytes);
        }

    }

}   //  end class Base64Test

