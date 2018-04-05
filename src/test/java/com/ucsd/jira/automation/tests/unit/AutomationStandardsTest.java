package com.ucsd.jira.automation.tests.unit;

import com.pwc.core.framework.util.PropertiesUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class AutomationStandardsTest {

    private static final String WEB_TEST_DIRECTORY_LOCATION = "com/ucsd/jira/automation/tests/web";
    private Collection<File> allFiles = new LinkedList<>();
    private Collection<File> allTestFiles = new LinkedList<>();

    @Before
    public void setUp() throws Exception {
        File directory = PropertiesUtils.getFileFromResources(WEB_TEST_DIRECTORY_LOCATION);
        allFiles.addAll(FileUtils.listFiles(directory, new String[]{"class"}, true));

        for (File allFile : allFiles) {
            if (allFile.getName().contains("Test.class")) {
                allTestFiles.add(allFile);
            }
        }

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test()
    public void testFirstLetterCapitalizedTest() {

        for (File testFile : allTestFiles) {
            String firstLetterInClassName = testFile.getName().substring(0, 1);
            Assert.assertTrue("Verify Test Name begins with CAPITAL for test='" + testFile.getName() + "'", StringUtils.isAllUpperCase(firstLetterInClassName));
        }

    }

    @Test()
    public void testNamedCorrectlyTest() {

        for (File testFile : allTestFiles) {
            Assert.assertTrue("Verify Test Naming for test='" + testFile.getName() + "'", StringUtils.endsWith(testFile.getName(), "Test.class"));
        }

    }

    @Test()
    public void testNameNotDuplicatedTest() {

        for (File testFile : allTestFiles) {

            int numberOfTestsNamedIdentically = 0;
            for (File individual : allTestFiles) {
                if (individual.getName().equals(testFile.getName())) {
                    numberOfTestsNamedIdentically++;
                }
            }

            assertEquals("Verify Test Name Not duplicated for test='" + testFile.getName() + "'", 1, numberOfTestsNamedIdentically);
        }

    }

    @Test
    public void testNameEndsWithTest() throws IOException {

        for (File testFile : allTestFiles) {

            assertTrue("Verify test ends with 'Test' suffix", StringUtils.endsWith(testFile.getName(), "Test.class"));

        }

    }

    @Test
    public void testNameSameAsMethodName() {

        final String DELIMITER = "\u0001";

        for (File testFile : allTestFiles) {

            String expectedMethodName = testFile.getName();
            expectedMethodName = StringUtils.removeEnd(expectedMethodName, "Test.class");
            expectedMethodName = "test" + expectedMethodName;

            List<String> testContents = readCompiledClass(testFile);
            boolean foundMethodName = false;
            for (String testContent : testContents) {
                if (StringUtils.contains(testContent, expectedMethodName)) {
                    List<String> methodContents = Arrays.asList(StringUtils.split(testContent, DELIMITER));
                    for (String methodContent : methodContents) {
                        if (StringUtils.containsIgnoreCase(methodContent, expectedMethodName)) {
                            String actualMethodName = "test" + StringUtils.substringAfter(methodContent, "test");
                            if (StringUtils.equalsIgnoreCase(expectedMethodName, actualMethodName)) {
                                foundMethodName = true;
                                break;
                            } else {
                                break;
                            }
                        }
                    }
                }
            }

            Assert.assertTrue("Test method name matches and adheres to naming standards of class name for test='" + testFile.getName() + "'", foundMethodName);

        }

    }

    @Test
    public void testGherkinLoggingPresent() throws Exception {

        for (File testFile : allTestFiles) {

            List<String> testContents = readCompiledClass(testFile);
            for (String testContent : testContents) {
                if (testContent.contains("FEATURE")) {
                    assertTrue("FEATURE Gherkin logging is present for test='" + testFile.getName() + "'", testContent.contains("FEATURE"));
                    assertTrue("SCENARIO Gherkin logging is present for test='" + testFile.getName() + "'", testContent.contains("SCENARIO"));
                }
            }
        }

    }

    @Test
    public void testGroupNamesPresent() throws Exception {

        for (File testFile : allTestFiles) {

            List<String> testContents = readCompiledClass(testFile);
            for (String testContent : testContents) {
                if (testContent.contains("groups")) {
                    assertTrue("TestNG Group annotation present for test='" + testFile.getName() + "'", testContent.contains("groups"));
                }
            }
        }

    }

    private List<String> readCompiledClass(File testFile) {
        return com.pwc.core.framework.util.FileUtils.readFile(testFile, testFile.getName());
    }

}
