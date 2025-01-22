public class GridMonitor implements GridMonitorInterface {
    private double[][] baseGrid;
    private double[][] sumGrid;
    private double[][] avgGrid;
    private double[][] deltaGrid;
    private boolean[][] dangerGrid;
    
    /**
     * Constructs a GridMonitor object given a 2D array (base grid).
     * Performs all the necessary computations (sum, average, delta, danger).
     */
    public GridMonitor(double[][] baseGrid) {
        // Store a deep copy of the base grid to protect from external modification.
        this.baseGrid = copy2DArray(baseGrid);
        
        // Initialize all arrays.
        int rows = baseGrid.length;
        int cols = baseGrid[0].length;
        
        sumGrid = new double[rows][cols];
        avgGrid = new double[rows][cols];
        deltaGrid = new double[rows][cols];
        dangerGrid = new boolean[rows][cols];
        
        // Compute the derived grids.
        computeSurroundingSums();
        computeSurroundingAvgs();
        computeDeltas();
        computeDanger();
    }
    
    /**
     * Returns the original base grid of values.
     */
    @Override
    public double[][] getBaseGrid() {
        return copy2DArray(baseGrid);
    }
    
    /**
     * Returns the grid of sums of each cell's 4 neighbors.
     */
    @Override
    public double[][] getSurroundingSumGrid() {
        return copy2DArray(sumGrid);
    }
    
    /**
     * Returns a grid of the averages of the 4 neighboring cells.
     */
    @Override
    public double[][] getSurroundingAvgGrid() {
        return copy2DArray(avgGrid);
    }
    
    /**
     * Returns a grid of the deltas, which is half the surrounding average.
     */
    @Override
    public double[][] getDeltaGrid() {
        return copy2DArray(deltaGrid);
    }
    
    /**
     * Returns a grid of booleans indicating if a cell is in danger of exploding.
     */
    @Override
    public boolean[][] getDangerGrid() {
        // Return a copy if you want to avoid external modification
        boolean[][] copy = new boolean[dangerGrid.length][dangerGrid[0].length];
        for (int i = 0; i < dangerGrid.length; i++) {
            System.arraycopy(dangerGrid[i], 0, copy[i], 0, dangerGrid[i].length);
        }
        return copy;
    }
    
    /**
     * Helper method to compute the 4-neighbor sums for each cell.
     */
    private void computeSurroundingSums() {
        int rows = baseGrid.length;
        int cols = baseGrid[0].length;
        
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // For border cells, use the cell's own value
                // if a neighbor is "missing".
                
                double up    = (r > 0) ? baseGrid[r - 1][c] : baseGrid[r][c];
                double down  = (r < rows - 1) ? baseGrid[r + 1][c] : baseGrid[r][c];
                double left  = (c > 0) ? baseGrid[r][c - 1] : baseGrid[r][c];
                double right = (c < cols - 1) ? baseGrid[r][c + 1] : baseGrid[r][c];
                
                sumGrid[r][c] = up + down + left + right;
            }
        }
    }
    
    /**
     * Helper method to compute the average for each cell (sum / 4).
     */
    private void computeSurroundingAvgs() {
        int rows = sumGrid.length;
        int cols = sumGrid[0].length;
        
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                avgGrid[r][c] = sumGrid[r][c] / 4.0;
            }
        }
    }
    
    /**
     * Helper method to compute the delta for each cell, which is half the average.
     */
    private void computeDeltas() {
        int rows = avgGrid.length;
        int cols = avgGrid[0].length;
        
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                deltaGrid[r][c] = avgGrid[r][c] / 2.0;
            }
        }
    }
    
    /**
     * Helper method to compute the danger boolean for each cell.
     * A cell is considered in danger if it differs from the surrounding average
     * by more than 50% (i.e., more than its delta).
     */
    private void computeDanger() {
        int rows = baseGrid.length;
        int cols = baseGrid[0].length;
        
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double val = baseGrid[r][c];
                double avg = avgGrid[r][c];
                double delta = deltaGrid[r][c];
                
                // If the cell's value is outside the safe range: (avg - delta) to (avg + delta)
                // then it is in danger.
                if (val < (avg - delta) || val > (avg + delta)) {
                    dangerGrid[r][c] = true;
                } else {
                    dangerGrid[r][c] = false;
                }
            }
        }
    }
    
    /**
     * Utility method to make a deep copy of a 2D array of doubles.
     */
    private double[][] copy2DArray(double[][] source) {
        double[][] copy = new double[source.length][source[0].length];
        for (int i = 0; i < source.length; i++) {
            System.arraycopy(source[i], 0, copy[i], 0, source[i].length);
        }
        return copy;
    }
}
