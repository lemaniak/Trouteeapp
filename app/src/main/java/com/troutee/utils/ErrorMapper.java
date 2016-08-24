package com.troutee.utils;

import com.troutee.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vicente on 16/03/16.
 */
public class ErrorMapper {
    public static Map<Integer,Integer> errors = new HashMap<>();

    public ErrorMapper() {
        errors.put(2000, R.string.error_firstname_required);
        errors.put(2001,R.string.error_firstname_invalid_length);
        errors.put(2002,R.string.error_lastname_required);
        errors.put(2003,R.string.error_lastname_invalid_length);
        errors.put(2004,R.string.error_password_required);
        errors.put(2005,R.string.error_password_invalid_length);
        errors.put(2007,R.string.error_user_not_found);
        errors.put(2008,R.string.error_logging_out_user);
        errors.put(2010,R.string.error_email_required);
        errors.put(2011,R.string.error_email_invalid);
        errors.put(2013,R.string.error_email_already_in_use);
        errors.put(9001,R.string.error_invalid_credentials);
        errors.put(9002,R.string.error_expires_token);
        errors.put(3000,R.string.error_invalid_token);
        errors.put(4000,R.string.error_firstname_required);
        errors.put(4001,R.string.error_firstname_invalid_length);
        errors.put(4002,R.string.error_lastname_required);
        errors.put(4003,R.string.error_lastname_invalid_length);
        errors.put(4004,R.string.error_phone_invalid_format);
        errors.put(4005,R.string.error_vendor_code_invalid_size);
        errors.put(4006,R.string.error_invalid_interval_value);
        errors.put(4007,R.string.error_weekdays_empty);
        errors.put(5003,R.string.error_syncronizing_data);
        errors.put(5000,R.string.error_updating_client);
        errors.put(5001,R.string.error_updating_client);
        errors.put(5002,R.string.error_updating_client);
        errors.put(5004,R.string.error_updating_client);
        errors.put(5005,R.string.error_updating_client);
        errors.put(5006,R.string.error_updating_client);
        errors.put(5007,R.string.error_updating_client_coordinates);
        errors.put(5008,R.string.error_invalid_coordinates);
        errors.put(5009,R.string.error_updating_client);
        errors.put(5010,R.string.error_not_near_checkin_point);
    }
}
