import java.io.*;
import java.util.*;

public class GridMonitor implements GridMonitorInterface {
    private double[][] baseGrid;
    private double[][] surroundingSumGrid;
    private double[][] surroundingAvgGrid;
    private double[][] deltaGrid;
    private boolean[][] dangerGrid;

    /**
     * Constructor that takes in the name of a text file.
     * The first line of the file contains two integers (rows and columns).
     * The subsequent lines contain the grid values.
     */
    public GridMonitor(String testFile) throws IOException {
        // Read the file and populate the base grid
        List<double[]> tempGrid = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(testFile))) {
            String[] dimensions = br.readLine().split(" ");
            int rows = Integer.parseInt(dimensions[0]);
            int cols = Integer.parseInt(dimensions[1]);

            for (int i = 0; i < rows; i++) {
                String[] values = br.readLine().split(" ");
                double[] row = new double[cols];
                for (int j = 0; j < cols; j++) {
                    row[j] = Double.parseDouble(values[j]);
                }
                tempGrid.add(row);
            }
        }

        // Convert the list to a 2D array
        baseGrid = tempGrid.toArray(new double[0][0]);

        // Create grids of the same size
        int rows = baseGrid.length;
        int cols = baseGrid[0].length;
        surroundingSumGrid = new double[rows][cols];
        surroundingAvgGrid = new double[rows][cols];
        deltaGrid = new double[rows][cols];
        dangerGrid = new boolean[rows][cols];

        // Compute all the necessary grids
        computeSurroundingSumGrid();
        computeSurroundingAvgGrid();
        computeDeltaGrid();
        computeDangerGrid();
    }

    /**
     * Returns a copy of the original base grid.
     */
    @Override
    public double[][] getBaseGrid() {
        return copy2DArray(this.baseGrid);
    }

    /**
     * Returns a copy of the surrounding sum grid.
     */
    @Override
    public double[][] getSurroundingSumGrid() {
        return copy2DArray(this.surroundingSumGrid);
    }

    /**
     * Returns a copy of the surrounding average grid.
     */
    @Override
    public double[][] getSurroundingAvgGrid() {
        return copy2DArray(this.surroundingAvgGrid);
    }

    /**
     * Returns a copy of the delta grid (50% of the surrounding average).
     */
    @Override
    public double[][] getDeltaGrid() {
        return copy2DArray(this.deltaGrid);
    }

    /**
     * Returns a copy of the boolean danger grid, where true indicates
     * a cell is at risk of exploding.
     */
    @Override
    public boolean[][] getDangerGrid() {
        boolean[][] copy = new boolean[dangerGrid.length][dangerGrid[0].length];
        for (int r = 0; r < dangerGrid.length; r++) {
            System.arraycopy(dangerGrid[r], 0, copy[r], 0, dangerGrid[r].length);
        }
        return copy;
    }

    /**
     * A well-formatted, clearly labeled String with useful information
     * about the GridMonitor. We print out each of our grids here.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== GridMonitor Report ===\n");

        sb.append("--- Base Grid ---\n");
        sb.append(format2DArray(baseGrid)).append("\n");

        sb.append("--- Surrounding Sum Grid ---\n");
        sb.append(format2DArray(surroundingSumGrid)).append("\n");

        sb.append("--- Surrounding Average Grid ---\n");
        sb.append(format2DArray(surroundingAvgGrid)).append("\n");

        sb.append("--- Delta Grid (50% of Avg) ---\n");
        sb.append(format2DArray(deltaGrid)).append("\n");

        sb.append("--- Danger Grid (true/false) ---\n");
        sb.append(format2DBooleanArray(dangerGrid)).append("\n");

        return sb.toString();
    }

    /* ----------------------------------------------------------------------
       Below are all helper methods used by the constructor to fill the grids.
       ---------------------------------------------------------------------- */

    /**
     * For each cell, compute the sum of its 4 neighbors:
     * up, down, left, right. For out-of-bounds neighbors, "mirror"
     * by using the cell's own value instead.
     */
    private void computeSurroundingSumGrid() {
        int rows = baseGrid.length;
        int cols = baseGrid[0].length;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double up    = (r > 0)            ? baseGrid[r - 1][c] : baseGrid[r][c];
                double down  = (r < rows - 1)     ? baseGrid[r + 1][c] : baseGrid[r][c];
                double left  = (c > 0)            ? baseGrid[r][c - 1] : baseGrid[r][c];
                double right = (c < cols - 1)     ? baseGrid[r][c + 1] : baseGrid[r][c];

                surroundingSumGrid[r][c] = up + down + left + right;
            }
        }
    }

    /**
     * Divide each sum by 4.0 to get the surrounding average.
     */
    private void computeSurroundingAvgGrid() {
        int rows = baseGrid.length;
        int cols = baseGrid[0].length;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                surroundingAvgGrid[r][c] = surroundingSumGrid[r][c] / 4.0;
            }
        }
    }

    /**
     * The delta is 50% (half) of the surrounding average.
     */
    private void computeDeltaGrid() {
        int rows = baseGrid.length;
        int cols = baseGrid[0].length;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                deltaGrid[r][c] = surroundingAvgGrid[r][c] / 2.0;
            }
        }
    }

    /**
     * A cell is in danger if its value is less than (avg - delta)
     * or greater than (avg + delta).
     */
    private void computeDangerGrid() {
        int rows = baseGrid.length;
        int cols = baseGrid[0].length;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double val = baseGrid[r][c];
                double avg = surroundingAvgGrid[r][c];
                double delta = deltaGrid[r][c];

                // True if val is outside the safe range [avg - delta, avg + delta]
                dangerGrid[r][c] = (val < avg - delta || val > avg + delta);
            }
        }
    }

    /* ----------------------------------------------------------------------
       Utility methods for copying arrays and for formatting them as Strings
       in the toString() method.
       ---------------------------------------------------------------------- */

    /**
     * Return a deep copy of the given 2D array.
     */
    private double[][] copy2DArray(double[][] source) {
        double[][] copy = new double[source.length][source[0].length];
        for (int r = 0; r < source.length; r++) {
            System.arraycopy(source[r], 0, copy[r], 0, source[r].length);
        }
        return copy;
    }

    /**
     * Format double[][] arrays for clean printing.
     */
    private String format2DArray(double[][] arr) {
        StringBuilder sb = new StringBuilder();
        for (double[] row : arr) {
            for (double val : row) {
                sb.append(String.format("%8.3f ", val));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Format boolean[][] arrays for clean printing.
     */
    private String format2DBooleanArray(boolean[][] arr) {
        StringBuilder sb = new StringBuilder();
        for (boolean[] row : arr) {
            for (boolean val : row) {
                sb.append(val).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
