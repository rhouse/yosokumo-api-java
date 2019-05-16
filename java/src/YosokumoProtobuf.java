// YosokumoProtobuf.java

package com.yosokumo.core;

import java.util.Collections;
import java.util.List;

import com.yosokumo.core.protobuf.*;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Implements all functionality for transforming HTTP entity bytes in Google 
 * Protocol Buffer form into Yosokumo Java objects (e.g., {@code Catalog} and 
 * {@code Study}) and vice versa.  In general there are six functions for 
 * each Yosokumo class, illustrated here for {@code Study}:
 * <p>
 * Converting from HTTP entity bytes to Study:
 * <pre>
 *   public           Study makeStudyFromBytes        (byte [] studyAsBytes)
 *   private ProtoBuf.Study makeProtobufStudyFromBytes(byte [] studyAsBytes)
 *   private          Study makeStudyFromProtobufStudy(ProtoBuf.Study protoStudy)
 * </pre>
 * Converting from Study to HTTP entity bytes:
 * <pre>
 *   public         byte [] makeBytesFromStudy        (Study study)
 *   private ProtoBuf.Study makeProtobufStudyFromStudy(Study study)
 *   private        byte [] makeBytesFromProtobufStudy(ProtoBuf.Study protoStudy)
 * </pre>
 * Note how Google Protocol Buffer objects are used as the intermediaries 
 * between HTTP entity bytes and Yosokumo objects.  Here is another view of the
 * transformations done by the functions shown above:
 * <pre>
 *    makeStudyFromBytes:
 *      byte []        -> makeProtobufStudyFromBytes -> ProtoBuf.Study
 *      ProtoBuf.Study -> makeStudyFromProtobufStudy -> Study
 *
 *    makeBytesFromStudy:
 *      Study          -> makeProtobufStudyFromStudy -> ProtoBuf.Study
 *      ProtoBuf.Study -> makeBytesFromProtobufStudy -> byte []
 * </pre>
 */
class YosokumoProtobuf implements YosokumoDIF
{
    private ServiceException exception = null;

    public String getContentType()
    {
        return "application/yosokumo+protobuf";
    }

    public ServiceException getException()
    {
        return exception;
    }


    public Catalog makeCatalogFromBytes(byte [] catalogAsBytes)
    {
        ProtoBuf.Catalog protoCatalog = makeProtobufCatalogFromBytes(catalogAsBytes);

        if (protoCatalog == null)
            return null;

        return makeCatalogFromProtobufCatalog(protoCatalog);

    }   //  end makeCatalogFromBytes


    private ProtoBuf.Catalog makeProtobufCatalogFromBytes(byte [] catalogAsBytes)
    {
        // Parse the input bytes into a Protobuf catalog object

        ProtoBuf.Catalog protoCatalog = null; 

        try
        {
            protoCatalog = ProtoBuf.Catalog.parseFrom(catalogAsBytes);
        }
        catch (InvalidProtocolBufferException e)
        {
            exception = new ServiceException(
                                "parseFrom bytes to Protobuf catalog failed", e);
            return null;
        }

        return protoCatalog;

    }   //  end makeProtobufCatalogFromBytes


    private Catalog makeCatalogFromProtobufCatalog(ProtoBuf.Catalog protoCatalog)
    {
        // Create a Yosokumo Catalog from a Protobuf catalog

        Catalog catalog = new Catalog();

        // Note that if any of the following fields are missing in the 
        // Protobuf catalog, they will be empty strings

        catalog.setUserIdentifier (protoCatalog.getUserIdentifier());
        catalog.setUserName       (protoCatalog.getUserName()      );
        catalog.setCatalogLocation(protoCatalog.getLocation()      );

        for (ProtoBuf.Study protoStudy : protoCatalog.getStudyList())
        {
            Study study = catalog.addStudy(makeStudyFromProtobufStudy(protoStudy));
            if (study != null)
            {
                exception = new ServiceException("Yosokumo Catalog contains" +
                                    " two studies with the same identifier");
                return null;
            }
        }

        return catalog;

    }   //  end makeCatalogFromProtobufCatalog


    public Study makeStudyFromBytes(byte [] studyAsBytes)
    {
        ProtoBuf.Study protoStudy = makeProtobufStudyFromBytes(studyAsBytes);

        if (protoStudy == null)
            return null;

        return makeStudyFromProtobufStudy(protoStudy);

    }   //  end makeStudyFromBytes


    private ProtoBuf.Study makeProtobufStudyFromBytes(byte [] studyAsBytes)
    {
        // Parse the input bytes into a Protobuf study object

        ProtoBuf.Study protoStudy = null; 

        try
        {
            protoStudy = ProtoBuf.Study.parseFrom(studyAsBytes);
        }
        catch (InvalidProtocolBufferException e)
        {
            exception = new ServiceException(
                                "parseFrom bytes to Protobuf study failed", e);
            return null;
        }

        return protoStudy;

    }   //  end makeProtobufStudyFromBytes


