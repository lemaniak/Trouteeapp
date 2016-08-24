package com.troutee.api;

import com.troutee.utils.Connector;

import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;

/**
 * Created by vicente on 11/03/16.
 */
public class BaseService {
    public static final String TROUTEE_API_BASE_URL="http://52.42.251.198/api-1.0.0/api";
    public static Connector connector;
    public static PostMethod postMethod;
    public static PutMethod putMethod;
    public static GetMethod getMethod;
    public static DeleteMethod deleteMethod;
}
