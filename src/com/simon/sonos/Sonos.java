package com.simon.sonos;

import com.simon.Main;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;


class browseItem {
    int numberReturned,TotalMatches,updateID;
    String result;


    ArrayList<trackData> tracks = new ArrayList<>();



public void print(){
    System.out.println("Number Returned : "+numberReturned);
    System.out.println("Total matches : "+TotalMatches);
    System.out.println("update id : "+updateID);
    System.out.println("Result : "+result);
    System.out.println("");
    for (int i =0;i<tracks.size();i++){
        System.out.println("item id : "+tracks.get(i).itemID);
        System.out.println("parent id : "+tracks.get(i).parentID);
        System.out.println("Name : "+tracks.get(i).name);
        System.out.println("Creator : "+tracks.get(i).creator);
        System.out.println("Album : "+tracks.get(i).album);
        System.out.println("Duratition : "+tracks.get(i).duration);

        System.out.println("");
    }
}

}
class trackData{
    String name,creator,album,enqueuedURI,itemID,parentID;
    String duration;
    public static trackData analyse(String item){
     trackData data = new trackData();


     return data;
    }
    public void print(){

            System.out.println("item id : "+itemID);
            System.out.println("parent id : "+parentID);
            System.out.println("Name : "+name);
            System.out.println("Creator : "+creator);
            System.out.println("Album : "+album);
            System.out.println("Duratition : "+duration);

            System.out.println("");

    }
    public void track(String name,String creator,String album,String duration,String enqueuedURI,String itemID,String parentID) {
        this.name = name;
        this.creator = creator;
        this.album = album;
        this.duration = duration;
        this.enqueuedURI = enqueuedURI;
        this.itemID = itemID;
        this.parentID = parentID;

    }

}

public class Sonos {
    String sonos_set_queue_0 = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body><u:AddURIToQueue xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\"><InstanceID>0</InstanceID><EnqueuedURI>x-rincon-cpcontainer:1006206cspotify%3auser%3a";
    String sonos_set_queue_1 = ""; // username
    String sonos_set_queue_2 = "%3aplaylist%3a";
    String sonos_set_queue_3 = ""; // playlist id
    String sonos_set_queue_4 = "</EnqueuedURI><EnqueuedURIMetaData>&lt;DIDL-Lite xmlns:dc=&quot;http://purl.org/dc/elements/1.1/&quot; xmlns:upnp=&quot;urn:schemas-upnp-org:metadata-1-0/upnp/&quot; xmlns:r=&quot;urn:schemas-rinconnetworks-com:metadata-1-0/&quot; xmlns=&quot;urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/&quot;&gt;&lt;item id=&quot;1006206cspotify%3auser%3a";
    String sonos_set_queue_5 = ""; // username
    String sonos_set_queue_6 = "%3aplaylist%3a";
    String sonos_set_queue_7 = ""; // playlist id
    String sonos_set_queue_8 = "&quot; parentID=&quot;10082664playlists&quot; restricted=&quot;true&quot;&gt;&lt;dc:title&gt;";
    String sonos_set_queue_9 = ""; // play list name
    String sonos_set_queue_10 = "&lt;/dc:title&gt;&lt;upnp:class&gt;object.container.playlistContainer&lt;/upnp:class&gt;&lt;desc id=&quot;cdudn&quot; nameSpace=&quot;urn:schemas-rinconnetworks-com:metadata-1-0/&quot;&gt;SA_RINCON2311_X_#Svc2311-0-Token&lt;/desc&gt;&lt;/item&gt;&lt;/DIDL-Lite&gt;</EnqueuedURIMetaData><DesiredFirstTrackNumberEnqueued>0</DesiredFirstTrackNumberEnqueued><EnqueueAsNext>0</EnqueueAsNext></u:AddURIToQueue></s:Body></s:Envelope>";


    private static String ipAdress;
    public Sonos(String ipAddress) {
        this.ipAdress = ipAddress;

    }