    private Study makeStudyFromProtobufStudy(ProtoBuf.Study protoStudy)
    {
        Study study = new Study();

        // Note that if any of the following fields are missing in the 
        // protobuf, they will be empty strings

        study.setStudyIdentifier(protoStudy.getStudyIdentifier());
        study.setStudyName      (protoStudy.getStudyName()      );
        study.setStudyLocation  (protoStudy.getLocation()       );

        study.setType      (protoTypeToType            (protoStudy.getType()  ));
        study.setStatus    (protoStatusToStatus        (protoStudy.getStatus()));
        study.setVisibility(protoVisibilityToVisibility(
                                                   protoStudy.getVisibility()));

        study.setOwnerIdentifier(protoStudy.getOwner().getUserIdentifier());
        study.setOwnerName      (protoStudy.getOwner().getUserName());
        study.setTableLocation  (protoStudy.getTable().getLocation());
        study.setModelLocation  (protoStudy.getModel().getLocation());
        study.setPanelLocation  (protoStudy.getPanel().getLocation());
        study.setRosterLocation (protoStudy.getRoster().getLocation());

        return study;

    }   //  end makeStudyFromProtobufStudy


    public byte [] makeBytesFromStudy(Study study)
    {
        ProtoBuf.Study protoStudy = makeProtobufStudyFromStudy(study);
        return makeBytesFromProtobufStudy(protoStudy);
    }

    private ProtoBuf.Study makeProtobufStudyFromStudy(Study study)
    {
        ProtoBuf.Study.Type      type   = typeToProtobufType(study.getType());
        ProtoBuf.Study.Status    status = statusToProtobufStatus(study.getStatus());
        ProtoBuf.Study.Visibility visibility = visibilityToProtobufVisibility(
                                                    study.getVisibility());

        // The following throws an exception if there is no study identifier 
        // because the Protobuf definition says that a study identifier is 
        // required

        ProtoBuf.Study protoStudy = ProtoBuf.Study.newBuilder()
            .setStudyIdentifier (study.getStudyIdentifier())
            .setStudyName       (study.getStudyName())
            .setType            (type      )
            .setStatus          (status    )
            .setVisibility      (visibility)
            .build();  

        return protoStudy;
    }

    private byte [] makeBytesFromProtobufStudy(ProtoBuf.Study protoStudy)
    {
        return protoStudy.toByteArray();
    }

    private Study.Type protoTypeToType(ProtoBuf.Study.Type protoType)
    {
        switch (protoType)
        {
        case Class:   return Study.Type.CLASS ;
        case Rank:    return Study.Type.RANK  ;
        case Number:  return Study.Type.NUMBER;
        case Chance:  return Study.Type.CHANCE;
        }
        return null;
    }

    private ProtoBuf.Study.Type typeToProtobufType(Study.Type type)
    {
        switch (type)
        {
        case CLASS :  return ProtoBuf.Study.Type.Class ;
        case RANK  :  return ProtoBuf.Study.Type.Rank  ;
        case NUMBER:  return ProtoBuf.Study.Type.Number;
        case CHANCE:  return ProtoBuf.Study.Type.Chance;
        }
        return null;
    }

    private Study.Status protoStatusToStatus(ProtoBuf.Study.Status protoStatus)
    {
        switch (protoStatus)
        {
        case Running:  return Study.Status.RUNNING;
        case Standby:  return Study.Status.STANDBY;
        case Stopped:  return Study.Status.STOPPED;
        }
        return null;
    }

    private ProtoBuf.Study.Status statusToProtobufStatus(Study.Status status)
    {
        switch (status)
        {
        case RUNNING:  return ProtoBuf.Study.Status.Running;
        case STANDBY:  return ProtoBuf.Study.Status.Standby;
        case STOPPED:  return ProtoBuf.Study.Status.Stopped;
        }
        return null;
    }

    private Study.Visibility protoVisibilityToVisibility(
        ProtoBuf.Study.Visibility protoVisibility)
    {
        switch (protoVisibility)
        {
        case Private:   return Study.Visibility.PRIVATE ;
        case Public:    return Study.Visibility.PUBLIC  ;
        }
        return null;
    }

    private ProtoBuf.Study.Visibility visibilityToProtobufVisibility(
        Study.Visibility visibility)
    {
        switch (visibility)
        {
        case PRIVATE :  return ProtoBuf.Study.Visibility.Private ;
        case PUBLIC  :  return ProtoBuf.Study.Visibility.Public  ;
        }
        return null;
    }

    public byte [] makeBytesFromStudyName(String name)
    {
        ProtoBuf.Panel.StudyNameControl protoNameContol = 
                                    makeProtobufStudyNameControlFromName(name);
        return makeBytesFromProtobufStudyNameControl(protoNameContol);
    }

