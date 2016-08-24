package com.troutee.api;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.troutee.dto.request.XLogin;
import com.troutee.dto.request.XToken;
import com.troutee.dto.request.XSignup;
import com.troutee.dto.request.XUpdatePassword;
import com.troutee.dto.request.XUpdateUser;
import com.troutee.dto.response.RError;
import com.troutee.dto.response.RToken;
import com.troutee.dto.response.RUser;
import com.troutee.dto.response.Response;
import com.troutee.utils.Connector;
import com.troutee.utils.Utils;


import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;


import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Created by vicente on 11/03/16.
 */
public class UserService extends BaseService{

    private final String TAG = UserService.class.getSimpleName();

    public Response signup(XSignup xSignup){
        Response response=null;
        try {
            postMethod = new PostMethod(TROUTEE_API_BASE_URL + "/user/signup");
            postMethod.addRequestHeader(Utils.createHeader("accept", "application/json"));
            postMethod.addRequestHeader(Utils.createHeader("Content-type", "application/json"));
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);
            Gson gson = gsonBuilder.create();
            String json = gson.toJson(xSignup);
            RequestEntity requestEntity = new ByteArrayRequestEntity(json.getBytes(), "application/json");
            postMethod.setRequestEntity(requestEntity);
            connector = new Connector();
            int statusCode = connector.connectPostMethod(postMethod);
            if (statusCode == HttpStatus.SC_CREATED) {
                RUser user = gson.fromJson(postMethod.getResponseBodyAsString(),RUser.class);
                response= new Response(statusCode,user);
                return response;
            } else {
                if(statusCode==HttpStatus.SC_INTERNAL_SERVER_ERROR){
                    return  new Response(statusCode,null);
                }else if(statusCode==HttpStatus.SC_BAD_REQUEST){
                    RError error = gson.fromJson(postMethod.getResponseBodyAsString(),RError.class);
                    response= new Response(statusCode,error);
                    return response;
                }
            }
        } catch (ConnectTimeoutException cte) {
            //Took too long to connect to remote host
            Log.e(TAG, cte.toString());
        } catch (SocketTimeoutException ste) {
            //Remote host didn’t respond in time
            Log.e(TAG, ste.toString());
        } catch(IOException ioex){
            Log.e(TAG, ioex.toString());
        } finally {
            // Release the connection.
            postMethod.releaseConnection();
        }

