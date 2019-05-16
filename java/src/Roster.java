// Roster.java

package com.yosokumo.core;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

/**
 * For each study, a list of users that have a role on the study.  A roster 
 * has these attributes:
 * <ul>
 * <li>an identifier and a name of the study to which the roster belongs
 * <li>a collection of all the roles associated with the study, indexed by 
 *     user identifier
 * </ul>
 *
 * @author  Roger House
 * @version 0.9
 */
public class Roster
{
    private String studyIdentifier  = null;
    private String studyName        = null;

    private String rosterLocation   = null;

    // At present the roleCollection map is implemented as a LinkedHashMap
    // because this implementation has O(1) performance for all three of 
    // "get", "containsKey", and "next".  The latter is used to iterate 
    // over the map, and thus we do not want it to be expensive.  For the 
    // HashMap implementation, "next" is O(h/n) where h is the capacity of
    // the map, which is indeed expensive, especially when the map is 
    // lightly loaded.  

    private Map<String, Role> roleCollection = 
                                        new LinkedHashMap<String, Role>();

    // Constructors

    /**
     * Initializes a newly created {@code Roster} object with default 
     * attributes.
     */
    Roster()
    {
    }

    /**
     * Initializes a newly created {@code Roster} object with attributes 
     * specified by the input parameters.
     *
     * @param  studyIdentifier  the identifier of the study the roster is for.
     * @param  studyName        the name of the study the roster is for.
     */
    Roster(String studyIdentifier, String studyName)
    {
        setStudyIdentifier(studyIdentifier);
        setStudyName(studyName);
    }


    // Setters and getters

    /**
     * Set the study identifier.
     *
     * @param  id  the identifier of the study to which the roster belongs. 
     * May be null.
     */
    void setStudyIdentifier(String id)
    {
        studyIdentifier = id;
    }

    /**
     * Return the study identifier.
     *
     * @return the identifier of the study to which the roster belongs.
     * May be null.
     */
    public String getStudyIdentifier()
    {
        return studyIdentifier;
    }

    /**
     * Set the study name.
     *
     * @param  name  the name of the study to which the roster belongs.  
     * May be null.
     */
    void setStudyName(String name)
    {
        studyName = name;
    }

    /**
     * Return the study name.
     *
     * @return the name of the study to which the roster belongs.  May be null.
     */
    public String getStudyName()
    {
        return studyName;
    }

    /**
     * Set the roster location.
     *
     * @param  loc  the location to assign to this roster.  May be null.
     */
    void setRosterLocation(String loc)
    {
        rosterLocation = loc;
    }

    /**
     * Return the roster location.
     *
     * @return the location of this roster.  May be null.
     */
    String getRosterLocation()
    {
        return rosterLocation;
    }

    // Access to the role collection
    //
    // That a map is used to implement the role collection is hidden 
    // from the Roster client, as much as this makes sense.

    /**
     * Add a role to the roster.  In all cases the {@code Role} parameter
     * is added to the roster.  The return value distinguishes two 
     * possibilities.
     *
     * @param   role  the {@code Role} to add to the roster.
     * @return  {@code null} means there was no role with the same user 
     *              identifier already in the roster.
     *          non-{@code null} means that there was a role with the same user
     *              identifier already in the roster, and the return value is 
     *              the old role which has been replaced by the new one.
     */
    Role addRole(Role role)
    {
        return roleCollection.put(role.getUserIdentifier(), role);
    }

    /**
     * Remove a role from the roster.
     *
     * @param   userIdentifier the user identifier of the {@code Role} to 
     *                          remove from the roster.
     * @return  {@code null} means there was no role in the roster with the 
     *              user identifier specified by the parameter; the roster 
     *              is left unchanged.
     *          non-{@code null} means that there was a role in the roster
     *              with the user identifier specified by the parameter; the 
     *              role has been removed from the roster, and the return 
     *              value is the removed role.
     */
    Role removeRole(String userIdentifier)
    {
        return roleCollection.remove(userIdentifier);
    }

    /**
     * Remove all roles from the roster.  After a call of this method,
     * the roster is empty, i.e., it contains no roles.
     *
     */
    void clearRoles()
    {
        roleCollection.clear();
    }

    /**
     * Return a role from the roster.
     *
     * @param   userIdentifier the identifier of the {@code Role} to 
     *                          get from the roster.
     * @return  {@code null} means there is no role in the roster with the 
     *              user identifier specified by the parameter.
     *          non-{@code null} means that there is a role in the roster
     *              with the user identifier specified by the parameter, and 
     *              the return value is the specified role.
     */
    public Role getRole(String userIdentifier)
    {
        return roleCollection.get(userIdentifier);
    }

    /**
     * Test if a role is in the roster.
     *
     * @param   userIdentifier the identifier of the {@code Role} to 
     *                          test for.
     * @return  {@code false} means there is no role in the roster with the 
     *              user identifier specified by the parameter.
     *          {@code true} means that there is a role in the roster
     *              with the user identifier specified by the parameter.
     */
    public boolean containsRole(String userIdentifier)
    {
        return roleCollection.containsKey(userIdentifier);
    }

    /**
     * Return the number of roles in the roster.
     *
     * @return  the number of roles in the roster.
     */
    public int size()
    {
        return roleCollection.size();
    }

    /**
     * Return {@code true} if the roster contains no roles.
     *
     * @return {@code true} if the roster contains no roles.
     *         {@code false} otherwise.
     */
    public boolean isEmpty()
    {
        return roleCollection.isEmpty();
    }

    /**
     * Return the user identifiers of all the roles in the roster as a 
     * {@code Set<String>}.  This makes it possible to iterate over the 
     * role identifiers like this:
     * <pre>
     *   for (String s : roster.getUserIdentifiersSet())<br/>
     *   {
     *       process role identifier s
     *   }
     * </pre>
     *
     * @return a set of the user identifiers of all roles in the roster.
     */
    public Set<String> getUserIdentifiersSet()
    {
        return roleCollection.keySet();
    }

    /**
     * Return all roles in the roster as a {@code Collection<Role>}.  
     * This makes it possible to iterate over all roles in the roster 
     * like this:
     * <pre>
     *   for (Role r : roster.getRoleCollection())
     *   {
     *       process role r
     *   }
     * </pre>
     *
     * @return a collection of all roles in the roster.
     */
    public Collection<Role> getRoleCollection()
    {
        return roleCollection.values();
    }

    // Utility

    /**
     * Return a string representation of this {@code Roster}.  Note that 
     * for a role in the roster only the role identifier is represented 
     * as a string, not the entire role.
     *
     * @return  the string representation of this {@code Roster}.
     */
    public String toString()
    {
        return toStringInternal(false);
    }

    /**
     * Return a string representation of this {@code Roster}.  Note that 
     * for a role in the roster only the role identifier is represented 
     * as a string, not the entire role.
     *
     * @param  showAll specifies if internal data members should be shown.
     * @return  the string representation of this {@code Roster}.
     */
    String toStringInternal(boolean showAll)
    {
        StringBuilder b = new StringBuilder
        (
            "Roster:"                                     + "\n" +
            "  studyIdentifier = " + getStudyIdentifier() + "\n" +
            "  studyName       = " + getStudyName()       + "\n"
        );

        if (showAll)
            b.append
            (
            "  rosterLocation  = " + getRosterLocation()  + "\n"
            );

        b.append
        (
            "  Roles:"                                    + "\n"
        );

        for (Role r : getRoleCollection())
        {
            b.append("    " + r.getUserIdentifier());
            b.append("    " + r.getUserName() + "\n");
        }

        return b.toString();
    }

}   // end class Roster

// end Roster.java