    private ProtoBuf.Panel.StudyNameControl makeProtobufStudyNameControlFromName(
                                                              String name)
    {
        ProtoBuf.Panel.StudyNameControl protoNameControl = 
            ProtoBuf.Panel.StudyNameControl.newBuilder()
            .setStudyName(name)
            .build();  

        return protoNameControl;
    }

    private byte [] makeBytesFromProtobufStudyNameControl(
                          ProtoBuf.Panel.StudyNameControl protoNameControl)
    {
        return protoNameControl.toByteArray();
    }


    public byte [] makeBytesFromStudyStatus(Study.Status status)
    {
        ProtoBuf.Panel.StatusControl protoStatusContol = 
                                    makeProtobufStatusControlFromStatus(status);
        return makeBytesFromProtobufStatusControl(protoStatusContol);
    }

    private ProtoBuf.Panel.StatusControl makeProtobufStatusControlFromStatus(
                                                      Study.Status status)
    {
        ProtoBuf.Study.Status protoStatus = statusToProtobufStatus(status);

        ProtoBuf.Panel.StatusControl protoStatusControl = 
            ProtoBuf.Panel.StatusControl.newBuilder()
            .setStatus(protoStatus)
            .build();  

        return protoStatusControl;
    }

    private byte [] makeBytesFromProtobufStatusControl(
                          ProtoBuf.Panel.StatusControl protoStatusControl)
    {
        return protoStatusControl.toByteArray();
    }

    public byte [] makeBytesFromStudyVisibility(Study.Visibility visibility)
    {
        ProtoBuf.Panel.VisibilityControl protoVisibilityContol = 
                        makeProtobufVisibilityControlFromVisibility(visibility);
        return makeBytesFromProtobufVisibilityControl(protoVisibilityContol);
    }

    private ProtoBuf.Panel.VisibilityControl 
        makeProtobufVisibilityControlFromVisibility(Study.Visibility visibility)
    {
        ProtoBuf.Study.Visibility protoVisibility = 
                                    visibilityToProtobufVisibility(visibility);

        ProtoBuf.Panel.VisibilityControl protoVisibilityControl = 
            ProtoBuf.Panel.VisibilityControl.newBuilder()
            .setVisibility(protoVisibility)
            .build();  

        return protoVisibilityControl;
    }

    private byte [] makeBytesFromProtobufVisibilityControl(
                      ProtoBuf.Panel.VisibilityControl protoVisibilityControl)
    {
        return protoVisibilityControl.toByteArray();
    }


    public Panel makePanelFromBytes(byte [] panelAsBytes)
    {
        ProtoBuf.Panel protoPanel = makeProtobufPanelFromBytes(panelAsBytes);

        if (protoPanel == null)
            return null;

        return makePanelFromProtobufPanel(protoPanel);

    }   //  end makePanelFromBytes


    private ProtoBuf.Panel makeProtobufPanelFromBytes(byte [] panelAsBytes)
    {
        // Parse the input bytes into a Protobuf panel object

        ProtoBuf.Panel protoPanel = null; 

        try
        {
            protoPanel = ProtoBuf.Panel.parseFrom(panelAsBytes);
        }
        catch (InvalidProtocolBufferException e)
        {
            exception = new ServiceException(
                                "parseFrom bytes to Protobuf panel failed", e);
            return null;
        }

        return protoPanel;

    }   //  end makeProtobufPanelFromBytes


    private Panel makePanelFromProtobufPanel(ProtoBuf.Panel protoPanel)
    {
        // Create a Yosokumo Panel from a Protobuf panel

        Panel panel = new Panel();

        panel.setNameControlLocation(
                    protoPanel.getStudyNameControl().getLocation());
        panel.setStatusControlLocation(
                    protoPanel.getStatusControl().getLocation());
        panel.setVisibilityControlLocation(
                    protoPanel.getVisibilityControl().getLocation());

        panel.setBlockCount(
                    protoPanel.getBlockCountControl().getBlockCount());
        panel.setCellCount(
                    protoPanel.getCellCountControl().getCellCount());
        panel.setProspectCount(
                    protoPanel.getProspectCountControl().getProspectCount());

        panel.setCreationTime(
            protoPanel.getCreationTimeControl().getCreationTime()      );
        panel.setLatestBlockTime(
            protoPanel.getLatestBlockTimeControl().getLatestBlockTime()   );
        panel.setLatestProspectTime(
            protoPanel.getLatestProspectTimeControl().getLatestProspectTime());

        return panel;

    }   //  end makePanelFromProtobufPanel

    public Roster makeRosterFromBytes(byte [] rosterAsBytes)
    {
        ProtoBuf.Roster protoRoster = makeProtobufRosterFromBytes(rosterAsBytes);

        if (protoRoster == null)
            return null;

        return makeRosterFromProtobufRoster(protoRoster);

    }   //  end makeRosterFromBytes


