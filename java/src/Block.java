// Block.java

package com.yosokumo.core;

/**
 * An abstract base class for {@code EmptyBlock}, {@code PredictorBlock}, and 
 * {@code SpecimenBlock}.  The {@code Block} class is package-private so it 
 * is unknown in the API.  It is a way to package up {@code Predictors} and 
 * {@code Specimens} for transmission to the Yosokumo web service via HTTP.
 */

abstract class Block
{
    /**
     * Indicates what is stored in the {@code Block}, e.g., {@code Predictors},
     * or {@code Specimens}.
     */
    enum Type
    {
        /**
         * an empty block.
         */
        EMPTY,
        /**
         * a {@code Predictor} block.
         */
        PREDICTOR,
        /**
         * a {@code Specimen} block.
         */
        SPECIMEN
    }

    /**
     * Specifies the study which the block is associated with.
     */

    private String studyIdentifier = null;

    /**
     * Initializes a newly created {@code Block} object with default 
     * attributes.
     */
    Block()
    {
        studyIdentifier = null;
    }

    /**
     * Initializes a newly created {@code Block} object with a study 
     * identifier.
     *
     * @param  id a study identifier for the block.
     */
    Block(String id)
    {
        setStudyIdentifier(id);
    }

    /**
     * Return the type of the block.
     *
     * @return the type of the block.
     */
    abstract Type getType();


    /**
     * Set the study identifier of the block.
     *
     * @param id the identifier to assign to the block.
     */
    void setStudyIdentifier(String id)
    {
        studyIdentifier = id;
    }

    /**
     * Return the study identifier of the block.
     *
     * @return the study identifier of the block.
     */
    String getStudyIdentifier()
    {
        return studyIdentifier;
    }

    /**
     * Return the block as a String.
     *
     * @return the block as a String.
     */
    public abstract String toString();

}   // end class Block

// end Block.java
