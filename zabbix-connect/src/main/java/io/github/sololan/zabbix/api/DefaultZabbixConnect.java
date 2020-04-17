package io.github.sololan.zabbix.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wejoyclass.core.util.RedisUtil;
import io.github.sololan.zabbix.request.DeleteRequestEntity;
import io.github.sololan.zabbix.request.RequestEntity;
import io.github.sololan.zabbix.request.RequestEntityBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class DefaultZabbixConnect implements ZabbixConnect {
    private static final Logger logger = LoggerFactory.getLogger(DefaultZabbixConnect.class);

    private CloseableHttpClient httpClient;

    private URI uri;

    private String auth;

    public static DefaultZabbixConnect newZabbixConnect(String url){
        return new DefaultZabbixConnect(url);
    }

    public DefaultZabbixConnect(String url) {
        try {
            uri = new URI(url.trim());
        } catch (URISyntaxException e) {
            throw new RuntimeException("url invalid", e);
        }
    }

    public DefaultZabbixConnect(URI uri) {
        this.uri = uri;
    }

    @Override
    public DefaultZabbixConnect init() {
        if(httpClient == null){
            httpClient = HttpClients.custom().build();
        }
        return this;
    }

    @Override
    public DefaultZabbixConnect destroy() {
        if(httpClient != null){
            try {
                httpClient.close();
            } catch (Exception e) {
                logger.error("close httpclient error!", e);
            }
        }
        return this;
    }

    @Override
    public DefaultZabbixConnect login(String user, String password) {
        // 如果redis中不存在zabbixAuth，那么就再去请求一次
        if(RedisUtil.get("zabbixAuth") == null){
            try {
                logger.info("从zabbix中重新获取auth");
                RequestEntity requestEntity = RequestEntityBuilder.newBuilder().method("user.login").paramEntry("user", user).paramEntry("password", password).build();
                HttpUriRequest httpRequest = RequestBuilder.post().setUri(uri)
                        .addHeader("Content-Type", "application/json")
                        .setEntity(new StringEntity(JSON.toJSONString(requestEntity), ContentType.APPLICATION_JSON)).build();
                CloseableHttpResponse response = httpClient.execute(httpRequest);
                HttpEntity entity = response.getEntity();
                byte[] bytes = EntityUtils.toByteArray(entity);
                auth = (((JSONObject) JSON.parse(bytes)).getString("result"));
                RedisUtil.put("zabbixAuth",auth,600L);
            }catch (IOException e){
                throw new RuntimeException("DefaultZabbixConnect login exception!", e);
            }
        }
        // 如果redis里存在，则用redis中的
        else {
            auth = RedisUtil.get("zabbixAuth");
        }
        return this;
    }

    public JSONObject call(RequestEntity requestEntity) {
        if(httpClient == null){
            logger.error("you need init first!");
        }
        if(auth == null || auth == ""){
            logger.error("you need login first!");
        }
        if(requestEntity.getAuth() == null || requestEntity.getAuth() == ""){
            requestEntity.setAuth(auth);
        }
        try {
            System.out.println(requestEntity);
            HttpUriRequest httpRequest = RequestBuilder.post().setUri(uri)
                    .addHeader("Content-Type", "application/json")
                    .setEntity(new StringEntity(JSON.toJSONString(requestEntity), ContentType.APPLICATION_JSON)).build();
            CloseableHttpResponse response = httpClient.execute(httpRequest);
            HttpEntity entity = response.getEntity();
            byte[] bytes = EntityUtils.toByteArray(entity);
            return (JSONObject) JSON.parse(bytes);
        }catch (IOException e){
            throw new RuntimeException("DefaultZabbixConnect call exception!", e);
        }finally {
            destroy();
        }
    }

    public JSONObject callDelete(DeleteRequestEntity deleteRequestEntity) {
        if(httpClient == null){
            logger.error("you need init first!");
        }
        if(auth == null || auth == ""){
            logger.error("you need login first!");
        }
        if(deleteRequestEntity.getAuth() == null || deleteRequestEntity.getAuth() == ""){
            deleteRequestEntity.setAuth(auth);
        }
        try {
            System.out.println(deleteRequestEntity);
            HttpUriRequest httpRequest = RequestBuilder.post().setUri(uri)
                    .addHeader("Content-Type", "application/json")
                    .setEntity(new StringEntity(JSON.toJSONString(deleteRequestEntity), ContentType.APPLICATION_JSON)).build();
            CloseableHttpResponse response = httpClient.execute(httpRequest);
            HttpEntity entity = response.getEntity();
            byte[] bytes = EntityUtils.toByteArray(entity);
            return (JSONObject) JSON.parse(bytes);
        }catch (IOException e){
            throw new RuntimeException("DefaultZabbixConnect call exception!", e);
        }finally {
            destroy();
        }
    }

}
