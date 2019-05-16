// Service.java

package com.yosokumo.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * Provides the basic operations available from the Yosokumo web service, e.g.,
 * obtainCatalog and createStudy.  Whenever a basic operation fails, it throws 
 * a ServiceException.  See {@link ServiceException} for a description of the 
 * possible exceptions. 
 *
 * @author  Roger House
 * @version 0.9
 */

public class Service
{
    /**
     * Specifies the Data Interchange Format (DIF) of bytes transmitted to and 
     * from the Yosokumo server.
     */
    public enum DIFType
    {
        /**
         * Google Protocol Buffers
         */
        PROTOBUF,
        /**
         * Extensible Markup Language
         */
        XML,
        /**
         * JavaScript Object Notation
         */
        JSON,
        /**
         * Abstract Syntax Notation One
         */
        ASN_1
    }

    private int[][] validStatusCode =
    {
//             200 201 202 203 204 400 401 402 403 404 405 406 407 408 409 410 411 412 413 414 415
/*GET  stud*/ { 1,  0,  0,  0,  0,  1,  1,  0,  1,  1,  0,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0 },
/*DEL  stud*/ { 0,  0,  0,  0,  1,  1,  1,  0,  1,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0 },
/*GET  rost*/ { 1,  0,  0,  0,  0,  1,  1,  0,  1,  1,  0,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0 },
/*POST rost*/ { 0,  1,  0,  0,  0,  1,  1,  0,  1,  1,  0,  0,  0,  0,  1,  0,  0,  0,  0,  0,  1 },
/*GET  role*/ { 1,  0,  0,  0,  0,  1,  1,  0,  1,  1,  0,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0 },
/*PUT  role*/ { 1,  0,  0,  0,  1,  1,  1,  0,  1,  1,  0,  0,  0,  0,  1,  0,  0,  0,  0,  0,  1 },
/*DEL  role*/ { 0,  0,  0,  0,  1,  1,  1,  0,  1,  1,  0,  0,  0,  0,  1,  0,  0,  0,  0,  0,  0 },
/*GET  panl*/ { 1,  0,  0,  0,  0,  1,  1,  0,  1,  1,  0,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0 },
/*GET  cont*/ { 1,  0,  0,  0,  0,  1,  1,  0,  1,  1,  0,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0 },
/*PUT  cont*/ { 0,  0,  0,  0,  1,  1,  1,  0,  1,  1,  0,  0,  0,  0,  1,  0,  0,  0,  0,  0,  1 },
/*POST tabl*/ { 0,  0,  1,  0,  0,  1,  1,  0,  1,  1,  0,  0,  0,  0,  1,  0,  0,  0,  1,  0,  1 },
/*GET  modl*/ { 1,  0,  0,  0,  1,  1,  1,  0,  1,  1,  0,  1,  0,  0,  1,  0,  0,  0,  0,  1,  0 },
/*POST modl*/ { 1,  0,  0,  0,  1,  1,  1,  0,  1,  1,  0,  1,  0,  0,  1,  0,  0,  0,  1,  0,  1 },
/*GET  cata*/ { 1,  0,  0,  0,  0,  1,  1,  0,  1,  1,  0,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0 },
/*POST cata*/ { 0,  1,  0,  0,  0,  1,  1,  0,  1,  1,  0,  0,  0,  0,  1,  0,  0,  0,  0,  0,  1 },
    };


    private static final int MAX_ITEMS_TO_SEND_VIA_HTTP = 100000;

    /*
     * Parameters for constructing a YosokumoRequest. 
     */
    private Credentials credentials = null;
    private String      hostName    = "yosokumo.ws";
    private int         port        = 443;
    private DIFType     dif         = DIFType.PROTOBUF;
    private String      contentType = null;

    /*
     * Handles all basic HTTP requests.
     */
    private YosokumoRequest yRequest = null;

    /*
     * Converts from HTTP bytes to Yosokumo Java objects and vice versa.
     */
    private YosokumoDIF ydif = null;

    /*
     * Store an exception thrown during HTTP processing.
     */
    private ServiceException exception = null;

    /*
     * Store the name of the main method currently being executed.
     */
    private String methodName = null;

    /*
     * Store a flag indicating if the returned status code is allowed.
     */
    private boolean invalidStatusCode = false;

    /**
     * A cached copy of the user's catalog, providing fast access to all
     * studies managed by the catalog.   
     * The Service API presented to the outer world uses identifiers, not 
     * URIs, to specify the objects to operate on.  However, all interaction 
     * with the Yosokumo server uses URIs.  To map from identifiers to URIs,
     * a {@code cachedCatalog} is maintained.  As an example of how it is 
     * used, here is the logic for obtaining a study from the server:
     * <ul>
     * <li>test if a catalog is cached
     * <li>if not, get a catalog from the server
     * <li>if get of catalog fails, exit with failure
     * <li>otherwise, cache the catalog
     * <li>lookup the study identifier in the cached catalog
     * <li>if not found, exit with failure
     * <li>get the study from the server
     * <li>if get of study fails, exit with failure
     * <li>exit with success:  return the study
     * </ul>
     * 
     */
    private Catalog cachedCatalog = null;

    /**
     * An output from mapStudyIdentifierToStudy.  When true it means that 
     * mapStudyIdentifierToStudy refreshed the cached catalog, and when false 
     * it means that mapStudyIdentifierToStudy did not refresh the cached 
     * catalog.
     */
    private boolean refreshedCachedCatalog = false;

    // Constructors

    /**
     * Initializes a newly created {@code Service} object using defaults for 
     * the credentials, host name, port, and DIF.
     */
    public Service() throws ServiceException
    {
        initDifAndRequest();
    }

    /**
     * Initializes a newly created {@code Service} object with attributes 
     * specified by the input parameters, using defaults for the host name,
     * port, and DIF.
     *
     * @param  credentials user id and key for authentication.
     */
    public Service(Credentials credentials) throws ServiceException
    {
        this.credentials = credentials;

        initDifAndRequest();
    }

    /**
     * Initializes a newly created {@code Service} object with attributes 
     * specified by the input parameters, using defaults for the port and DIF.
     *
     * @param  credentials user id and key for authentication.
     * @param  hostName    host name for the Yosokumo server.
     */
    public Service(
        Credentials credentials,
        String      hostName) throws ServiceException
    {
        this.credentials = credentials;
        this.hostName    = hostName;

        initDifAndRequest();
    }

