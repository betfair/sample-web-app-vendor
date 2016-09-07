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
 * Helper methods for the application
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
        System.out.printf("\n%s obtained: %s", grantType, grantValue);
        System.out.printf("\nMaking token call with parameters: \n- client_id: %s\n- client_secret: %s\n- grant_type: %s\n- code: %s", CLIENT_ID, SECRET, grantParam, grantValue);

        //Building JSON RPC call
        HttpPost post = new HttpPost(ACCOUNT_API_ENDPOINT);
        HttpEntity entity = new StringEntity("{\"jsonrpc\": \"2.0\"," +
                " \"method\": \"AccountAPING/v1.0/token\"," +
                " \"params\": {\"client_id\":\"" + CLIENT_ID + "\", \"client_secret\":\"" + SECRET + "\",\"grant_type\":\"" + grantType + "\",\"" + grantParam + "\":\"" + grantValue + "\"}," +
                " \"id\": 1" +
                "}");
        post.setHeader("X-Application", APPKEY);
        post.setHeader("X-Authentication", SESSION);
        post.setEntity(entity);

        String response = EntityUtils.toString(httpClient.execute(post).getEntity());

        VendorAccessTokenInfo tokenInfo = GSON.fromJson(response, VendorAccessTokenInfoContainer.class).getResult();

        //logging
        if (tokenInfo == null) {
            AccountApiError error = GSON.fromJson(response, AccountApiErrorContainer.class).getError();
            String errorMessage = error == null ? "\nToken call failure" : "\nToken call failed with error code " + error.getData().getAccountAPINGException().getErrorCode() + ":" + " " + error.getData().getAccountAPINGException().getErrorDetails();
            System.out.println(errorMessage);
        } else {
            System.out.printf("\nAccess token returned along with following information: \n- access_token: %s\n- token_type: %s\n- expires_in: %d\n- refresh_token: %s\n- vendor_client_id: %s",
                    tokenInfo.getAccess_token(), tokenInfo.getToken_type(), tokenInfo.getExpires_in(), tokenInfo.getRefresh_token(), tokenInfo.getApplication_subscription().getVendorClientId());
        }

        return tokenInfo;
    }

    public static AccountFunds getAccountFunds(String accessToken, String tokenType) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(ACCOUNT_API_ENDPOINT);
        HttpEntity entity = new StringEntity("{\"jsonrpc\": \"2.0\"," +
                "\"method\": \"AccountAPING/v1.0/getAccountFunds\"," +
                "\"params\": {}," +
                "\"id\": 1}");
        post.setHeader("X-Application", APPKEY);
        post.setHeader("Authorization", tokenType + " " + accessToken);
        post.setEntity(entity);

        String response = EntityUtils.toString(httpClient.execute(post).getEntity());

        AccountFunds accountFunds = GSON.fromJson(response, AccountFundsContainer.class).getResult();

        //logging
        if (accountFunds == null) {
            AccountApiError error = GSON.fromJson(response, AccountApiErrorContainer.class).getError();
            String errorMessage = error == null ? "\nGet account funds failed" : "\nGet account funds failed with error code " + error.getData().getAccountAPINGException().getErrorCode() + ":" + " " + error.getData().getAccountAPINGException().getErrorDetails();
            System.out.println(errorMessage);
        } else {
            System.out.printf("\ngetAccountFunds made on behalf of customer with the following headers:\n- X-Application: %s (vendor's app key)\n- Authorization: %s %s (token_type + ' ' + access_token)",
                    APPKEY, tokenType, accessToken);
        }

        return accountFunds;
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
