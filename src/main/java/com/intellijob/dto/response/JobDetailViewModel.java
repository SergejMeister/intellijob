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

import com.intellijob.domain.User;
import com.intellijob.elasticsearch.domain.EsJobDetail;
import org.springframework.data.domain.Page;

/**
 * Represents JobDetail view.
 */
public class JobDetailViewModel {

    private ResponseJobDetailTableData tableData;

    private ResponseUserData userData;

    public JobDetailViewModel(User user, Page<EsJobDetail> jobDetailsPage, Boolean hasContent) {
        userData = new ResponseUserData(user);
        tableData = new ResponseJobDetailTableData(jobDetailsPage, hasContent);
    }

    public ResponseUserData getUserData() {
        return userData;
    }

    public void setUserData(ResponseUserData userData) {
        this.userData = userData;
    }

    public ResponseJobDetailTableData getTableData() {
        return tableData;
    }

    public void setTableData(ResponseJobDetailTableData tableData) {
        this.tableData = tableData;
    }
}
