package com.simon.max1704x_lipo_gauge;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

/**
 * Created by simon on 4/2/2017.
 */
public class max17043 {
        private static I2CDevice device;

    public static int address = 0x36;


    static byte VCELL_REGISTER	=(byte)0x02;
    static byte SOC_REGISTER		=(byte)0x04;
    static byte MODE_REGISTER	=(byte)0x06;
    static byte VERSION_REGISTER	=(byte)0x08;
    static byte CONFIG_REGISTER	=(byte)0x0C;
    static byte CONFIG_REGISTER_ATHRD_ADDR	=(byte)0x0D;
    static byte COMMAND_REGISTER= (byte) 0xFE;
    private I2CDevice getDevice(I2CBus i2c) throws IOException {
        I2CDevice device = i2c.getDevice(this.address);
        return device;
    }
    public max17043(I2CBus i2c, int address) throws IOException, I2CFactory.UnsupportedBusNumberException {
        this.device = device;
        this.address = address;
        this.device = getDevice(i2c);

        if(!isSleeping(device)){
            sleep(device);
        }
        clearAlertInterrupt(device);
    }
    public max17043(I2CBus i2c) throws IOException, I2CFactory.UnsupportedBusNumberException {

        this.device = getDevice(i2c);

        if(!isSleeping(device)){
            sleep(device);
        }
        
        clearAlertInterrupt(device);
    }
    public static boolean isSleeping(I2CDevice device) throws IOException {

        int b = (getStatus(device) & 0x80);
        if(b == 0x80){
            System.out.println("checking if max17043 is sleeping : true" );
            return true;
        }else{
            System.out.println("checking if max17043 is sleeping : false" );
            return false;
        }

    }
    public static void reset() throws IOException {
        wake(device);
        reset(device);
        sleep(device);

    }
    public static void QuickStart() throws IOException {
        wake(device);
        QuickStart(device);
        sleep(device);

    }
    public static int getVersion() throws IOException {
        wake(device);
        int version = GetVersion(device);
        sleep(device);
        return version;
    }
    public static double getSOC() throws IOException, InterruptedException {
        if(isSleeping(device)) {
            wake(device);
        }
      //  Thread.sleep(1000);
        double SOC = getSoC(device);
//        sleep(device);
        return SOC;
    }
    public double getVcell() throws IOException, InterruptedException {
        wake(this.device);
        Thread.sleep(150);
        double vCell = getVCell(this.device);
        sleep(this.device);
        return vCell;
    }
    public static int getAlertThreshold() throws IOException {
        wake(device);
//        (32-(buffer[1]&0x1F))
        int thrs = (32-(getAlertThreshold(device)[1]&0x1F));
        sleep(device);
        return thrs;
    }
    public static void setAlertThreshold(int AlertThreshold) throws IOException {
        wake(device);
        setAlertThreshold(device,AlertThreshold);
        sleep(device);

    }

    public static void clearAlertInterrupt(I2CDevice device) throws IOException {
        wake(device);
        byte comp = getCompensation(device);
        byte status = (byte) getStatus(device);

        byte [] buffer = {CONFIG_REGISTER,comp, (byte) (0xDF & status)};
        System.out.println("clearing alert interrupt pin goes back to high");
        device.write(buffer);
        sleep(device);
    }
    private static void wake(I2CDevice device) throws IOException {
//        System.out.println("waking max17043 device from sleep");

        byte comp = getCompensation(device);
        byte thrd = getAlertThreshold(device)[1];
        byte [] buffer = {CONFIG_REGISTER,comp, (byte) (0x7f & thrd)};

        device.write(buffer);

    }