    /**
     * Initializes a newly created {@code Service} object with attributes 
     * specified by the input parameters, using a default for the DIF.
     *
     * @param  credentials user id and key for authentication.
     * @param  hostName    host name for the Yosokumo server.
     * @param  port        port to use for HTTP communication.
     */
    public Service(
        Credentials credentials,
        String      hostName,
        int         port) throws ServiceException
    {
        this.credentials = credentials;
        this.hostName    = hostName;
        this.port        = port;

        initDifAndRequest();
    }

    /**
     * Initializes a newly created {@code Service} object with attributes 
     * specified by the input parameters.
     *
     * @param  credentials user id and key for authentication.
     * @param  hostName    host name for the Yosokumo server.
     * @param  port        port to use for HTTP communication.
     * @param  dif         specifies the DIF to use to talk to the server.
     * @throws ServiceException if the DIF is not implemented
     */
    public Service(
        Credentials credentials,
        String      hostName,
        int         port,
        DIFType     dif) throws ServiceException
    {
        this.credentials = credentials;
        this.hostName    = hostName;
        this.port        = port;
        this.dif         = dif;

        initDifAndRequest();
    }


    /**
     * Initialize DIF processor and HTTP request processor.  The input dif 
     * is used to instantiate a DIF processor in ydif, as well as contentType.
     * In addition, yRequest is set to an instance of YosokumoRequest for 
     * processing HTTP requests.
     *
     * @throws ServiceException if the DIF is not implemented
     */
    private void initDifAndRequest() throws ServiceException
    {
        switch (dif)
        {
        case XML:
        case JSON:
        case ASN_1: throw new ServiceException("Unimplementd DIF:  " + 
                                                               dif.name());

        case PROTOBUF:  ydif = new YosokumoProtobuf();  break;
        }

        contentType = ydif.getContentType();

        yRequest = new YosokumoRequest(credentials, hostName, port, 
                                                            contentType);
    }

    /**
     * Initialize for an HTTP operation.  Init various data fields to prepare 
     * for execution of a service request.
     *
     * @param  methodName is the name of the service method being executed,
     *             e.g., "obtainCatalog".  It is used in messages if an error
     *             occurs.
     */
    private void initForOperation(String methodName)
    {
        exception = null;
        invalidStatusCode = false;
        this.methodName = methodName;
        yRequest.initForOperation();
    }

    /**
     * Check the status code returned by an HTTP request.
     *
     * @param  operation specifies the HTTP request, e.g. GET_STUDY.
     * @return is true if and only if the status code is 2xx and the 
     *             validStatusCode table indicates the status code value is
     *             allowed for the operation.  In all other cases, false is
     *             returned to indicate that the request was not successfully
     *             performed.  In addition the data member invalidStatusCode
     *             is set true if and only if one of these conditions holds:
     *             1) the status code is 2xx or 4xx and validStatusCode table 
     *             indicates the status code value is not allowed for the 
     *             operation, or 2) the status code is not 5xx.  
     */
    private boolean requestOk(Role.Privilege operation)
    {
        int code = yRequest.getStatusCode();
        int op   = operation.getNumber() - 1;

        if (200 <= code && code <= 204)
            return !(invalidStatusCode = (validStatusCode[op][code-200] == 0));

        if (400 <= code && code <= 415)
        {
            invalidStatusCode = (validStatusCode[op][code-400+5] == 0);
            return false;
        }

        invalidStatusCode = !(500 <= code && code <= 599);

        return false;
    }

    /**
     * Set the trace flag.
     *
     * @param  traceOn  setting to turn on or off trace output of HTTP 
     *                  requests.
     */
    void setTrace(boolean traceOn)
    {
        yRequest.setTrace(traceOn);
    }

    /**
     * Return Service exception.
     *
     * @return  exception occurring during HTTP processing.  Null means none.
     */
    ServiceException getException()
    {
        return exception;
    }

    /**
     * Return the status code from the last Service operation.
     *
     * @return the status code.
     */
    public int getStatusCode()
    {
        return yRequest.getStatusCode();
    }

    /**
     * Obtain the user's catalog of studies.  
     *
     * @return  a reference to the user's catalog, containing all the 
     *          studies the user has a role on.
     * @throws ServiceException
     */
    public Catalog obtainCatalog() throws ServiceException
    {
        initForOperation("obtainCatalog");

        Catalog catalog = obtainCatalogX();

        if (catalog == null)
            throw getException();

        return catalog;
    }

    /**
     * Create a default study and add it to the user's catalog of studies.  
     *
     * @return  a reference to a newly-created study with default name, 
     *          type, status, and visibility.  (Note that the name will be 
     *          an empty string.)  The study identifier, owner identifier, 
     *          and owner name are initialized, as well as references to 
     *          study components.
     * @throws ServiceException
     */
    public Study createStudy() throws ServiceException
    {
        Study study = createStudyX();

        if (study == null)
            throw getException();

        return study;
    }

    /**
     * Create a named study and add it to the user's catalog of studies.  
     *
     * @param   studyName specifies the study name.
     * @return  a reference to a newly-created study with name as specified 
     *          by the parameter, and default type, status, and visibility.  
     *          The study identifier, owner identifier, and owner name are 
     *          initialized, as well as references to study components.
     * @throws ServiceException
     */
    public Study createStudy(String studyName) throws ServiceException
    {
        Study study = createStudyX(studyName);

        if (study == null)
            throw getException();

        return study;
    }

    /**
     * Create a study with specified name and type and add it to the user's 
     *      catalog of studies.  
     *
     * @param   studyName specifies the study name.
     * @param   studyType specifies the study type.
     * @return  a reference to a newly-created study with name and type as 
     *          specified by the parameters, and default status and visibility.
     *          The study identifier, owner identifier, and owner name are 
     *          initialized, as well as references to study components.
     * @throws ServiceException
     */
    public Study createStudy(String studyName, Study.Type studyType) 
        throws ServiceException
    {
        Study study = createStudyX(studyName, studyType);

        if (study == null)
            throw getException();

        return study;
    }

