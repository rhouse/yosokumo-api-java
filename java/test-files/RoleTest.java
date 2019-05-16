// RoleTest.java  -  Test the Role class with JUnit

package com.yosokumo.core;

import org.junit.* ;
import static org.junit.Assert.* ;

public class RoleTest
{
    public boolean allPrivileges(Role role)
    {
        assertTrue(role.getPrivilege(Role.Privilege.GET_STUDY   ));
        assertTrue(role.getPrivilege(Role.Privilege.DELETE_STUDY));
        assertTrue(role.getPrivilege(Role.Privilege.GET_ROSTER  ));
        assertTrue(role.getPrivilege(Role.Privilege.POST_ROSTER ));
        assertTrue(role.getPrivilege(Role.Privilege.GET_ROLE    ));
        assertTrue(role.getPrivilege(Role.Privilege.PUT_ROLE    ));
        assertTrue(role.getPrivilege(Role.Privilege.DELETE_ROLE ));
        assertTrue(role.getPrivilege(Role.Privilege.GET_PANEL   ));
        assertTrue(role.getPrivilege(Role.Privilege.GET_CONTROL ));
        assertTrue(role.getPrivilege(Role.Privilege.PUT_CONTROL ));
        assertTrue(role.getPrivilege(Role.Privilege.POST_TABLE  ));
        assertTrue(role.getPrivilege(Role.Privilege.GET_MODEL   ));
        assertTrue(role.getPrivilege(Role.Privilege.POST_MODEL  ));

        return true;
    }

    public boolean noPrivileges(Role role)
    {
        assertFalse(role.getPrivilege(Role.Privilege.GET_STUDY   ));
        assertFalse(role.getPrivilege(Role.Privilege.DELETE_STUDY));
        assertFalse(role.getPrivilege(Role.Privilege.GET_ROSTER  ));
        assertFalse(role.getPrivilege(Role.Privilege.POST_ROSTER ));
        assertFalse(role.getPrivilege(Role.Privilege.GET_ROLE    ));
        assertFalse(role.getPrivilege(Role.Privilege.PUT_ROLE    ));
        assertFalse(role.getPrivilege(Role.Privilege.DELETE_ROLE ));
        assertFalse(role.getPrivilege(Role.Privilege.GET_PANEL   ));
        assertFalse(role.getPrivilege(Role.Privilege.GET_CONTROL ));
        assertFalse(role.getPrivilege(Role.Privilege.PUT_CONTROL ));
        assertFalse(role.getPrivilege(Role.Privilege.POST_TABLE  ));
        assertFalse(role.getPrivilege(Role.Privilege.GET_MODEL   ));
        assertFalse(role.getPrivilege(Role.Privilege.POST_MODEL  ));

        return true;
    }

    @Test
    public void defaultConstructor()
    {
        Role role = new Role(null, null);

        assertTrue(role.getRoleLocation()    == null);
        assertTrue(role.getUserIdentifier()  == null);
        assertTrue(role.getUserName().       equals(""));
        assertTrue(role.getStudyIdentifier() == null);
        assertTrue(role.getStudyName().      equals(""));

        assertTrue(noPrivileges(role));
    }

    @Test
    public void userIdentifierConstructor()
    {
        String userId = "user identifier 1";

        Role role = new Role(userId, null);

        assertTrue(role.getRoleLocation()    == null);
        assertTrue(role.getUserIdentifier(). equals(userId));
        assertTrue(role.getUserName().       equals(""));
        assertTrue(role.getStudyIdentifier() == null);
        assertTrue(role.getStudyName().      equals(""));

        assertTrue(noPrivileges(role));
    }

    @Test
    public void userAndStudyIdentifierConstructor()
    {
        String userId   = "user identifier 2";
        String studyId  = "study identifier 2";

        Role role = new Role(userId, studyId);

        assertTrue(role.getRoleLocation()    == null);
        assertTrue(role.getUserIdentifier(). equals(userId));
        assertTrue(role.getUserName().       equals(""));
        assertTrue(role.getStudyIdentifier().equals(studyId));
        assertTrue(role.getStudyName().      equals(""));

        assertTrue(noPrivileges(role));
    }

    @Test
    public void settersAndGetters()
    {
        String location = "location 2";
        String userId   = "user identifier 2";
        String userName = "user name 2";
        String studyId  = "study identifier 2";
        String studyName= "study name 2";

        Role role = new Role(null, null);

        role.setRoleLocation   (location );
        role.setUserIdentifier (userId   );
        role.setUserName       (userName );
        role.setStudyIdentifier(studyId  );
        role.setStudyName      (studyName);

        assertTrue(role.getRoleLocation().   equals(location ));
        assertTrue(role.getUserIdentifier(). equals(userId   ));
        assertTrue(role.getUserName().       equals(userName ));
        assertTrue(role.getStudyIdentifier().equals(studyId  ));
        assertTrue(role.getStudyName().      equals(studyName));
    }

