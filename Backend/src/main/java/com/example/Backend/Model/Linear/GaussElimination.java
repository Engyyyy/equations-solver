package com.example.Backend.Model.Linear;

import com.example.Backend.Model.Linear.Gauss;

public class GaussElimination extends Gauss {

    //function for forward elimination
    static void Felimination(double[][] arr, double[][] m, int rows, int cols, int s, int prec) {
        long startTime = System.nanoTime();
        for(int i=s; i+1<rows && arr[s][s]!=0; i++) {
            m[i+1][s] = precision(arr[i+1][s], prec) / precision(arr[s][s], prec);
            m[i+1][s] = precision(m[i+1][s], prec);
            System.out.print("m");System.out.print(i+2);System.out.print(s+1);System.out.println("= "+ m[i+1][s]);
            System.out.println("the new matrix: ");
            for(int j=0; j<cols; j++) {
                arr[i+1][j] = -1 * precision(m[i+1][s], prec) * precision(arr[s][j], prec) + precision(arr[i+1][j], prec);
                arr[i+1][j] = precision(arr[i+1][j], prec);
            }
            print_matrix(arr, rows, cols);
        }//end of for
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
    }//end of function

    //function to calculate the matrix of each step
    public static void gauss_steps(double[][] arr, int rows, int cols, int prec) {
        long startTime = System.nanoTime();
        double[][] m = new double[rows][cols];
        System.out.println("the given matrix: ");
        print_matrix(arr, rows, cols);
        for(int s=0; s+2<cols; s++) {
            scaling(arr, s, rows, cols);
            if(flag==1) {
                pivoting(arr, rows, cols, s);
            }
            else {
                System.out.println("NO need for PIVOTING.");
            }
            Felimination(arr, m, rows, cols, s, prec);
        }//end of for
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
    }//end of function

    //function to calculate the resulting matrix
    public static double[][] result_matrix(double[][] arr, int rows, int cols, int prec) {
        //static void result_matrix(double[][] arr, int rows, int cols, int prec) {
        long startTime = System.nanoTime();
        double [][] res = new double[rows][1];
        double var;
        for(int i=0; i<rows; i++) {
            var = precision(arr[rows-1-i][cols-1], prec);
            for(int j=0; j<i; j++) {
                var = var- precision(arr[rows-1-i][cols-2-j], prec) * precision(res[cols-2-j][0], prec);
                var = precision(var, prec);
            }
            if(arr[rows-1-i][rows-1-i] != 0) {
                res[rows-1-i][0] = precision(var, prec) / precision(arr[rows-1-i][rows-1-i], prec);
                res[rows-1-i][0] = precision(res[rows-1-i][0], prec);
            }
        }
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return res;
	    /*System.out.println("The resulting matrix:");
		print_matrix(res, rows, 1);
		long endTime = System.nanoTime();
		funTime = endTime - startTime;
		totalTime = totalTime + funTime;*/
    }

    public static void main(String[] args) {
        double [][] arr = {
                { 1, 1,-1  , 2},
                { 2, 0,-6  , 8},
                { 3, 0,-3  ,10}
        };
        int rows = 3;
        int cols = 4;
        int prec = 5;
        gauss_steps(arr, rows, cols, prec);
        result_matrix(arr, rows, cols, prec);
        System.out.print("Run Time = " + totalTime);
    }//end of main
}//end of class