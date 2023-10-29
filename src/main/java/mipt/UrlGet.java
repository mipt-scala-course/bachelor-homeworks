package mipt;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executors;

public class UrlGet {
    private static int count = 0;
  
    public static String getURL(String url) {
        int index = count++;
        System.out.println("New read: " + index);
        try (InputStream in = new URL(url).openStream()) {
            byte[] bytes = in.readAllBytes(); 
            System.out.println("Finished: " + index);
            return new String(bytes);
        } catch (IOException e) {
            System.out.println("FAILED: " + index);
            throw new RuntimeException(e);
        }
    }


    public static void readThreaded() {
        for (int i = 0; i < 1_000_000; i++) {
            // good, old Java Threads
            new Thread( getURL("https://www.google.com"))
                .start();
        }
    }

    public static void readVirtual() {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 1_000_000; i++) {
                // Virtual threads
                executor.submit(() -> getURL("https://www.google.com"));
            }
        }
    }

}