    /**
     * Create a study with specified name, type, status, and visibility, and 
     *      add it to the user's catalog of studies.  
     *
     * @param   studyName specifies the study name.
     * @param   studyType specifies the study type.
     * @param   studyStatus specifies the study status.
     * @param   studyVisibility specifies the study visibility.
     * @return  a reference to a newly-created study with name, type, status, 
     *          and visibility as specified by the parameters.  The study 
     *          identifier, owner identifier, and owner name are initialized, 
     *          as well as references to study components.
     * @throws ServiceException
     */
    public Study createStudy(
        String           studyName,
        Study.Type       studyType,
        Study.Status     studyStatus,
        Study.Visibility studyVisibility)  throws ServiceException
    {
        Study study = createStudyX(studyName, studyType, studyStatus, 
                                                        studyVisibility);

        if (study == null)
            throw getException();

        return study;
    }

    /**
     * Update a study, changing the study name.
     *
     * @param   studyId specifies the identifier of the study to update.
     * @param   studyName is the new name to assign to the study.
     * @return  a reference to the {@code Study} specified by studyId, with 
     *          its name changed to studyName.  The name is also changed on 
     *          the server.
     * @throws ServiceException
     */
    public Study updateStudy(String studyId, String studyName) 
        throws ServiceException
    {
        Study study = updateStudyX(studyId, studyName);

        if (study == null)
            throw getException();

        return study;
    }

    /**
     * Update a study, changing the status.
     *
     * @param   studyId specifies the identifier of the study to update.
     * @param   studyStatus is the new status to assign to the study.
     * @return  a reference to the {@code Study} specified by studyId, with 
     *          its status changed to studyStatus.  The status is also changed
     *          on the server.
     * @throws ServiceException
     */
    public Study updateStudy(String studyId, Study.Status studyStatus)
         throws ServiceException
    {
        Study study = updateStudyX(studyId, studyStatus);

        if (study == null)
            throw getException();

        return study;
    }

    /**
     * Update a study, changing the visibility.
     *
     * @param   studyId specifies the identifier of the study to update.
     * @param   studyVisibility is the new visibility to assign to the study.
     * @return  a reference to the {@code Study} specified by studyId, with 
     *          its visibility changed to studyVisibility.  The visibility is 
     *          also changed on the server.
     * @throws ServiceException
     */
    public Study updateStudy(String studyId, Study.Visibility studyVisibility)
         throws ServiceException
    {
        Study study = updateStudyX(studyId, studyVisibility);

        if (study == null)
            throw getException();

        return study;
    }

    /**
     * Obtain a study from the server. 
     *
     * @param   studyId specifies the identifier of the study to obtain.
     * @return  a reference to a study obtained from the server with study 
     *          identifier equal to studyId.  
     * @throws ServiceException
     */
    public Study obtainStudy(String studyId) throws ServiceException
    {
        Study study = obtainStudyX(studyId);

        if (study == null)
            throw getException();

        return study;
    }


    /**
     * Delete a study from the server. 
     *
     * @param   studyId specifies the identifier of the study to delete.
     * @throws ServiceException
     */
    public void deleteStudy(String studyId) throws ServiceException
    {
        if (!deleteStudyX(studyId))
            throw getException();
    }


    /**
     * Obtain the roster for a study.
     *
     * @param   studyId specifies the identifier of the study whose roster 
     *              is to be obtained.
     * @return  a reference to the roster for the study specified by the 
     *          input parameter.
     * @throws ServiceException
     */
    public Roster obtainRoster(String studyId) throws ServiceException
    {
        Roster roster = obtainRosterX(studyId);

        if (roster == null)
            throw getException();

        return roster;
    }

    /**
     * Add a {@code Role} to a study's roster.
     *
     * @param  role  the role to add to the roster.  The user identifier 
     *               and study identifier fields of the {@code role} must 
     *               be set.  The role is added to the roster of the study, 
     *               specifying the privileges of the user on the study.
     *
     * @return  a reference to a newly-created role corresponding to the 
     *          input role with additional fields filled out by the service.
     * @throws ServiceException
     */
    public Role createRole(Role role) throws ServiceException
    {
        Role newRole = createRoleX(role);

        if (newRole == null)
            throw getException();

        return newRole;
    }


    /**
     * Update a role on the server.
     *
     * @param   role specifies the role to change.
     * @return  the role on the server is updated with the fields specified 
     *              by the input parameter {@code role}.  A null return value
     *              indicates there were no issues with the update. A non-null
     *              return value is a reference to a Message which describes 
     *              how the request was completed provisionally. This happens,
     *              e.g., when an attempt is made by user U to change the 
     *              delete_role privilege on a study owned by user U.  The 
     *              privilege is left unchanged in such cases to ensure that 
     *              there is always at least one user (the study creator) who 
     *              is authorized to reset privileges for any roleholder for 
     *              the study.
     * @throws ServiceException
     */
    public Message updateRole(Role role) throws ServiceException
    {
        if (updateRoleX(role) == null)
            throw getException();

        if (yRequest.getStatusCode() == 204)
            return null;    // Everything worked fine

        // The update operation was completed provisionally

        Message message = null;

        byte [] messageAsBytes = yRequest.getEntity();
        if (messageAsBytes != null)
            message = ydif.makeMessageFromBytes(messageAsBytes);

        if (message == null)
        {
            reportProblem("Expected updateRole to return a Message");
            throw getException();
        }

        return message;
    }


    /**
     * Obtain a role.  The role of a specific user on a specific study is
     * obtained.
     *
     * @param   userId specifies the user.
     * @param   studyId specifies the study.
     *
     * @return  a reference to the role of the specified user on the 
     *           specified study.
     * @throws ServiceException
     */
    public Role obtainRole(String userId, String studyId) 
        throws ServiceException
    {
        Role role = obtainRoleX(userId, studyId);

        if (role == null)
            throw getException();

        return role;
    }

    /**
     * Delete a role.  The role played on by a specific user on a specific 
     * study is deleted.
     *
     * @param   userId specifies the user.
     * @param   studyId specifies the study.
     * @throws ServiceException
     */
    public void deleteRole(String userId, String studyId)
         throws ServiceException
    {
        if (!deleteRoleX(userId, studyId))
            throw getException();
    }

    /**
     * Describe a study's predictors.  A list of column definitions is 
     * associated with a study.
     *
     * @param   studyId specifies the study whose predictors are to be 
     *              described.
     * @param   predictorList a list of the predictors (= variables = columns)
     *              for the table.
     * @throws ServiceException
     */
    public void describePredictors(
        String studyId, 
        List<Predictor> predictorList) throws ServiceException
    {
        if (!describePredictorsX(studyId, predictorList))
            throw getException();
    }


