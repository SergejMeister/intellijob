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

package com.intellijob.controllers.impl;

import com.intellijob.controllers.AuditController;
import com.intellijob.domain.AuditData;
import com.intellijob.models.AuditModel;
import com.intellijob.repository.AuditDataRepository;
import com.intellijob.repository.JobDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import java.util.Date;

/**
 * This is a implementation of interface <code>AuditController</code>.
 */
@Controller
public class AuditControllerImpl implements AuditController {

    @Autowired
    private JobDetailRepository jobDetailRepository;

    @Autowired
    private AuditDataRepository auditDataRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public AuditModel createAuditModel(int pageIndex, int limit) {
        AuditData currentAuditData = createCurrentAuditData();

        PageRequest request = new PageRequest(pageIndex, limit, new Sort(Sort.Direction.ASC, "createdDate"));
        Page<AuditData> histories = auditDataRepository.findAll(request);

        AuditModel auditModel = new AuditModel(histories);
        auditModel.setCurrentData(currentAuditData);

        return auditModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuditData createCurrentAuditData() {
        long countJobDetails = jobDetailRepository.count();
        long countEmptyContactPersons = jobDetailRepository.countEmptyContactPersons();
        long countNotEmptyContactPersons = countJobDetails - countEmptyContactPersons;

        AuditData currentAuditData = new AuditData();
        currentAuditData.setCountJobDetails(countJobDetails);
        currentAuditData.setCountEmptyContactPersons(countEmptyContactPersons);
        currentAuditData.setCountNotEmptyContactPersons(countNotEmptyContactPersons);

        return currentAuditData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuditData saveCurrentAuditData(AuditData auditData) {
        auditData.setCreatedDate(new Date());
        AuditData result = auditDataRepository.save(auditData);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuditData deleteById(String auditId) {
        AuditData auditDataToDelete = auditDataRepository.findOne(auditId);
        auditDataRepository.delete(auditDataToDelete);
        return auditDataToDelete;
    }
}
