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

angular.module('intelliJob').directive('tagManager', function () {
    return {
        restrict: 'E',
        scope: {tags: '='},
        templateUrl: '/views/templates/tag-language.html',
        //template: '<div class="tags">' +
        //'<a ng-repeat="(idx, tag) in tags" class="tag" ng-click="remove(idx)">{{tag}}</a>' +
        //'</div>' +
        //'<input type="text" placeholder="Add a tag..." ng-model="new_value"></input> ' +
        //'<a class="btn" ng-click="add()">Add</a>',
        link: function ($scope, $element) {
            // FIXME: this is lazy and error-prone
            var input = angular.element($element.children()[1]);

            // This adds the new tag to the tags array
            $scope.add = function () {
                $scope.tags.push($scope.new_value);
                $scope.new_value = "";
            };

            // This is the ng-click handler to remove an item
            $scope.remove = function (idx) {
                $scope.tags.splice(idx, 1);
            };

            // Capture all keypresses
            input.bind('keypress', function (event) {
                // But we only care when Enter was pressed
                if (event.keyCode == 13) {
                    // There's probably a better way to handle this...
                    $scope.$apply($scope.add);
                }
            });
        }
    };
});

app.controller('MainCtrl', function ($scope) {
    $scope.tags = ['cool', 'awesome', 'angular', 'directive', 'javascript', 'html'];
});
