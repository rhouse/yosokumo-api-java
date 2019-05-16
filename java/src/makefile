# begin makefile to compile yosokumo Java code

include ../makefile.inc

.PHONY: all
all : compile

.PHONY: compile
compile :                               \
    $(CLASS_DIR)/Base64.class           \
    $(CLASS_DIR)/Block.class            \
    $(CLASS_DIR)/Catalog.class          \
    $(CLASS_DIR)/Cell.class             \
    $(CLASS_DIR)/Credentials.class      \
    $(CLASS_DIR)/DigestRequest.class    \
    $(CLASS_DIR)/EmptyBlock.class       \
    $(CLASS_DIR)/EmptyValue.class       \
    $(CLASS_DIR)/IntegerValue.class     \
    $(CLASS_DIR)/Message.class          \
    $(CLASS_DIR)/NaturalValue.class     \
    $(CLASS_DIR)/Panel.class            \
    $(CLASS_DIR)/Predictor.class        \
    $(CLASS_DIR)/PredictorBlock.class   \
    $(CLASS_DIR)/RealValue.class        \
    $(CLASS_DIR)/Role.class             \
    $(CLASS_DIR)/Roster.class           \
    $(CLASS_DIR)/Service.class          \
    $(CLASS_DIR)/ServiceException.class \
    $(CLASS_DIR)/SpecialValue.class     \
    $(CLASS_DIR)/Specimen.class         \
    $(CLASS_DIR)/SpecimenBlock.class    \
    $(CLASS_DIR)/Study.class            \
    $(CLASS_DIR)/Value.class            \
    $(CLASS_DIR)/YosokumoDIF.class      \
    $(CLASS_DIR)/YosokumoProtobuf.class \
    $(CLASS_DIR)/YosokumoRequest.class

$(CLASS_DIR)/Base64.class : Base64.java
	@rm -f $(CLASS_DIR)/Base64.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. Base64.java 

$(CLASS_DIR)/Block.class : Block.java 
	@rm -f Block.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. Block.java 

$(CLASS_DIR)/Catalog.class : Catalog.java \
                              $(CLASS_DIR)/Study.class            
	@rm -f $(CLASS_DIR)/Catalog.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. Catalog.java 

$(CLASS_DIR)/Cell.class : Cell.java \
                              $(CLASS_DIR)/Value.class
	@rm -f $(CLASS_DIR)/Cell.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. Cell.java 

$(CLASS_DIR)/Credentials.class : Credentials.java                 \
                              $(CLASS_DIR)/ServiceException.class
	@rm -f $(CLASS_DIR)/Credentials.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. Credentials.java 

$(CLASS_DIR)/DigestRequest.class : DigestRequest.java             \
                              $(CLASS_DIR)/Base64.class           \
                              $(CLASS_DIR)/ServiceException.class
	@rm -f $(CLASS_DIR)/DigestRequest.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. DigestRequest.java 

$(CLASS_DIR)/EmptyBlock.class : EmptyBlock.java        \
                              $(CLASS_DIR)/Block.class
	@rm -f $(CLASS_DIR)/EmptyBlock.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. EmptyBlock.java 

$(CLASS_DIR)/EmptyValue.class : EmptyValue.java        \
                              $(CLASS_DIR)/Value.class
	@rm -f $(CLASS_DIR)/EmptyValue.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. EmptyValue.java 

$(CLASS_DIR)/IntegerValue.class : IntegerValue.java    \
                              $(CLASS_DIR)/Value.class
	@rm -f $(CLASS_DIR)/IntegerValue.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. IntegerValue.java 

$(CLASS_DIR)/Message.class : Message.java
	@rm -f $(CLASS_DIR)/Message.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. Message.java 

$(CLASS_DIR)/NaturalValue.class : NaturalValue.java \
                              $(CLASS_DIR)/Value.class
	@rm -f $(CLASS_DIR)/NaturalValue.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. NaturalValue.java 

$(CLASS_DIR)/Panel.class : Panel.java
	@rm -f $(CLASS_DIR)/Panel.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. Panel.java 

$(CLASS_DIR)/Predictor.class : Predictor.java
	@rm -f $(CLASS_DIR)/Predictor.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. Predictor.java 

$(CLASS_DIR)/PredictorBlock.class : PredictorBlock.java    \
                              $(CLASS_DIR)/Block.class     \
                              $(CLASS_DIR)/Predictor.class
	@rm -f $(CLASS_DIR)/PredictorBlock.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. PredictorBlock.java 

$(CLASS_DIR)/RealValue.class : RealValue.java          \
                              $(CLASS_DIR)/Value.class
	@rm -f $(CLASS_DIR)/RealValue.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. RealValue.java 

$(CLASS_DIR)/Role.class : Role.java
	@rm -f $(CLASS_DIR)/Role.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. Role.java 

$(CLASS_DIR)/Roster.class : Roster.java                           \
                              $(CLASS_DIR)/Role.class
	@rm -f $(CLASS_DIR)/Roster.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. Roster.java 
