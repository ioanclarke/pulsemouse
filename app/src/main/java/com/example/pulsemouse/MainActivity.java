package com.example.pulsemouse;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.*;
import java.net.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize clicking and mouse movement buttons
        Button btnClick = findViewById(R.id.btnClick);
        btnClick.setOnClickListener(this);
        Button btnLeft = findViewById(R.id.btnLeft);
        btnLeft.setOnClickListener(this);
        Button btnRight = findViewById(R.id.btnRight);
        btnRight.setOnClickListener(this);
        Button btnUp = findViewById(R.id.btnUp);
        btnUp.setOnClickListener(this);
        Button btnDown = findViewById(R.id.btnDown);
        btnDown.setOnClickListener(this);

        //Initialize text box used for typing
        EditText edtInput = findViewById(R.id.edtInput);
        edtInput.addTextChangedListener(textWatcher);
    }

    //Used to detect when text box is typed in
    private TextWatcher textWatcher = new TextWatcher() {
        String message = null;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //When text box is typed in, get the character typed, create socket, and send it through the socket
            //TODO find a way to directly get keyboard inputs rather than typing into an EditText (which should let us use backspace)
            char newChar = s.charAt(s.length()-1);
            System.out.println(newChar);
            message = "TYPING_" + Character.toString(newChar);
            System.out.println(message);
            Thread socketThread = new Thread(new SocketThread(message));
            socketThread.start();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {
        //When one of the clicking or mouse movement buttons is clicked, create socket and send
        //appropriate message through the socket to control mouse
        //TODO find a way to use a trackpad-like object to input mouse control
        String message = null;
        switch (v.getId()) {
            case R.id.btnClick:
                message = "MOVE_MOUSE_Click";
                break;
            case R.id.btnLeft:
                message = "MOVE_MOUSE_Left";
                break;
            case R.id.btnRight:
                message = "MOVE_MOUSE_Right";
                break;
            case R.id.btnUp:
                message = "MOVE_MOUSE_Up";
                break;
            case R.id.btnDown:
                message = "MOVE_MOUSE_Down";
                break;
            default:
                break;
        }
        System.out.println(message);
        Thread socketThread = new Thread(new SocketThread(message));
        socketThread.start();
    }

    //Socket class used to create socket and send message through
    //TODO potentially find way to use same socket for each message rather than creating a new socket each time
    class SocketThread implements Runnable {
        String HOST = "192.168.0.20";
        int PORT = 65000;
        private String msg;
        public SocketThread(String msg) {
            this.msg = msg;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void run(){
            try (
                Socket echoSocket = new Socket(HOST, PORT);
                PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
//                BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
//                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
            ) {
                System.out.println("connected");
                out.println(msg);

            } catch (UnknownHostException e) {
                System.err.println("Don't know about host " + HOST);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to " + HOST);
                System.exit(1);

            }
        }
    }

//    class SendMessageThread implements Runnable {
//        private String message;
//        SendMessageThread(String message) {
//            this.message = message;
//        }
//
//        @Override
//        public void run() {
//
//        }
//    }



//    Thread socketThread = new Thread(new SocketThread());
//    socketThread.start();



}