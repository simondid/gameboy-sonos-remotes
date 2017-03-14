package com.simon.sonos;

public class browseContainer{
   public String name;
           public String containerId,parentId;

    public browseContainer(String name, String containerId, String parentId) {
        this.name = name;
        this.containerId = containerId;
        this.parentId = parentId;
    }

    public void print(){
        System.out.println("name : "+name);
        System.out.println("containerId : "+containerId);
        System.out.println("parentId : "+parentId);
    }
}
