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
 * Mail Table Controller.
 */
intelliJobControllers.controller(
        'MailTableCtrl',
        [
            '$scope',
            '$rootScope',
            '$location',
            '$http',
            '$cookieStore',
            '$routeParams',
            '$route',
            'MailServices',
            function ($scope, $rootScope, $location, $http, $cookieStore, $routeParams, $route, MailServices) {
                $scope.mailTableCurrentPage = 1;
                $scope.mailTableNumPerPage = 50;
                $scope.mailTableMaxPage = 10;
                $scope.showPagination = false;
                $scope.mails;
                var pageIndex = $scope.mailTableCurrentPage - 1;
                //watch state should not be active by init page, to avoid 2 get request!
                var isWatchActive = false;
                MailServices.getMailPage(pageIndex, $scope.mailTableNumPerPage).success(function (response) {
                    $scope.mails = response.mails;
                    $scope.mailTableTotalItems = response.totalItemSize;
                    $scope.showPagination = true;
                    //Set watch on pagination numbers
                    $scope.$watch('mailTableCurrentPage + mailTableNumPerPage', function () {
                        if (isWatchActive) {
                            pageIndex = $scope.mailTableCurrentPage - 1;
                            MailServices.getMailPage(pageIndex, $scope.mailTableNumPerPage).success(function (response) {
                                $scope.mails = response.mails;
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
                $scope.showContext = function (mailId) {
                    $location.path("/intellijob/mails/" + mailId + "/content");
                    //window.open('/intellijob/mails/' + mailId);
                };

                /**
                 * Delete jobLink.
                 */
                $scope.deleteMail = function (mailId) {
                    MailServices.deleteById(mailId).success(function (response) {
                        var deletedMailId = response.id;
                        var index = -1;
                        var mailArr = eval($scope.mails);
                        for (var i = 0; i < mailArr.length; i++) {
                            if (mailArr[i].id === deletedMailId) {
                                index = i;
                                break;
                            }
                        }
                        if (index === -1) {
                            alert("Something gone wrong");
                        }
                        $scope.mails.splice(index, 1);
                        $rootScope.success = "E-Mail wurde erfolgreich gelöscht! (Id - " + deletedMailId + " )";
                    }).error(function (error) {
                        console.log(error);
                    });
                };

            }]);