        return response;
    }

    public Response update(XUpdateUser xUpdateUser){
        Response response=null;
        try {
            postMethod = new PostMethod(TROUTEE_API_BASE_URL + "/user/update");
            postMethod.addRequestHeader(Utils.createHeader("accept", "application/json"));
            postMethod.addRequestHeader(Utils.createHeader("Content-type", "application/json"));
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);
            Gson gson = gsonBuilder.create();
            String json = gson.toJson(xUpdateUser);
            RequestEntity requestEntity = new ByteArrayRequestEntity(json.getBytes(), "application/json");
            postMethod.setRequestEntity(requestEntity);
            connector = new Connector();
            int statusCode = connector.connectPostMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK) {
                RUser user = gson.fromJson(postMethod.getResponseBodyAsString(),RUser.class);
                response= new Response(statusCode,user);
                return response;
            } else {
                if(statusCode==HttpStatus.SC_INTERNAL_SERVER_ERROR){
                    return  new Response(statusCode,null);
                }else if(statusCode==HttpStatus.SC_UNAUTHORIZED){
                    RError error = gson.fromJson(postMethod.getResponseBodyAsString(),RError.class);
                    response= new Response(statusCode,error);
                    return response;
                }else if(statusCode==HttpStatus.SC_BAD_REQUEST){
                    RError error = gson.fromJson(postMethod.getResponseBodyAsString(),RError.class);
                    response= new Response(statusCode,error);
                    return response;
                }
            }
        } catch (ConnectTimeoutException cte) {
            //Took too long to connect to remote host
            Log.e(TAG, cte.toString());
        } catch (SocketTimeoutException ste) {
            //Remote host didn’t respond in time
            Log.e(TAG, ste.toString());
        } catch(IOException ioex){
            Log.e(TAG, ioex.toString());
        } finally {
            // Release the connection.
            postMethod.releaseConnection();
        }

        return response;
    }

    public Response updatePassword(XUpdatePassword xUpdatePassword){
        Response response=null;
        try {
            postMethod = new PostMethod(TROUTEE_API_BASE_URL + "/user/update/password");
            postMethod.addRequestHeader(Utils.createHeader("accept", "application/json"));
            postMethod.addRequestHeader(Utils.createHeader("Content-type", "application/json"));
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);
            Gson gson = gsonBuilder.create();
            String json = gson.toJson(xUpdatePassword);
            RequestEntity requestEntity = new ByteArrayRequestEntity(json.getBytes(), "application/json");
            postMethod.setRequestEntity(requestEntity);
            connector = new Connector();
            int statusCode = connector.connectPostMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK) {
                RUser user = gson.fromJson(postMethod.getResponseBodyAsString(),RUser.class);
                response= new Response(statusCode,user);
                return response;
            } else {
                if(statusCode==HttpStatus.SC_INTERNAL_SERVER_ERROR){
                    return  new Response(statusCode,null);
                }else if(statusCode==HttpStatus.SC_UNAUTHORIZED){
                    RError error = gson.fromJson(postMethod.getResponseBodyAsString(),RError.class);
                    response= new Response(statusCode,error);
                    return response;
                }else if(statusCode==HttpStatus.SC_BAD_REQUEST){
                    RError error = gson.fromJson(postMethod.getResponseBodyAsString(),RError.class);
                    response= new Response(statusCode,error);
                    return response;
                }
            }
        } catch (ConnectTimeoutException cte) {
            //Took too long to connect to remote host
            Log.e(TAG, cte.toString());
        } catch (SocketTimeoutException ste) {
            //Remote host didn’t respond in time
            Log.e(TAG, ste.toString());
        } catch(IOException ioex){
            Log.e(TAG, ioex.toString());
        } finally {
            // Release the connection.
            postMethod.releaseConnection();
        }

        return response;
    }

    public Response login(XLogin xLogin){
        Response response=null;
        try {
            postMethod = new PostMethod(TROUTEE_API_BASE_URL + "/user/login");
            postMethod.addRequestHeader(Utils.createHeader("accept", "application/json"));
            postMethod.addRequestHeader(Utils.createHeader("Content-type", "application/json"));
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);
            Gson gson = gsonBuilder.create();
            String json = gson.toJson(xLogin);
            RequestEntity requestEntity = new ByteArrayRequestEntity(json.getBytes(), "application/json");
            postMethod.setRequestEntity(requestEntity);
            connector = new Connector();
            int statusCode = connector.connectPostMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK) {
                RUser user = gson.fromJson(postMethod.getResponseBodyAsString(),RUser.class);
                user.setLogged(true);
                response= new Response(statusCode,user);
                return response;
            } else {
                if(statusCode==HttpStatus.SC_INTERNAL_SERVER_ERROR){
                    return  new Response(statusCode,null);
                }else if(statusCode==HttpStatus.SC_BAD_REQUEST || statusCode==HttpStatus.SC_NOT_FOUND){
                    RError error = gson.fromJson(postMethod.getResponseBodyAsString(),RError.class);
                    response= new Response(statusCode,error);
                    return response;
                }
            }
        } catch (ConnectTimeoutException cte) {
            //Took too long to connect to remote host
            Log.e(TAG, cte.toString());
        } catch (SocketTimeoutException ste) {
            //Remote host didn’t respond in time
            Log.e(TAG, ste.toString());
        } catch(IOException ioex){
            Log.e(TAG, ioex.toString());
        } finally {
            // Release the connection.
            postMethod.releaseConnection();
        }

        return response;
    }

    public Response validateToken(XToken xToken){
        Response response=null;
        try {
            postMethod = new PostMethod(TROUTEE_API_BASE_URL + "/user/validate_token");
            postMethod.addRequestHeader(Utils.createHeader("accept", "application/json"));
            postMethod.addRequestHeader(Utils.createHeader("Content-type", "application/json"));
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);
            Gson gson = gsonBuilder.create();
            String json = gson.toJson(xToken);
            RequestEntity requestEntity = new ByteArrayRequestEntity(json.getBytes(), "application/json");
            postMethod.setRequestEntity(requestEntity);
            connector = new Connector();
            int statusCode = connector.connectPostMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK) {
                RToken rToken = gson.fromJson(postMethod.getResponseBodyAsString(),RToken.class);
                response= new Response(statusCode,rToken);
                return response;
            } else {
                if(statusCode==HttpStatus.SC_INTERNAL_SERVER_ERROR){
                    return  new Response(statusCode,null);
                }else if(statusCode==HttpStatus.SC_UNAUTHORIZED){
                    RError error = gson.fromJson(postMethod.getResponseBodyAsString(),RError.class);
                    response= new Response(statusCode,error);
                    return response;
                }else if(statusCode==HttpStatus.SC_BAD_REQUEST){
                    RError error = gson.fromJson(postMethod.getResponseBodyAsString(),RError.class);
                    response= new Response(statusCode,error);
                    return response;
                }
            }
        } catch (ConnectTimeoutException cte) {
            //Took too long to connect to remote host
            Log.e(TAG, cte.toString());
        } catch (SocketTimeoutException ste) {
            //Remote host didn’t respond in time
            Log.e(TAG, ste.toString());
        } catch(IOException ioex){
            Log.e(TAG, ioex.toString());
        } finally {
            // Release the connection.
            postMethod.releaseConnection();
        }

        return response;
    }

    public Response logout(XToken xlogout){
        Response response=null;
        try {
            postMethod = new PostMethod(TROUTEE_API_BASE_URL + "/user/logout");
            postMethod.addRequestHeader(Utils.createHeader("accept", "application/json"));
            postMethod.addRequestHeader(Utils.createHeader("Content-type", "application/json"));
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);
            Gson gson = gsonBuilder.create();
            String json = gson.toJson(xlogout);
            RequestEntity requestEntity = new ByteArrayRequestEntity(json.getBytes(), "application/json");
            postMethod.setRequestEntity(requestEntity);
            connector = new Connector();
            int statusCode = connector.connectPostMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK) {
                response= new Response(statusCode,null);
                return response;
            } else {
                if(statusCode==HttpStatus.SC_INTERNAL_SERVER_ERROR){
                    return  new Response(statusCode,null);
                }else if(statusCode==HttpStatus.SC_BAD_REQUEST){
                    RError error = gson.fromJson(postMethod.getResponseBodyAsString(),RError.class);
                    response= new Response(statusCode,error);
                    return response;
                }
            }
        } catch (ConnectTimeoutException cte) {
            //Took too long to connect to remote host
            Log.e(TAG, cte.toString());
        } catch (SocketTimeoutException ste) {
            //Remote host didn’t respond in time
            Log.e(TAG, ste.toString());
        } catch(IOException ioex){
            Log.e(TAG, ioex.toString());
        } finally {
            // Release the connection.
            postMethod.releaseConnection();
        }

        return response;
    }
}
