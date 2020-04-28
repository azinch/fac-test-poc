# fac-test-poc

TEST DATA MANAGEMENT.

0. System Self-testing. 
   Testing NAPI (FAC) should be based on NAPI itself, at least in terms of preparing COMPLEX data/entity (BAN, Subscriber, etc.). 
   It also allows to significantly reduce maintanance of a new API for testing.   

1. PRE-BUILD phase of CI pipeline (prepare Initial data).

   Existing NAPI jobs should prepare data in RM, before UnitTests start (e.g. NAPI BOX Create , etc.).
   Jobs can be run periodically (not each build) .
   
2. BUILD-PHASE of the pipeline (prepare UT data).

   Each UnitTest running should create its own test data to avoid data exhaustion after multiple builds, manual data preparation, 
   Race conditions between different UnitTests for same data (in case of parallel testing), etc.
   
3. POST-BUILD phase (clean all data, return to initial database).
  
   To prevent fast grow of the database (each build can generate lots of new test data) and avoid UT performance degradation,
   a new shell script should be run periodically (e.g. replace the current database with initial snapshot).
     
4. To simplify and speed up UnitTests development in terms of data preparation, UT should use API specifically designed 
   to create data in convinient manner (set of methods in the new NapiDataFactory class).               

5. NapiDataFactory methods should use existing NAPI to create COMPLEX data/entity (BAN, Subscriber, etc.). 

6. NapiDataFactory core class uses existing NapiFactory  (java/com support) .

7. NapiDataFactory is thread-safe for PARALLEL testing using @ResourceLock by JUnit .

8. NapiDataFactory can be configured to work with different ensemble/napi jobs 
        to facilitate test data preparation at different stages (box create, pre-activate, sale, register) .

9. Dual activities share same test data inside of Test classes (suspend/restore ctn, cancel/resume , etc.) .

10. Unit Tests can support Mocking for external objects .

11. Integration Tests (narrowed, end-to-end, long-running scenarios, using only fac api) will be run in a special build profile -
      NOT to impact Unit tests performance . 

TEST FLOW .

1.	Dev makes code changes , covers with UT (use **UTest templates , NapiDataFactory methods – samples in the fac-test-poc )  .

2.	Dev check-in the code and Git notifies the integration server (Jenkins) to start the UT build .

3.	Jenkins running the build pipeline script downloads the code changes into its workspace and runs the build using maven POM.
(working on the script , will be added to the proj later)

4.	mvn runs Jacoco plugin to report/notify on the code test coverage , depending on threshold it can fail the build .
(code quality report – SonarQube will be added to the proj later ).

5.	UT run in PARALLEL , each test class creating its own data (based on some initial preparations by existing ensemble jobs ) .

6.	Integration Tests (narrowed, end-to-end, long-running scenarios, using only fac api) will be prepared by TESTERs and run in a special build profile -
    to avoid impact Unit tests performance . 

IMPLEMENTATION .

When reviewing pay attention to :

-	pom.xml   (make file)
-	NapiDataFactory.java   (core class to facilitate test data generation)
-	ExternalSystemApiClientUTest.java   (template for mocking External obj , java Mockito )
-	SuspendRestoreCTNUTest.java   (dual activity u-test using NapiDataFactory, JUnit 5 : parallel support, test ordering, test conditions, etc .)
-	ChangeCtnUTest.java
-  junit-platform.properties   (common settings for parallel testing) 

