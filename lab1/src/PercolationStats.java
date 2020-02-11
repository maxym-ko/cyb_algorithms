import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double COEFFICIENT = 1.96;
    private final double[] testResult;
    private final int testsNumber;
    private final int gridHeight;
    private final int gridSize;
    private double sampleMean;
    private double stdDeviation;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("n and trial need to be positive");

        sampleMean = -1;
        stdDeviation = -1;

        gridHeight = n;
        gridSize = n * n;
        testsNumber = trials;
        testResult = new double[testsNumber];
        for (int i = 0; i < testsNumber; i++) {
            testResult[i] = doTest(new Percolation(n));
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        sampleMean = StdStats.mean((testResult));
        return sampleMean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        stdDeviation = StdStats.stddev(testResult);
        return stdDeviation;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double x = sampleMean == -1 ? mean() : sampleMean;
        double s = stdDeviation == -1 ? stddev() : stdDeviation;
        return x - (COEFFICIENT * s / Math.sqrt(testsNumber));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double x = sampleMean == -1 ? mean() : sampleMean;
        double s = stdDeviation == -1 ? stddev() : stdDeviation;
        return x + (COEFFICIENT * s / Math.sqrt(testsNumber));
    }

    // to do a test
    private double doTest(Percolation p) {
        int row;
        int col;
        while (!p.percolates()) {
            do {
                row = StdRandom.uniform(gridHeight) + 1;
                col = StdRandom.uniform(gridHeight) + 1;
            } while (p.isOpen(row, col));
            p.open(row, col);
        }
        return p.numberOfOpenSites() * 1. / gridSize;
    }

    // test client (see below)
    public static void main(String[] args) {
//        PercolationStats percolationStats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        PercolationStats percolationStats = new PercolationStats(20, 10);
        double mean = percolationStats.mean();
        double stddev = percolationStats.stddev();
        double lo = percolationStats.confidenceLo();
        double hi = percolationStats.confidenceHi();
        System.out.println("mean                    = " + mean + "\nstddev                  = " + stddev + "\n95% confidence interval = [" + lo + ", " + hi + "]");
    }
}