    /**
     * Populate the rows of a study's table with subjects.  A list of 
     * rows is inserted into the study's table.
     *
     * @param   studyId specifies the study whose table is to be populated.
     * @param   specimenList a list of the specimens 
     *              (= rows = observations = records) for the table.
     * @throws ServiceException
     */
    public void loadSubjects(
        String studyId, 
        List<Specimen> specimenList) throws ServiceException
    {
        if (!loadSubjectsX(studyId, specimenList))
            throw getException();
    }

    /**
     * Score prospects (compute predictands) using a study's model.  
     *
     * @param   studyId specifies the study whose model is to be used.
     * @param   specimenList is the list of specimens 
     *              (= rows = observations = records) whose predictands
     *              are to be computed.  Upon return from this method, the 
     *              predictand member of each Specimen in the list
     *              contains the results of the computations (the score).
     * @throws ServiceException
     */
    public void scoreProspects(
        String studyId, 
        List<Specimen> specimenList) throws ServiceException
    {
        if (!scoreProspectsX(studyId, specimenList))
            throw getException();
    }


    /**
     * Obtain the user's catalog of studies.  
     *
     * @return  {@code null} means the user's catalog could not be obtained
     *              (call {@code getException()} for details).
     *          Otherwise the return value is a reference to the user's 
     *              catalog, containing all the studies the user has a role on.
     */
    private Catalog obtainCatalogX()
    {
        cachedCatalog = null;

        // The next line insures that the Get Catalog HTTP request will 
        // get full entries for studies; if the second parameter is "off",
        // then only partial studies are obtained
        yRequest.setAuxHeader("x-yosokumo-full-entries", "on");

        String catalogUri = "/";

        if (!yRequest.getFromServer(catalogUri))
            return (Catalog)reportProblem();

        byte [] catalogAsBytes = yRequest.getEntity();

        if (catalogAsBytes == null || !requestOk(Role.Privilege.GET_CATALOG))
            return (Catalog)reportProblem();

        Catalog catalog = ydif.makeCatalogFromBytes(catalogAsBytes);

        if (catalog == null)
        {
            exception = ydif.getException();
            return null;
        }

        for (Study s : catalog.getStudyCollection())
        {
            if (mergePanelIntoStudy(s) == null)
                return null;
        }

        if (catalog != null)
            cachedCatalog = Catalog.copyCatalog(catalog);

        return catalog;

    }   //  end obtainCatalogX

    /**
     * Create a default study and add it to the user's catalog of studies.  
     *
     * @return  {@code null} means the study could not be created (call 
     *              {@code getException()} for details).
     *          Otherwise the return value is a reference to a newly-created 
     *              study with default name, type, status, and visibility.
     *              (Note that the name will be an empty string.)
     *              The study identifier, owner identifier, and owner name 
     *              are initialized, as well as references to study components.
     */
    private Study createStudyX()
    {
        return createStudy(new Study());
    }

    /**
     * Create a named study and add it to the user's catalog of studies.  
     *
     * @param   studyName specifies the study name.
     * @return  {@code null} means the study could not be created (call 
     *              {@code getException()} for details).
     *          Otherwise the return value is a reference to a newly-created 
     *              study with name as specified by the parameter, and default
     *              type, status, and visibility.  The study identifier, 
     *              owner identifier, and owner name are initialized, as well 
     *              as references to study components.
     */
    private Study createStudyX(String studyName)
    {
        Study study = new Study();
        study.setStudyName(studyName);
        return createStudy(study);
    }

    /**
     * Create a study with specified name and type and add it to the user's 
     *      catalog of studies.  
     *
     * @param   studyName specifies the study name.
     * @param   studyType specifies the study type.
     * @return  {@code null} means the study could not be created (call 
     *              {@code getException()} for details).
     *          Otherwise the return value is a reference to a newly-created 
     *              study with name and type as specified by the parameters, 
     *              and default status and visibility.  The study identifier, 
     *              owner identifier, and owner name are initialized, as well 
     *              as references to study components.
     */
    private Study createStudyX(String studyName, Study.Type studyType)
    {
        Study study = new Study();
        study.setStudyName(studyName);
        study.setType(studyType);
        return createStudy(study);
    }

    /**
     * Create a study with specified name, type, status, and visibility, and 
     *      add it to the user's catalog of studies.  
     *
     * @param   studyName specifies the study name.
     * @param   studyType specifies the study type.
     * @param   studyStatus specifies the study status.
     * @param   studyVisibility specifies the study visibility.
     * @return  {@code null} means the study could not be created (call 
     *              {@code getException()} for details).
     *          Otherwise the return value is a reference to a newly-created 
     *              study with name, type, status, and visibility as specified 
     *              by the parameters.  The study identifier, owner identifier,
     *              and owner name are initialized, as well as references to 
     *              study components.
     */
    private Study createStudyX(
        String           studyName,
        Study.Type       studyType,
        Study.Status     studyStatus,
        Study.Visibility studyVisibility)
    {
        return createStudy(new Study(studyName, studyType, studyStatus, 
                                                        studyVisibility));
    }


    /**
     * Update a study, changing the study name.
     *
     * @param   studyId specifies the identifier of the study to update.
     * @param   studyName is the new name to assign to the study.
     * @return  {@code null} means the study could not be changed (call 
     *              {@code getException()} for details).
     *          Otherwise the return value is the {@code Study} specified by 
     *              studyId, with its name changed to studyName.  The name 
     *              is also changed on the server.
     */
    private Study updateStudyX(String studyId, String studyName)
    {
        initForOperation("updateStudy with name change");

        Study study = mapStudyIdentifierToStudy(studyId);

        if (study == null)
            return (Study)reportProblem();

        study.setStudyName(studyName);

        byte [] nameAsBytes = 
                    ydif.makeBytesFromStudyName(study.getStudyName());

        if (!yRequest.putToServer(study.getNameControlLocation(), 
                        nameAsBytes) || !requestOk(Role.Privilege.PUT_CONTROL))
            return (Study)reportProblem();

        return study;
    }


