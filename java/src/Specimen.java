// Specimen.java

package com.yosokumo.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A row of values.  Other terminology that is sometimes used:  An 
 * observation, a record, an example, or an instance.  These are 
 * the primary characteristics of a {@code Specimen}: 
 *
 * <ul>
 * <li>a key - unsigned integer
 * <li>a status - active or inactive
 * <li>a weight - postivie integer
 * <li>a predictand - value for this specimen
 * <li>a sequence of zero or more {@code Cells} containing the values 
 *          of the row
 * </ul>
 *
 * @author  Roger House
 * @version 0.9
 */
public class Specimen
{
    /**
     * Provides a logical delete facility.  The default value is 
     * {@code ACTIVE}.
     */
    public enum Status
    {
        /**
         * the specimen is active, i.e., it should be considered when 
         * constructing the model.
         */
        ACTIVE,
        /**
         * the specimen is inactive, i.e., it should not be considered when 
         * constructing the model.
         */
        INACTIVE
    }

    private long   key        = -1;

    private Status status     = Status.ACTIVE;
    private long   weight     = 1;
    private Value  predictand = null;

    // At present the cellSequence list is implemented as an ArrayList
    // because this implementation has O(1) performance for all three of 
    // "get", "add", and "next".  The latter is used to iterate over the 
    // list, and thus we do not want it to be expensive.  The "add" method 
    // appends to the end of the list. --  Essentially, we want an array 
    // which can grow in size.  

    /**
     * A sequence of cells.
     */
    private List<Cell> cellSequence = new ArrayList<Cell>();

    // Constructors

    /**
     * Initializes a newly created {@code Specimen} object with key specified
     * by the input parameter.  Other attributes are set to default values:
     * <ul>
     * <li>status - ACTIVE
     * <li>weight - 1
     * <li>predictand - EmptyValue
     * <li>cell sequence - empty
     * </ul>
     *
     * @param  key  the key of the specimen.
     */
    public Specimen(long key)
    {
        setSpecimenKey(key);
        setStatus     (Status.ACTIVE);
        setWeight     (1);
        setPredictand (new EmptyValue());
    }

    /**
     * Initializes a newly created {@code Specimen} object with key and 
     * cell sequence as specified by the input parameters.  Other attributes 
     * are set to default values:
     * <ul>
     * <li>status - ACTIVE
     * <li>weight - 1
     * <li>predictand - EmptyValue
     * </ul>
     *
     * @param  key      the key of the specimen.
     * @param  cells    the cell sequence of the specimen.
     */
    public Specimen(long key, Collection<Cell> cells)
    {
        addCells(cells);
        setSpecimenKey(key);
        setStatus     (Status.ACTIVE);
        setWeight     (1);
        setPredictand (new EmptyValue());
    }


    /**
     * Initializes a newly created {@code Specimen} object with key,
     * predictand, and cell sequence as specified by the input parameters.  
     * Other attributes are set to default values:
     * <ul>
     * <li>status - ACTIVE
     * <li>weight - 1
     * </ul>
     *
     *
     * @param  key          the key of the specimen.
     * @param  predictand   the predictand of the specimen.
     * @param  cells        the cell sequence of the specimen.
     */
    public Specimen
    (
        long             key,
        Value            predictand,
        Collection<Cell> cells
    )
    {
        addCells(cells);
        setSpecimenKey(key);
        setStatus     (Status.ACTIVE);
        setWeight     (1);
        setPredictand (predictand);
    }

    /**
     * Initializes a newly created {@code Specimen} object with attributes 
     * specified by the input parameters.
     *
     * @param  key          the key of the specimen.
     * @param  status       the status of the specimen.
     * @param  weight       the weight of the specimen.
     * @param  predictand   the predictand of the specimen.
     * @param  cells        the cell sequence of the specimen.
     */
    public Specimen
    (
        long             key,
        Status           status,
        long             weight,
        Value            predictand,
        Collection<Cell> cells
    )
    {
        addCells(cells);
        setSpecimenKey(key);
        setStatus     (status);
        setWeight     (weight);
        setPredictand (predictand);
    }


    // Setters and getters

    /**
     * Set the specimen key.
     *
     * @param  key  the key to assign to this specimen.  This is the 
     * unique identification of the specimen.  It must be positive.
     */
    public void setSpecimenKey(long key)
    {
        this.key = key;
    }

    /**
     * Return the specimen key.
     *
     * @return the key of this specimen, which is the unique 
     * identification of the specimen.  
     */
    public long getSpecimenKey()
    {
        return key;
    }


    /**
     * Set the specimen status.
     *
     * @param  s  the status to assign to this specimen.
     */
    public void setStatus(Status s)
    {
        status = s;
    }

    /**
     * Return the specimen status.
     *
     * @return the status of this specimen.
     */
    public Status getStatus()
    {
        return status;
    }


    /**
     * Set the specimen weight.
     *
     * @param  w  the weight to assign to this specimen.
     */
    public void setWeight(long w)
    {
        weight = w;
    }