    private ProtoBuf.Roster makeProtobufRosterFromBytes(byte [] rosterAsBytes)
    {
        // Parse the input bytes into a Protobuf roster object

        ProtoBuf.Roster protoRoster = null; 

        try
        {
            protoRoster = ProtoBuf.Roster.parseFrom(rosterAsBytes);
        }
        catch (InvalidProtocolBufferException e)
        {
            exception = new ServiceException(
                                "parseFrom bytes to Protobuf roster failed", e);
            return null;
        }

        return protoRoster;

    }   //  end makeProtobufRosterFromBytes


    private Roster makeRosterFromProtobufRoster(ProtoBuf.Roster protoRoster)
    {
        // Create a Yosokumo Roster from a Protobuf roster

        Roster roster = new Roster();

        // Note that if any of the following fields are missing in the 
        // Protobuf roster, they will be empty strings

        roster.setStudyIdentifier(protoRoster.getStudyIdentifier());
        roster.setStudyName      (protoRoster.getStudyName()     );
        roster.setRosterLocation (protoRoster.getLocation()      );

        for (ProtoBuf.Role protoRole : protoRoster.getRoleList())
        {
            Role role = roster.addRole(makeRoleFromProtobufRole(protoRole));
            if (role != null)
            {
                exception = new ServiceException("Yosokumo Roster contains" +
                                    " two roles with the same identifier");

                return null;
            }
        }

        return roster;

    }   //  end makeRosterFromProtobufRoster


    public Role makeRoleFromBytes(byte [] roleAsBytes)
    {
        ProtoBuf.Role protoRole = makeProtobufRoleFromBytes(roleAsBytes);

        if (protoRole == null)
            return null;

        return makeRoleFromProtobufRole(protoRole);

    }   //  end makeRoleFromBytes


    private ProtoBuf.Role makeProtobufRoleFromBytes(byte [] roleAsBytes)
    {
        // Parse the input bytes into a Protobuf role object

        ProtoBuf.Role protoRole = null; 

        try
        {
            protoRole = ProtoBuf.Role.parseFrom(roleAsBytes);
        }
        catch (InvalidProtocolBufferException e)
        {
            exception = new ServiceException(
                                "parseFrom bytes to Protobuf role failed", e);
            return null;
        }

        return protoRole;

    }   //  end makeProtobufRoleFromBytes


    private Role makeRoleFromProtobufRole(ProtoBuf.Role protoRole)
    {
        Role role = new Role(
            protoRole.getRoleholder().getUserIdentifier(),
            protoRole.getStudy().getStudyIdentifier());

        // Note that if any of the following fields are missing in the 
        // protobuf, they will be empty strings

        role.setRoleLocation(protoRole.getLocation());
        role.setUserName    (protoRole.getRoleholder().getUserName());
        role.setStudyName   (protoRole.getStudy().getStudyName());

        ProtoBuf.Role.Privileges p = protoRole.getPrivileges();

        if (p.getGetStudy())    role.addPrivilege(Role.Privilege.GET_STUDY);
        if (p.getDeleteStudy()) role.addPrivilege(Role.Privilege.DELETE_STUDY);
        if (p.getGetRoster())   role.addPrivilege(Role.Privilege.GET_ROSTER);
        if (p.getPostRoster())  role.addPrivilege(Role.Privilege.POST_ROSTER);
        if (p.getGetRole())     role.addPrivilege(Role.Privilege.GET_ROLE);
        if (p.getPutRole())     role.addPrivilege(Role.Privilege.PUT_ROLE);
        if (p.getDeleteRole())  role.addPrivilege(Role.Privilege.DELETE_ROLE);
        if (p.getGetPanel())    role.addPrivilege(Role.Privilege.GET_PANEL);
        if (p.getGetControl())  role.addPrivilege(Role.Privilege.GET_CONTROL);
        if (p.getPutControl())  role.addPrivilege(Role.Privilege.PUT_CONTROL);
        if (p.getPostTable())   role.addPrivilege(Role.Privilege.POST_TABLE);
        if (p.getGetModel())    role.addPrivilege(Role.Privilege.GET_MODEL);
        if (p.getPostModel())   role.addPrivilege(Role.Privilege.POST_MODEL);

        return role;

    }   //  end makeRoleFromProtobufRole


    public byte [] makeBytesFromRole(Role role)
    {
        ProtoBuf.Role protoRole = makeProtobufRoleFromRole(role);
        return makeBytesFromProtobufRole(protoRole);
    }

