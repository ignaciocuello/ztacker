package ztacker.framework;

import ztacker.display.DisplayPanel;

public final class MainFrameWork {

    private static final double NS_PER_SECOND = 1.0E9;

    private double totalElapsedTime;

    private final Dynamic environment;
    private final DisplayPanel display;

    private Thread thread;

    private volatile boolean running;

    public MainFrameWork(Dynamic environment, DisplayPanel display) {
        this.environment = environment;
        this.display = display;
    }

    public void start() {
        initPrincipleThread();

        thread.start();
    }

    private void initPrincipleThread() {
        running = true;
        thread = new Thread(() -> {

            long currentTime = System.nanoTime();
            long lastTime = currentTime;

            double deltaTime;
            while (running) {
                currentTime = System.nanoTime();
                deltaTime = (currentTime - lastTime) / NS_PER_SECOND;

                totalElapsedTime += deltaTime;

                mainLoop(deltaTime);

                lastTime = currentTime;
            }
        });
    }

    private void mainLoop(double deltaTime) {
        if (environment != null) {
            environment.update(deltaTime, totalElapsedTime);
        }
        
        if (display != null) {
            display.repaint();
        }
    }
}
