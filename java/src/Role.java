// Role.java

package com.yosokumo.core;

import java.util.BitSet;

/**
 * A set of privileges granted to a particular user for a particular study. 
 * One of the privileges granted to a study creator is the authority to 
 * create new roles on the study for other enrolled users.  In this way, 
 * the study creator can allow others to load data, get predictions, view 
 * study statistics, or even themselves grant privileges on the study to 
 * other users. Roles may be severely limited or provide full access and 
 * may be changed or revoked at any time.
 * <p>
 * When an enrolled user creates a study, he is authorized automatically to 
 * perform any action on that study. More precisely, when a study is created, 
 * the service simultaneously creates a role for the creator on the newly 
 * created study in which all privileges are set to {@code true}. 
 * <p>
 * A single user may have a role on many studies; conversely, many users may 
 * have a role on a single study. The list of studies on which a particular 
 * user has a role is called the catalog for that user. The list of users 
 * that have a role on a particular study is called the roster for that study.
 * <p>
 * A {@code Role} has these attributes:
 * <ul>
 * <li>a user identifier and a user name
 * <li>a study identifier and a study name
 * <li>a set of 13 booleans specifying which operations the user may perform
 *         on the study
 * </ul>
 *
 * @author  Roger House
 * @version 0.9
 */
public class Role
{
    /**
     * Indicates an operation which a given user is or is not allowed to 
     * perform on a given study.
     */
    public enum Privilege
    {
        GET_STUDY   ( 1),
        DELETE_STUDY( 2),
        GET_ROSTER  ( 3),
        POST_ROSTER ( 4),
        GET_ROLE    ( 5),
        PUT_ROLE    ( 6),
        DELETE_ROLE ( 7),
        GET_PANEL   ( 8),
        GET_CONTROL ( 9),
        PUT_CONTROL (10),
        POST_TABLE  (11),
        GET_MODEL   (12),
        POST_MODEL  (13),

        GET_CATALOG (14),   // Not a role; exists so indices exist for all ops
        POST_CATALOG(15),   // Not a role; exists so indices exist for all ops
        ;
  
        /**
         * Return the number of privileges.
         *
         * @return the number of privileges available.
         */
        public static int numberOfPrivileges()
        {
            return 13;
        }

        /**
         * Return the {@code Privilege} enum value as an integer.
         *
         * @return the enum value of this {@code Privilege} as an integer.
         */
        public final int getNumber()
        {
            return value;
        }
  
        /**
         * Convert an integer to a {@code Privilege} enum value.
         *
         * @param  value  the integer to convert.
         *
         * @return the {@code Privilege} enum value corresponding to the value 
         *         parameter, or null if the parameter does not correspond
         *         to any enum value.  
         */
        public static Privilege valueOf(int value) 
        {
            switch (value)
            {
            case  1:  return GET_STUDY   ;
            case  2:  return DELETE_STUDY;
            case  3:  return GET_ROSTER  ;
            case  4:  return POST_ROSTER ;
            case  5:  return GET_ROLE    ;
            case  6:  return PUT_ROLE    ;
            case  7:  return DELETE_ROLE ;
            case  8:  return GET_PANEL   ;
            case  9:  return GET_CONTROL ;
            case 10:  return PUT_CONTROL ;
            case 11:  return POST_TABLE  ;
            case 12:  return GET_MODEL   ;
            case 13:  return POST_MODEL  ;
            default:  return null        ;
            }
        }

        private final int value;

        private Privilege(int value)
        {
            this.value = value;
        }

    }   //  end enum Privilege

    private String roleLocation    = null;

    private String userIdentifier  = null;
    private String userName        = "";

    private String studyIdentifier = null;
    private String studyName       = "";

    private BitSet privilegeSet   = new BitSet(Privilege.numberOfPrivileges());

    // Constructors

    /**
     * Initializes a newly created {@code Role} object with attributes 
     * specified by the input parameters.
     *
     * @param  userIdentifier  the user who has the role.
     * @param  studyIdentifier the study on which the user has the role.
     */
    public Role(String userIdentifier, String studyIdentifier)
    {
        setUserIdentifier(userIdentifier);
        setStudyIdentifier(studyIdentifier);
    }

    // Setters and getters

    /**
     * Set the role location.
     *
     * @param  loc  the location to assign to this role.  May be null.
     *
     * @return this {@code Role}.
     */
    Role setRoleLocation(String loc)
    {
        roleLocation = loc;
        return this;
    }

    /**
     * Return the role location.
     *
     * @return the location of this role.  May be null.
     */
    String getRoleLocation()
    {
        return roleLocation;
    }

