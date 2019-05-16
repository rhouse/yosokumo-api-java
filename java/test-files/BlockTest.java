// BlockTest.java  -  Test the Block class and its subclasses with JUnit

package com.yosokumo.core;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BlockTest
{
    @Test
    public void minimalConstructor()
    {
        Block block = new EmptyBlock();
        assertTrue(block.getStudyIdentifier() == null);
        assertTrue(block.getType() == Block.Type.EMPTY);

        PredictorBlock pblock = new PredictorBlock();
        assertTrue(pblock.getStudyIdentifier() == null);
        assertTrue(pblock.getType() == Block.Type.PREDICTOR);
        assertTrue(pblock.size() == 0);
        assertTrue(pblock.isEmpty());

        SpecimenBlock sblock = new SpecimenBlock();
        assertTrue(sblock.getStudyIdentifier() == null);
        assertTrue(sblock.getType() == Block.Type.SPECIMEN);
        assertTrue(sblock.size() == 0);
        assertTrue(sblock.isEmpty());
    }

    @Test
    public void usualConstructor()
    {
        String id = "predictor id 123";

        Block block = new EmptyBlock(id);
        assertTrue(block.getStudyIdentifier().equals(id));

        PredictorBlock pblock = new PredictorBlock(id);
        assertTrue(pblock.getStudyIdentifier().equals(id));
        assertTrue(pblock.getType() == Block.Type.PREDICTOR);
        assertTrue(pblock.size() == 0);
        assertTrue(pblock.isEmpty());

        pblock = new PredictorBlock(makePredictorList());
        assertTrue(pblock.getStudyIdentifier() == null);
        assertTrue(pblock.getType() == Block.Type.PREDICTOR);
        checkPredictorSequence(pblock);

        pblock = new PredictorBlock(id, makePredictorList());
        assertTrue(pblock.getStudyIdentifier().equals(id));
        assertTrue(pblock.getType() == Block.Type.PREDICTOR);
        checkPredictorSequence(pblock);

        SpecimenBlock sblock = new SpecimenBlock(id);
        assertTrue(sblock.getStudyIdentifier().equals(id));
        assertTrue(sblock.getType() == Block.Type.SPECIMEN);
        assertTrue(sblock.size() == 0);
        assertTrue(sblock.isEmpty());

        sblock = new SpecimenBlock(makeSpecimenList());
        assertTrue(sblock.getStudyIdentifier() == null);
        assertTrue(sblock.getType() == Block.Type.SPECIMEN);
        checkSpecimenSequence(sblock);

        sblock = new SpecimenBlock(id, makeSpecimenList());
        assertTrue(sblock.getStudyIdentifier().equals(id));
        assertTrue(sblock.getType() == Block.Type.SPECIMEN);
        checkSpecimenSequence(sblock);
    }

    @Test
    public void settersAndGetters()
    {
        String id1 = "1234567890abcdef";
        String id2 = "fedcba0987654321";

        Block block = new EmptyBlock(id1);
        assertTrue(block.getStudyIdentifier().equals(id1));
        block.setStudyIdentifier(id2);
        assertTrue(block.getStudyIdentifier().equals(id2));

        PredictorBlock pblock = new PredictorBlock(id1);
        assertTrue(pblock.getStudyIdentifier().equals(id1));
        pblock.setStudyIdentifier(id2);
        assertTrue(pblock.getStudyIdentifier().equals(id2));

        SpecimenBlock sblock = new SpecimenBlock(id1);
        assertTrue(sblock.getStudyIdentifier().equals(id1));
        sblock.setStudyIdentifier(id2);
        assertTrue(sblock.getStudyIdentifier().equals(id2));
    }

    @Test
    public void basicAccessToSequences()
    {
        Predictor predictor1 = new Predictor(
            11111, 
            Predictor.Status.ACTIVE, 
            Predictor.Type.CATEGORICAL,
            Predictor.Level.INTERVAL);

        Predictor predictor2 = new Predictor(
            22222, 
            Predictor.Status.INACTIVE, 
            Predictor.Type.CONTINUOUS,
            Predictor.Level.NOMINAL);

        Predictor predictor3 = new Predictor(
            33333, 
            Predictor.Status.ACTIVE, 
            Predictor.Type.CATEGORICAL,
            Predictor.Level.ORDINAL);

  // PredictorBlock tests

        PredictorBlock pblock = new PredictorBlock("454567");

        // test addPredictor, getPredictor, size, and isEmpty

        assertTrue(pblock.isEmpty());

        pblock.addPredictor(predictor1);
        assertTrue(pblock.size() == 1);
        assertFalse(pblock.isEmpty());
        assertTrue(pblock.getPredictor(0) == predictor1);

        pblock.addPredictor(predictor2);
        assertTrue(pblock.size() == 2);
        assertFalse(pblock.isEmpty());
        assertTrue(pblock.getPredictor(1) == predictor2);

        pblock.addPredictor(predictor3);
        assertTrue(pblock.size() == 3);
        assertFalse(pblock.isEmpty());
        assertTrue(pblock.getPredictor(2) == predictor3);

        pblock.addPredictor(predictor3);
        assertTrue(pblock.size() == 4);
        assertFalse(pblock.isEmpty());
        assertTrue(pblock.getPredictor(3) == predictor3);

        // test removePredictors

        boolean b;

        b = pblock.removePredictors(0);
        assertFalse(b);
        assertTrue(pblock.size() == 4);

        b = pblock.removePredictors(-1);
        assertFalse(b);
        assertTrue(pblock.size() == 4);

        b = pblock.removePredictors(2);
        assertTrue(b);
        assertTrue(pblock.size() == 2);
        assertFalse(pblock.isEmpty());
        assertTrue(pblock.getPredictor(0) == predictor1);
        assertTrue(pblock.getPredictor(1) == predictor2);

        b = pblock.removePredictors(5);
        assertTrue(b);
        assertTrue(pblock.size() == 0);
        assertTrue(pblock.isEmpty());

        b = pblock.removePredictors(5);
        assertFalse(b);
        assertTrue(pblock.size() == 0);
        assertTrue(pblock.isEmpty());

        // test clearPredictors

        pblock.addPredictor(predictor1);
        pblock.addPredictor(predictor2);
        pblock.addPredictor(predictor3);
        assertTrue(pblock.size() == 3);

        pblock.clearPredictors();
        assertTrue(pblock.size() == 0);
        assertTrue(pblock.isEmpty());

  // SpecimenBlock tests

        List<Cell> list = Collections.emptyList();

        Specimen specimen1 = new Specimen(11111, list);
        Specimen specimen2 = new Specimen(22222, list);
        specimen2.setStatus(Specimen.Status.INACTIVE);
        Specimen specimen3 = new Specimen(33333, list);

        SpecimenBlock sblock = new SpecimenBlock("765454");

        // test addSpecimen, getSpecimen, size, and isEmpty

        assertTrue(sblock.isEmpty());

        sblock.addSpecimen(specimen1);
        assertTrue(sblock.size() == 1);
        assertFalse(sblock.isEmpty());
        assertTrue(sblock.getSpecimen(0) == specimen1);

        sblock.addSpecimen(specimen2);
        assertTrue(sblock.size() == 2);
        assertFalse(sblock.isEmpty());
        assertTrue(sblock.getSpecimen(1) == specimen2);

        sblock.addSpecimen(specimen3);
        assertTrue(sblock.size() == 3);
        assertFalse(sblock.isEmpty());
        assertTrue(sblock.getSpecimen(2) == specimen3);

        sblock.addSpecimen(specimen3);
        assertTrue(sblock.size() == 4);
        assertFalse(sblock.isEmpty());
        assertTrue(sblock.getSpecimen(3) == specimen3);

        // test removeSpecimens

        b = sblock.removeSpecimens(0);
        assertFalse(b);
        assertTrue(sblock.size() == 4);

        b = sblock.removeSpecimens(-1);
        assertFalse(b);
        assertTrue(sblock.size() == 4);

        b = sblock.removeSpecimens(2);
        assertTrue(b);
        assertTrue(sblock.size() == 2);
        assertFalse(sblock.isEmpty());
        assertTrue(sblock.getSpecimen(0) == specimen1);
        assertTrue(sblock.getSpecimen(1) == specimen2);

        b = sblock.removeSpecimens(5);
        assertTrue(b);
        assertTrue(sblock.size() == 0);
        assertTrue(sblock.isEmpty());

        b = sblock.removeSpecimens(5);
        assertFalse(b);
        assertTrue(sblock.size() == 0);
        assertTrue(sblock.isEmpty());

        // test clearSpecimens

        sblock.addSpecimen(specimen1);
        sblock.addSpecimen(specimen2);
        sblock.addSpecimen(specimen3);
        assertTrue(sblock.size() == 3);

        sblock.clearSpecimens();
        assertTrue(sblock.size() == 0);
        assertTrue(sblock.isEmpty());

    }   //  end basicAccessToSequences()

    private Collection<Predictor> makePredictorList()
    {
        boolean b;

        LinkedList<Predictor> predictorList = new LinkedList<Predictor>();

        Predictor p;

        p = new Predictor(
            11111, 
            Predictor.Status.ACTIVE, 
            Predictor.Type.CATEGORICAL,
            Predictor.Level.INTERVAL);
        b = predictorList.add(p);
        assertTrue(b);

        p = new Predictor(
            22222, 
            Predictor.Status.INACTIVE, 
            Predictor.Type.CONTINUOUS,
            Predictor.Level.NOMINAL);
        b = predictorList.add(p);
        assertTrue(b);

        p = new Predictor(
            33333, 
            Predictor.Status.ACTIVE, 
            Predictor.Type.CATEGORICAL,
            Predictor.Level.ORDINAL);
        b = predictorList.add(p);
        assertTrue(b);

        p = new Predictor(
            44444, 
            Predictor.Status.INACTIVE, 
            Predictor.Type.CONTINUOUS,
            Predictor.Level.RATIO);
        b = predictorList.add(p);
        assertTrue(b);

        return predictorList;

    }   //  end makePredictorList


    private Collection<Specimen> makeSpecimenList()
    {
        boolean b;

        LinkedList<Specimen> specimenList = new LinkedList<Specimen>();

        List<Cell> list = Collections.emptyList();

        Specimen p;

        p = new Specimen(11111, list);
        b = specimenList.add(p);
        assertTrue(b);

        p = new Specimen(22222, list);
        p.setStatus(Specimen.Status.INACTIVE);
        b = specimenList.add(p);
        assertTrue(b);

        p = new Specimen(33333, list);
        b = specimenList.add(p);
        assertTrue(b);

        p = new Specimen(44444, list);
        p.setStatus(Specimen.Status.INACTIVE);
        b = specimenList.add(p);
        assertTrue(b);

        return specimenList;

    }   //  end makeSpecimenList


    private void checkPredictorSequence(PredictorBlock block)
    {
        assertTrue(block.size() == 4);
        assertFalse(block.isEmpty());

        // test getPredictorSequence -- use an Iterator

        List<Predictor> predictors = block.getPredictorSequence();

        int i = 0;

        for (Iterator<Predictor> iter = predictors.iterator();  iter.hasNext(); )
        {
            Predictor p = iter.next();

            verifyPredictor(p, ++i);
        } 

        assertTrue(i == 4);

        // test getPredictorSequence -- use a for-loop with indices

        for (i = 0;  i < block.size();  ++i)
        {
            Predictor p = block.getPredictor(i);
            verifyPredictor(p, i+1);
        }

        assertTrue(i == 4);

        // test getPredictorSequence -- use a for-each-loop

        i = 0;

        for (Predictor p : block.getPredictorSequence())
        {
            verifyPredictor(p, ++i);
        }

        assertTrue(i == 4);

    }   //  end checkPredictorSequence


    private void checkSpecimenSequence(SpecimenBlock block)
    {
        assertTrue(block.size() == 4);
        assertFalse(block.isEmpty());

        // test getSpecimenSequence -- use an Iterator

        List<Specimen> specimens = block.getSpecimenSequence();

        int i = 0;

        for (Iterator<Specimen> iter = specimens.iterator();  iter.hasNext(); )
        {
            Specimen s = iter.next();

            verifySpecimen(s, ++i);
        } 

        assertTrue(i == 4);

        // test getSpecimenSequence -- use a for-loop with indices

        for (i = 0;  i < block.size();  ++i)
        {
            Specimen s = block.getSpecimen(i);
            verifySpecimen(s, i+1);
        }

        assertTrue(i == 4);

        // test getSpecimenSequence -- use a for-each-loop

        i = 0;

        for (Specimen s : block.getSpecimenSequence())
        {
            verifySpecimen(s, ++i);
        }

        assertTrue(i == 4);

    }   //  end checkSpecimenSequence


    private void verifyPredictor(Predictor p, int i)
    {
        switch (i)
        {
        case 1:  assertTrue(p.getPredictorName() == 11111);
                 assertTrue(p.getStatus() == Predictor.Status.ACTIVE);
                 assertTrue(p.getType()   == Predictor.Type.  CATEGORICAL);
                 assertTrue(p.getLevel()  == Predictor.Level. INTERVAL);
                 break;

        case 2:  assertTrue(p.getPredictorName() == 22222);
                 assertTrue(p.getStatus() == Predictor.Status.INACTIVE);
                 assertTrue(p.getType()   == Predictor.Type.  CONTINUOUS);
                 assertTrue(p.getLevel()  == Predictor.Level. NOMINAL);
                 break;

        case 3:  assertTrue(p.getPredictorName() == 33333);
                 assertTrue(p.getStatus() == Predictor.Status.ACTIVE);
                 assertTrue(p.getType()   == Predictor.Type.  CATEGORICAL);
                 assertTrue(p.getLevel()  == Predictor.Level. ORDINAL);
                 break;

        case 4:  assertTrue(p.getPredictorName() == 44444);
                 assertTrue(p.getStatus() == Predictor.Status.INACTIVE);
                 assertTrue(p.getType()   == Predictor.Type.  CONTINUOUS);
                 assertTrue(p.getLevel()  == Predictor.Level. RATIO);
                 break;

        default: assertTrue(false);
        }
    }

    private void verifySpecimen(Specimen p, int i)
    {
        switch (i)
        {
        case 1:  assertTrue(p.getSpecimenKey() == 11111);
                 assertTrue(p.getStatus() == Specimen.Status.ACTIVE);
                 break;

        case 2:  assertTrue(p.getSpecimenKey() == 22222);
                 assertTrue(p.getStatus() == Specimen.Status.INACTIVE);
                 break;

        case 3:  assertTrue(p.getSpecimenKey() == 33333);
                 assertTrue(p.getStatus() == Specimen.Status.ACTIVE);
                 break;

        case 4:  assertTrue(p.getSpecimenKey() == 44444);
                 assertTrue(p.getStatus() == Specimen.Status.INACTIVE);
                 break;

        default: assertTrue(false);
        }
    }

}   //  end class BlockTest
