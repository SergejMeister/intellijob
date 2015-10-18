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
 * Job Details Controller.
 */
intelliJobControllers.controller(
        'AuditCtrl',
        [
            '$scope',
            '$rootScope',
            '$location',
            '$http',
            '$cookieStore',
            '$routeParams',
            'AuditServices',
            function ($scope, $rootScope, $location, $http, $cookieStore, $routeParams, AuditServices) {
                $scope.auditHistoryTableCurrentPage = 1;
                $scope.auditHistoryTableNumPerPage = 5;
                $scope.auditHistoryTableMaxPage = 5;
                $scope.showPagination = false;
                $scope.historyAuditData;
                $scope.currentAuditData;
                var pageIndex = $scope.auditHistoryTableCurrentPage - 1;
                //watch state should not be active by init page, to avoid 2 get request!
                var isWatchActive = false;
                AuditServices.getAudit(pageIndex, $scope.auditHistoryTableNumPerPage).success(function (response) {
                    $scope.currentAuditData = response.currentAuditData;
                    $scope.historyAuditData = response.historyData;
                    $scope.auditHistoryTableTotalItems = response.totalItemSize;
                    if ($scope.auditHistoryTableTotalItems > $scope.auditHistoryTableNumPerPage) {
                        $scope.showPagination = true;
                    }

                    //Set watch on pagination numbers
                    $scope.$watch('auditHistoryTableCurrentPage + auditHistoryTableNumPerPage', function () {
                        if (isWatchActive) {
                            pageIndex = $scope.auditHistoryTableCurrentPage - 1;
                            AuditServices.getAudit(pageIndex, $scope.auditHistoryTableNumPerPage).success(function (response) {
                                $scope.historyAuditData = response.historyAuditData;
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
                 * Save current audit data and add to the history.
                 */
                $scope.saveCurrentAuditData = function () {
                    AuditServices.save($scope.currentAuditData).success(function (response) {
                        var newHistoryAuditData = response;
                        $scope.historyAuditData.push(newHistoryAuditData);
                        $scope.auditHistoryTableTotalItems = $scope.auditHistoryTableTotalItems + 1;
                        $rootScope.success = "Current audit data is added to the history!";
                    }).error(function (error) {
                        console.log(error);
                    });
                };

                /**
                 * Delete history audit data.
                 */
                $scope.deleteAuditHistory = function (auditId) {
                    AuditServices.deleteById(auditId).success(function (response) {
                        var deletedAuditId = response.id;
                        var index = -1;
                        var historyArr = eval($scope.historyAuditData);
                        for (var i = 0; i < historyArr.length; i++) {
                            if (historyArr[i].id === deletedAuditId) {
                                index = i;
                                break;
                            }
                        }
                        if (index === -1) {
                            alert("Something gone wrong");
                        }
                        $scope.historyAuditData.splice(index, 1);
                        $rootScope.success = "Deleted successfully! (Id - " + auditId + " )";
                    }).error(function (error) {
                        console.log(error);
                    });
                };

                /**
                 * Show detail to given audit history.
                 */
                $scope.showAuditHistory = function (auditId) {
                    //not implemented!
                    //$location.path("/intellijob/jobs/" + jobId + "/content");
                };
            }]);