    /**
     * Set the user identifier.
     *
     * @param  userIdentifier  the user identifier to assign to this role.  
     *
     * @return this {@code Role}.
     */
    public Role setUserIdentifier(String userIdentifier)
    {
        this.userIdentifier = userIdentifier;
        return this;
    }

    /**
     * Return the user identifier.
     *
     * @return the user identifier of this role.  May be null.
     */
    public String getUserIdentifier()
    {
        return userIdentifier;
    }


    /**
     * Set the user name.
     *
     * @param  name  the user name to assign to this role.  May be null.
     *
     * @return this {@code Role}.
     */
    public Role setUserName(String name)
    {
        userName = name;
        return this;
    }

    /**
     * Return the user name.
     *
     * @return the name of the user of this role.  May be null.
     */
    public String getUserName()
    {
        return userName;
    }


    /**
     * Set the study identifier.
     *
     * @param  studyIdentifier  the study identifier to assign to this role.  
     *
     * @return this {@code Role}.
     */
    public Role setStudyIdentifier(String studyIdentifier)
    {
        this.studyIdentifier = studyIdentifier;
        return this;
    }

    /**
     * Return the study identifier.
     *
     * @return the study identifier of this role.  May be null.
     */
    public String getStudyIdentifier()
    {
        return studyIdentifier;
    }


    /**
     * Set the study name.
     *
     * @param  name  the name to assign to the study of this role.  
     *               May be null.
     *
     * @return this {@code Role}.
     */
    public Role setStudyName(String name)
    {
        studyName = name;
        return this;
    }

    /**
     * Return the study name.
     *
     * @return the name of the study of this role.  May be null.
     */
    public String getStudyName()
    {
        return studyName;
    }


    // Get and set privileges

    /**
     * Add a privilege.  Grant a specific privilege.
     *
     * @param  privilege is the privilege to grant.
     *
     * @return this {@code role}.
     */
    public Role addPrivilege(Privilege privilege)
    {
        privilegeSet.set(privilege.getNumber());

        return this;
    }

    /**
     * Remove a privilege.  A specific privilege is removed.
     *
     * @param  privilege is the privilege to remove.
     *
     * @return this {@code Role}.
     */
    public Role removePrivilege(Privilege privilege)
    {
        privilegeSet.clear(privilege.getNumber());

        return this;
    }

    /**
     * Add all privileges.  All privileges are granted.
     *
     * @return this {@code Role}.
     */
    public Role addAllPrivileges()
    {
        privilegeSet.set(1, Privilege.numberOfPrivileges() + 1);

        return this;
    }

    /**
     * Remove all privileges.
     *
     * @return this {@code Role}.
     */
    public Role removeAllPrivileges()
    {
        privilegeSet.clear();

        return this;
    }

    /**
     * Get a specific privilege.
     *
     * @param  privilege is the privilege to check for.
     *
     * @return {@code true} if {@code privilege} is granted, {@code false} 
     *         otherwise.
     */
    public boolean getPrivilege(Privilege privilege)
    {
        return privilegeSet.get(privilege.getNumber());
    }


    // Utility

    /**
     * Return a string representation of this {@code Role}.
     *
     * @return  the string representation of this {@code Role}.
     */
    public String toString()
    {
        return toStringInternal(false);
    }

    /**
     * Return a string representation of this {@code Role}.
     *
     * @param  showAll specifies if internal data members should be shown.
     * @return  the string representation of this {@code Role}.
     */
    String toStringInternal(boolean showAll)
    {
        StringBuilder s = new StringBuilder();

        s.append
        (
            "Role:"                                          + "\n" +
            "  userIdentifier  = " + getUserIdentifier()     + "\n" +
            "  userName        = " + getUserName()           + "\n" +
                                                               "\n" +
            "  studyIdentifier = " + getStudyIdentifier()    + "\n" +
            "  studyName       = " + getStudyName()          + "\n"
        );

        if (showAll)
            s.append
            (
                                                               "\n" +
            "  roleLocation    = " + getRoleLocation()       + "\n"
            );

        s.append
        (
                                                               "\n" +
            "  privilegeSet"                                 + "\n" 
        );

        for (int i = 1;  i <= Privilege.numberOfPrivileges();  ++i)
        {
            Privilege privilege = Privilege.valueOf(i);

            s.append("    ");
            s.append(getPrivilege(privilege) ? "yes " : "no  ");
            s.append(privilege.name());
            s.append("\n");
        }

        return s.toString();
    }

}   // end class Role

// end Role.java
