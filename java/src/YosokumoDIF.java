// YosokumoDIF.java

package com.yosokumo.core;

/**
 * Defines methods which transform bytes obtained from HTTP requests into 
 * Yosokumo Java objects (such as {@code Catalog} and {@code Study}) and 
 * vice versa.  Each class implementing this interface handles a different 
 * DIF, e.g., XML and Google Protocol Buffers.
 */

interface YosokumoDIF
{
    /**
     * Return a content type string for use in Accept and Content-Type HTTP
     * header lines.  For example, "application/yosokumo+protobuf".
     *
     * @return a content type string for use in Accept and other HTTP headers.
     */
    String getContentType();

    /**
     * Return a {@code ServiceException} from the code-decode process.
     *
     * @return a {@code ServiceException}; null means there is no exception.
     */
    ServiceException getException();

    /**
     * Make a Yosokumo {@code Catalog} object out of the bytes of an HTTP 
     * Entity. 
     *
     * @param  catalogAsBytes  a catalog as bytes from an HTTP Entity.
     *
     * @return the Yosokumo {@code Catalog} object represented by the 
     * input bytes.
     */
    Catalog makeCatalogFromBytes(byte [] catalogAsBytes);

    /**
     * Make a Yosokumo {@code Study} object out of the bytes of an HTTP 
     * Entity. 
     *
     * @param  studyAsBytes  a study as bytes from an HTTP Entity.
     *
     * @return the Yosokumo {@code Study} object represented by the 
     * input bytes.
     */
    Study makeStudyFromBytes(byte [] studyAsBytes);

    /**
     * Make the bytes for an HTTP Entity out of a Yosokumo {@code Study} 
     * object.
     *
     * @param  study  a Yosokumo {@code Study} object.
     *
     * @return the bytes of an HTTP Entity representing the input Yosokumo 
     *         {@code Study} object.
     */
    byte [] makeBytesFromStudy(Study study);


    /**
     * Make the bytes for an HTTP Entity out of a Yosokumo {@code Study} 
     * name.
     *
     * @param  name a Yosokumo {@code Study} name.
     *
     * @return the bytes of an HTTP Entity representing the input Yosokumo 
     *         {@code Study} name.
     */
    byte [] makeBytesFromStudyName(String name);

    /**
     * Make the bytes for an HTTP Entity out of a Yosokumo {@code Study} 
     * status.
     *
     * @param  status a Yosokumo {@code Study} status.
     *
     * @return the bytes of an HTTP Entity representing the input Yosokumo 
     *         {@code Study} status.
     */
    byte [] makeBytesFromStudyStatus(Study.Status status);

    /**
     * Make the bytes for an HTTP Entity out of a Yosokumo {@code Study} 
     * visibility.
     *
     * @param  visibility a Yosokumo {@code Study} visibility.
     *
     * @return the bytes of an HTTP Entity representing the input Yosokumo 
     *         {@code Study} visibility.
     */
    byte [] makeBytesFromStudyVisibility(Study.Visibility visibility);

    /**
     * Make a Yosokumo {@code Panel} object out of the bytes of an HTTP 
     * Entity. 
     *
     * @param  panelAsBytes  a panel as bytes from an HTTP Entity.
     *
     * @return the Yosokumo {@code Panel} object represented by the 
     * input bytes.
     */
    Panel makePanelFromBytes(byte [] panelAsBytes);


    /**
     * Make a Yosokumo {@code Roster} object out of the bytes of an HTTP 
     * Entity. 
     *
     * @param  rosterAsBytes  a roster as bytes from an HTTP Entity.
     *
     * @return the Yosokumo {@code Roster} object represented by the 
     * input bytes.
     */
    Roster makeRosterFromBytes(byte [] rosterAsBytes);

