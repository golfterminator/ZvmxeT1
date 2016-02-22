package com.zeevee.zvmxe;

import android.os.AsyncTask;
import android.provider.DocumentsContract;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;

import static java.lang.Thread.sleep;

public class FindChannels extends AsyncTask<String, Void, Void> {
    byte [] buffer = new byte[1500];

    @Override
    protected Void doInBackground(String... params) {
        DatagramPacket datagramPkt = new DatagramPacket(buffer, buffer.length);
        MulticastSocket mSock = null;
        Document doc;

        System.out.println("   in FindChannels ");
        try {
            mSock = new MulticastSocket(21217);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InetAddress mAddr = null;
        try {
            mAddr = InetAddress.getByName(params[0]);
            mSock.joinGroup(mAddr);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                mSock.receive(datagramPkt);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.printf("Received packet lenght %d\n", datagramPkt.getLength());
            try {
                String bufferstr = new String(buffer, "UTF-8");
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                InputSource insrc = new InputSource(new StringReader(bufferstr));
                doc  = builder.parse(insrc);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            System.out.println("Root element: " );
            if (datagramPkt.getData().toString().contains("Zeevee_channel_guide:")) {
                System.out.println("found channel ");
            } else
                System.out.println("FindChannels: received message but not guide info?");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}