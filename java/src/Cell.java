// Cell.java

package com.yosokumo.core;

/**
 * Represents a {@code Value} with an associated key or name.
 * <ul>
 * <li>name when the cell appears in a row (only case seen in the API)
 * <li>key when the cell appears in a column
 * </ul> 
 */

public class Cell
{
    /**
     * A name or a key, depending on the context in which the cell appears.
     */
    private long  nameOrKey;
    /**
     * The value of the cell.
     */
    private Value value;

    public Cell(long nameOrKey, Value value)
    {
        assert nameOrKey >= 0;

        this.nameOrKey = nameOrKey;
        this.value     = value;
    }

    /**
     * Return the name of the cell.
     *
     * @return the name of the cell.
     */
    public long getName()
    {
        return nameOrKey;
    }

    /**
     * Return the key of the cell.
     *
     * @return the key of the cell.
     */
    public long getKey()
    {
        return nameOrKey;
    }

    /**
     * Return the value of the cell.
     *
     * @return the value of the cell.
     */
    public Value getValue()
    {
        return value;
    }

    /**
     * Return a string representation of this {@code Cell}.
     *
     * @return  the string representation of this {@code Cell}.
     */
    public String toString()
    {
        return
        "Cell: nameOrKey=" + nameOrKey + " value=" + value+ "\n";
    }


}   // end class Cell

// end Cell.java
