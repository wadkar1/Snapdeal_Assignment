package com.sf.utility;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;

public class RetryListenerClass implements IAnnotationTransformer {
	public void transform(ITestAnnotation testannotation, Class testClass, Constructor testConstructor,
			Method testMethod) {
		Class<? extends IRetryAnalyzer> retry = testannotation.getRetryAnalyzerClass();
		if (retry == null) {
			testannotation.setRetryAnalyzer(RetryFailedTestCases.class);
		}
	}
}