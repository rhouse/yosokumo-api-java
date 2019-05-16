// StudyTest.java  -  Test the Study class with JUnit

package com.yosokumo.core;

import org.junit.* ;
import static org.junit.Assert.* ;

public class StudyTest
{
    @Test
    public void defaultConstructor()
    {
        Study study = new Study();
        assertTrue(study.getStudyIdentifier().equals(""));
        assertTrue(study.getStudyName().      equals(""));
        assertTrue(study.getStudyLocation()   == null);
        assertTrue(study.getType()            == Study.Type.NUMBER);
        assertTrue(study.getStatus()          == Study.Status.RUNNING);
        assertTrue(study.getVisibility()      == Study.Visibility.PRIVATE);
        assertTrue(study.getOwnerIdentifier() == null); 
        assertTrue(study.getOwnerName()       == null); 
        assertTrue(study.getTableLocation()   == null); 
        assertTrue(study.getModelLocation()   == null); 
        assertTrue(study.getPanelLocation()   == null); 
        assertTrue(study.getRosterLocation()  == null); 
    }

    @Test
    public void usualConstructor()
    {
        Study study = new Study(
            "Study name", 
            Study.Type.RANK, 
            Study.Status.STANDBY,
            Study.Visibility.PUBLIC);
        study.setStudyIdentifier("1234567890abcdef");
        assertTrue(study.getStudyIdentifier().equals("1234567890abcdef"));
        assertTrue(study.getStudyName().      equals("Study name"));
        assertTrue(study.getStudyLocation()   == null);
        assertTrue(study.getType()            == Study.Type.RANK);
        assertTrue(study.getStatus()          == Study.Status.STANDBY);
        assertTrue(study.getVisibility()      == Study.Visibility.PUBLIC);
        assertTrue(study.getOwnerIdentifier() == null); 
        assertTrue(study.getOwnerName()       == null); 
        assertTrue(study.getTableLocation()   == null); 
        assertTrue(study.getModelLocation()   == null); 
        assertTrue(study.getPanelLocation()   == null); 
        assertTrue(study.getRosterLocation()  == null); 
    }

    @Test
    public void settersAndGetters()
    {
        Study study = new Study();

        study.setStudyIdentifier("study8identifier");
        study.setStudyName      ("study name");
        study.setStudyLocation  ("study location");
        study.setType           (Study.Type.CLASS);
        study.setStatus         (Study.Status.STOPPED);
        study.setVisibility     (Study.Visibility.PRIVATE);
        study.setOwnerIdentifier("owner8identifier");
        study.setOwnerName      ("owner name");
        study.setTableLocation  ("table location");
        study.setModelLocation  ("model location");
        study.setPanelLocation  ("panel location");
        study.setRosterLocation ("roster location");

        assertTrue(study.getStudyIdentifier().equals("study8identifier"));
        assertTrue(study.getStudyName()      .equals("study name"));
        assertTrue(study.getStudyLocation()  .equals("study location"));
        assertTrue(study.getType()            == Study.Type.CLASS);
        assertTrue(study.getStatus()          == Study.Status.STOPPED);
        assertTrue(study.getVisibility()      == Study.Visibility.PRIVATE);
        assertTrue(study.getOwnerIdentifier().equals("owner8identifier"));
        assertTrue(study.getOwnerName()      .equals("owner name"));
        assertTrue(study.getTableLocation()  .equals("table location"));
        assertTrue(study.getModelLocation()  .equals("model location"));
        assertTrue(study.getPanelLocation()  .equals("panel location"));
        assertTrue(study.getRosterLocation() .equals("roster location"));
    }

    @Test
    public void isValid()
    {
        Study study = new Study();

        study.setStudyIdentifier("study8identifier");
        study.setStudyName      ("study name");
        study.setStudyLocation  ("study location");
        study.setType           (Study.Type.CLASS);
        study.setStatus         (Study.Status.STOPPED);
        study.setVisibility     (Study.Visibility.PUBLIC);
        study.setOwnerIdentifier("owner8identifier");
        study.setOwnerName      ("owner name");
        study.setTableLocation  ("table location");
        study.setModelLocation  ("model location");
        study.setPanelLocation  ("panel location");
        study.setRosterLocation ("roster location");

        study.setStudyIdentifier("too short");
        study.setStudyIdentifier("study8identifier");

        study.setStudyName      ("\n");
        study.setStudyName      ("study name");

        study.setStudyLocation  ("");
        study.setStudyLocation  ("study location");

        study.setOwnerIdentifier("too short");
        study.setOwnerIdentifier("owner8identifier");

        study.setOwnerName      ("\n");
        study.setOwnerName      ("owner name");

        study.setTableLocation  ("");
        study.setTableLocation  ("table location");

        study.setModelLocation  ("");
        study.setModelLocation  ("model location");

        study.setPanelLocation  ("");
        study.setPanelLocation  ("panel location");

        study.setRosterLocation  ("");
        study.setRosterLocation  ("roster location");
    }

}   //  end class StudyTest