    /**
     * Update a study, changing the status.
     *
     * @param   studyId specifies the identifier of the study to update.
     * @param   studyStatus is the new status to assign to the study.
     * @return  {@code null} means the study could not be changed (call 
     *              {@code getException()} for details).
     *          Otherwise the return value is the {@code Study} specified by 
     *              studyId, with its status changed to studyStatus.  The 
     *              status is also changed on the server.
     */
    private Study updateStudyX(String studyId, Study.Status studyStatus)
    {
        initForOperation("updateStudy with status change");

        Study study = mapStudyIdentifierToStudy(studyId);

        if (study == null)
            return (Study)reportProblem();

        study.setStatus(studyStatus);

        byte [] statusAsBytes = 
                    ydif.makeBytesFromStudyStatus(study.getStatus());

        if (!yRequest.putToServer(study.getStatusControlLocation(), 
                    statusAsBytes) || !requestOk(Role.Privilege.PUT_CONTROL))
            return (Study)reportProblem();

        return study;
    }

    /**
     * Update a study, changing the visibility.
     *
     * @param   studyId specifies the identifier of the study to update.
     * @param   studyVisibility is the new visibility to assign to the study.
     * @return  {@code null} means the study could not be changed (call 
     *              {@code getException()} for details).
     *          Otherwise the return value is the {@code Study} specified by 
     *              studyId, with its visibility changed to studyVisibility.  
     *              The visibility is also changed on the server.
     */
    private Study updateStudyX(String studyId, Study.Visibility studyVisibility)
    {
        initForOperation("updateStudy with visibility change");

        Study study = mapStudyIdentifierToStudy(studyId);

        if (study == null)
            return (Study)reportProblem();

        study.setVisibility(studyVisibility);

        byte [] visibilityAsBytes = 
                    ydif.makeBytesFromStudyVisibility(study.getVisibility());

        if (!yRequest.putToServer(study.getVisibilityControlLocation(), 
                visibilityAsBytes) || !requestOk(Role.Privilege.PUT_CONTROL))
            return (Study)reportProblem();

        return study;
    }

    /**
     * Obtain a study from the server. 
     *
     * @param   studyId specifies the identifier of the study to obtain.
     * @return  {@code null} means the study could not be obtained (call 
     *              {@code getException()} for details).
     *          Otherwise the return value is a reference to a study obtained
     *              from the server with study identifier == studyId.  
     */
    private Study obtainStudyX(String studyId)
    {
        initForOperation("obtainStudy");

        Study study = mapStudyIdentifierToStudy(studyId);

        if (study == null)
            return (Study)reportProblem();

        if (refreshedCachedCatalog)
            return study;

        // A study with id equal to studyId was found in the 
        // cachedCatalog, but since the cachedCatalog was not refreshed,
        // the study is gotten from the server

        if (!yRequest.getFromServer(study.getStudyLocation()))
            return (Study)reportProblem();

        byte [] studyAsBytes = yRequest.getEntity();

        if (studyAsBytes == null || !requestOk(Role.Privilege.GET_STUDY))
            return (Study)reportProblem();

        study = ydif.makeStudyFromBytes(studyAsBytes);

        if (study == null)
        {
            exception = ydif.getException();
            return null;
        }

        study = mergePanelIntoStudy(study);

        return study;

    }   //  end obtainStudyX


    /**
     * Delete a study from the server. 
     *
     * @param   studyId specifies the identifier of the study to delete.
     * @return  {@code false} means the study could not be deleted (call 
     *              {@code getException()} for details).
     *          {@code true} means the study was deleted.
     */
    private boolean deleteStudyX(String studyId)
    {
        initForOperation("deleteStudy");

        Study study = mapStudyIdentifierToStudy(studyId);

        if (study == null)
        {
            reportProblem();
            return false;
        }

        if (!yRequest.deleteFromServer(study.getStudyLocation()) || 
                                    !requestOk(Role.Privilege.DELETE_STUDY))
        {
            reportProblem();
            return false;
        }

        return true;

    }   //  end deleteStudyX


    /**
     * Obtain the roster for a study.
     *
     * @param   studyId specifies the identifier of the study whose roster 
     *              is to be obtained.
     * @return  {@code null} means the roster could not be obtained (call 
     *              {@code getException()} for details).
     *          Otherwise the return value is a reference to the roster for 
     *              the study specified by the input parameter.
     */
    private Roster obtainRosterX(String studyId)
    {
        initForOperation("obtainRoster");

        Study study = mapStudyIdentifierToStudy(studyId);

        if (study == null)
            return (Roster)reportProblem();

        // The next line insures that the Get Roster HTTP request will 
        // get full entries for roles; if the second parameter is "off",
        // then only partial roles are obtained
        yRequest.setAuxHeader("x-yosokumo-full-entries", "on");

        if (!yRequest.getFromServer(study.getRosterLocation()))
            return (Roster)reportProblem();

        byte [] rosterAsBytes = yRequest.getEntity();

        if (rosterAsBytes == null || !requestOk(Role.Privilege.GET_ROSTER))
            return (Roster)reportProblem();

        Roster roster = ydif.makeRosterFromBytes(rosterAsBytes);

        if (roster == null)
            exception = ydif.getException();

        return roster;

    }   //  end obtainRosterX

    /**
     * Add a {@code Role} to a study's roster.
     *
     * @param  role  the role to add to the roster.  The user identifier 
     *               and study identifier fields must be set.  The role 
     *               is added to the roster of the study, specifying the 
     *               privileges of the user on the study.
     *
     * @return  {@code null} means the role could not be created (call 
     *              {@code getException()} for details).
     *          Otherwise the return value is a reference to a newly-created
     *              role corresponding to the input role with additional 
     *              fields filled out by the service.
     */
    private Role createRoleX(Role role)
    {
        initForOperation("createRole");

        Study study = mapStudyIdentifierToStudy(role.getStudyIdentifier());

        if (study == null)
            return (Role)reportProblem();

        byte [] roleAsBytes = ydif.makeBytesFromRole(role);

        if (!yRequest.postToServer(study.getRosterLocation(), roleAsBytes))
            return (Role)reportProblem();

        byte [] newRoleAsBytes = yRequest.getEntity();

        if (newRoleAsBytes == null || !requestOk(Role.Privilege.POST_ROSTER))
            return (Role)reportProblem();

        Role newRole = ydif.makeRoleFromBytes(newRoleAsBytes);

        if (newRole == null)
            exception = ydif.getException();

        return newRole;

    }   //  end createRoleX


