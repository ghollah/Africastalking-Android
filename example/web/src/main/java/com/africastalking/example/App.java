package com.africastalking.example;


import com.africastalking.*;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.net.*;

import static spark.Spark.*;

public class App
{

    private static final int PORT = 9000; // you can change the port to any number
    private static HandlebarsTemplateEngine hbs = new HandlebarsTemplateEngine("/views");
    private static SMSService sms;
    private static VoiceService voice;
    private static PaymentsService payment;
    private static AirtimeService airtime;

    private static void initSDK(String username, String apiKey) {
        AfricasTalking.initialize(
                username,
                apiKey,
                Format.JSON
        );
        AfricasTalking.setEnvironment(Environment.SANDBOX);
        AfricasTalking.setLogger(new Logger() {
            @Override
            public void log(String message, Object... args) {
                System.out.println(message);
            }
        });
        AfricasTalking.enableLogging(true);


        voice = AfricasTalking.getService(VoiceService.class);
        sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        payment = AfricasTalking.getService(AfricasTalking.SERVICE_PAYMENTS);
        airtime = AfricasTalking.getService(AfricasTalking.SERVICE_AIRTIME);

    }

    public static void main(String[] args) throws MalformedURLException, IOException
    {


        Socket s =  new Socket("localhost",6666);
        InputStream is = s.getInputStream();
        DataInputStream ds = new DataInputStream(is);
        String username = (String)ds.readUTF();
        String apiKey = (String)ds.readUTF();

        is.close();
        s.close();
        ds.close();

        initSDK(username, apiKey);

        exception(Exception.class, (e, req, res) -> e.printStackTrace()); // print all exceptions

        staticFiles.location("/public");

        port(PORT);

        get("/", (req, res) -> {
            Map<String, Object> data = new HashMap<>();
            data.put("req", req.pathInfo());
            return render("index", data);
        });

        get("/payments", (req, res) -> {
            return payment.checkout("TestProduct", "+254719896899", 8976, Currency.KES); // 1. product api, 2. phone number, 3.amount, 4.currency
        });

        get("/airtime", (req,res) -> {
            return airtime.send("+254719896899",100);
        });



        post("/ussd", (rq, res) -> "END Hello Friend");

        post("/register/:phone", (req, res) -> sms.send("Welcome to Awesome Company", new String[] {req.params("phone")}));

    }

    private static String render(String view, Map data) {
        return hbs.render(new ModelAndView(data, view + ".hbs"));
    }

}
