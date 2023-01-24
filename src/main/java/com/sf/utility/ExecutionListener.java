package com.sf.utility;

import org.testng.IExecutionListener;

import com.sf.base.TestBase;

public class ExecutionListener implements IExecutionListener {

	@Override
	public void onExecutionStart() {

	}

	@Override
	public void onExecutionFinish() {
		new TestBase().createdEmailableReport();
	}

}
