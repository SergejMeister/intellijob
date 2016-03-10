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
import com.intellijob.domain.Job;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Unit class to test <code>JobLinkController</code>
 */
public class JobControllerTest extends BaseTester {

    @Autowired
    private JobController jobController;

    @Test
    public void setsAllJobAsExtractedLive() {
        if (!isProduction) {
            Assert.assertTrue("Don't run this test.", Boolean.TRUE);
            return;
        }

        List<Job> allJobs = jobController.findAll();
        Assert.assertTrue("Should be not null or empty", allJobs.size() > 0);
        List<Job> updatedJobs = jobController.setExtractedFlag(allJobs, Boolean.TRUE);
        Assert.assertEquals(allJobs.size(), updatedJobs.size());
        Assert.assertTrue(updatedJobs.get(0).isExtracted());
    }

    @Test
    public void setsAllJobAsNotExtractedLive() {
        if (!isProduction) {
            Assert.assertTrue("Don't run this test.", Boolean.TRUE);
            return;
        }

        List<Job> allJobs = jobController.findAll();
        Assert.assertTrue("Should be not null or empty", allJobs.size() > 0);
        List<Job> updatedJobs = jobController.setExtractedFlag(allJobs, Boolean.FALSE);
        Assert.assertEquals(allJobs.size(), updatedJobs.size());
        Assert.assertFalse(updatedJobs.get(0).isExtracted());
    }
}
