yosokumo-api-java
=================

         Java API for accessing the Yosokumo data mining web service

ŷosokumo was a data mining web service which allowed users to make
predictions about new individuals based on data supplied about previously
seen examples of similar individuals.  It offered quite sophisticated
prediction modeling of various kinds.  Click 
<a href="http://rogerfhouse.com/programming/yosokumo">here</a> to see a 
specification of the web service, as well as Javadoc for the programmer
writing Java code to access the service.


             How to Install and Use the Yosokumo Java Client

IMPORTANT:  The ŷosokumo project was abandoned, and hence any links to
yosokumo.com are broken.  Also, it is not certain that all the following
instructions are uptodate.

Prerequisites

See the section at the end of this doc about external components needed to 
build, test, and run the Yosokumo Java Client.  Details of setting up 
CLASSPATH are shown.


Run it

Provided that the external components have been installed and the CLASSPATH 
is set up, especially that the CLASSPATH points to these two jar files

    /home/roger/yosokumo/java/jar
        Yosokumo.jar
        YosokumoProtobuf.jar

a test can be run out of the box:

    cd /home/roger/yosokumo/java
    ./u

The u script contains one line:

    java -enableassertions com.yosokumo.core.test.YosokumoSmokeTest user_ids roger.key

This runs a smoke test which connects to hermes.yosokumo.ws and executes all 
methods of the service.  Note that the smoke test takes two parameters, the 
name of a file containing user ids and the name of a file containing a key.
The first file must contain exactly two user ids, and the second file must 
contain the key of the first user.


Look at the documentation

Now use a browser to open the file index.html in 

    /home/roger/yosokumo/java/javadoc

The public Javadoc for the client is shown.  Note that only classes, enums, 
methods, etc., that are available to the programmer are described.

Use a browser to open the file index.html in 

    /home/roger/yosokumo/java/javadoc-private

The private Javadoc for the client is shown.  This will be useful for 
developers because it covers everything, private as well as public.


Build the whole thing

First:  Edit /home/roger/yosokumo/java/makefile.inc, changing this line 

    YOSOKUMO_DIR = /home/roger/yosokumo/java

to specify the directory where the tar file is extracted.

Now create the protobuf class files:

    cd /home/roger/yosokumo/java
    make protobuf

If this is successful, a large number of class files will be created in
 
    /home/roger/yosokumo/java/com/yosokumo/core/protobuf

Now build the client itself:

    cd /home/roger/yosokumo/java
    make

This will create close to 50 class files in 
 
    /home/roger/yosokumo/java/com/yosokumo/core

Now create jar files from the two sets of class files created just above

    cd /home/roger/yosokumo/java
    make jar

This will create two jar files in
 
    /home/roger/yosokumo/java/jar

Finally, compile JUnit tests:

    cd /home/roger/yosokumo/java
    make tests

This will add 13 more class files to 
 
    /home/roger/yosokumo/java/com/yosokumo/core

and one class file (YosokumoSmokeTest.class) to 
 
    /home/roger/yosokumo/java/com/yosokumo/test


Do some testing

Run JUnit tests:

    cd /home/roger/yosokumo/java
    ./t

The t script runs a group of tests and displays this if everything is okay:

    JUnit version 4.8.1
    .............................................................
    Time: 0.838

    OK (61 tests)

Now run the u script again to make sure the newly-built smoke test is okay:

    cd /home/roger/yosokumo/java
    ./u


Other make file options available

To create Javadoc:

    make javadoc
    make javadoc-private

To clean up the main class files:

    make clean

To clean up more or less everything:

    make real-clean


External components needed by the Yosokumo web service client

    To run the client these are needed: 
        Java JVM
        Google protocol buffer jar
        HttpCore, HttpClient, and related jars

    To build and test the client these are needed: 
        Java compiler
        Google protocol buffer compiler
        Javadoc
        JUnit

Following is a log of sorts showing details of the external components, and, 
in particular, what needs to be on the CLASSPATH.


2011 May 06

Java

Using the Ubuntu Software Center:  Installed the Sun Java JDK 6.24

    Ubuntu Software Center says:  6.24-1build0.10.04.1 (sun-java6-jdk)

    javac --version says:         javac 1.6.0_24

    java -version says:           java version "1.6.0_20" and more:
        OpenJDK Runtime Environment (IcedTea6 1.9.7) 
            (6b20-1.9.7-0ubuntu1~10.04.1)
        OpenJDK 64-Bit Server VM (build 19.0-b09, mixed mode)


2011 May 10

Using Synaptic:  Installed JUnit 4

    /usr/share/java/junit4.jar is a link pointing to 
    /usr/share/java/junit4-4.8.1.jar

Also installed junit4-doc, but can't find it anywhere on my system.  Use

    http://kentbeck.github.com/junit/javadoc/latest/

to get to the online Javadoc for JUnit.

Add to CLASSPATH to access JUnit:

    export CLASSPATH=.:/usr/share/java/junit4.jar:$CLASSPATH


2011 May 12

At some point in the past, downloaded HttpCore and HttpClient and set up the 
CLASSPATH as follows:

    export CLASSPATH=$CLASSPATH:/home/roger/OpenSourceCode/HttpClient/httpcomponents-client-4.0.1/lib/httpcore.jar
    export CLASSPATH=$CLASSPATH:/home/roger/OpenSourceCode/HttpClient/httpcomponents-client-4.0.1/lib/httpclient.jar
    export CLASSPATH=$CLASSPATH:/home/roger/OpenSourceCode/HttpClient/httpcomponents-client-4.0.1/lib/commons-codec.jar
    export CLASSPATH=$CLASSPATH:/home/roger/OpenSourceCode/HttpClient/httpcomponents-client-4.0.1/lib/commons-logging.jar

Vague memory:  Read in the past that it is important to use version 4.x rather 
than 3.y, which is what Ubuntu wants to install.


2011 May 13

Used Ubuntu Software Center to install Java bindings for Google Protocol 
Buffers:  Version: 2.2.0a-0.1ubuntu1 (libprotobuf-java).  This created the 
following in /usr/share/java

    protobuf-2.2.0.jar
    protobuf.jar (link to the above)

Added to CLASSPATH:

    export CLASSPATH=$CLASSPATH:/usr/share/java/protobuf.jar

Note that the original install of Ubuntu 10.04 installed the protocol 
compiler:

    /usr/bin/protoc

When this is run with --version, the result is

    libprotoc 2.2..0

Need these in order to use the Yosokumo jar files:

    export CLASSPATH=$CLASSPATH:/home/roger/yosokumo/java/jar/YosokumoProtobuf.jar
    export CLASSPATH=$CLASSPATH:/home/roger/yosokumo/java/jar/Yosokumo.jar

<end>
