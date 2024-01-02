import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdArrayIO;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    boolean[][] grid; // 2D array representing the grid. Each (row, col) is a site.
    // (row, col) is true if the site is open, false if the site is closed.
    int gridsize; // The size of the grid.
    int gridSquared; // The number of sites in a grid.
    WeightedQuickUnionUF wquFind; // Weighted quick union-find object used to
    // keep track of all connected/opened sites.
    int virtualTop; // Index of a virtual top in the size and parent arrays
    // in WeightedQuickUnionFind. Connect the virtual top to every open site in the first row of the grid.
    int virtualBottom; // Index of a virtual bottom in the size and parent arrays in WeightedQuickUnionFind.

    // Initialize
    public Percolation(int n) {
        gridsize = n;
        gridSquared = n * n;

        grid = new boolean[n][n];
        wquFind = new WeightedQuickUnionUF(gridSquared + 2); // +2 for virtual top and bottom
        virtualTop = 0; // Virtual top index
        virtualBottom = gridSquared + 1; // Virtual bottom index

        // Set all sites to closed initially
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = false;
            }
        }
    }

    /**
     * Opens a site at (row, col). If the site is already open, do nothing.
     *
     * @param row
     * @param col
     */
    public void openSite(int row, int col) {
        if (!grid[row][col]) {
            grid[row][col] = true;

            // Connect to virtual top if in the first row
            if (row == 0) {
                wquFind.union(getIndex(row, col), virtualTop);
            }
            // Connect to virtual bottom if in the last row
            if (row == gridsize - 1) {
                wquFind.union(getIndex(row, col), virtualBottom);
            }
            // Connect to adjacent open sites
            connectToAdjacent(row, col);
        }
    }

    /**
     * Opens all sites in the grid. Starting at the first site at index (0,0) and
     * moving row-wise through all the sites, each site is opened with probability p.
     */
    public void openAllSites() {
        for (int i = 0; i < gridsize; i++) {
            for (int j = 0; j < gridsize; j++) {
                openSite(i, j);
            }
        }
    }

    /**
     * Returns true if the system percolates.
     *
     * @return
     */
    public boolean percolationCheck() {
        return wquFind.connected(virtualTop, virtualBottom);
    }

    /**
     * Displays the grid. An open site is colored blue, a closed site is colored
     * black.
     */
    public void displayGrid() {
    	 // Set the drawing scale for the x and y axes
        StdDraw.setXscale(0, gridsize);
        StdDraw.setYscale(0, gridsize);
        // Iterate through each row in the grid
        for (int i = 0; i < gridsize; i++) {
            for (int j = 0; j < gridsize; j++) {
            	// Set the pen color based on whether the site is open or closed
                if (grid[i][j]) {
                    StdDraw.setPenColor(StdDraw.BLUE);// Blue for open sites
                } else {
                    StdDraw.setPenColor(StdDraw.BLACK);// Black for closed sites
                }

                // Draw a filled square at the center of the current grid cell
                // Note: Adjusting the coordinates and size to center and fit the square in the cell
                StdDraw.filledSquare(j + 0.5, gridsize - i - 0.5, 0.5);
            }
        }
    }

    /**
     * Connects the opened site to its adjacent open sites.
     *
     * @param row
     * @param col
     */
    private void connectToAdjacent(int row, int col) {
    	// Define possible directions to adjacent sites: up, down, left, right
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        // Iterate through each direction
        for (int[] dir : directions) {
         // Calculate the new row and column indices based on the direction
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            // Check if the new position is within the grid and if the site is open
            if (isValid(newRow, newCol) && isOpen(newRow, newCol)
                    && !wquFind.connected(getIndex(row, col), getIndex(newRow, newCol))) {
            	   // Connect the opened site to the adjacent open site
                wquFind.union(getIndex(row, col), getIndex(newRow, newCol));
            }
        }
    }

    /**
     * Checks if the site at (row, col) is open.
     *
     * @param row
     * @param col
     * @return
     */
    private boolean isOpen(int row, int col) {
        return grid[row][col];
    }

/**
 * Checks if (row, col) is a valid site in the grid.
 * @param row
 * @param col
 * @return
 */
private boolean isValid(int row, int col) {
	  
    return row >= 0 && row < gridsize && col >= 0 && col < gridsize;
}

/**
 * Converts 2D coordinates to 1D index.
 * @param row
 * @param col
 * @return
 */
private int getIndex(int row, int col) {
    return row * gridsize + col;
}
private static void pause(int msec) {
	try {
		Thread.sleep(msec);
	}
	catch(InterruptedException e){
		System.err.println("Interrupted: " + e.getMessage());
		
	}
	
}


	public static void main(String[] args) {
		double siteOpenProbability = 0.593;//percolation probability 

		  for (int i = 0; i < 10; i++) {
		        int gridSize = StdRandom.uniform(5, 15); // Random grid size between 5 and 15

		        Percolation percolation = new Percolation(gridSize);

		        System.out.println("Percolation Problem " + (i + 1));
		        System.out.println("Grid Size: " + gridSize);

		        // Randomly open sites until the system may or may not percolate
		        for (int row = 0; row < gridSize; row++) {
		            for (int col = 0; col < gridSize; col++) {
		                if (StdRandom.uniform() < siteOpenProbability) { // Adjust the probability here 
		                    percolation.openSite(row, col);
		                }
		            }
		        }

		        percolation.displayGrid();
		        System.out.println("Percolates: " + percolation.percolationCheck());
		        System.out.println();
		        Percolation.pause(3000);
		    }
}
}

