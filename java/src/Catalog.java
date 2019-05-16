// Catalog.java

package com.yosokumo.core;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

/**
 * An index to all the studies on which a given user has a role.  A catalog 
 * has these attributes:
 * <ul>
 * <li>an identifier and a name of the user to whom the catalog belongs
 * <li>a collection of all the studies for which the user has a role, indexed
 *          by study identifier
 * </ul>
 *
 * @author  Roger House
 * @version 0.9
 */

public class Catalog
{
    /**
     * Identifier of the user to whom the catalog belongs.
     */
    private String userIdentifier = null;

    /**
     * User name of the user to whom the catalog belongs.
     */
    private String userName = null;

    /**
     * URI of the catalog.
     */
    private String catalogLocation = null;

    // At present the studyCollection map is implemented as a LinkedHashMap
    // because this implementation has O(1) performance for all three of 
    // "get", "containsKey", and "next".  The latter is used to iterate 
    // over the map, and thus we do not want it to be expensive.  For the 
    // HashMap implementation, "next" is O(h/n) where h is the capacity of
    // the map, which is indeed expensive, especially when the map is 
    // lightly loaded.  

    /**
     * Collection of studies comprising the catalog.
     */
    private Map<String, Study> studyCollection = 
                                        new LinkedHashMap<String, Study>();

    // Constructors

    /**
     * Initializes a newly created {@code Catalog} object with default 
     * attributes.
     * <ul>
     * <li>user identifier "ABCDEF0123456789"
     * <li>user name ""
     * </ul>
     */
    Catalog()
    {
        setUserIdentifier("ABCDEF0123456789");
        setUserName("");
    }

    /**
     * Initializes a newly created {@code Catalog} object with attributes 
     * specified by the input parameters.
     *
     * @param  userIdentifier  the unique identifier for the user.
     * @param  userName        the name of the user.
     */
    Catalog(String userIdentifier, String userName)
    {
        setUserIdentifier(userIdentifier);
        setUserName(userName);
    }

    // Copy catalog

    /**
     * Make a deep copy of a catalog.
     *
     * @param  oldCatalog        the catalog to copy.
     * @return                   a deep copy of oldCatalog.
     */
    static Catalog copyCatalog(Catalog oldCatalog)
    {
        Catalog catalog = new Catalog();

        catalog.setUserIdentifier( oldCatalog.getUserIdentifier() );
        catalog.setUserName(       oldCatalog.getUserName()       );
        catalog.setCatalogLocation(oldCatalog.getCatalogLocation());

        for (Study s : oldCatalog.getStudyCollection())
            catalog.addStudy(Study.copyStudy(s));

        return catalog;
    }


    // Setters and getters

    /**
     * Set the user identifier.
     *
     * @param  id  the identifier of the user to whom the catalog belongs. 
     * May be null.
     */
    void setUserIdentifier(String id)
    {
        userIdentifier = id;
    }

    /**
     * Return the user identifier.
     *
     * @return the identifier of the user to whom the catalog belongs.
     * May be null.
     */
    public String getUserIdentifier()
    {
        return userIdentifier;
    }

    /**
     * Set the user name.
     *
     * @param  name  the name of the user to whom the catalog belongs.  
     * May be null.
     */
    void setUserName(String name)
    {
        userName = name;
    }

    /**
     * Return the user name.
     *
     * @return the name of the user to whom the catalog belongs.  May be null.
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * Set the catalog location.
     *
     * @param  loc  the location of this catalog.  May be null.
     */
    void setCatalogLocation(String loc)
    {
        catalogLocation = loc;
    }

    /**
     * Return the catalog location.
     *
     * @return the location of this catalog.  May be null.
     */
    String getCatalogLocation()
    {
        return catalogLocation;
    }

    // Access to the study collection
    //
    // That a map is used to implement the study collection is hidden 
    // from the Catalog client, as much as this makes sense.

    /**
     * Add a study to the catalog.  In all cases the {@code Study} parameter
     * is added to the catalog.  The return value distinguishes two 
     * possibilities.
     *
     * @param   study  the {@code Study} to add to the catalog.
     * @return  {@code null} means there was no study with the same identifier 
     *              already in the catalog.
     *          non-{@code null} means that there was a study with the same 
     *              identifier already in the catalog, and the return value is 
     *              the old study which has been replaced by the new one.
     */
    Study addStudy(Study study)
    {
        return studyCollection.put(study.getStudyIdentifier(), study);
    }

