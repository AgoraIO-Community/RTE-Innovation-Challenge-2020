package org.daigua.crushonmeetings.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Shell {

    public static void execShell(String shell) {
        try {
            Process process = Runtime.getRuntime().exec(shell);
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void execShell(String[] shell) {
        try {
            Process process = Runtime.getRuntime().exec(shell);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(),"utf-8"));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            int re = process.waitFor();
            System.out.println(re);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
