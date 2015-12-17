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

package com.intellijob.utility;

import java.util.Collection;

/**
 * ListUtility
 */
public final class ListUtility {

    private ListUtility() {
    }

    /**
     * Check collection if null or empty.
     *
     * @param value collection to check.
     *
     * @return true if null or empty.
     */
    public static boolean isBlank(Collection value) {
        return value == null || value.size() == 0;
    }
}
