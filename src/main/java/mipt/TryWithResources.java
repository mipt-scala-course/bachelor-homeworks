package mipt;

import java.io.BufferedReader;
import java.io.FileReader;


public class TryWithResources {
  
    public static void main(String[] args) {

        try (BufferedReader reader = new BufferedReader(new FileReader("file2.txt"))) {
            String content = reader.readLine();
            System.out.println(content);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
