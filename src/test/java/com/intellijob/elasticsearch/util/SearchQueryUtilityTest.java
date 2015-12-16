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
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
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


    public static long countNumberOfOccurrencesOfWordInString(String content, String searchWord) {
        return Arrays.stream(content.split("[ ,\\.]")).filter(s -> s.equals(searchWord)).count();
    }

    @Before
    public void before() throws UserNotFoundException {
        this.user = userController.getUniqueUser();
        userSkills = user.getSkills().getAllSkills();
        countAllJobDetails = esJobDetailRepository.count();
    }

    @Test
    public void testBuildFullTextSearchQuery_2() throws Exception {
        final String testName = "ORIGIN FULL TEXT TEST 2";
        printStartTest(testName);

        String testSearchData = "Werkstudent Java,Datenbanken,Jenkins";

        PageRequest pageRequest = new PageRequest(DEFAULT_OFFSET, Constants.DB_RESULT_LIMIT);

        SearchQuery searchQuery = SearchQueryUtility.buildFullTextSearchMatchQuery_2(testSearchData, pageRequest);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse =
                getEsClient().prepareSearch("intellijob").setQuery(searchQuery.getQuery()).setExplain(true)
                        .setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty!", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplain(testSearchData, searchResponse);
        printEndTest(testName);
    }

    @Test
    public void testBuildFullTextSearchQuery_3() throws Exception {
        final String testName = "ORIGIN FULL TEXT TEST 3";
        printStartTest(testName);

        String testSearchData = "Werkstudent Java,Datenbanken,Jenkins";

        PageRequest pageRequest = new PageRequest(DEFAULT_OFFSET, Constants.DB_RESULT_LIMIT);
        SearchQuery searchQuery = SearchQueryUtility.buildFullTextSearchMatchQuery_3(testSearchData, pageRequest);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse =
                getEsClient().prepareSearch("intellijob").setQuery(searchQuery.getQuery()).setExplain(true)
                        .setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty.", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplain(testSearchData, searchResponse);
        printEndTest(testName);
    }

    @Test
    public void testBuildFullTextSearchQuery_4() throws Exception {
        final String testName = "ORIGIN FULL TEXT TEST 4";
        printStartTest(testName);

        String testSearchData = "Werkstudent Java,Datenbanken,Jenkins";

        PageRequest pageRequest = new PageRequest(DEFAULT_OFFSET, Constants.DB_RESULT_LIMIT);
        SearchQuery searchQuery = SearchQueryUtility.buildFullTextSearchMatchQuery_4(testSearchData, pageRequest);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse =
                getEsClient().prepareSearch("intellijob").setQuery(searchQuery.getQuery()).setExplain(true)
                        .setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty.", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplain(testSearchData, searchResponse);
        printEndTest(testName);
    }

    @Test
    public void testBuildFullTextSearchQuery_4_And_Sort() throws Exception {
        final String testName = "ORIGIN FULL TEXT TEST 4 with Extra Sort";
        printStartTest(testName);

        String testSearchData = "Werkstudent Java,Datenbanken,Jenkins";

        PageRequest pageRequest = new PageRequest(DEFAULT_OFFSET, Constants.DB_RESULT_LIMIT);
        SearchQuery searchQuery = SearchQueryUtility.buildFullTextSearchMatchQuery_4(testSearchData, pageRequest);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse =
                getEsClient().prepareSearch("intellijob").setQuery(searchQuery.getQuery()).setExplain(true)
                        .setSize(Constants.DB_RESULT_LIMIT).addSort(Constants.DB_FIELD_RECEIVED_DATE, SortOrder.DESC)
                        .get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty.", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplain(testSearchData, searchResponse);
        printEndTest(testName);
    }

    @Test
    public void testBuildBoolQueryAndBoostRatingField_1() throws Exception {
        final String testName = "SKILL RATING SEARCH TEST 1";
        printStartTest(testName);

        List<SkillRatingNode> skillRatingNodes = user.getSkills().getAllSkills();
        SearchQuery searchQuery = SearchQueryUtility
                .buildBoolQueryAndBoostRatingField_1(skillRatingNodes, DEFAULT_OFFSET, Constants.DB_RESULT_LIMIT);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse = getEsClient().prepareSearch("intellijob")
                .setQuery(searchQuery.getQuery()).setExplain(true).setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty.", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplain(skillRatingNodes, searchResponse);
        printEndTest(testName);
    }

    @Test
    public void testBuildBoolQueryAndBoostRatingField_2() throws Exception {
        final String testName = "SKILL RATING SEARCH TEST 2";
        printStartTest(testName);

        List<SkillRatingNode> skillRatingNodes = user.getSkills().getAllSkills();
        SearchQuery searchQuery = SearchQueryUtility
                .buildBoolQueryAndBoostRatingField_2(skillRatingNodes, DEFAULT_OFFSET, Constants.DB_RESULT_LIMIT);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse = getEsClient().prepareSearch("intellijob")
                .setQuery(searchQuery.getQuery()).setExplain(true).setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty.", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplain(skillRatingNodes, searchResponse);
        printEndTest(testName);
    }

    @Test
    public void testBuildBoolQueryAndBoostRatingField_3() throws Exception {
        final String testName = "SKILL RATING SEARCH TEST 3";
        printStartTest(testName);

        List<SkillRatingNode> skillRatingNodes = user.getSkills().getAllSkills();
        SearchQuery searchQuery = SearchQueryUtility
                .buildBoolQueryAndBoostRatingField_3(skillRatingNodes, DEFAULT_OFFSET, Constants.DB_RESULT_LIMIT);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse = getEsClient().prepareSearch("intellijob")
                .setQuery(searchQuery.getQuery()).setExplain(true).setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty.", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplain(skillRatingNodes, searchResponse);
        printEndTest(testName);
    }

    @Test
    public void testBuildBoolQueryAndBoostRatingField_4() throws Exception {
        final String testName = "SKILL RATING SEARCH TEST 4";
        printStartTest(testName);

        List<SkillRatingNode> skillRatingNodes = user.getSkills().getAllSkills();
        SearchQuery searchQuery = SearchQueryUtility
                .buildBoolQueryAndBoostRatingField_4(skillRatingNodes, DEFAULT_OFFSET, Constants.DB_RESULT_LIMIT);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse = getEsClient().prepareSearch("intellijob")
                .setQuery(searchQuery.getQuery()).setExplain(true).setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty.", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplain(skillRatingNodes, searchResponse);
        printEndTest(testName);
    }

    @Test
    public void testBuildBoolQueryAndBoostRatingField_5() throws Exception {
        final String testName = "SKILL RATING SEARCH TEST 5";
        printStartTest(testName);

        List<SkillRatingNode> skillRatingNodes = user.getSkills().getAllSkills();
        SearchQuery searchQuery = SearchQueryUtility
                .buildBoolQueryAndBoostRatingField_5(skillRatingNodes, DEFAULT_OFFSET, Constants.DB_RESULT_LIMIT);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse = getEsClient().prepareSearch("intellijob")
                .setQuery(searchQuery.getQuery()).setExplain(true).setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty.", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplain(skillRatingNodes, searchResponse);
        printEndTest(testName);
    }

    @Test
    public void testBuildBoolQueryAndBoostRatingField_6() throws Exception {
        final String testName = "SKILL RATING SEARCH TEST 6";
        printStartTest(testName);

        List<SkillRatingNode> skillRatingNodes = user.getSkills().getAllSkills();
        SearchQuery searchQuery = SearchQueryUtility
                .buildBoolQueryAndBoostRatingField_6(skillRatingNodes, DEFAULT_OFFSET, Constants.DB_RESULT_LIMIT);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse = getEsClient().prepareSearch("intellijob")
                .setQuery(searchQuery.getQuery()).setExplain(true).setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty.", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplain(skillRatingNodes, searchResponse);
        printEndTest(testName);
    }

    @Test
    public void testBuildBoolQueryAndBoostRatingField_7() throws Exception {
        final String testName = "SKILL RATING SEARCH TEST 7";
        printStartTest(testName);

        List<SkillRatingNode> skillRatingNodes = user.getSkills().getAllSkills();
        SearchQuery searchQuery = SearchQueryUtility
                .buildBoolQueryAndBoostRatingField_7(skillRatingNodes, DEFAULT_OFFSET, Constants.DB_RESULT_LIMIT);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse = getEsClient().prepareSearch("intellijob")
                .setQuery(searchQuery.getQuery()).setExplain(true).setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty.", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplain(skillRatingNodes, searchResponse);
        printEndTest(testName);
    }

    @Test
    public void testBuildBoolQueryAndBoostRatingField_8() throws Exception {
        final String testName = "SKILL RATING SEARCH TEST 8";
        printStartTest(testName);

        List<SkillRatingNode> skillRatingNodes = user.getSkills().getAllSkills();
        SearchQuery searchQuery = SearchQueryUtility
                .buildBoolQueryAndBoostRatingField_8(skillRatingNodes, DEFAULT_OFFSET, Constants.DB_RESULT_LIMIT);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse = getEsClient().prepareSearch("intellijob")
                .setQuery(searchQuery.getQuery()).setExplain(true).setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty.", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplain(skillRatingNodes, searchResponse);
        printEndTest(testName);
    }


    private void printQuery(QueryBuilder query) {
        System.out.println("----------------------------- QUERY  ----------------------------------------");
        System.out.println(query.toString());
        System.out.println("-----------------------------------------------------------------------------");
    }

    private void printFullTextExplain(final Collection<SkillRatingNode> skillRatingNodes,
                                      final SearchResponse searchResponse) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (SkillRatingNode skillRatingNode : skillRatingNodes) {
            sb.append("{name: ").append(skillRatingNode.getSkillNode().getName()).append(",");
            sb.append("rating: ").append(skillRatingNode.getRating()).append("}");
        }
        sb.append("]");

        printFullTextExplain(sb.toString(), searchResponse);
    }

    private void printFullTextExplain(final String testSearchData, final SearchResponse searchResponse) {
        System.out.println("------------------------------ RESPONSE -------------------------------------");
        System.out.println("Job size: " + countAllJobDetails);
        System.out.println("Search term: " + testSearchData);
        System.out.println("Hits: " + searchResponse.getHits().totalHits());
        System.out.println("MaxScore: " + searchResponse.getHits().getMaxScore());
        for (int i = 0; i < Constants.DB_RESULT_LIMIT; i++) {
            System.out.println("-----------------------------------------------------------------------------");
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
}