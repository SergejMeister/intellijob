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

package com.intellijob.repository.skills;

import com.intellijob.BaseTester;
import com.intellijob.domain.localization.LocalizableObject;
import com.intellijob.domain.skills.SkillCategory;
import com.intellijob.domain.skills.SkillKnowledge;
import com.intellijob.domain.skills.SkillNode;
import com.intellijob.enums.SkillCategoryEnum;
import junit.framework.Assert;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class SkillKnowledgeRepositoryTest extends BaseTester {

    protected static final String KNOWLEDGES_RES_PATH = "skills/knowledgeRootCategories.txt";
    protected static final String KNOWLEDGES_JOB_FORMS_RES_PATH = "skills/jobforms.txt";
    protected static final String KNOWLEDGES_JOB_PLACES_RES_PATH = "skills/jobplaces.txt";
    protected static final String KNOWLEDGES_BRANCH_RES_PATH = "skills/branch.txt";
    protected static final String KNOWLEDGES_SUB_BUILDINNG_RES_PATH = "skills/sub_building.txt";
    protected static final String KNOWLEDGES_SUB_JOB_SERVICES_RES_PATH = "skills/sub_job_services.txt";
    protected static final String KNOWLEDGES_SUB_HOTEL_RES_PATH = "skills/sub_hotel_turismus.txt";
    protected static final String KNOWLEDGES_SUB_IT_RES_PATH = "skills/sub_it.txt";
    protected static final String KNOWLEDGES_SUB_GURDENING_RES_PATH = "skills/sub_gurdening.txt";
    protected static final String KNOWLEDGES_SUB_MEDIEN_RES_PATH = "skills/sub_medien.txt";
    protected static final String KNOWLEDGES_SUB_MANUFACTURE_RES_PATH = "skills/sub_manufacture.txt";
    protected static final String KNOWLEDGES_SUB_SPORT_RES_PATH = "skills/sub_sport.txt";
    protected static final String KNOWLEDGES_SUB_TRANSPORT_RES_PATH = "skills/sub_transport.txt";
    protected static final String KNOWLEDGES_SUB_GOODS_RES_PATH = "skills/sub_goods.txt";
    protected static final String KNOWLEDGES_SUB_ECONOMY_RES_PATH = "skills/sub_economy.txt";
    protected static final String KNOWLEDGES_SUB_RESEARCH_RES_PATH = "skills/sub_research.txt";
    protected static final String KNOWLEDGES_SUB_SUB_ARCHITECTURE_RES_PATH = "skills/sub_sub_architekture.txt";


    @Autowired
    private SkillCategoryRepository skillCategoryRepository;

    @Autowired
    private SkillKnowledgeRepository skillKnowledgeRepository;

    @Test
    public void testSave() {
        SkillCategory category = skillCategoryRepository.findByType(SkillCategoryEnum.KNOWLEDGE.getTypeId());
        SkillKnowledge skillKnowledge = new SkillKnowledge(category);
        List<SkillNode> knowledges = initAllKnowledges();
        skillKnowledge.setKnowledges(knowledges);
        skillKnowledgeRepository.save(skillKnowledge);

        SkillKnowledge testFind = skillKnowledgeRepository.findFirstByOrderByIdAsc();
        Assert.assertNotNull(testFind);
        Assert.assertFalse("Should not be empty!", testFind.getKnowledges().isEmpty());
    }

    private List<SkillNode> initAllKnowledges() {
        List<SkillNode> rootCategories = readResourceData(KNOWLEDGES_RES_PATH);
        List<SkillNode> newList =
                addSubSkills("Arbeits-, Einsatzformen", rootCategories, KNOWLEDGES_JOB_FORMS_RES_PATH);
        newList = addSubSkills("Arbeitsorte", newList, KNOWLEDGES_JOB_PLACES_RES_PATH);
        newList = addSubSkills("Bau, Architektur", newList, KNOWLEDGES_SUB_BUILDINNG_RES_PATH);
        addSubSkillsRecursiv("Architektur, Bauplanung, Bausachverständigenwesen", newList,
                KNOWLEDGES_SUB_SUB_ARCHITECTURE_RES_PATH);

        newList = addSubSkills("Branchen", newList, KNOWLEDGES_BRANCH_RES_PATH);
        newList = addSubSkills("Dienstleistungen", newList, KNOWLEDGES_SUB_JOB_SERVICES_RES_PATH);
        newList = addSubSkills("Hotel, Gaststätten, Tourismus", newList, KNOWLEDGES_SUB_HOTEL_RES_PATH);
        newList = addSubSkills("IT, DV, Computer", newList, KNOWLEDGES_SUB_IT_RES_PATH);
        newList = addSubSkills("Land-, Forstwirtschaft, Gartenbau", newList, KNOWLEDGES_SUB_GURDENING_RES_PATH);
        newList = addSubSkills("Medien, Kunst, Gestaltung", newList, KNOWLEDGES_SUB_MEDIEN_RES_PATH);
        newList = addSubSkills("Produktion, Verarbeitung, Technik", newList, KNOWLEDGES_SUB_MANUFACTURE_RES_PATH);
        newList = addSubSkills("Soziales, Erziehung, Gesundheit, Sport", newList, KNOWLEDGES_SUB_SPORT_RES_PATH);
        newList = addSubSkills("Ordners Transport, Verkehr", newList, KNOWLEDGES_SUB_TRANSPORT_RES_PATH);
        newList = addSubSkills("Waren- und Produktkenntnisse", newList, KNOWLEDGES_SUB_GOODS_RES_PATH);
        newList = addSubSkills("Wirtschaft, Verwaltung", newList, KNOWLEDGES_SUB_ECONOMY_RES_PATH);
        newList = addSubSkills("Wissenschaft, Forschung, Entwicklung", newList, KNOWLEDGES_SUB_RESEARCH_RES_PATH);
        return newList;
    }

    private List<SkillNode> addSubSkills(String skillName, List<SkillNode> list, String resPath) {
        List<SkillNode> result = new ArrayList<>();
        for (SkillNode skillNode : list) {
            if (skillNode.getName().equals(skillName)) {
                List<SkillNode> subSkills = readResourceData(resPath);
                skillNode.setNodes(subSkills);
                result.add(skillNode);
            } else {
                result.add(skillNode);
            }
        }

        return result;
    }

    private void addSubSkillsRecursiv(String skillName, List<SkillNode> list, String resPath) {
        if (list.isEmpty()) {
            return;
        }

        for (SkillNode skillNode : list) {
            if (skillNode.getName().equals(skillName)) {
                List<SkillNode> subSkills = readResourceData(resPath);
                skillNode.setNodes(subSkills);
                return;
            } else {
                //call recursiv
                addSubSkillsRecursiv(skillName, skillNode.getNodes(), resPath);
            }
        }
    }

    private List<SkillNode> readResourceData(String resourcePath) {
        List<SkillNode> result = new ArrayList<>();

        BufferedReader br;
        String line;
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(resourcePath)) {
            br = new BufferedReader(new InputStreamReader(inputStream, DEFAULT_ENCODING));
            while ((line = br.readLine()) != null) {
                LocalizableObject localizableObject = new LocalizableObject(line);
                SkillNode skillNode = new SkillNode(new ObjectId(), localizableObject);
                skillNode.setName(line);
                result.add(skillNode);
            }
        } catch (Exception e) {
            LOG.error("Error occurred while read file (" + resourcePath + ")", e);
            Assert.fail("Can not read file");
        }

        return result;
    }

    private SkillKnowledge init() {
        SkillCategory category = skillCategoryRepository.findByType(SkillCategoryEnum.KNOWLEDGE.getTypeId());
        SkillKnowledge skillKnowledges = new SkillKnowledge(category);

        List<SkillNode> knowledges = new ArrayList<>();
        // IT Start -------------------------------------------------------
        SkillNode itRootSkill = initSimpleSkillNode("IT,DV,Computer", "it knowledges");

        List<SkillNode> itSubNodes = new ArrayList<>();

        //operatinSystemKnowledges
        SkillNode operatinSystemKnowledges = initSimpleSkillNode("operation systems", null);

        List<SkillNode> osNodes = new ArrayList<>();
        osNodes.add(initSimpleSkillNode("windows", null));
        osNodes.add(initSimpleSkillNode("Linux,Unix", null));
        osNodes.add(initSimpleSkillNode("Mac OS", null));
        osNodes.add(initSimpleSkillNode("Android", null));

        operatinSystemKnowledges.setNodes(osNodes);

        itSubNodes.add(operatinSystemKnowledges);

        //database
        SkillNode databaseKnowledges = initSimpleSkillNode("Database, Database management", "all database knowledges");
        List<SkillNode> dbNodes = new ArrayList<>();
        dbNodes.add(initSimpleSkillNode("Oracle", null));
        dbNodes.add(initSimpleSkillNode("MySql", null));
        dbNodes.add(initSimpleSkillNode("PostgreeSql", null));
        dbNodes.add(initSimpleSkillNode("Mongo", null));

        databaseKnowledges.setNodes(dbNodes);

        itSubNodes.add(databaseKnowledges);

        itRootSkill.setNodes(itSubNodes);

        knowledges.add(itRootSkill);

        //IT End ------------------------------------------------------------


        //Building Start ------------------------------------------------------------
        SkillNode buildingRootSkill = initSimpleSkillNode("Building, architecture", "Building, architecture");

        List<SkillNode> buildingSubNodes = new ArrayList<>();
        SkillNode architekturAndPlainingKnowledges =
                initSimpleSkillNode("Architecture, planning, construction", "architecture and planning");
        List<SkillNode> plainingNodes = new ArrayList<>();
        plainingNodes.add(initSimpleSkillNode("architecture", null));
        plainingNodes.add(initSimpleSkillNode("acceptance of construction work", null));

        architekturAndPlainingKnowledges.setNodes(plainingNodes);
        buildingSubNodes.add(architekturAndPlainingKnowledges);

        SkillNode buildingConstructionKnowledges = initSimpleSkillNode("Baubetrieb, Bauabrechnung", null);
        List<SkillNode> buildingConstructionNodes = new ArrayList<>();
        buildingConstructionNodes.add(initSimpleSkillNode("Aufmaß", null));
        buildingConstructionNodes.add(initSimpleSkillNode("Baubetrieb", null));
        buildingConstructionNodes.add(initSimpleSkillNode("Baukontrolle", null));
        buildingConstructionNodes.add(initSimpleSkillNode("Bauleitung", null));
        buildingConstructionNodes.add(initSimpleSkillNode("Baumaschinensachkunde", null));
        buildingConstructionNodes.add(initSimpleSkillNode("Industrialisiertes Bauen/ Bauverfahrenstechnik", null));
        buildingConstructionNodes.add(initSimpleSkillNode("Massenermittlung, Massenberechnung", null));

        buildingConstructionKnowledges.setNodes(buildingConstructionNodes);
        buildingSubNodes.add(buildingConstructionKnowledges);

        buildingRootSkill.setNodes(buildingSubNodes);

        knowledges.add(buildingRootSkill);
        //Building End ------------------------------------------------------------

        skillKnowledges.setKnowledges(knowledges);

        return skillKnowledges;

    }

    private SkillNode initSimpleSkillNode(String name, String description) {
        SkillNode skillNode = new SkillNode();
        skillNode.setName(name);
        skillNode.setDescription(description);
        return skillNode;
    }


}
