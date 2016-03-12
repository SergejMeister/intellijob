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
 * Job link table Controller.
 */
intelliJobControllers.controller(
        'JobLinkTableCtrl',
        [
            '$scope',
            '$rootScope',
            '$location',
            '$http',
            '$cookieStore',
            '$routeParams',
            '$route',
            'JobLinkServices',
            function ($scope, $rootScope, $location, $http, $cookieStore, $routeParams, $route, JobLinkServices) {
                $scope.jobLinksTableCurrentPage = 1;
                $scope.jobLinksTableNumPerPage = 50;
                $scope.jobLinksTableMaxPage = 10;
                $scope.showPagination = false;
                $scope.jobLinks;
                var pageIndex = $scope.jobLinksTableCurrentPage - 1;

                //watch state should not be active by init page, to avoid 2 get request!
                var isWatchActive = false;
                JobLinkServices.getJobLinksPage(pageIndex, $scope.jobLinksTableNumPerPage).success(function (response) {
                    $scope.jobLinks = response.jobLinks;
                    $scope.jobLinksTableTotalItems = response.totalItemSize;
                    $scope.showPagination = true;
                    //Set watch on pagination numbers
                    $scope.$watch('jobLinksTableCurrentPage + jobLinksTableNumPerPage', function () {
                        if (isWatchActive) {
                            pageIndex = $scope.jobLinksTableCurrentPage - 1;
                            JobLinkServices.getJobLinksPage(pageIndex, $scope.jobLinksTableNumPerPage).success(function (response) {
                                $scope.jobLinks = response.jobLinks;
                            }).error(function (error) {
                                console.log(error);
                            });
                        }
                        isWatchActive = true;
                    });
                }).error(function (error) {
                    console.log(error);
                });

                /**
                 * download html content of given link.
                 */
                $scope.downloadById = function (jobLinkId) {
                    JobLinkServices.downloadById(jobLinkId).success(function (response) {
                        for (var i = 0; i < $scope.jobLinks.length; i++) {
                            if ($scope.jobLinks[i].jobLinkId === response.jobLinkId) {
                                $scope.jobLinks[i].downloaded = true;
                                $rootScope.success = "Downloaded successfully!";
                                break;
                            }
                        }
                    }).error(function (error) {
                        console.log(error);
                    })
                };

                /**
                 * download html content of all links.
                 */
                $scope.downloadAll = function () {
                    JobLinkServices.downloadAll().success(function (response) {
                        for (var i = 0; i < $scope.jobLinks.length; i++) {
                                $scope.jobLinks[i].downloaded = true;
                        }
                        $rootScope.success = "Downloaded successfully!";
                    }).error(function (error) {
                        console.log(error);
                    })
                };

                /**
                 * Delete jobLink.
                 */
                $scope.deleteJobLink = function (jobLinkId) {
                    JobLinkServices.deleteById(jobLinkId).success(function (response) {
                        var deletedJobLinkId = response.jobLinkId;
                        var index = -1;
                        var jobLinkArr = eval($scope.jobLinks);
                        for (var i = 0; i < jobLinkArr.length; i++) {
                            if (jobLinkArr[i].jobLinkId === deletedJobLinkId) {
                                index = i;
                                break;
                            }
                        }
                        if (index === -1) {
                            alert("Something gone wrong");
                        }
                        $scope.jobLinks.splice(index, 1);
                        $rootScope.success = "Deleted successfully! (Id - " + deletedJobLinkId + " )";
                    }).error(function (error) {
                        console.log(error);
                    });
                };
            }]);
