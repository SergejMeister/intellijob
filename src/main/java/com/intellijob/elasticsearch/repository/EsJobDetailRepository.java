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

package com.intellijob.elasticsearch.repository;

import com.intellijob.elasticsearch.domain.EsJobDetail;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Elasticsearch repository to manage JobDetails
 */
public interface EsJobDetailRepository extends ElasticsearchRepository<EsJobDetail, String> {

    /**
     * Find jodDetail data in elasticsearch indexes by id.
     *
     * @param id jobDetail id.
     *
     * @return elasticsearch job detail data.
     */
    EsJobDetail findById(String id);


    /**
     * Find jodDetail data in elasticsearch indexes by name.
     *
     * @param name job detail name.
     *
     * @return elasticsearch job detail data.
     */
    EsJobDetail findByName(String name);
}
