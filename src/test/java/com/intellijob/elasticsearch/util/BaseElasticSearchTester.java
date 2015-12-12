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

package com.intellijob.elasticsearch.util;


import com.intellijob.BaseTester;
import com.intellijob.ElasticsearchConfiguration;
import org.elasticsearch.client.Client;
import org.junit.AfterClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

public class BaseElasticSearchTester extends BaseTester {

    @Autowired
    protected ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ElasticsearchConfiguration elasticsearchConfiguration;

    @AfterClass
    public static void closeEsClient() {

    }

    protected Client getEsClient() {
        return elasticsearchConfiguration.getNode().client();
    }
}
