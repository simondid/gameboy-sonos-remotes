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

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by simon on 3/21/2017.
 */
public class VolumeControle {
    double deviceVoltage=0.0;
    public VolumeControle(boolean pi4jActive)  throws IOException, I2CFactory.UnsupportedBusNumberException{



        if(pi4jActive) {
            pi4j();
        }


    }

    private void pi4j() throws IOException, I2CFactory.UnsupportedBusNumberException {
        System.out.println("<--Pi4J--> ADS1115 GPIO Example ... started.");
        // number formatters
        final DecimalFormat df = new DecimalFormat("#.##");
        final DecimalFormat pdf = new DecimalFormat("###.#");



        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();



        // create custom ADS1115 GPIO provider
        final ADS1115GpioProvider gpioProvider = new ADS1115GpioProvider(I2CBus.BUS_1, ADS1115GpioProvider.ADS1115_ADDRESS_0x48);

        // provision gpio analog input pins from ADS1115
        GpioPinAnalogInput myInputs[] = {
//                gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A0, "MyAnalogInput-A0"),
                gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A1, "MyAnalogInput-A1"),
                gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A2, "MyAnalogInput-A2"),
//                gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A3, "MyAnalogInput-A3"),
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
        gpioProvider.setEventThreshold(500,ADS1115Pin.ALL);
        gpioProvider.setEventThreshold(500, ADS1115Pin.INPUT_A1);


        // Define the monitoring thread refresh interval (in milliseconds).
        // This governs the rate at which the monitoring thread will read input values from the ADC chip
        // (a value less than 50 ms is not permitted)
        gpioProvider.setMonitorInterval(100);


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

                percent = voltage/deviceVoltage*100;
                // display output
                System.out.println(" (" + event.getPin().getName() +") : VOLTS=" + df.format(voltage) + "  | PERCENT=" + pdf.format(percent) + "% | RAW=" + value + "       ");
                try {
                    Main.sonos.setGroupVolume((int)percent);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };


        myInputs[1].addListener(new GpioPinListenerAnalog() {
            @Override
            public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event) {
                double value = event.getValue();
                double percent =  ((value * 100) / ADS1115GpioProvider.ADS1115_RANGE_MAX_VALUE);
                deviceVoltage = gpioProvider.getProgrammableGainAmplifier(event.getPin()).getVoltage() * (percent/100);
                System.out.println("new device voltage : " + deviceVoltage);
            }
        });
        myInputs[0].addListener(listener);
//        myInputs[2].addListener(listener);
//        myInputs[3].addListener(listener);


        // keep program running for 10 minutes

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)


        System.out.println("Exiting ADS1115GpioExample");
    }


}

