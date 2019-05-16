// PredictorBlock.java

package com.yosokumo.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a block of {@code Predictors}.
 */
class PredictorBlock extends Block
{
    // At present the predictorSequence list is implemented as an ArrayList
    // because this implementation has O(1) performance for all three of 
    // "get", "add", and "next".  The latter is used to iterate over the 
    // list, and thus we do not want it to be expensive.  The "add" method 
    // appends to the end of the list. --  Essentially, we want an array 
    // which can grow in size.  

    /**
     * A sequence of {@code Predictors}.
     */
    private List<Predictor> predictorSequence = new ArrayList<Predictor>();

    // Constructors

    /**
     * Initializes a newly created {@code PredictorBlock} object with default 
     * attributes.
     */
    PredictorBlock()
    {
        super();
    }

    /**
     * Initializes a newly created {@code PredictorBlock} object with a study 
     * identifier.
     *
     * @param  id a study identifier for the block.
     */
    PredictorBlock(String id)
    {
        super(id);
    }

    /**
     * Initializes a newly created {@code PredictorBlock} object with the 
     * predictor sequence specified by the input parameter.
     *
     * @param  predictorCollection the predictor sequence of the specimen.
     */
    PredictorBlock(Collection<Predictor> predictorCollection)
    {
        super();

        addPredictors(predictorCollection);
    }

    /**
     * Initializes a newly created {@code PredictorBlock} object with the 
     * study identifier and the predictor sequence specified by the input 
     * parameters.
     *
     * @param  id a study identifier for the block.
     * @param  predictorCollection the predictor sequence of the specimen.
     */
    PredictorBlock(
        String id,
        Collection<Predictor> predictorCollection)
    {
        super(id);

        addPredictors(predictorCollection);
    }


    /**
     * Return the predictor block type.
     *
     * @return the predictor block type:  Type.PREDICTOR.
     */
    Type getType()
    {
        return Type.PREDICTOR;
    }


    // Access to the predictor sequence

    /**
     * Add a {@code Predictor} to the block. The predictor parameter is 
     * appended to the end of the predictor sequence.
     *
     * @param   predictor  the {@code Predictor} to add to the block.
     */
    void addPredictor(Predictor predictor)
    {
        predictorSequence.add(predictor);
    }


    /**
     * Add a collection of {@code Predictors} to the block.  The 
     * {@code Predictors} in the collection specified by the parameter are 
     * appended to the end of the predictor sequence.  The order the 
     * predictors are appended is the order in which the collection's 
     * {@code Iterator} returns the predictors.
     *
     * @param   predictors  the collection of {@code Predictors} to add to the 
     *                      block.
     *
     * @return true if and only if the predictor sequence changes.
     */
    boolean addPredictors(Collection<Predictor> predictors)
    {
        return predictorSequence.addAll(predictors);
    }


    /**
     * Remove predictors from the end of the block.  The specified number of 
     * predictors are removed from the end of the predictor sequence.
     *
     * @param   numPredictorsToRemove is the number of predictors to remove 
     *          from the end of the block.  If this value is zero or negative,
     *          no predictors are removed.  If this value exceeds the number 
     *          of predictors in the block, then all predictors are removed.
     *
     * @return true if and only if the sequence is changed.
     */
    boolean removePredictors(int numPredictorsToRemove)
    {
        if (numPredictorsToRemove <= 0 || isEmpty())
            return false;

        if (numPredictorsToRemove >= size())
            clearPredictors();
        else
            do 
            {
                predictorSequence.remove(size()-1);

            } while (--numPredictorsToRemove > 0);

        return true;
    }


    /**
     * Return a predictor from the block.  This makes it possible to iterate 
     * over all predictors in the sequence like this:
     * <pre>
     *   for (int i = 0;  i < predictorSequence.size();  ++i)
     *   {
     *       Predictor c = predictorSequence.getPredictor(i)
     *       process predictor c
     *   }
     * </pre>
     * Caution:  It is better to iterate over all predictors by using 
     * getPredictorSequence as shown below because that approach is efficient 
     * no matter what kind of List is used to implement the sequence.  
     * Iterating as shown just above is extremely expensive if the LinkedList 
     * implementation is used.
     *
     * @param   index specifying the predictor of the 0-based block.
     *
     * @return  the {@code Predictor} at the location specified by the index.
     */
    Predictor getPredictor(int index)
    {
        return predictorSequence.get(index);
    }

    /**
     * Return all predictors in the block as a {@code List<Predictor>}.  
     * This makes it possible to iterate over all predictors in the block 
     * like this:
     * <pre>
     *   for (Predictor c : predictorSequence.getPredictorSequence())
     *   {
     *       process predictor c
     *   }
     * </pre>
     *
     * @return a sequence of all predictors in the predictor sequence.
     */
    List<Predictor> getPredictorSequence()
    {
        return predictorSequence;
    }


    /**
     * Remove all predictors from the block.  After a call of this method,
     * the sequence is empty, i.e., it contains no predictors.
     *
     */
    void clearPredictors()
    {
        predictorSequence.clear();
    }

    /**
     * Return the number of predictors in the block.
     *
     * @return  the number of predictors in the block.
     */
    int size()
    {
        return predictorSequence.size();
    }

    /**
     * Return {@code true} if the block contains no predictors.
     *
     * @return {@code true} if the block contains no predictors.
     *         {@code false} otherwise.
     */
    boolean isEmpty()
    {
        return predictorSequence.isEmpty();
    }


    // Utility

    /**
     * Return a string representation of this {@code PredictorBlock}.
     *
     * @return  the string representation of this {@code PredictorBlock}.
     */
    public String toString()
    {
        StringBuilder b = new StringBuilder
        (
            "PredictorBlock:"                             + "\n" +
            "  studyIdentifier = " + getStudyIdentifier() + "\n"
        );

        for (Predictor p : getPredictorSequence())
        {
            b.append("\n" + "  " + p);
        }

        return b.toString();
    }

}   // end class PredictorBlock

// end PredictorBlock.java