    /**
     * Return the specimen weight.
     *
     * @return the weight of this specimen.
     */
    public long getWeight()
    {
        return weight;
    }


    /**
     * Set the specimen predictand.
     *
     * @param  v  the predictand to assign to this specimen.
     */
    public void setPredictand(Value v)
    {
        predictand = v;
    }

    /**
     * Return the specimen predictand.
     *
     * @return the predictand of this specimen.
     */
    public Value getPredictand()
    {
        return predictand;
    }


    // Access to the cell sequence

    /**
     * Add a {@code Cell} to the cell sequence.  The {@code Cell} parameter is 
     * appended to the end of the cell sequence.
     *
     * @param   cell  the {@code Cell} to add to the cell sequence.
     */
    void addCell(Cell cell)
    {
        cellSequence.add(cell);
    }


    /**
     * Add a collection of {@code Cells} to the cell sequence.  The 
     * {@code Cells} in the collection specified by the parameter are appended 
     * to the end of the cell sequence.  The order the cells are appended is 
     * the order in which the collection's {@code Iterator} returns the cells.
     *
     * @param   cells  the collection of {@code Cells} to add to the 
     *                 cell sequence.
     *
     * @return true if and only if the cell sequence changes.
     */
    boolean addCells(Collection<Cell> cells)
    {
        return cellSequence.addAll(cells);
    }


    /**
     * Remove cells from the end of the sequence.  The specified number of 
     * cells are removed from the end of the cell sequence.
     *
     * @param   numCellsToRemove is the number of cells to remove from the 
     *          end of the sequence.  If this value is zero or negative, no 
     *          cells are removed.  If this value exceeds the number of cells 
     *          in the sequence, then all cells are removed.
     *
     * @return true if and only if the sequence is changed.
     */
    boolean removeCells(int numCellsToRemove)
    {
        if (numCellsToRemove <= 0 || isEmpty())
            return false;

        if (numCellsToRemove >= size())
            clearCells();
        else
            do 
            {
                cellSequence.remove(size()-1);


            } while (--numCellsToRemove > 0);

        return true;
    }


    /**
     * Return a cell from the cell sequence.  This makes it possible to 
     * iterate over all cells in the sequence like this:
     * <pre>
     *   for (int i = 0;  i < cellSequence.size();  ++i)
     *   {
     *       Cell c = cellSequence.getCell(i)
     *       process cell c
     *   }
     * </pre>
     * Caution:  It is better to iterate over all cells by using 
     * getCells as shown below because that approach is efficient no
     * matter what kind of List is used to implement the sequence.  Iterating 
     * as shown just above is extremely expensive if the LinkedList 
     * implementation is used.
     *
     * @param   index specifying which cell of the sequence to return. 0-based.
     *
     * @return  the {@code Cell} at the location specified by the index.
     */
    Cell getCell(int index)
    {
        return cellSequence.get(index);
    }

    /**
     * Return all cells in the cell sequence as a {@code List<Cell>}.  
     * This makes it possible to iterate over all cells in the sequence 
     * like this:
     * <pre>
     *   for (Cell c : cellSequence.getCells())
     *   {
     *       process cell c
     *   }
     * </pre>
     *
     * @return a sequence of all cells in the cell sequence.
     */
    public List<Cell> getCells()
    {
        return cellSequence;
    }

    /**
     * Set the cells of this {@code Specimen} to the cell list specified 
     * by the parameter.
     *
     * @param cells  a list of cells to assign to this {@code Specimen} 
     */
    public void setCells(List<Cell> cells)
    {
        cellSequence = cells;
    }

    /**
     * Remove all cells from the sequence.  After a call of this method,
     * the sequence is empty, i.e., it contains no cells.
     *
     */
    void clearCells()
    {
        cellSequence.clear();
    }

    /**
     * Return the number of cells in the sequence.
     *
     * @return  the number of cells in the sequence.
     */
    public int size()
    {
        return cellSequence.size();
    }

    /**
     * Return {@code true} if the sequence contains no cells.
     *
     * @return {@code true} if the sequence contains no cells.
     *         {@code false} otherwise.
     */
    public boolean isEmpty()
    {
        return cellSequence.isEmpty();
    }


    // Utility

    /**
     * Return a string representation of this {@code Specimen}.
     *
     * @return  the string representation of this {@code Specimen}.
     */
    public String toString()
    {
        StringBuilder b = new StringBuilder
        (
            "Specimen:"                          + "\n" +
            "  key        = " + getSpecimenKey() + "\n" +
                                                   "\n" +
            "  status     = " + getStatus()      + "\n" +
            "  weight     = " + getWeight()      + "\n" +
            "  predictand = " + predictand       + "\n"
        );

        for (Cell c : getCells())
        {
            b.append("  " + c);
        }

        return b.toString();
    }

}   // end class Specimen

// end Specimen.java