    public void playSpotifyPlayList(item activeListItem) {
        try {
            playSpotifyPlayList(activeListItem.creator,activeListItem.uri,activeListItem.title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void playSpotifyPlayList(String username,String playlist,String PlayListName) throws IOException {
        System.out.println
                (new Exception().getStackTrace()[0].getMethodName());

        URL url = new URL("http://" + ipAdress + ":1400/MediaRenderer/AVTransport/Control");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();

        request.setRequestMethod("POST");
        request.addRequestProperty("SOAPACTION", "\"urn:schemas-upnp-org:service:AVTransport:1#AddURIToQueue\"");
        request.setDoOutput(true);
        request.setReadTimeout(152000);

        sonos_set_queue_1 = username;
        sonos_set_queue_5 = username;
        sonos_set_queue_3 = playlist;
        sonos_set_queue_7 = playlist;
        sonos_set_queue_9 = PlayListName;
        String temp = sonos_set_queue_0+sonos_set_queue_1+sonos_set_queue_2+sonos_set_queue_3+sonos_set_queue_4+sonos_set_queue_5+sonos_set_queue_6+sonos_set_queue_7+sonos_set_queue_8+sonos_set_queue_9+sonos_set_queue_10;
        OutputStreamWriter input = new OutputStreamWriter(request.getOutputStream());

        input.write(temp);
        input.write("");



        input.flush();


        BufferedReader output = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        String oneResponse = new String();
        String line;
        while ((line = output.readLine()) != null) {
            oneResponse += line + "\r\n";
        }
        System.out.println(oneResponse);

        Main.getQueue();
    }
    public static browse Browse(String objectID,boolean BrowseFlag,String Filter,int StartingIndex,int RequestCount,String SortCiteria) throws IOException {
        URL url = new URL("http://" + ipAdress + ":1400/MediaServer/ContentDirectory/Control");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();

        request.setRequestMethod("POST");
        request.addRequestProperty("SOAPACTION", "urn:schemas-upnp-org:service:ContentDirectory:1#Browse");
        request.setDoOutput(true);
        request.setReadTimeout(9000);


        OutputStreamWriter input = new OutputStreamWriter(request.getOutputStream());
        String flag;
        if(BrowseFlag){
            flag = "BrowseDirectChildren";
        }else{
            flag = "BrowseMetadata";
        }

        input.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<s:Envelope s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "   <s:Body>\n" +
                "      <u:Browse xmlns:u=\"urn:schemas-upnp-org:service:ContentDirectory:1\">\n" +
                "         <ObjectID>"+objectID+"</ObjectID>\n" +
                "         <BrowseFlag>"+flag+"</BrowseFlag>\n" +
                "        <Filter>"+Filter+"</Filter>"+
                "         <StartingIndex>"+StartingIndex+"</StartingIndex>\n" +
                "         <RequestedCount>"+RequestCount+"</RequestedCount>\n" +
                "         <SortCriteria>"+SortCiteria+"</SortCriteria>\n" +
                "      </u:Browse>\n" +
                "   </s:Body>\n" +
                "</s:Envelope>");
        input.write("");




        input.flush();


        BufferedReader output = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        String oneResponse = new String();
        String line;
        while ((line = output.readLine()) != null) {
            // oneResponse += line + "\r\n";
            oneResponse +=line;
        }

        System.out.println(oneResponse);

        browse data = new browse();
        data.result = html2text(oneResponse.substring(oneResponse.indexOf("<Result>")+"<Result>".length(),oneResponse.indexOf("</Result>")));
        //gettting NumberReturned
        data.numberReturned= new Integer(oneResponse.substring(oneResponse.indexOf("<NumberReturned>")+"<NumberReturned>".length(),oneResponse.indexOf("</NumberReturned>")).toString());
        //gettting TotalMatches
        data.TotalMatches= new Integer(oneResponse.substring(oneResponse.indexOf("<TotalMatches>")+"<TotalMatches>".length(),oneResponse.indexOf("</TotalMatches>")).toString());
        //gettting updateID
        data.updateID= new Integer(oneResponse.substring(oneResponse.indexOf("<UpdateID>")+"<UpdateID>".length(),oneResponse.indexOf("</UpdateID>")).toString());

        return data;
    }

    public static ArrayList<item> ItemAnalyser(String Container) throws IOException {
        int maxSize =1;
        int StartingIndex =0;
        ArrayList<item> list = new ArrayList<>();
        for(int r =0;r<maxSize;r++) {
            String objectid = Container;
            boolean BrowseFlag = true;
            String Filter = "";

            int RequestCount = 1000;
            String SortCiteria = "";


            browse input = Browse(objectid, BrowseFlag, Filter, StartingIndex, RequestCount, SortCiteria);
            int matchesCount = input.TotalMatches;
            System.out.println
                    (new Exception().getStackTrace()[0].getMethodName());
            System.out.println(input.result);
            String result = input.result;
            int size = result.indexOf("<item");

            if (size != -1) {
                result = result.replace(result.substring(0, size), "");
                String name, containerId, parentId, protocolInfo, upnpalbumArtURI, upnpClass, uri, creator, album;


                for (int i = 0; i < input.numberReturned; i++) {


                    int i1 = 0;
                    int i2 = result.indexOf("</item>") + "</item>".length();
                    String active = result.substring(i1, i2);

                    i1 = active.indexOf("<dc:title>") + "<dc:title>".length();
                    i2 = active.indexOf("</dc:title>");
                    if (i1 != -1 && i2 != -1) {
                        name = active.substring(i1, i2);
                    } else {
                        name = "null";
                    }

                    i1 = active.indexOf("<item id=\"") + "<item id=\"".length();
                    i2 = active.indexOf("\"", i1);
                    if (i1 != -1 && i2 != -1) {
                        containerId = active.substring(i1, i2);
                    } else {
                        containerId = "null";
                    }

                    i1 = active.indexOf("parentID=\"") + "parentID=\"".length();
                    i2 = active.indexOf("\"", i1);
                    if (i1 != -1 && i2 != -1) {
                        parentId = active.substring(i1, i2);
                    } else {
                        parentId = "null";
                    }

                    i1 = active.indexOf("<res protocolInfo=\"") + "<res protocolInfo=\"".length();
                    i2 = active.indexOf("\"", i1);
                    if (i1 != -1 && i2 != -1) {
                        protocolInfo = active.substring(i1, i2);
                    } else {
                        protocolInfo = "null";
                    }

                    i1 = active.indexOf("<res");
                    int i3 = active.indexOf(">", i1) + 1;
                    i2 = active.indexOf("</res>", i1);
                    if (i1 != -1 && i2 != -1) {
                        uri = active.substring(i3, i2);
                    } else {
                        uri = "null";
                    }


                    i1 = active.indexOf("<upnp:albumArtURI>") + "<upnp:albumArtURI>".length();
                    i2 = active.indexOf("</upnp:albumArtURI>", i1);
                    if (i1 != -1 && i2 != -1) {
                        upnpalbumArtURI = active.substring(i1, i2);
                    } else {
                        upnpalbumArtURI = "null";
                    }
                    i1 = active.indexOf("<upnp:class>") + "<upnp:class>".length();
                    i2 = active.indexOf("</upnp:class>", i1);
                    if (i1 != -1 && i2 != -1) {
                        upnpClass = active.substring(i1, i2);
                    } else {
                        upnpClass = "null";
                    }

                    i1 = active.indexOf("<dc:creator>") + "<dc:creator>".length();
                    i2 = active.indexOf("</dc:creator>");
                    if (i1 != -1 && i2 != -1) {
                        creator = active.substring(i1, i2);
                    } else {
                        creator = "null";
                    }
                    i1 = active.indexOf("<upnp:album>") + "<upnp:album>".length();
                    i2 = active.indexOf("</upnp:album>");
                    if (i1 != -1 && i2 != -1) {
                        album = active.substring(i1, i2);
                    } else {
                        album = "null";
                    }

                    System.out.println(i);
                    System.out.println(active);
                    result = result.replace(active, "");
                    item item = new item(name, containerId, parentId, protocolInfo, upnpalbumArtURI, upnpClass, uri, creator, album);
                    item.print();
                    list.add(item);
                }
            }
        if(list.size()!=matchesCount){
                maxSize++;
                StartingIndex=list.size();

        }
        }
        System.out.println("return size : " + list.size());
        return list;

    }

    public static browse BrowseMusicFolders() throws IOException {

        String objectid="S:";
        boolean BrowseFlag=true;
        String Filter="";
        int StartingIndex=0;
        int RequestCount=100;
        String SortCiteria ="";

        browse input = Browse(objectid,BrowseFlag,Filter,StartingIndex,RequestCount,SortCiteria);
        System.out.print("");
        String result = input.result;
        System.out.println(result);
        System.out.println();
        browse data = new browse();
        data.numberReturned = input.numberReturned;
        data.updateID = input.updateID;
        data.TotalMatches = input.TotalMatches;

         result = result.replace(result.substring(0,result.indexOf("<container")),"");
        String name,containerId,parentId;
        for(int i =0;i<input.numberReturned;i++){
            name = "";
            containerId ="";
            parentId="";

            int i1 = 0;
            int i2 = result.indexOf("</container>") + "</container>".length();
            String active = result.substring(i1, i2);

             i1 = active.indexOf("<dc:title>") + "<dc:title>".length();
             i2 = active.indexOf("</dc:title>");
            if (i1 != -1 && i2 != -1) {
                name = active.substring(i1, i2);
            } else {
                name = "null";
            }

             i1 = active.indexOf("<container id=\"") + "<container id=\"".length();
             i2 = active.indexOf("\"",i1);
            if (i1 != -1 && i2 != -1) {
                containerId = active.substring(i1, i2);
            } else {
                containerId = "null";
            }

            i1 = active.indexOf("parentID=\"") + "parentID=\"".length();
            i2 = active.indexOf("\"",i1);
            if (i1 != -1 && i2 != -1) {
                parentId = active.substring(i1, i2);
            } else {
                parentId = "null";
            }
            System.out.println(i);
            System.out.println(active);
            result = result.replace(active,"");

            data.container.add(new browseContainer(name,containerId,parentId));
        }

        data.print();

        return data;
    }

    /*
    *   BrowseFlag true = direct children :: false = browseMeta data
     */
    public static ArrayList<item> BrowseRadioStations() throws IOException {
/*
        String objectid="R:0/0";
        boolean BrowseFlag=true;
        String Filter="";
        int StartingIndex=0;
        int RequestCount=100;
        String SortCiteria ="";

        browse input = Browse(objectid,BrowseFlag,Filter,StartingIndex,RequestCount,SortCiteria);

        browseItem data = new browseItem();
        trackData track;


        String result = input.result;
*/
        ArrayList<item> l = ItemAnalyser("R:0/0");

        return l;

        /*
        System.out.println("result32");
        System.out.println(result);
        result = result.replace(result.substring(0,result.indexOf("<item")),"");
        if(BrowseFlag) {
            for (int i = 0; i < input.numberReturned; i++) {
                track = new trackData();
                //  int i1 = result.indexOf("<item id=\"")+"<item id=>\"".length();
                int i1 = 0;
                int i2 = result.indexOf("</item>") + "</item>".length();
                String active = result.substring(i1, i2);

                i1 = active.indexOf("<dc:title>") + "<dc:title>".length();
                i2 = active.indexOf("</dc:title>");
                if (i1 != -1 && i2 != -1) {
                    track.name = active.substring(i1, i2);
                } else {
                    track.name = "null";
                }

                i1 = active.indexOf("<dc:creator>") + "<dc:creator>".length();
                i2 = active.indexOf("</dc:creator>");
                if (i1 != -1 && i2 != -1) {
                    track.creator = active.substring(i1, i2);
                } else {
                    track.creator = "null";
                }
                i1 = active.indexOf("<upnp:album>") + "<upnp:album>".length();
                i2 = active.indexOf("</upnp:album>");
                if (i1 != -1 && i2 != -1) {
                    track.album = active.substring(i1, i2);
                } else {
                    track.album = "null";
                }


                i1 = active.indexOf("<item id=\"") + "<item id=\"".length();
                i2 = active.indexOf("\"", i1);
                if (i1 != -1 && i2 != -1) {
                    track.itemID = active.substring(i1, i2);
                } else {
                    track.itemID = "null";
                }
                i1 = active.indexOf("parentID=\"") + "parentID=\"".length();
                i2 = active.indexOf("\"", i1);
                if (i1 != -1 && i2 != -1) {
                    track.parentID = active.substring(i1, i2);
                } else {
                    track.parentID = "null";
                }


                i1 = active.indexOf("duration=\"");
                if (i1 != -1) {
                    i1 = i1 + "duration=\"".length();
                }

                i2 = active.indexOf("\">", i1);
                if (i1 != -1 && i2 != -1) {

                    track.duration = active.substring(i1, i2);
                } else {
                    track.duration = "null";
                }


                result = result.replace(active, "");
                //   System.out.println(active);


                data.tracks.add(track);

            }

        }else{
            // handling browse meta data
            System.out.println("ERROR !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! meta data is unhandled!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
*/
       // data.print();
    }

    public static ArrayList<item> BrowseQueue(int QueueID, int StartingIndex, int RequestedCount) throws IOException {
        System.out.println
                (new Exception().getStackTrace()[0].getMethodName());
/*
        URL url = new URL("http://" + ipAdress + ":1400/MediaRenderer/Queue/Control");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();

        request.setRequestMethod("POST");
        request.addRequestProperty("SOAPACTION", "\"urn:schemas-upnp-org:service:AVTransport:1#Browse\"");
        request.setDoOutput(true);
        request.setReadTimeout(2000);


        OutputStreamWriter input = new OutputStreamWriter(request.getOutputStream());

        input.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<s:Envelope s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "   <s:Body>\n" +
                "      <u:Browse xmlns:u=\"urn:schemas-sonos-com:service:Queue:1\">\n" +
                "         <QueueID>"+QueueID+"</QueueID>\n" +
                "         <StartingIndex>"+StartingIndex+"</StartingIndex>\n" +
                "         <RequestedCount>"+RequestedCount+"</RequestedCount>\n" +
                "      </u:Browse>\n" +
                "   </s:Body>\n" +
                "</s:Envelope>");
        input.write("");



        input.flush();


        BufferedReader output = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        String oneResponse = new String();
        String line;
        while ((line = output.readLine()) != null) {
           // oneResponse += line + "\r\n";
            oneResponse +=line;
        }
        System.out.println(oneResponse);

        browseItem data = new browseItem();
        data.result = oneResponse.substring(oneResponse.indexOf("<Result>")+"<Result>".length(),oneResponse.indexOf("</Result>"));
        //gettting NumberReturned
        data.numberReturned= new Integer(oneResponse.substring(oneResponse.indexOf("<NumberReturned>")+"<NumberReturned>".length(),oneResponse.indexOf("</NumberReturned>")).toString());
        //gettting TotalMatches
        data.TotalMatches= new Integer(oneResponse.substring(oneResponse.indexOf("<TotalMatches>")+"<TotalMatches>".length(),oneResponse.indexOf("</TotalMatches>")).toString());
        //gettting updateID
        data.updateID= new Integer(oneResponse.substring(oneResponse.indexOf("<UpdateID>")+"<UpdateID>".length(),oneResponse.indexOf("</UpdateID>")).toString());

*/
        ArrayList<item> list = ItemAnalyser("Q:0");


        return list;
    }
    public static ArrayList<String> BrowseQueueOld(int QueueID, int StartingIndex, int RequestedCount) throws IOException {
        System.out.println
                (new Exception().getStackTrace()[0].getMethodName());

        URL url = new URL("http://" + ipAdress + ":1400/MediaRenderer/Queue/Control");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();

        request.setRequestMethod("POST");
        request.addRequestProperty("SOAPACTION", "\"urn:schemas-upnp-org:service:AVTransport:1#Browse\"");
        request.setDoOutput(true);
        request.setReadTimeout(2000);


        OutputStreamWriter input = new OutputStreamWriter(request.getOutputStream());

        input.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<s:Envelope s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "   <s:Body>\n" +
                "      <u:Browse xmlns:u=\"urn:schemas-sonos-com:service:Queue:1\">\n" +
                "         <QueueID>"+QueueID+"</QueueID>\n" +
                "         <StartingIndex>"+StartingIndex+"</StartingIndex>\n" +
                "         <RequestedCount>"+RequestedCount+"</RequestedCount>\n" +
                "      </u:Browse>\n" +
                "   </s:Body>\n" +
                "</s:Envelope>");
        input.write("");



        input.flush();


        BufferedReader output = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        String oneResponse = new String();
        String line;
        while ((line = output.readLine()) != null) {
            // oneResponse += line + "\r\n";
            oneResponse +=line;
        }
        System.out.println(oneResponse);

        browseItem data = new browseItem();
        data.result = oneResponse.substring(oneResponse.indexOf("<Result>")+"<Result>".length(),oneResponse.indexOf("</Result>"));
        //gettting NumberReturned
        data.numberReturned= new Integer(oneResponse.substring(oneResponse.indexOf("<NumberReturned>")+"<NumberReturned>".length(),oneResponse.indexOf("</NumberReturned>")).toString());
        //gettting TotalMatches
        data.TotalMatches= new Integer(oneResponse.substring(oneResponse.indexOf("<TotalMatches>")+"<TotalMatches>".length(),oneResponse.indexOf("</TotalMatches>")).toString());
        //gettting updateID
        data.updateID= new Integer(oneResponse.substring(oneResponse.indexOf("<UpdateID>")+"<UpdateID>".length(),oneResponse.indexOf("</UpdateID>")).toString());

        trackData track = new trackData();
        String replace;

  /*      String result = data.result.replaceAll("\\&lt;","<");
        result = result.replaceAll("\\&gt;",">");
        result = result.replaceAll("\\&qout","\"");
*/
        String result = html2text(data.result);
        System.out.println("result32");

        result = result.replace(result.substring(0,result.indexOf("<item")),"");
        //  System.out.println(result);
        for(int i=0;i<data.numberReturned;i++){
            track = new trackData();
            //  int i1 = result.indexOf("<item id=\"")+"<item id=>\"".length();
            int i1=0;
            int i2 =result.indexOf("</item>")+"</item>".length();
            String active = result.substring(i1,i2);

            i1 = active.indexOf("<dc:title>")+"<dc:title>".length();
            i2 =active.indexOf("</dc:title>");
            if(i1!=-1&&i2!=-1) {
                track.name = active.substring(i1,i2);
            }else {
                track.name = "null";
            }

            i1 = active.indexOf("<dc:creator>")+"<dc:creator>".length();
            i2 = active.indexOf("</dc:creator>");
            if(i1!=-1&&i2!=-1) {
                track.creator = active.substring(i1, i2);
            }else {
                track.creator = "null";
            }
            i1 = active.indexOf("<upnp:album>")+"<upnp:album>".length();
            i2 = active.indexOf("</upnp:album>");
            if(i1!=-1&&i2!=-1) {
                track.album = active.substring(i1,i2);
            }else {
                track.album = "null";
            }


            i1 = active.indexOf("<item id=\"")+"<item id=\"".length();
            i2 = active.indexOf("\"",i1);
            if(i1!=-1&&i2!=-1) {
                track.itemID = active.substring(i1,i2);
            }else {
                track.itemID = "null";
            }
            i1 = active.indexOf("parentID=\"")+"parentID=\"".length();
            i2 = active.indexOf("\"",i1);
            if(i1!=-1&&i2!=-1) {
                track.parentID = active.substring(i1,i2);
            }else {
                track.parentID = "null";
            }


            i1 = active.indexOf("duration=\"");
            if(i1!=-1) {
                i1 = i1 + "duration=\"".length();
            }

            i2 = active.indexOf("\">",i1);
            if(i1!=-1&&i2!=-1) {

                track.duration = active.substring(i1,i2);
            }else {
                track.duration = "null";
            }



            result = result.replace(active,"");
            //   System.out.println(active);



            data.tracks.add(track);
        }

        data.print();
        ArrayList<String> l = new ArrayList<>();
        for(int r =0;r<data.tracks.size();r++){
            l.add(data.tracks.get(r).name);
        }

        return l;
    }
    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }
    public static void Previous() throws IOException {
        System.out.println
                (new Exception().getStackTrace()[0].getMethodName());

        URL url = new URL("http://" + ipAdress + ":1400/MediaRenderer/AVTransport/Control");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();

        request.setRequestMethod("POST");
        request.addRequestProperty("SOAPACTION", "\"urn:schemas-upnp-org:service:AVTransport:1#Previous\"");
        request.setDoOutput(true);
        request.setReadTimeout(2000);


        OutputStreamWriter input = new OutputStreamWriter(request.getOutputStream());

        input.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<s:Envelope s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "   <s:Body>\n" +
                "      <u:Previous xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">\n" +
                "         <InstanceID>0</InstanceID>\n" +
                "      </u:Previous>\n" +
                "   </s:Body>\n" +
                "</s:Envelope>");
        input.write("");



