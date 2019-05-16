// Message.java

package com.yosokumo.core;

/**
 * A text message with a type indicating if the message is information or 
 * an error.  Some responses from the Yokosumo service return messages, 
 * especially in case of error.
 */
public class Message
{
    /**
     * Indicates whether the message is informational or an error message.
     */
    public enum Type
    {
        /**
         * the message is informational.
         */
        INFORMATION,
        /**
         * the message indicates an error.
         */
        ERROR
    }

    /**
     * The type of message.
     */
    private Type type = null;

    /**
     * The message text.
     */
    private String text = null;

    // Constructors

    /**
     * Initializes a newly created {@code Message} object with attributes 
     * specified by the input parameters.
     *
     * @param  type   the type of the message:  INFORMATION or ERROR.
     * @param  text   the text of the message.
     */
    Message(Type type, String text)
    {
        this.type = type;
        this.text = text;
    }

    // Getters (there are no setters)

    /**
     * Return the message text.
     *
     * @return the text of the message.
     */
    public String getText()
    {
        return text;
    }

    /**
     * Return the message type.
     *
     * @return the type of this message.
     */
    public Type getType()
    {
        return type;
    }

    // Utility

    /**
     * Return a string representation of this {@code Message}.
     *
     * @return  the string representation of this {@code Message}.
     */
    public String toString()
    {
        return
            "Message:"              + "\n" +
            "  type = " + getType() + "\n" +
            "  text = " + getText() + "\n"
        ;
    }

}   // end class Message

// end Message.java
