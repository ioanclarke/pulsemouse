package com.example.sockettest;

import java.awt.event.InputEvent;
import java.net.*;
import java.io.*;
import java.awt.*;

public class Server {
    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
                //PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        ) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread serverThread = new Thread(new ServerThread(clientSocket));
                serverThread.start();
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}

class ServerThread implements Runnable {

    private Socket socket = null;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("thread started");
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                //out.println(inputLine);
                System.out.println(inputLine);
                try {
                    Robot robot = new Robot();
                    if (inputLine.startsWith("MOVE_MOUSE")){
                        int mouse_x = MouseInfo.getPointerInfo().getLocation().x;
                        int mouse_y = MouseInfo.getPointerInfo().getLocation().y;
                        switch (inputLine) {
                            case "MOVE_MOUSE_Click":
                                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                                break;
                            case "MOVE_MOUSE_Left":
                                robot.mouseMove(mouse_x - 50, mouse_y);
                                break;
                            case "MOVE_MOUSE_Right":
                                robot.mouseMove(mouse_x + 50, mouse_y);
                                break;
                            case "MOVE_MOUSE_Up":
                                robot.mouseMove(mouse_x, mouse_y - 50);
                                break;
                            case "MOVE_MOUSE_Down":
                                robot.mouseMove(mouse_x, mouse_y + 50);
                                break;
                            default:
                                break;
                        }
                    } else if (inputLine.startsWith("TYPING")) {
                        char c = inputLine.charAt(7);
                        System.out.println(c);
                        robot.keyPress(Character.toUpperCase(c));
                        robot.keyRelease(Character.toUpperCase(c));
                    }


//                    if (inputLine.equals("Click")) {
//                        robot.mouseMove(500, 500);
//                        //robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
//                    }
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


