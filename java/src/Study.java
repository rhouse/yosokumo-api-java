// Study.java

package com.yosokumo.core;

/**
 * A container for all resources associated with an analytic project.  A 
 * study has these attributes:
 * <ul>
 * <li>a study identifier and a study name
 * <li>a type, a status, and a visibility
 * <li>an identifier and a name of the user who owns the study 
 * <li>references to the table, model, panel, and roster for the study
 * </ul>
 *
 * @author  Roger House
 * @version 0.9
 */
public class Study
{
    /**
     * Indicates the quality of the predictands associated with subjects in 
     * the study.  The {@code Type} determines the type of the predictive 
     * model that will be built for the study.  The type attribute may not 
     * be changed after it has been set at study creation.  If the type 
     * attribute is not included in the posted study document, the default 
     * value is {@code NUMBER}.
     */
    public enum Type
    {
        /**
         * the predictand is categorical and nominal.
         */
        CLASS,
        /**
         * the predictand is categorical, but also ordinal.
         */
        RANK,
        /**
         * the predictand is continuous and ratio.
         */
        NUMBER,
        /**
         * the predictand represents a probability given 
         * as a continuous value between zero and one inclusive. 
         */
        CHANCE
    }

    /**
     * Describes the state of the study, whether it is running, on standby, 
     * or stopped.  The {@code Status} can be changed at any time. If the 
     * status attribute is not included in the posted study document, the 
     * default status is {@code RUNNING}.
     */
    public enum Status
    {
        /**
         * the service is accepting data into the study's table, analyzing 
         * the data to improve the study's predictive model and estimating 
         * predictands based on the model.
         */
        RUNNING,
        /**
         * the service is no longer accepting or analyzing data, but is still 
         * estimating predictands for the study based on the current model.
         */
        STANDBY,
        /**
         * the service is neither accepting nor analyzing data, and is not 
         * estimating predictands for the study. 
         */
        STOPPED
    }

    /**
     * Tells whether unidentified users can get estimated predictands from 
     * the model of the study.  The visibility of a study can be changed at 
     * any time.  If the visibility is not included in the posted study 
     * document, the default visibility is {@code PRIVATE}.
     */
    public enum Visibility
    {
        /**
         * only users that are authenticated and authorized may make a Get 
         * Model request for the study.
         */
        PRIVATE,
        /**
         * any web client may make a Get Model request for the study.
         */
        PUBLIC
    }

    private String studyIdentifier = null;
    private String studyName       = "";
    private String studyLocation   = null;

    private Type       type        = null;
    private Status     status      = null;
    private Visibility visibility  = null;

    private String ownerIdentifier = null;
    private String ownerName       = null;

    private String tableLocation   = null;
    private String modelLocation   = null;
    private String panelLocation   = null;
    private String rosterLocation  = null;

    // Panel info

    private String nameControlLocation       = null;
    private String statusControlLocation     = null;
    private String visibilityControlLocation = null;

    private long blockCount    = -1;
    private long cellCount     = -1;
    private long prospectCount = -1;

    private String creationTime       = null;
    private String latestBlockTime    = null;
    private String latestProspectTime = null;

    // Constructors

    /**
     * Initializes a newly created {@code Study} object with default 
     * attributes.
     * <ul>
     * <li>identifier ""
     * <li>name ""
     * <li>type NUMBER
     * <li>status RUNNING
     * <li>visibility PRIVATE
     * </ul>
     * The type of a Study cannot be changed after construction.
     */
    Study()
    {
        setStudyIdentifier("");
        setStudyName      (""                );
        setType           (Type.NUMBER       );
        setStatus         (Status.RUNNING    );
        setVisibility     (Visibility.PRIVATE);
    }

    /**
     * Initializes a newly created {@code Study} object with attributes 
     * specified by the input parameters.
     *
     * @param  studyName        the name of the study.
     * @param  type             the type of the study.
     * @param  status           the status of the study.
     * @param  visibility       the visibility of the study.
     *
     * The type of a Study cannot be changed after construction.
     */
    Study
    (
        String     studyName, 
        Type       type, 
        Status     status,
        Visibility visibility
    )
    {
        setStudyIdentifier(""        );
        setStudyName      (studyName );
        setType           (type      );
        setStatus         (status    );
        setVisibility     (visibility);
    }