$(CLASS_DIR)/Service.class : Service.java                          \
                              $(CLASS_DIR)/Block.class             \
                              $(CLASS_DIR)/Catalog.class           \
                              $(CLASS_DIR)/Credentials.class       \
                              $(CLASS_DIR)/Message.class           \
                              $(CLASS_DIR)/Panel.class             \
                              $(CLASS_DIR)/PredictorBlock.class    \
                              $(CLASS_DIR)/Role.class              \
                              $(CLASS_DIR)/Roster.class            \
                              $(CLASS_DIR)/ServiceException.class  \
                              $(CLASS_DIR)/Specimen.class          \
                              $(CLASS_DIR)/SpecimenBlock.class     \
                              $(CLASS_DIR)/Study.class             \
                              $(CLASS_DIR)/Value.class             \
                              $(CLASS_DIR)/YosokumoDIF.class       \
                              $(CLASS_DIR)/YosokumoProtobuf.class  \
                              $(CLASS_DIR)/YosokumoRequest.class            
	@rm -f $(CLASS_DIR)/Service.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. Service.java 

$(CLASS_DIR)/ServiceException.class : ServiceException.java
	@rm -f $(CLASS_DIR)/ServiceException.class
	javac -classpath .. -Xlint:all -Xlint:-path -Xlint:-serial -d .. \
        ServiceException.java 

$(CLASS_DIR)/SpecialValue.class : SpecialValue.java    \
                              $(CLASS_DIR)/Value.class            
	@rm -f $(CLASS_DIR)/SpecialValue.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. SpecialValue.java 

$(CLASS_DIR)/Specimen.class : Specimen.java                   \
                              $(CLASS_DIR)/Cell.class         \
                              $(CLASS_DIR)/EmptyValue.class   \
                              $(CLASS_DIR)/Value.class        
	@rm -f $(CLASS_DIR)/Specimen.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. Specimen.java 

$(CLASS_DIR)/SpecimenBlock.class : SpecimenBlock.java     \
                              $(CLASS_DIR)/Block.class    \
                              $(CLASS_DIR)/Specimen.class 
	@rm -f $(CLASS_DIR)/SpecimenBlock.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. SpecimenBlock.java 
$(CLASS_DIR)/Study.class : Study.java
	@rm -f $(CLASS_DIR)/Study.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. Study.java 

$(CLASS_DIR)/Value.class : Value.java
	@rm -f $(CLASS_DIR)/Value.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. Value.java 

$(CLASS_DIR)/YosokumoDIF.class : YosokumoDIF.java                  \
                              $(CLASS_DIR)/Block.class             \
                              $(CLASS_DIR)/Catalog.class           \
                              $(CLASS_DIR)/Cell.class              \
                              $(CLASS_DIR)/Message.class           \
                              $(CLASS_DIR)/Panel.class             \
                              $(CLASS_DIR)/Predictor.class         \
                              $(CLASS_DIR)/Role.class              \
                              $(CLASS_DIR)/Roster.class            \
                              $(CLASS_DIR)/ServiceException.class  \
                              $(CLASS_DIR)/Specimen.class          \
                              $(CLASS_DIR)/Study.class             
	@rm -f $(CLASS_DIR)/YosokumoDIF.class
	javac -classpath .. -Xlint:all -Xlint:-path -d .. YosokumoDIF.java 

$(CLASS_DIR)/YosokumoProtobuf.class : YosokumoProtobuf.java        \
                              $(CLASS_DIR)/Block.class             \
                              $(CLASS_DIR)/Catalog.class           \
                              $(CLASS_DIR)/Cell.class              \
                              $(CLASS_DIR)/EmptyBlock.class        \
                              $(CLASS_DIR)/EmptyValue.class        \
                              $(CLASS_DIR)/IntegerValue.class      \
                              $(CLASS_DIR)/Message.class           \
                              $(CLASS_DIR)/NaturalValue.class      \
                              $(CLASS_DIR)/Panel.class             \
                              $(CLASS_DIR)/Predictor.class         \
                              $(CLASS_DIR)/PredictorBlock.class    \
                              $(CLASS_DIR)/RealValue.class         \
                              $(CLASS_DIR)/Role.class              \
                              $(CLASS_DIR)/Roster.class            \
                              $(CLASS_DIR)/ServiceException.class  \
                              $(CLASS_DIR)/SpecialValue.class      \
                              $(CLASS_DIR)/Specimen.class          \
                              $(CLASS_DIR)/SpecimenBlock.class     \
                              $(CLASS_DIR)/Study.class             \
                              $(CLASS_DIR)/Value.class             \
                              $(CLASS_DIR)/YosokumoDIF.class       
	@rm -f $(CLASS_DIR)/YosokumoProtobuf.class
	javac -classpath ..:$(GOOGLE_PROTOBUF_JAR) -Xlint:all -Xlint:-path -d .. \
        YosokumoProtobuf.java 

$(CLASS_DIR)/YosokumoRequest.class : YosokumoRequest.java          \
                              $(CLASS_DIR)/Credentials.class       \
                              $(CLASS_DIR)/DigestRequest.class     \
                              $(CLASS_DIR)/ServiceException.class             
	@rm -f $(CLASS_DIR)/YosokumoRequest.class
	javac -classpath ..:$(HTTP_PATH) -Xlint:all -Xlint:-path -d .. \
        YosokumoRequest.java 

# clean gets rid of all class files in CLASS_DIR

.PHONY: clean
clean :
	@rm -f $(CLASS_DIR)/*.class

# end makefile to compile yosokumo Java code
