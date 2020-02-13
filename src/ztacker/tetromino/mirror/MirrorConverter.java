package ztacker.tetromino.mirror;

public final class MirrorConverter {
    
    public long[] convert(long[] hgrid) {
        if (hgrid == null) {
            return null;
        }
        
        long[] newMatrix = new long[3];
        long column =
                0b1000_1000_1000_1000_1000_1000_1000_1000_1000_1000_1000_1000L;
        
        long c0 = hgrid[0] & column;
        long c9 = hgrid[2] & (column >>> 2);
        
        newMatrix[0] |= c9 << 2;
        newMatrix[2] |= c0 >>> 2;
        
        long c1 = hgrid[0] & (column >>> 1);
        long c8 = hgrid[2] & (column >>> 1); 
        
        newMatrix[0] |= c8;
        newMatrix[2] |= c1;
        
        long c2 = hgrid[0] & (column >>> 2);
        long c7 = hgrid[2] & column;
        
        newMatrix[0] |= c7 >>> 2;
        newMatrix[2] |= c2 << 2;
        
        long c3 = hgrid[0] & (column >>> 3);
        long c6 = hgrid[1] & (column >>> 2);
        
        newMatrix[0] |= c6 >>> 1;
        newMatrix[1] |= c3 << 1;
        
        long c4 = hgrid[1] & column;
        long c5 = hgrid[1] & (column >> 1);
        
        newMatrix[1] |= c4 >>> 1;
        newMatrix[1] |= c5 << 1;    
        
        return newMatrix;
    }
}