    /**
     * Make a copy of a study.
     *
     * @param  s        the study to copy.
     * @return          a copy of s.
     */
    static Study copyStudy(Study s)
    {
        Study study = new Study();

        study.setStudyIdentifier(s.getStudyIdentifier());
        study.setStudyName      (s.getStudyName()      );
        study.setStudyLocation  (s.getStudyLocation()  );
        study.setType           (s.getType()           );
        study.setStatus         (s.getStatus()         );
        study.setVisibility     (s.getVisibility()     );
        study.setOwnerIdentifier(s.getOwnerIdentifier());
        study.setOwnerName      (s.getOwnerName()      );
        study.setTableLocation  (s.getTableLocation()  );
        study.setModelLocation  (s.getModelLocation()  );
        study.setPanelLocation  (s.getPanelLocation()  );
        study.setRosterLocation (s.getRosterLocation() );

        // Panel info

        study.setNameControlLocation      (s.getNameControlLocation()      );
        study.setStatusControlLocation    (s.getStatusControlLocation()    );
        study.setVisibilityControlLocation(s.getVisibilityControlLocation());
        study.setBlockCount               (s.getBlockCount()               );
        study.setCellCount                (s.getCellCount()                );
        study.setProspectCount            (s.getProspectCount()            );
        study.setCreationTime             (s.getCreationTime()             );
        study.setLatestBlockTime          (s.getLatestBlockTime()          );
        study.setLatestProspectTime       (s.getLatestProspectTime()       );

        return study;

    }   //  end copyStudy

    // Setters and getters

    /**
     * Set the study identifier.
     *
     * @param  id  the identifier to assign to this study.  This is the 
     * unique identification of the study.  May be null.
     */
    void setStudyIdentifier(String id)
    {
        studyIdentifier = id;
    }

    /**
     * Return the study identifier.
     *
     * @return the identifier of this study, which is the unique 
     * identification of the study.  May be null.
     */
    public String getStudyIdentifier()
    {
        return studyIdentifier;
    }

    /**
     * Set the study name.
     *
     * @param  name  the name to assign to this study.  May be null.
     */
    void setStudyName(String name)
    {
        studyName = name;
    }

    /**
     * Return the study name.
     *
     * @return the name of this study.  May be null.
     */
    public String getStudyName()
    {
        return studyName;
    }

    /**
     * Set the study location.
     *
     * @param  loc  the location to assign to this study.  May be null.
     */
    void setStudyLocation(String loc)
    {
        studyLocation = loc;
    }

    /**
     * Return the study location.
     *
     * @return the location of this study.  May be null.
     */
    String getStudyLocation()
    {
        return studyLocation;
    }

    /**
     * Set the study type.
     *
     * @param  t  the type to assign to this study.
     */
    void setType(Type t)
    {
        type = t;
    }

    /**
     * Return the study type.
     *
     * @return the type of this study.
     */
    public Type getType()
    {
        return type;
    }

    /**
     * Set the study status.
     *
     * @param  s  the status to assign to this study.
     */
    void setStatus(Status s)
    {
        status = s;
    }

    /**
     * Return the study status.
     *
     * @return the status of this study.
     */
    public Status getStatus()
    {
        return status;
    }

    /**
     * Set the study visibility.
     *
     * @param  v  the type to assign to this study.
     */
    void setVisibility(Visibility v)
    {
        visibility = v;
    }

    /**
     * Return the study visibility.
     *
     * @return the visibility of this study.
     */
    public Visibility getVisibility()
    {
        return visibility;
    }

    /**
     * Set the owner identifier.
     *
     * @param  id  the identifier of the owner of this study.  May be null.
     */
    void setOwnerIdentifier(String id)
    {
        ownerIdentifier = id;
    }

