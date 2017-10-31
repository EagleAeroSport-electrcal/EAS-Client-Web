package org.erau.eas.clientweb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class DataReader {
    private RandomAccessFile randomAccessFile;
    private Long headerSize;
    private File file;

    public Long getHeaderSize() {
        return headerSize;
    }

    public DataReader(File input) throws FileNotFoundException {
        file = input;
        randomAccessFile = new RandomAccessFile(input,"r");
    }

    public Long getFileSize(){
        return file.length();
    }

    public void toDataStart(Long startAddress) throws IOException{
        randomAccessFile.seek(startAddress);
    }

    public byte[] getNextFromFile(Long dataLength) throws IOException {
        byte[] output = new byte[dataLength.intValue()];
        int status = randomAccessFile.read(output);
        if(status == -1)
        {
            throw new IOException();
        }
        return output;
    }

    public String getHeaderAFromFile(Integer headerASize) throws IOException{

        headerSize = headerASize.longValue();
        byte[] fromFile = new byte[headerASize];
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
