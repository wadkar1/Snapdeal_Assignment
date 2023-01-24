package com.sf.utility;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryFailedTestCases implements IRetryAnalyzer {
	private int retryCnt = 0;
	// You could mentioned maxRetryCnt (Maximiun Retry Count) as per your
	// requirement. Here I took 2, If any failed testcases then it runs two times
	private int maxRetryCnt;

	// This method will be called everytime a test fails. It will return TRUE if a
	// test fails and need to be retried, else it returns FALSE
	@Override
	public boolean retry(ITestResult result) {
		maxRetryCnt = Integer.parseInt(ConfigReader.getLocalValue("retryCount"));
		if (retryCnt < maxRetryCnt) {

			System.out.println("Retrying " + result.getName() + " again for " + (retryCnt + 1) + " time");
			retryCnt++;
			return true;
		}
		return false;
	}

}
