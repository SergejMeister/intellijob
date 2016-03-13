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
            'ngAnimate',
            'intelliJobControllers',
            'ui.bootstrap',
            'angucomplete',
            'angular-jqcloud'
        ]);
var intelliJobControllers = angular.module('intelliJobControllers', []);

//Interceptors
intelliJob.factory('HttpResponseInterceptor', ['$q', '$location', '$rootScope', '$cookieStore', function ($q, $location, $rootScope, $cookieStore) {
    return function (promise) {

        var success = function (response) {
            delete $rootScope.success;
            delete $rootScope.error;
            return response;
        };

        var error = function (response) {
            delete $rootScope.success;
            delete $rootScope.error;
            if (response.status == 401) {
                $cookieStore.remove("user");
                delete $rootScope.globalUser;
                $location.path('/home');
                return $q.reject(response);
            }

            return $q.reject(response);
        };

        return promise.then(success, error);
    };
}]);

/**
 * App configuration.
 */
intelliJob.config([
    '$routeProvider',
    '$locationProvider',
    '$httpProvider',
    function ($routeProvider, $locationProvider, $httpProvider) {
        $locationProvider.html5Mode({
            enabled: true,
            requireBase: false
        });

        $httpProvider.interceptors.push('HttpResponseInterceptor');

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

    $rootScope.setGlobalUserData = function (user) {
        var globalUserObj = {};
        globalUserObj.userId = user.userId;
        globalUserObj.profileData = {};
        globalUserObj.profileData.firstName = user.profileData.firstName;
        globalUserObj.profileData.secondName = user.profileData.secondName;
        globalUserObj.profileData.fullName = user.profileData.fullName;
        $rootScope.globalUser = globalUserObj;
        $cookieStore.put("user", globalUserObj);
    };

    $rootScope.isUserValid = function () {
        return $rootScope.globalUser !== undefined && $rootScope.globalUser.userId !== undefined && $rootScope.globalUser.userId !== null;
    };

    $rootScope.$on('$routeChangeSuccess', function () {
        /* Try getting valid user from cookie*/
        $rootScope.globalUser = $cookieStore.get('user');
        if (!$rootScope.isUserValid()) {
            //user not valid, request getUser
            UserServices.getUser().success(function (response) {
                setGlobalUserData(response);
            }).error(function (error) {
                $rootScope.error = status + ": " + error.data.message;
            });
        }
    });

    console.log($rootScope);
});
