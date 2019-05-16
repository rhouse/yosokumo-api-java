// EmptyBlock.java

package com.yosokumo.core;

/**
 * Represents an empty block, i.e., a block containing no {@code Predictors} 
 * and no {@code Specimens}. An empty block can be used to test whether the 
 * requesting user has permission to post to the table or whether the study 
 * is 'running' and therefore accepting blocks into the table.
 */

class EmptyBlock extends Block
{
    /**
     * Initializes a newly created {@code EmptyBlock} object with default 
     * attributes.
     */
    EmptyBlock()
    {
        super();
    }

    /**
     * Initializes a newly created {@code EmptyBlock} object with a study 
     * identifier.
     *
     * @param  id a study identifier for the block.
     */
    EmptyBlock(String id)
    {
        super(id);
    }

    /**
     * Return the empty block type.
     *
     * @return the empty block type:  Type.EMPTY.
     */
    Type getType()
    {
        return Type.EMPTY;
    }

    /**
     * Return the empty block as a String.
     *
     * @return the empty block as a String.
     */
    public String toString()
    {
        return "<empty block>";
    }

}   // end class EmptyBlock

// end EmptyBlock.java
