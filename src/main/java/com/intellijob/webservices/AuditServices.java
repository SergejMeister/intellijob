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

import com.intellijob.controllers.AuditController;
import com.intellijob.domain.AuditData;
import com.intellijob.dto.RequestAuditData;
import com.intellijob.dto.ResponseAuditData;
import com.intellijob.dto.ResponseAuditModelData;
import com.intellijob.models.AuditModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Audit-Services.
 * <p>
 * Handle all request with endpoints <code>/audit**</code>
 */
@RestController
public class AuditServices extends BaseServices {

    @Autowired
    private AuditController auditController;

    /**
     * Get audit model with current and history audit data.
     *
     * @return data transfer object <code>ResponseAuditModelData.java</code>
     */
    @RequestMapping(value = Endpoints.AUDIT_PAGING, method = RequestMethod.GET)
    public @ResponseBody ResponseAuditModelData getAudit(@PathVariable int pageIndex, @PathVariable int limit) {
        AuditModel auditModel = auditController.createAuditModel(pageIndex, limit);
        return new ResponseAuditModelData(auditModel);
    }

    /**
     * Save current audit data.
     * <p>
     * New GET-request <code>url</code>
     * Save html response into jobs collection.
     *
     * @return job data.
     */
    @RequestMapping(value = Endpoints.AUDIT, method = RequestMethod.POST)
    public @ResponseBody ResponseAuditData saveCurrentAuditData(@RequestBody RequestAuditData requestAuditData) {
        AuditData currentAuditDataToSave = new AuditData();
        currentAuditDataToSave.setCountJobDetails(requestAuditData.getCountJobDetails());
        currentAuditDataToSave.setCountNotEmptyContactPersons(requestAuditData.getCountNotEmptyContactPersons());
        currentAuditDataToSave.setCountEmptyContactPersons(requestAuditData.getCountEmptyContactPersons());

        AuditData newHistoryAuditData = auditController.saveCurrentAuditData(currentAuditDataToSave);
        return new ResponseAuditData(newHistoryAuditData);
    }

    /**
     * Delete history audit data by id.
     *
     * @return data transfer object <code>ResponseJobDetailData.java</code>
     */
    @RequestMapping(value = Endpoints.AUDIT_BY_ID, method = RequestMethod.DELETE)
    public @ResponseBody ResponseAuditData deleteJobDetail(@PathVariable String auditId) throws Exception {
        auditController.deleteById(auditId);
        return new ResponseAuditData(auditId);
    }
}
