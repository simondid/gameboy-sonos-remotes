package com.simon.sonos;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by simon on 3/14/2017.
 */
public class CommandLine {

    public CommandLine() {


    }
    public static void ShutDownLinux(){
        StringBuffer output = new StringBuffer();


        try {
            Process p = Runtime.getRuntime().exec("sudo shutdown");
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(output);
    }
    public static void pingGoogle(){
        StringBuffer output = new StringBuffer();


        try {
            Process p = Runtime.getRuntime().exec("ping -n 3 google.com");
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(output);
    }
}
