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

package com.intellijob.webservices;


import com.intellijob.dto.response.DownloadResultData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class JobLinkServicesTest extends BaseWebServiceTester {

    //public static final String PLACEHOLDER_JOBlINK_ID = "{jobLinkId}";

    @Test
    public void testLiveDownloadAllJobContents() throws Exception {
        if (!isProduction) {
            Assert.assertTrue("Don't run this test.", Boolean.TRUE);
            return;
        }

        //String jobLinkId = "5568b2a044ae360321161380";
        //String url = Endpoints.JOBS_BY_JOBLINK_ID_DOWNLOAD.replace(PLACEHOLDER_JOBlINK_ID, jobLinkId);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.put(Endpoints.JOBLINKS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        DownloadResultData downloadResultData = mapper.readValue(content, DownloadResultData.class);
        Assert.assertNotNull(downloadResultData);
        Assert.assertNotNull(downloadResultData.getDownloadedCount());
    }
}
