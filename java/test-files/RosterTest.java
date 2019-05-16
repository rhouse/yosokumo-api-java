// RosterTest.java  -  Test the Roster class with JUnit

package com.yosokumo.core;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class RosterTest
{
    @Test
    public void defaultConstructor()
    {
        Roster roster = new Roster();

        assertTrue(roster.getStudyIdentifier() == null);
        assertTrue(roster.getStudyName()       == null);
        assertTrue(roster.getRosterLocation()  == null);
        assertTrue(roster.size()               == 0);
    }

    @Test
    public void usualConstructor()
    {
        String studyIdentifier = "my study identifier";
        String studyName       = "my study name";

        Roster roster = new Roster(studyIdentifier, studyName);

        assertTrue(roster.getStudyIdentifier().equals(studyIdentifier));
        assertTrue(roster.getStudyName().      equals(studyName));
        assertTrue(roster.getRosterLocation()  == null);
        assertTrue(roster.size()               == 0);
    }

    @Test
    public void settersAndGetters()
    {
        String studyIdentifier = "my study identifier 2";
        String studyName       = "my study name 2";
        String studyLocation   = "my study location 2";

        Roster roster = new Roster();

        roster.setStudyIdentifier (studyIdentifier);
        roster.setStudyName       (studyName);
        roster.setRosterLocation  (studyLocation);

        assertTrue(roster.getStudyIdentifier() .equals(studyIdentifier));
        assertTrue(roster.getStudyName()       .equals(studyName));
        assertTrue(roster.getRosterLocation()  .equals(studyLocation));
    }



    @Test
    public void basicAccessToRoleCollection()
    {
        String userIdentifier1  = "user identifier 1";
        String studyIdentifier1 = "study identifier 1";

        String userIdentifier2  = "user identifier 2";
        String studyIdentifier2 = "study identifier 2";

        String userIdentifier3  = "user identifier 3";
        String studyIdentifier3 = "study identifier 3";

        Role role1 = new Role(userIdentifier1, studyIdentifier1);
        Role role2 = new Role(userIdentifier2, studyIdentifier2);
        Role role3 = new Role(userIdentifier3, studyIdentifier3);

        String rosterStudyIdentifier = "roster study identifier";
        String rosterStudyName       = "roster study name";

        Roster roster = new Roster(rosterStudyIdentifier, rosterStudyName);

        Role r;

        // test addRole, getRole, containsRole, size, and isEmpty

        assertTrue(roster.isEmpty());

        r = roster.addRole(role1);
        assertTrue(r == null);
        assertTrue(roster.size() == 1);
        assertFalse(roster.isEmpty());
        assertTrue(roster.getRole(userIdentifier1) == role1);
        assertTrue(roster.containsRole(userIdentifier1));

        r = roster.addRole(role2);
        assertTrue(r == null);
        assertTrue(roster.size() == 2);
        assertFalse(roster.isEmpty());
        assertTrue(roster.getRole(userIdentifier2) == role2);
        assertTrue(roster.containsRole(userIdentifier2));

        r = roster.addRole(role3);
        assertTrue(r == null);
        assertTrue(roster.size() == 3);
        assertFalse(roster.isEmpty());
        assertTrue(roster.getRole(userIdentifier3) == role3);
        assertTrue(roster.containsRole(userIdentifier3));

        r = roster.addRole(role3);
        assertTrue(r == role3);
        assertTrue(roster.size() == 3);
        assertFalse(roster.isEmpty());
        assertTrue(roster.getRole(userIdentifier3) == role3);

        // test removeRole and clear

        r = roster.removeRole(userIdentifier3);
        assertTrue(r == role3);
        assertTrue(roster.size() == 2);
        assertFalse(roster.isEmpty());
        assertTrue(roster.getRole(userIdentifier3) == null);
        assertFalse(roster.containsRole(userIdentifier3));

        r = roster.removeRole(userIdentifier3);
        assertTrue(r == null);
        assertTrue(roster.size() == 2);
        assertFalse(roster.isEmpty());
        assertTrue(roster.getRole(userIdentifier3) == null);

        roster.clearRoles();
        assertTrue(roster.size() == 0);
        assertTrue(roster.isEmpty());
        assertTrue(roster.getRole(userIdentifier3) == null);

    }   //  end basicAccessToRoleCollection()


    @Test
    public void advancedAccessToRoleCollection()
    {
        String userIdentifier1  = "user identifier 1";
        String studyIdentifier1 = "study identifier 1";

        String userIdentifier2  = "user identifier 2";
        String studyIdentifier2 = "study identifier 2";

        String userIdentifier3  = "user identifier 3";
        String studyIdentifier3 = "study identifier 3";

        Role role1 = new Role(userIdentifier1, studyIdentifier1);
        Role role2 = new Role(userIdentifier2, studyIdentifier2);
        Role role3 = new Role(userIdentifier3, studyIdentifier3);

        String rosterStudyIdentifier = "roster study identifier";
        String rosterStudyName       = "roster study name";

        Roster roster = new Roster(rosterStudyIdentifier, rosterStudyName);

        Role r;

        roster.addRole(role1);
        roster.addRole(role2);
        roster.addRole(role3);

        // test getRoleIdentifiersSet

        Set<String> identifiers = roster.getUserIdentifiersSet();

        int i = 0;

        for (Iterator<String> iter = identifiers.iterator(); iter.hasNext();)
        {
            String t = iter.next();

            switch (++i)
            {
            case 1:  assertTrue(t.equals(userIdentifier1));  break;
            case 2:  assertTrue(t.equals(userIdentifier2));  break;
            case 3:  assertTrue(t.equals(userIdentifier3));  break;
            default: assertTrue(false);
            }
        }

        assertTrue(i == 3);

        // test getRoleCollection

        Collection<Role> roles = roster.getRoleCollection();

        i = 0;

        for (Iterator<Role> iter = roles.iterator();  iter.hasNext(); )
        {
            Role st = iter.next();
            boolean b;

            switch (++i)
            {
            case 1:  b = st.getUserIdentifier().equals(userIdentifier1);
                     assertTrue(b);
                     break;
            case 2:  b = st.getUserIdentifier().equals(userIdentifier2);
                     assertTrue(b);
                     break;
            case 3:  b = st.getUserIdentifier().equals(userIdentifier3);
                     assertTrue(b);
                     break;
            default: assertTrue(false);
            }
        } 

        assertTrue(i == 3);

    }   //  end advancedAccessToRoleCollection



    static String makeUserId(int n)
    {
        // Return a 16-character string of the form 9Zd, where 9 is a 
        // sequence of 9's and d is n as digits:

        String s = String.format("%16d", -n).replace('-', 'Z').
                                                        replace(' ', '9');
        return s;
    }


    @Test
    public void stressTestAccessToRoleCollection()
    {
        int numRoles = 20000;

        Roster roster = new Roster();

        // Create a bunch of roles and put them in a roster

        for (int i = 1;  i <= numRoles;  i++)
        {
            Role role = new Role(makeUserId(i), "study id");
            Role s = roster.addRole(role);
            assertTrue(s == null);
        }

        assertTrue(roster.size() == numRoles);

        // Make sure all the roles can be found

        for (int i = numRoles;  i > 0 ;  i--)
        {
            String userId = makeUserId(i);
            Role role = roster.getRole(userId);
            assertTrue(role != null);
            assertTrue(role.getUserIdentifier().equals(userId));
        }

        // Remove each role from the roster

        for (int i = 1;  i <= numRoles;  i++)
        {
            String userId = makeUserId(i);
            Role role = roster.removeRole(userId);
            assertTrue(role != null);
            assertTrue(role.getUserIdentifier().equals(userId));
        }

        assertTrue(roster.isEmpty());

    }   //  end stressTestAccessToRoleCollection


}   //  end class RosterTest
