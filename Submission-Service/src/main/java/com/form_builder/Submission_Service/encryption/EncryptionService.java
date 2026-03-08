package com.form_builder.Submission_Service.encryption;

import java.util.Map;

public class EncryptionService {
    public static void encryptFields(Map<String, Object> data) {

        for (String key : data.keySet()) {

            if (key.toLowerCase().contains("password")
                    || key.toLowerCase().contains("token")
                    || key.toLowerCase().contains("secret")) {

                String value = data.get(key).toString();

                data.put(key, AESUtil.encrypt(value));
            }
        }
    }
    }
