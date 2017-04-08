package com.simon;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

/**
 * Created by simon on 4/5/2017.
 */
//class Shutdown {
//    private Thread thread = null;
//
//    public Shutdown() {
//        thread = new Thread("Sample thread") {
//            public void run() {
//                while (true) {
//                    System.out.println("[Sample thread] Sample thread speaking...");
//                    try {
//                        Thread.currentThread().sleep(5000);
//                    } catch (InterruptedException ie) {
//                        break;
//                    }
//                }
//                System.out.println("[Sample thread] Stopped");
//            }
//        };
//        thread.start();
//    }
//
//    public void stopThread() {
//        thread.interrupt();
//    }
//}
class ShutdownThread extends Thread {
//    private Shutdown shutdown = null;

    public ShutdownThread() {
        super();
//        this.shutdown = shutdown;
    }

    public void run() {
        System.out.println("[Shutdown thread] Shutting down");
        for(int i =0;i<Main.timers.size();i++){
            Main.timers.get(i).stop();
        }
        if(Main.Pi4jActive) {
            final GpioController instance = GpioFactory.getInstance();
            instance.shutdown();
            Main.wifiOn();
        }

//        shutdown.stopThread();
        System.out.println("[Shutdown thread] Shutdown complete");

    }
}
