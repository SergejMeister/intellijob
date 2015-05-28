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

package com.intellijob.dto;

import com.intellijob.domain.JobLink;

import java.util.ArrayList;
import java.util.List;

/**
 * Data transfer object represents table of jobLinks
 */
public class ResponseJobLinkTableData extends ResponseData {

    List<ResponseJobLinkData> jobLinks;


    /**
     * Constructor with list of domain object <code>JobLink</code>.
     *
     * @param listOfDomainObjects list of domain object <code>JobLink</code>.
     */
    public ResponseJobLinkTableData(List<JobLink> listOfDomainObjects) {
        this.jobLinks = new ArrayList<>();
        for (JobLink jobLink : listOfDomainObjects) {
            ResponseJobLinkData responseJobLinkData = new ResponseJobLinkData(jobLink);
            this.jobLinks.add(responseJobLinkData);
        }
    }

    /**
     * Returns list of response jobLinks data.
     *
     * @return list of <code>ResponseJobLinkData</code>.
     */
    public List<ResponseJobLinkData> getJobLinks() {
        return jobLinks;
    }

    /**
     * Sets list of response job links data.
     *
     * @param jobLinks list of jobLinks data.
     */
    public void setJobLinks(List<ResponseJobLinkData> jobLinks) {
        this.jobLinks = jobLinks;
    }
}
