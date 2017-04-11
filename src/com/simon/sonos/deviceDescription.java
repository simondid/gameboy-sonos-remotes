package com.simon.sonos;

/**
 * Created by simon on 4/11/2017.
 */
public class deviceDescription {
    public  String ip,name,CurrentZoneGroupID;
    public deviceDescription(String ip,String name,String CurrentZoneGroupID) {

        this.ip = ip;
        this.name = name;
        this.CurrentZoneGroupID = CurrentZoneGroupID;
    }

    public String getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }

    public String getCurrentZoneGroupID() {
        return CurrentZoneGroupID;
    }
}
