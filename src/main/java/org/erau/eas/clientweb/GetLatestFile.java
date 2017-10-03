package org.erau.eas.clientweb;

import java.io.File;
import java.io.PrintStream;

public class GetLatestFile {

    private File lastModifiedFile;

    public GetLatestFile(String path) {
        File dir = new File(path);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            this.lastModifiedFile = null;
        }

        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
            }
        }
        this.lastModifiedFile = lastModifiedFile;
    }

    public File getLastModifiedFile() {
        return lastModifiedFile;
    }
}
