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
import com.intellijob.domain.LocalizableObject;
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
    protected static final String KNOWLEDGES_SUB_SUB_CONSTRUCTION_RES_PATH = "skills/sub_sub_construction.txt";
    protected static final String KNOWLEDGES_SUB_SUB_CARPENTRY_RES_PATH = "skills/sub_sub_carpentry.txt";
    protected static final String KNOWLEDGES_SUB_SUB_TILING_RES_PATH = "skills/sub_sub_tiling.txt";
    protected static final String KNOWLEDGES_SUB_SUB_STAGING_RES_PATH = "skills/sub_sub_staging.txt";
    protected static final String KNOWLEDGES_SUB_SUB_HIGH_BUILDING_RES_PATH = "skills/sub_sub_high_building.txt";
    protected static final String KNOWLEDGES_SUB_SUB_PAINTING_RES_PATH = "skills/sub_sub_painting.txt";
    protected static final String KNOWLEDGES_SUB_SUB_PLASTER_WORK_RES_PATH = "skills/sub_sub_plasterwork.txt";
    protected static final String KNOWLEDGES_SUB_SUB_ROAD_WORKS_RES_PATH = "skills/sub_sub_roadworks.txt";
    protected static final String KNOWLEDGES_SUB_SUB_WELL_BUILDING_RES_PATH = "skills/sub_sub_well_building.txt";
    protected static final String KNOWLEDGES_SUB_SUB_MESSURING_RES_PATH = "skills/sub_sub_messuring.txt";
    protected static final String KNOWLEDGES_SUB_SUB_SEVERAGE_RES_PATH = "skills/sub_sub_severage.txt";
    protected static final String KNOWLEDGES_SUB_SUB_FUNERAL_RES_PATH = "skills/sub_sub_funeral.txt";
    protected static final String KNOWLEDGES_SUB_SUB_CALL_CENTER_RES_PATH = "skills/sub_sub_call_center.txt";
    protected static final String KNOWLEDGES_SUB_SUB_ENERGY_SUPPLY_RES_PATH = "skills/sub_sub_energy_supply.txt";
    protected static final String KNOWLEDGES_SUB_SUB_HOME_ECONOMY_RES_PATH = "skills/sub_sub_home_economy.txt";
    protected static final String KNOWLEDGES_SUB_SUB_CONGRESS_RES_PATH = "skills/sub_sub_congress.txt";
    protected static final String KNOWLEDGES_SUB_SUB_CLEANING_RES_PATH = "skills/sub_sub_cleaning.txt";
    protected static final String KNOWLEDGES_SUB_SUB_INTERPRETOR_RES_PATH = "skills/sub_sub_interpretor.txt";
    protected static final String KNOWLEDGES_SUB_SUB_LEASING_RES_PATH = "skills/sub_sub_leasing.txt";
    protected static final String KNOWLEDGES_SUB_SUB_SECURITY_RES_PATH = "skills/sub_sub_security.txt";
    protected static final String KNOWLEDGES_SUB_SUB_WATER_SYS_RES_PATH = "skills/sub_sub_water_system.txt";
    protected static final String KNOWLEDGES_SUB_SUB_GASTRONOMY_RES_PATH = "skills/sub_sub_gastronomy.txt";
    protected static final String KNOWLEDGES_SUB_SUB_HOTEL_RES_PATH = "skills/sub_sub_hotel.txt";
    protected static final String KNOWLEDGES_SUB_SUB_KITCHEN_RES_PATH = "skills/sub_sub_kitchen.txt";
    protected static final String KNOWLEDGES_SUB_SUB_COOKING_INTER_RES_PATH = "skills/sub_sub_cooking_inter.txt";
    protected static final String KNOWLEDGES_SUB_SUB_TOURISM_RES_PATH = "skills/sub_sub_tourism.txt";
    protected static final String KNOWLEDGES_SUB_SUB_FISHING_RES_PATH = "skills/sub_sub_fishing.txt";
    protected static final String KNOWLEDGES_SUB_SUB_FLORISTICS_RES_PATH = "skills/sub_sub_floristics.txt";
    protected static final String KNOWLEDGES_SUB_SUB_FORESTRY_RES_PATH = "skills/sub_sub_forestry.txt";
    protected static final String KNOWLEDGES_SUB_SUB_GURDENING_RES_PATH = "skills/sub_sub_gurdening.txt";
    protected static final String KNOWLEDGES_SUB_SUB_FARMING_RES_PATH = "skills/sub_sub_farming.txt";
    protected static final String KNOWLEDGES_SUB_SUB_ANIMAL_BREEDING_RES_PATH = "skills/sub_sub_animal_breeding.txt";
    protected static final String KNOWLEDGES_SUB_SUB_ZOOKEEPER_RES_PATH = "skills/sub_sub_zookeeper.txt";
    protected static final String KNOWLEDGES_SUB_SUB_VINICULTURE_RES_PATH = "skills/sub_sub_viniculture.txt";
    protected static final String KNOWLEDGES_SUB_SUB_IT_OS_RES_PATH = "skills/sub_sub_it_os.txt";
    protected static final String KNOWLEDGES_SUB_SUB_IT_DB_RES_PATH = "skills/sub_sub_it_db.txt";
    protected static final String KNOWLEDGES_SUB_SUB_IT_EDV_RES_PATH = "skills/sub_sub_it_edv.txt";
    protected static final String KNOWLEDGES_SUB_SUB_IT_CERT_RES_PATH = "skills/sub_sub_it_cert.txt";
    protected static final String KNOWLEDGES_SUB_SUB_IT_NET_RES_PATH = "skills/sub_sub_network.txt";
    protected static final String KNOWLEDGES_SUB_SUB_IT_NET_TECH_RES_PATH = "skills/sub_sub_net_tech.txt";
    protected static final String KNOWLEDGES_SUB_SUB_IT_NET_CERT_RES_PATH = "skills/sub_sub_it_net_cert.txt";
    protected static final String KNOWLEDGES_SUB_SUB_IT_PROG_RES_PATH = "skills/sub_sub_it_prog.txt";
    protected static final String KNOWLEDGES_SUB_SUB_IT_SOFT_RES_PATH = "skills/sub_sub_it_soft.txt";
    protected static final String KNOWLEDGES_SUB_SUB_IT_SOFT_COMMERC_RES_PATH = "skills/sub_sub_it_soft_commerc.txt";
    protected static final String KNOWLEDGES_SUB_SUB_IT_SOFT_TECH_RES_PATH = "skills/sub_sub_it_soft_tech.txt";
    protected static final String KNOWLEDGES_SUB_SUB_IT_SPEC_RES_PATH = "skills/sub_sub_it_spec.txt";
    protected static final String KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_BUILDING_RES_PATH = "skills/sub_sub_sub_it_soft_building.txt";
    protected static final String KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_FINANCE_RES_PATH = "skills/sub_sub_sub_it_soft_finance.txt";
    protected static final String KNOWLEDGES_SUB_SUB_SUB_IT_OFFICE_RES_PATH = "skills/sub_sub_sub_it_office.txt";
    protected static final String KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_MEDICINE_RES_PATH = "skills/sub_sub_sub_it_soft_medicine.txt";
    protected static final String KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_IMMOB_RES_PATH = "skills/sub_sub_sub_it_soft_immob.txt";
    protected static final String KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_JUSTICE_RES_PATH = "skills/sub_sub_sub_it_soft_justice.txt";
    protected static final String KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_PERSON_RES_PATH = "skills/sub_sub_sub_it_soft_person.txt";
    protected static final String KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_TRIP_RES_PATH = "skills/sub_sub_sub_it_soft_trip.txt";
    protected static final String KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_OTHER_RES_PATH = "skills/sub_sub_sub_it_soft_others.txt";
    protected static final String KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_CAD_RES_PATH = "skills/sub_sub_sub_it_soft_cad.txt";
    protected static final String KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_CNS_RES_PATH = "skills/sub_sub_sub_it_soft_cns.txt";
    protected static final String KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_ENGIN_RES_PATH = "skills/sub_sub_sub_it_soft_engin.txt";
    protected static final String KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_GEO_RES_PATH = "skills/sub_sub_sub_it_soft_geo.txt";
    protected static final String KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_GRAFIC_RES_PATH = "skills/sub_sub_sub_it_soft_grafic.txt";
    protected static final String KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_WEB_RES_PATH = "skills/sub_sub_sub_it_web.txt";
    protected static final String KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_TECH_OTHER_RES_PATH = "skills/sub_sub_sub_it_soft_tech_other.txt";
    protected static final String KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_SPS_RES_PATH = "skills/sub_sub_sib_it_soft_sps.txt";
    protected static final String KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_MATHE_RES_PATH = "skills/sub_sub_sub_it_soft_mathe.txt";


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

        List<SkillNode> newList = addSubSkills("Arbeits-, Einsatzformen", rootCategories, KNOWLEDGES_JOB_FORMS_RES_PATH);

        //Arbeitsorte
        newList = addSubSkills("Arbeitsorte", newList, KNOWLEDGES_JOB_PLACES_RES_PATH);

        //Bau, Architektur
        newList = addSubSkills("Bau, Architektur", newList, KNOWLEDGES_SUB_BUILDINNG_RES_PATH);
        addSubSkillsRecursiv("Architektur, Bauplanung, Bausachverständigenwesen", newList, KNOWLEDGES_SUB_SUB_ARCHITECTURE_RES_PATH);
        addSubSkillsRecursiv("Baubetrieb, Bauabrechnung", newList, KNOWLEDGES_SUB_SUB_CONSTRUCTION_RES_PATH);
        addSubSkillsRecursiv("Dachdeckerei, Zimmerei, Bautischlerei", newList, KNOWLEDGES_SUB_SUB_CARPENTRY_RES_PATH);
        addSubSkillsRecursiv("Fliesen-, Bodenlegerei, Raumausst./Rollladen-, Jalousiebau", newList, KNOWLEDGES_SUB_SUB_TILING_RES_PATH);
        addSubSkillsRecursiv("Gerüstbau, Baustellenvorbereitung, Sprengungen", newList, KNOWLEDGES_SUB_SUB_STAGING_RES_PATH);
        addSubSkillsRecursiv("Hochbau, Beton- u. Stahlbetonbau, Feuerung.-/Schornsteinbau", newList, KNOWLEDGES_SUB_SUB_HIGH_BUILDING_RES_PATH);
        addSubSkillsRecursiv("Malerei, Lackiererei, Glasbau", newList, KNOWLEDGES_SUB_SUB_PAINTING_RES_PATH);
        addSubSkillsRecursiv("Putz-, Dämm-, Isolierarbeiten", newList, KNOWLEDGES_SUB_SUB_PLASTER_WORK_RES_PATH);
        addSubSkillsRecursiv("Straßenbau, Gleisbau", newList, KNOWLEDGES_SUB_SUB_ROAD_WORKS_RES_PATH);
        addSubSkillsRecursiv("Tief- und Spezialtiefbau, Brunnenbau, Wasserbau", newList, KNOWLEDGES_SUB_SUB_WELL_BUILDING_RES_PATH);
        addSubSkillsRecursiv("Vermessung", newList,KNOWLEDGES_SUB_SUB_MESSURING_RES_PATH);

        //Branchen
        newList = addSubSkills("Branchen", newList, KNOWLEDGES_BRANCH_RES_PATH);

        //Dienstleistungen
        newList = addSubSkills("Dienstleistungen", newList, KNOWLEDGES_SUB_JOB_SERVICES_RES_PATH);
        addSubSkillsRecursiv("Abfall-, Abwasserentsorgung", newList, KNOWLEDGES_SUB_SUB_SEVERAGE_RES_PATH);
        addSubSkillsRecursiv("Bestattungswesen", newList, KNOWLEDGES_SUB_SUB_FUNERAL_RES_PATH);
        addSubSkillsRecursiv("Call Center", newList, KNOWLEDGES_SUB_SUB_CALL_CENTER_RES_PATH);
        addSubSkillsRecursiv("Energieversorgung", newList, KNOWLEDGES_SUB_SUB_ENERGY_SUPPLY_RES_PATH);
        addSubSkillsRecursiv("Hauswirtschaft, Ernährung, private Dienstleistungen", newList, KNOWLEDGES_SUB_SUB_HOME_ECONOMY_RES_PATH);
        addSubSkillsRecursiv("Messe-, Kongress-, Tagungswirtschaft", newList, KNOWLEDGES_SUB_SUB_CONGRESS_RES_PATH);
        addSubSkillsRecursiv("Reinigungsdienste", newList, KNOWLEDGES_SUB_SUB_CLEANING_RES_PATH);
        addSubSkillsRecursiv("Übersetzungs-, Dolmetscherdienste", newList, KNOWLEDGES_SUB_SUB_INTERPRETOR_RES_PATH);
        addSubSkillsRecursiv("Vermietung, Verleih", newList, KNOWLEDGES_SUB_SUB_LEASING_RES_PATH);
        addSubSkillsRecursiv("Wach- und Sicherheitsdienste", newList, KNOWLEDGES_SUB_SUB_SECURITY_RES_PATH);
        addSubSkillsRecursiv("Wasserversorgung", newList, KNOWLEDGES_SUB_SUB_WATER_SYS_RES_PATH);

        //Hotel, Gaststätten, Tourismus
        newList = addSubSkills("Hotel, Gaststätten, Tourismus", newList, KNOWLEDGES_SUB_HOTEL_RES_PATH);
        addSubSkillsRecursiv("Gastronomie - Service", newList, KNOWLEDGES_SUB_SUB_GASTRONOMY_RES_PATH);
        addSubSkillsRecursiv("Hotellerie", newList, KNOWLEDGES_SUB_SUB_HOTEL_RES_PATH);
        addSubSkillsRecursiv("Kochen, Küchentätigkeiten", newList, KNOWLEDGES_SUB_SUB_KITCHEN_RES_PATH);
        addSubSkillsRecursiv("Regionale und internationale Küche", newList, KNOWLEDGES_SUB_SUB_COOKING_INTER_RES_PATH);
        addSubSkillsRecursiv("Tourismus", newList, KNOWLEDGES_SUB_SUB_TOURISM_RES_PATH);

        //IT
        newList = addSubSkills("IT, DV, Computer", newList, KNOWLEDGES_SUB_IT_RES_PATH);
        addSubSkillsRecursiv("Betriebssysteme", newList, KNOWLEDGES_SUB_SUB_IT_OS_RES_PATH);
        addSubSkillsRecursiv("Datenbanken, Datenverwaltungssysteme", newList, KNOWLEDGES_SUB_SUB_IT_DB_RES_PATH);
        addSubSkillsRecursiv("EDV-Dienstleistungen", newList, KNOWLEDGES_SUB_SUB_IT_EDV_RES_PATH);
        addSubSkillsRecursiv("IT-Zertifikate", newList, KNOWLEDGES_SUB_SUB_IT_CERT_RES_PATH);
        addSubSkillsRecursiv("Netzprotokolle", newList, KNOWLEDGES_SUB_SUB_IT_NET_RES_PATH);
        addSubSkillsRecursiv("Netzwerktechnik", newList, KNOWLEDGES_SUB_SUB_IT_NET_TECH_RES_PATH);
        addSubSkillsRecursiv("Netzwerkzertifizierungen", newList, KNOWLEDGES_SUB_SUB_IT_NET_CERT_RES_PATH);
        addSubSkillsRecursiv("Programmiersprachen, Entwicklungsumgebungen", newList, KNOWLEDGES_SUB_SUB_IT_PROG_RES_PATH);
        addSubSkillsRecursiv("Softwareentwicklung, Programmierung", newList, KNOWLEDGES_SUB_SUB_IT_SOFT_RES_PATH);

        addSubSkillsRecursiv("Software - kaufmännische Anwendungsgebiete", newList, KNOWLEDGES_SUB_SUB_IT_SOFT_COMMERC_RES_PATH);
        //add subcategories for Software - kaufmännische Anwendungsgebiete
        addSubSkillsRecursiv("Bauabwicklungssoftware", newList, KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_BUILDING_RES_PATH);
        addSubSkillsRecursiv("Betriebswirtschafts-, Finanz- und Controllingsoftware", newList, KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_FINANCE_RES_PATH);
        addSubSkillsRecursiv("Bürokommunikation, MS-Office", newList, KNOWLEDGES_SUB_SUB_SUB_IT_OFFICE_RES_PATH);
        addSubSkillsRecursiv("Gesundheitsverwaltungs- und Praxissoftware", newList, KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_MEDICINE_RES_PATH);
        addSubSkillsRecursiv("Immobilienmanagement-Software", newList, KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_IMMOB_RES_PATH);
        addSubSkillsRecursiv("Kanzlei-, Justizverwaltungssoftware", newList, KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_JUSTICE_RES_PATH);
        addSubSkillsRecursiv("Personalverwaltungs- und -abrechnungssoftware", newList, KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_PERSON_RES_PATH);
        addSubSkillsRecursiv("Reiseinformations-, Reservierungs- und Gastronomiesysteme", newList, KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_TRIP_RES_PATH);
        addSubSkillsRecursiv("Sonstige Software - kaufmännische Anwendungsgebiete", newList, KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_OTHER_RES_PATH);

        addSubSkillsRecursiv("Software - technische Anwendungsgebiete", newList, KNOWLEDGES_SUB_SUB_IT_SOFT_TECH_RES_PATH);
        //add subcategories for Software - technische Anwendungsgebiete
        addSubSkillsRecursiv("CAD-/CAM-Anwendungen", newList, KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_CAD_RES_PATH);
        addSubSkillsRecursiv("CNC-, NC-Programme", newList, KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_CNS_RES_PATH);
        addSubSkillsRecursiv("Engineering- und Entwicklungssysteme", newList, KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_ENGIN_RES_PATH);
        addSubSkillsRecursiv("Geoinformationssysteme, Kartografie-, Vermessungssoftware", newList, KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_GEO_RES_PATH);
        addSubSkillsRecursiv("Grafik-, Bildbearbeitungs-, DTP-, Multimediasoftware", newList, KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_GRAFIC_RES_PATH);
        addSubSkillsRecursiv("HTML-Editoren, Webdesign- und Webcontentsoftware, Browser", newList, KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_WEB_RES_PATH);
        addSubSkillsRecursiv("Sonstige Software - technische Anwendungsgebiete", newList, KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_TECH_OTHER_RES_PATH);
        addSubSkillsRecursiv("SPS-Software", newList, KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_SPS_RES_PATH);
        addSubSkillsRecursiv("Mathematiksoftware", newList, KNOWLEDGES_SUB_SUB_SUB_IT_SOFT_MATHE_RES_PATH);

        addSubSkillsRecursiv("Spezielle Informatikgebiete", newList, KNOWLEDGES_SUB_SUB_IT_SPEC_RES_PATH);

        //Land-, Forstwirtschaft, Gartenbau
        newList = addSubSkills("Land-, Forstwirtschaft, Gartenbau", newList, KNOWLEDGES_SUB_GURDENING_RES_PATH);
        addSubSkillsRecursiv("Fischerei, Fischzucht", newList, KNOWLEDGES_SUB_SUB_FISHING_RES_PATH);
        addSubSkillsRecursiv("Floristik", newList, KNOWLEDGES_SUB_SUB_FLORISTICS_RES_PATH);
        addSubSkillsRecursiv("Forstwirtschaft, Jagd", newList, KNOWLEDGES_SUB_SUB_FORESTRY_RES_PATH);
        addSubSkillsRecursiv("Gartenbau", newList, KNOWLEDGES_SUB_SUB_GURDENING_RES_PATH);
        addSubSkillsRecursiv("Landwirtschaft", newList, KNOWLEDGES_SUB_SUB_FARMING_RES_PATH);
        addSubSkillsRecursiv("Landwirtschaftliche Tierhaltung, Tierzucht", newList, KNOWLEDGES_SUB_SUB_ANIMAL_BREEDING_RES_PATH);
        addSubSkillsRecursiv("Tierpflege, Pferdehaltung", newList, KNOWLEDGES_SUB_SUB_ZOOKEEPER_RES_PATH);
        addSubSkillsRecursiv("Weinbau", newList, KNOWLEDGES_SUB_SUB_VINICULTURE_RES_PATH);

        //Medien, Kunst, Gestaltung
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