    private ProtoBuf.Role makeProtobufRoleFromRole(Role role)
    {
        ProtoBuf.Role.Roleholder roleholder = 
            ProtoBuf.Role.Roleholder.newBuilder()
            .setUserIdentifier(role.getUserIdentifier())
            .setUserName      (role.getUserName())
            .build();  

        ProtoBuf.Role.Study study = 
            ProtoBuf.Role.Study.newBuilder()
            .setStudyIdentifier(role.getStudyIdentifier())
            .setStudyName      (role.getStudyName())
            .build();  

        ProtoBuf.Role.Privileges privileges = 
            ProtoBuf.Role.Privileges.newBuilder()
            .setGetStudy   (role.getPrivilege(Role.Privilege.GET_STUDY))
            .setDeleteStudy(role.getPrivilege(Role.Privilege.DELETE_STUDY))
            .setGetRoster  (role.getPrivilege(Role.Privilege.GET_ROSTER))
            .setPostRoster (role.getPrivilege(Role.Privilege.POST_ROSTER))
            .setGetRole    (role.getPrivilege(Role.Privilege.GET_ROLE))
            .setPutRole    (role.getPrivilege(Role.Privilege.PUT_ROLE))
            .setDeleteRole (role.getPrivilege(Role.Privilege.DELETE_ROLE))
            .setGetPanel   (role.getPrivilege(Role.Privilege.GET_PANEL))
            .setGetControl (role.getPrivilege(Role.Privilege.GET_CONTROL))
            .setPutControl (role.getPrivilege(Role.Privilege.PUT_CONTROL))
            .setPostTable  (role.getPrivilege(Role.Privilege.POST_TABLE))
            .setGetModel   (role.getPrivilege(Role.Privilege.GET_MODEL))
            .setPostModel  (role.getPrivilege(Role.Privilege.POST_MODEL))
            .build();  

        ProtoBuf.Role protoRole = ProtoBuf.Role.newBuilder()
            .setRoleholder(roleholder)
            .setPrivileges(privileges)
            .setStudy(study)
	    .build();  

        return protoRole;
    }

    private byte [] makeBytesFromProtobufRole(ProtoBuf.Role protoRole)
    {
        return protoRole.toByteArray();
    }


    public Predictor makePredictorFromBytes(byte [] predictorAsBytes)
    {
        ProtoBuf.Predictor protoPredictor = 
                                makeProtobufPredictorFromBytes(predictorAsBytes);

        if (protoPredictor == null)
            return null;

        return makePredictorFromProtobufPredictor(protoPredictor);

    }   //  end makePredictorFromBytes


    private ProtoBuf.Predictor makeProtobufPredictorFromBytes(
        byte [] predictorAsBytes)
    {
        // Parse the input bytes into a Protobuf predictor object

        ProtoBuf.Predictor protoPredictor = null; 

        try
        {
            protoPredictor = ProtoBuf.Predictor.parseFrom(predictorAsBytes);
        }
        catch (InvalidProtocolBufferException e)
        {
            exception = new ServiceException(
                                "parseFrom bytes to Protobuf predictor failed", e);
            return null;
        }

        return protoPredictor;

    }   //  end makeProtobufPredictorFromBytes


    private Predictor makePredictorFromProtobufPredictor(
        ProtoBuf.Predictor protoPredictor)
    {
        Predictor predictor = new Predictor(protoPredictor.getName());

        predictor.setStatus(protoStatusToStatus(protoPredictor.getStatus()));
        predictor.setType  (protoTypeToType    (protoPredictor.getType())  );
        predictor.setLevel (protoLevelToLevel  (protoPredictor.getLevel()) );

        return predictor;

    }   //  end makePredictorFromProtobufPredictor


    public byte [] makeBytesFromPredictor(Predictor predictor)
    {
        ProtoBuf.Predictor protoPredictor = 
                                    makeProtobufPredictorFromPredictor(predictor);
        return makeBytesFromProtobufPredictor(protoPredictor);
    }

    private ProtoBuf.Predictor makeProtobufPredictorFromPredictor(
        Predictor predictor)
    {
        ProtoBuf.Predictor.Builder b = ProtoBuf.Predictor.newBuilder()
            .setName(predictor.getPredictorName());

        b.setStatus(statusToProtobufStatus(predictor.getStatus()))
         .setType  (typeToProtobufType    (predictor.getType())  )
         .setLevel (levelToProtobufLevel  (predictor.getLevel()) );

        ProtoBuf.Predictor protoPredictor = b.build();

        return protoPredictor;
    }

    private byte [] makeBytesFromProtobufPredictor(ProtoBuf.Predictor protoPredictor)
    {
        return protoPredictor.toByteArray();
    }

    // Enum conversions:  Predictor <-> ProtoBuf.Predictor

    private Predictor.Status protoStatusToStatus(
        ProtoBuf.Predictor.Status protoStatus)
    {
        switch (protoStatus)
        {
        case Active:    return Predictor.Status.ACTIVE;
        case Inactive:  return Predictor.Status.INACTIVE;
        }
        return null;
    }

