package com.simon.max1704x_lipo_gauge;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.sun.org.apache.bcel.internal.generic.I2D;

import java.io.IOException;

/**
 * Created by simon on 4/2/2017.
 * importent link for code referance
 * https://github.com/awelters/LiPoFuelGauge/blob/master/MAX17043.cpp
 */
public class max17043 {
    public static I2CDevice device;

    public static int address = 0x36;


    static byte VCELL_REGISTER	=(byte)0x02;
    static byte SOC_REGISTER		=(byte)0x04;
    static byte MODE_REGISTER	=(byte)0x06;
    static byte VERSION_REGISTER	=(byte)0x08;
    static byte CONFIG_REGISTER	=(byte)0x0C;
    static byte CONFIG_REGISTER_ATHRD_ADDR	=(byte)0x0D;
    static byte COMMAND_REGISTER= (byte) 0xFE;
    static byte maxAlertTrue = (byte) 0x01;
    private I2CDevice getDevice(I2CBus i2c) throws IOException {
        I2CDevice device = i2c.getDevice(this.address);
        return device;
    }
    public max17043(I2CBus i2c, int address) throws IOException, I2CFactory.UnsupportedBusNumberException, InterruptedException {
        this.device = device;
        this.address = address;
        this.device = getDevice(i2c);

        if(!isSleeping(device)){
            sleep(device);
        }
//        clearAlertInterrupt(device);
    }
    public max17043(I2CBus i2c) throws IOException, I2CFactory.UnsupportedBusNumberException, InterruptedException {

        this.device = getDevice(i2c);

        if(!isSleeping(device)){
            sleep(device);
        }

//        clearAlertInterrupt(device);
    }
    public static boolean isSleeping(I2CDevice device) throws IOException {

        int b = (getStatus(device) & 0x80);
        if(b == 0x80){
//            System.out.println("checking if max17043 is sleeping : true" );
            return true;
        }else{
//            System.out.println("checking if max17043 is sleeping : false" );
            return false;
        }

    }
    public static void reset() throws IOException, InterruptedException {
        wake(device);
        reset(device);
        sleep(device);

    }
    public static void QuickStart() throws IOException, InterruptedException {
        wake(device);
        QuickStart(device);
        sleep(device);

    }
    public static int getVersion() throws IOException, InterruptedException {

        wake(device);

        int version = GetVersion(device);
        sleep(device);
        return version;
    }
    public static double getSOC() throws IOException, InterruptedException {

        wake(device);

//          Thread.sleep(1000);
        double SOC = getSoC(device);
//        sleep(device);
        return SOC;
    }
    public double getVcell() throws IOException, InterruptedException {
        wake(this.device);

        double vCell = getVCell(this.device);
        sleep(this.device);
        return vCell;
    }
    public static int getAlertThreshold() throws IOException, InterruptedException {
        wake(device);
//        (32-(buffer[1]&0x1F))
        int thrs = (32-(getAlertThreshold(device)[1]&0x1F));
        sleep(device);
        return thrs;
    }
    public static void setAlertThreshold(int AlertThreshold) throws IOException, InterruptedException {
        wake(device);
        setAlertThreshold(device,AlertThreshold);
        sleep(device);

    }

