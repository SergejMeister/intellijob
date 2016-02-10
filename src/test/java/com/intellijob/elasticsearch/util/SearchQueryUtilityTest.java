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
import com.intellijob.elasticsearch.EsConstants;
import com.intellijob.elasticsearch.domain.EsUserSkills;
import com.intellijob.elasticsearch.repository.EsJobDetailRepository;
import com.intellijob.elasticsearch.repository.EsUserSkillsRepository;
import com.intellijob.exceptions.UserNotFoundException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private EsUserSkillsRepository esUserSkillsRepository;

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
    public void testBuildFullTextSearchQuery_1() throws Exception {
        final String testName = "ORIGIN FULL TEXT TEST 1";
        printStartTest(testName);

        String testSearchData = "Werkstudent Java,Datenbanken";

        PageRequest pageRequest = new PageRequest(DEFAULT_OFFSET, Constants.DB_RESULT_LIMIT);

        SearchQuery searchQuery = SearchQueryUtility.buildFullTextSearchBoolQuery_1(testSearchData, pageRequest);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse =
                getEsClient().prepareSearch(EsConstants.INDEX_INTELLIJOB).setQuery(searchQuery.getQuery())
                        .setExplain(true)
                        .setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty!", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplain(testSearchData, searchResponse);
        printEndTest(testName);
    }

    @Test
    public void testBuildFullTextSearchQuery_2() throws Exception {
        final String testName = "ORIGIN FULL TEXT TEST 2 - SIMPLE SORT BY RECEIVED_DATE";
        printStartTest(testName);

        String testSearchData = "Werkstudent Java,Datenbanken";

        PageRequest pageRequest = new PageRequest(DEFAULT_OFFSET, Constants.DB_RESULT_LIMIT);

        SearchQuery searchQuery = SearchQueryUtility.buildFullTextSearchBoolQuery_1(testSearchData, pageRequest);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse =
                getEsClient().prepareSearch(EsConstants.INDEX_INTELLIJOB).setQuery(searchQuery.getQuery())
                        .setExplain(true)
                        .addSort(Constants.DB_FIELD_RECEIVED_DATE,SortOrder.DESC)
                        .setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty!", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplain(testSearchData, searchResponse);
        printEndTest(testName);
    }

    @Test
    public void testBuildFullTextSearchQuery_3() throws Exception {
        final String testName = "ORIGIN FULL TEXT TEST 3 - DECAY FUNCTION FOR RECEIVED_DATE";
        printStartTest(testName);

        String testSearchData = "Werkstudent Java,Datenbanken";

        PageRequest pageRequest = new PageRequest(DEFAULT_OFFSET, Constants.DB_RESULT_LIMIT);

        SearchQuery searchQuery = SearchQueryUtility.buildFullTextSearchMatchQuery_3(testSearchData, pageRequest);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse =
                getEsClient().prepareSearch(EsConstants.INDEX_INTELLIJOB).setQuery(searchQuery.getQuery())
                        .setExplain(true)
                        .setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty!", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplain(testSearchData, searchResponse);
        printEndTest(testName);
    }

    @Test
    public void testBuildFullTextSearchQuery_4() throws Exception {
        final String testName = "ORIGIN FULL TEXT TEST 4 - Bool Query for read field";
        printStartTest(testName);

        String testSearchData = "Werkstudent Java,Datenbanken";

        PageRequest pageRequest = new PageRequest(DEFAULT_OFFSET, Constants.DB_RESULT_LIMIT);

        SearchQuery searchQuery = SearchQueryUtility.buildFullTextSearchMatchQuery_4(testSearchData, pageRequest);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse =
                getEsClient().prepareSearch(EsConstants.INDEX_INTELLIJOB).setTypes(EsConstants.TYPE_JOB_DETAILS).setQuery(searchQuery.getQuery())
                        .setExplain(true)
                        .setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty!", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplain(testSearchData, searchResponse);
        printEndTest(testName);
    }

    @Test
    public void testBuildFullTextSearchQuery_5() throws Exception {
        final String testName = "ORIGIN FULL TEXT TEST - FINAL";
        printStartTest(testName);

        String testSearchData = "Werkstudent Java,Datenbanken";

        PageRequest pageRequest = new PageRequest(DEFAULT_OFFSET, Constants.DB_RESULT_LIMIT);

        SearchQuery searchQuery = SearchQueryUtility.buildFullTextSearchMatchQuery(testSearchData, pageRequest);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse =
                getEsClient().prepareSearch(EsConstants.INDEX_INTELLIJOB).setQuery(searchQuery.getQuery())
                        .setExplain(true)
                        .setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty!", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplain(testSearchData, searchResponse);
        printEndTest(testName);
    }

    @Test
    public void testBuildFullTextSearchQueryUsingOrConjunction() throws Exception {
        final String testName = "ORIGIN FULL TEXT TEST 5 - OR-CONJUCTION";
        printStartTest(testName);

        String testSearchData = "Werkstudent Java,Datenbanken";

        PageRequest pageRequest = new PageRequest(DEFAULT_OFFSET, Constants.DB_RESULT_LIMIT);

        SearchQuery searchQuery = SearchQueryUtility.buildFullTextSearchMatchQueryUsingOrConjunction(testSearchData, pageRequest);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse =
                getEsClient().prepareSearch(EsConstants.INDEX_INTELLIJOB).setTypes(EsConstants.TYPE_JOB_DETAILS).setQuery(searchQuery.getQuery())
                        .setExplain(true)
                        .setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty!", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplain(testSearchData, searchResponse);
        printEndTest(testName);
    }

    @Test
    @Ignore
    public void testBuildFullTextSearchQuery_Bosting_FieldReceivedDate() throws Exception {
        final String testName = "ORIGIN FULL TEXT TEST 3 - BOOST FIELD RECEIVED_DATE mit 1.5 FACTOR";
        printStartTest(testName);

        String testSearchData = "Werkstudent Java,Datenbanken";

        PageRequest pageRequest = new PageRequest(DEFAULT_OFFSET, Constants.DB_RESULT_LIMIT);
        SearchQuery searchQuery = SearchQueryUtility.buildFullTextSearchMatchQuery_Boosting_FieldReceivedDate(testSearchData, pageRequest);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse =
                getEsClient().prepareSearch(EsConstants.INDEX_INTELLIJOB).setQuery(searchQuery.getQuery())
                        .setExplain(true)
                        .setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty.", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplain(testSearchData, searchResponse);
        printEndTest(testName);
    }

    // -------------------------------------------------------------------------------------------------
    // Boosting Query
    // -------------------------------------------------------------------------------------------------

    @Test
    public void testBuildBoolQueryAndBoostRatingField_1() throws Exception {
        final String testName = "SKILL RATING SEARCH TEST 1";
        printStartTest(testName);

        List<EsUserSkills> userSkills = createSkillRatingNodesForBoostingTest();
        SearchQuery searchQuery = SearchQueryUtility
                .buildBoolQueryAndBoostRatingField_1(userSkills, DEFAULT_OFFSET, Constants.DB_RESULT_LIMIT);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse = getEsClient().prepareSearch(EsConstants.INDEX_INTELLIJOB).setTypes(EsConstants.TYPE_JOB_DETAILS)
                .setQuery(searchQuery.getQuery()).setExplain(true).setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty.", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplainUsingElastic(userSkills, searchResponse);
        printEndTest(testName);
    }

    @Test
    public void testBuildBoolQueryAndBoostRatingField_2() throws Exception {
        final String testName = "SKILL RATING SEARCH TEST 2 - USE OR AND WEIGHT_SCORE_FUNCTION";
        printStartTest(testName);

        List<EsUserSkills> userSkills = createSkillRatingNodesForBoostingTest();
        SearchQuery searchQuery = SearchQueryUtility
                .buildBoolQueryAndBoostRatingField_2(userSkills, DEFAULT_OFFSET, Constants.DB_RESULT_LIMIT);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse = getEsClient().prepareSearch(EsConstants.INDEX_INTELLIJOB).setTypes(EsConstants.TYPE_JOB_DETAILS)
                .setQuery(searchQuery.getQuery()).setExplain(true).setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty.", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplainUsingElastic(userSkills, searchResponse);
        printEndTest(testName);
    }

    @Test
    public void testBuildBoolQueryAndBoostRatingField_Final() {
        final String testName = "SKILL RATING SEARCH TEST - FINAL";
        printStartTest(testName);

        List<EsUserSkills> userSkills = createSkillRatingNodesForBoostingTest();
        SearchQuery searchQuery = SearchQueryUtility
                .buildBoolQueryAndBoostRatingFieldUsingEsUserSkills_Final(userSkills, DEFAULT_OFFSET,
                        Constants.DB_RESULT_LIMIT);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse = getEsClient().prepareSearch(EsConstants.INDEX_INTELLIJOB).setTypes(EsConstants.TYPE_JOB_DETAILS)
                .setQuery(searchQuery.getQuery()).setExplain(true).setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty.", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplainUsingElastic(userSkills, searchResponse);
        printEndTest(testName);
    }

    @Test
    public void testBuildBoolQueryAndBoostRatingField_Final_UsingEsUserSkills() {
        final String testName = "SKILL RATING SEARCH WITH ELASTIC USER SKILLS TEST";
        printStartTest(testName);

        List<EsUserSkills> userSkills = esUserSkillsRepository.findByUserId(user.getId());

        SearchQuery searchQuery = SearchQueryUtility
                .buildBoolQueryAndBoostRatingFieldUsingEsUserSkills_Final(userSkills, DEFAULT_OFFSET,
                        Constants.DB_RESULT_LIMIT);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse = getEsClient().prepareSearch(EsConstants.INDEX_INTELLIJOB).setTypes(EsConstants.TYPE_JOB_DETAILS)
                .setQuery(searchQuery.getQuery()).setExplain(true).setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty.", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplainUsingElastic(userSkills, searchResponse);
        printEndTest(testName);
    }

    @Test
    public void testBuildBoolQueryAndBoostRatingFieldUsingExpansion() throws Exception {
        final String testName = "SKILL RATING SEARCH TEST - USING EXPANSION";
        printStartTest(testName);

        List<EsUserSkills> userSkills = createSkillRatingNodesForBoostingTest();
        SearchQuery searchQuery = SearchQueryUtility
                .buildBoolQueryAndBoostRatingFieldUsingExpansion(userSkills, DEFAULT_OFFSET, Constants.DB_RESULT_LIMIT);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse = getEsClient().prepareSearch(EsConstants.INDEX_INTELLIJOB)
                .setQuery(searchQuery.getQuery()).setExplain(true).setSize(Constants.DB_RESULT_LIMIT).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty.", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplainUsingElastic(userSkills, searchResponse);
        printEndTest(testName);
    }

    private List<EsUserSkills> createSkillRatingNodesForBoostingTest(){
        List<EsUserSkills> userSkills = new ArrayList<>();
        userSkills.add(createEsUserSkills("1", user.getId(), "Spring Framework", 5.0f));
        //userSkills.add(createEsUserSkills("1", user.getId(), "Spring", 5.0f));
        userSkills.add(createEsUserSkills("2", user.getId(), "Java", 3.0f));
        userSkills.add(createEsUserSkills("3", user.getId(), "PostgreSQL", 2.0f));
        userSkills.add(createEsUserSkills("4", user.getId(), "Datenbanken", 2.0f));

        return userSkills;
    }

    private EsUserSkills createEsUserSkills(String id, String userId, String name, float rating) {
        EsUserSkills esUserSkills = new EsUserSkills(id, userId);
        esUserSkills.setName(name);
        esUserSkills.setRating(rating);

        return esUserSkills;
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------


    @Test
    public void testSimpleCompareSearchEngine() {
        final String testName = "SIMPLE COMPARE SEARCH ENGINE";
        printStartTest(testName);

        int limit = 10;
        String testSearchData = "Werkstudent Java,Datenbanken,Jenkins";

        PageRequest pageRequest = new PageRequest(DEFAULT_OFFSET, limit);

        SearchQuery searchQuery = SearchQueryUtility.buildFullTextSearchMatchQuery_3(testSearchData, pageRequest);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse simpleSearchResponse =
                getEsClient().prepareSearch(EsConstants.INDEX_INTELLIJOB).setQuery(searchQuery.getQuery())
                        .setExplain(true).setSize(limit).get();
        Assert.assertNotNull(simpleSearchResponse);
        Assert.assertTrue("Hits should not be empty!", simpleSearchResponse.getHits().getTotalHits() > 0);

        printFullTextExplain(testSearchData, simpleSearchResponse, limit);
        printEndTest(testName);

        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------------------");
        List<EsUserSkills> userSkills = new ArrayList<>();
        userSkills.add(createEsUserSkills("1", user.getId(), "Werkstudent Java", 5.0f));
        userSkills.add(createEsUserSkills("2", user.getId(), "Datenbanken", 3.0f));
        userSkills.add(createEsUserSkills("3", user.getId(), "Jenkins", 3.0f));

        searchQuery = SearchQueryUtility.buildBoolQueryAndBoostRatingFieldUsingEsUserSkills_Final(userSkills, DEFAULT_OFFSET,
                limit);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse complexSearchResponse = getEsClient().prepareSearch(EsConstants.INDEX_INTELLIJOB)
                .setQuery(searchQuery.getQuery()).setExplain(true).setSize(limit).get();
        Assert.assertNotNull(complexSearchResponse);
        Assert.assertTrue("Hits should not be empty.", complexSearchResponse.getHits().getTotalHits() > 0);

        printFullTextExplainUsingElastic(userSkills, complexSearchResponse, limit);
        printSearchComparing(simpleSearchResponse.getHits(),complexSearchResponse.getHits(),limit);
        printEndTest(testName);
    }

    @Test
    public void testComplexCompareSearchEngine() {
        final String testName = "COMPLEX COMPARE SEARCH ENGINE";
        printStartTest(testName);

        int limit = 10;
        List<EsUserSkills> userSkills = esUserSkillsRepository.findByUserId(user.getId());
        String testSearchData = createSimpleSearchText(userSkills);

        PageRequest pageRequest = new PageRequest(DEFAULT_OFFSET, limit);

        SearchQuery searchQuery = SearchQueryUtility.buildFullTextSearchMatchQuery_3(testSearchData, pageRequest);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse simpleSearchResponse =
                getEsClient().prepareSearch(EsConstants.INDEX_INTELLIJOB).setQuery(searchQuery.getQuery())
                        .setExplain(true).setSize(limit).get();
        Assert.assertNotNull(simpleSearchResponse);
        Assert.assertTrue("Hits should not be empty!", simpleSearchResponse.getHits().getTotalHits() > 0);

        printFullTextExplain(testSearchData, simpleSearchResponse, limit);
        printEndTest(testName);

        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------------------");

        searchQuery = SearchQueryUtility.buildBoolQueryAndBoostRatingFieldUsingEsUserSkills_Final(userSkills, DEFAULT_OFFSET,
                limit);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse complexSearchResponse = getEsClient().prepareSearch(EsConstants.INDEX_INTELLIJOB)
                .setQuery(searchQuery.getQuery()).setExplain(true).setSize(limit).get();
        Assert.assertNotNull(complexSearchResponse);
        Assert.assertTrue("Hits should not be empty.", complexSearchResponse.getHits().getTotalHits() > 0);

        printFullTextExplainUsingElastic(userSkills, complexSearchResponse, limit);
        printSearchComparing(simpleSearchResponse.getHits(),complexSearchResponse.getHits(),limit);
        printEndTest(testName);
    }

    @Test
    @Ignore("This test is broken, Please fix it.")
    public void testBuildAutocompleteKnowledgeQuery() throws Exception {
        final String testName = "AUTOCOMPLETE KNOWLEDGE QUERY ";
        printStartTest(testName);

        String searchData = "Java";
        SearchQuery searchQuery = SearchQueryUtility.buildAutocompleteKnowledgeQuery(searchData);
        Assert.assertNotNull(searchQuery);
        printQuery(searchQuery.getQuery());

        SearchResponse searchResponse =
                getEsClient().prepareSearch(EsConstants.INDEX_INTELLIJOB).setTypes("autocomplete")
                        .setQuery(searchQuery.getQuery()).setExplain(true).setSize(20).get();
        Assert.assertNotNull(searchResponse);
        Assert.assertTrue("Hits should not be empty.", searchResponse.getHits().getTotalHits() > 0);

        printFullTextExplain(searchData, searchResponse);
        printEndTest(testName);
    }

    private String createSimpleSearchText(List<EsUserSkills> userSkills) {
        StringBuilder sb = new StringBuilder();
        for(EsUserSkills esUserSkill : userSkills){
            sb.append(esUserSkill.getName()).append(",");
        }

        return sb.toString();
    }


    private void printQuery(QueryBuilder query) {
        System.out.println("----------------------------- QUERY  ----------------------------------------");
        System.out.println(query.toString());
        System.out.println("-----------------------------------------------------------------------------");
    }

    private void printSearchComparing(final SearchHits simpleSearchHit, final SearchHits complexSearchHit, int limit) {
        System.out.println("------------------------------ COMPARING HITS -----------------------------------");
        for (int i = 0; i < limit; i++) {
            System.out.println("---------------------------  SIMPLE -------------------------------------------");
            int index = i + 1;
            SearchHit seample = simpleSearchHit.getAt(i);
            String receivedDate = formatDate((Long) seample.getSource().get(Constants.DB_FIELD_RECEIVED_DATE));

            StringBuilder firstLine = new StringBuilder();
            firstLine.append(index + " Hit: ");
            firstLine.append("score(").append(seample.getScore()).append(") | ");
            firstLine.append("receivedDate(").append(receivedDate).append(") | ");
            firstLine.append("name(").append(seample.getSource().get("name")).append(") | ");
            firstLine.append("id(").append(seample.getSource().get("id")).append(") ");
            System.out.println(firstLine);

            System.out.println("---------------------------  COMPLEX -------------------------------------------");
            SearchHit complex = complexSearchHit.getAt(i);
            receivedDate = formatDate((Long) complex.getSource().get(Constants.DB_FIELD_RECEIVED_DATE));
            StringBuilder secondLine = new StringBuilder();
            secondLine.append(index + " Hit: ");
            secondLine.append("score(").append(complex.getScore()).append(") | ");
            secondLine.append("receivedDate(").append(receivedDate).append(") | ");
            secondLine.append("name(").append(complex.getSource().get("name")).append(") | ");
            secondLine.append("id(").append(complex.getSource().get("id")).append(") ");
            System.out.println(secondLine);
            System.out.println("-----------------------------------------------------------------------------");
        }
    }

    private void printFullTextExplainUsingElastic(final Collection<EsUserSkills> skillRatingNodes,
                                                  final SearchResponse searchResponse) {
        printFullTextExplainUsingElastic(skillRatingNodes, searchResponse, Constants.DB_RESULT_LIMIT);
    }

    private void printFullTextExplainUsingElastic(final Collection<EsUserSkills> skillRatingNodes,
                                                  final SearchResponse searchResponse, int limit) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (EsUserSkills skillRatingNode : skillRatingNodes) {
            sb.append("{name: ").append(skillRatingNode.getName()).append(",");
            sb.append("rating: ").append(skillRatingNode.getRating()).append("}");
        }
        sb.append("]");

        printFullTextExplain(sb.toString(), searchResponse, limit);
    }

    private void printFullTextExplain(final String testSearchData, final SearchResponse searchResponse) {
        printFullTextExplain(testSearchData, searchResponse, Constants.DB_RESULT_LIMIT);
    }

    private void printFullTextExplain(final String testSearchData, final SearchResponse searchResponse, int limit) {
        System.out.println("------------------------------ RESPONSE -------------------------------------");
        System.out.println("Job size: " + countAllJobDetails);
        System.out.println("Search term: " + testSearchData);
        System.out.println("Hits: " + searchResponse.getHits().totalHits());
        System.out.println("MaxScore: " + searchResponse.getHits().getMaxScore());
        for (int i = 0; i < limit; i++) {
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