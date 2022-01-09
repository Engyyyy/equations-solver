package com.example.Backend.Model.Linear;

import com.example.Backend.Model.Linear.GaussElimination;

public class GaussJordan extends GaussElimination {

    //function for backward elimination
    static void Belimination(double[][] arr, double[][] m, int rows, int cols, int s, int prec) {
        long startTime = System.nanoTime();
        for(int i=s; i-1>-1 && arr[s][s]!=0; i--) {
            m[i-1][s] = precision(arr[i-1][s], prec) / precision(arr[s][s], prec);
            m[i-1][s] = precision(m[i-1][s], prec);
            System.out.print("m");System.out.print(i);System.out.print(s+1);System.out.println("= "+ m[i-1][s]);
            System.out.println("the new matrix: ");
            for(int j=cols-2; j>-1; j--) {
                arr[i-1][j+1] = -1 * precision(m[i-1][s], prec) * precision(arr[s][j+1], prec) + precision(arr[i-1][j+1], prec);
                arr[i-1][j+1] = precision(arr[i-1][j+1], prec);
            }
            print_matrix(arr, rows, cols);
        }//end of for
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
    }//end of function

    //function to calculate the matrix of each step
    public static void jordan_steps(double[][] arr, int rows, int cols, int prec) {
        long startTime = System.nanoTime();
        double[][] m = new double[rows][cols];
        for(int s=cols-2; s>0; s--) {
            Belimination(arr, m, rows, cols, s, prec);
        }//end of for
        //scaling the diagonal
        for(int i=0; i<rows; i++) {
            for(int j=0; j<cols; j++) {
                if(i==j) {
                    double temp = arr[i][j];
                    for(int s=j; s<cols && temp!=0; s++) {
                        arr[i][s] = precision(arr[i][s], prec) / precision(temp, prec);
                        arr[i][s] = precision(arr[i][s], prec);
                    }
                }
            }
        }
        System.out.println("the final matrix:");
        print_matrix(arr, rows, cols);
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
    }//end of function

    public static void main(String[] args) {
        double [][] arr = {
                { 1, 1, 2  , 8},
                {-1,-2, 3  , 1},
                { 3, 7, 4  ,10}
        };
        int rows = 3;
        int cols = 4;
        int prec = 3;
        gauss_steps(arr, rows, cols, prec);
        jordan_steps(arr, rows, cols, prec);
        result_matrix(arr, rows, cols, prec);

    }//end of main
}