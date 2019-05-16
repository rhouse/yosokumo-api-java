// DigestRequest.java

package com.yosokumo.core;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

/**
 * Provides a method to digest (using HMAC) and encode (using Base64) an 
 * input message.
 */

class DigestRequest
{
    /**
     * Length of the encoded digest.
     */
    static final int ENCODED_LEN = 88;  // Length of encoded digest

    /**
     * Make an encoded digest of a message.
     *
     * @param  message  the input message to encode.
     * @param  key      the key to use to digest the input message.  It must
     *                      be exactly 64 bytes in length.
     * @return the digested, encoded key.  It is exactly 88 characters long.
     * @throws ServiceException if the input key length is not correct.
     * @throws ServiceException if there is any problem encoding the message.
     */

    static public String makeDigest(String message, byte[] key) 
        throws ServiceException
    {
        if (key.length != Credentials.KEY_LEN)
	    throw new ServiceException("Invalid key length (" + key.length + 
                                                    ") for making digest");

         // digest of input message
        byte[] digest;

        try
        {
            SecretKey seckey = new SecretKeySpec(key, "HmacSHA512");
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(seckey);
            digest = mac.doFinal(message.getBytes());
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new ServiceException("HMAC encoding failed", e);
        }
        catch (InvalidKeyException e)
        {
            throw new ServiceException("HMAC encoding failed", e);
        }

        String request = Base64.encodeBytes(digest);

        if (request.length() != ENCODED_LEN)
	    throw new ServiceException("Invalid encoded digest length (" +
                request.length() + ")");

        return request;

    }   //  end makeDigest

}   //  end class DigestRequest

// end DigestRequest.java
