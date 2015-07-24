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
                $scope.jobDetailTableCurrentPage = 1;
                $scope.jobDetailTableNumPerPage = 50;
                $scope.jobDetailTableMaxPage = 10;
                $scope.showPagination = false;
                $scope.jobDetails;
                var pageIndex = $scope.jobDetailTableCurrentPage - 1;
                //watch state should not be active by init page, to avoid 2 get request!
                var isWatchActive = false;
                JobDetailServices.getJobDetailPage(pageIndex, $scope.jobDetailTableNumPerPage).success(function (response) {
                    $scope.jobDetails = response.jobDetails;
                    $scope.jobDetailTableTotalItems = response.totalItemSize;
                    $scope.showPagination = true;
                    //Set watch on pagination numbers
                    $scope.$watch('jobDetailTableCurrentPage + jobDetailTableNumPerPage', function () {
                        if (isWatchActive) {
                            pageIndex = $scope.jobDetailTableCurrentPage - 1;
                            JobDetailServices.getJobDetailPage(pageIndex, $scope.jobDetailTableNumPerPage).success(function (response) {
                                $scope.jobDetails = response.jobDetails;
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
                $scope.showJobText = function (jobDetailId) {
                    $location.path("/intellijob/jobdetails/" + jobDetailId + "/detail");
                };

                /**
                 * Get mail by given id.
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
                        $rootScope.success = "Deleted successfully! (Id - " + jobDetailId + " )";
                    }).error(function (error) {
                        console.log(error);
                    });
                };
            }
        ])
;
