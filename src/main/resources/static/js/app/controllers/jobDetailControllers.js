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
        'JobDetailCtrl',
        [
            '$scope',
            '$rootScope',
            '$location',
            '$http',
            '$cookieStore',
            '$routeParams',
            'JobDetailServices',
            function ($scope, $rootScope, $location, $http, $cookieStore, $routeParams, JobDetailServices) {

                var jobDetailId = $routeParams.jobDetailId;
                $scope.jobDetail;
                JobDetailServices.getJobDetailById(jobDetailId).success(function (response) {
                    $scope.jobDetail = response;
                }).error(function (error) {
                    console.log(error);
                });
            }]);
