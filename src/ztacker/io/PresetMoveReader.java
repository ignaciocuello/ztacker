package ztacker.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import ztacker.move.PresetMove;
import ztacker.move.SoftDropCommands;
import ztacker.robot.out.Command;
import ztacker.tetromino.Tetromino;

public class PresetMoveReader extends GridReader {

    private static final int GRID_INDEX = 0;
    private static final int FILL_INDEX = 1;
    private static final int FREE_SOFT_INDEX = 2;
    private static final int FREE_HARD_INDEX = 3;

    public ArrayList<PresetMove> readPresetMoves(BufferedReader br)
            throws IOException {
        ArrayList<long[]> grids = readGrids(br);
        ArrayList<PresetMove> preset = new ArrayList<>();

        String line;
        while ((line = readNextLine(br)) != null && line.charAt(0) != '[') {
            preset.add(readPresetMove(removeCommentFrom(line), grids,
                    preset));
        }

        return preset;
    }

    private PresetMove readPresetMove(String line, ArrayList<long[]> grids,
            ArrayList<PresetMove> preset) {
        if (line.charAt(0) == 'M') {
            line = line.substring(1, line.length())
                    .replace("(", "").replace(")", "");
            return preset.get(Integer.parseInt(line)).mirror();
        } else {
            SoftDropCommands softDrop = null;
            if (line.contains("-")) {
                String[] strs = line.split("-");
                String sd = strs[1];
                line = strs[0];

                strs = sd.split(",");
                softDrop = new SoftDropCommands(
                        readGrid(strs[0].replace("[", "")),
                        Command.valueOf(strs[1].replace("]", "")));
            }

            Tetromino type = Tetromino.valueOf(line.substring(0, 1) + "_TYPE");
            line = line.substring(1).replace("(", "").replace(")", "");
            String[] gridReferences = line.split(",");
            long[] grid = grids.get(Integer.parseInt(
                    gridReferences[GRID_INDEX]));

            if (gridReferences.length > 1) {
                long[] fill = grids.get(Integer.parseInt(
                        gridReferences[FILL_INDEX]));
                if (gridReferences.length > 2) {
                    long[] freeSoft
                            = grids.get(Integer.parseInt(
                                    gridReferences[FREE_SOFT_INDEX]));
                    if (gridReferences.length > 3) {
                        long[] freeHard
                                = grids.get(Integer.parseInt(
                                        gridReferences[FREE_HARD_INDEX]));
                        return new PresetMove(grid, fill, freeSoft,
                                freeHard, type, softDrop);
                    }
                    return new PresetMove(grid, fill, freeSoft, type,
                            softDrop);
                }

                return new PresetMove(grid, fill, type);
            }

            return new PresetMove(grid, type);
        }
    }

}
