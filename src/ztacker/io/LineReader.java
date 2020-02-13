package ztacker.io;

import java.io.BufferedReader;
import java.io.IOException;

public class LineReader {
    
    @SuppressWarnings("empty-statement")
    public String readNextLine(BufferedReader br) throws IOException {
        String line = "";
        while ((line = br.readLine()) != null 
                && (line = line.trim()).isEmpty());
        return line;
    }
    
    public String removeCommentFrom(String line) {
        int commentIndex = line.indexOf("#");
        return line.substring(
                0, commentIndex == -1 ? line.length() : commentIndex).trim();
    }
    
    public final String readNextCleanLine(BufferedReader br) throws IOException {
        return removeCommentFrom(readNextLine(br));
    }
}
