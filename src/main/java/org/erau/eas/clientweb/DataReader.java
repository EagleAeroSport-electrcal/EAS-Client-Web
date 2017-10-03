package org.erau.eas.clientweb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class DataReader {
    private RandomAccessFile randomAccessFile;

    public DataReader(File input) throws FileNotFoundException {

        randomAccessFile = new RandomAccessFile(input,"r");
    }

    public void toDataStart(Long startAddress) throws IOException{
        randomAccessFile.seek(startAddress);
    }

    public byte[] getNextFromFile(Long dataLength) throws IOException {
        byte[] output = new byte[dataLength.intValue()];
        randomAccessFile.read(output);
        return output;
    }

    public String getHeaderAFromFile() throws IOException{

        byte[] fromFile = new byte[0x800];
        randomAccessFile.seek(0);
        randomAccessFile.read(fromFile);

        StringBuilder toReturn = new StringBuilder();
        toReturn.setLength(0);

        for(int i = 0; i < 0x800; i++)
        {
            if(fromFile[i] != 0x00){
                char temp = (char)fromFile[i];
                toReturn.append(temp);
            }
        }

        return toReturn.toString();
    }
}
