package com.betfair.vendor.web;

import com.betfair.vendor.domain.AccountFunds;
import com.betfair.vendor.domain.CredStatus;
import com.betfair.vendor.domain.DomainAccount;
import com.betfair.vendor.domain.VendorAccessTokenInfo;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.betfair.vendor.Helper.*;

/**
 * The Main Controller class for the application
 */
@Controller
public class MainController implements ErrorController{

    private Map<String, VendorAccessTokenInfo> sessionInformation = new HashMap<>();
    private List<DomainAccount> activeAccounts = new ArrayList<>();
    private Map<String, String> usernameToId = new HashMap<>();

    @ModelAttribute("client_id")
    public String addClientId() {
        return properties.getProperty("client.id");
    }

    @ModelAttribute("login_redirect_endpoint")
    public String addRedirectEndpoint() {
        return properties.getProperty("login.redirect.endpoint");
    }

    //MAPPINGS FOR SAMPLE APP

    //Landing page
    @RequestMapping("/")
    public String index() throws IOException {
        return "index";
    }

    //Page to request Betfair login from user
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    //Page the user is redirected to after logging in on Betfair. Mapping value must match redirect URL from your vendor application.
    @RequestMapping(value = "/use-auth-code", method=RequestMethod.GET)
    public String useAuthCode(@RequestParam String code,
                              @ModelAttribute DomainAccount domainAccount) throws IOException {

        //Exchanging the received authorisation code for the access token
        VendorAccessTokenInfo vendorAccessTokenInfo = token(code, "AUTHORISATION_CODE", "code");

        //Storing this information keyed by VendorClientId
        sessionInformation.put(vendorAccessTokenInfo.getApplication_subscription().getVendorClientId(), vendorAccessTokenInfo);

        //Propagating vendor client ID to account creation page
        domainAccount.setId(vendorAccessTokenInfo.getApplication_subscription().getVendorClientId());

        return "accountcreation";
    }

    @RequestMapping(value = "/welcome", method=RequestMethod.POST)
    public String welcome(@ModelAttribute DomainAccount domainAccount) {

        //Basic account management: mapping vendor client ID to username, and adding account to active accounts
        usernameToId.put(domainAccount.getUsername(), domainAccount.getId());
        activeAccounts.add(domainAccount);

        System.out.printf("Registered account with vendorClientId %s under username %s", domainAccount.getId(), domainAccount.getUsername());

        return "welcome";
    }

    //Page greeting a returning customer
    @RequestMapping("/returning")
    public String returning(@ModelAttribute DomainAccount domainAccount) {
        return "returning";
    }

    //Refreshing the access token for existing customers
    @RequestMapping("/refresh")
    public String refresh(@ModelAttribute DomainAccount domainAccount) throws IOException {
        //Checking if customer credentials are valid
        if (!(isValidInput(domainAccount, activeAccounts) == CredStatus.VALID)) {
            return "index";
        }

        //Get vendor client ID from username
        String id = usernameToId.get(domainAccount.getUsername());

        //Get refresh token from stored session information
        String refreshToken = sessionInformation.get(id).getRefresh_token();

        //Calling token with refresh token grant type to refresh access token
        VendorAccessTokenInfo vendorAccessTokenInfo = token(refreshToken, "REFRESH_TOKEN", "refresh_token");

        //store new session information
        sessionInformation.put(id, vendorAccessTokenInfo);

        //propagate vendor client id
        domainAccount.setId(id);

        return "refresh";
    }

    //Get Funds call
    @RequestMapping(value = "/get-funds", method = RequestMethod.POST)
    public String getFunds(@ModelAttribute DomainAccount domainAccount,
                           @ModelAttribute AccountFunds accountFunds,
                           Model model) throws IOException {

        VendorAccessTokenInfo vendorAccessTokenInfo = sessionInformation.get(domainAccount.getId());
        //get access token and token type for vendor client id
        String accessToken = vendorAccessTokenInfo.getAccess_token();
        String tokenType = vendorAccessTokenInfo.getToken_type().toString();

        //call getAccountFunds with app key and access token
        accountFunds = getAccountFunds(accessToken, tokenType);

        model.addAttribute(accountFunds);

        return "myaccount";
    }


    //MAPPINGS FOR ODDS PUBLISHER

    //Landing page
    @RequestMapping("/publisher")
    public String indexPub() throws IOException {
        return "indexPub";
    }


    //Logging triggered by clicking on a price. Once logged in, redirected to here based on redirect URL + url params passed in the login call (see indexPub.html)
    @RequestMapping(value = "/use-auth-code-pub", method = RequestMethod.GET)
    public String useAuthCodePub(@RequestParam String code,
                                 @ModelAttribute DomainAccount account,
                                 @ModelAttribute AccountFunds accountFunds,
                                 Model model) throws IOException {

        //Exchange authorisation code for access token with token() call
        VendorAccessTokenInfo vendorAccessTokenInfo = token(code, "AUTHORISATION_CODE", "code");

        //Store information for that vendor client ID
        sessionInformation.put(vendorAccessTokenInfo.getApplication_subscription().getVendorClientId(), vendorAccessTokenInfo);

        String accessToken = vendorAccessTokenInfo.getAccess_token();
        String tokenType = vendorAccessTokenInfo.getToken_type().toString();

        //call getAccountFunds with access token
        accountFunds = getAccountFunds(accessToken, tokenType);

        model.addAttribute(accountFunds);


        return "betPub";
    }


    //mapping for error page
    @RequestMapping("/error")
    public String error() {
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
