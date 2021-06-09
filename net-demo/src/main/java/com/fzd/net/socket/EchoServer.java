package com.fzd.net.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author fuzude
 * @version : 2021-06-07
 */
@Slf4j
public class EchoServer {

    public static void main(String... args) {
        if (args.length != 1) {
            System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }
        Integer portNumber = Integer.parseInt(args[0]);
        log.info("echo server started: " + portNumber);
        try (ServerSocket serverSocket = new ServerSocket(portNumber);
             Socket socketClient = serverSocket.accept();
             PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                out.println(inputLine);
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
