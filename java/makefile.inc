# makefile.inc for yosokumo

# The yosokumo Java class files are in 
#
#       $(YOSOKUMO_DIR)/com/yosokumo/core

YOSOKUMO_DIR   = /home/roger/yosokumo/java

SRC_DIR        = $(YOSOKUMO_DIR)/src
SRC_CLASS_ONLY = com/yosokumo/core
CLASS_DIR      = $(YOSOKUMO_DIR)/$(SRC_CLASS_ONLY)
TEST_DIR       = $(CLASS_DIR)/test

JAR_DIR             = $(YOSOKUMO_DIR)/jar
JAVADOC_DIR         = $(YOSOKUMO_DIR)/javadoc
JAVADOC_PRIVATE_DIR = $(YOSOKUMO_DIR)/javadoc-private

PROTO_SRC_DIR       = $(YOSOKUMO_DIR)/protobuf
PROTO_SERVICE_DIR   = $(YOSOKUMO_DIR)/com/yosokumo/core/protobuf
PROTO_CLASS_ONLY    = com/yosokumo/core/protobuf
PROTO_CLASS_DIR     = $(YOSOKUMO_DIR)/$(PROTO_CLASS_ONLY)

GOOGLE_PROTOBUF_JAR = /usr/share/java/protobuf.jar

HTTP_DIR = /home/roger/OpenSourceCode/HttpClient/httpcomponents-client-4.0.1/lib
HTTP_PATH := $(HTTP_DIR)/httpcore.jar
HTTP_PATH := $(HTTP_PATH):$(HTTP_DIR)/httpclient.jar
#HTTP_PATH := $(HTTP_PATH):$(HTTP_DIR)/commons-codec.jar
HTTP_PATH := $(HTTP_PATH):$(HTTP_DIR)/commons-logging.jar

JUNIT_JAR = /usr/share/java/junit4.jar

# end makefile.inc