        input.flush();


        BufferedReader output = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        String oneResponse = new String();
        String line;
        while ((line = output.readLine()) != null) {
            oneResponse += line + "\r\n";
        }
        System.out.println(oneResponse);
    }
    public static void play() throws IOException {
        System.out.println
                (new Exception().getStackTrace()[0].getMethodName());

        URL url = new URL("http://" + ipAdress + ":1400/MediaRenderer/AVTransport/Control");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();

        request.setRequestMethod("POST");
        request.addRequestProperty("SOAPACTION", "\"urn:schemas-upnp-org:service:AVTransport:1#Play\"");
        request.setDoOutput(true);
        request.setReadTimeout(2000);


        OutputStreamWriter input = new OutputStreamWriter(request.getOutputStream());

        input.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<s:Envelope s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "   <s:Body>\n" +
                "      <u:Play xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">\n" +
                "         <InstanceID>0</InstanceID>\n" +
                "         <Speed>1</Speed>\n" +
                "      </u:Play>\n" +
                "   </s:Body>\n" +
                "</s:Envelope>");
        input.write("");



        input.flush();


        BufferedReader output = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        String oneResponse = new String();
        String line;
        while ((line = output.readLine()) != null) {
            oneResponse += line + "\r\n";
        }
        System.out.println(oneResponse);
    }
    public static class posisitionInfo{

        public String Duratation,RelTime;

        public posisitionInfo(String duratation, String relTime) {
            Duratation = duratation;
            RelTime = relTime;
        }
    }
    public static posisitionInfo getPosistionInfo() throws IOException {
        System.out.println
                (new Exception().getStackTrace()[0].getMethodName());

        URL url = new URL("http://" + ipAdress + ":1400/MediaRenderer/AVTransport/Control");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();

        request.setRequestMethod("POST");
        request.addRequestProperty("SOAPACTION", "\"urn:schemas-upnp-org:service:AVTransport:1#GetPositionInfo\"");
        request.setDoOutput(true);
        request.setReadTimeout(500);


        OutputStreamWriter input = new OutputStreamWriter(request.getOutputStream());

        input.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<s:Envelope s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "   <s:Body>\n" +
                "      <u:GetPositionInfo xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">\n" +
                "         <InstanceID>0</InstanceID>\n" +
                "      </u:GetPositionInfo>\n" +
                "   </s:Body>\n" +
                "</s:Envelope>");
        input.write("");



        input.flush();


        BufferedReader output = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        String oneResponse = new String();
        String line;
        while ((line = output.readLine()) != null) {
            oneResponse += line + "\r\n";
        }
        System.out.println(oneResponse);
        String duratation = oneResponse.substring(oneResponse.indexOf("<TrackDuration>")+"<TrackDuration>".length(),oneResponse.indexOf("</TrackDuration>"));
        String RelTime = oneResponse.substring(oneResponse.indexOf("<RelTime>")+"<RelTime>".length(),oneResponse.indexOf("</RelTime>"));
        return new posisitionInfo(duratation,RelTime);

    }
    public void SetTvAsInput() throws IOException {

        System.out.println
                (new Exception().getStackTrace()[0].getMethodName());

        URL url = new URL("http://" + ipAdress + ":1400/MediaRenderer/AVTransport/Control");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();

        request.setRequestMethod("POST");
        request.addRequestProperty("SOAPACTION", "\"urn:schemas-upnp-org:service:AVTransport:1#SetAVTransportURI\"");
        request.setDoOutput(true);
        request.setReadTimeout(2000);


        OutputStreamWriter input = new OutputStreamWriter(request.getOutputStream());

        input.write("<s:Envelope s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "   <s:Body>\n" +
                "      <u:SetAVTransportURI xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">\n" +
                "         <InstanceID>0</InstanceID>\n" +
                "         <CurrentURI>x-rincon-stream:RINCON_000E582DD47001400</CurrentURI>\n" +
                "         <CurrentURIMetaData />\n" +
                "      </u:SetAVTransportURI>\n" +
                "   </s:Body>\n" +
                "</s:Envelope>");
        input.write("");



        input.flush();


        BufferedReader output = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        String oneResponse = new String();
        String line;
        while ((line = output.readLine()) != null) {
            oneResponse += line + "\r\n";
        }
        System.out.println(oneResponse);
    }
    public void SetPlayListAsInput() throws IOException {

        System.out.println
                (new Exception().getStackTrace()[0].getMethodName());

        URL url = new URL("http://" + ipAdress + ":1400/MediaRenderer/AVTransport/Control");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();

        request.setRequestMethod("POST");
        request.addRequestProperty("SOAPACTION", "\"urn:schemas-upnp-org:service:AVTransport:1#SetAVTransportURI\"");
        request.setDoOutput(true);
        request.setReadTimeout(2000);


        OutputStreamWriter input = new OutputStreamWriter(request.getOutputStream());

        input.write("<s:Envelope s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "   <s:Body>\n" +
                "      <u:SetAVTransportURI xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">\n" +
                "         <InstanceID>0</InstanceID>\n" +
                "         <CurrentURI>x-rincon-queue:RINCON_000E582DD47001400#0</CurrentURI>\n" +
                "         <CurrentURIMetaData />\n" +
                "      </u:SetAVTransportURI>\n" +
                "   </s:Body>\n" +
                "</s:Envelope>");
        input.write("");



        input.flush();


        BufferedReader output = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        String oneResponse = new String();
        String line;
        while ((line = output.readLine()) != null) {
            oneResponse += line + "\r\n";
        }
        System.out.println(oneResponse);
    }
    public static void Stop() throws IOException {
        System.out.println
                (new Exception().getStackTrace()[0].getMethodName());

        URL url = new URL("http://" + ipAdress + ":1400/MediaRenderer/AVTransport/Control");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();

        request.setRequestMethod("POST");
        request.addRequestProperty("SOAPACTION", "\"urn:schemas-upnp-org:service:AVTransport:1#Pause\"");
        request.setDoOutput(true);
        request.setReadTimeout(2000);


        OutputStreamWriter input = new OutputStreamWriter(request.getOutputStream());

        input.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<s:Envelope s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "   <s:Body>\n" +
                "      <u:Pause xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">\n" +
                "         <InstanceID>0</InstanceID>\n" +
                "      </u:Pause>\n" +
                "   </s:Body>\n" +
                "</s:Envelope>");
        input.write("");



        input.flush();


        BufferedReader output = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        String oneResponse = new String();
        String line;
        while ((line = output.readLine()) != null) {
            oneResponse += line + "\r\n";
        }
        System.out.println(oneResponse);
    }
    public static void Next() throws IOException {
        System.out.println
                (new Exception().getStackTrace()[0].getMethodName());

        URL url = new URL("http://" + ipAdress + ":1400/MediaRenderer/AVTransport/Control");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();

        request.setRequestMethod("POST");
        request.addRequestProperty("SOAPACTION", "\"urn:schemas-upnp-org:service:AVTransport:1#Next\"");
        request.setDoOutput(true);
        request.setReadTimeout(2000);


        OutputStreamWriter input = new OutputStreamWriter(request.getOutputStream());

        input.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<s:Envelope s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "   <s:Body>\n" +
                "      <u:Next xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">\n" +
                "         <InstanceID>0</InstanceID>\n" +
                "      </u:Next>\n" +
                "   </s:Body>\n" +
                "</s:Envelope>");
        input.write("");



        input.flush();


        BufferedReader output = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        String oneResponse = new String();
        String line;
        while ((line = output.readLine()) != null) {
            oneResponse += line + "\r\n";
        }
        System.out.println(oneResponse);
    }
    public static void Seek_track_nr(int position) throws IOException {


        System.out.println
                (new Exception().getStackTrace()[0].getMethodName());

        URL url = new URL("http://" + ipAdress + ":1400/MediaRenderer/AVTransport/Control");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();

        request.setRequestMethod("POST");
        request.addRequestProperty("SOAPACTION", "\"urn:schemas-upnp-org:service:AVTransport:1#Seek\"");
        request.setDoOutput(true);
        request.setReadTimeout(2000);


        OutputStreamWriter input = new OutputStreamWriter(request.getOutputStream());

        input.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<s:Envelope s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "   <s:Body>\n" +
                "      <u:Seek xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">\n" +
                "         <InstanceID>0</InstanceID>\n" +
                "         <Unit>TRACK_NR</Unit>\n" +
                "         <Target>"+position+"</Target>\n" +
                "      </u:Seek>\n" +
                "   </s:Body>\n" +
                "</s:Envelope>");
        input.write("");



        input.flush();


        BufferedReader output = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        String oneResponse = new String();
        String line;
        while ((line = output.readLine()) != null) {
            oneResponse += line + "\r\n";
        }
        System.out.println(oneResponse);

    }

    public static void RemoveAllTracks() throws IOException {
        System.out.println
                (new Exception().getStackTrace()[0].getMethodName());

        URL url = new URL("http://" + ipAdress + ":1400/MediaRenderer/Queue/Control");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();

        request.setRequestMethod("POST");
        request.addRequestProperty("SOAPACTION", " \"urn:schemas-sonos-com:service:Queue:1#RemoveAllTracks\"");
        request.setDoOutput(true);
        request.setReadTimeout(2000);


        OutputStreamWriter input = new OutputStreamWriter(request.getOutputStream());

        input.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<s:Envelope s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "   <s:Body>\n" +
                "      <u:RemoveAllTracks xmlns:u=\"urn:schemas-sonos-com:service:Queue:1\">\n" +
                "         <QueueID>0</QueueID>\n" +
                "         <UpdateID>0</UpdateID>\n" +
                "      </u:RemoveAllTracks>\n" +
                "   </s:Body>\n" +
                "</s:Envelope>");
        input.write("");



        input.flush();


        BufferedReader output = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        String oneResponse = new String();
        String line;
        while ((line = output.readLine()) != null) {
            oneResponse += line + "\r\n";
        }
        System.out.println(oneResponse);
    }
    public static boolean setGroupVolume(int volume) throws IOException {
        System.out.println
                (new Exception().getStackTrace()[0].getMethodName());
        if(volume<100&&volume>=0){
            URL url = new URL("http://" + ipAdress + ":1400/MediaRenderer/GroupRenderingControl/Control");

            HttpURLConnection request = (HttpURLConnection) url.openConnection();

            request.setRequestMethod("POST");
//        request.addRequestProperty("SOAPACTION", "\"urn:schemas-upnp-org:service:ContentDirectory:1#Browse\"");
            request.addRequestProperty("SOAPACTION", "\"urn:schemas-upnp-org:service:GroupRenderingControl:1#SetGroupVolume\"");
            request.setDoOutput(true);
            request.setReadTimeout(2000);



            OutputStreamWriter input = new OutputStreamWriter(request.getOutputStream());

            input.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<s:Envelope s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "   <s:Body>\n" +
                    "      <u:SetGroupVolume xmlns:u=\"urn:schemas-upnp-org:service:GroupRenderingControl:1\">\n" +
                    "         <InstanceID>0</InstanceID>\n" +
                    "         <DesiredVolume>"+volume+"</DesiredVolume>\n" +
                    "      </u:SetGroupVolume>\n" +
                    "   </s:Body>\n" +
                    "</s:Envelope>");
            input.write("");



            input.flush();


            BufferedReader output = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            String oneResponse = new String();
            String line;
            while ((line = output.readLine()) != null) {
                oneResponse += line + "\r\n";
            }

            System.out.println(oneResponse);


        }else{
            System.out.println("error volume level out off bounds ");
            return false;
        }
        return true;
    }

    public static boolean getTransportInfo() throws IOException {
        System.out.println
                (new Exception().getStackTrace()[0].getMethodName());
        URL url = new URL("http://" + ipAdress + ":1400/MediaRenderer/AVTransport/Control");

        HttpURLConnection request = (HttpURLConnection) url.openConnection();

        request.setRequestMethod("POST");
//        request.addRequestProperty("SOAPACTION", "\"urn:schemas-upnp-org:service:ContentDirectory:1#Browse\"");
        request.addRequestProperty("SOAPACTION", "\"urn:schemas-upnp-org:service:AVTransport:1#GetTransportInfo\"");
        request.setDoOutput(true);
        request.setReadTimeout(2000);



            OutputStreamWriter input = new OutputStreamWriter(request.getOutputStream());

            input.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            input.write("<s:Envelope s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">");
            input.write("   <s:Body>");
            input.write("   <u:GetTransportInfo xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">");
            input.write(" <InstanceID>0</InstanceID>");
            input.write("   </u:GetTransportInfo>");
            input.write("  </s:Body>");
            input.write("</s:Envelope>");

            input.flush();


            BufferedReader output = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            String oneResponse1 = new String();
            String line;
            while ((line = output.readLine()) != null) {
                oneResponse1 += line + "\r\n";
            }

            String oneResponse = oneResponse1;

            if (oneResponse.contains("<CurrentTransportState>")) {
                oneResponse = oneResponse.substring(oneResponse.indexOf("<CurrentTransportState>") + "<CurrentTransportState>".length());
                oneResponse = oneResponse.substring(0, oneResponse.indexOf("</CurrentTransportState>"));

                switch (oneResponse) {

                    case "PLAYING":

                        System.out.println(oneResponse);
                        System.out.println(oneResponse1);

                        return true;
                    case "PAUSED_PLAYBACK":
                        return false;


                }
            }

        return false;
    }

    public int playUri(item item) throws IOException {

        System.out.println
                (new Exception().getStackTrace()[0].getMethodName());

        URL url = new URL("http://" + ipAdress + ":1400/MediaRenderer/AVTransport/Control");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();

        request.setRequestMethod("POST");
        request.addRequestProperty("SOAPACTION",  "urn:schemas-upnp-org:service:AVTransport:1#AddURIToQueue");
        request.setDoOutput(true);
        request.setReadTimeout(2000);


        OutputStreamWriter input = new OutputStreamWriter(request.getOutputStream());

        input.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<s:Envelope s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "   <s:Body>\n" +
                "      <u:AddURIToQueue xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">\n" +
                "         <InstanceID>0</InstanceID>\n" +
                "         <EnqueuedURI>"+item.uri+"</EnqueuedURI>\n" +
                "         <EnqueuedURIMetaData>"+item.upnpalbumArtURI+"</EnqueuedURIMetaData>\n" +
                "         <DesiredFirstTrackNumberEnqueued>0</DesiredFirstTrackNumberEnqueued>\n" +
                "         <EnqueueAsNext>1</EnqueueAsNext>\n" +
                "      </u:AddURIToQueue>\n" +
                "   </s:Body>\n" +
                "</s:Envelope>");
        input.write("");



        input.flush();


        BufferedReader output = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        String oneResponse = new String();
        String line;
        while ((line = output.readLine()) != null) {
            oneResponse += line + "\r\n";
        }
        System.out.println(oneResponse);


        int i1 = oneResponse.indexOf("<FirstTrackNumberEnqueued>")+"<FirstTrackNumberEnqueued>".length();
        int i2 = oneResponse.indexOf("</FirstTrackNumberEnqueued>");
        if(i1!=-1&&i2!=-1) {
            return Integer.parseInt(oneResponse.substring(i1,i2));
        }else {
            return -1;
        }

    }


//
//    public static getPostionInfo getPositionInfo() throws IOException, ParseException {
//        getPostionInfo info = new getPostionInfo();
//        Date newDate = null;
//        URL url = new URL("http://" + ipAdress + ":1400/MediaRenderer/AVTransport/Control");
//
//        HttpURLConnection request = (HttpURLConnection) url.openConnection();
//
//        request.setRequestMethod("POST");
////        request.addRequestProperty("SOAPACTION", "\"urn:schemas-upnp-org:service:ContentDirectory:1#Browse\"");
//        request.addRequestProperty("SOAPACTION", "\"urn:schemas-upnp-org:service:AVTransport:1#GetPositionInfo\"");
//        request.setDoOutput(true);
//        request.setReadTimeout(2000);
//
//
//        request.connect();
//
//
//        OutputStreamWriter input = new OutputStreamWriter(request.getOutputStream());
//
//
//        input.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
//        input.write("<s:Envelope s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">");
//        input.write("<s:Body>");
//        input.write("<u:GetPositionInfo xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">");
//        input.write(" <InstanceID>0</InstanceID>");
//        input.write("</u:GetPositionInfo>");
//        input.write("  </u:GetPositionInfo>");
//        input.write("</s:Body>");
//        input.write("</s:Envelope>");
////        input.write("");
//        input.flush();
//
//        BufferedReader output = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
//        String oneResponse = new String();
//        String line;
//        while ((line = output.readLine()) != null) {
//            oneResponse += line + "\r\n";
//        }
//        String orginal = oneResponse;
//        System.out.println(oneResponse);
//
//
//        if (oneResponse.contains("<RelTime>")) {
//            oneResponse = oneResponse.substring(oneResponse.indexOf("<RelTime>") + 9);
//            oneResponse = oneResponse.substring(0, oneResponse.indexOf("</RelTime>"));
//            if(oneResponse.contains("NOT_IMPLEMENTED")){
//                info.Stoped = true;
//            }else {
//                info.RelTime = TimeFormat.parse(oneResponse);
//            }
//        }
//
//        oneResponse = orginal;
//        if(oneResponse.contains("<Track>")){
//            oneResponse = oneResponse.substring(oneResponse.indexOf("<Track>") + 7);
//            oneResponse = oneResponse.substring(0, oneResponse.indexOf("</Track>"));
//
//            if(oneResponse.contains("NOT_IMPLEMENTED")){
//                info.Stoped = true;
//            }else {
//                info.track = Integer.parseInt(oneResponse);
//            }
//        }
//        return info;
//    }
}
