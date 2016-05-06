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
 * Job Controller.
 */
/**
 * Mail Table Controller.
 */
intelliJobControllers.controller(
        'JobDetailTableCtrl',
        [
            '$scope',
            '$rootScope',
            '$location',
            '$http',
            '$cookieStore',
            '$routeParams',
            '$route',
            'JobDetailServices',
            function ($scope, $rootScope, $location, $http, $cookieStore, $routeParams, $route, JobDetailServices) {
                $scope.showSimpleSearchDialog = false;
                $scope.showComplexSearchDialog = false;

                $scope.jobDetailTableCurrentPage = 1;
                $scope.jobDetailTableNumPerPage = 50;
                $scope.jobDetailTableMaxPage = 10;
                $scope.showPagination = false;
                $scope.jobDetails;
                $scope.readonly = $rootScope.globalUser.userId == null;
                var pageIndex = $scope.jobDetailTableCurrentPage - 1;
                //watch state should not be active by init page, to avoid 2 get request!
                $scope.isWatchActive = false;
                JobDetailServices.getViewModel().success(function (response) {
                    $scope.words = $scope.createTagClouds(response.userSkills);
                    $scope.user = response.userData;
                    $scope.readonly = $scope.user.userId == null;
                    $scope.searchEngine = $scope.user.profileData.searchEngine;
                    $scope.switchSearchEngine($scope.searchEngine);
                    $scope.searchData = response.userData.simpleSearchField;

                    $scope.jobDetails = response.tableData.jobDetails;
                    $scope.jobDetailTableTotalItems = response.tableData.totalItemSize;
                    $scope.showPagination = true;
                    //Set watch on pagination numbers
                    $scope.$watch('jobDetailTableCurrentPage + jobDetailTableNumPerPage', function () {
                        if ($scope.isWatchActive) {
                            pageIndex = $scope.jobDetailTableCurrentPage - 1;
                            if ($scope.searchEngine === 'SIMPLE') {
                                JobDetailServices.getJobDetailPageBySearchFilterAndSearchData($scope.searchEngine, $scope.searchData, pageIndex, $scope.jobDetailTableNumPerPage).success(function (response) {
                                    $scope.jobDetails = response.jobDetails;
                                }).error(function (error) {
                                    console.log(error);
                                });
                            } else {
                                JobDetailServices.getJobDetailPage($scope.searchEngine, pageIndex, $scope.jobDetailTableNumPerPage).success(function (response) {
                                    $scope.jobDetails = response.jobDetails;
                                }).error(function (error) {
                                    console.log(error);
                                });
                            }
                        }
                        $scope.isWatchActive = true;
                    });
                }).error(function (error) {
                    console.log(error);
                });


                /**
                 * Show job content in plain text.
                 */
                $scope.showJobText = function (jobDetailId) {
                    $location.path("/intellijob/jobdetails/" + jobDetailId + "/detail");
                };

                /**
                 * Update read state.
                 */
                $scope.updateOneReadState = function (jobDetailId, newReadState) {
                    JobDetailServices.updateOneReadState(jobDetailId, newReadState).success(function (response) {
                        for (var i = 0; i < $scope.jobDetails.length; i++) {
                            if ($scope.jobDetails[i].jobDetailId === jobDetailId) {
                                $scope.jobDetails[i].read = newReadState;
                            }
                        }
                        if (newReadState) {
                            $rootScope.success = 'Arbeitsangebot wurde als gelesen markiert!';
                        } else {
                            $rootScope.success = $rootScope.success = 'Mark as unread!';
                        }
                    }).error(function (error) {
                        console.log(error);
                    });
                };

                /**
                 * Update read state.
                 */
                $scope.updateAllReadState = function (newReadState) {
                    var ids = [];
                    for (var i = 0; i < $scope.jobDetails.length; i++) {
                        ids.push($scope.jobDetails[i].jobDetailId);
                    }
                    JobDetailServices.updateAllReadState(ids, newReadState).success(function (response) {
                        //var jobDetailArr = eval($scope.jobDetails);
                        for (var i = 0; i < $scope.jobDetails.length; i++) {
                            $scope.jobDetails[i].read = newReadState;
                        }
                        if (newReadState) {
                            $rootScope.success = 'Alle Arbeitsangebote wurden als gelesen markiert!';
                        } else {
                            $rootScope.success = $rootScope.success = 'All mark as unread!';
                        }
                    }).error(function (error) {
                        console.log(error);
                    });
                };


                /**
                 * Delete job detail.
                 */
                $scope.deleteJobDetail = function (jobDetailId) {
                    JobDetailServices.deleteById(jobDetailId).success(function (response) {
                        var deletedJobDetailId = response.jobDetailId;
                        var index = -1;
                        var jobDetailArr = eval($scope.jobDetails);
                        for (var i = 0; i < jobDetailArr.length; i++) {
                            if (jobDetailArr[i].jobDetailId === deletedJobDetailId) {
                                index = i;
                                break;
                            }
                        }
                        if (index === -1) {
                            alert("Something gone wrong");
                        }
                        $scope.jobDetails.splice(index, 1);
                        $rootScope.success = "Arbeitsdaten wurden erfolgreich gelöscht! (Id - " + jobDetailId + " )";
                    }).error(function (error) {
                        console.log(error);
                    });
                };

                /**
                 * Update search engine.
                 */
                $scope.createTagClouds = function (userSkills) {
                    var result = [];
                    if (userSkills) {
                        for (var i = 0; i < userSkills.length; i++) {
                            var tagCloud = {};
                            tagCloud.text = userSkills[i].name;
                            tagCloud.weight = userSkills[i].rating * 10;
                            if (userSkills[i].parent) {
                                tagCloud.weight = userSkills[i].rating
                            }
                            result.push(tagCloud);
                        }
                    }
                    return result;
                };

                /**
                 * Update search engine.
                 */
                $scope.changeSearchEngine = function (selectedSearchEngine) {
                    $scope.user.profileData.searchEngine = selectedSearchEngine;
                    $scope.switchSearchEngine(selectedSearchEngine, true);
                };

                /**
                 * Update search engine.
                 */
                $scope.search = function () {
                    $scope.isWatchActive = false;
                    var pageIndex = 0;
                    JobDetailServices.getJobDetailPageBySearchFilterAndSearchData($scope.searchEngine, $scope.searchData, pageIndex, $scope.jobDetailTableNumPerPage).success(function (response) {
                        $scope.jobDetails = response.jobDetails;
                        $scope.jobDetailTableCurrentPage = 1;
                        $scope.jobDetailTableTotalItems = response.totalItemSize;
                    }).error(function (error) {
                        console.log(error);
                    });
                };

                /**
                 * Select new search engine and deactivate old engine.
                 */
                $scope.switchSearchEngine = function (selectedSearchEngine, fire) {
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

                    if (fire) {
                        $scope.isWatchActive = false;
                        var pageIndex = 0;
                        JobDetailServices.getJobDetailPage(selectedSearchEngine, pageIndex, $scope.jobDetailTableNumPerPage).success(function (response) {
                            $scope.jobDetails = response.jobDetails;
                            $scope.jobDetailTableCurrentPage = 1;
                            $scope.jobDetailTableTotalItems = response.totalItemSize;
                            $scope.searchEngine = selectedSearchEngine;
                            $scope.searchData = $scope.user.simpleSearchField;
                        }).error(function (error) {
                            console.log(error);
                        });
                    }
                };
            }
        ])
;