    /**
     * Update a role on the server.
     *
     * @param   role specifies the role to change.
     * @return  {@code null} means the role could not be changed (call 
     *              {@code getException()} for details).
     *          Otherwise the return value is the input parameter {@code role}
     *              and all the fields for role have been changed on the 
     *              server.
     */
    private Role updateRoleX(Role role)
    {
        initForOperation("updateRole");

        Role r = getRoleHelper(role.getUserIdentifier(), 
                                                role.getStudyIdentifier());

        if (r == null)
            return (Role)reportProblem();

        byte [] roleAsBytes = ydif.makeBytesFromRole(role);

        if (!yRequest.putToServer(r.getRoleLocation(), roleAsBytes) || 
                                        !requestOk(Role.Privilege.PUT_ROLE))
            return (Role)reportProblem();

        return role;
    }


    /**
     * Obtain a role.  The role of a specific user on a specific study is
     * obtained.
     *
     * @param   userId specifies the user.
     * @param   studyId specifies the study.
     *
     * @return  {@code null} means the role could not be obtained (call 
     *              {@code getException()} for details).
     *          Otherwise the return value is a reference to the role of 
     *              the specified user on the specified study.
     */
    private Role obtainRoleX(String userId, String studyId)
    {
        initForOperation("obtainRole");

        return getRoleHelper(userId, studyId);
    }


    /**
     * Delete a role.  The role of a specific user on a specific study is
     * deleted.
     *
     * @param   userId specifies the user.
     * @param   studyId specifies the study.
     *
     * @return  {@code false} means the role could not be deleted (call 
     *              {@code getException()} for details).
     *          {@code true} means the role played on the user specified by 
     *              userId on the study specified by studyId was deleted.
     */
    private boolean deleteRoleX(String userId, String studyId)
    {
        initForOperation("deleteRole");

        Role role = getRoleHelper(userId, studyId);

        if (role == null)
            return false;

        if (!yRequest.deleteFromServer(role.getRoleLocation()) || 
                                     !requestOk(Role.Privilege.DELETE_ROLE))
        {
            reportProblem();
            return false;
        }

        return true;

    }   //  end deleteRoleX

    /**
     * Describe a study's table.  A list of column definitions is associated
     * with a study.
     *
     * @param   studyId specifies the study whose table is to be described.
     * @param   predictorList a list of the predictors (= variables = columns)
     *              for the table.
     *
     * @return  {@code false} means the table description failed (call 
     *              {@code getException()} for details).
     *          {@code true} means the study's table was successfully 
     *              described.
     */
    private boolean describePredictorsX(
        String studyId, 
        List<Predictor> predictorList)
    {
        initForOperation("describePredictors");

        Study study = mapStudyIdentifierToStudy(studyId);

        if (study == null)
        {
            reportProblem();
            return false;
        }

        int totalToTransmit = predictorList.size();
        String tableUri = study.getTableLocation();

        if (totalToTransmit == 0)
            return postEmptyBlock(studyId, tableUri, 
                                                Role.Privilege.POST_TABLE);

        // The input list of Predictors is transmitted to the server
        // in batches no bigger than MAX_ITEMS_TO_SEND_VIA_HTTP.

        //??? Consider removing the logic to break up into smaller blocks
        //??? because no specimen with more than MAX_ITEMS_TO_SEND_VIA_HTTP 
        //??? cells can be scored.

        int firstIdx = 0;

        while (totalToTransmit > 0)
        {
            int n = totalToTransmit;
            if (n > MAX_ITEMS_TO_SEND_VIA_HTTP)
                n = MAX_ITEMS_TO_SEND_VIA_HTTP;
            int lastIdx = firstIdx + n;

            List<Predictor> plist = predictorList.subList(firstIdx, lastIdx);

            totalToTransmit -= n;
            firstIdx = lastIdx;

            PredictorBlock pblock = new PredictorBlock(studyId, plist);

            byte [] blockAsBytes = ydif.makeBytesFromBlock(pblock);

            if (!yRequest.postToServer(tableUri, blockAsBytes) || 
                                    !requestOk(Role.Privilege.POST_TABLE))
            {
                reportProblem();
                return false;
            }
        }

        return true;

    }   //  end describePredictorsX


    /**
     * Populate the rows of a study's table.  A list of rows is 
     * inserted into a study's table.
     *
     * @param   studyId specifies the study whose table is to be populated.
     * @param   specimenList a list of the specimens 
     *              (= rows = observations = records) for the table.
     *
     * @return  {@code false} means the table population failed (call 
     *              {@code getException()} for details).
     *          {@code true} means the study's table was successfully 
     *              populated with rows.
     */
    private boolean loadSubjectsX(
        String studyId, 
        List<Specimen> specimenList)
    {
        initForOperation("loadSubjects");

        return postSpecimens(studyId, specimenList, true);

    }   //  end loadSubjectsX


    /**
     * Score prospects (compute predictands) using a study's model.  
     *
     * @param   studyId specifies the study whose model is to be used.
     * @param   specimenList a list of specimens 
     *              (= rows = observations = records) whose predictands
     *              are to be computed.
     *
     * @return  {@code false} means the predictand computation failed (call 
     *              {@code getException()} for details).
     *          {@code true} means the predictand computations were succesful;
     *              the predictand members of each Specimen in the list
     *              contain the results of the computations.
     */
    private boolean scoreProspectsX(
        String studyId, 
        List<Specimen> specimenList)
    {
        initForOperation("scoreProspects");

        return postSpecimens(studyId, specimenList, false);

    }   //  end scoreProspectsX