    private ProtoBuf.Predictor.Status statusToProtobufStatus(
        Predictor.Status status)
    {
        switch (status)
        {
        case ACTIVE:    return ProtoBuf.Predictor.Status.Active;

        case INACTIVE:  return ProtoBuf.Predictor.Status.Inactive;
        }
        return null;
    }

    private Predictor.Type protoTypeToType(
        ProtoBuf.Predictor.Type protoType)
    {
        switch (protoType)
        {
        case Categorical:  return Predictor.Type.CATEGORICAL;
        case Continuous:   return Predictor.Type.CONTINUOUS;
        }
        return null;
    }

    private ProtoBuf.Predictor.Type typeToProtobufType(
        Predictor.Type type)
    {
        switch (type)
        {
        case CATEGORICAL:  return ProtoBuf.Predictor.Type.Categorical;
        case CONTINUOUS:   return ProtoBuf.Predictor.Type.Continuous;
        }
        return null;
    }

    private Predictor.Level protoLevelToLevel(
        ProtoBuf.Predictor.Level protoLevel)
    {
        switch (protoLevel)
        {
        case Nominal:   return Predictor.Level.NOMINAL; 
        case Ordinal:   return Predictor.Level.ORDINAL; 
        case Interval:  return Predictor.Level.INTERVAL;
        case Ratio:     return Predictor.Level.RATIO;   
        }
        return null;
    }


    private ProtoBuf.Predictor.Level levelToProtobufLevel(
        Predictor.Level level)
    {
        switch (level)
        {
        case NOMINAL:   return ProtoBuf.Predictor.Level.Nominal; 
        case ORDINAL:   return ProtoBuf.Predictor.Level.Ordinal; 
        case INTERVAL:  return ProtoBuf.Predictor.Level.Interval;
        case RATIO:     return ProtoBuf.Predictor.Level.Ratio;   
        }
        return null;
    }

    public Specimen makeSpecimenFromBytes(byte [] specimenAsBytes)
    {
        ProtoBuf.Specimen protoSpecimen = 
                                makeProtobufSpecimenFromBytes(specimenAsBytes);

        if (protoSpecimen == null)
            return null;

        return makeSpecimenFromProtobufSpecimen(protoSpecimen);

    }   //  end makeSpecimenFromBytes


    private ProtoBuf.Specimen makeProtobufSpecimenFromBytes(
        byte [] specimenAsBytes)
    {
        // Parse the input bytes into a Protobuf specimen object

        ProtoBuf.Specimen protoSpecimen = null; 

        try
        {
            protoSpecimen = ProtoBuf.Specimen.parseFrom(specimenAsBytes);
        }
        catch (InvalidProtocolBufferException e)
        {
            exception = new ServiceException(
                                "parseFrom bytes to Protobuf specimen failed", e);
            return null;
        }

        return protoSpecimen;

    }   //  end makeProtobufSpecimenFromBytes


    private Specimen makeSpecimenFromProtobufSpecimen(
        ProtoBuf.Specimen protoSpecimen)
    {
        List<Cell> list = Collections.emptyList();
        Specimen specimen = new Specimen(protoSpecimen.getKey(), list);

        specimen.setStatus(protoStatusToStatus(protoSpecimen.getStatus()));
        specimen.setWeight(protoSpecimen.getWeight());

        Value value;
        if (protoSpecimen.hasEmpty()  )
                    value = new EmptyValue();
        else if (protoSpecimen.hasNatural())
                    value = new NaturalValue(protoSpecimen.getNatural());
        else if (protoSpecimen.hasInteger())
                    value = new IntegerValue(protoSpecimen.getInteger());
        else if (protoSpecimen.hasReal()   )
                    value = new RealValue(protoSpecimen.getReal());
        else
        {
            exception = new ServiceException("Protobuf specimen has no value");
            return null;
        }

        specimen.setPredictand(value);

        for (ProtoBuf.Cell protoCell : protoSpecimen.getCellList())
            specimen.addCell(makeCellFromProtobufCell(protoCell));

        return specimen;

    }   //  end makeSpecimenFromProtobufSpecimen


    public byte [] makeBytesFromSpecimen(Specimen specimen)
    {
        ProtoBuf.Specimen protoSpecimen = 
                                    makeProtobufSpecimenFromSpecimen(specimen);
        return makeBytesFromProtobufSpecimen(protoSpecimen);
    }

    private ProtoBuf.Specimen makeProtobufSpecimenFromSpecimen(
        Specimen specimen)
    {
        ProtoBuf.Specimen.Builder b = ProtoBuf.Specimen.newBuilder()
            .setKey(specimen.getSpecimenKey());

        b.setStatus(statusToProtobufStatus(specimen.getStatus()))
         .setWeight(specimen.getWeight());

        Value v = specimen.getPredictand();

        switch (v.getType())
        {
        case EMPTY:    b.setEmpty(true);                            break;
        case NATURAL:  b.setNatural(((NaturalValue)v).getValue());  break;
        case INTEGER:  b.setInteger(((IntegerValue)v).getValue());  break;
        case REAL:     b.setReal   (((RealValue)   v).getValue());  break;
        default:
            b.setEmpty(true);
            exception = 
                new ServiceException("Yosokumo specimen predictand value " + 
                                                        "has unknown type");
        }

        for (Cell c : specimen.getCells())
            b.addCell(makeProtobufCellFromCell(c));

        ProtoBuf.Specimen protoSpecimen = b.build();

        return protoSpecimen;
    }

