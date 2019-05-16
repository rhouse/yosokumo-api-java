// Value.java

package com.yosokumo.core;

/**
 * An abstract base class for value classes which store specific primitive 
 * data types, e.g., {@code IntegerValue} and {@code RealValue}.
 */
public abstract class Value
{
    /**
     * Indicates the data type stored in the {@code Value}, e.g., integer or
     * real.
     */
    public enum Type
    {
        /**
         *  empty value.
         */
        EMPTY,
        /**
         * an unsigned integer.
         */
        NATURAL,
        /**
         * a signed integer.
         */
        INTEGER,
        /**
         * real, i.e., floating-point.
         */
        REAL,
        /**
         *  special value (represented as an unsigned integer).
         */
        SPECIAL
    }

    /**
     * Return the type of the value.
     *
     * @return the type of the value.
     */
    public abstract Type getType();


    /**
     * Return the value as a String.
     *
     * @return the value as a String.
     */
    public abstract String toString();

}   // end class Value

// end Value.java