    @Test
    public void addAndRemovePrivileges()
    {
        Role role = new Role("my user", "my study");
        role.addPrivilege            (Role.Privilege.GET_STUDY );
        assertTrue(role.getPrivilege (Role.Privilege.GET_STUDY));
        role.removePrivilege         (Role.Privilege.GET_STUDY );
        assertFalse(role.getPrivilege(Role.Privilege.GET_STUDY));

        role.addPrivilege            (Role.Privilege.DELETE_STUDY );
        assertTrue(role.getPrivilege (Role.Privilege.DELETE_STUDY));
        role.removePrivilege         (Role.Privilege.DELETE_STUDY );
        assertFalse(role.getPrivilege(Role.Privilege.DELETE_STUDY));

        role.addPrivilege            (Role.Privilege.GET_ROSTER );
        assertTrue(role.getPrivilege (Role.Privilege.GET_ROSTER));
        role.removePrivilege         (Role.Privilege.GET_ROSTER );
        assertFalse(role.getPrivilege(Role.Privilege.GET_ROSTER));

        role.addPrivilege            (Role.Privilege.POST_ROSTER );
        assertTrue(role.getPrivilege (Role.Privilege.POST_ROSTER));
        role.removePrivilege         (Role.Privilege.POST_ROSTER );
        assertFalse(role.getPrivilege(Role.Privilege.POST_ROSTER));

        role.addPrivilege            (Role.Privilege.GET_ROLE );
        assertTrue(role.getPrivilege (Role.Privilege.GET_ROLE));
        role.removePrivilege         (Role.Privilege.GET_ROLE );
        assertFalse(role.getPrivilege(Role.Privilege.GET_ROLE));

        role.addPrivilege            (Role.Privilege.PUT_ROLE );
        assertTrue(role.getPrivilege (Role.Privilege.PUT_ROLE));
        role.removePrivilege         (Role.Privilege.PUT_ROLE );
        assertFalse(role.getPrivilege(Role.Privilege.PUT_ROLE));

        role.addPrivilege            (Role.Privilege.DELETE_ROLE );
        assertTrue(role.getPrivilege (Role.Privilege.DELETE_ROLE));
        role.removePrivilege         (Role.Privilege.DELETE_ROLE );
        assertFalse(role.getPrivilege(Role.Privilege.DELETE_ROLE));

        role.addPrivilege            (Role.Privilege.GET_PANEL );
        assertTrue(role.getPrivilege (Role.Privilege.GET_PANEL));
        role.removePrivilege         (Role.Privilege.GET_PANEL );
        assertFalse(role.getPrivilege(Role.Privilege.GET_PANEL));

        role.addPrivilege            (Role.Privilege.GET_CONTROL );
        assertTrue(role.getPrivilege (Role.Privilege.GET_CONTROL));
        role.removePrivilege         (Role.Privilege.GET_CONTROL );
        assertFalse(role.getPrivilege(Role.Privilege.GET_CONTROL));

        role.addPrivilege            (Role.Privilege.PUT_CONTROL );
        assertTrue(role.getPrivilege (Role.Privilege.PUT_CONTROL));
        role.removePrivilege         (Role.Privilege.PUT_CONTROL );
        assertFalse(role.getPrivilege(Role.Privilege.PUT_CONTROL));

        role.addPrivilege            (Role.Privilege.POST_TABLE );
        assertTrue(role.getPrivilege (Role.Privilege.POST_TABLE));
        role.removePrivilege         (Role.Privilege.POST_TABLE );
        assertFalse(role.getPrivilege(Role.Privilege.POST_TABLE));

        role.addPrivilege            (Role.Privilege.GET_MODEL );
        assertTrue(role.getPrivilege (Role.Privilege.GET_MODEL));
        role.removePrivilege         (Role.Privilege.GET_MODEL );
        assertFalse(role.getPrivilege(Role.Privilege.GET_MODEL));

        role.addPrivilege            (Role.Privilege.POST_MODEL );
        assertTrue(role.getPrivilege (Role.Privilege.POST_MODEL));
        role.removePrivilege         (Role.Privilege.POST_MODEL );
        assertFalse(role.getPrivilege(Role.Privilege.POST_MODEL));

        assertTrue(noPrivileges(role));
    }

    @Test
    public void allAndNoPrivileges()
    {
        Role role = new Role("my user", "my study");

        assertTrue(noPrivileges(role));

        role.addAllPrivileges();
        assertTrue(allPrivileges(role));

        role.removeAllPrivileges();
        assertTrue(noPrivileges(role));
    }

}   //  end class RoleTest
