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
intelliJob.directive('nodeTree', function () {
    return {
        template: '<node ng-repeat="node in tree"></node>',
        replace: true,
        transclude: true,
        restrict: 'E',
        scope: {
            tree: '=ngModel'
        }
    };
});

intelliJob.directive('node', function ($compile) {
    return {
        restrict: 'E',
        replace: true,
        templateUrl: 'views/templates/the-tree.html',
        link: function (scope, elm, attrs) {

            //$(elm).parent('ul').find('span.leaf').on('click', function (e) {
            $(elm).find('span.leaf').on('click', function (e) {

                var children = $(elm).find('li');

                if (children.is(":visible")) {
                    children.hide('fast');
                    $(elm).find('span.leaf i.icon-minus-sign').addClass('icon-plus-sign').removeClass('icon-minus-sign');
                }
                else {

                    children.show('fast');
                    $(elm).find('span.leaf i.icon-plus-sign').addClass('icon-minus-sign').removeClass('icon-plus-sign');
                }
                e.stopPropagation();
            });


            scope.nodeClicked = function (node) {
                node.checked = !node.checked;
                function checkChildren(c) {
                    angular.forEach(c.children, function (c) {
                        c.checked = node.checked;
                        checkChildren(c);
                    });
                }

                checkChildren(node);
            };

            scope.switcher = function (booleanExpr, trueValue, falseValue) {
                return booleanExpr ? trueValue : falseValue;
            };

            scope.isLeaf = function (_data) {
                return _data.skills.length === 0;

            };


            if (scope.node.skills.length > 0) {
                var childNode = $compile('<ul ><node-tree ng-model="node.skills"></node-tree></ul>')(scope)
                elm.append(childNode);
            }
        }
    };
});
