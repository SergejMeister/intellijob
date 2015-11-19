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

                $scope.showSimpleSearchDialog = false;
                $scope.showComplexSearchDialog = false;

                // If user not valid, that means a new user should be created!
                // For this use case is readonly mode false, otherwise true!
                $scope.readonly = $rootScope.isUserValid();

                var userLanguages = [];
                $scope.userLanguages = userLanguages;

                UserServices.getViewUserModelById($rootScope.globalUser.userId).success(function (response) {
                    $scope.user = response.userData;
                    $scope.searchEngine = $scope.user.profileData.searchEngine;
                    $scope.switchSearchEngine($scope.searchEngine);
                    $scope.supportedLanguages = response.supportedLanguages;
                    if ($scope.user.languages) {
                        $scope.userLanguages = $scope.user.languages;
                    }

                    $scope.displayTree = response.supportedPersonalStrengths;
                }).error(function (error) {
                    console.log(error);
                });

                $scope.panelStatus = {
                    isEducationSkillOpen: true,
                    isEducationSkillDisabled: false,
                    isKnowledgeSkillOpen: true,
                    isKnowledgeSkillDisabled: false,
                    isPersonSkillOpen: true,
                    isPersonSkillDisabled: false,
                    isLanguageSkillOpen: true,
                    isLanguageSkillDisabled: false
                };

                $scope.deleteLanguage = function (index) {
                    $scope.userLanguages.splice(index, 1);
                };

                $scope.addLanguage = function (newUserLanguage) {
                    var skillRatingData = {};
                    skillRatingData.skillData = {};
                    skillRatingData.rating = 1;

                    var language = newUserLanguage.originalObject;
                    if (language) {
                        if (language.name && language.name !== '') {
                            skillRatingData.skillData = language;
                            $scope.userLanguages.push(skillRatingData);
                        }
                    } else {
                        var language = {};
                        language.id = '';
                        language.name = newUserLanguage;
                        skillRatingData.skillData = language;
                        $scope.userLanguages.push(skillRatingData);
                    }

                    $scope.selectedLanguage = null;
                };

                /**
                 * Save user data.
                 */
                $scope.save = function (userData) {
                    userData.languages = $scope.userLanguages;
                    UserServices.updateUser(userData).success(function (response) {
                        $rootScope.success = response.message;
                        $cookieStore.put("user", userData);
                        $scope.readonly = true;
                    }).error(function (error) {
                        console.log(error);
                    });
                };

                /**
                 * search mails in mail box.
                 */
                $scope.changeSearchEngine = function (selectedSearchEngine) {
                    $scope.user.profileData.searchEngine = selectedSearchEngine;
                    $scope.switchSearchEngine(selectedSearchEngine);
                };

                /**
                 * search mails in mail box.
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

            }
        ]);
