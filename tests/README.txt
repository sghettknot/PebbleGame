Instructions to run the test suite:

Step 1) Open the command prompt and navigate to this location.

Step 2) Recompile the java files using this command:
javac -cp junit-4.12.jar;hamcrest-core-1.3.jar pebble/*.java

NOTE: You will recieve a warning upon compilation, this behaviour is expected as we are utilising reflection.

Step 3) Now run the test suite with the command:
java -cp junit-4.12.jar;hamcrest-core-1.3.jar;. org.junit.runner.JUnitCore pebble.PebbleGameTestSuite

NOTE: The test will take at least 2 seconds to run, since one of the tests waits for 2 seconds.

Expected output:
All 27 tests will report no failures, the tests that are running will also be printed out,
along with any outputs from the main application.