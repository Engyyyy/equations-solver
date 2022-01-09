package com.example.Backend.Model.Linear;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
public class GuassSeidel2 {
    public boolean flag = false;
    public static long totalTime = 0;
    public static long funTime = 0;

    // function to print the augmented matrix
    public void print(double[][] mat) {
        long startTime = System.nanoTime();
        int n = mat.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n + 1; j++)
                System.out.print(mat[i][j] + " ");
            System.out.println();
        }
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
    }

    public void printm(double[] mat) {
        long startTime = System.nanoTime();
        int n = mat.length;
        System.out.print("{");
        for (int i = 0; i < n; i++) {
            System.out.print(mat[i] + " ");
        }
        System.out.println("}");
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
    }

    //function for precision
    public double precision(double value, int prec) {
        long startTime = System.nanoTime();
        double rounded;
        BigDecimal bd = new BigDecimal(value);
        bd = bd.round(new MathContext(prec));
        rounded = bd.doubleValue();
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return rounded;
    }//end of function

    // function to transform a matrix ,if possible,to a diagonally dominant
    public boolean TransformToDominant(double[][] mat, int r, boolean[] checked, int[] rows) {
        long startTime = System.nanoTime();
        int n = mat.length;
        if (r == mat.length) {
            double[][] T = new double[n][n + 1];
            for (int i = 0; i < rows.length; i++) {
                for (int j = 0; j < n + 1; j++)
                    T[i][j] = mat[rows[i]][j];
            }
            mat = T;
            return true;
        }
        for (int i = 0; i < n; i++) {
            if (checked[i])
                continue;
            double sum = 0;
            for (int j = 0; j < n; j++)
                sum += Math.abs(mat[i][j]);
            if (2 * Math.abs(mat[i][r]) >= sum) {
                // diagonally dominant?
                checked[i] = true;
                rows[r] = i;
                if (2 * Math.abs(mat[i][r]) > sum) {
                    flag = true;
                }
                if (TransformToDominant(mat, r + 1, checked, rows))
                    return (true && flag);
                checked[i] = false;
            }
        }
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return false;
    }

    // function to check if the matrix is diagonally dominant or not
    public boolean checkDominant(double[][] mat) {
        long startTime = System.nanoTime();
        boolean[] checked = new boolean[mat.length];
        int[] rows = new int[mat.length];
        Arrays.fill(checked, false);
        boolean check = TransformToDominant(mat, 0, checked, rows);
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return check;
    }

    // function to solve the system of equations by GaussSeidel method
    public double[] GuassSolution(double[][] mat, int numOfIterations, double relativeError, double[] iniGuess, int precision) {
        long startTime = System.nanoTime();
        int iterations = 0;
        int n = mat.length;
        double[] X = new double[n]; // Approximations
        double[] P = new double[n]; // Prev
        X = (double[]) iniGuess.clone();
        P = (double[]) iniGuess.clone();
        checkDominant(mat);
        if (!checkDominant(mat)) {
            System.out.println(
                    "The system isn't diagonally dominant: "
                            + "The method cannot guarantee convergence.");
        }
        while (true) {
            for (int i = 0; i < n; i++) {
                double sum = mat[i][n]; // b_n
                for (int j = 0; j < n; j++)
                    if (j != i) {
                        sum -= mat[i][j] * X[j];
                        double summ = precision(sum, precision);
                        sum = summ;
                    }
                // Update each xi to use in the row calculation
                double ans = 1 / mat[i][i] * sum;
                X[i] = precision(ans, precision);
            }
            System.out.print("X" + iterations + " = {");
            for (int i = 0; i < n; i++) {
                System.out.print(X[i] + " ");
            }
            System.out.println("}");
            iterations++;
            boolean stop = true;
            for (int i = 0; i < n && stop; i++)
                if ((Math.abs(X[i] - P[i]) * 100 / X[i]) > relativeError)
                    stop = false;
            if (stop || iterations == numOfIterations)
                break;
            P = (double[]) X.clone();
        }
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return X;
    }
}