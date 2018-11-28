package org.erau.eas.clientweb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.primitives.Bytes;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    private static long fileLocation;
    public static void main(String[] args) throws JsonProcessingException {
        long packetSize = 24;
        int headerSize = 0x800;
        File workingFile;
        String output = "";
        String[] sensorSet;
        byte mac[] = {0};
        final HTTPClient client = new HTTPClient("http://localhost:8080");
        int deviceID = 0;
        int flightID = 0;
        DataReader dataReader = null;

        // Final path for the file will be /EAS/logs
        GetLatestFile getLatestFile = new GetLatestFile("C:\\Users\\ferri\\IdeaProjects\\EAS-Client-Web\\src\\main\\resources");
        workingFile = getLatestFile.getLastModifiedFile();

        try {
            dataReader = new DataReader(workingFile);
            output = dataReader.getHeaderAFromFile(headerSize);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try {
            InetAddress address = InetAddress.getLocalHost();
            NetworkInterface nwi = NetworkInterface.getByInetAddress(address);
            mac = nwi.getHardwareAddress();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        MacAddress macAddress = new MacAddress(mac);

        try {
            deviceID = client.getDeviceID(macAddress.getAddress());
            flightID = client.getFlightID();
        }
        catch (Exception e){
            e.printStackTrace();
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

        try {
            if(dataReader != null){
                dataReader.toDataStart((long) headerSize);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert dataReader != null;
        fileLocation = dataReader.getHeaderSize();

        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        final DataReader finalDataReader = dataReader;
        int finalFlightID = flightID;
        int finalDeviceID = deviceID;
        executorService.schedule(() -> DataCollectorTask(client, finalDataReader, finalFlightID, finalDeviceID, packetSize),0, TimeUnit.MILLISECONDS);
//        executorService.scheduleAtFixedRate(new Runnable() {
//            public void run() {
//                DataCollectorTask(client, finalDataReader, finalFlightID, finalDeviceID);
//            }
//        }, 200, 200, TimeUnit.MILLISECONDS);
    }

    private static void DataCollectorTask(HTTPClient client, DataReader dataReader, int flightId, int deviceId, long packetSize){
        List<Byte> toSend = new ArrayList<>();
        Long loops = 0L;
        Boolean endOfFile = false;
        Long fileSize = dataReader.getFileSize();
        while(!endOfFile && fileLocation < fileSize){
            try{
                byte[] toAdd = dataReader.getNextFromFile(packetSize);
                loops++;
                for (byte aToAdd : toAdd) {
                    toSend.add(aToAdd);
                }
                fileLocation += packetSize;
            } catch (IOException e) {
                try {
                    if(loops != 0){
                        dataReader.toDataStart((loops * packetSize) + dataReader.getHeaderSize());
                    }
                    endOfFile = true;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        byte[] output = Bytes.toArray(toSend);
        DataSend dataSend = new DataSend(flightId, deviceId);
        dataSend.setPacketSize(packetSize);
        dataSend.setData(output);

        try {
            client.sendSensorData(dataSend);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}