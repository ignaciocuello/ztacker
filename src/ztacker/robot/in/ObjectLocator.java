package ztacker.robot.in;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public final class ObjectLocator {

	//TODO make this be read from a file
	/** The width of the portion of the screen that is captured */
    public static final int WIDTH = 300;
    /** The height of the portion of the screen that is captured */
    public static final int HEIGHT = 350;

    private final int[] gameCorner;
    private final int[] startCorner;
    private final int[] restartCorner;
    private final int[] playCorner;

    private Robot robot;

    private Point gameCache;
    private Point startCache;
    private Point restartCache;
    private Point playCache;

    private boolean inGame;
    private boolean inStart;
    private boolean inPlay;
    private boolean inRestart;

    private BufferedImage gmsrc;

    public ObjectLocator(
            int[] gameCorner, int[] startCorner, int[] restartCorner,
            int[] playCorner) {
        this.gameCorner = gameCorner;
        this.startCorner = startCorner;
        this.restartCorner = restartCorner;
        this.playCorner = playCorner;
    }

    public final void update() {
        if (gameCache != null) {
            gmsrc = robot.createScreenCapture(
                    new Rectangle(gameCache.x, gameCache.y, WIDTH, HEIGHT));
            inGame = isTopLeft(0, 0, gameCorner, gmsrc);
            if (!inGame) {
                if (playCache != null) {
                    inPlay
                            = isTopLeft(playCache.x, playCache.y,
                                    playCorner, gmsrc);
                } else {
                    playCache = updateBounds(playCorner, gmsrc);
                }

                if (!inPlay) {
                    if (restartCache != null) {
                        inRestart
                                = isTopLeft(restartCache.x, restartCache.y,
                                        restartCorner, gmsrc);
                    } else {
                        restartCache = updateBounds(restartCorner, gmsrc);
                    }
                }
            }
        } else {
            BufferedImage screen = robot.createScreenCapture(
                    new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            Point gamePoint = updateBounds(gameCorner, screen);
            if (gamePoint != null) {
                gameCache = gamePoint;
            }
        }
    }

    public void updateInStart() {
        if (startCache != null) {
            inStart
                    = isTopLeft(startCache.x, startCache.y, startCorner, gmsrc);
        } else {
            startCache = updateBounds(startCorner, gmsrc);
        }
    }

    private Point updateBounds(int[] corner,
            BufferedImage screen) {
        for (int y = 0; y < screen.getHeight(); y++) {
            for (int x = 0; x < screen.getWidth(); x++) {
                if (isTopLeft(x, y, corner, screen)) {
                    return new Point(x, y);
                }
            }
        }

        return null;
    }

    private boolean isTopLeft(int x, int y, int[] corner,
            BufferedImage screen) {        
        for (int i = 0; i < corner.length; i++) {
            if (x + i >= screen.getWidth()
                    || screen.getRGB(x + i, y) != corner[i]) {
                return false;
            }
        }

        return true;
    }

    public boolean inGame() {
        return inGame;
    }

    public Point getStartPress() {
        return new Point(gameCache.x + startCache.x, gameCache.y
                + startCache.y);
    }

    public boolean inStart() {
        return inStart;
    }

    public Point getRestartPress() {
        return new Point(gameCache.x + restartCache.x, gameCache.y
                + restartCache.y);
    }

    public boolean inRestart() {
        return inRestart;
    }

    public Point getPlayPress() {
        return new Point(gameCache.x + playCache.x, gameCache.y + playCache.y);
    }

    public boolean inPlay() {
        return inPlay;
    }

    public BufferedImage getGameSource() {
        return gmsrc;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }
}
