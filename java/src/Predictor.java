// Predictor.java

package com.yosokumo.core;

/**
 * A column of values, an independent variable.  Other terminology that is 
 * sometimes used:  An attribute or an input.  These are the primary 
 * characteristics of a {@code Predictor}: 
 * <ul>
 * <li>a name - unsigned positive integer
 * <li>a status - active or inactive
 * <li>a type - categorical or continuous
 * <li>a level - nominal, ordinal, interval, ratio
 * </ul>
 *
 * @author  Roger House
 * @version 0.9
 */
public class Predictor
{
    /**
     * Provides a logical delete facility.  The default value is 
     * {@code ACTIVE}.
     */
    public enum Status
    {
        /**
         * the predictor is active, i.e., it should be considered when 
         * constructing the model.
         */
        ACTIVE,
        /**
         * the predictor is inactive, i.e., it should not be considered when 
         * constructing the model.
         */
        INACTIVE
    }


    /**
     * Describes the quality of the predictor and determines the statistical 
     * operations that can be performed on the feature.  The default value 
     * is {@code CONTINUOUS}.
     */
    public enum Type
    {
        /**
         * the feature takes on discrete values.
         */
        CATEGORICAL,
        /**
         * the feature may take any real value.
         */
        CONTINUOUS
    }

    /**
     * Indicates the level of measurement for the feature. The default value 
     * is {@code RATIO}.
     */
    public enum Level
    {
        /**
         * the values are names or labels for certain characteristics, 
         * without any implied order among them.
         */
        NOMINAL,
        /**
         * the feature takes values that represent the rank order of the 
         * specimen, but the distance between the values is not meaningful. 
         */
        ORDINAL,
        /**
         * the predictor values can be ranked and have a standard unit of 
         * measurement, but the zero value is arbitrary.
         */
        INTERVAL,
        /**
         * The values taken by the predictor have a standard unit of 
         * measurement, are rankable, and the zero value is non-arbitrary.
         */
        RATIO
    }

    private long   name   = -1;

    private Status status = Status.ACTIVE;
    private Type   type   = Type.CONTINUOUS;
    private Level  level  = Level.RATIO;

    // Constructors

    /**
     * Initializes a newly created {@code Predictor} object with default 
     * attributes.
     * <ul>
     * <li>status ACTIVE
     * <li>type CONTINUOUS
     * <li>level RATIO
     * </ul>
     *
     * @param  name        the name of the predictor.
     */
    public Predictor(long name)
    {
        setPredictorName(name);
        setStatus       (Status.ACTIVE  );
        setType         (Type.CONTINUOUS);
        setLevel        (Level.RATIO    );
    }

    /**
     * Initializes a newly created {@code Predictor} object with attributes 
     * specified by the input parameters.
     *
     * @param  name    the name of the predictor.
     * @param  status  the status of the predictor.
     * @param  type    the type of the predictor.
     * @param  level   the level of the predictor.
     */
    public Predictor
    (
        long   name,
        Status status,
        Type   type, 
        Level  level
    )
    {
        setPredictorName(name  );
        setStatus       (status);
        setType         (type  );
        setLevel        (level );
    }


    // Setters and getters

    /**
     * Set the predictor name.
     *
     * @param  name  the name to assign to this predictor.  This is the 
     * unique identification of the predictor.  It must be positive.
     */
    public void setPredictorName(long name)
    {
        assert(name > 0);
        this.name = name;
    }

    /**
     * Return the predictor name.
     *
     * @return the name of this predictor, which is the unique 
     * identification of the predictor.  
     */
    public long getPredictorName()
    {
        return name;
    }


    /**
     * Set the predictor status.
     *
     * @param  s  the status to assign to this predictor.
     */
    public void setStatus(Status s)
    {
        status = s;
    }

    /**
     * Return the predictor status.
     *
     * @return the status of this predictor.
     */
    public Status getStatus()
    {
        return status;
    }


    /**
     * Set the predictor type.
     *
     * @param  t  the type to assign to this predictor.
     */
    public void setType(Type t)
    {
        type = t;
    }

    /**
     * Return the predictor type.
     *
     * @return the type of this predictor.
     */
    public Type getType()
    {
        return type;
    }


    /**
     * Set the predictor level.
     *
     * @param  L  the level to assign to this predictor.
     */
    public void setLevel(Level L)
    {
        level = L;
    }

    /**
     * Return the predictor level.
     *
     * @return the level of this predictor.
     */
    public Level getLevel()
    {
        return level;
    }


    // Utility

    /**
     * Return a string representation of this {@code Predictor}.
     *
     * @return  the string representation of this {@code Predictor}.
     */
    public String toString()
    {
        StringBuilder b = new StringBuilder
        (
            "Predictor:"                       + "\n" +
            "  name   = " + getPredictorName() + "\n" +
            "  status = " + getStatus()        + "\n" +
            "  type   = " + getType()          + "\n" +
            "  level  = " + getLevel()         + "\n"
        );

        return b.toString();
    }

}   // end class Predictor


// end Predictor.java