    /**
     * Post specimens to a study's table or model.  In the former case,
     * the rows of the table are populated with the specimens, and in the
     * latter case the specimens are scored.
     *
     * @param   studyId specifies the study to post to.
     * @param   specimenList is a list of the specimens (= rows = observations 
     *              = records) to post.
     * @param   postToTable indicates whether to post to the study's table or
     *              model.
     *
     * @return  false means the post operation failed (call {@code 
     *              getException()} for details).
     *          true means the post operation succeeded.
     */
    private boolean postSpecimens(
        String         studyId, 
        List<Specimen> specimenList,
        boolean        postToTable)
    {
        Study study = mapStudyIdentifierToStudy(studyId);

        if (study == null)
        {
            reportProblem();
            return false;
        }

        String uri;
        Role.Privilege privilege;

        if (postToTable)
        {
            uri = study.getTableLocation();
            privilege = Role.Privilege.POST_TABLE;
        }
        else
        {
            uri = study.getModelLocation();
            privilege = Role.Privilege.POST_MODEL;
        }

        // If the input specimen list is empty, post an empty block 

        int numSpecimensToTransmit = specimenList.size();

        if (numSpecimensToTransmit == 0)
            return postEmptyBlock(studyId, uri, privilege);

        // Count the total number of cells to transmit

        int numCellsToTransmit = 0;

        for (Specimen s : specimenList)
            numCellsToTransmit += s.size();

        int firstIdx = 0; 

        // If the total number of items to transmit is small enough, transmit 
        // everything as one SpecimenBlock

        int numItemsToTransmit = numCellsToTransmit + numSpecimensToTransmit;

        if (numItemsToTransmit <= MAX_ITEMS_TO_SEND_VIA_HTTP)
            return (-1 != postSpecimenBlock(study, specimenList, firstIdx, 
                                    numSpecimensToTransmit, postToTable));

        // Due to the number of items in the specimen list, it is necessary 
        // to break the list up into two or more SpecimenBlocks

        numSpecimensToTransmit = 0;
        numCellsToTransmit = 0;

        for (Specimen s : specimenList)
        {
            numItemsToTransmit = numCellsToTransmit + numSpecimensToTransmit;

            if (numItemsToTransmit+s.size()+1 <= MAX_ITEMS_TO_SEND_VIA_HTTP)
            {
                numCellsToTransmit += s.size();
                ++numSpecimensToTransmit;
                continue;
            }

            firstIdx = postSpecimenBlock(study, specimenList, firstIdx, 
                                    numSpecimensToTransmit, postToTable);
            if (firstIdx == -1)
                return false;

            numSpecimensToTransmit = 1;
            numCellsToTransmit = s.size();

            numItemsToTransmit = numCellsToTransmit + numSpecimensToTransmit;

            if (numItemsToTransmit <= MAX_ITEMS_TO_SEND_VIA_HTTP)
                continue;

            // Specimen s contains more than MAX_ITEMS_TO_SEND_VIA_HTTP cells,
            // which means it cannot be handled in one HTTP request

            reportProblem("A specimen cannot be scored because it contains " +
                "more than " + MAX_ITEMS_TO_SEND_VIA_HTTP + " cells");

            return false;
        }

        return (-1 != postSpecimenBlock(study, specimenList, firstIdx, 
                                    numSpecimensToTransmit, postToTable));

    }   //  end postSpecimens


    /**
     * Post an empty block to the service.
     *
     * @param   studyId specifies the study to which to post the empty block.
     * @param   uri is either the table location or the model location of the
     *              study.
     * @param   operation is either Role.Privilege.POST_TABLE or 
     *              Role.Privilege.POST_MODEL, agreeing with the uri.
     *
     * @return  {@code false} means the post failed (call 
     *              {@code getException()} for details).
     *          {@code true} means an empty block was successfully posted.
     */
    private boolean postEmptyBlock(
        String studyId,
        String uri,
        Role.Privilege operation)
    {
        EmptyBlock eblock = new EmptyBlock(studyId);

        byte [] blockAsBytes = ydif.makeBytesFromBlock(eblock);

        if (!yRequest.postToServer(uri, blockAsBytes) || 
                                                    !requestOk(operation))
        {
            reportProblem();
            return false;
        }

        return true;
    }


    /**
     * Post a specimen block to a study's table or model.  In the former case,
     * the rows of the table are populated with the specimen block, and in the
     * latter case the specimens in the block are scored.
     *
     * @param   study specifies the study to post to.
     * @param   specimenList is a list of the specimens (= rows = observations 
     *              = records) from which the specimen block is constructed.
     * @param   firstIdx (>= 0) is an index to the first element in 
     *              specimenList to include in the specimen block.
     * @param   numSpecimensToTransmit (>= 0) is the number of specimens to 
     *              include in the block.
     * @param   postToTable indicates whether to post to the study's table or
     *              model.
     *
     * @return  -1 means the post operation failed (call {@code 
     *              getException()} for details).
     *          >= 0 means the post operation succeeded, and the return value 
     *              is an index to the first element in specimenList which 
     *              was not part of the posted block.
     */
    private int postSpecimenBlock(
        Study          study,
        List<Specimen> specimenList,
        int            firstIdx,
        int            numSpecimensToTransmit,
        boolean        postToTable)
    {
        assert firstIdx >= 0;

        if (numSpecimensToTransmit == 0)
            return firstIdx;

        String studyId = study.getStudyIdentifier(); 
        String uri;
        Role.Privilege privilege;

        if (postToTable)
        {
            uri = study.getTableLocation();
            privilege = Role.Privilege.POST_TABLE;
        }
        else
        {
            uri = study.getModelLocation();
            privilege = Role.Privilege.POST_MODEL;
        }
 
        int lastIdx = firstIdx + numSpecimensToTransmit;

        List<Specimen> slist = specimenList.subList(firstIdx, lastIdx);

        SpecimenBlock sblock = new SpecimenBlock(studyId, slist);
        byte [] blockAsBytes = ydif.makeBytesFromBlock(sblock);

        if (!yRequest.postToServer(uri, blockAsBytes) || !requestOk(privilege))
        {
            reportProblem();
            return -1;
        }

        if (postToTable)
            return lastIdx;

        // We are posting to a model, i.e., scoring specimens, so get the 
        // scores and stash them in the specimen list

        blockAsBytes = yRequest.getEntity();

        if (blockAsBytes == null)
        {
            reportProblem();
            return -1;
        }

        sblock = (SpecimenBlock)ydif.makeBlockFromBytes(blockAsBytes);

        if (sblock == null)
        {
            exception = ydif.getException();
            return -1;
        }

        if (sblock.size() != slist.size())
        {
            String message = slist.size() + " specimens " + 
                "posted to model, " + sblock.size() + " predictions returned";
            reportProblem(message);
            return -1;
        }

        Iterator<Specimen> iter = slist.iterator();

        for (Specimen s : sblock.getSpecimenSequence())
        {
            Specimen inSpecimen = iter.next();

            if (s.getSpecimenKey() != inSpecimen.getSpecimenKey())
            {
                String message = "Specimen key mismatch:  Input key = " + 
                    inSpecimen.getSpecimenKey() + ", output key = " + 
                    s.getSpecimenKey();
                reportProblem(message);
                return -1;
            }

            inSpecimen.setPredictand(s.getPredictand());
        }

        return lastIdx;

    }   //  end postSpecimenBlock



