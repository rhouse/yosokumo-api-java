// ServiceException.java

package com.yosokumo.core;

/**
 * Encapsulates all exceptions which can be thrown by the Yosokumo Service.
 */

public class ServiceException extends Exception
{
    private int statusCode = 0;
    private String failedMethodName = null;

    /**
     * Constructs a new exception with the specified detail message.  The 
     * cause is not initialized and may subsequently be initialized by a call 
     * to {@code Throwable.initCause(java.lang.Throwable)}.
     *
     * @param  message the detail message. The detail message is saved for 
     *                 later retrieval by the {@code Throwable.getMessage()} 
     *                 method.
     */
    public ServiceException(String message)
    {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * Note that the detail message associated with cause is not automatically
     * incorporated in this exception's detail message.
     *
     * @param  message the detail message (which is saved for later retrieval 
     *                 by the {@code Throwable.getMessage()} method.
     * @param  cause   the cause (which is saved for later retrieval by the 
     *                 {@code Throwable.getCause()} method). (A null value is 
     *                 permitted, and indicates that the cause is nonexistent 
     *                 or unknown.)
     */
    public ServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified detail message, cause, 
     * status code and failed method name.  Note that the detail message 
     * associated with cause is not automatically incorporated in this 
     * exception's detail message.
     *
     * @param  message the detail message (which is saved for later retrieval 
     *                 by the {@code Throwable.getMessage()} method.
     * @param  cause   the cause (which is saved for later retrieval by the 
     *                 {@code Throwable.getCause()} method). (A null value is 
     *                 permitted, and indicates that the cause is nonexistent 
     *                 or unknown.)
     */
    public ServiceException(
        String    message,
        Throwable cause,
        int       statusCode,
        String    failedMethodName)
    {
        super(message, cause);
        this.statusCode = statusCode;
        this.failedMethodName = failedMethodName;
    }

    // getters

    /**
     * Return HTTP status code.
     *
     * @return  status code from most recent HTTP response.  A zero means 
     *              the most recent HTTP request did not result in a response,
     *              e.g., there is no network connection.
     */
    public int getStatusCode()
    {
        return statusCode;
    }

    /**
     * Return the name of the Service method which failed with this exception.
     *
     * @return  name of the Service method which failed.
     */
    public String getFailedMethodName()
    {
        return failedMethodName;
    }

}   //  end class ServiceException

// end Service.javaException
