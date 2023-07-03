package no.nav.onpremstatuspoll.util;

import com.nimbusds.oauth2.sdk.*;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.token.AccessToken;

import java.io.IOException;
import java.net.*;

public class OauthUtil {
    // Construct the client credentials grant
    private static String CLIENT_SECRET = System.getenv("AZURE_APP_CLIENT_SECRET");
    private static String CLIENT_ID = System.getenv("AZURE_APP_CLIENT_ID");
    private static String TENANT = System.getenv("TENANT");
    private static String ENV = System.getenv("ENV");
    private static ClientID clientID = new ClientID(CLIENT_ID);
    private static Secret clientSecret = new Secret(CLIENT_SECRET);
    private static ClientAuthentication clientAuth = new ClientSecretBasic(clientID, clientSecret);
    private static Scope scope = new Scope("api://"+ENV+"-gcp.navdig.portalserver/.default");





    public static String getAccessTokenForPortal() throws URISyntaxException, IOException, ParseException {

        AuthorizationGrant clientGrant = new ClientCredentialsGrant();
        // The token endpoint
        URI tokenEndpoint =  new URI("https://login.microsoftonline.com/"+TENANT+"/oauth2/v2.0/token");
        System.out.println(System.getenv("JAVA_PROXY_OPTIONS"));
        // Make the token request
        HTTPRequest request = new TokenRequest(tokenEndpoint, clientAuth, clientGrant, scope).toHTTPRequest();

        SocketAddress addr = new InetSocketAddress("webproxy.nais", 8088);
        Proxy proxy = new Proxy(Proxy.Type.HTTP,addr);
        request.setProxy(proxy);
        TokenResponse response = TokenResponse.parse(request.send());

        if (!response.indicatesSuccess()) {
            // We got an error response...
            TokenErrorResponse errorResponse = response.toErrorResponse();
            System.out.println(errorResponse.toErrorResponse().toJSONObject());
        }

        AccessTokenResponse successResponse = response.toSuccessResponse();

        // Get the access token
        AccessToken accessToken = successResponse.getTokens().getAccessToken();

        return accessToken.toJSONString();
    }
}
