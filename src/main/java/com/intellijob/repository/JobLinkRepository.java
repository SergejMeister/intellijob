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

import com.intellijob.domain.JobLink;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

/**
 * Repository interface for domain object <code>JobLink</code>.
 */
public interface JobLinkRepository extends MongoRepository<JobLink, String> {

    /**
     * Find jobLink by specified id.
     *
     * @param id job link id.
     *
     * @return job link object.
     */
    JobLink findOne(String id);

    /**
     * Returns job links with specified downloaded flag.
     *
     * @param downloaded downloaded flag.
     *
     * @return list of founded job links.
     */
    List<JobLink> findByDownloaded(Boolean downloaded);

    List<JobLink> findByHrefIn(Set<String> hrefs);
}
