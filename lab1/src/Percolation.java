import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid;
    private final int gridHeight;
    private final int gridSize;
    private int openSites;
    private final WeightedQuickUnionUF uf;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n need to be positive");

        gridHeight = n;
        grid = new boolean[gridHeight][gridHeight];
        gridSize = gridHeight * gridHeight;
        openSites = 0;
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridHeight; j++) {
                grid[i][j] = false;
            }
        }

        // n - virtual top site
        // n + 1 - virtual bottom site
        uf = new WeightedQuickUnionUF(gridSize + 2);
        for (int i = 0; i < gridHeight; i++) {
            uf.union(gridSize, i);
        }
        for (int i = toIndex(gridHeight, 1); i < gridSize; i++) {
            uf.union(gridSize + 1, i);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!siteExist(row, col)) throw new IllegalArgumentException("site (" + row + ", " + col + ") is out of a grid");

        if (!isOpen(row, col)) {
            grid[row - 1][col - 1] = true;
            openSites++;

            int currentSiteIndex = toIndex(row, col);
            int topSiteIndex = toIndex(row - 1, col);
            int bottomSiteIndex = toIndex(row + 1, col);
            int leftSiteIndex = toIndex(row, col - 1);
            int rightSiteIndex = toIndex(row, col + 1);

            if (siteExist(row - 1, col) && isOpen(row - 1, col)) {
                uf.union(currentSiteIndex, topSiteIndex);
            }
            if (siteExist(row + 1, col) && isOpen(row + 1, col)) {
                uf.union(currentSiteIndex, bottomSiteIndex);
            }
            if (siteExist(row, col - 1) && isOpen(row, col - 1)) {
                uf.union(currentSiteIndex, leftSiteIndex);
            }
            if (siteExist(row, col + 1) && isOpen(row, col + 1)) {
                uf.union(currentSiteIndex, rightSiteIndex);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!siteExist(row, col)) throw new IllegalArgumentException("site (" + row + ", " + col + ") is out of a grid");
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!siteExist(row, col)) throw new IllegalArgumentException("site (" + row + ", " + col + ") is out of a grid");
        return uf.find(gridSize) == uf.find(toIndex(row, col)) && isOpen(row, col);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(gridSize) == uf.find(gridSize + 1);
    }

    // check if site(row, col) is out of grid
    private boolean siteExist(int row, int col) {
        return row > 0 && row <= gridHeight && col > 0 && col <= gridHeight;
    }

    // transform row and col to index
    private int toIndex(int row, int col) {
        return gridHeight * (row - 1) + col - 1;
    }

    // test client (optional)
    public static void main(String[] args) {
        // example from website
        Percolation p = new Percolation(8);
        p.open(1, 3);
        p.open(1, 4);
        p.open(1, 5);
        p.open(2, 4);
        p.open(2, 5);
        p.open(2, 6);
        p.open(2, 7);
        p.open(2, 8);
        p.open(3, 6);
        p.open(3, 7);
        p.open(4, 6);
        p.open(4, 7);
        p.open(4, 8);
        p.open(5, 6);
        p.open(5, 7);
        p.open(6, 7);
        p.open(6, 8);
        p.open(7, 5);
        p.open(7, 6);
        p.open(7, 7);
        p.open(7, 8);
        p.open(8, 6);

        System.out.println(p.percolates());

        Percolation p2 = new Percolation(3);
        p2.open(1,3);
        p2.open(2,3);
        p2.open(3,3);
        p2.open(3,1);
        p2.open(2,1);
        p2.open(1,1);
        System.out.println(p2.isFull(3, 1));
    }
}
