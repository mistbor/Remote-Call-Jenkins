package com.weiqing.snarl.app.jenkins;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author miaoying
 * @date 4/2/18
 */
@Slf4j
public class JenkinsBuildService {
    private String jenkinsHost = "192.168.128.104";
    private int jenkinsPort = 8080;
    private String jenkinsUser = "niushiyu";
    private String jenkinsPassword = "niushiyu";
    private CloseableHttpClient httpClient = HttpClientPool.getHttpClient();

    public void build(String jobName, Map<String, String> parameters) {
        String format = "http://%s:%s/job/%s/buildWithParameters";
        CrumbEntity crumbEntity = getCrumb();
        HttpPost httpPost = new HttpPost(String.format(format, jenkinsHost, jenkinsPort, jobName));
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (String key : parameters.keySet()) {
            params.add(new BasicNameValuePair(key, parameters.get(key)));
        }

        UrlEncodedFormEntity urlEntity = new UrlEncodedFormEntity(params, Consts.UTF_8);
        CloseableHttpResponse rsp = null;

        try {
            httpPost.setEntity(urlEntity);
            httpPost.addHeader(crumbEntity.getCrumbRequestField(), crumbEntity.getCrumb());
            rsp = httpClient.execute(httpPost, this.getHttpClientContext());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HttpUtil.close(rsp);
            format = null;
            crumbEntity = null;
            httpPost = null;
            params.clear();
            parameters.clear();
            params = null;
            parameters = null;
        }
    }

    public CrumbEntity getCrumb() {
        String format = "http://%s:%s/crumbIssuer/api/json";
        CloseableHttpResponse rsp = null;
        HttpGet httpGet = new HttpGet(String.format(format, jenkinsHost, jenkinsPort));
        try {
            rsp = httpClient.execute(httpGet, this.getHttpClientContext());
            String jsonResult = EntityUtils.toString(rsp.getEntity());
            CrumbEntity crumbEntity = JSONObject.parseObject(jsonResult, CrumbEntity.class);
            return crumbEntity;
        } catch (Exception e) {
            log.error(null, e);
        } finally {
            HttpUtil.close(rsp);
            format = null;
            httpGet = null;
        }
        return null;
    }

    private HttpHost getHttpHost() {
        return new HttpHost(jenkinsHost, jenkinsPort);
    }

    private CredentialsProvider getCredentialProvider() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(this.getHttpHost().getHostName(), this.getHttpHost().getPort()),
                new UsernamePasswordCredentials(jenkinsUser, jenkinsPassword)
        );
        return credentialsProvider;
    }

    private AuthCache getAuthCache() {
        AuthCache authCache = new BasicAuthCache();
        BasicScheme basicScheme = new BasicScheme();
        authCache.put(getHttpHost(), basicScheme);
        return authCache;
    }

    private HttpClientContext getHttpClientContext() {
        HttpClientContext httpClientContext = HttpClientContext.create();
        httpClientContext.setCredentialsProvider(this.getCredentialProvider());
        httpClientContext.setAuthCache(this.getAuthCache());
        return httpClientContext;
    }
}
