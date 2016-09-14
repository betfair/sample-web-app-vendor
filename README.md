#Sample Web App Vendor
A simple demo app that integrates with Betfair's OAuth2 flow, and gets the user's Betfair balance on their behalf.
To run, enter your details in the config.properties file and run the main method in Application.java.

Two endpoints are available:
###ODDS PUBLISHER (localhost:8080/publisher when run locally)
A dummy odds publishing website. The user is redirected to the Betfair login page by clicking on a price. This redirect has to include the vendor’s client id.
The user needs to enter their Betfair credentials and agree to the permissions page. They will then be redirected back to the odds publisher.
This redirect will contain an authorization code, which is passed down to the server side and exchanged for an access token.
The server can now make calls on the user’s behalf using the vendor app key and the access token (in this case, a getAccountFunds call).

###SAMPLE WEB APP (localhost:8080 when run locally)
A slightly more elaborate example that keeps track of customers using its own account system. 
On the first visit, user would click new joiner, log in to Betfair, and accept the permissions.
The same exchange of authorization code for access token happens as above. Here we also pay attention other fields that are returned:
-              vendorClientId, unique identifier for a Betfair customer
-              Refresh token

The user creates an account with the app, and all of this information is saved alongside it.
Next time the user visits, they can choose ‘I Have an Account’ and enter their web app credentials. This time, there is no need for a Betfair login: the access token can be obtained with the refresh token.
