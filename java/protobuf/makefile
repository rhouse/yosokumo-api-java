# begin makefile to create Java source code from a Google Protocol Buffer 
# description and compile the Java 

include ../makefile.inc

.PHONY: all
all : compile

.PHONY: compile
compile : $(PROTO_CLASS_DIR)/ProtoBuf.class 

$(PROTO_CLASS_DIR)/ProtoBuf.class : $(PROTO_SERVICE_DIR)/ProtoBuf.java
	@rm -f $(PROTO_CLASS_DIR)/*.class
	@javac -classpath $(GOOGLE_PROTOBUF_JAR) -d $(YOSOKUMO_DIR)  \
           $(PROTO_SERVICE_DIR)/ProtoBuf.java

$(PROTO_SERVICE_DIR)/ProtoBuf.java : $(PROTO_SRC_DIR)/yosokumo.proto
	@protoc -I=$(PROTO_SRC_DIR) \
           --java_out=$(YOSOKUMO_DIR)  \
           $(PROTO_SRC_DIR)/yosokumo.proto

# clean gets rid of all class files in PROTO_CLASS_DIR

.PHONY: clean
clean :
	@rm -f $(PROTO_CLASS_DIR)/*.class
	@rm -f $(PROTO_SERVICE_DIR)/ProtoBuf.java

# end makefile to create and compile yosokumo Java code from GPB
