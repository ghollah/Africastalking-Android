package com.africastalking.example;

/**
 * Created by ghollah on 6/7/17.
 */
import com.africastalking.AfricasTalking;
import com.africastalking.AirtimeService;
import com.africastalking.Environment;
import com.africastalking.Format;
import com.africastalking.Logger;
import com.africastalking.PaymentsService;
import com.africastalking.SMSService;
import com.africastalking.VoiceService;

import java.io.*;
import java.net.*;

import spark.template.handlebars.HandlebarsTemplateEngine;

public class ClientAPI
{

    public static void main(String args[])
    {
        try
        {
            String user = "at2fa";
            String api = "8c940cd77db666ca100e9dd0d784191ada2ee3eaa1d0a952170a68595313f4ab";
            Socket s = new Socket("localhost",6666);
            ServerSocket ss = new ServerSocket();
            DataOutputStream ds = new DataOutputStream(s.getOutputStream());
            ds.writeUTF(user);
            ds.writeUTF(api);
            ds.flush();
            ds.close();
            s.close();

        }
        catch(Exception e)
        {System.out.println(e);}
    }
}


