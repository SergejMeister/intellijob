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

package com.intellijob.controllers.impl;

import com.intellijob.controllers.UserController;
import com.intellijob.domain.Profile;
import com.intellijob.domain.User;
import com.intellijob.domain.skills.SkillKnowledge;
import com.intellijob.domain.skills.SkillNode;
import com.intellijob.domain.skills.SkillRatingNode;
import com.intellijob.elasticsearch.domain.EsUserSkills;
import com.intellijob.elasticsearch.repository.EsUserSkillsRepository;
import com.intellijob.exceptions.NotMailSyncException;
import com.intellijob.exceptions.UserNotFoundException;
import com.intellijob.repository.skills.SkillKnowledgeRepository;
import com.intellijob.repository.user.UserProfileRepository;
import com.intellijob.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Represents profile controllers.
 */
@Controller
public class UseControllerImpl implements UserController {

    private final static Logger LOG = LoggerFactory.getLogger(UseControllerImpl.class);

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillKnowledgeRepository skillKnowledgeRepository;

    @Autowired
    private EsUserSkillsRepository esUserSkillsRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Profile updateMailSyncDate(User user) {
        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
        }

        profile.setLastMailSyncDate(new Date());
        user.setProfile(profile);

        User savedUser = userRepository.save(user);
        return savedUser.getProfile();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getLastMailSyncDate(String userId) throws UserNotFoundException, NotMailSyncException {
        User user = userProfileRepository.findByUserId(userId);
        if (user == null) {
            throw new UserNotFoundException();
        }

        return getProfile(user).getLastMailSyncDate();
    }

    private Profile getProfile(User user) throws NotMailSyncException {
        Profile profile = user.getProfile();
        if (profile == null || profile.getLastMailSyncDate() == null) {
            throw new NotMailSyncException();
        }

        return profile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getLastMailSyncDate() throws UserNotFoundException, NotMailSyncException {
        User user = getUniqueUser();
        return getProfile(user).getLastMailSyncDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUniqueUser() throws UserNotFoundException {
        List<User> users = userRepository.findAll();
        if (users != null && users.size() == 1) {
            return users.get(0);
        }

        throw new UserNotFoundException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser(String userId) throws UserNotFoundException {
        User user = userRepository.findOne(userId);
        if (user == null) {
            LOG.error("No user for id: " + userId);
            throw new UserNotFoundException();
        }
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User save(User user) {
        User createUser = userRepository.save(user);
        createUserSkillsIndexes(user);
        return createUser;
    }

    private void createUserSkillsIndexes(User user) {
        final SkillKnowledge suppordedSkillKnowledges = skillKnowledgeRepository.findFirstByOrderByIdAsc();

        List<EsUserSkills> esUserSkills = user.getSkills().getLanguages().stream()
                .map(skillRatingNode -> mapSkillRatingNodeTo(user.getId(), skillRatingNode, Boolean.FALSE))
                .collect(Collectors.toList());

        esUserSkills.addAll(user.getSkills().getPersonalStrengths().stream()
                .map(skillRatingNode -> mapSkillRatingNodeTo(user.getId(), skillRatingNode, Boolean.FALSE))
                .collect(Collectors.toList()));

        List<SkillNode> parentNodes = new ArrayList<>();
        for (SkillRatingNode knowledge : user.getSkills().getKnowledges()) {
            if (knowledge.getSkillNode().getId() == null || knowledge.getSkillNode().getId().length() == 0) {
                //user specific skills. this skills does't exist in database.
                esUserSkills.add(mapSkillRatingNodeTo(user.getId(), knowledge, Boolean.FALSE));
            } else {
                //supported skills, found all parents
                esUserSkills.add(mapSkillRatingNodeTo(user.getId(), knowledge, Boolean.FALSE));
                parentNodes.addAll(findAllParents(suppordedSkillKnowledges.getKnowledges(), knowledge));
            }
        }

        esUserSkills.addAll(createParentsEsUserSkills(user.getId(), parentNodes));

        List<EsUserSkills> oldUserIndexes = esUserSkillsRepository.findByUserId(user.getId());
        esUserSkillsRepository.delete(oldUserIndexes);
        esUserSkillsRepository.save(esUserSkills);
    }

    private EsUserSkills mapSkillRatingNodeTo(String userId, SkillRatingNode skillRatingNode, Boolean isParent) {
        EsUserSkills esUserSkills = new EsUserSkills(skillRatingNode.getSkillNode().getId(), userId);
        esUserSkills.setName(skillRatingNode.getSkillNode().getName());
        esUserSkills.setRating(skillRatingNode.getRating());
        esUserSkills.setParent(isParent);

        return esUserSkills;
    }

    private EsUserSkills mapSkillNodeTo(String userId, SkillNode skillNode, Boolean isParent) {
        EsUserSkills esUserSkills = new EsUserSkills(skillNode.getId(), userId);
        esUserSkills.setName(skillNode.getName());
        esUserSkills.setRating(1);
        esUserSkills.setParent(isParent);

        return esUserSkills;
    }

    private Collection<EsUserSkills> createParentsEsUserSkills(final String userId, final List<SkillNode> parentNodes) {
        Map<String, EsUserSkills> ratingNodesMap = new HashMap<>();
        for (SkillNode skillNode : parentNodes) {
            EsUserSkills esUserSkills;
            if (ratingNodesMap.containsKey(skillNode.getId())) {
                esUserSkills = ratingNodesMap.get(skillNode.getId());
                esUserSkills.increaseRating(1.0f);
            } else {
                esUserSkills = mapSkillNodeTo(userId, skillNode, Boolean.TRUE);
            }
            ratingNodesMap.put(skillNode.getId(), esUserSkills);
        }

        return ratingNodesMap.values();
    }

    private List<SkillNode> findAllParents(final List<SkillNode> skillKnowledges, final SkillRatingNode knowledge) {
        List<SkillNode> parentNodes = new ArrayList<>();
        return findAllParents(skillKnowledges, parentNodes, knowledge);
    }

    private List<SkillNode> findAllParents(final List<SkillNode> skillKnowledges, List<SkillNode> parentNodes,
                                           final SkillRatingNode knowledge) {

        for (SkillNode skillNode : skillKnowledges) {
            if (skillNode.getId().equals(knowledge.getSkillNode().getId())) {
                return parentNodes;
            } else {
                parentNodes.add(skillNode);
                List<SkillNode> founded = findAllParents(skillNode.getNodes(), parentNodes, knowledge);
                if (founded.size() > 0) {
                    return founded;
                }
            }
        }

        //not found
        if (!parentNodes.isEmpty()) {
            //remove last parent
            parentNodes.remove(parentNodes.size() - 1);
        }

        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUser(String userId) {
        userRepository.delete(userId);
    }

}
