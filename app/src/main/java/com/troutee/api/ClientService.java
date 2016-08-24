package com.troutee.api;

import android.location.Location;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.troutee.dto.request.XCheckinRequest;
import com.troutee.dto.request.XClient;
import com.troutee.dto.request.XClientIds;
import com.troutee.dto.request.XLogin;
import com.troutee.dto.request.XSignup;
import com.troutee.dto.request.XToken;
import com.troutee.dto.request.XUpdatePassword;
import com.troutee.dto.request.XUpdateUser;
import com.troutee.dto.response.RClient;
import com.troutee.dto.response.RClientVersions;
import com.troutee.dto.response.RClientsResponse;
import com.troutee.dto.response.RError;
import com.troutee.dto.response.RToken;
import com.troutee.dto.response.RUser;
import com.troutee.dto.response.Response;
import com.troutee.utils.Connector;
import com.troutee.utils.Utils;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Created by vicente on 11/03/16.
 */
public class ClientService extends BaseService {

    private final String TAG = ClientService.class.getSimpleName();

    public Response getAllClients(XToken xToken){
        Response response=null;
        try {
            postMethod = new PostMethod(TROUTEE_API_BASE_URL + "/client/find_all");
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
                RClientsResponse clients= gson.fromJson(postMethod.getResponseBodyAsString(),RClientsResponse.class);
                response= new Response(statusCode,clients);
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

    public Response getClientVersions(XToken xToken){
        Response response=null;
        try {
            postMethod = new PostMethod(TROUTEE_API_BASE_URL + "/client/get_client_versions");
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
                RClientVersions clientVersions= gson.fromJson(postMethod.getResponseBodyAsString(),RClientVersions.class);
                response= new Response(statusCode,clientVersions);
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

    public Response getClientByIds(XClientIds clientIds){
        Response response=null;
        try {
            postMethod = new PostMethod(TROUTEE_API_BASE_URL + "/client/get_client_by_ids");
            postMethod.addRequestHeader(Utils.createHeader("accept", "application/json"));
            postMethod.addRequestHeader(Utils.createHeader("Content-type", "application/json"));
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);
            Gson gson = gsonBuilder.create();
            String json = gson.toJson(clientIds);
            RequestEntity requestEntity = new ByteArrayRequestEntity(json.getBytes(), "application/json");
            postMethod.setRequestEntity(requestEntity);
            connector = new Connector();
            int statusCode = connector.connectPostMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK) {
                RClientsResponse clients= gson.fromJson(postMethod.getResponseBodyAsString(),RClientsResponse.class);
                response= new Response(statusCode,clients);
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

    public Response updateClient(XClient client){
        Response response=null;
        try {
            postMethod = new PostMethod(TROUTEE_API_BASE_URL + "/client/update");
            postMethod.addRequestHeader(Utils.createHeader("accept", "application/json"));
            postMethod.addRequestHeader(Utils.createHeader("Content-type", "application/json"));
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);
            Gson gson = gsonBuilder.create();
            String json = gson.toJson(client);
            RequestEntity requestEntity = new ByteArrayRequestEntity(json.getBytes(), "application/json");
            postMethod.setRequestEntity(requestEntity);
            connector = new Connector();
            int statusCode = connector.connectPostMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK) {
                RClient updatedclient= gson.fromJson(postMethod.getResponseBodyAsString(),RClient.class);
                response= new Response(statusCode,updatedclient);
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


    public Response checkin(XClient client,Location location){
        Response response=null;
        try {
            postMethod = new PostMethod(TROUTEE_API_BASE_URL + "/client/checkin");
            postMethod.addRequestHeader(Utils.createHeader("accept", "application/json"));
            postMethod.addRequestHeader(Utils.createHeader("Content-type", "application/json"));
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);
            Gson gson = gsonBuilder.create();
            String json = gson.toJson(new XCheckinRequest(client.getToken(),client.getId(),location.getLatitude(),location.getLongitude()));
            RequestEntity requestEntity = new ByteArrayRequestEntity(json.getBytes(), "application/json");
            postMethod.setRequestEntity(requestEntity);
            connector = new Connector();
            int statusCode = connector.connectPostMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK) {
                response= new Response(HttpStatus.SC_OK,null);
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



}
