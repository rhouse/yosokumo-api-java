// SpecimenTest.java  -  Test the Specimen class with JUnit

package com.yosokumo.core;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SpecimenTest
{
    private Collection<Cell> makeList()
    {
        boolean b;

        LinkedList<Cell> cellList = new LinkedList<Cell>();

        b = cellList.add(new Cell(11111, new RealValue(99.999)));
        assertTrue(b);
        b = cellList.add(new Cell(22222, new RealValue(88.888)));
        assertTrue(b);
        b = cellList.add(new Cell(33333, new RealValue(77.777)));
        assertTrue(b);

        return cellList;
    }

    private void checkCellSequence(Specimen specimen)
    {
        assertTrue(specimen.size() == 3);
        assertFalse(specimen.isEmpty());

        // test getCellSequence -- use an Iterator

        List<Cell> cells = specimen.getCells();

        int i = 0;

        for (Iterator<Cell> iter = cells.iterator();  iter.hasNext(); )
        {
            Cell c = iter.next();

            verifyCell(c, ++i);
        } 

        assertTrue(i == 3);

        // test getCellSequence -- use a for-loop with indices

        for (i = 0;  i < specimen.size();  ++i)
        {
            Cell c = specimen.getCell(i);
            verifyCell(c, i+1);
        }

        assertTrue(i == 3);

        // test getCellSequence -- use a for-each-loop

        i = 0;

        for (Cell c : specimen.getCells())
        {
            verifyCell(c, ++i);
        }

        assertTrue(i == 3);

    }   //  end checkCellSequence

    @Test
    public void minimalConstructor()
    {
        Specimen specimen = new Specimen(123456789, makeList());

        assertTrue(specimen.getSpecimenKey()         == 123456789);
        assertTrue(specimen.getStatus()              == Specimen.Status.ACTIVE);
        assertTrue(specimen.getWeight()              == 1);
        assertTrue(specimen.getPredictand().getType()== Value.Type.EMPTY);
        checkCellSequence(specimen);
    }

    @Test
    public void anotherConstructor()
    {
        Specimen specimen = new Specimen(
            987654321, 
            new RealValue(31.4159),
            makeList());

        assertTrue(specimen.getSpecimenKey()         == 987654321);
        assertTrue(specimen.getStatus()              == Specimen.Status.ACTIVE);
        assertTrue(specimen.getWeight()              == 1);
        assertTrue(((RealValue)specimen.getPredictand()).getValue()==31.4159);

        checkCellSequence(specimen);
    }

    @Test
    public void usualConstructor()
    {
        Specimen specimen = new Specimen(
            123456789,
            Specimen.Status.INACTIVE,
            101010,
            new IntegerValue(314159),
            makeList());

        assertTrue(specimen.getSpecimenKey()       == 123456789);
        assertTrue(specimen.getStatus()            == Specimen.Status.INACTIVE);
        assertTrue(specimen.getWeight()            == 101010);
        assertTrue(((IntegerValue)specimen.getPredictand()).getValue()==314159);

        checkCellSequence(specimen);
    }

    @Test
    public void settersAndGetters()
    {
        Specimen specimen = new Specimen(314159, makeList());

        specimen.setSpecimenKey(951413);
        specimen.setStatus(Specimen.Status.INACTIVE);
        specimen.setWeight(1000000);
        specimen.setPredictand(new NaturalValue(999999999));

        assertTrue(specimen.getSpecimenKey() == 951413);
        assertTrue(specimen.getStatus()       == Specimen.Status.INACTIVE);
        assertTrue(specimen.getWeight()       == 1000000);
        assertTrue(((NaturalValue)specimen.getPredictand()).getValue()==999999999);
    }

    @Test
    public void basicAccessToCellSequence()
    {
        Cell cell1 = new Cell(11111, new IntegerValue(99999));
        Cell cell2 = new Cell(22222, new IntegerValue(88888));
        Cell cell3 = new Cell(33333, new IntegerValue(77777));

        Specimen specimen = new Specimen(454567, makeList());
        specimen.clearCells();

        // test addCell, getCell, size, and isEmpty

        assertTrue(specimen.isEmpty());

        specimen.addCell(cell1);
        assertTrue(specimen.size() == 1);
        assertFalse(specimen.isEmpty());
        assertTrue(specimen.getCell(0) == cell1);

        specimen.addCell(cell2);
        assertTrue(specimen.size() == 2);
        assertFalse(specimen.isEmpty());
        assertTrue(specimen.getCell(1) == cell2);

        specimen.addCell(cell3);
        assertTrue(specimen.size() == 3);
        assertFalse(specimen.isEmpty());
        assertTrue(specimen.getCell(2) == cell3);

        specimen.addCell(cell3);
        assertTrue(specimen.size() == 4);
        assertFalse(specimen.isEmpty());
        assertTrue(specimen.getCell(3) == cell3);

        // test removeCells

        boolean b;

        b = specimen.removeCells(0);
        assertFalse(b);
        assertTrue(specimen.size() == 4);

        b = specimen.removeCells(-1);
        assertFalse(b);
        assertTrue(specimen.size() == 4);

        b = specimen.removeCells(2);
        assertTrue(b);
        assertTrue(specimen.size() == 2);
        assertFalse(specimen.isEmpty());
        assertTrue(specimen.getCell(0) == cell1);
        assertTrue(specimen.getCell(1) == cell2);

        b = specimen.removeCells(5);
        assertTrue(b);
        assertTrue(specimen.size() == 0);
        assertTrue(specimen.isEmpty());

        b = specimen.removeCells(5);
        assertFalse(b);
        assertTrue(specimen.size() == 0);
        assertTrue(specimen.isEmpty());

        // test clearCells

        specimen.addCell(cell1);
        specimen.addCell(cell2);
        specimen.addCell(cell3);
        assertTrue(specimen.size() == 3);

        specimen.clearCells();
        assertTrue(specimen.size() == 0);
        assertTrue(specimen.isEmpty());

    }   //  end basicAccessToCellSequence()

    @Test
    public void advancedAccessToCellSequence()
    {
        Specimen specimen = new Specimen(765454, makeList());
        checkCellSequence(specimen);
    }

    private void verifyCell(Cell c, int i)
    {
        switch (i)
        {
        case 1:  assertTrue(c.getKey() == 11111);
                 assertTrue(((RealValue)c.getValue()).getValue()==99.999);
                 break;
        case 2:  assertTrue(c.getKey() == 22222);
                 assertTrue(((RealValue)c.getValue()).getValue()==88.888);
                 break;
        case 3:  assertTrue(c.getKey() == 33333);
                 assertTrue(((RealValue)c.getValue()).getValue()==77.777);
                 break;
        default: assertTrue(false);
        }
    }

    @Test
    public void stressTestAccessToCellSequence()
    {
        int numCells = 100000;

        Specimen specimen = new Specimen(1234567890, makeList());
        specimen.clearCells();

        // Create a bunch of cells and put them in a specimen

        for (int i = 1;  i <= numCells;  i++)
            specimen.addCell(new Cell(i, new NaturalValue(i)));

        assertTrue(specimen.size() == numCells);

        // Remove each cell from the specimen

        for (int i = numCells;  i >= 1;  i--)
        {
            Cell c = specimen.getCell(i-1);
            assertTrue(c.getKey() == i);
            assertTrue(((NaturalValue)c.getValue()).getValue() == i);
            specimen.removeCells(1);
        }
        assertTrue(specimen.isEmpty());

    }   //  end stressTestAccessToCellSequence

}   //  end class SpecimenTest
