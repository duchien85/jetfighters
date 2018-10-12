package com.badlogic.jetfighters.client;

import com.badlogic.jetfighters.dto.JetFighterDto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UdpClientOld {

    private static InetAddress serverAddress = null;

    static {
        try {
            serverAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        try {
            DatagramSocket socket = new DatagramSocket();
            JetFighterDto dto = new JetFighterDto(22.55f, 17.34f);
            byte[] dtoBytes = serialize(dto);
            System.out.println("DTO length to send: " + dtoBytes.length);
            DatagramPacket packet = new DatagramPacket(dtoBytes, dtoBytes.length, serverAddress, 9956);
            socket.send(packet);
            socket.receive(packet);
            System.out.println("Response" + new String(packet.getData()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
}
