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

import com.intellijob.domain.Job;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository interface for domain object <code>Job</code>.
 */
public interface JobRepository extends MongoRepository<Job, String> {

    /**
     * Returns all jobs.
     *
     * @return list of jobs.
     */
    List<Job> findAll();

    /**
     * Find job by specified id.
     *
     * @param id job id.
     *
     * @return job object.
     */
    Job findOne(String id);
}