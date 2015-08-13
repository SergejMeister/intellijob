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

import com.intellijob.domain.AuditData;
import com.intellijob.models.AuditModel;

/**
 * This controller provide audit methods.
 */
public interface AuditController {


    /**
     * Create auditModel with current audit data and histories.
     *
     * @param pageIndex page index for histories data.
     * @param limit     page limit for histories data.
     *
     * @return created auditModel.
     */
    AuditModel createAuditModel(int pageIndex, int limit);

    /**
     * Create current audit data.
     *
     * @return created current auditData.
     */
    AuditData createCurrentAuditData();

    /**
     * Save currentAuditData and returns saved data with object id.
     */
    AuditData saveCurrentAuditData(AuditData auditData);

    /**
     * Delete history audit data and return deleted object.
     */
    AuditData deleteById(String auditId);
}