    /**
     * Return the owner identifier.
     *
     * @return the identifier of the owner of this study.  May be null.
     */
    public String getOwnerIdentifier()
    {
        return ownerIdentifier;
    }

    /**
     * Set the owner name.
     *
     * @param  name  the name to assign to the owner of this study.  
     *     May be null.
     */
    void setOwnerName(String name)
    {
        ownerName = name;
    }


    /**
     * Return the owner name.
     *
     * @return the name of the owner of this study.  May be null.
     */

    public String getOwnerName()
    {
        return ownerName;
    }

    /**
     * Set the table location.
     *
     * @param  loc  the location of the table for this study.  May be null.
     */
    void setTableLocation(String loc)
    {
        tableLocation = loc;
    }

    /**
     * Return the table location.
     *
     * @return the location of the table for this study.  May be null.
     */
    String getTableLocation()
    {
        return tableLocation;
    }

    /**
     * Set the model location.
     *
     * @param  loc  the location of the model for this study.  May be null.
     */
    void setModelLocation(String loc)
    {
        modelLocation = loc;
    }

    /**
     * Return the model location.
     *
     * @return the location of the model for this study.  May be null.
     */
    String getModelLocation()
    {
        return modelLocation;
    }

    /**
     * Set the panel location.
     *
     * @param  loc  the location of the panel for this study.  May be null.
     */
    void setPanelLocation(String loc)
    {
        panelLocation = loc;
    }

    /**
     * Return the panel location.
     *
     * @return the location of the panel for this study.  May be null.
     */
    String getPanelLocation()
    {
        return panelLocation;
    }

    /**
     * Set the roster location.
     *
     * @param  loc  the location of the roster for this study.  May be null.
     */
    void setRosterLocation(String loc)
    {
        rosterLocation = loc;
    }

    /**
     * Return the roster location.
     *
     * @return the location of the roster for this study.  May be null.
     */
    String getRosterLocation()
    {
        return rosterLocation;
    }

    // Panel setters and getters

    /**
     * Set the name control location.
     *
     * @param  nameControlLocation  the URI to use to change the 
     *                              name of the study.
     */
    void setNameControlLocation(String nameControlLocation)
    {
        this.nameControlLocation = nameControlLocation;
    }

    /**
     * Return the name control location.
     *
     * @return the URI to use to change the name of the study.
     */
    String getNameControlLocation()
    {
        return nameControlLocation;
    }


    /**
     * Set the status control location.
     *
     * @param  statusControlLocation  the URI to use to change the 
     *                                status of the study.
     */
    void setStatusControlLocation(String statusControlLocation)
    {
        this.statusControlLocation = statusControlLocation;
    }

    /**
     * Return the status control location.
     *
     * @return the URI to use to change the status of the study.
     */
    String getStatusControlLocation()
    {
        return statusControlLocation;
    }


    /**
     * Set the visibility control location.
     *
     * @param  visibilityControlLocation  the URI to use to change the 
     *                                    visibility of the study.
     */
    void setVisibilityControlLocation(String visibilityControlLocation)
    {
        this.visibilityControlLocation = visibilityControlLocation;
    }

    /**
     * Return the visibility control location.
     *
     * @return the URI to use to change the visibility of the study.
     */
    String getVisibilityControlLocation()
    {
        return visibilityControlLocation;
    }


    /**
     * Set the block count.
     *
     * @param  blockCount  the number of posted blocks that have been 
     *         accepted into the study table.
     */
    void setBlockCount(long blockCount)
    {
        this.blockCount = blockCount;
    }

    /**
     * Return the block count.
     *
     * @return the number of posted blocks that have been accepted into 
     *         the study table.
     */
    public long getBlockCount()
    {
        return blockCount;
    }

    /**
     * Set the cell count.
     *
     * @param  cellCount  the total number of cells contained in the blocks 
     *         reported in the block count. 
     */
    void setCellCount(long cellCount)
    {
        this.cellCount = cellCount;
    }

    /**
     * Return the cell count.
     *
     * @return the total number of cells contained in the blocks reported in 
     *         the block count. 
     */
    public long getCellCount()
    {
        return cellCount;
    }

