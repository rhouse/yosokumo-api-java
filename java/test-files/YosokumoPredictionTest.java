// YosokumoPredictionTest.java

package com.yosokumo.core.test;

import com.yosokumo.core.*;

import java.util.List;
import java.util.ArrayList;
import java.io.*;

/**
 * A test of Yosokumo's prediction features.
 *
 * @author  Roger House
 * @version 0.9
 */
public class YosokumoPredictionTest
{
    static private String userIdFileName;
    static private String keyFileName;
    static private String dataFileName;

    static private String userId;

    static private String hostName = "hermes.yosokumo.ws";
    static private int    port     = 80;

    static private String mainStudyName = "Suburb Project";
    static private String mainStudyId   = "";
    static private Study  mainStudy     = null;
    static private Service service      = null;

    static private List<Specimen> slist = new ArrayList<Specimen>();

    static private Credentials credentials = null;

    static private List<SuburbProfile> splist = new ArrayList<SuburbProfile>();

    private static void displayUsage()
    {
        System.out.println();
        System.out.println("Usage:");
        System.out.println();
        System.out.println("        YosokumoPredictionTest  user-id-filename  key-filename  data-filename");
        System.out.println();
        System.out.println("where"); 
        System.out.println();
        System.out.println("    user-id-filename contains one Yosokumo user id");
        System.out.println("    key-filename     contains the 64-byte key for the user.");
        System.out.println("    data-filename    contains suburb profile data");
    }

    public static void main(String[] args)
    {
    // Process the command line arguments

        if (args.length != 3)
        {
            displayUsage();
            return;
        }

        userIdFileName = args[0];
        keyFileName    = args[1];
        dataFileName   = args[2];

    // Let the fun begin ...

        System.out.println();
        System.out.println("Begin YosokumoPredictionTest");

    // Read the user ids file

        if (!loadUserIdFile(userIdFileName))
        {
            System.out.println();
            System.out.println("Load of user id file " + userIdFileName + " failed");
            System.out.println("End YosokumoPredictionTest ***** failure *****");
            return;
        }

    // Credentials

        byte [] key = getByteFile(keyFileName, Credentials.KEY_LEN);
        if (key == null)
        {
            System.out.println();
            System.out.println("Load of key file " + keyFileName + " failed");
            System.out.println("End YosokumoPredictionTest ***** failure *****");
            return;
        }

        try
        { 
            credentials = new Credentials(userId, key); 
        }
        catch (ServiceException e)
        {
            System.out.println("Error in Credentials ctor:  " + 
                                                           e.getMessage());
            System.out.println("End YosokumoPredictionTest ***** failure *****");
            return;
        }

// Don't be too eager to print credentials
//        System.out.print(credentials.toString());

    // Data file

        if (!loadSuburbDataFile(dataFileName, splist))
        {
            System.out.println();
            System.out.println("***** Load of data file failed *****");
            return;
        }

        System.out.println("Data file read successfully!");
        System.out.println("    It contains " + splist.size() + " records");
        System.out.println("    crimeRate from first record:  " + 
                                                splist.get(0).crimeRate);
        System.out.println("    medHomeValue from last record:  " + 
                                splist.get(splist.size()-1).medHomeValue);

        // Create a list of specimens

        long rowNum = 0;

        slist.clear();

        for (SuburbProfile sp :  splist)
        {
            // Create a cell sequence of SuburbProfile fields:

            List<Cell> cellseq = new ArrayList<Cell>();

            cellseq.add(new Cell( 1, new RealValue   (sp.crimeRate     )));
            cellseq.add(new Cell( 2, new RealValue   (sp.zonedBig      )));
            cellseq.add(new Cell( 3, new RealValue   (sp.industrialArea)));
            cellseq.add(new Cell( 4, new IntegerValue(sp.boundsRiver   )));
            cellseq.add(new Cell( 5, new RealValue   (sp.noxCon        )));
            cellseq.add(new Cell( 6, new RealValue   (sp.numRooms      )));
            cellseq.add(new Cell( 7, new RealValue   (sp.houseAge      )));
            cellseq.add(new Cell( 8, new RealValue   (sp.distWork      )));
            cellseq.add(new Cell( 9, new IntegerValue(sp.accessHwy     )));
            cellseq.add(new Cell(10, new RealValue   (sp.taxRate       )));
            cellseq.add(new Cell(11, new RealValue   (sp.pupilTeacher  )));
            cellseq.add(new Cell(12, new RealValue   (sp.blackPop      )));
            cellseq.add(new Cell(13, new RealValue   (sp.lowerStat     )));

            Value predictand = new RealValue(sp.medHomeValue);

            // Create a specimen (table row) with predictand and 
            //     cell sequence and append it to slist:

            Specimen specimen = new Specimen(++rowNum, predictand, cellseq);

            slist.add(specimen);
        }

    // the Main Test

        try
        {
            theMainTest();
        }
        catch  (ServiceException e)
        {
            System.out.println("  exception:  " + e.getMessage());
            if (e.getCause() != null)
                System.out.println("      cause:  " + 
                                            e.getCause().getMessage());
            e.printStackTrace();
            System.out.println("End YosokumoPredictionTest ***** failure *****");
            System.exit(1);
        }
 
    // The end

        System.out.println();
        System.out.println("End YosokumoPredictionTest");

    }   //  end main


