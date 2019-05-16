// CatalogTest.java  -  Test the Catalog class with JUnit

package com.yosokumo.core;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class CatalogTest
{
    @Test
    public void defaultConstructor()
    {
        Catalog catalog = new Catalog();
        assertTrue(catalog.getUserIdentifier().equals("ABCDEF0123456789"));
        assertTrue(catalog.getUserName().      equals(""));
        assertTrue(catalog.getCatalogLocation() == null);
        assertTrue(catalog.size()               == 0);
    }

    @Test
    public void usualConstructor()
    {
        Catalog catalog = new Catalog("1234567890abcdef", "Catalog name");
        assertTrue(catalog.getUserIdentifier().equals("1234567890abcdef"));
        assertTrue(catalog.getUserName().      equals("Catalog name"));
        assertTrue(catalog.getCatalogLocation()   == null);
        assertTrue(catalog.size()                 == 0);
    }

    @Test
    public void settersAndGetters()
    {
        Catalog catalog = new Catalog();

        catalog.setUserIdentifier ("user8Identifier9");
        catalog.setUserName       ("user name");
        catalog.setCatalogLocation("catalog location");

        assertTrue(catalog.getUserIdentifier() .equals("user8Identifier9"));
        assertTrue(catalog.getUserName()       .equals("user name"));
        assertTrue(catalog.getCatalogLocation().equals("catalog location"));

    }


    @Test
    public void basicAccessToStudyCollection()
    {
        Study study1 = new Study(
            "Study1 name", 
            Study.Type.RANK, 
            Study.Status.STANDBY,
            Study.Visibility.PUBLIC);
        study1.setStudyIdentifier("9999999999999991");

        Study study2 = new Study(
            "Study2 name", 
            Study.Type.NUMBER, 
            Study.Status.STOPPED,
            Study.Visibility.PRIVATE);
        study2.setStudyIdentifier("9999999999999992");

        Study study3 = new Study(
            "Study3 name", 
            Study.Type.CHANCE, 
            Study.Status.RUNNING,
            Study.Visibility.PUBLIC);
        study3.setStudyIdentifier("9999999999999993");

        Catalog catalog = new Catalog("catalogIdentifie", "catalog name");
        Study s;

        // test addStudy, getStudy, containsStudy, size, and isEmpty

        assertTrue(catalog.isEmpty());

        s = catalog.addStudy(study1);
        assertTrue(s == null);
        assertTrue(catalog.size() == 1);
        assertFalse(catalog.isEmpty());
        assertTrue(catalog.getStudy("9999999999999991") == study1);
        assertTrue(catalog.containsStudy("9999999999999991"));

        s = catalog.addStudy(study2);
        assertTrue(s == null);
        assertTrue(catalog.size() == 2);
        assertFalse(catalog.isEmpty());
        assertTrue(catalog.getStudy("9999999999999992") == study2);
        assertTrue(catalog.containsStudy("9999999999999992"));

        s = catalog.addStudy(study3);
        assertTrue(s == null);
        assertTrue(catalog.size() == 3);
        assertFalse(catalog.isEmpty());
        assertTrue(catalog.getStudy("9999999999999993") == study3);
        assertTrue(catalog.containsStudy("9999999999999993"));

        s = catalog.addStudy(study3);
        assertTrue(s == study3);
        assertTrue(catalog.size() == 3);
        assertFalse(catalog.isEmpty());
        assertTrue(catalog.getStudy("9999999999999993") == study3);

        // test removeStudy and clear

        s = catalog.removeStudy("9999999999999993");
        assertTrue(s == study3);
        assertTrue(catalog.size() == 2);
        assertFalse(catalog.isEmpty());
        assertTrue(catalog.getStudy("9999999999999993") == null);
        assertFalse(catalog.containsStudy("9999999999999993"));

        s = catalog.removeStudy("9999999999999993");
        assertTrue(s == null);
        assertTrue(catalog.size() == 2);
        assertFalse(catalog.isEmpty());
        assertTrue(catalog.getStudy("9999999999999993") == null);

        catalog.clearStudies();
        assertTrue(catalog.size() == 0);
        assertTrue(catalog.isEmpty());
        assertTrue(catalog.getStudy("9999999999999993") == null);

    }   //  end basicAccessToStudyCollection()

    @Test
    public void advancedAccessToStudyCollection()
    {
        Study study1 = new Study(
            "Study1 name", 
            Study.Type.RANK, 
            Study.Status.STANDBY,
            Study.Visibility.PUBLIC);
        study1.setStudyIdentifier("9999999999999991");

        Study study2 = new Study(
            "Study2 name", 
            Study.Type.NUMBER, 
            Study.Status.STOPPED,
            Study.Visibility.PRIVATE);
        study2.setStudyIdentifier("9999999999999992");

        Study study3 = new Study(
            "Study3 name", 
            Study.Type.CHANCE, 
            Study.Status.RUNNING,
            Study.Visibility.PUBLIC);
        study3.setStudyIdentifier("9999999999999993");

        Catalog catalog = new Catalog("catalogIdentifie", "catalog name");
        Study s;

        catalog.addStudy(study1);
        catalog.addStudy(study2);
        catalog.addStudy(study3);

        // test getStudyIdentifiersSet

        Set<String> identifiers = catalog.getStudyIdentifiersSet();

        int i = 0;

        for (Iterator<String> iter = identifiers.iterator(); iter.hasNext();)
        {
            String t = iter.next();

            switch (++i)
            {
            case 1:  assertTrue(t.equals("9999999999999991"));  break;
            case 2:  assertTrue(t.equals("9999999999999992"));  break;
            case 3:  assertTrue(t.equals("9999999999999993"));  break;
            default: assertTrue(false);
            }
        }

        assertTrue(i == 3);

        // test getStudyCollection

        Collection<Study> studies = catalog.getStudyCollection();

        i = 0;

        for (Iterator<Study> iter = studies.iterator();  iter.hasNext(); )
        {
            Study st = iter.next();
            boolean b;

            switch (++i)
            {
            case 1:  b = st.getStudyIdentifier().equals("9999999999999991");
                     assertTrue(b);
                     break;
            case 2:  b = st.getStudyIdentifier().equals("9999999999999992");
                     assertTrue(b);
                     break;
            case 3:  b = st.getStudyIdentifier().equals("9999999999999993");
                     assertTrue(b);
                     break;
            default: assertTrue(false);
            }
        } 

        assertTrue(i == 3);

    }   //  end advancedAccessToStudyCollection


    static String makeStudyId(int n)
    {
        // Return a 16-character string of the form 9Zd, where 9 is a 
        // sequence of 9's and d is n as digits:

        String s = String.format("%16d", -n).replace('-', 'Z').
                                                        replace(' ', '9');
        return s;
    }


    @Test
    public void stressTestAccessToStudyCollection()
    {
        int numStudies = 20000;

        // On an HP Z400 running Windows 7, the total time for all tests
        // when numStudies == 1,000,000 is 27 seconds

        // On an old clunker running Ubuntu 9.04, the total time for all tests
        // when numStudies == 100,000 is 4 seconds

        Catalog catalog = new Catalog();

        // Create a bunch of studies and put them in a catalog

        for (int i = 1;  i <= numStudies;  i++)
        {
            Study study = new Study(
                "study name", 
                Study.Type.RANK, 
                Study.Status.STANDBY,
                Study.Visibility.PUBLIC);
            study.setStudyIdentifier(makeStudyId(i));

            Study s = catalog.addStudy(study);
            assertTrue(s == null);
        }

        assertTrue(catalog.size() == numStudies);

        // Make sure all the studies can be found

        for (int i = numStudies;  i > 0 ;  i--)
        {
            String studyId = makeStudyId(i);
            Study study = catalog.getStudy(studyId);
            assertTrue(study != null);
            assertTrue(study.getStudyIdentifier().equals(studyId));
        }

        // Remove each study from the catalog

        for (int i = 1;  i <= numStudies;  i++)
        {
            String studyId = makeStudyId(i);
            Study study = catalog.removeStudy(studyId);
            assertTrue(study != null);
            assertTrue(study.getStudyIdentifier().equals(studyId));
        }

        assertTrue(catalog.isEmpty());

    }   //  end stressTestAccessToStudyCollection


    @Test
    public void isValid()
    {
        Catalog catalog = new Catalog();

        catalog.setUserIdentifier ("user8Identifier9");
        catalog.setUserName       ("user name");
        catalog.setCatalogLocation("catalog location");

        catalog.setUserIdentifier("too short");
        catalog.setUserIdentifier("user8Identifier9");

        catalog.setUserName      ("\n");
        catalog.setUserName      ("user name");

        catalog.setCatalogLocation  ("");
        catalog.setCatalogLocation  ("catalog location");

        Study study1 = new Study(
            "Study1 name", 
            Study.Type.RANK, 
            Study.Status.STANDBY,
            Study.Visibility.PUBLIC);
        study1.setStudyIdentifier("9999999999999991");

        Study study2 = new Study(
            "Study2 name", 
            Study.Type.RANK, 
            Study.Status.STANDBY,
            Study.Visibility.PUBLIC);
        study2.setStudyIdentifier("9999999999999992");

        catalog.addStudy(study1);
        catalog.addStudy(study2);

        study1.setStudyLocation("");
        study1.setStudyLocation(null);

        study2.setStudyLocation("");
        study2.setStudyLocation(null);
    }

}   //  end class CatalogTest
