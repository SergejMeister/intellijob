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

import com.intellijob.models.AuditModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Data transfer object to represents current and history of audit data.
 */
public class ResponseAuditModelData extends ResponseTableData {

    private ResponseAuditData currentAuditData;

    private List<ResponseAuditData> historyData;

    public ResponseAuditModelData(AuditModel auditModel) {
        super(auditModel.getHistoriesPage());
        this.historyData = new ArrayList<>();
        historyData.addAll(auditModel.getHistoriesList().stream().map(ResponseAuditData::new)
                .collect(Collectors.toList()));
        this.currentAuditData = new ResponseAuditData(auditModel);
    }

    /**
     * Current audit data doesn't have id, because not saved.
     */
    public ResponseAuditData getCurrentAuditData() {
        return currentAuditData;
    }

    /**
     * Sets current audit data.
     */
    public void setCurrentAuditData(ResponseAuditData currentAuditData) {
        this.currentAuditData = currentAuditData;
    }

    /**
     * Returns histories, that saved.
     */
    public List<ResponseAuditData> getHistoryData() {
        return historyData;
    }

    /**
     * Sets new history data.
     */
    public void setHistoryData(List<ResponseAuditData> historyData) {
        this.historyData = historyData;
    }
}
