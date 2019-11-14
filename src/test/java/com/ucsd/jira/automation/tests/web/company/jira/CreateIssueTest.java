package com.ucsd.jira.automation.tests.web.company.jira;

import com.pwc.core.framework.annotations.Issue;
import com.pwc.core.framework.listeners.Retry;
import com.ucsd.jira.automation.data.Constants;
import com.ucsd.jira.automation.frameworksupport.Groups;
import com.ucsd.jira.automation.frameworksupport.JiraTestCase;
import com.ucsd.jira.automation.frameworksupport.type.JiraIssue;
import org.testng.annotations.Test;

import static com.pwc.assertion.AssertService.assertEquals;
import static com.pwc.logging.service.LoggerService.*;


public class CreateIssueTest extends JiraTestCase {

    @Override
    public void beforeMethod() {
    }

    @Override
    public void afterMethod() {
    }

    //   @Issue("STORY-1234")
    @Test(retryAnalyzer = Retry.class, groups = {Groups.ACCEPTANCE_TEST})
    public void testCreateIssue() {

        FEATURE("Create JIRA issue");
        SCENARIO("User logs in and creates JIRA issue");

        GIVEN("I am a valid user");
        webElementVisible(Constants.NEW_TEST_HEADING);

        WHEN("I navigate to the Create Global Item icon");
        webAction(Constants.CREATE_ISSUE_BUTTON);
//        redirect(Constants.HOME_URL);

        THEN("Input the summary and description of the issue");
        String test_text = Constants.SUMMARY_TEXT + " for CarolynTag" ;
        webAction(Constants.SUMMARY_INPUT, test_text );
        webAction(Constants.DESCRIPTION_TEXTAREA, Constants.DESCRIPTION_TEXT);
        webAction(Constants.CREATE_BUTTON);

        webAction(Constants.SEARCH_ISSUE_BUTTON);
        webAction(Constants.ISSUE_TYPE_COMBOBOX, Constants.ISSUE_SELECT_TEXT);
        webAction(Constants.SEARCH_ID_INPUT, "CarolynTag");

        assertEquals("Verify text", test_text);

        //  GIVEN("I have a known issue number=%s to search for", issueNumber);

        //    WHEN("I search for a Jira issue number");
        //    JiraIssue jiraIssue = findRecentIssue(issueNumber);
//        THEN("The correct Jira issue was found or created");
        //     assertEquals("Verify Issue Number", jiraIssue.getMetadata(), issueNumber);
        redirect(Constants.HOME_URL);

    }

}