    /**
     * Set the prospect count.
     *
     * @param  prospectCount  the total number of specimens contained in 
     *                        all Post Model and Get Model requests for 
     *                        the study.
     */
    void setProspectCount(long prospectCount)
    {
        this.prospectCount = prospectCount;
    }

    /**
     * Return the prospect count.
     *
     * @return the total number of specimens contained in all Post Model 
     *         and Get Model requests for the study.
     */
    public long getProspectCount()
    {
        return prospectCount;
    }

    /**
     * Set the creation time.
     *
     * @param  creationTime  the UTC time the study was created. 
     */
    void setCreationTime(String creationTime)
    {
        this.creationTime = creationTime;
    }

    /**
     * Return the creation time.
     *
     * @return the UTC time the study was created. 
     */
    public String getCreationTime()
    {
        return creationTime;
    }

    /**
     * Set the latest block time.
     *
     * @param  latestBlockTime  the UTC time that the service accepted 
     *                          the most recent block into the study table. 
     */
    void setLatestBlockTime(String latestBlockTime)
    {
        this.latestBlockTime = latestBlockTime;
    }

    /**
     * Return the latest block time.
     *
     * @return the UTC time that the service accepted the most recent block 
     *         into the study table. 
     */
    public String getLatestBlockTime()
    {
        return latestBlockTime;
    }

    /**
     * Set the latest prospect time.
     *
     * @param  latestProspectTime  the UTC time of the most recent Post 
     *                             Model or Get Model request.
     */
    void setLatestProspectTime(String latestProspectTime)
    {
        this.latestProspectTime = latestProspectTime;
    }

    /**
     * Return the latest prospect time.
     *
     * @return the UTC time of the most recent Post Model or Get Model request.
     */
    public String getLatestProspectTime()
    {
        return latestProspectTime;
    }

    // Utility

    /**
     * Return a string representation of this {@code Study}.
     *
     * @return  the string representation of this {@code Study}.
     */
    public String toString()
    {
        return toStringInternal(false);
    }

    /**
     * Return a string representation of this {@code Study}.
     *
     * @param  showAll specifies if internal data members should be shown.
     * @return  the string representation of this {@code Study}.
     */
    String toStringInternal(boolean showAll)
    {
        StringBuilder s = new StringBuilder
        (
            "Study:"                                                  + "\n" +
            "  studyIdentifier          = " + getStudyIdentifier()    + "\n" +
            "  studyName                = " + getStudyName()          + "\n" +
                                                                        "\n" +
            "  type                     = " + getType()               + "\n" +
            "  status                   = " + getStatus()             + "\n" +
            "  visibility               = " + getVisibility()         + "\n" +
                                                                        "\n" +
            "  ownerIdentifier          = " + getOwnerIdentifier()    + "\n" +
            "  ownerName                = " + getOwnerName()          + "\n" +
                                                                        "\n" +
            "  blockCount               = " + getBlockCount()         + "\n" +
            "  cellCount                = " + getCellCount()          + "\n" +
            "  prospectCount            = " + getProspectCount()      + "\n" +
                                                                        "\n" +
            "  creationTime             = " + getCreationTime()       + "\n" +
            "  latestBlockTime          = " + getLatestBlockTime()    + "\n" +
            "  latestProspectTime       = " + getLatestProspectTime() + "\n"
        );

        if (showAll)
            s.append
            (
                                                                         "\n"+
            "  studyLocation            = " + getStudyLocation()        +"\n"+
            "  tableLocation            = " + getTableLocation()        +"\n"+
            "  modelLocation            = " + getModelLocation()        +"\n"+
            "  panelLocation            = " + getPanelLocation()        +"\n"+
            "  rosterLocation           = " + getRosterLocation()       +"\n"+
            "  nameControlLocation      = " + getNameControlLocation()  +"\n"+
            "  statusControlLocation    = " + getStatusControlLocation()+"\n"+
            "  visibilityControlLocation= " 
                                      + getVisibilityControlLocation()  +"\n"
            );

        return s.toString();
    }


}   // end class Study

// end Study.java
