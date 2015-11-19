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
intelliJob.directive('skillTree', function () {
    return {
        template: '<skill-node ng-repeat="skillNode in tree"></skill-node>',
        replace: true,
        transclude: true,
        restrict: 'E',
        scope: {
            tree: '=ngModel',
            selected: '=',
            expanded: '='
        }
    };
});

intelliJob.directive('skillNode', function ($compile, $filter) {
    return {
        restrict: 'E',
        replace: true,
        templateUrl: 'views/templates/skill-tree.html',
        link: function (scope, elm, attrs) {
            scope.expanded = scope.$parent.expanded;

            scope.isLeaf = function (_value) {
                return _value.skills.length === 0;
            };

            scope.isRoot = function (_value) {
                return !scope.isLeaf(_value);
            };

            /**
             * Firstly check id, if check id undefined, than check skill name!
             * @returns {boolean} true if found, otherwise false.
             */
            scope.isSkillSelected = function () {
                var selectedSkillNode = $filter("filter")(scope.$parent.selected, {skillData: {id: scope.skillNode.id}});
                if (selectedSkillNode.length === 1) {
                    return true;
                } else {
                    selectedSkillNode = $filter("filter")(scope.$parent.selected, {skillData: {name: scope.skillNode.name}});
                    return selectedSkillNode.length === 1;
                }
            };

            if (scope.skillNode.skills.length > 0) {
                var childNode = $compile('<ul ng-show="expanded"><skill-tree ng-model="skillNode.skills" expanded="expanded" selected="$parent.selected"></skill-tree></ul>')(scope);
                elm.append(childNode);
            } else {
                scope.skillNode.checked = scope.isSkillSelected(scope.skillNode);
            }

            scope.switcher = function (booleanExpr, trueValue, falseValue) {
                return booleanExpr ? trueValue : falseValue;
            };

            scope.isRootExpanded = function (_skillNodeValue, _expandedValue) {
                return scope.isRoot(_skillNodeValue) && _expandedValue === true;
            };

            scope.isRootNotExpanded = function (_skillNodeValue, _expandedValue) {
                return scope.isRoot(_skillNodeValue) && _expandedValue === false;
            };

            scope.addNodeToSelected = function (_skillNode) {
                var skillRatingData = {};
                skillRatingData.rating = 1;
                skillRatingData.skillData = {};
                skillRatingData.skillData = _skillNode;
                scope.$parent.selected.push(skillRatingData);
            };

            scope.removeNodeFromSelected = function (_skillNode) {
                var index = scope.$parent.selected.indexOf(_skillNode);
                scope.$parent.selected.splice(index, 1);
            };

            scope.nodeClicked = function (skillNode, expandedValue) {
                scope.expanded = !expandedValue;
                skillNode.checked = !skillNode.checked;
                if (scope.isLeaf(skillNode)) {
                    if (skillNode.checked) {
                        scope.addNodeToSelected(skillNode);
                    } else {
                        scope.removeNodeFromSelected(skillNode);
                    }
                }
            };
        }
    };
});
