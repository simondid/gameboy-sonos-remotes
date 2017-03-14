package com.simon.sonos;

public class item{
    public String title,containerId,parentId,protocolInfo,upnpalbumArtURI,upnpClass,uri,creator,album;


    public item() {

    }

    public item(String title, String containerId, String parentId, String protocolInfo, String upnpalbumArtURI, String upnpClass, String uri, String creator, String album) {
        this.title = title;
        this.containerId = containerId;
        this.parentId = parentId;
        this.protocolInfo = protocolInfo;
        this.upnpalbumArtURI = upnpalbumArtURI;
        this.upnpClass = upnpClass;
        this.uri = uri;
        this.creator = creator;
        this.album = album;
    }

 /*   public item analyse(String input){


        int i1 = input.indexOf("<dc:title>") + "<dc:title>".length();
        int i2 = input.indexOf("</dc:title>");
        if (i1 != -1 && i2 != -1) {
            this.title = input.substring(i1, i2);
        } else {
            this.title = "null";
        }

        i1 = input.indexOf("<container id=\"") + "<container id=\"".length();
        i2 = input.indexOf("\"",i1);
        if (i1 != -1 && i2 != -1) {
            this.containerId = input.substring(i1, i2);
        } else {
            this.containerId = "null";
        }

        i1 = input.indexOf("parentID=\"") + "parentID=\"".length();
        i2 = input.indexOf("\"",i1);
        if (i1 != -1 && i2 != -1) {
            this.parentId = input.substring(i1, i2);
        } else {
            this.parentId = "null";
        }

        return this;
    }*/
    public void print(){
        System.out.println("Title : "+title);
        System.out.println("ContainerId : " + containerId);
        System.out.println("parenId : " + parentId);
        System.out.println("protocolInfo : " + protocolInfo);
        System.out.println("upnpalbumArtURI : " + upnpalbumArtURI);
        System.out.println("upnpClass : " + upnpClass);
        System.out.println("Uri : " + uri);
        System.out.println("creator : " + creator);
        System.out.println("album : " + album);

    }
}
