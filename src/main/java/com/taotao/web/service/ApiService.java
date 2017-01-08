package com.taotao.web.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.web.httpclient.HttpResult;

@Service
public class ApiService implements BeanFactoryAware {

    @Autowired
    private RequestConfig requestConfig;

    private BeanFactory beanFactory;

    /**
     * 无参数的get请求
     * 
     * @param url
     * @return
     * @throws IOException
     */
    public String doGet(String url) throws IOException {

        // 创建http GET请求
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(this.requestConfig);
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = this.getHttpClient().execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    /**
     * 带参数的get请求
     * 
     * @param url
     * @param params
     * @return
     * @throws URISyntaxException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String doGet(String url, Map<String, String> params) throws URISyntaxException,
            ClientProtocolException, IOException {
        // 定义请求的参数
        URIBuilder uriBuilder = new URIBuilder(url);
        for (Map.Entry<String, String> param : params.entrySet()) {
            uriBuilder.setParameter(param.getKey(), param.getValue());
        }
        return this.doGet(uriBuilder.build().toString());
    }

    /**
     * 带有参数的post请求
     * 
     * @param url
     * @param params
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public HttpResult doPost(String url, Map<String, String> params) throws ClientProtocolException,
            IOException {
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(this.requestConfig);
        // 设置2个post参数，一个是scope、一个是q
        if (params != null) {
            List<NameValuePair> parameters = new ArrayList<NameValuePair>(0);
            for (Map.Entry<String, String> param : params.entrySet()) {
                parameters.add(new BasicNameValuePair(param.getKey(), param.getValue()));
            }
            // 构造一个form表单式的实体
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(formEntity);
        }

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = this.getHttpClient().execute(httpPost);
            return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
                    response.getEntity(), "UTF-8"));
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * 不带参数的post请求
     * 
     * @param url
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public HttpResult doPost(String url) throws ClientProtocolException, IOException {
        return this.doPost(url, null);
    }

    // 由factorybean创建多例对象
    private CloseableHttpClient getHttpClient() {
        return this.beanFactory.getBean(CloseableHttpClient.class);
    }
    
    //该方法是在spring容易初始化的时候调用,并传入beanFactory
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