    private static void theMainTest() throws ServiceException
    {
        // Get catalog from the server

        service = new Service(credentials, hostName, port);

        // Obtain the catalog

        Catalog catalog = service.obtainCatalog();

        System.out.println();
        System.out.print(catalog.toString());

        System.out.println("Studies in the catalog:");

        mainStudy = null;

        if (catalog.isEmpty())
            System.out.println("  <no studies in the catalog>");
        else
            for (Study s : catalog.getStudyCollection())
            {
                System.out.println(s.toString());
                if (s.getStudyName().equals(mainStudyName))
                    mainStudy = s;
            }

        if (mainStudy == null)
            setupMainStudy();

        mainStudyId = mainStudy.getStudyIdentifier();

        // Score prospects

//??? Change the numToScore value later
        int numToScore = 5;

        // Remove all but the first numToScore elements from Specimen list

        slist.subList(numToScore, slist.size()).clear();

        System.out.println();
        System.out.println("Score prospects:  Specimens input to " +
                                                        "scoreProspects");
        for (Specimen s : slist)
            System.out.println("  Specimen:  key=" + s.getSpecimenKey() + 
                "  predictand=" + s.getPredictand());

        service.scoreProspects(mainStudyId, slist);

        System.out.println();
        System.out.println("scoreProspects returned okay:  " +
            "status code = " + service.getStatusCode());
        assert(service.getStatusCode() == 200);

        System.out.println();
        System.out.println("Predictands output from scoreProspects");
        for (Specimen s : slist)
            System.out.println("  Specimen:  key=" + s.getSpecimenKey() + 
                "  predictand=" + s.getPredictand());


        // Obtain the study again to see if the number of blocks has changed

        System.out.println();

        mainStudy = service.obtainStudy(mainStudyId);

        System.out.println("The study after a call of scoreProspects:");
        System.out.println(mainStudy.toString());

    }   //  end theMainTest

    private static void setupMainStudy() throws ServiceException
    {
        System.out.println();
        System.out.println("Create the study " + mainStudyName);

        mainStudy = service.createStudy(
            mainStudyName,
            Study.Type.NUMBER,
            Study.Status.RUNNING,
            Study.Visibility.PUBLIC);

        mainStudyId = mainStudy.getStudyIdentifier();

        // Describe predictors

        List<Predictor> plist = new ArrayList<Predictor>();

        plist.add(new Predictor( 1, Predictor.Status.ACTIVE, 
                      Predictor.Type.CONTINUOUS , Predictor.Level.RATIO));
        plist.add(new Predictor( 2, Predictor.Status.ACTIVE, 
                      Predictor.Type.CONTINUOUS , Predictor.Level.RATIO));
        plist.add(new Predictor( 3, Predictor.Status.ACTIVE, 
                      Predictor.Type.CONTINUOUS , Predictor.Level.RATIO));
        plist.add(new Predictor( 4, Predictor.Status.ACTIVE, 
                      Predictor.Type.CATEGORICAL, Predictor.Level.NOMINAL));
        plist.add(new Predictor( 5, Predictor.Status.ACTIVE, 
                      Predictor.Type.CONTINUOUS , Predictor.Level.RATIO));
        plist.add(new Predictor( 6, Predictor.Status.ACTIVE, 
                      Predictor.Type.CONTINUOUS , Predictor.Level.RATIO));
        plist.add(new Predictor( 7, Predictor.Status.ACTIVE, 
                      Predictor.Type.CONTINUOUS , Predictor.Level.RATIO));
        plist.add(new Predictor( 8, Predictor.Status.ACTIVE, 
                      Predictor.Type.CONTINUOUS , Predictor.Level.RATIO));
        plist.add(new Predictor( 9, Predictor.Status.ACTIVE, 
                      Predictor.Type.CATEGORICAL, Predictor.Level.ORDINAL));
        plist.add(new Predictor(10, Predictor.Status.ACTIVE, 
                      Predictor.Type.CONTINUOUS , Predictor.Level.RATIO));
        plist.add(new Predictor(11, Predictor.Status.ACTIVE, 
                      Predictor.Type.CONTINUOUS , Predictor.Level.RATIO));
        plist.add(new Predictor(12, Predictor.Status.ACTIVE, 
                      Predictor.Type.CONTINUOUS , Predictor.Level.RATIO));
        plist.add(new Predictor(13, Predictor.Status.ACTIVE, 
                      Predictor.Type.CONTINUOUS , Predictor.Level.RATIO));
        System.out.println();
        System.out.println("  List of predictors");

        for (Predictor p : plist)
            System.out.println(p.toString());

        service.describePredictors(mainStudyId, plist);

        System.out.println("describePredictors returned okay");
        assert(service.getStatusCode() == 202);

        // Load subjects

        System.out.println("Begin loading subjects to server");

        service.loadSubjects(mainStudyId, slist);
        assert(service.getStatusCode() == 202);

        System.out.println();
        System.out.println("Loaded " + splist.size() + " subjects to server");

        System.out.println();
        System.out.println("Main study after subjects loaded:");

        mainStudy = service.obtainStudy(mainStudyId);

        System.out.print(mainStudy.toString());

    }   //  end setupMainStudy


