package com.betfair.vendor.web;

import com.betfair.vendor.domain.*;
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

@Controller
public class MainController implements ErrorController{

    private Map<String, VendorAccessTokenInfo> sessionInformation = new HashMap<>();
    private List<DomainAccount> activeAccounts = new ArrayList<>();
    private Map<String, String> usernameToId = new HashMap<>();

    @ModelAttribute("client_id")
    public String addClientId() {
        return properties.getProperty("client.id");
    }

    @ModelAttribute("app_key")
    public String addAppKey() {
        return properties.getProperty("application.key");
    }

    @ModelAttribute("client_secret")
    public String addClientSecret() {
        return properties.getProperty("client.secret");
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
                              @ModelAttribute AuthorisationCode authorisationCode,
                              @ModelAttribute VendorAccessTokenInfo vendorAccessTokenInfo,
                              @ModelAttribute DomainAccount domainAccount,
                              Model model) throws IOException {

        //Exchanging the received authorisation code for the access token
        vendorAccessTokenInfo = token(code, "AUTHORISATION_CODE", "code");

        //Storing this information keyed by VendorClientId
        sessionInformation.put(vendorAccessTokenInfo.getApplication_subscription().getVendorClientId(), vendorAccessTokenInfo);

        //Propagating vendor client ID to account creation page
        model.addAttribute(authorisationCode);
        model.addAttribute(vendorAccessTokenInfo);
        domainAccount.setId(vendorAccessTokenInfo.getApplication_subscription().getVendorClientId());
        model.addAttribute("domainAccount", domainAccount);

        return "accountcreation";
    }

    @RequestMapping(value = "/welcome", method=RequestMethod.POST)
    public String welcome(@ModelAttribute DomainAccount domainAccount) {

        //Basic account management: mapping vendor client ID to username, and adding account to active accounts
        usernameToId.put(domainAccount.getUsername(), domainAccount.getId());
        activeAccounts.add(domainAccount);

        return "welcome";

    }

    //Page greeting a returning customer
    @RequestMapping("/returning")
    public String returning(@ModelAttribute DomainAccount domainAccount) {
        return "returning";
    }

    //Refreshing the access token for existing customers
    @RequestMapping("/refresh")
    public String refresh(@ModelAttribute DomainAccount domainAccount,
                          @ModelAttribute VendorAccessTokenInfo vendorAccessTokenInfo,
                          Model model) throws IOException {
        //Checking if customer credentials are valid
        if (!(isValidInput(domainAccount, activeAccounts) == CredStatus.VALID)) {
            return "index";
        }

        //Get vendor client ID from username
        String id = usernameToId.get(domainAccount.getUsername());

        //Get refresh token from stored session information
        String refreshToken = sessionInformation.get(id).getRefresh_token();

        //Calling token with refresh token grant type to refresh access token
        vendorAccessTokenInfo = token(refreshToken, "REFRESH_TOKEN", "refresh_token");

        //store new session information
        sessionInformation.put(id, vendorAccessTokenInfo);

        //propagate vendor client id
        domainAccount.setId(id);
        model.addAttribute("domainAccount", domainAccount);
        model.addAttribute("vendorAccessTokenInfo", vendorAccessTokenInfo);

        return "refresh";
    }

    //Get Funds call
    @RequestMapping(value = "/get-funds", method = RequestMethod.POST)
    public String getFunds(@ModelAttribute DomainAccount domainAccount,
                           @ModelAttribute AccountFunds accountFunds,
                           @ModelAttribute VendorAccessTokenInfo vendorAccessTokenInfo,
                           Model model) throws IOException {

        //get access token for vendor client id
        vendorAccessTokenInfo = sessionInformation.get(domainAccount.getId());
        String accessToken = vendorAccessTokenInfo.getAccess_token();

        //call getAccountFunds with app key and access token
        accountFunds = getAccountFunds(accessToken);

        model.addAttribute(accountFunds);
        model.addAttribute(vendorAccessTokenInfo);

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
                                 @ModelAttribute AuthorisationCode authorisationCode,
                                 @ModelAttribute AccountFunds accountFunds,
                                 @ModelAttribute VendorAccessTokenInfo vendorAccessTokenInfo,
                                 Model model) throws IOException {

        //Exchange authorisation code for access token with token() call
        vendorAccessTokenInfo = token(code, "AUTHORISATION_CODE", "code");

        //Store information for that vendor client ID
        sessionInformation.put(vendorAccessTokenInfo.getApplication_subscription().getVendorClientId(), vendorAccessTokenInfo);

        String accessToken = vendorAccessTokenInfo.getAccess_token();

        //call getAccountFunds with access token
        accountFunds = getAccountFunds(accessToken);

        model.addAttribute(accountFunds);
        model.addAttribute("vendorAccessTokenInfo", vendorAccessTokenInfo);
        model.addAttribute("authorisationCode", authorisationCode);

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
