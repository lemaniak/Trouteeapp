package com.troutee.utils;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.params.HttpParams;

import java.io.IOException;

public class Connector {
	private HttpClient client;
	private static final int CONNECTION_TIMEOT_MILLIS = 1000000; 
	private static final int CONNECTION_SO_MILLIS = 1000000; 

	public Connector() {
	}

	public int connectGetMethod(GetMethod method) throws IOException {
		// Create an instance of HttpClient.
		client = new HttpClient();
		
		HttpParams params = client.getParams();
		params.setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, CONNECTION_TIMEOT_MILLIS);
		params.setParameter(HttpConnectionParams.SO_TIMEOUT,CONNECTION_SO_MILLIS);

		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler(3, false));

		// Execute the method.
		return client.executeMethod(method);

	}

	public int connectPostMethod(PostMethod method) throws IOException {
		// Create an instance of HttpClient.
		client = new HttpClient();

		HttpParams params = client.getParams();
		params.setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, CONNECTION_TIMEOT_MILLIS);
		params.setParameter(HttpConnectionParams.SO_TIMEOUT,CONNECTION_SO_MILLIS);

		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler(3, false));

		// Execute the method.
		return client.executeMethod(method);

	}

    public int connectPutMethod(PutMethod method) throws IOException {
        // Create an instance of HttpClient.
        client = new HttpClient();

        HttpParams params = client.getParams();
        params.setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, CONNECTION_TIMEOT_MILLIS);
        params.setParameter(HttpConnectionParams.SO_TIMEOUT,CONNECTION_SO_MILLIS);

        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler(3, false));

        // Execute the method.
        return client.executeMethod(method);

    }

    public int connectDeleteMethod(DeleteMethod method) throws IOException {
        // Create an instance of HttpClient.
        client = new HttpClient();

        HttpParams params = client.getParams();
        params.setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, CONNECTION_TIMEOT_MILLIS);
        params.setParameter(HttpConnectionParams.SO_TIMEOUT,CONNECTION_SO_MILLIS);

        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler(3, false));

        // Execute the method.
        return client.executeMethod(method);

    }

	public void disconnectGetMethod(GetMethod getMethod) {
		getMethod.releaseConnection();
	}

	public void disconnectPostMethod(PostMethod postMethod) {
		postMethod.releaseConnection();
	}

    public void disconnectDeleteMethod(DeleteMethod deleteMethod) {
        deleteMethod.releaseConnection();
    }

	public HttpClient getClient() {
		return client;
	}

	public void setClient(HttpClient client) {
		this.client = client;
	}
}
