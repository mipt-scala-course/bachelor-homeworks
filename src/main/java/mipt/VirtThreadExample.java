package mipt;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;


public class VirtThreadExample {

    public static void main(String[] args) {

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 100)
                .forEach(i ->
                    executor.submit(() -> {
                        Thread.sleep(Duration.ofSeconds(1));
                        for(int i1 = 0; i1 < 100; i1++) {
                            System.out.print(i + "->" + i1 + "; ");
                        }
                        System.out.println("");
                        return i;
                    }
                )
            );
        }

    }


}
