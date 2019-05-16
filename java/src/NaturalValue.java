// NaturalValue.java

package com.yosokumo.core;

/**
 * Represents an unsigned integer value.  Since Java does not support 
 * unsigned, the value is stored in a long.  All 64 bits of the unsigned
 * value are stored, but no arithmetic operations are allowed on the value.
 * Tests for equality and inequality are allowed, but not tests for greater 
 * than or less than.
 */
public class NaturalValue extends Value
{
    private long naturalValue;

    /**
     * Initialize a newly created {@code NaturalValue} object with the 
     * value specified by the input parameter.
     *
     * @param  value is the natural value (>= 0).
     */
    public NaturalValue(long value)
    {
        assert value >= 0;
        naturalValue = value;
    }

    /**
     * Return the natural type.
     *
     * @return the natural type:  Type.NATURAL.
     */
    public Type getType()
    {
        return Type.NATURAL;
    }

    /**
     * Return the natural value stored in this object.
     *
     * @return the natural value.
     */
    public long getValue()
    {
        return naturalValue;
    }

    /**
     * Return the natural value as a String.
     *
     * @return the natural value as a String.
     */
    public String toString()
    {
        return Long.toString(naturalValue);
    }


}   // end class NaturalValue

// end NaturalValue.java
