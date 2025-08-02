package com.github.david32768.jynxround;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.github.david32768.jynxfree.jynx.Global.LOG;
import static com.github.david32768.jynxround.my.Message.M414;
import static com.github.david32768.jynxround.my.Message.M415;
import static com.github.david32768.jynxround.my.Message.M416;
import static com.github.david32768.jynxround.my.Message.M990;

public record CompareStrings(String original, String result) {

    public boolean compareString() {
        boolean success = result.equals(original);
        if (success) {
            // "string comparison succeeded"
            LOG(M415);
        } else {
            // "string comparison failed"
            LOG(M416);
            try {
                diffString(original, result);
            } catch (IOException | InterruptedException ex) {
                LOG(ex);
            }
        }
        return success;
    }

    private static final List<String> WINDOWS_COMPARE_COMMAND = List.of("fc.exe", "/N");
    private static final List<String> MACOS_COMPARE_COMMAND = List.of("diff", "-u");

    private static final int MAX_LINES = 100;
    
    private static boolean diffString(String originalstr, String resultstr)
            throws IOException, InterruptedException {
        boolean windows = System.getProperty("os.name").startsWith("Windows"); 
        var compare = windows?
                WINDOWS_COMPARE_COMMAND:
                MACOS_COMPARE_COMMAND;

        Path path1 = Files.createTempFile(null, ".ORIGINAL");
        Path path2 = Files.createTempFile(null, ".RESULT");
        Files.writeString(path1, originalstr);
        Files.writeString(path2, resultstr);
        var cmdlist = new ArrayList<>(compare);
        cmdlist.add(path1.toString());
        cmdlist.add(path2.toString());
        var process = new ProcessBuilder(cmdlist)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .redirectInput(ProcessBuilder.Redirect.INHERIT)
                .start();
        int ct = 0;
        try (var in = new Scanner(process.getInputStream())) {
            while (in.hasNextLine()) {
                var line = in.nextLine();
                ++ct;
                if (ct > MAX_LINES) {
                    // "difference output exceeds %d lines and has been truncated"
                    LOG(M414, MAX_LINES);
                    break;
                }
                if (windows) {
                    line = line
                        .replace(path1.toString().toUpperCase(), "ORIGINAL")
                        .replace(path2.toString().toUpperCase(), "RESULT");
                } else {
                    line = line
                        .replace(path1.toString(), "ORIGINAL")
                        .replace(path2.toString(), "RESULT");
                    
                }
                // "%s"
                LOG(M990,line);
            }
        }
        process.waitFor();
        Files.deleteIfExists(path1);
        Files.deleteIfExists(path2);
        return false; 
    }

}
