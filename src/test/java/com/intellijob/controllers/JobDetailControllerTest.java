/*
 * Copyright 2015 Sergej Meister
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellijob.controllers;

import com.intellijob.BaseTester;
import com.intellijob.domain.JobDetail;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Unit class to test <code>JobDetailController</code>
 */
public class JobDetailControllerTest extends BaseTester {

    @Autowired
    private JobController jobController;

    @Autowired
    private JobDetailController jobDetailController;

    @Test
    public void extractJobDetailsOfAllJobLive() {
        if (!RUNNING_LIVE) {
            Assert.assertTrue("Don't run this test.", Boolean.TRUE);
            return;
        }

        List<JobDetail> jobDetails = jobDetailController.extractJobs();
        Assert.assertFalse(jobDetails.isEmpty());
        Assert.assertTrue(jobDetails.size() > 1);
    }
}
