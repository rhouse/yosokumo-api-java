// SpecialValue.java

package com.yosokumo.core;

/**
 * Represents an unsigned integer value.  Since Java does not support 
 * unsigned, the value is stored in a long.  All 64 bits of the unsigned
 * value are stored, but no arithmetic operations are allowed on the value.
 * Tests for equality and inequality are allowed, but not tests for greater 
 * than or less than.
 */
public class SpecialValue extends Value
{
    private long specialValue;

    /**
     * Initialize a newly created {@code SpecialValue} object with the 
     * value specified by the input parameter.
     *
     * @param  value is the special value (>= 0).
     */
    public SpecialValue(long value)
    {
        assert value >= 0;
        specialValue = value;
    }

    /**
     * Return the special value type.
     *
     * @return the special value type:  Type.SPECIAL.
     */
    public Type getType()
    {
        return Type.SPECIAL;
    }

    /**
     * Return the special value stored in this object.
     *
     * @return the special value.
     */
    public long getValue()
    {
        return specialValue;
    }

    /**
     * Return the special value as a String.
     *
     * @return the special value as a String.
     */
    public String toString()
    {
        return Long.toString(specialValue);
    }

}   // end class SpecialValue

// end SpecialValue.java
