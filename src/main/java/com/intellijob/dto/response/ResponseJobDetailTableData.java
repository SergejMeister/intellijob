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

package com.intellijob.dto.response;

import com.intellijob.domain.JobDetail;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Data transfer object represents table of jobDetails.
 */
public class ResponseJobDetailTableData extends ResponseTableData {

    List<ResponseJobDetailData> jobDetails;

    public ResponseJobDetailTableData(List<JobDetail> listOfJobDetail, Boolean hasContent) {
        init(listOfJobDetail, hasContent);
    }

    public ResponseJobDetailTableData(Page<JobDetail> jobDetailPage, Boolean hasContent) {
        super(jobDetailPage);
        init(jobDetailPage.getContent(), hasContent);
    }

    private void init(List<JobDetail> listOfJobDetail, Boolean hasContent) {
        this.jobDetails = new ArrayList<>();
        this.jobDetails.addAll(
                listOfJobDetail.stream().map(jobDetail -> new ResponseJobDetailData(jobDetail, hasContent))
                        .collect(Collectors.toList()));
    }


    /**
     * Returns jobDetails.
     *
     * @return list of jobDetail.
     */
    public List<ResponseJobDetailData> getJobDetails() {
        return jobDetails;
    }

    /**
     * Sets jobDetails.
     *
     * @param jobDetails jobDetails to set.
     */
    public void setJobDetails(List<ResponseJobDetailData> jobDetails) {
        this.jobDetails = jobDetails;
    }
}
