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

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility Class to hash data.
 */
public final class HashUtility {

    private static final String SHA_512 = "SHA-512";
    private static final String SHA_1 = "SHA-1";
    private static final String SHA_256 = "SHA-256";

    private HashUtility() {
    }

    public static String hashAsString(String plain) {
        return hash(plain, SHA_512);
    }

    public static byte[] hashAsByte(String plain) {
        return hashToByte(plain, SHA_512);
    }

    public static String hash(String plain, String algorithm) {
        byte[] hash = hashToByte(plain, algorithm);
        return byteHashToHexString(hash);
    }

    private static byte[] hashToByte(String value, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            return md.digest(value.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException();
        }
    }

    private static String byteHashToHexString(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash).toLowerCase();
    }
}