    private static int getStatus(I2CDevice device) throws IOException {
        byte [] buffer = {CONFIG_REGISTER_ATHRD_ADDR};
        device.write(buffer);
        return device.read();
    }
    private static void sleep(I2CDevice device) throws IOException {
//        System.out.println("putting max17043 device to sleep");

        byte comp = getCompensation(device);
        byte thrd = getAlertThreshold(device)[1];
        byte [] buffer = {CONFIG_REGISTER,comp, (byte) (0x80 | thrd)};

        device.write(buffer);
    }
    private static byte getCompensation(I2CDevice device) throws IOException {

        byte [] buffer = {CONFIG_REGISTER};
        device.write(buffer);
        byte buffer2[] = new byte[2];  // receive 16 bits (2 bytes)
        int byteCount = 0;
        try
        {
            byteCount = device.read(buffer2, 0, 8);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

//        System.out.println("getting compensation");
//        System.out.println(("repsonse : "+String.format("0x%02x", buffer2[1])));
//        System.out.println("msb : " + buffer2[0] + "  : lsb :" +buffer2[1]);
        return buffer2[1];


    }


    private static boolean getAlert(I2CDevice device) throws IOException {
        System.out.println("getting alert state");
        System.out.println(" note working a 100% atm");
//        byte[] buffer = {CONFIG_REGISTER,0,0};
//        int response = device.read(CONFIG_REGISTER);
//        System.out.println("config register response : " + response);
//        byte b = (byte) ((byte)response & 0x20);
//        System.out.println("modified output : " + b);


        System.out.println("Getting alert trigger value version 2");


        device.write(CONFIG_REGISTER);

        byte buffer2[] = new byte[2];  // receive 16 bits (2 bytes)
        int byteCount = 0;
        try
        {
            byteCount = device.read(buffer2, 0, 8);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        System.out.println("msb : " + buffer2[0] + "  : lsb :" +buffer2[1]);

        System.out.println("");
        System.out.println(Integer.toBinaryString(buffer2[1]));
        System.out.println(Integer.toBinaryString(buffer2[1]).charAt(2));
        if(Integer.toBinaryString(buffer2[1]).charAt(2) =='1'){
            System.out.println("alarm triggered");
            return true;
        }else {
            System.out.println("alarm not triggered");
            return false;
        }
    }
    protected static String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    private static int GetVersion(I2CDevice device) throws IOException {
//        System.out.println("getting device version // shut be version 4//");

        device.write(VERSION_REGISTER);

        byte buffer[] = new byte[2];  // receive 16 bits (2 bytes)
        int byteCount = 0;
        try
        {
            byteCount = device.read(buffer, 0, 2);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


//        System.out.println("msb : " + buffer[0] + "  : lsb :" +buffer[1]);
        byte b = (byte) (buffer[0]<<8|buffer[1]);
//        System.out.println("modified output : " + b);
//        System.out.println("bytes to hex : " + bytesToHex(buffer));
//        System.out.println(("repsonse : "+String.format("0x%02x", response) + " raw response : " + response));
//        byte r1 = (byte) (response<<8);
//
//        System.out.println("modified response : " + r1);

//        return response;

        return b;
    }
    private static byte[] getAlertThreshold(I2CDevice device) throws IOException {
//        System.out.println("getting device getAlertThreshold // shut be version 4//");

        device.write(CONFIG_REGISTER);

        byte buffer[] = new byte[2];  // receive 16 bits (2 bytes)
        int byteCount = 0;
        try
        {
            byteCount = device.read(buffer, 0, 2);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


//        System.out.println("msb : " + buffer[0] + "  : lsb :" +buffer[1]);
//        System.out.println("modified output : " +(32-(buffer[1]&0x1F)));
//        System.out.println("bytes to hex : " + bytesToHex(buffer));

        return buffer;
    }

    private static void reset(I2CDevice device) throws IOException {
//        System.out.println("device reset ");

        byte [] buffer = {COMMAND_REGISTER,0x54};
        device.write(buffer);
    }
    private static void QuickStart(I2CDevice device) throws IOException {
        int response = 0;
//        System.out.println("write the mode Register traying to do a quickStart");
        byte [] buffer = {MODE_REGISTER,0x40};
        device.write(buffer);


//        response = device.read(CONFIG_REGISTER);
//        System.out.println(("repsonse : "+String.format("0x%02x", response) + " raw response : " + response));


    }
    private static void setAlertThreshold(I2CDevice device, int threshold) throws IOException {
//        System.out.println("Setting the alert threadhold to : " + threshold + "% // default level is 4%//");
//        System.out.println("dos not work atm ");


        byte[] buffer = getAlertThreshold(device);
        if(threshold > 32) threshold = 32;
        int newthreshold = 32 - threshold;

//        System.out.println("newthreashold value :" + newthreshold   );
        writeRegister(CONFIG_REGISTER,newthreshold,device);

    }
    protected static void writeRegister(int register, int value , I2CDevice device) throws IOException {

        // create packet in data buffer
        byte packet[] = new byte[3];
        packet[0] = (byte)(register);     // register byte
        packet[1] = (byte)(value>>8);     // value MSB
        packet[2] = (byte)(value & 0xFF); // value LSB

        // write data to I2C device
        device.write(packet, 0, 3);
    }



    private static double getSoC(I2CDevice device) throws IOException {
//        System.out.println("getting soc");
        int response = device.read(SOC_REGISTER);

//        System.out.println("repsonse : "+String.format("0x%02x", response) + " raw response : " + response);

//        System.out.println("getting device version // shut be version 4//");

        device.write(SOC_REGISTER);

        byte buffer[] = new byte[2];  // receive 16 bits (2 bytes)
        int byteCount = 0;
        try
        {
            byteCount = device.read(buffer, 0, 2);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


//        System.out.println("msb : " + buffer[0] + "  : lsb :" +buffer[1]);
        double b = buffer[0]+(buffer[1] / 256.0);
//        System.out.println("modified output : " + b);
//        System.out.println("bytes to hex : " + bytesToHex(buffer));
        System.out.println("getting new SOC");
        return b;
    }


    private static double getVCell(I2CDevice device) throws IOException {

        int response = device.read(VCELL_REGISTER);
        byte i = (byte) response;
        double i2 = i&0xff;
//        System.out.println(i);
//        System.out.println(i2);
        double i3 = (5.0/255.0);
//        System.out.println(i3);
        double i4 = i3*i2;
//        System.out.println(i4);
//        System.out.println("repsonse : "+String.format("0x%02x", response) + " raw response : " + ((byte) response& 0xff) + " vCell = " + i4 );

        return i4;
//        System.out.println("get v cell version 2");
//
//        device.write(VCELL_REGISTER);
//        byte buffer[] = new byte[2];  // receive 16 bits (2 bytes)
//        int byteCount = 0;
//        try
//        {
//            byteCount = device.read(VCELL_REGISTER,buffer, 0, 2);
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//        System.out.println("msb : " + buffer[0] + "  : lsb :" +buffer[1]);
//        double voltage = ((buffer[0]<<4)|buffer[1]>>4)*0.00125*1;
//
//        System.out.println("version 2 calculated voltage  : " + voltage);

    }

    private static void ReadConfig(I2CDevice device) throws IOException {
        int response =0;
        System.out.println("reading the config register default value 0x97 ");
        // no idear how to change / the gain off chaing this value
        response = device.read(CONFIG_REGISTER);
        System.out.println(("repsonse : "+String.format("0x%02x", response) + " raw response : " + response));

    }



}
