import java.io.*; 
import java.util.*; 

public class GridMonitor implements GridMonitorInterface {
    private double[][] baseGrid; // 2D array of doubles
    private double[][] surroundingSumGrid; // 2D array of doubles of the sum of the surrounding cells
    private double[][] surroundingAvgGrid; // 2D array of doubles of the sum of the surrounding cells with each cell being divided by 4
    private double[][] deltaGrid; // 2D array of doubles of the 50 range of the average of the surrounding cells
    private boolean[][] dangerGrid; // 2D boolean array of whether a cell is 50% or higher out of the range of the surroundingaverage grid compared to the base grid

    public GridMonitor(String testFile) throws IOException {
        List<double[]> tempGrid = new ArrayList<>(); // Creates an array list of double arrays called tempGrid to store the unknown size and values of the grid 
        try (BufferedReader br = new BufferedReader(new FileReader(testFile))) { // Trys to read the file and throws and IO excpetion if it can't read or find it 
            String[] dimensions = br.readLine().split(" "); // Reads the demensions of the array and stores it in dimensions
            int rows = Integer.parseInt(dimensions[0]);
            int cols = Integer.parseInt(dimensions[1]);

            for (int i = 0; i < rows; i++) { // Loops through the rows of the grid
                String[] values = br.readLine().split(" ");
                double[] row = new double[cols];
                for (int j = 0; j < cols; j++) { // Loops through the columns of the grid
                    row[j] = Double.parseDouble(values[j]); // Converts the string values to double values and stores them in the row array
                }
                tempGrid.add(row); // Adds the rows to the tempGrid array list of double arrays
                baseGrid = tempGrid.toArray(new double[rows][cols]); // Stores my newly filled tempGrid array List into the baseGrid 2D array of doubles
            }
        }
        int rows = baseGrid.length; // Gets the length of the grid we will be testing
        int cols = baseGrid[0].length; // Gets the length of the columns of the grid we will be testing
        surroundingSumGrid = new double[rows][cols];
        surroundingAvgGrid = new double[rows][cols];
        deltaGrid = new double[rows][cols];
        dangerGrid = new boolean[rows][cols];
    }
    
}

public static void main(System[] args) {
    GridMonitor test = new GridMonitor("sample.txt");
    System.out.println(test.baseGrid);
    System.out.println(test.surroundingSumGrid);
    System.out.println(test.surroundingAvgGrid);
    System.out.println(test.deltaGrid);
    System.out.println(test.dangerGrid);
}
