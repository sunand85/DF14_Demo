package com.dreamforce.demo.sfdc;

import java.io.IOException;
import java.sql.SQLException;

import com.dreamforce.demo.sfdc.bean.BulkJobInfo;
import com.dreamforce.demo.sfdc.bean.JobInfo;

public interface IJobProcessor {

	public void init() throws IOException;

	public void getListOfJobs();

	public BulkJobInfo execute(JobInfo jobInfo) throws IOException, SQLException;
}
