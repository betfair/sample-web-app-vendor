package com.betfair.vendor;

import com.betfair.vendor.domain.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Created by MezeretN on 02/08/2016.
 */
public class Helper {

    private static Gson GSON = new Gson();
    private static HttpClient httpClient = HttpClients.createDefault();
    private static String APPKEY, SECRET, SESSION, LOGIN_ENDPOINT, ACCOUNT_API_ENDPOINT, USERNAME, PASSWORD, CLIENT_ID;
    public static Properties properties;

    static {
        properties = new Properties();
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
        if (stream != null) {
            try {
                properties.load(stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        APPKEY = (String) properties.get("application.key");
        SECRET = (String) properties.get("client.secret");
        LOGIN_ENDPOINT = (String) properties.get("login.endpoint");
        ACCOUNT_API_ENDPOINT = (String) properties.get("account.api.endpoint");
        USERNAME = (String) properties.get("username");
        PASSWORD = (String) properties.get("password");
        CLIENT_ID = (String) properties.get("client.id");
        try {
            vendorBetfairLogin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void vendorBetfairLogin() throws IOException {
        HttpPost post = new HttpPost(LOGIN_ENDPOINT);
        HttpEntity entity = new StringEntity("username="+USERNAME+"&password="+PASSWORD);
        post.setHeader("Accept","application/json");
        post.setHeader("Content-Type","application/x-www-form-urlencoded");
        post.setHeader("X-Application", APPKEY);
        post.setEntity(entity);
        LoginResponse response = GSON.fromJson(EntityUtils.toString(httpClient.execute(post).getEntity()), LoginResponse.class);
        SESSION = response.getToken();

    }


    public static VendorAccessTokenInfo token(String grantValue, String grantType, String grantParam) throws IOException {
        HttpPost post = new HttpPost(ACCOUNT_API_ENDPOINT);
        HttpEntity entity = new StringEntity("{\"jsonrpc\": \"2.0\"," +
                " \"method\": \"AccountAPING/v1.0/token\"," +
                " \"params\": {\"client_id\":\"" + CLIENT_ID + "\", \"client_secret\":\"" + SECRET + "\",\"grant_type\":\"" + grantType + "\",\"" + grantParam + "\":\"" + grantValue + "\"}," +
                " \"id\": 1" +
                "}");
        post.setHeader("X-Application", APPKEY);
        post.setHeader("X-Authentication", SESSION);
        post.setEntity(entity);

        return GSON.fromJson(EntityUtils.toString(httpClient.execute(post).getEntity()), VendorAccessTokenInfoContainer.class).getResult();

    }

    public static AccountFunds getAccountFunds(String accessToken) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(ACCOUNT_API_ENDPOINT);
        HttpEntity entity = new StringEntity("{\"jsonrpc\": \"2.0\"," +
                "\"method\": \"AccountAPING/v1.0/getAccountFunds\"," +
                "\"params\": {}," +
                "\"id\": 1}");
        post.setHeader("X-Application", APPKEY);
        post.setHeader("Authorization", "BEARER " + accessToken);
        post.setEntity(entity);

        return GSON.fromJson(EntityUtils.toString(httpClient.execute(post).getEntity()), AccountFundsContainer.class).getResult();

    }

    public static CredStatus isValidInput(DomainAccount creds, List<DomainAccount> activeAccounts) {
        for (DomainAccount account : activeAccounts) {
            if (account.getUsername().equals(creds.getUsername())) {
                if (account.getPassword().equals(creds.getPassword())) {
                    return CredStatus.VALID;
                } else {
                    return CredStatus.INVALID;
                }
            }
        }
        return CredStatus.NEW;
    }

}
