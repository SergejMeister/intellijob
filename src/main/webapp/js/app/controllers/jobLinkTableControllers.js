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

                $scope.jobLinks;
                JobLinkServices.getJobLinks().success(function (response) {
                    $scope.jobLinks = response.jobLinks;
                }).error(function (error) {
                    console.log(error);
                });
            }]);
