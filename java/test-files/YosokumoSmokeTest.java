// YosokumoSmokeTest.java

package com.yosokumo.core.test;

import com.yosokumo.core.*;

import java.util.List;
import java.util.ArrayList;
import java.io.*;

/**
 * A quick test of all Yosokumo classes.
 *
 * @author  Roger House
 * @version 0.9
 */
public class YosokumoSmokeTest
{
    static private String userIdFileName;
    static private String keyFileName;

    static private String userId;
    static private String otherUserId;

    static private String hostName = "hermes.yosokumo.ws";
    static private int    port     = 80;

    static Credentials credentials = null;

    private static void displayUsage()
    {
        System.out.println();
        System.out.println("Usage:");
        System.out.println();
        System.out.println("        YosokumoSmokeTest  user-id-filename  key-filename");
        System.out.println();
        System.out.println("where user-id-filename contains two Yosokumo user ids, one per line, and");
        System.out.println("key-filename contains the 64-byte key for the first user.");

    }

    public static void main(String[] args)
    {
    // Process the command line arguments

        if (args.length != 2)
        {
            displayUsage();
            return;
        }

        userIdFileName = args[0];
        keyFileName    = args[1];

    // Let the fun begin ...

        System.out.println();
        System.out.println("Begin YosokumoSmokeTest");

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
            System.out.println("End YosokumoSmokeTest ***** failure *****");
            return;
        }

// Don't be too eager to print credentials
//        System.out.print(credentials.toString());

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
            System.out.println("End YosokumoSmokeTest ***** failure *****");
            System.exit(1);
        }
 
    // The end

        System.out.println();
        System.out.println("End YosokumoSmokeTest");

    }   //  end main


    private static void theMainTest() throws ServiceException
    {
        // Get catalog from the server

        Service service = new Service(credentials, hostName, port);

        // Delete studies from the beginning of the catalog

        int num_to_delete = 0;  // Change to no. of studies to delete
        int ii = 0;
        while (ii < num_to_delete && deleteFirstStudy(service))
            ++ii;

        System.out.println();
        System.out.println("obtainCatalog test:");

        Catalog catalog = service.obtainCatalog();

        System.out.print(catalog.toString());

        System.out.println("Here are all the studies in the catalog:");

        if (catalog.isEmpty())
            System.out.println("  <no studies in the catalog>");
        else
            for (Study s : catalog.getStudyCollection())
                System.out.println(s.toString());

        System.out.println();
        System.out.println("createStudy test:");

        // Post a study to the catalog

        System.out.println();
        System.out.println("  Post a Study to the catalog:");

        int i = 2;

//        String number = new String("" + i);
//        String studyName = "Sebastopol study " + number;
        String studyName = "Sebastopol study " + i;

        Study study = service.createStudy(
            studyName,
            Study.Type.CLASS,
            Study.Status.RUNNING,
            Study.Visibility.PUBLIC);

        System.out.print(study.toString());

        // Get a study

        System.out.println();
        System.out.println("obtainStudy test:");

        Study study2 = service.obtainStudy(study.getStudyIdentifier());

        assert study2.getStudyName().equals(study.getStudyName());
        assert study2.getType().equals(study.getType());
        assert study2.getStatus().equals(study.getStatus());

        // Change name, status, and visibility of a study

        String           newName       = "Changed Sebastopol study name";
        Study.Status     newStatus     = Study.Status.RUNNING;
        Study.Visibility newVisibility = Study.Visibility.PUBLIC;

        System.out.println();
        System.out.println("updateStudy test:");

        // Put a Study to the catalog

        System.out.println();
        System.out.println("  Update a Study:");

        String studyId = study.getStudyIdentifier();

        study = service.updateStudy(studyId, newName);
        study = service.updateStudy(studyId, newStatus);
        study = service.updateStudy(studyId, newVisibility);

        System.out.print(study.toString());

        System.out.println();
        System.out.println("obtainStudy test:");

        study2 = service.obtainStudy(study.getStudyIdentifier());

        assert study2.getStudyName().equals(study.getStudyName());
        assert study2.getType().equals(study.getType());
        assert study2.getStatus().equals(study.getStatus());

        // Get a roster

        System.out.println();
        System.out.println("obtainRoster test:");

        Roster roster = service.obtainRoster(study.getStudyIdentifier());

        System.out.println(roster.toString());

        for (Role r : roster.getRoleCollection())
            System.out.println(r.toString());

        // Post a role to the roster

        Role role = new Role(null, null);

        role.setUserIdentifier(otherUserId);
        role.setStudyIdentifier(roster.getStudyIdentifier());

        // Add every other privilege

        role.addPrivilege(Role.Privilege.GET_STUDY   );
        role.addPrivilege(Role.Privilege.GET_ROSTER  );
        role.addPrivilege(Role.Privilege.GET_ROLE    );
        role.addPrivilege(Role.Privilege.DELETE_ROLE );
        role.addPrivilege(Role.Privilege.GET_CONTROL );
        role.addPrivilege(Role.Privilege.POST_TABLE  );
        role.addPrivilege(Role.Privilege.POST_MODEL  );

        System.out.println("Role to be created:");
        System.out.println(role.toString());

        // Replace role with what gets filled in by the server

        System.out.println();
        System.out.println("createRole test:");

        // Post a Role to the roster

        System.out.println();
        System.out.println("  Post a Role to the roster:");

        role = service.createRole(role);

        System.out.print(role.toString());

        // See how the roster has changed

        System.out.println("The roster after a role has been added to it:");
        roster = service.obtainRoster(study.getStudyIdentifier());
        System.out.println(roster.toString());

        for (Role r : roster.getRoleCollection())
            System.out.println(r.toString());


        // Put an updated role

        // Remove all the privileges assigned above:
        role.removePrivilege(Role.Privilege.GET_STUDY   );
        role.removePrivilege(Role.Privilege.GET_ROSTER  );
        role.removePrivilege(Role.Privilege.GET_ROLE    );
        role.removePrivilege(Role.Privilege.DELETE_ROLE );
        role.removePrivilege(Role.Privilege.GET_CONTROL );
        role.removePrivilege(Role.Privilege.POST_TABLE  );
        role.removePrivilege(Role.Privilege.POST_MODEL  );

        // Add other privileges:
        role.addPrivilege(Role.Privilege.DELETE_STUDY );
        role.addPrivilege(Role.Privilege.POST_ROSTER  );
        role.addPrivilege(Role.Privilege.PUT_ROLE     );
        role.addPrivilege(Role.Privilege.GET_PANEL    );
        role.addPrivilege(Role.Privilege.PUT_CONTROL  );
        role.addPrivilege(Role.Privilege.GET_MODEL    );

        System.out.println();
        System.out.println("updateRole test:");

        // Update a Role

        System.out.println();
        System.out.println("  Update a Role:");
        System.out.print(role.toString());

        Message message = service.updateRole(role);

        System.out.println("    Message returned by updateRole:");
        if (message == null)
            System.out.println("      <none>");
        else
            System.out.print(message.toString());

        // Get a role

        System.out.println();
        System.out.println("obtainRole test:");

        // Get a role from the server

        Role role2 = service.obtainRole(role.getUserIdentifier(), 
                                                role.getStudyIdentifier());

        assert role2.getUserName().equals(role.getUserName());
        assert role2.getStudyName().equals(role.getStudyName());
        assert role2.getPrivilege(Role.Privilege.GET_STUDY) ==  
                role.getPrivilege(Role.Privilege.GET_STUDY);

        // Delete a Role

        System.out.println();
        System.out.println("  Delete a Role from a roster:");

        service.deleteRole(role.getUserIdentifier(), 
                                                role.getStudyIdentifier());

        System.out.println("    deleteRole returned okay");

        // Update the role of the owner of the study

        System.out.println(" Update the role of the owner of the study");

        role.setUserIdentifier(study.getOwnerIdentifier());
        role.setUserName(study.getOwnerName());
        System.out.print(role.toString());

        message = service.updateRole(role);

        System.out.println("    Message returned by updateRole:");
        if (message == null)
            System.out.println("      <none>");
        else
            System.out.print(message.toString());

        role = service.obtainRole(role.getUserIdentifier(), 
                                                role.getStudyIdentifier());
        System.out.print("Actual role after update");
        System.out.print(role.toString());

        // Now give the owner all privileges

        System.out.println("Give the owner all privileges");
        role.addAllPrivileges();
        message = service.updateRole(role);

        System.out.println("    Message returned by updateRole:");
        if (message == null)
            System.out.println("      <none>");
        else
            System.out.print(message.toString());

        role = service.obtainRole(role.getUserIdentifier(), 
                                                role.getStudyIdentifier());
        System.out.print("Actual role after update");
        System.out.print(role.toString());

        // Describe predictors

        List<Predictor> plist = new ArrayList<Predictor>();

        System.out.println();
        System.out.println("  Describe predictors:  Empty list of predictors");

        service.describePredictors(study.getStudyIdentifier(), plist);

        System.out.println("    describePredictors returned okay:  " +
            "status code = " + service.getStatusCode());
        assert(service.getStatusCode() == 202);

        plist.add(new Predictor(11, Predictor.Status.ACTIVE,   
                        Predictor.Type.CATEGORICAL, Predictor.Level.INTERVAL));
        plist.add(new Predictor(22, Predictor.Status.INACTIVE, 
                        Predictor.Type.CONTINUOUS,  Predictor.Level.RATIO));
        plist.add(new Predictor(33, Predictor.Status.ACTIVE,   
                        Predictor.Type.CATEGORICAL, Predictor.Level.NOMINAL));
        plist.add(new Predictor(44, Predictor.Status.ACTIVE,   
                        Predictor.Type.CATEGORICAL, Predictor.Level.NOMINAL));
        plist.add(new Predictor(55, Predictor.Status.ACTIVE,   
                        Predictor.Type.CATEGORICAL, Predictor.Level.NOMINAL));

        System.out.println();
        System.out.println("  Describe predictors:  List of predictors");

        for (Predictor p : plist)
            System.out.println(p.toString());

        service.describePredictors(study.getStudyIdentifier(), plist);

        System.out.println("    describePredictors returned okay:  " +
            "status code = " + service.getStatusCode());
        assert(service.getStatusCode() == 202);

        // Change the status code to 200
        study2 = service.obtainStudy(study.getStudyIdentifier());
        System.out.println("    obtainStudy returned okay:  " +
            "status code = " + service.getStatusCode());
        assert(service.getStatusCode() == 200);

        // Load subjects

        List<Specimen> slist = new ArrayList<Specimen>();

        System.out.println();
        System.out.println("  Load subjects:  Empty list of subjects");

        service.loadSubjects(study.getStudyIdentifier(), slist);

        System.out.println("    loadSubjects returned okay:  " +
            "status code = " + service.getStatusCode());
        assert(service.getStatusCode() == 202);

        List<Cell> cellseq = new ArrayList<Cell>();

        // First specimen
        cellseq.add(new Cell(1, new IntegerValue(314)));
        cellseq.add(new Cell(2, new RealValue(3.14)));
        cellseq.add(new Cell(3, new IntegerValue(27)));
        Specimen specimen1 = new Specimen(1, Specimen.Status.ACTIVE, 1, 
                                        new IntegerValue(14), cellseq);
        slist.add(specimen1);

        // Second specimen
        cellseq.clear();
        cellseq.add(new Cell(1, new IntegerValue(1001)));
        cellseq.add(new Cell(2, new RealValue(10.01)));
        cellseq.add(new Cell(3, new IntegerValue(2002)));
        Specimen specimen2 = new Specimen(2, Specimen.Status.ACTIVE, 10, 
                                        new IntegerValue(1400), cellseq);
        slist.add(specimen2);

        // Third specimen
        cellseq.clear();
        cellseq.add(new Cell(1, new IntegerValue(1001001001)));
        Specimen specimen3 = new Specimen(3, Specimen.Status.ACTIVE, 100, 
                                        new IntegerValue(14001400), cellseq);
        slist.add(specimen3);

        System.out.println();
        System.out.println("Load subjects:  Specimens input to loadSubjects");
        for (Specimen s : slist)
            System.out.println(s.toString());

        service.loadSubjects(study.getStudyIdentifier(), slist);

        System.out.println("    loadSubjects returned okay:  " +
            "status code = " + service.getStatusCode());
        assert(service.getStatusCode() == 202);

        // Score prospects

        System.out.println();
        System.out.println("  Score prospects:  Empty list of subjects");

        List<Specimen> elist = new ArrayList<Specimen>();

        service.scoreProspects(study.getStudyIdentifier(), elist);

        System.out.println("    scoreProspects returned okay:  " +
            "status code = " + service.getStatusCode());
        assert(service.getStatusCode() == 204);

        System.out.println();
        System.out.println("  Score prospects:  Specimens input to " +
                                                        "scoreProspects");
        for (Specimen s : slist)
            System.out.println(s.toString());

        service.scoreProspects(study.getStudyIdentifier(), slist);

        System.out.println("    scoreProspects returned okay:  " +
            "status code = " + service.getStatusCode());
        assert(service.getStatusCode() == 200);


        System.out.println("Predictands output from scoreProspects");
        for (Specimen s : slist)
            System.out.println("  Specimen:  key=" + s.getSpecimenKey() + 
                "  predictand=" + s.getPredictand());


        // Obtain the study again to see if the number of blocks has changed

        System.out.println();
        System.out.println("obtainStudy test:");

        study = service.obtainStudy(study.getStudyIdentifier());

        System.out.println("The study after a call of scoreProspects:");
        System.out.println(study.toString());

        // Delete a Study from the server (the one we just posted)

        System.out.println();
        System.out.println("  Delete a Study from the catalog:");

        service.deleteStudy(study.getStudyIdentifier());

        System.out.println("    deleteStudy returned okay ");

        // Delete studies from the end of the catalog

        num_to_delete = 0;  // Change to no. of studies to delete
        ii = 0;
        while (ii < num_to_delete && deleteLastStudy(service))
            ++ii;

    }   //  end theMainTest


    private static boolean deleteLastStudy(Service service)
        throws ServiceException
    {
        Catalog catalog = service.obtainCatalog();

        if (catalog.isEmpty())
            return false;

        int n = catalog.size();
        String[] studyIdArray = 
                    catalog.getStudyIdentifiersSet().toArray(new String[0]);
        String lastStudyId = studyIdArray[n-1];

        System.out.println();
        System.out.println("Delete the last Study from the catalog:");
        System.out.println("  studyIdentifier:  " + lastStudyId);

        service.deleteStudy(lastStudyId);

        return true;

    }   //  end deleteLastStudy

    private static boolean deleteFirstStudy(Service service)
        throws ServiceException
    {
        Catalog catalog = service.obtainCatalog();

        if (catalog.isEmpty())
            return false;

        String[] studyIdArray = 
                    catalog.getStudyIdentifiersSet().toArray(new String[0]);
        String firstStudyId = studyIdArray[0];

        System.out.println();
        System.out.println("Delete the first Study from the catalog:");
        System.out.println("  studyIdentifier:  " + firstStudyId);

        service.deleteStudy(firstStudyId);

        return true;

    }   //  end deleteFirstStudy

    private static boolean loadUserIdFile(String userIdFileName)
    {
        userId      = null;
        otherUserId = null;

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
                if (userId == null)
                    userId = inLine;
                else if (otherUserId == null)
                    otherUserId = inLine;
                else
                    return false;   // too many user ids in the file
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

}   //  end class YosokumoSmokeTest

// end YosokumoSmokeTest.java
