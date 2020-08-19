package robhopkins.iam;

import robhopkins.wc.iam.Main;

import java.security.SecureRandom;
import java.util.Random;

public final class IAMService {
    public static IAMService newService() {
        return new IAMService();
    }

    private Thread thread;

    private IAMService() {

    }

    public int start() {
        final int port = randomPort();
        final Runnable runme = startService(port);
        thread = new Thread(runme);
        thread.start();
        pause();
        return port;
    }

    private void pause() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    private int randomPort() {
        final Random rng = new SecureRandom();
        final int basePortOffset = 30000;
        return basePortOffset + rng.nextInt(2000);
    }

    private Runnable startService(final int port) {
        return () -> {
            try {
                Main.main(String.valueOf(port));
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        };
    }

    public void stop() {
        thread.interrupt();
    }
}
