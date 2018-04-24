/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runonpi;

import java.util.Scanner;
import java.util.concurrent.Executor;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.security.*;
import static java.security.MessageDigest.isEqual;

/**
 *
 * @author jt024
 */
public class ROP {
    public static boolean found = false;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String pw = sc.next();
        
        Executor executor = java.util.concurrent.Executors.newSingleThreadExecutor();
        executor.execute(new Runnable(){
            @Override
            public void run(){
                String fileName = "src\\runonpi\\Dictionary.txt";
                String line;

                try 
                {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    md.update(pw.getBytes());
                    byte[] thePW = md.digest();
                    StringBuilder sb = new StringBuilder();
                    for (byte b : thePW) 
                    {
                        sb.append(String.format("%02x", b & 0xff));
                    }
                    FileReader fileReader =
                        new FileReader(fileName);
                    BufferedReader bufferedReader =
                        new BufferedReader(fileReader);
            
                    while((line = bufferedReader.readLine()) != null) {
                        md = MessageDigest.getInstance("MD5");
                        md.update(line.getBytes());
                        byte[] theLine = md.digest();
                        sb = new StringBuilder();
                        for (byte b : theLine) {
                            sb.append(String.format("%02x", b & 0xff));
                        }
                        if (isEqual(thePW, theLine)) {
                            found = true;
                            System.out.println("found");
                            break;
                        }
                    }            
                }catch(FileNotFoundException ex) {
                    System.out.println(
                    "Unable to open file '" 
                        +fileName+ "'");
                }catch(IOException ex) {
                    System.out.println(
                    "Error reading file '"
                        +fileName+ "'");
                }catch (NoSuchAlgorithmException ex) {
                    System.exit(0);
                }
            }
            
        });
        
        
        
    }
    
}