    /**
     * Make a Yosokumo {@code Role} object out of the bytes of an HTTP 
     * Entity. 
     *
     * @param  roleAsBytes  a role as bytes from an HTTP Entity.
     *
     * @return the Yosokumo {@code Role} object represented by the 
     * input bytes.
     */
    Role makeRoleFromBytes(byte [] roleAsBytes);

    /**
     * Make the bytes for an HTTP Entity out of a Yosokumo {@code Role} 
     * object.
     *
     * @param  role  a Yosokumo {@code Role} object.
     *
     * @return the bytes of an HTTP Entity representing the input Yosokumo 
     *         {@code Role} object.
     */
    byte [] makeBytesFromRole(Role role);

    /**
     * Make a Yosokumo {@code Predictor} object out of the bytes of an HTTP 
     * Entity. 
     *
     * @param  predictorAsBytes  a predictor as bytes from an HTTP Entity.
     *
     * @return the Yosokumo {@code Predictor} object represented by the 
     * input bytes.
     */
    Predictor makePredictorFromBytes(byte [] predictorAsBytes);

    /**
     * Make the bytes for an HTTP Entity out of a Yosokumo {@code Predictor} 
     * object.
     *
     * @param  predictor  a Yosokumo {@code Predictor} object.
     *
     * @return the bytes of an HTTP Entity representing the input Yosokumo 
     *         {@code Predictor} object.
     */
    byte [] makeBytesFromPredictor(Predictor predictor);

    /**
     * Make a Yosokumo {@code Specimen} object out of the bytes of an HTTP 
     * Entity. 
     *
     * @param  specimenAsBytes  a specimen as bytes from an HTTP Entity.
     *
     * @return the Yosokumo {@code Specimen} object represented by the 
     * input bytes.
     */
    Specimen makeSpecimenFromBytes(byte [] specimenAsBytes);

    /**
     * Make the bytes for an HTTP Entity out of a Yosokumo {@code Specimen} 
     * object.
     *
     * @param  specimen  a Yosokumo {@code Specimen} object.
     *
     * @return the bytes of an HTTP Entity representing the input Yosokumo 
     *         {@code Specimen} object.
     */
    byte [] makeBytesFromSpecimen(Specimen specimen);

    /**
     * Make a Yosokumo {@code Cell} object out of the bytes of an HTTP 
     * Entity. 
     *
     * @param  cellAsBytes  a cell as bytes from an HTTP Entity.
     *
     * @return the Yosokumo {@code Cell} object represented by the 
     * input bytes.
     */
    Cell makeCellFromBytes(byte [] cellAsBytes);

    /**
     * Make the bytes for an HTTP Entity out of a Yosokumo {@code Cell} 
     * object.
     *
     * @param  cell  a Yosokumo {@code Cell} object.
     *
     * @return the bytes of an HTTP Entity representing the input Yosokumo 
     *         {@code Cell} object.
     */
    byte [] makeBytesFromCell(Cell cell);

    /**
     * Make a Yosokumo {@code Block} object out of the bytes of an HTTP 
     * Entity.
     *
     * @param  blockAsBytes a block as bytes from an HTTP Entity.
     *
     * @return the Yosokumo {@code Block} object represented by the input
     *         bytes.
     */
    Block makeBlockFromBytes(byte [] blockAsBytes);

    /**
     * Make the bytes for an HTTP Entity out of a Yosokumo {@code Block} 
     * object.
     *
     * @param  block  a Yosokumo {@code Block} object.
     *
     * @return the bytes of an HTTP Entity representing the input Yosokumo 
     *         {@code Block} object.
     */
    byte [] makeBytesFromBlock(Block block);


    /**
     * Make a Yosokumo {@code Message} object out of the bytes of an HTTP 
     * Entity. 
     *
     * @param  messageAsBytes  a message as bytes from an HTTP Entity.
     *
     * @return the Yosokumo {@code Message} object represented by the 
     * input bytes.
     */
    Message makeMessageFromBytes(byte [] messageAsBytes);

}   // end class YosokumoDIF

// end YosokumoDIF.java
