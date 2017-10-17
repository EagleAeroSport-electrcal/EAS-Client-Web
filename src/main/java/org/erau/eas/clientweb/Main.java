package org.erau.eas.clientweb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        File workingFile;
        String output = "";
        String[] sensorSet;
        byte mac[] = {0};
        HTTPClient client = new HTTPClient("http://localhost:8080");
        int deviceID = 0;
        int flightID = 0;

        // Final path for the file will be /EAS/logs
        GetLatestFile getLatestFile = new GetLatestFile("C:\\Users\\Ferrin\\IdeaProjects\\EAS-Client-Web\\src\\main\\resources");
        workingFile = getLatestFile.getLastModifiedFile();

        try {
            DataReader dataReader = new DataReader(workingFile);
            output = dataReader.getHeaderAFromFile();
        }
        catch (Exception e){
            System.out.println(e);
        }

        try {
            InetAddress address = InetAddress.getLocalHost();
            NetworkInterface nwi = NetworkInterface.getByInetAddress(address);
            mac = nwi.getHardwareAddress();
        }
        catch (Exception e){
            System.out.println(e);
        }

        MacAddress macAddress = new MacAddress(mac);

        try {
            deviceID = client.getDeviceID(macAddress.getAddress());
            flightID = client.getFlightID();
        }
        catch (Exception e){
            System.out.println(e);
        }

        Sensor sensor = new Sensor();

        sensor.setFlightID(flightID);
        sensor.setBoardId(deviceID);
        sensor.setBody(output);

        ObjectMapper mapper = new ObjectMapper();

        try {
            client.sendSensorSet(mapper.writeValueAsString(sensor));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}