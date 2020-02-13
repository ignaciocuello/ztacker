package ztacker.move.zt;

import java.util.ArrayList;

public final class LeftBaseSurface {

    private final int width;
    private final int height;
    private final int index;

    public LeftBaseSurface(int width, int height, int index) {
        this.width = width;
        this.height = height;
        this.index = index;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getIndex() {
        return index;
    }

    public ArrayList<LeftBaseSurface> generateChildrenSurfaces() {
        if (width == 1) {
            return null;
        }

        ArrayList<LeftBaseSurface> baseSurfaces = new ArrayList<>();
        for (int w = width - 1; w >= 1; w--) {
            int times = width - w + 1;
            for (int i = 0; i < times; i++) {
                baseSurfaces.add(new LeftBaseSurface(w, height, index + i));
            }
        }

        return baseSurfaces;
    }

    @Override
    public int hashCode() {
        return width & height & index;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LeftBaseSurface) {
            LeftBaseSurface qbs = (LeftBaseSurface) o;
            return width == qbs.width && height == qbs.height
                    && index == qbs.index;
        }

        return false;
    }
}
