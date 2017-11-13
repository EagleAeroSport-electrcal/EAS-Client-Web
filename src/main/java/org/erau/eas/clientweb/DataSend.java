package org.erau.eas.clientweb;

public class DataSend {
    private int flightId;

    private int deviceId;

    private long packetSize;

    private byte[] data;

    public DataSend(int flightId, int deviceId) {
        this.flightId = flightId;
        this.deviceId = deviceId;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public long getPacketSize() {
        return packetSize;
    }

    public void setPacketSize(long packetSize) {
        this.packetSize = packetSize;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
