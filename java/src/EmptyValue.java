// EmptyValue.java

package com.yosokumo.core;

/**
 * Represents no value or an unknown value.
 */
public class EmptyValue extends Value
{
    public Type getType()
    {
        return Type.EMPTY;
    }

    /**
     * Return the value stored in this empty object.
     *
     * @return an {@code EmptyValue} (null).
     */
    public Value getValue()
    {
        return null;
    }

    /**
     * Return the empty value as a String.
     *
     * @return the empty value as a String.
     */
    public String toString()
    {
        return "<empty value>";
    }

}   // end class EmptyValue

// end EmptyValue.java
