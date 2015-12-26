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

/**
 * User Controllers.
 */
intelliJobControllers.controller(
        'UserCtrl',
        [
            '$scope',
            '$rootScope',
            '$location',
            '$http',
            '$cookieStore',
            '$routeParams',
            '$route',
            'UserServices',
            function ($scope, $rootScope, $location, $http, $cookieStore, $routeParams, $route, UserServices) {
                $scope.educationLevels = [
                    'Promotion',
                    'Hochschulabschluss - Master',
                    'Hochschulabschluss - Bachelor',
                    'Ausbildung',
                    'Handelsschule',
                    'Abitur',
                    'Mittlere Reife',
                    'Hauptschule',
                    'Ohne Schulabschluss',
                    'Andere'
                ];

                $scope.praxisLevels = [
                    'Schühler',
                    'Auszubildender',
                    'Student',
                    'Junior',
                    'Senior',
                    'Expert'
                ];

                $scope.showSimpleSearchDialog = false;
                $scope.showComplexSearchDialog = false;

                $scope.userSkillStatus = {
                    isEducationEmpty: true,
                    isKnowledgeEmpty: true,
                    isPersonEmpty: true,
                    isLanguageEmpty: true
                };

                // If user not valid, that means a new user should be created!
                // For this use case is readonly mode false, otherwise true!
                $scope.readonly = $rootScope.isUserValid();

                $scope.userKnowledges = [];
                $scope.userPersonalStrengths = [];
                $scope.userLanguages = [];

                UserServices.getViewUserModelById($rootScope.globalUser.userId).success(function (response) {
                    $scope.user = response.userData;
                    $scope.searchEngine = $scope.user.profileData.searchEngine;
                    $scope.switchSearchEngine($scope.searchEngine);

                    $scope.supportedKnowledges = response.supportedKnowledges;
                    if ($scope.user.knowledges && $scope.user.knowledges.length > 0) {
                        $scope.userKnowledges = $scope.user.knowledges;
                        $scope.userSkillStatus.isKnowledgeEmpty = false;
                    }

                    if ($scope.user.languages && $scope.user.languages.length > 0) {
                        $scope.userLanguages = $scope.user.languages;
                        $scope.userSkillStatus.isLanguageEmpty = false;
                    }

                    $scope.supportedPersonalStrengths = response.supportedPersonalStrengths;
                    if ($scope.user.personalStrengths && $scope.user.personalStrengths.length > 0) {
                        $scope.userPersonalStrengths = $scope.user.personalStrengths;
                        $scope.userSkillStatus.isPersonEmpty = false;
                    }
                }).error(function (error) {
                    console.log(error);
                });

                $scope.panelStatus = {
                    isEducationSkillOpen: open,
                    isEducationSkillDisabled: false,
                    isKnowledgeSkillOpen: false,
                    isKnowledgeSkillDisabled: false,
                    isPersonSkillOpen: false,
                    isPersonSkillDisabled: false,
                    isLanguageSkillOpen: false,
                    isLanguageSkillDisabled: false
                };

                $scope.deleteLanguage = function (index) {
                    $scope.userLanguages.splice(index, 1);
                };

                $scope.deletePersonalStrength = function (index) {
                    $scope.userPersonalStrengths.splice(index, 1);
                };

                $scope.deleteKnowledge = function (index) {
                    $scope.userKnowledges.splice(index, 1);
                };

                $scope.createAutoCompleteSkillRatingData = function (selectedSkill) {
                    var skillRatingData = {};
                    skillRatingData.skillData = {};
                    skillRatingData.rating = 1;
                    if (selectedSkill.originalObject) {
                        if (selectedSkill.originalObject.name && selectedSkill.originalObject.name !== '') {
                            skillRatingData.skillData = selectedSkill.originalObject;
                            return skillRatingData;
                        }
                    } else {
                        var skill = {};
                        skill.id = '';
                        skill.name = selectedSkill;
                        skillRatingData.skillData = skill;
                        return skillRatingData;
                    }
                };

                $scope.addLanguage = function (newUserLanguage) {
                    var skillRatingData = $scope.createAutoCompleteSkillRatingData(newUserLanguage);
                    $scope.userLanguages.push(skillRatingData);
                    $scope.selectedLanguage = null;
                };

                $scope.addKnowledge = function (newUserKnowledge) {
                    var skillRatingData = $scope.createAutoCompleteSkillRatingData(newUserKnowledge);
                    $scope.userKnowledges.push(skillRatingData);
                    $scope.selectedKnowledge = null;
                };

                /**
                 * Update search engine.
                 */
                $scope.changeSearchEngine = function (selectedSearchEngine) {
                    $scope.user.profileData.searchEngine = selectedSearchEngine;
                    $scope.switchSearchEngine(selectedSearchEngine);
                };

                /**
                 * Select new search engine and deactivate old engine.
                 */
                $scope.switchSearchEngine = function (selectedSearchEngine) {
                    if (selectedSearchEngine === 'SIMPLE') {
                        $scope.showSimpleSearchDialog = true;
                        $scope.showComplexSearchDialog = false;
                    } else if (selectedSearchEngine === 'COMPLEX') {
                        $scope.showSimpleSearchDialog = false;
                        $scope.showComplexSearchDialog = true;
                    } else {
                        $scope.showSimpleSearchDialog = false;
                        $scope.showComplexSearchDialog = false;
                    }
                };

                $scope.setReadOnlyMode = function (value) {
                    $scope.readonly = value;
                };

                /**
                 * Save user data.
                 */
                $scope.save = function (userData) {
                    userData.personalStrengths = $scope.userPersonalStrengths;
                    userData.knowledges = $scope.userKnowledges;
                    userData.languages = $scope.userLanguages;
                    UserServices.updateUser(userData).success(function (response) {
                        $rootScope.success = response.message;
                        $cookieStore.put('user', userData);
                        $scope.readonly = true;
                    }).error(function (error) {
                        console.log(error);
                    });
                };
            }
        ]);
