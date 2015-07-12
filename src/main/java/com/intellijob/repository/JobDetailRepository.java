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

package com.intellijob.repository;

import com.intellijob.domain.JobDetail;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository interface for domain object <code>JobDetail</code>.
 */
public interface JobDetailRepository extends MongoRepository<JobDetail, String> {

    /**
     * Returns all jobs details.
     *
     * @return list of jobs details.
     */
    List<JobDetail> findAll();

    /**
     * Find jobDetail by specified id.
     *
     * @param id job detail id.
     *
     * @return job detail object.
     */
    JobDetail findOne(String id);
}
