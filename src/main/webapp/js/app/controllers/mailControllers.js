/*
 * Copyright 2015 Sergej Meister
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
* Index Controller.
*/
intelliJobControllers.controller(
        'MailCtrl',
        [
            '$scope',
            '$rootScope',
            '$location',
            '$http',
            '$cookieStore',
            '$routeParams',
            '$route',
            'MailServices',
            function ($scope,$rootScope,$location,$http,$cookieStore,$routeParams,$route,MailServices) {

                $scope.mailAccounts = [
                    {name:'gmail'},
                    {name:'rambler'}
                ];
                $scope.selectedMailAccount = $scope.mailAccounts[0];


                $scope.mailUsername="";
                $scope.mailPassword="";
                /**
                 * remove image from shopping cart
                 */
                $scope.search = function (mailUsername, mailPassword) {
                    var requestMailData = {};
                    requestMailData.username = mailUsername;
                    requestMailData.password = mailPassword;
                    requestMailData.mailAccount = $scope.selectedMailAccount.name;
                    MailServices.searchMail(requestMailData).success(function (responseMailData) {
                        $rootScope.success = responseMailData.message;
                    }).error(function (data) {
                        //$scope.log(data.errors);
                        if($scope.selectedMailAccount.name == "gmail") {
                            $rootScope.error =  $rootScope.error + "When you sure that your access data are correct, than read more about google secure apps (https://support.google.com/accounts/answer/6010255?hl=de) !";
                        }

                    })
                };

            }]);
