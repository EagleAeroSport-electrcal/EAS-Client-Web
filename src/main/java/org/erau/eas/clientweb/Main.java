package org.erau.eas.clientweb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        File workingFile;
        String output = "";
        String[] sensorSet;

        GetLatestFile getLatestFile = new GetLatestFile("/Users/ferrinkatz/IdeaProjects/EAS-Client-Web/src/main/resources/");
        workingFile = getLatestFile.getLastModifiedFile();

        try {
            DataReader dataReader = new DataReader(workingFile);
            output = dataReader.getHeaderAFromFile();
        }
        catch (FileNotFoundException e){
            output = "asdf";
        }
        catch (IOException e){
            output = "hjkl";
        }

        sensorSet = output.split("-{2,}");


    }
}
