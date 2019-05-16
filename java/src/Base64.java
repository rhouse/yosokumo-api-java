// Base64.java

package com.yosokumo.core;

/**
 * Provides methods for converting byte sequences to Base64 character strings 
 * and vice versa.
 *
 * <ul>
 * <li>{@code public static String encodeBytes(byte[] source)}
 * <li>{@code public static byte[] decodeString(String source)}
 * </ul>
 *
 * Be aware that there is not a one-to-one correspondence between byte 
 * sequences and Base64 character sequences.  Given any character sequence C
 * created by {@code encodeBytes}, the call {@code decodeString(C)} will 
 * return the original byte sequence.  However, there exist character 
 * sequences which are never produced by {@code encodeBytes}, and, hence, 
 * applying {@code decodeString} to such character sequences produces byte 
 * sequences from which the original character sequences cannot be recovered.
 * For example, {@code decodeString} converts both AA== and AB== to the single
 * byte 0, and {@code encodeBytes} converts the single byte 0 to AA==.  There 
 * does not exist a sequence of bytes which will cause {@code encodeBytes} to 
 * return AB==.
 *
 * @author  Roger House
 * @version 0.9
 */

class Base64
{
    /** 
     * Base64 character set:  A-Z, a-z, 0-9, +, /.
     **/

    private final static char [] encode64 = 
    {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 
        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 
        'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
        'w', 'x', 'y', 'z', '0', '1', '2', '3', 
        '4', '5', '6', '7', '8', '9', '+', '/'
    };

    /**
     * Convert a sequence of three bytes to a sequence of four characters.
     *
     * Map three 8-bit bytes to four 6-bit bytes:
     *
     *<pre>
     *       765432 10 7654 3210 76 543210 
     *      +---------+---------+---------+
     *      |AAAAAA AA|BBBB BBBB|CC CCCCCC|
     *      +------+-------+-------+------+
     *      |aaaaaa|bb bbbb|cccc cc|dddddd|
     *      +------+-------+-------+------+
     *       543210 54 3210 5432 10 543210
     *</pre>
     *
     * @param  A        the first byte.
     * @param  B        the second byte.
     * @param  C        the third byte.
     * @param  abcd     the resulting four characters are appended to abcd.
     */

    private static void map3to4(byte A, byte B, byte C, StringBuilder abcd)
    {
        int AA = ((int)A) & 0xff;
        int BB = ((int)B) & 0xff;
        int CC = ((int)C) & 0xff;

        char a, b, c, d;

        a = encode64[(AA >> 2) & 0x3f];
        b = encode64[((AA << 4) & 0x3f) | ((BB >> 4) & 0x3f)];
        c = encode64[((BB << 2) & 0x3f) | ((CC >> 6) & 0x3f)];
        d = encode64[CC & 0x3f];

        abcd.append(a).append(b).append(c).append(d);
    }

    /**
     * Convert a sequence of bytes to a Base64 string.
     *
     * @param  source is a sequence of zero or more 8-bit bytes.
     * @return a String containing the Base64 representation of the input
     *             bytes, 6-bits per character.
     */

    public static String encodeBytes(byte[] source)
    {
        StringBuilder abcd = new StringBuilder();
        int n = source.length/3;    // No. times to loop
        int i, j;

        for (i = 0, j = 1;  j <= n;  ++j, i += 3)
            map3to4(source[i], source[i+1], source[i+2], abcd);

        switch (source.length % 3)
        {
        case 1:
            map3to4(source[i], (byte)0, (byte)0, abcd);
            abcd.replace(abcd.length()-2, abcd.length(), "==");
            break;

        case 2:
            map3to4(source[i], source[i+1], (byte)0, abcd);
            abcd.replace(abcd.length()-1, abcd.length(), "=");
            break;
        }

        return abcd.toString();

    }   //  end encodeBytes


    /**
     * Convert a 6-bit Base64 character to an 8-bit byte.
     *
     * @param  c a 6-bit Base64 character.
     * @return the 8-bit byte corresponding to the input character.
     * @throws IllegalArgumentException if the input character is not a
     *             Base64 character.
     */

    static byte decode64(char c)
    {
        int b;

        if ('A' <= c && c <= 'Z')
            b = (int)c - (int)'A';

        else if ('a' <= c && c <= 'z')
            b = (int)c - (int)'a' + 26;

        else if ('0' <= c && c <= '9')
            b = (int)c - (int)'0' + 52;

        else if (c == '+')
            b = 62;

        else if (c == '/')
            b = 63;

        else 
            throw new IllegalArgumentException("Base64 string contains " +
                                                "invalid character: " + c);

        return (byte)b;
    }

    /**
     * Convert a sequence of four characters to a sequence of three bytes.
     * See the comment for map3to4 for the exact mapping.
     *
     * @param  a        the first character.
     * @param  b        the second character.
     * @param  c        the third character.
     * @param  d        the fourth character.
     * @param  ABC      the resulting three bytes are stored here.
     * @param  j        an index to the first byte in ABC to change.
     * @param  n        1, 2, or 3, indicating how many bytes to store in ABC.
     * @throws IllegalArgumentException if any input character is not a
     *             Base64 character.
     */

    private static void map4to3(
        char a, char b, char c, char d, 
        byte [] ABC, int j,
        int n)
    {
        int aa = decode64(a);
        int bb = decode64(b);
        int cc = decode64(c);
        int dd = decode64(d);

        byte A = (byte)((aa << 2) | (bb >> 4));
        byte B = (byte)((bb << 4) | (cc >> 2));
        byte C = (byte)((cc << 6) | dd);

        ABC[j] = A;
        --n;
        if (n-- > 0)
            ABC[j+1] = B;
        if (n > 0)
            ABC[j+2] = C;
    }

    /**
     * Convert a Base64 string to a sequence of bytes.
     *
     * @param  source a string of zero or more 6-bit Base64 characters.
     * @return a byte sequence containing the 8-bit bytes corresponding to 
     *             the input characters.
     * @throws IllegalArgumentException if the length of the input string is 
     *             not a multiple of four.
     * @throws IllegalArgumentException if the input string contains a 
     *             character which is not a Base64 character.
     */

    public static byte[] decodeString(String source)
    {
        int m = source.length();

        if (m % 4 != 0)
            throw new IllegalArgumentException("Base64 string length not " + 
                                                        "a multiple of 4");
        int resultLen = (m/4) * 3;

        if (source.endsWith("=="))
        {
            resultLen -= 2;
            m -= 2;
        }
        else if (source.endsWith("="))
        {
            --resultLen;
            --m;
        }

        // Now m is the no. of source chars to transform and resultLen is the 
        // number of output bytes to return

        byte [] result = new byte [resultLen];

        int n = m / 4;      // No. times to loop
        int i, j, k = 0;

        for (i = 0, j = 1;  j <= n;  ++j, i += 4, k += 3)
            map4to3(source.charAt(i), source.charAt(i+1), source.charAt(i+2), 
                    source.charAt(i+3), result, k, 3);

        switch (m % 4)
        {
        case 1:
            assert true : "Impossible length during base64 decoding";
            break;

        case 2:
            map4to3(source.charAt(i), source.charAt(i+1), 
                                                    'A', 'A', result, k, 1);
            break;

        case 3:
            map4to3(source.charAt(i), source.charAt(i+1), source.charAt(i+2), 
                                                        'A', result, k, 2);
            break;
        }

        return result;

    }   //  end encodeBytes

}   //  end class Base64

// end Base64.java

