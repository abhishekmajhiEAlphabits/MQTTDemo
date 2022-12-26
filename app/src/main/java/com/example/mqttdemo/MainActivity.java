package com.example.mqttdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

//import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MainActivity extends AppCompatActivity {

    MqttClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }


    public void connect(View view) {

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

    }


    public void publish(View view) {
        String topic = "event";
        String message = "the payload is working";
        try {
            //client.connect();
            client.publish(topic, message.getBytes(), 0, false);
            Toast.makeText(this, "Published Message", Toast.LENGTH_SHORT).show();
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
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
    }

    public void subscribe(View view) {
        try {
            //client.setCallback((MqttCallback) this);
            client.connect();
            client.subscribe("abhi", 1);
            client.setCallback(new MqttCallback() {
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

            /*IMqttMessageListener iMqttMessageListener = new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.i("TAG", "subscribed");
                }
            };


*/


        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}