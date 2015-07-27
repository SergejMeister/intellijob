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
        'JobTableCtrl',
        [
            '$scope',
            '$rootScope',
            '$location',
            '$http',
            '$cookieStore',
            '$routeParams',
            '$route',
            'JobServices',
            function ($scope, $rootScope, $location, $http, $cookieStore, $routeParams, $route, JobServices) {
                $scope.jobTableCurrentPage = 1;
                $scope.jobTableNumPerPage = 50;
                $scope.jobTableMaxPage = 10;
                $scope.showPagination = false;
                $scope.jobs;
                var pageIndex = $scope.jobTableCurrentPage - 1;
                //watch state should not be active by init page, to avoid 2 get request!
                var isWatchActive = false;
                JobServices.getJobPage(pageIndex, $scope.jobTableNumPerPage).success(function (response) {
                    $scope.jobs = response.jobs;
                    $scope.jobTableTotalItems = response.totalItemSize;
                    $scope.showPagination = true;
                    //Set watch on pagination numbers
                    $scope.$watch('jobTableCurrentPage + jobTableNumPerPage', function () {
                        if (isWatchActive) {
                            pageIndex = $scope.jobTableCurrentPage - 1;
                            JobServices.getJobPage(pageIndex, $scope.jobTableNumPerPage).success(function (response) {
                                $scope.jobs = response.jobs;
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
                 * Get mail by given id.
                 */
                $scope.showJobContent = function (jobId) {
                    $location.path("/intellijob/jobs/" + jobId + "/content");
                };

                /**
                 * download html content of given link.
                 */
                $scope.extractDataById = function (jobId) {
                    JobServices.extractDataById(jobId).success(function (response) {
                        for (var i = 0; i < $scope.jobs.length; i++) {
                            if ($scope.jobs[i].jobId === response.jobId) {
                                $scope.jobs[i].extracted = true;
                                $rootScope.success = "Extracted successfully!";
                                break;
                            }
                        }
                    }).error(function (error) {
                        console.log(error);
                    })
                };

                /**
                 * Delete job.
                 */
                $scope.deleteJob = function (jobId) {
                    JobServices.deleteById(jobId).success(function (response) {
                        var deletedJobId = response.jobId;
                        var index = -1;
                        var jobArr = eval($scope.jobs);
                        for (var i = 0; i < jobArr.length; i++) {
                            if (jobArr[i].jobId === deletedJobId) {
                                index = i;
                                break;
                            }
                        }
                        if (index === -1) {
                            alert("Something gone wrong");
                        }
                        $scope.jobs.splice(index, 1);
                        $rootScope.success = "Deleted successfully! (Id - " + deletedJobId + " )";
                    }).error(function (error) {
                        console.log(error);
                    });
                };

            }
        ])
;
