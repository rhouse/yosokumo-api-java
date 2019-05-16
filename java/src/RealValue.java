// RealValue.java

package com.yosokumo.core;

/**
 * Represents a floating-point value.
 */
public class RealValue extends Value
{
    private double realValue;

    /**
     * Initialize a newly created {@code RealValue} object with the 
     * value specified by the input parameter.
     *
     * @param  value is the real value.
     */
    public RealValue(double value)
    {
        realValue = value;
    }

    /**
     * Return the real type.
     *
     * @return the real type:  Type.REAL.
     */
    public Type getType()
    {
        return Type.REAL;
    }

    /**
     * Return the real value stored in this object.
     *
     * @return the real value .
     */
    public double getValue()
    {
        return realValue;
    }

    /**
     * Return the real value as a String.
     *
     * @return the real value as a String.
     */
    public String toString()
    {
        return Double.toString(realValue);
    }

}   // end class RealValue

// end RealValue.java
