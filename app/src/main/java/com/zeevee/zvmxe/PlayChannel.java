package com.zeevee.zvmxe;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.widget.VideoView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by zeevee on 2/19/16.
 */
public class PlayChannel extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... params) {
        byte [] buffer = new byte[1500];
        DatagramPacket datagramPkt = new DatagramPacket(buffer, buffer.length);
        MulticastSocket mSock = null;
        System.out.println("   in PlayChannel  ");
        try {
            InetAddress mAddr = null;
            mSock = new MulticastSocket(21216);
            mAddr = InetAddress.getByName(params[0]);
            mSock.joinGroup(mAddr);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        //doMedia();
/*
        while(true) {
            try {
                mSock.receive(datagramPkt);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.printf("Received packet lenght %d\n", datagramPkt.getLength());
        }
        */
        return null;
    }

    protected void doMedia() {
        String url = "udp://224.1.1.86:21216";
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        System.out.println("mediaPlayer started");
    }

}
