// SpecimenBlock.java

package com.yosokumo.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A block of {@code Specimens}.
 */
class SpecimenBlock extends Block
{
    // At present the specimenSequence list is implemented as an ArrayList
    // because this implementation has O(1) performance for all three of 
    // "get", "add", and "next".  The latter is used to iterate over the 
    // list, and thus we do not want it to be expensive.  The "add" method 
    // appends to the end of the list. --  Essentially, we want an array 
    // which can grow in size.  

    /**
     * A sequence of {@code Specimens}. 
     */
    private List<Specimen> specimenSequence = new ArrayList<Specimen>();

    // Constructors

    /**
     * Initializes a newly created {@code SpecimenBlock} object with default 
     * attributes.
     */
    SpecimenBlock()
    {
        super();
    }

    /**
     * Initializes a newly created {@code SpecimenBlock} object with a study 
     * identifier.
     *
     * @param  id a study identifier for the block.
     */
    SpecimenBlock(String id)
    {
        super(id);
    }

    /**
     * Initializes a newly created {@code SpecimenBlock} object with the 
     * specimen sequence specified by the input parameter.
     *
     * @param  specimenCollection the specimen sequence of the specimen.
     */
    SpecimenBlock(Collection<Specimen> specimenCollection)
    {
        super();

        addSpecimens(specimenCollection);
    }

    /**
     * Initializes a newly created {@code SpecimenBlock} object with the 
     * study identifier and the specimen sequence specified by the input 
     * parameters.
     *
     * @param  id a study identifier for the block.
     * @param  specimenCollection the specimen sequence of the specimen.
     */
    SpecimenBlock(
        String id,
        Collection<Specimen> specimenCollection)
    {
        super(id);

        addSpecimens(specimenCollection);
    }


    /**
     * Return the specimen block type.
     *
     * @return the specimen block type:  Type.SPECIMEN.
     */
    Type getType()
    {
        return Type.SPECIMEN;
    }


    // Access to the specimen sequence

    /**
     * Add a {@code Specimen} to the block. The specimen parameter is 
     * appended to the end of the specimen sequence.
     *
     * @param   specimen  the {@code Specimen} to add to the block.
     */
    void addSpecimen(Specimen specimen)
    {
        specimenSequence.add(specimen);
    }


    /**
     * Add a collection of {@code Specimens} to the block.  The 
     * {@code Specimens} in the collection specified by the parameter are 
     * appended to the end of the specimen sequence.  The order the 
     * specimens are appended is the order in which the collection's 
     * {@code Iterator} returns the specimens.
     *
     * @param   specimens  the collection of {@code Specimens} to add to the 
     *                      block.
     *
     * @return true if and only if the specimen sequence changes.
     */
    boolean addSpecimens(Collection<Specimen> specimens)
    {
        return specimenSequence.addAll(specimens);
    }


    /**
     * Remove specimens from the end of the block.  The specified number of 
     * specimens are removed from the end of the specimen sequence.
     *
     * @param   numSpecimensToRemove is the number of specimens to remove 
     *          from the end of the block.  If this value is zero or negative,
     *          no specimens are removed.  If this value exceeds the number 
     *          of specimens in the block, then all specimens are removed.
     *
     * @return true if and only if the sequence is changed.
     */
    boolean removeSpecimens(int numSpecimensToRemove)
    {
        if (numSpecimensToRemove <= 0 || isEmpty())
            return false;

        if (numSpecimensToRemove >= size())
            clearSpecimens();
        else
            do 
            {
                specimenSequence.remove(size()-1);

            } while (--numSpecimensToRemove > 0);

        return true;
    }


    /**
     * Return a specimen from the block.  This makes it possible to iterate 
     * over all specimens in the sequence like this:
     * <pre>
     *   for (int i = 0;  i < specimenSequence.size();  ++i)
     *   {
     *       Specimen c = specimenSequence.getSpecimen(i)
     *       process specimen c
     *   }
     * </pre>
     * Caution:  It is better to iterate over all specimens by using 
     * getSpecimenSequence as shown below because that approach is efficient 
     * no matter what kind of List is used to implement the sequence.  
     * Iterating as shown just above is extremely expensive if the LinkedList 
     * implementation is used.
     *
     * @param   index specifying the specimen of the 0-based block.
     *
     * @return  the {@code Specimen} at the location specified by the index.
     */
    Specimen getSpecimen(int index)
    {
        return specimenSequence.get(index);
    }

    /**
     * Return all specimens in the block as a {@code List<Specimen>}.  
     * This makes it possible to iterate over all specimens in the block 
     * like this:
     * <pre>
     *   for (Specimen c : specimenSequence.getSpecimenSequence())
     *   {
     *       process specimen c
     *   }
     * </pre>
     *
     * @return a sequence of all specimens in the specimen sequence.
     */
    List<Specimen> getSpecimenSequence()
    {
        return specimenSequence;
    }


    /**
     * Remove all specimens from the block.  After a call of this method,
     * the sequence is empty, i.e., it contains no specimens.
     *
     */
    void clearSpecimens()
    {
        specimenSequence.clear();
    }

    /**
     * Return the number of specimens in the block.
     *
     * @return  the number of specimens in the block.
     */
    int size()
    {
        return specimenSequence.size();
    }

    /**
     * Return {@code true} if the block contains no specimens.
     *
     * @return {@code true} if the block contains no specimens.
     *         {@code false} otherwise.
     */
    boolean isEmpty()
    {
        return specimenSequence.isEmpty();
    }


    // Utility

    /**
     * Return a string representation of this {@code SpecimenBlock}.
     *
     * @return  the string representation of this {@code SpecimenBlock}.
     */
    public String toString()
    {
        StringBuilder b = new StringBuilder
        (
            "SpecimenBlock:"                             + "\n" +
            "  studyIdentifier = " + getStudyIdentifier() + "\n"
        );

        for (Specimen s : getSpecimenSequence())
        {
            b.append("\n" + "  " + s);
        }

        return b.toString();
    }

}   // end class SpecimenBlock

// end SpecimenBlock.java
