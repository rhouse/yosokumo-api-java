# begin makefile to compile yosokumo Java code for unit tests and other tests

include ../makefile.inc

.PHONY: tests
tests : compile

.PHONY: compile
compile :                                        \
         $(CLASS_DIR)/Base64Test.class           \
         $(CLASS_DIR)/Base64XXX.class            \
         $(CLASS_DIR)/BlockTest.class            \
         $(CLASS_DIR)/CatalogTest.class          \
         $(CLASS_DIR)/PredictorTest.class        \
         $(CLASS_DIR)/RoleTest.class             \
         $(CLASS_DIR)/RosterTest.class           \
         $(CLASS_DIR)/SpecimenTest.class         \
         $(CLASS_DIR)/StudyTest.class            \
         $(TEST_DIR)/YosokumoSmokeTest.class     \
         $(TEST_DIR)/SuburbProfile.class         \
         $(TEST_DIR)/YosokumoPredictionTest.class

$(CLASS_DIR)/Base64Test.class : Base64Test.java \
                            $(CLASS_DIR)/Base64XXX.class
	@rm -f $(CLASS_DIR)/Base64Test.class
	javac -classpath ..:$(JUNIT_JAR) -d .. Base64Test.java 

$(CLASS_DIR)/Base64XXX.class : Base64XXX.java
	@rm -f $(CLASS_DIR)/Base64XXX.class
	javac -classpath ..:$(JUNIT_JAR) -d .. Base64XXX.java 

$(CLASS_DIR)/BlockTest.class : BlockTest.java
	@rm -f $(CLASS_DIR)/BlockTest.class
	javac -classpath ..:$(JUNIT_JAR) -d .. BlockTest.java 

$(CLASS_DIR)/CatalogTest.class : CatalogTest.java
	@rm -f $(CLASS_DIR)/CatalogTest.class
	javac -classpath ..:$(JUNIT_JAR) -d .. CatalogTest.java 

$(CLASS_DIR)/PredictorTest.class : PredictorTest.java
	@rm -f $(CLASS_DIR)/PredictorTest.class
	javac -classpath ..:$(JUNIT_JAR) -d .. PredictorTest.java 

$(CLASS_DIR)/RoleTest.class : RoleTest.java
	@rm -f $(CLASS_DIR)/RoleTest.class
	javac -classpath ..:$(JUNIT_JAR) -d .. RoleTest.java 

$(CLASS_DIR)/RosterTest.class : RosterTest.java
	@rm -f $(CLASS_DIR)/RosterTest.class
	javac -classpath ..:$(JUNIT_JAR) -d .. RosterTest.java 

$(CLASS_DIR)/SpecimenTest.class : SpecimenTest.java
	@rm -f $(CLASS_DIR)/SpecimenTest.class
	javac -classpath ..:$(JUNIT_JAR) -d .. SpecimenTest.java 

$(CLASS_DIR)/StudyTest.class : StudyTest.java
	@rm -f $(CLASS_DIR)/StudyTest.class
	javac -classpath ..:$(JUNIT_JAR) -d .. StudyTest.java 

$(TEST_DIR)/YosokumoSmokeTest.class : YosokumoSmokeTest.java
	@rm -f $(TEST_DIR)/YosokumoSmokeTest.class
	javac -classpath .. -d .. YosokumoSmokeTest.java 

$(TEST_DIR)/SuburbProfile.class : SuburbProfile.java
	@rm -f $(TEST_DIR)/SuburbProfile.class
	javac -classpath .. -d .. SuburbProfile.java 

$(TEST_DIR)/YosokumoPredictionTest.class : YosokumoPredictionTest.java \
                              $(TEST_DIR)/SuburbProfile.class
	@rm -f $(TEST_DIR)/YosokumoPredictionTest.class
	javac -classpath .. -d .. YosokumoPredictionTest.java 

# clean gets rid of all test class files in CLASS_DIR and TEST_DIR

.PHONY: clean
clean :
	@rm -f $(CLASS_DIR)/Base64Test.class
	@rm -f $(CLASS_DIR)/Base64XXX.class
	@rm -f $(CLASS_DIR)/Base64XXX$1.class
	@rm -f $(CLASS_DIR)/Base64XXX$InputStream.class
	@rm -f $(CLASS_DIR)/Base64XXX$OutputStream.class
	@rm -f $(CLASS_DIR)/BlockTest.class
	@rm -f $(CLASS_DIR)/CatalogTest.class
	@rm -f $(CLASS_DIR)/PredictorTest.class
	@rm -f $(CLASS_DIR)/RoleTest.class
	@rm -f $(CLASS_DIR)/RosterTest.class
	@rm -f $(CLASS_DIR)/SpecimenTest.class
	@rm -f $(CLASS_DIR)/StudyTest.class
	@rm -f $(TEST_DIR)/YosokumoSmokeTest.class
	@rm -f $(TEST_DIR)/SuburbProfile.class
	@rm -f $(TEST_DIR)/YosokumoPredictionTest.class
	@rm -f $(TEST_DIR)/YosokumoStressTest.class
	@rm -f $(CLASS_DIR)/YAndroidTest.class

# end makefile to compile yosokumo Java code for unit tests and other tests
