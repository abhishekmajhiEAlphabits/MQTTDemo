package com.example.mqttdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MainActivity extends AppCompatActivity {

    MqttClient client;
    MqttAndroidClient mqttAndroidClient;
    String serverURI = "tcp://192.168.1.18:1883";
    String topic = "event";
    String clientId = MqttClient.generateClientId();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*try {
            client = new MqttClient("tcp://192.168.1.18:1883", "", new MemoryPersistence());
            client.connect();
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Toast.makeText(MainActivity.this, "connected!!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Toast.makeText(MainActivity.this, "arrived", Toast.LENGTH_LONG).show();

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Toast.makeText(MainActivity.this, "delivered!!", Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }*/


        //creating mqttAndroidClient object and setting the callback for responses
        mqttAndroidClient = new MqttAndroidClient(this, serverURI, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Log.w("mqtt", s);
            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Mqtt", mqttMessage.toString());
                Toast.makeText(MainActivity.this, "Received message : " + mqttMessage.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }


    //method called on connect button click
    public void connect(View view) {
/*
        String clientId = MqttClient.generateClientId();
        try {
            client = new MqttClient("tcp://192.168.1.18:1883", "", new MemoryPersistence());
            client.connect();
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Toast.makeText(MainActivity.this, "connected!!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Toast.makeText(MainActivity.this, "arrived", Toast.LENGTH_LONG).show();

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Toast.makeText(MainActivity.this, "delivered!!", Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        /*try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity.this, "connected!!", Toast.LENGTH_LONG).show();
                    //setSubscription();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MainActivity.this, "connection failed!!", Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
*/


        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        //mqttConnectOptions.setUserName(username);
        //mqttConnectOptions.setPassword(password.toCharArray());

        try {

            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    //subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Failed to connect to server " + exception.toString());
                }
            });


        } catch (MqttException ex) {
            ex.printStackTrace();
        }

    }


    //method called on publish button click
    public void publish(View view) {
        //String topic = "event";
        String message = "the payload is working";
        try {
            //client.connect();
            mqttAndroidClient.publish(topic, message.getBytes(), 0, false);
            Toast.makeText(this, "Published Message", Toast.LENGTH_SHORT).show();
        } catch (MqttException e) {
            e.printStackTrace();
        }

       /* client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Toast.makeText(MainActivity.this, "connection lost", Toast.LENGTH_LONG).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Toast.makeText(MainActivity.this, "arrived" + message.getPayload(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Toast.makeText(MainActivity.this, "delivered!!", Toast.LENGTH_LONG).show();
            }
        });

        */
    }

    //method called on subscribe button click
    public void subscribe(View view) {
        try {

            mqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w("Mqtt", "Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Subscribed fail!");
                }
            });

        } catch (MqttException ex) {
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }

    }

    //method called on disconnect button click
    public void disconnect(View view) {
        try {
            mqttAndroidClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    //method called on unsubscribe button click
    public void unsubscribe(View view) {
        try {
            mqttAndroidClient.unsubscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}