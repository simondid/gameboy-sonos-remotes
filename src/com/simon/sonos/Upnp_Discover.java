package com.simon.sonos;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.message.header.STAllHeader;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;

import java.util.ArrayList;

/**
 * Created by simon on 4/11/2017.
 */
public class Upnp_Discover extends Thread {
    ArrayList<deviceDescription> devices;

    public Upnp_Discover() {
        this.devices = new ArrayList<>();
    }

    @Override
    public void run() {
        // This will create necessary network resources for UPnP right away
//        System.out.println("Starting Cling...");
        UpnpService upnpService = new UpnpServiceImpl(listener);

        // Send a search message to all devices and services, they should respond soon
        upnpService.getControlPoint().search(new STAllHeader());

        // Let's wait 10 seconds for them to respond
//        System.out.println("Waiting 10 seconds before shutting down...");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Release all resources and advertise BYEBYE to other UPnP devices
//        System.out.println("Stopping Cling...");
        upnpService.shutdown();


    }
    RegistryListener listener = new RegistryListener() {

        public void remoteDeviceDiscoveryStarted(Registry registry,
                                                 RemoteDevice device) {
            System.out.println(
                    "Discovery started: " + device.getDisplayString()
            );
        }

        public void remoteDeviceDiscoveryFailed(Registry registry,
                                                RemoteDevice device,
                                                Exception ex) {
            System.out.println(
                    "Discovery failed: " + device.getDisplayString() + " => " + ex
            );
        }

        public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
            System.out.println(
                    "Remote device available: " + device.getDisplayString()
            );
            System.out.println("base url : "+device.getDetails().getFriendlyName());
            System.out.println("model number : "+ device.getDetails().getModelDetails().getModelNumber());
            if(device.getDisplayString().toLowerCase().contains("sonos")) {

                String ip = device.getDetails().getFriendlyName().substring(0, device.getDetails().getFriendlyName().indexOf(" -"));
                System.out.println("ip :" + ip);
                devices.add(new deviceDescription(ip, null, null));
                }
            }

        public void remoteDeviceUpdated(Registry registry, RemoteDevice device) {
            System.out.println(
                    "Remote device updated: " + device.getDisplayString()
            );
        }

        public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
            System.out.println(
                    "Remote device removed: " + device.getDisplayString()
            );
        }

        public void localDeviceAdded(Registry registry, LocalDevice device) {
            System.out.println(
                    "Local device added: " + device.getDisplayString()
            );

        }

        public void localDeviceRemoved(Registry registry, LocalDevice device) {
            System.out.println(
                    "Local device removed: " + device.getDisplayString()
            );
        }

        public void beforeShutdown(Registry registry) {
            System.out.println(
                    "Before shutdown, the registry has devices: "
                            + registry.getDevices().size()
            );

        }

        public void afterShutdown() {
            System.out.println("Shutdown of registry complete!");

        }
    };

    public ArrayList<deviceDescription> getDevices() {
        return devices;
    }


}