    /**
     * Remove a study from the catalog.
     *
     * @param   studyIdentifier the identifier of the {@code Study} to 
     *                          remove from the catalog.
     * @return  {@code null} means there was no study in the catalog with the 
     *              identifier specified by the parameter; the catalog is left 
     *              unchanged.
     *          non-{@code null} means that there was a study in the catalog
     *              with the identifier specified by the parameter; the study 
     *              has been removed from the catalog, and the return value is 
     *              the removed study.
     */
    Study removeStudy(String studyIdentifier)
    {
        return studyCollection.remove(studyIdentifier);
    }

    /**
     * Remove all studies from the catalog.  After a call of this method,
     * the catalog is empty, i.e., it contains no studies.
     *
     */
    void clearStudies()
    {
        studyCollection.clear();
    }

    /**
     * Return a study from the catalog.
     *
     * @param   studyIdentifier the identifier of the {@code Study} to 
     *                          get from the catalog.
     * @return  {@code null} means there is no study in the catalog with the 
     *              identifier specified by the parameter.
     *          non-{@code null} means that there is a study in the catalog
     *              with the identifier specified by the parameter, and the 
     *              return value is the specified study.
     */
    public Study getStudy(String studyIdentifier)
    {
        return studyCollection.get(studyIdentifier);
    }

    /**
     * Test if a study is in the catalog.
     *
     * @param   studyIdentifier the identifier of the {@code Study} to 
     *                          test for.
     * @return  {@code false} means there is no study in the catalog with the 
     *              identifier specified by the parameter.
     *          {@code true} means that there is a study in the catalog
     *              with the identifier specified by the parameter.
     */
    public boolean containsStudy(String studyIdentifier)
    {
        return studyCollection.containsKey(studyIdentifier);
    }

    /**
     * Return the number of studies in the catalog.
     *
     * @return  the number of studies in the catalog.
     */
    public int size()
    {
        return studyCollection.size();
    }

    /**
     * Return {@code true} if the catalog contains no studies.
     *
     * @return {@code true} if the catalog contains no studies.
     *         {@code false} otherwise.
     */
    public boolean isEmpty()
    {
        return studyCollection.isEmpty();
    }

    /**
     * Return the identifiers of all the studies in the catalog as a 
     * {@code Set<String>}.  This makes it possible to iterate over the 
     * study identifiers like this:
     * <pre>
     *   for (String s : catalog.getStudyIdentifiersSet())<br/>
     *   {
     *       process study identifier s
     *   }
     * </pre>
     *
     * @return a set of the identifiers of all studies in the catalog.
     */
    public Set<String> getStudyIdentifiersSet()
    {
        return studyCollection.keySet();
    }

    /**
     * Return all studies in the catalog as a {@code Collection<Study>}.  
     * This makes it possible to iterate over all studies in the catalog 
     * like this:
     * <pre>
     *   for (Study s : catalog.getStudyCollection())
     *   {
     *       process study s
     *   }
     * </pre>
     *
     * @return a collection of all studies in the catalog.
     */
    public Collection<Study> getStudyCollection()
    {
        return studyCollection.values();
    }


    // Utility

    /**
     * Return a string representation of this {@code Catalog}.  Note that 
     * for a study in the catalog only the study identifier and study name 
     * are represented as a string, not the entire study.
     *
     * @return  the string representation of this {@code Catalog}.
     */
    public String toString()
    {
        return toStringInternal(false);
    }


    /**
     * Return a string representation of this {@code Catalog}.  Note that 
     * for a study in the catalog only the study identifier and study name 
     * are represented as a string, not the entire study.
     *
     * @param  showAll specifies if internal data members should be shown.
     * @return  the string representation of this {@code Catalog}.
     */
    String toStringInternal(boolean showAll)
    {
        StringBuilder b = new StringBuilder
        (
            "Catalog:"                                    + "\n" +
            "  userIdentifier  = " + getUserIdentifier()  + "\n" +
            "  userName        = " + getUserName()        + "\n"
        );

        if (showAll)
            b.append("  catalogLocation = " + getCatalogLocation() + "\n");

        b.append("  Studies:" + "\n");

        for (Study s : getStudyCollection())
        {
            b.append("    " + s.getStudyIdentifier());
            b.append("    " + s.getStudyName() + "\n");
        }

        return b.toString();
    }

}   // end class Catalog

// end Catalog.java
