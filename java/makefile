# top-level makefile for building Java version of yosokumo

include makefile.inc


# Create Java code, compile Java code, and make a jar file for yosokumo
.PHONY: all
all :  protobuf compile jar

# Create Java source files from a Google Protocol Buffer description, and 
# compile the created files
.PHONY: protobuf
protobuf :
	@cd protobuf; $(MAKE) $(MAKEFLAGS)

# compile Java code not created from protocol buffer descriptions
.PHONY: compile
compile : protobuf
	@rm -f $(JAR_DIR)/Yosokumo.jar
	@cd src; $(MAKE) $(MAKEFLAGS)

#Create a jar file for yosokumo (includes classes from generated Java)
.PHONY: jar
jar: compile
	@jar cf $(JAR_DIR)/Yosokumo.jar $(SRC_CLASS_ONLY)/*.class \
                                    $(PROTO_CLASS_ONLY)/*.class

# Compile all the JUnit test classes for yosokumo as well as other tests
.PHONY: tests
tests :
	@cd test-files; $(MAKE) $(MAKEFLAGS)

#Create public and private javadoc for yosokumo
.PHONY: javadoc
javadoc:
	@javadoc -classpath .:$(GOOGLE_PROTOBUF_JAR):$(HTTP_PATH) \
             -d $(JAVADOC_DIR) src/*.java
	@javadoc -classpath .:$(GOOGLE_PROTOBUF_JAR):$(HTTP_PATH) \
             -private -d $(JAVADOC_PRIVATE_DIR) src/*.java

# Get rid of all class files in CLASS_DIR
.PHONY: clean
clean :
	@rm -f $(CLASS_DIR)/*.class

# Get rid of all class files and other files
.PHONY: real-clean
real-clean :
	@rm -f $(CLASS_DIR)/*.class
	@rm -f $(JAR_DIR)/Yosokumo.jar
	@rm -rf $(JAVADOC_DIR)
	@mkdir $(JAVADOC_DIR)
	@rm -rf $(JAVADOC_PRIVATE_DIR)
	@mkdir $(JAVADOC_PRIVATE_DIR)
	@cd test-files;  $(MAKE) clean
	@cd protobuf;    $(MAKE) clean

# end makefile