    /**
     * Create a study and add it to the user's catalog of studies.  
     *
     * @param   study specifies the name, type, status, and visibility of the 
     *              new study to create.  The type can never be changed.
     * @return  {@code null} means the study could not be created (call 
     *              {@code getException()} for details).
     *          Otherwise the return value is a reference to a newly-created 
     *              study with name, type, status, and visibility as specified
     *              by the input parameter.  The study identifier, owner 
     *              identifier, and owner name are initialized, as well as 
     *              various references.
     */
    Study createStudy(Study study)
    {
        initForOperation("createStudy");

        String catalogUri = "/";

        byte [] studyAsBytes = ydif.makeBytesFromStudy(study);

        if (!yRequest.postToServer(catalogUri, studyAsBytes))
            return (Study)reportProblem();

        byte [] newStudyAsBytes = yRequest.getEntity();

        if (newStudyAsBytes == null || !requestOk(Role.Privilege.POST_CATALOG))
            return (Study)reportProblem();

        Study newStudy = ydif.makeStudyFromBytes(newStudyAsBytes);

        if (newStudy == null)
            exception = ydif.getException();
        else
            newStudy = mergePanelIntoStudy(newStudy);

        return newStudy;

    }   //  end createStudy

    /**
     * Merge the panel data for a study into the {@code Study} object itself.
     *
     * @param   study specifies the study to change.
     * @return  {@code null} means the panel merge was not successful (call 
     *              {@code getException()} for details).
     *          Otherwise the return value is the input parameter {@code study}
     *              with all the fields from the study's panel merged into the 
     *              corresponding fields in the {@code Study} object.
     */
    private Study mergePanelIntoStudy(Study study)
    {
        if (!yRequest.getFromServer(study.getPanelLocation()))
            return (Study)reportProblem();

        byte [] panelAsBytes = yRequest.getEntity();

        if (panelAsBytes == null || !requestOk(Role.Privilege.GET_PANEL))
            return (Study)reportProblem();

        Panel panel = ydif.makePanelFromBytes(panelAsBytes);

        if (panel == null)
        {
            exception = ydif.getException();
            return null;
        }

        study.setNameControlLocation(
                                    panel.getNameControlLocation());
        study.setStatusControlLocation(
                                    panel.getStatusControlLocation());
        study.setVisibilityControlLocation(
                                    panel.getVisibilityControlLocation());

        study.setBlockCount        (panel.getBlockCount()        );
        study.setCellCount         (panel.getCellCount()         );
        study.setProspectCount     (panel.getProspectCount()     );

        study.setCreationTime      (panel.getCreationTime()      );
        study.setLatestBlockTime   (panel.getLatestBlockTime()   );
        study.setLatestProspectTime(panel.getLatestProspectTime());

        return study;

    }   //  end mergePanelIntoStudy

    /**
     * Get the role of a specific user on a specific study.
     *
     * @param   userId specifies the user.
     * @param   studyId specifies the study.
     * @return  {@code null} means the role could not be obtained.
     *          Otherwise the return value is the {@code role} of the 
     *              specified user on the specified study.
     */
    private Role getRoleHelper(
        String userId, 
        String studyId) 
    {
        Roster roster = obtainRosterX(studyId);

        if (roster == null)
            return (Role)reportProblem();

        Role role = roster.getRole(userId);

        if (role == null)
            return (Role)reportProblem("A Role for user " + userId + 
                            " on study " + studyId + " does not exist");

        return role;

    }   //  end obtainRole

    /**
     * Get the study corresponding to a study id.  Whenever possible, the 
     * study is obtained from the cached catalog.
     *
     * @param   studyId specifies the study to get.
     * @return  {@code null} means the study could not be obtained.
     *          Otherwise the return value is the {@code Study} specified 
     *              by studyId.  Note that the boolean refreshedCachedCatalog
     *              is set to indicate if this method refreshed the cached 
     *              catalog or not.
     */

    private Study mapStudyIdentifierToStudy(String studyId)
    {
        refreshedCachedCatalog = false;

        Study study = null;

        boolean gotCachedCatalog = (cachedCatalog != null);

        if (gotCachedCatalog)
        {
            study = cachedCatalog.getStudy(studyId);
            if (study == null)
                gotCachedCatalog = false;
        }

        if (!gotCachedCatalog)
        {
            if (obtainCatalogX() == null)
                return null;    // obtainCatalog reports an error
            refreshedCachedCatalog = true;
            study = cachedCatalog.getStudy(studyId);
        }

        if (study == null)
        {
            String message = "Study (id=" + studyId + ") not found " +
                "in catalog for user " + cachedCatalog.getUserName() + 
                " (id=" + cachedCatalog.getUserIdentifier() + ")";
              reportProblem(message);
        }

        return study;

    }   //  end mapStudyIdentifierToStudy

    /**
     * Set up a {@code ServiceException} describing a problem detected by 
     * the service.
     *
     * @return  {@code null} in all cases.  The field {@code exception} is 
     *              set to a {@code ServiceException} with a message 
     *              constructed from a pre-existing exception, a Message 
     *              object in the HTTP entity, and/or the HTTP status code.
     */
    private Object reportProblem()
    {
        return reportProblem("");
    }

    /**
     * Set up a {@code ServiceException} describing a problem detected by 
     * the service.
     *
     * @param   errMess  is the prefix to use in an error message.
     * @return  {@code null} in all cases.  The field {@code exception} is 
     *              set to a {@code ServiceException} with a message 
     *              constructed from a pre-existing exception, a Message 
     *              object in the HTTP entity, and/or the HTTP status code.
     */
    private Object reportProblem(String errMess)
    {
        int statusCode = yRequest.getStatusCode();

        if (errMess.isEmpty() && invalidStatusCode)
            errMess = "Status code invalid for the operation";

        if (errMess.isEmpty())
            errMess = methodName + " failed";
        else
            errMess = methodName + " failed:  " + errMess;
 
        if (exception != null)
        {
            String oldMess = exception.getMessage();
            if (oldMess == null || !oldMess.startsWith(errMess))
                exception = new ServiceException(errMess, exception,
                                                statusCode, methodName);
            return null;
        }

        Message message = null;

        byte [] messageAsBytes = yRequest.getEntity();
        if (messageAsBytes != null)
            message = ydif.makeMessageFromBytes(messageAsBytes);

        if (statusCode != 0)
            errMess = errMess + " (status code = " + statusCode + ")";

        if (message != null)
            errMess = errMess + ":  " + message.getText();

        exception = new ServiceException(errMess, yRequest.getException(),
                                                statusCode, methodName);

        return null;

    }   //  end reportProblem


}   // end class Service

// end Service.java
