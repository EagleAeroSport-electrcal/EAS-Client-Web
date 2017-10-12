package org.erau.eas.clientweb;

public class MacAddress {
    private String address;

    public MacAddress(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.length; i++)
        {
            stringBuilder.append(String.format("%02x", data[i]));
        }
        address = stringBuilder.toString();
    }

    public String getAddress() {
        return address;
    }
}
