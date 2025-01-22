import java.io.*; 
import java.util.Scanner; 

public class GridMonitor implements GridMonitorInterface {

    // Creating all of my grids that I will calculate based on my various text files
    private double[][] baseGrid; 
    private double[][] surroundingSumGrids; 
    private double[][] surroundingAvgGrid;
    private double[][] deltaGrid;
    private boolean[][] dangerGrid; 

    public class GridReader {
        public static void main(String[] args) {
                String fileName = "sample.txt"; // FilePath to the text file

                try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                    // Reads the first line to get the grid Dimensions
                    String[] dimensions = reader.readLine().split(" "); // Puts the first line of the read text file into a array of strings whose values are split by a space. 
                    int rows = Integer.parseInt(dimensions[0]); // This parses the first value in the text file and lables it with the number of nows that I want 
                    int cols = Integer.parseInt(dimensions[1]); // This parses the second value in the first line of the text file and tells me how many columns I want

                    // Creates the baseGrid grid without any of the values in it yet
                    double[][] baseGrid = new double[rows][cols]; 

                    // Reads the rest of the text file and then puts the values into the baseGrid 
                    for (int i = 0; i < rows; i++) {
                        String[] rowData = reader.readLine().split(" "); // Reads the data for the first row fo the text file 
                        for (int j  = 0; j < cols; j++) {
                            baseGrid[i][j] = Double.parseDouble(rowData[j]); 
                        }
                    }

                    // Display the grid to make sure that the data is being parsed correctly 
                    System.out.println("Sample.txt Grid: ");
                    for (double[] row : baseGrid) {
                        for (double rowData : row) {
                            System.out.print(rowData + " "); 
                        }
                        System.out.println();
                    }
                } catch (IOException e) {
                    System.out.println("Error reading this file: " + fileName);
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing the the values in the file: " + fileName);
                }
            }
        }

    public double[][] getBaseGrid() {
        return baseGrid;
    }

    public double[][] getSurroundingSumGrid() {
        return surroundingSumGrids;
    }

    public double[][] getSurroundingAvgGrid() {
        return surroundingAvgGrid;
    }   

    public double[][] getDeltaGrid() {
        return deltaGrid;
    }

    public boolean[][] getDangerGrid() {
        return dangerGrid;
    }

    public String toString() {
        return "Base Grid: " + baseGrid + "\n" + "Surrounding Sum Grid: " + surroundingSumGrids + "\n" + "Surrounding Average Grid: " + surroundingAvgGrid + "\n" + "Delta Grid: " + deltaGrid + "\n" + "Danger Grid: " + dangerGrid; 
    }
}