    private byte [] makeBytesFromProtobufSpecimen(ProtoBuf.Specimen protoSpecimen)
    {
        return protoSpecimen.toByteArray();
    }

    // Enum conversions:  Specimen <-> ProtoBuf.Specimen

    private Specimen.Status protoStatusToStatus(
        ProtoBuf.Specimen.Status protoStatus)
    {
        switch (protoStatus)
        {
        case Active:    return Specimen.Status.ACTIVE;
        case Inactive:  return Specimen.Status.INACTIVE;
        }
        return null;
    }

    private ProtoBuf.Specimen.Status statusToProtobufStatus(
        Specimen.Status status)
    {
        switch (status)
        {
        case ACTIVE:    return ProtoBuf.Specimen.Status.Active;
        case INACTIVE:  return ProtoBuf.Specimen.Status.Inactive;
        }
        return null;
    }

    public Cell makeCellFromBytes(byte [] cellAsBytes)
    {
        ProtoBuf.Cell protoCell = makeProtobufCellFromBytes(cellAsBytes);

        if (protoCell == null)
            return null;

        return makeCellFromProtobufCell(protoCell);

    }   //  end makeCellFromBytes


    private ProtoBuf.Cell makeProtobufCellFromBytes(byte [] cellAsBytes)
    {
        // Parse the input bytes into a Protobuf cell object

        ProtoBuf.Cell protoCell = null; 

        try
        {
            protoCell = ProtoBuf.Cell.parseFrom(cellAsBytes);
        }
        catch (InvalidProtocolBufferException e)
        {
            exception = new ServiceException(
                                "parseFrom bytes to Protobuf cell failed", e);
            return null;
        }

        return protoCell;

    }   //  end makeProtobufCellFromBytes


    private Cell makeCellFromProtobufCell(ProtoBuf.Cell protoCell)
    {
        long nameOrKey;
        if      (protoCell.hasKey() )  nameOrKey = protoCell.getKey();
        else if (protoCell.hasName())  nameOrKey = protoCell.getName();
        else
        {
            exception = new ServiceException("Protobuf cell has neither name " +
                                             "nor key");
            return null;
        }

        Value value;
        if (protoCell.hasEmpty()  )  
                    value = new EmptyValue();
        else if (protoCell.hasNatural())
                    value = new NaturalValue(protoCell.getNatural());
        else if (protoCell.hasInteger())
                    value = new IntegerValue(protoCell.getInteger());
        else if (protoCell.hasReal())
                    value = new RealValue(protoCell.getReal());
        else if (protoCell.hasSpecial())
                    value = new SpecialValue(protoCell.getSpecial());
        else
        {
            exception = new ServiceException("Protobuf cell has no value");
            return null;
        }

        return new Cell(nameOrKey, value);

    }   //  end makeCellFromProtobufCell


    public byte [] makeBytesFromCell(Cell cell)
    {
        ProtoBuf.Cell protoCell = makeProtobufCellFromCell(cell);
        return makeBytesFromProtobufCell(protoCell);
    }

    private ProtoBuf.Cell makeProtobufCellFromCell(Cell cell)
    {
        ProtoBuf.Cell.Builder b = ProtoBuf.Cell.newBuilder()
            .setName(cell.getName());

        Value v = cell.getValue();

        switch (v.getType())
        {
        case EMPTY:    b.setEmpty(true);                            break;
        case NATURAL:  b.setNatural(((NaturalValue)v).getValue());  break;
        case INTEGER:  b.setInteger(((IntegerValue)v).getValue());  break;
        case REAL:     b.setReal   (((RealValue)   v).getValue());  break;
        case SPECIAL:  b.setSpecial(((SpecialValue)v).getValue());  break;
        default:
            b.setEmpty(true);
            exception = 
                new ServiceException("Yosokumo cell value has unknown type");
        }

        ProtoBuf.Cell protoCell = b.build();

        return protoCell;

    }   //  end makeProtobufCellFromCell

    private byte [] makeBytesFromProtobufCell(ProtoBuf.Cell protoCell)
    {
        return protoCell.toByteArray();
    }


    public Block makeBlockFromBytes(byte [] blockAsBytes)
    {
        ProtoBuf.Block protoBlock = makeProtobufBlockFromBytes(blockAsBytes);

        if (protoBlock == null)
            return null;

        return makeBlockFromProtobufBlock(protoBlock);

    }   //  end makeBlockFromBytes


