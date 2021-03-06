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

package com.intellijob;

import java.time.ZoneId;

/**
 * Constants value.
 */
public final class Constants {

    public static final String DB_FIELD_RECEIVED_DATE = "receivedDate";

    public static final String DB_FIELD_CONTENT = "content";

    public static final String DB_FIELD_READ = "read";

    public static final String DB_FIELD_RATING = "rating";

    public static final String DB_FIELD_NAME = "name";

    public static final String DB_FIELD_ID = "id";

    public static final String UTC = "UTC";

    public static final String DEFAULT_DATE_PATTERN = "dd.MM.yyyy hh:mm";

    public static final ZoneId ZONE_ID_UTC = ZoneId.of(UTC);

    public static final int DB_RESULT_LIMIT = 50;

    private Constants() {
    }
}
