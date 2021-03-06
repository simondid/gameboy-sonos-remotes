package com.simon;

import java.io.IOException;

/**
 * Created by simon on 3/30/2017.
 */
public class LowPowerCalls {



    public static void turnOffOnbordLeds(){
        PowerSaveActLedOff();
        PowerSavePwrLedOff();
    }
    public static void turnOnOnbordLeds(){
        PowerSaveActLedOn();
        PowerSavePwrLedOn();
    }
    private static void PowerSavePwrLedOff(){
//        echo 0 | sudo tee /sys/class/leds/led1/brightness

        PowerSavePwrLedOn();
        PowerSavePwrLedOn();
        System.out.println("powering off PWR led");
        try {
            Runtime.getRuntime().exec("echo 0 | sudo tee /sys/class/leds/led1/brightness");
            Runtime.getRuntime().exec("echo 0 | sudo tee /sys/class/leds/led1/brightness");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void PowerSavePwrLedOn(){
//        echo 1 | sudo tee /sys/class/leds/led1/brightness
        System.out.println("powering on PWR LED");

        try {
            Runtime.getRuntime().exec("echo 1 | sudo tee /sys/class/leds/led1/brightness");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void PowerSaveActLedOff(){
//        class the follow command to turn off the act led to save power
       // echo 1 | sudo tee /sys/class/leds/led0/brightness

        PowerSaveActLedOn();
        PowerSaveActLedOn();
        System.out.println("Powering off ACT led");
        try {
            Runtime.getRuntime().exec("echo 1 | sudo tee /sys/class/leds/led0/brightness");
            Runtime.getRuntime().exec("echo 1 | sudo tee /sys/class/leds/led0/brightness");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void PowerSaveActLedOn(){
//        class the follow command to turn off the act led to save power
        // echo 0 | sudo tee /sys/class/leds/led0/brightness
        System.out.println("Powering on ACT led ");
        try {
            Runtime.getRuntime().exec("echo 0 | sudo tee /sys/class/leds/led0/brightness");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
