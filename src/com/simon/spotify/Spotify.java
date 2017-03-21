package com.simon.spotify;


import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.simon.sonos.item;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.CurrentUserRequest;
import com.wrapper.spotify.methods.UserPlaylistsRequest;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.*;

import java.util.ArrayList;

/**
 * Created by simon on 3/8/2017.
 */
public class Spotify {
    public static  String clientId = ""; // inser client public key
    public static  String clientSecret = ""; // insert client sercet key
    public static String userid = "";// inser user name
    public Spotify(String clientId,String clientSecret,String userid) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.userid = userid;
    }

    public static Api api;

    public static ArrayList<item> GetPublicPlayLists() {
        System.out.println("dav123");
        System.out.println
                (new Exception().getStackTrace()[0].getMethodName());
        ArrayList<item> data = new ArrayList<>();


        api = Api.builder()
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

    /* Set access token on the Api object so that it's used going forward */
                api.setAccessToken(clientCredentials.getAccessToken());

    /* Please note that this flow does not return a refresh token.
   * That's only for the Authorization code flow */

                final UserPlaylistsRequest request = api.getPlaylistsForUser(userid).build();

                try {
                    final Page<SimplePlaylist> playlistsPage = request.get();

                    for (SimplePlaylist playlist : playlistsPage.getItems()) {
                        System.out.println(playlist.getName() + " : "+ playlist.getId() + " : "+ playlist.getUri() + " : track count ="+ playlist.getTracks().getTotal());

                        item i = new item();
                        i.title = playlist.getName();
                        i.creator = playlist.getOwner().getId();
                        i.uri = playlist.getId();
                        data.add(i);
                    }
                } catch (Exception e) {
                    System.out.println("Something went wrong!" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
    /* An error occurred while getting the access token. This is probably caused by the client id or
     * client secret is invalid. */
                System.out.println("Something went wrong! 2" );
            throwable.printStackTrace();
            throwable.printStackTrace(System.out);
            }
        });
    return data;
    }

}
