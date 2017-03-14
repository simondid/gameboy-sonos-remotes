package com.simon.spotify;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.UserPlaylistsRequest;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.SimplePlaylist;

import static org.jsoup.helper.Validate.fail;

/**
 * Created by simon on 3/7/2017.
 */
public class applicatioAuthentication {

    public applicatioAuthentication() {

        final String clientId = "fa58fc5646fe4944a33be5eafaa6ab8a";
        final String clientSecret = "ddf8b0bc909b46219f428f714dc9de32";

        final Api api = Api.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();

    /* Create a request object. */
        final ClientCredentialsGrantRequest request = api.clientCredentialsGrant().build();


    /* Use the request object to make the request, either asynchronously (getAsync) or synchronously (get) */
        final SettableFuture<ClientCredentials> responseFuture = request.getAsync();

    /* Add callbacks to handle success and failure */
        Futures.addCallback(responseFuture, new FutureCallback<ClientCredentials>() {
            @Override
            public void onSuccess(ClientCredentials clientCredentials) {
        /* The tokens were retrieved successfully! */
                System.out.println("Successfully retrieved an access token! " + clientCredentials.getAccessToken());
                System.out.println("The access token expires in " + clientCredentials.getExpiresIn() + " seconds");

        /* Please note that this flow does not return a refresh token.
         * That's only for the Authorization code flow */
            }

            @Override
            public void onFailure(Throwable throwable) {
        /* An error occurred while getting the access token. This is probably caused by the client id or
         * client secret is invalid. */
                fail("Failed to resolve future: " + throwable.getMessage());
            }
        });

    }
}
