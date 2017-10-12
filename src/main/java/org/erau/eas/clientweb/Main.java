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

        GetLatestFile getLatestFile = new GetLatestFile("/Users/ferrinkatz/IdeaProjects/EAS-Client-Web/src/main/resources/");
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

        sensorSet = output.split("-{2,}");
        LinkedList<Sensor> sensorOutput = new LinkedList<Sensor>();

        for(int i = 0; i < sensorSet.length; i++)
        {
            HashMap<String, String> sensorData = new HashMap<String, String>();
            String[] currentSensor = sensorSet[i].split("\\R");
            for (int j = 0; j < currentSensor.length; j++)
            {
                String[] currentSection = currentSensor[j].split(":");
                sensorData.put(currentSection[0], currentSection[1]);
            }
            Sensor sensor = new Sensor();
            sensor.setFlightID(flightID);
            sensor.setBoardId(deviceID);
            sensor.setCalibration(0);
            sensor.setType(sensorData.get("Sensor type "));
            sensor.setSensorId(Integer.parseInt(sensorData.get("Sensor unique ID ").trim()));
            sensorOutput.add(sensor);
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            client.sendSensorSet(mapper.writeValueAsString(sensorOutput));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
