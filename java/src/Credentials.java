// Credentials.java

package com.yosokumo.core;

/**
 * Specifies credentials (user id and key) for Yosokumo authentication.
 */

public class Credentials
{
    /**
     * The exact length of a key.
     */
    public static final int KEY_LEN = 64;

    /**
     * The user to whom the key belongs.
     */
    private String userId;

    /**
     * The key belonging to the user.
     */
    private byte [] key = new byte[KEY_LEN];

    /**
     * Initializes a newly created {@code Credentials} object to 
     * the user id and key given as parameters.
     *
     * @throws ServiceException if the key length is not correct.
     */
    public Credentials(String userId, byte [] key) throws ServiceException
    {
        if (key.length != KEY_LEN)
            throw new ServiceException("Key is not the correct length");

        this.userId = userId;
        this.key = new byte [KEY_LEN];
        System.arraycopy(key, 0, this.key, 0, KEY_LEN);
    }

    /**
     * Return the user id.
     *
     * @return the user id.
     */
    public String getUserId()
    {
        return userId;
    } 

    /**
     * Return a copy of the user key.
     *
     * @return a copy of the user key.
     */
    public byte [] getKey()
    {
        byte [] keyToReturn = new byte [KEY_LEN];
        System.arraycopy(key, 0, keyToReturn, 0, KEY_LEN);
        return keyToReturn;
    } 

    /**
     * Return a string representation of the {@code Credentials}.
     *
     * @return  the string representation of the {@code Credentials}.
     */
    public String toString()
    {
        StringBuilder b = new StringBuilder
        (
            "Credentials:"               + "\n" +
            "  userId = "  + getUserId() + "\n"
        );

        b.append(keyToString(key));

        return b.toString();
    }

    /**
     * Return a string representation of a key.
     *
     * @param key  the key to convert to a string.
     * @return  the string representation of key.
     */
    public static String keyToString(byte [] key)
    {
        StringBuilder b = new StringBuilder
        (
            "  key    = "
        );

        if (key == null)
        {
            b.append("null\n");
            return b.toString();
        }

        b.append(key.length);

        int numPerLine = 15;
        int i = 0;

        for (byte n : key)
        {
            if (i++ % numPerLine == 0)
                b.append("\n ");
            b.append(" " + n);
        }
        b.append("\n");

        return b.toString();
    }
/*
  key    = 64 bytes
  -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx
  -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx
  -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx
  -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx -xxx
  -xxx -xxx -xxx -xxx
*/

}   //  end class Credentials

// end Credentials.java
