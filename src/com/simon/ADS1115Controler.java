package com.simon;

import com.pi4j.gpio.extension.ads.ADS1115GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1115Pin;
import com.pi4j.gpio.extension.ads.ADS1x15GpioProvider;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import com.simon.sonos.Sonos;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by simon on 3/21/2017.
 */
public class ADS1115Controler {
    double deviceVoltage=0.0;
    private static String pin0 = "MyAnalogInput-A0";
    String pin1 = "MyAnalogInput-A1";
    String pin2 = "MyAnalogInput-A2";
    String pin3 = "MyAnalogInput-A3";
    Sonos sonos;
    private double inputVoltage =ADS1115GpioProvider.ADS1115_RANGE_MAX_VALUE;
    public boolean isCharing = false;
    public ADS1115Controler()  throws IOException, I2CFactory.UnsupportedBusNumberException{{
        System.out.println("<--Pi4J--> ADS1115 GPIO Example ... started.");

        // number formatters
        final DecimalFormat df = new DecimalFormat("#.##");
        final DecimalFormat pdf = new DecimalFormat("###.#");


        sonos = Main.sonos;
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();



        // create custom ADS1115 GPIO provider
        final ADS1115GpioProvider gpioProvider = new ADS1115GpioProvider(I2CBus.BUS_1, ADS1115GpioProvider.ADS1115_ADDRESS_0x48);

        // provision gpio analog input pins from ADS1115
        GpioPinAnalogInput myInputs[] = {
                gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A0, pin0),
                gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A1, pin1),
                gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A2, pin2),
                gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A3, pin3),
        };


        // ATTENTION !!
        // It is important to set the PGA (Programmable Gain Amplifier) for all analog input pins.
        // (You can optionally set each input to a different value)
        // You measured input voltage should never exceed this value!
        //
        // In my testing, I am using a Sharp IR Distance Sensor (GP2Y0A21YK0F) whose voltage never exceeds 3.3 VDC
        // (http://www.adafruit.com/products/164)
        //
        // PGA value PGA_4_096V is a 1:1 scaled input,
        // so the output values are in direct proportion to the detected voltage on the input pins
        gpioProvider.setProgrammableGainAmplifier(ADS1x15GpioProvider.ProgrammableGainAmplifierValue.PGA_6_144V, ADS1115Pin.ALL);


        // Define a threshold value for each pin for analog value change events to be raised.
        // It is important to set this threshold high enough so that you don't overwhelm your program with change events for insignificant changes
        gpioProvider.setEventThreshold(1000,ADS1115Pin.ALL);
        gpioProvider.setEventThreshold(750,ADS1115Pin.INPUT_A0);

//        gpioProvider.setEventThreshold(500, ADS1115Pin.INPUT_A1);


        // Define the monitoring thread refresh interval (in milliseconds).
        // This governs the rate at which the monitoring thread will read input values from the ADC chip
        // (a value less than 50 ms is not permitted)
        gpioProvider.setMonitorInterval(200);
        try {
            Thread.sleep(220);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        inputVoltage = myInputs[2].getValue();
        double value = myInputs[1].getValue();

        // percentage
        double percent =  ((value * 100) / ADS1115GpioProvider.ADS1115_RANGE_MAX_VALUE);

        // approximate voltage ( *scaled based on PGA setting )
        double voltage = gpioProvider.getProgrammableGainAmplifier(myInputs[1].getPin()).getVoltage() * (percent/100);

        if(voltage<3){
            isCharing = false;
        }else {
            isCharing = true;
        }

        System.out.println("ADS init values : isCharing =" +isCharing + " inputVoltageRAW ="+inputVoltage+ " inputVoltage =" +voltage);

        // create analog pin value change listener
        GpioPinListenerAnalog listener = new GpioPinListenerAnalog()
        {
            @Override
            public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event)
            {
                // RAW value
                double value = event.getValue();

                // percentage
                double percent =  ((value * 100) / ADS1115GpioProvider.ADS1115_RANGE_MAX_VALUE);

                // approximate voltage ( *scaled based on PGA setting )
                double voltage = gpioProvider.getProgrammableGainAmplifier(event.getPin()).getVoltage() * (percent/100);

                percent = voltage/6.144*100;
                // display output
                System.out.println(" (" + event.getPin().getName() +") : VOLTS=" + df.format(voltage) + "  | PERCENT=" + pdf.format(percent) + "% | RAW=" + value + "       ");
//                    try {
//                        new Sonos("192.168.1.118").setGroupVolume((int)percent);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                switch (event.getPin().getName()){
                    case "MyAnalogInput-A0":
                        double d = 100.0 -(((inputVoltage-value)/inputVoltage)*100.0);

                        System.out.println("settings volume level to :" +((int) d) + " input value is :" + inputVoltage + " Raw value is :"+value);
                        if(Main.sonos!=null) {
                            int vol=0;
                            try {
                                vol = Main.sonos.getGroupVolume(0);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (d < vol+10){
                                try {
                                    Main.sonos.setGroupVolume((int)d);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else{

                            }
                        }

                        break;
                    case "MyAnalogInput-A1":
                        if(voltage<3){
                            isCharing = false;
                        }else {
                            isCharing = true;
                        }
                        System.out.println("circuit state :"+isCharing);
                        break;
                    case "MyAnalogInput-A2":
                        inputVoltage = value;
                        break;
                    case "MyAnalogInput-A3":
                        break;


                }
            }
        };


//        myInputs[1].addListener(new GpioPinListenerAnalog() {
//            @Override
//            public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event) {
//                double value = event.getValue();
//                double percent =  ((value * 100) / ADS1115GpioProvider.ADS1115_RANGE_MAX_VALUE);
//                deviceVoltage = gpioProvider.getProgrammableGainAmplifier(event.getPin()).getVoltage() * (percent/100);
//                System.out.println("new device voltage : " + deviceVoltage);
//            }
//        });
        myInputs[0].addListener(listener);
        myInputs[1].addListener(listener);
        myInputs[2].addListener(listener);
        myInputs[3].addListener(listener);


        // keep program running for 10 minutes

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)


        System.out.println("Exiting ADS1115GpioExample");
    }

    }
    public static void RampAudio(int vol,int target){
        System.out.println("ramping audio current volume :"+vol + " target audio :"+target);
        for (int i =vol;i<target;i++){
            try {
                Main.sonos.setGroupVolume(i);
                Thread.sleep(250);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