    public static void clearAlertInterrupt() throws IOException, InterruptedException {
        wake(device);
        byte comp = getCompensation(device);
        byte status = (byte) getStatus(device);

        byte [] buffer = {CONFIG_REGISTER,comp, (byte) (0xDF & status)};
        System.out.println("clearing alert interrupt pin goes back to high");
        device.write(buffer);
        sleep(device);
    }
    public static boolean getAlertTriggered() throws IOException, InterruptedException {
        wake(device);

//        System.out.println("get alter v2 out put : "+ getAlertV2(device));
//        getAlertV3(device);
        if(getAlertV3(device)){

            sleep(device);
            return true;
        }
//        getAlertV2(device);
        return false;
    }
    //    private static boolean getAlert(I2CDevice device) throws IOException {
//        System.out.println("getting alert state v2");
//
//        device.write(CONFIG_REGISTER);
//
//        byte buffer[] = new byte[2];  // receive 16 bits (2 bytes)
//        int byteCount = 0;
//        try
//        {
//            byteCount = device.read(buffer, 0, 2);
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        System.out.println("msb : " + buffer[0] + "  : lsb :" +buffer[1]);
//        byte lsb = (byte) (buffer[1]&0x20);
//        System.out.println("lsb & 0x20 := " + lsb );
//        byte msb = (byte) (buffer[0]&0x20);
//        System.out.println("msb & 0x20 := " + msb);
//        return false;
//    }
//    private static boolean getAlertV2(I2CDevice device) throws IOException {
//        System.out.println("getting alert state");
//
//        device.write(CONFIG_REGISTER);
//
//        byte buffer[] = new byte[2];  // receive 16 bits (2 bytes)
//        int byteCount = 0;
//        try
//        {
//            byteCount = device.read(buffer, 0, 2);
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        System.out.println("msb : " + buffer[0] + "  : lsb :" +buffer[1]);
//        byte lsb = (byte) (buffer[1]&0x20);
//        System.out.println("lsb & 0x20 := " + lsb );
//        byte msb = (byte) (buffer[0]);
//        System.out.println("msb & 0x20 := " + msb);
//
//
//        System.out.println(((((buffer[0]<<8 )| buffer[1])) & (1<<6)));
//        if(((((buffer[0]<<8 )| buffer[1])) & (1<<6))==1){
//
//            return true;
//
//        }
//        return false;
//    }
    private static boolean getAlertV3(I2CDevice device) throws IOException {
//        System.out.println("getting alert state v3");

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

//        System.out.println("get alert v3 : = " +"msb : " + buffer[0] + "  : lsb :" +buffer[1]);
//
//
//        System.out.println("v3 comparison : ");
//        System.out.println((buffer[1] >>5 ) & 0x01);

        if(((buffer[1] >>5 ) & 0x01)==maxAlertTrue){
//            System.out.println("v3 returning true");
            return true;

        }
//        System.out.println("v3 returning false");
        return false;
    }
//    return ((uint16_t) msb << 8) | lsb;

//    uint8_t MAX17043::getAlert(bool clear)
//    {
//         Read config reg, so we don't modify any other values:
//        uint16_t configReg = read16(MAX17043_CONFIG);
//        if (configReg & (1<<6))
//        {
//            if (clear) // If the clear flag is set
//            {
//                configReg &= ~(1<<6); // Clear ALRT bit manually.
//                write16(configReg, MAX17043_CONFIG);
//            }
//            return 1;
//        }
//
//        return 0;
//    }


    private static void wake(I2CDevice device) throws IOException, InterruptedException {
//        System.out.println("waking max17043 device from sleep");
        if(isSleeping(device)) {


            byte comp = getCompensation(device);
            byte thrd = getAlertThreshold(device)[1];
            byte [] buffer = {CONFIG_REGISTER,comp, (byte) (0x7f & thrd)};

            device.write(buffer);
            Thread.sleep(505);
        }
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
            byteCount = device.read(buffer2, 0, 2);
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



    private static double getSoC(I2CDevice device) throws IOException, InterruptedException {
//        System.out.println("getting soc");
        int response = device.read(SOC_REGISTER);

//        System.out.println("repsonse : "+String.format("0x%02x", response) + " raw response : " + response);

//        System.out.println("getting device version // shut be version 4//");
        Thread.sleep(10);
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
//        System.out.println("getting new SOC");
        return b;
    }
    public static double getSoCv2(I2CDevice device) throws IOException, InterruptedException {
//        System.out.println("getting soc");
//        int response = device.read(SOC_REGISTER);

//        System.out.println("repsonse : "+String.format("0x%02x", response) + " raw response : " + response);

//        System.out.println("getting device version // shut be version 4//");
        Thread.sleep(10);
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


        System.out.println("msb : " + buffer[0] + "  : lsb :" +buffer[1]);

        float d = (float) ((((buffer[0]<<8) | buffer[1]) & 0xFF00) / 256.0);

//        System.out.println("modified output : " + b);
//        System.out.println("bytes to hex : " + bytesToHex(buffer));
//        System.out.println("getting new SOC");
        return d;
    }

//    uint16_t MAX17043::read16(uint8_t address)
//    {
//        uint8_t msb, lsb;
//        int16_t timeout = 1000;
//
//        Wire.beginTransmission(MAX17043_ADDRESS);
//        Wire.write(address);
//        Wire.endTransmission(false);
//
//        Wire.requestFrom(MAX17043_ADDRESS, 2);
//        while ((Wire.available() < 2) && (timeout-- > 0))
//            delay(1);
//        msb = Wire.read();
//        lsb = Wire.read();
//
//        return ((uint16_t) msb << 8) | lsb;
//    }


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
