// PredictorTest.java  -  Test the Predictor class with JUnit

package com.yosokumo.core;

import org.junit.*;
import static org.junit.Assert.*;

public class PredictorTest
{
    @Test
    public void minimalConstructor()
    {
        Predictor predictor = new Predictor(123456789);
        assertTrue(predictor.getPredictorName() == 123456789);
        assertTrue(predictor.getStatus()        == Predictor.Status.ACTIVE);
        assertTrue(predictor.getType()          == Predictor.Type.CONTINUOUS);
        assertTrue(predictor.getLevel()         == Predictor.Level.RATIO);
    }

    @Test
    public void usualConstructor()
    {
        Predictor predictor = new Predictor(
            987654321, 
            Predictor.Status.INACTIVE,
            Predictor.Type.CATEGORICAL,
            Predictor.Level.NOMINAL);
        assertTrue(predictor.getPredictorName() == 987654321);
        assertTrue(predictor.getStatus()        == Predictor.Status.INACTIVE);
        assertTrue(predictor.getType()          == Predictor.Type.CATEGORICAL);
        assertTrue(predictor.getLevel()         == Predictor.Level.NOMINAL);
    }

    @Test
    public void settersAndGetters()
    {
        Predictor predictor = new Predictor(314159);

        predictor.setPredictorName(951413);
        predictor.setStatus(Predictor.Status.INACTIVE);
        predictor.setType  (Predictor.Type.CATEGORICAL);
        predictor.setLevel (Predictor.Level.ORDINAL);

        assertTrue(predictor.getPredictorName() == 951413);
        assertTrue(predictor.getStatus()        == Predictor.Status.INACTIVE);
        assertTrue(predictor.getType()          == Predictor.Type.CATEGORICAL);
        assertTrue(predictor.getLevel()         == Predictor.Level.ORDINAL);
    }


}   //  end class PredictorTest
