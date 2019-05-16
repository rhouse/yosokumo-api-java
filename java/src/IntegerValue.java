// IntegerValue.java

package com.yosokumo.core;

/**
 * Represents a signed integer value.
 */
public class IntegerValue extends Value
{
    private long integerValue;

    /**
     * Initialize a newly created {@code IntegerValue} object with the 
     * value specified by the input parameter.
     *
     * @param  value is the integer value.
     */
    public IntegerValue(long value)
    {
        integerValue = value;
    }


    /**
     * Return the integer type.
     *
     * @return the integer type:  Type.INTEGER.
     */
    public Type getType()
    {
        return Type.INTEGER;
    }

    /**
     * Return the integer value stored in this object.
     *
     * @return the integer value.
     */
    public long getValue()
    {
        return integerValue;
    }

    /**
     * Return the integer value as a String.
     *
     * @return the integer value as a String.
     */
    public String toString()
    {
        return Long.toString(integerValue);
    }

}   // end class IntegerValue

// end IntegerValue.java
