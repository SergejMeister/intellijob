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

package com.intellijob.enums;

/**
 * Enum skill category for better access to document SkillCategory.
 */
public enum SkillCategoryEnum {

    EXPERIENCE(1),

    EDUCATION(2),

    KNOWLEDGE(3),

    LANGUAGE(4),

    PERSONAL_STRENGTH(5),

    MOBILITY(6);


    private final Integer typeId;

    SkillCategoryEnum(int typeId) {
        this.typeId = typeId;
    }

    public static SkillCategoryEnum findByTypeId(Integer typeId) {
        for (SkillCategoryEnum value : SkillCategoryEnum.values()) {
            if (value.getTypeId() == typeId) {
                return value;
            }
        }

        throw new IllegalArgumentException("No SkillCategoryEnum found for typeId: " + typeId);
    }


    public Integer getTypeId() {
        return typeId;
    }
}
