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
import com.intellijob.domain.skills.SkillCategory;
import com.intellijob.domain.skills.SkillKnowledge;
import com.intellijob.domain.skills.SkillNode;
import com.intellijob.enums.SkillCategoryEnum;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class SkillKnowledgeRepositoryTest extends BaseTester {

    @Autowired
    private SkillCategoryRepository skillCategoryRepository;

    @Autowired
    private SkillKnowledgeRepository skillKnowledgeRepository;

    @Test
    public void testSave() {
        SkillKnowledge skillKnowledge = init();
        skillKnowledgeRepository.save(skillKnowledge);

        SkillKnowledge testFind = skillKnowledgeRepository.findFirstByOrderByIdAsc();
        Assert.assertNotNull(testFind);
        Assert.assertFalse("Should not be empty!", testFind.getKnowledges().isEmpty());
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
