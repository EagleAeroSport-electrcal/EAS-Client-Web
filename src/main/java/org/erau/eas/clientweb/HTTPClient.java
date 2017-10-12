package org.erau.eas.clientweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;

public class HTTPClient {
     String URL;

    public HTTPClient(String URL) {
        this.URL = URL;
    }

    public int getDeviceID(String macAddress) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(URL + "/getid");

        StringEntity entity = new StringEntity(macAddress);
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-type", "text/html; charset=utf-8");

        CloseableHttpResponse response = client.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());
        int toReturn = Integer.parseInt(responseBody);
        client.close();
        return toReturn;
    }

    public int getFlightID() throws IOException{
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(URL + "/getflight");

        CloseableHttpResponse response = client.execute(httpGet);
        String responseBody = EntityUtils.toString(response.getEntity());
        int roReturn = Integer.parseInt(responseBody);
        client.close();
        return roReturn;
    }

    public void sendSensorSet(String sensorSetJSON) throws IOException{
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(URL + "sensorconfig");

        ObjectMapper mapper = new ObjectMapper();
        StringEntity entity = new StringEntity(sensorSetJSON);
        httpPut.setEntity(entity);
        httpPut.setHeader("Content-type", "application/json; charset=utf-8");

        CloseableHttpResponse response = client.execute(httpPut);
        client.close();
    }
}