    private ProtoBuf.Block makeProtobufBlockFromBytes(byte [] blockAsBytes)
    {
        // Parse the input bytes into a Protobuf block object

        ProtoBuf.Block protoBlock = null; 

        try
        {
            protoBlock = ProtoBuf.Block.parseFrom(blockAsBytes);
        }
        catch (InvalidProtocolBufferException e)
        {
            exception = new ServiceException(
                                "parseFrom bytes to Protobuf block failed", e);
            return null;
        }

        return protoBlock;

    }   //  end makeProtobufBlockFromBytes


    private Block makeBlockFromProtobufBlock(ProtoBuf.Block protoBlock)
    {
        // Create a Yosokumo Block from a Protobuf block

        Block block = null;
        Block.Type blockType;
        String id = protoBlock.getStudyIdentifier();

        if (protoBlock.hasEmpty() && protoBlock.getEmpty())
            blockType = Block.Type.EMPTY;
        else if (protoBlock.getPredictorCount() > 0)
	    blockType = Block.Type.PREDICTOR;
        else
	    blockType = Block.Type.SPECIMEN;

        switch (blockType)
        {
        case EMPTY:
            block = new EmptyBlock(id);
            break;

        case PREDICTOR:
            block = new PredictorBlock(id);
            PredictorBlock pblock = (PredictorBlock)block;
            for (ProtoBuf.Predictor p : protoBlock.getPredictorList())
                pblock.addPredictor(makePredictorFromProtobufPredictor(p));
            break;

        case SPECIMEN:
            block = new SpecimenBlock(id);
            SpecimenBlock sblock = (SpecimenBlock)block;
            for (ProtoBuf.Specimen s : protoBlock.getSpecimenList())
                sblock.addSpecimen(makeSpecimenFromProtobufSpecimen(s));
            break;
        }

        return block;

    }   //  end makeBlockFromProtobufBlock


    public byte [] makeBytesFromBlock(Block block)
    {
        ProtoBuf.Block protoBlock = makeProtobufBlockFromBlock(block);
        return makeBytesFromProtobufBlock(protoBlock);
    }

    private ProtoBuf.Block makeProtobufBlockFromBlock(Block block)
    {
        ProtoBuf.Block.Builder b = ProtoBuf.Block.newBuilder()
            .setStudyIdentifier(block.getStudyIdentifier());

        switch (block.getType())
        {
        case EMPTY:
            b.setEmpty(true);
            break;

        case PREDICTOR:  
        {
            b.clearEmpty();
            PredictorBlock pblock = (PredictorBlock)block;
            for (Predictor p : pblock.getPredictorSequence())
                b.addPredictor(makeProtobufPredictorFromPredictor(p));
            break;
        }

        case SPECIMEN:
        {
            b.clearEmpty();
            SpecimenBlock sblock = (SpecimenBlock)block;
            for (Specimen s : sblock.getSpecimenSequence())
                b.addSpecimen(makeProtobufSpecimenFromSpecimen(s));
            break;
        }

        default:
            b.setEmpty(true);
            exception = 
                new ServiceException("Yosokumo block has unknown type");
        }

        ProtoBuf.Block protoBlock = b.build(); 

        return protoBlock;
    }

    private byte [] makeBytesFromProtobufBlock(ProtoBuf.Block protoBlock)
    {
        return protoBlock.toByteArray();
    }

    public Message makeMessageFromBytes(byte [] messageAsBytes)
    {
        ProtoBuf.Message protoMessage = makeProtobufMessageFromBytes(messageAsBytes);

        if (protoMessage == null)
            return null;

        return makeMessageFromProtobufMessage(protoMessage);

    }   //  end makeMessageFromBytes


    private ProtoBuf.Message makeProtobufMessageFromBytes(byte [] messageAsBytes)
    {
        // Parse the input bytes into a Protobuf message object

        ProtoBuf.Message protoMessage = null; 

        try
        {
            protoMessage = ProtoBuf.Message.parseFrom(messageAsBytes);
        }
        catch (InvalidProtocolBufferException e)
        {
            exception = new ServiceException(
                                "parseFrom bytes to Protobuf message failed", e);
            return null;
        }

        return protoMessage;

    }   //  end makeProtobufMessageFromBytes


    private Message makeMessageFromProtobufMessage(ProtoBuf.Message protoMessage)
    {
        Message m = new Message(
            protoTypeToType(protoMessage.getType()),
            protoMessage.getText());

        return (m.getText().trim().isEmpty() ? null : m);
    }

    private Message.Type protoTypeToType(
        ProtoBuf.Message.Type protoType)
    {
        switch (protoType)
        {
        case Information:  return Message.Type.INFORMATION;
        case Error:        return Message.Type.ERROR;
        }
        return null;
    }


}   //  end class YosokumoProtobuf 

// end YosokumoProtobuf.java
