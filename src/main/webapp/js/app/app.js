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

var intelliJob = angular.module('intelliJob',
        [
            'ngRoute',
            'ngCookies',
            'intelliJobControllers',
            'ui.bootstrap'
        ]);
var intelliJobControllers = angular.module('intelliJobControllers', []);

/**
 * App configuration.
 */
intelliJob.config([
    '$routeProvider',
    '$locationProvider',
    '$httpProvider',

    function ($routeProvider, $locationProvider, $httpProvider) {
        // Enable HTML5 strategy (without # in urls)
        $locationProvider.html5Mode(true);

        $routeProvider.when('/intellijob/home', {
            templateUrl: '/intellijob/views/mailSearchForm.html',
            controller: 'MailCtrl'
        }).when('/intellijob/mails', {
            templateUrl: '/intellijob/views/mailTable.html',
            controller: 'MailTableCtrl'
        }).when('/intellijob/mails/:mailId/content', {
            templateUrl: '/intellijob/views/mailContent.html',
            controller: 'MailContentCtrl'
        }).when('/intellijob/index', {
            templateUrl: '/intellijob/views/mailSearchForm.html',
            controller: 'MailCtrl'
        }).when('/intellijob/joblinks', {
            templateUrl: '/intellijob/views/jobLinkTable.html',
            controller: 'JobLinkTableCtrl'
        }).when('/intellijob/jobs', {
            templateUrl: '/intellijob/views/jobTable.html',
            controller: 'JobTableCtrl'
        }).when('/intellijob/jobs/:jobId/content', {
            templateUrl: '/intellijob/views/jobContent.html',
            controller: 'JobContentCtrl'
        }).when('/intellijob/jobdetails', {
            templateUrl: '/intellijob/views/jobDetailTable.html',
            controller: 'JobDetailTableCtrl'
        }).when('/intellijob/jobdetails/:jobDetailId/detail', {
            templateUrl: '/intellijob/views/jobDetail.html',
            controller: 'JobDetailCtrl'
        }).when('/intellijob/audit', {
            templateUrl: '/intellijob/views/audit.html',
            controller: 'AuditCtrl'
        }).when('/intellijob/users', {
            templateUrl: '/intellijob/views/user.html',
            controller: 'UserCtrl'
        }).otherwise({
            redirectTo: '/intellijob',
            templateUrl: '/intellijob/views/mailSearchForm.html',
            controller: 'MailCtrl'
        });

        /* Intercept http errors */
        var interceptor = function ($rootScope, $cookieStore, $q) {
            function success(response) {
                delete $rootScope.success;
                delete $rootScope.error;
                return response;
            }

            function error(response) {
                delete $rootScope.success;
                delete $rootScope.error;
                var status = response.status;

                if (status == 401) {
                    $cookieStore.remove("user");
                    delete $rootScope.user;
                    $rootScope.error = status + ": " + response.data.message;
                }

                return $q.reject(response);
            }

            return function (promise) {
                return promise.then(success, error);
            };
        };

        $httpProvider.responseInterceptors.push(interceptor);

    }]).run(function ($rootScope, $cookieStore, UserServices) {
    //Reset error and success when a new view is loaded
    $rootScope.$on('$viewContentLoaded', function () {
        delete $rootScope.error;
        if ($rootScope.transferSuccess == false || $rootScope.transferSuccess == undefined) {
            delete $rootScope.success;
        }
        else {
            $rootScope.transferSuccess = false;
        }
    });

    $rootScope.$on('$routeChangeSuccess', function () {
        /* Try getting valid user from cookie*/
        $rootScope.user = $cookieStore.get('user');
        if (!$rootScope.isUserValid()) {
            //user not valid, request getUser
            UserServices.getUser().success(function (response) {
                $rootScope.user = response;
                $cookieStore.put("user", $rootScope.user);
            }).error(function (error) {
                $rootScope.error = status + ": " + error.data.message;
            });
        }
    });

    $rootScope.isUserValid = function () {
        return $rootScope.user !== undefined && $rootScope.user.userId !== undefined;
    };

    console.log($rootScope);
});