    private static boolean loadUserIdFile(String userIdFileName)
    {
        userId = null;

        BufferedReader is = null;

        try
        {
            is = new BufferedReader(new FileReader(userIdFileName));

            String inLine;

            while ((inLine = is.readLine()) != null)
            {
                inLine = inLine.trim();
                if (inLine.isEmpty() || inLine.startsWith("//"))
                    continue;
                userId = inLine;
                break;
            }
        }
        catch (FileNotFoundException e)
        {
            return false;
        }
        catch (IOException e)
        {
            return false;
        }
        finally
        {
            try
            {
                if (is != null)
                    is.close();
            }
            catch (Exception e)
            {
                return false;
            }
        }

        return true;
    }

    private static byte[] getByteFile(String byteFileName, int n)
    {
        File f = new File(byteFileName);

        if (!(f.exists() && f.isFile() && f.canRead() && f.length() == n))
            return null;

        DataInputStream ds;

        try
        {
            ds = new DataInputStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            return null;
        }

        try
        {
            byte[] bytes = new byte[n];
            ds.readFully(bytes);
            return bytes;
        }
        catch (IOException e)
        {
            return null;
        }
        finally
        {
            try
            {
                ds.close();
            }
            catch (IOException e)
            {
                assert true; // nothing to do at this point
            }
        }

    } // end getByteFile

    private static boolean loadSuburbDataFile(
        String fileName,
        List<SuburbProfile> splist)
    {
        int numFields = 14;

        BufferedReader is = null;

        splist.clear();

        try
        {
            is = new BufferedReader(new FileReader(fileName));

            int lineNum = 0;

            String inLine;

            while ((inLine = is.readLine()) != null)
            {
                ++lineNum;
                inLine = inLine.trim();
                if (inLine.isEmpty() || inLine.startsWith("//"))
                    continue;

                // Split the input line into fields delimited by one or 
                // more spaces:

                String[] fields = inLine.split("\\s+");

                if (fields.length != numFields)
                {
                    System.out.println("Line " + lineNum + " of " + fileName +
                        " contains " + fields.length + " fields, " + 
                        numFields + " expected");
                    return false;
                }

                SuburbProfile sp = new SuburbProfile();

                sp.crimeRate      = Double. parseDouble(fields[ 0]);
                sp.zonedBig       = Double. parseDouble(fields[ 1]);
                sp.industrialArea = Double. parseDouble(fields[ 2]);
                sp.boundsRiver    = Integer.parseInt   (fields[ 3]);
                sp.noxCon         = Double. parseDouble(fields[ 4]);
                sp.numRooms       = Double. parseDouble(fields[ 5]);
                sp.houseAge       = Double. parseDouble(fields[ 6]);
                sp.distWork       = Double. parseDouble(fields[ 7]);
                sp.accessHwy      = Integer.parseInt   (fields[ 8]);
                sp.taxRate        = Double. parseDouble(fields[ 9]);
                sp.pupilTeacher   = Double. parseDouble(fields[10]);
                sp.blackPop       = Double. parseDouble(fields[11]);
                sp.lowerStat      = Double. parseDouble(fields[12]);
                sp.medHomeValue   = Double. parseDouble(fields[13]);

                splist.add(sp);
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Cannot open " + fileName);
            return false;
        }
        catch (IOException e)
        {
            System.out.println("IOException while reading " + fileName);
            return false;
        }
        finally
        {
            try
            {
                if (is != null)
                    is.close();
            }
            catch (Exception e)
            {
                System.out.println("Exception while closing " + fileName);
                return false;
            }
        }

        return true;

    }   //  end loadSuburbDataFile

}   //  end class YosokumoPredictionTest

// end YosokumoPredictionTest.java
