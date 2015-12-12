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

import com.intellijob.Constants;
import com.intellijob.controllers.UserController;
import com.intellijob.domain.User;
import com.intellijob.domain.skills.SkillRatingNode;
import com.intellijob.elasticsearch.repository.EsJobDetailRepository;
import com.intellijob.exceptions.UserNotFoundException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class SearchQueryUtilityTest extends BaseElasticSearchTester {

    public static final int DEFAULT_OFFSET = 0;

    private static List<SkillRatingNode> userSkills;

    @Autowired
    private EsJobDetailRepository esJobDetailRepository;

    @Autowired
    private UserController userController;

    private User user;

    private long countAllJobDetails;


//    @BeforeClass
//    public static void beforeClass() {
//        userSkills = generateUserSkill();
//    }

    public static long countNumberOfOccurrencesOfWordInString(String content, String searchWord) {
//        return Arrays.stream(content.split("[ ,\\.]")).filter(s -> s.equals(searchWord)).count();
        return Arrays.stream(content.split("[ ,\\.]")).filter(s -> s.equals(searchWord)).count();
    }

    @Before
    public void before() throws UserNotFoundException {
        this.user = userController.getUniqueUser();
        userSkills = user.getSkills().getAllSkills();
        countAllJobDetails = esJobDetailRepository.count();
    }

    @Test
    public void testBuildFullTextSearchQuery() throws Exception {
        printStartTest("ORIGIN FULL TEXT");
        String testSearchData = "Werkstudent Java,Datenbanken,Jenkins";
//        String testSearchData = user.getSimpleSearchField();

        PageRequest pageRequest = new PageRequest(DEFAULT_OFFSET, Constants.DB_RESULT_LIMIT,
                new Sort(Sort.Direction.DESC, Constants.DB_FIELD_RECEIVED_DATE));

//        SearchQuery searchQuery = SearchQueryUtility.buildFullTextSearchBoolQuery(testSearchData, pageRequest);
//        SearchQuery searchQuery = SearchQueryUtility.buildFullTextSearchMatchQuery_2(testSearchData, pageRequest);
        SearchQuery searchQuery = SearchQueryUtility.buildFullTextSearchMatchQuery_3(testSearchData, pageRequest);



        Assert.assertNotNull(searchQuery);

//        SearchResponse searchResponse =
//                getEsClient().prepareSearch("intellijob").setQuery(searchQuery.getQuery())
//                        .setSize(Constants.DB_RESULT_LIMIT).addSort(Constants.DB_FIELD_RECEIVED_DATE, SortOrder.DESC).get();
        SearchResponse searchResponse = getEsClient().prepareSearch("intellijob").setQuery(searchQuery.getQuery()).setExplain(true)
                .setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);

        System.out.println("Job size: " + countAllJobDetails);
        System.out.println("Search term: " + testSearchData);
        System.out.println("Hits: " + searchResponse.getHits().totalHits());
        System.out.println("MaxScore: " + searchResponse.getHits().getMaxScore());
        for (int i = 0; i < Constants.DB_RESULT_LIMIT; i++) {
            System.out.println("----------------------------------------------------------------------------------");
            SearchHit searchHit = searchResponse.getHits().getAt(i);
            int index = i + 1;
            final String receivedDate = formatDate((Long) searchHit.getSource().get(Constants.DB_FIELD_RECEIVED_DATE));

            StringBuilder firstLine = new StringBuilder();
            firstLine.append(index + " Hit: ");
            firstLine.append("score(").append(searchHit.getScore()).append(") | ");
            firstLine.append("receivedDate(").append(receivedDate).append(") | ");
            firstLine.append("name(").append(searchHit.getSource().get("name")).append(") | ");
            firstLine.append("id(").append(searchHit.getSource().get("id")).append(") ");
            System.out.println(firstLine);

            StringBuilder secondLine = new StringBuilder();
            secondLine.append(index + " Explain(: ").append(searchHit.getExplanation().toString()).append(" )");
            System.out.println(secondLine);
        }

        printEndTest("ORIGIN FULL TEXT");
    }

    private List<String> splitSearchData(String searchData) {
        List<String> result = new ArrayList<>();
        String[] splitedArray = searchData.split(",");
        for (String splitedStr : splitedArray) {
            String[] words = splitedStr.split(" ");
            Collections.addAll(result, words);
        }

        return result;

    }

    private String formatDate(Long dateInMilliseconds) {
        Date date = new Date(dateInMilliseconds);
        final DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH-mm");
        return format.format(date);
    }

    private void printStartTest(String testName) {
        System.out.println("=====================================================================================");
        System.out.println("======================= START TEST " + testName);
        System.out.println("=====================================================================================");
    }

    private void printEndTest(String testName) {
        System.out.println("=====================================================================================");
        System.out.println("======================= END TEST " + testName);
        System.out.println("=====================================================================================");
        System.out.println();
    }

    @Test
    @Ignore("not implemented!")
    public void testBuildBoostingQueryAndBoostRatingField() throws Exception {

    }

    @Test
    @Ignore("not implemented!")
    public void testBuildFunctionScoreQueryAndBoostRatingField() throws Exception {

    }

    @Test
    @Ignore("not implemented!")
    public void testBuildBoolQueryAndBoostRatingField() throws Exception {

    }
}