// Panel.java

package com.yosokumo.core;

/**
 * Contains various data items associated with a study, e.g., creation time.
 *
 * @author  Roger House
 * @version 0.9
 */
class Panel
{
    private String nameControlLocation;
    private String statusControlLocation;
    private String visibilityControlLocation;

    private long blockCount;
    private long cellCount;
    private long prospectCount;

    private String creationTime;
    private String latestBlockTime;
    private String latestProspectTime;


    // Panel setters and getters

    /**
     * Set the name control location.
     *
     * @param  nameControlLocation  the URI to use to change the 
     *                              name of the study.
     */
    public void setNameControlLocation(String nameControlLocation)
    {
        this.nameControlLocation = nameControlLocation;
    }

    /**
     * Return the name control location.
     *
     * @return the URI to use to change the name of the study.
     */
    public String getNameControlLocation()
    {
        return nameControlLocation;
    }


    /**
     * Set the status control location.
     *
     * @param  statusControlLocation  the URI to use to change the 
     *                                status of the study.
     */
    public void setStatusControlLocation(String statusControlLocation)
    {
        this.statusControlLocation = statusControlLocation;
    }

    /**
     * Return the status control location.
     *
     * @return the URI to use to change the status of the study.
     */
    public String getStatusControlLocation()
    {
        return statusControlLocation;
    }


    /**
     * Set the visibility control location.
     *
     * @param  visibilityControlLocation  the URI to use to change the 
     *                                    visibility of the study.
     */
    public void setVisibilityControlLocation(String visibilityControlLocation)
    {
        this.visibilityControlLocation = visibilityControlLocation;
    }

    /**
     * Return the visibility control location.
     *
     * @return the URI to use to change the visibility of the study.
     */
    public String getVisibilityControlLocation()
    {
        return visibilityControlLocation;
    }


    /**
     * Set the block count.
     *
     * @param  blockCount  the number of posted blocks that have been 
     *         accepted into the study table.
     */
    public void setBlockCount(long blockCount)
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
    public void setCellCount(long cellCount)
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
    public void setProspectCount(long prospectCount)
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
    public void setCreationTime(String creationTime)
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
    public void setLatestBlockTime(String latestBlockTime)
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
    public void setLatestProspectTime(String latestProspectTime)
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

}   // end class Panel

// end Panel.java
