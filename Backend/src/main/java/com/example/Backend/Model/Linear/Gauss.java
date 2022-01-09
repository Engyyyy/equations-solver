package com.example.Backend.Model.Linear;

import java.math.BigDecimal;
import java.math.MathContext;
import java.lang.*;

public class Gauss {
    public static int flag;  //to specify if partial pivoting will be applied or not
    public static long totalTime = 0;
    public static long funTime = 0;

    //function for precision
    static double precision(double value, int prec) {
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

    //function to print the matrix
    public static void print_matrix(double[][] arr, int rows, int cols) {
        long startTime = System.nanoTime();
        for(int r=0; r<rows; r++) {
            for(int c=0; c<cols; c++) {
                System.out.print(arr[r][c]);
                System.out.print("\t");
            }
            System.out.println("");
        }
        System.out.println("");
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
    }//end of function

    //function to interchange to rows
    static void interchange(double[][] arr, int cols, int row1, int row2) {
        long startTime = System.nanoTime();
        double temp;
        for(int i=0; i<cols; i++) {
            temp = arr[row1][i];
            arr[row1][i] = arr[row2][i];
            arr[row2][i] = temp;
        }
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
    }//end of function

    //function for scaling
    static void scaling(double[][] arr, int s, int rows, int cols) {
        long startTime = System.nanoTime();
        flag = 0;
        double[][] scaled_arr = new double[rows][cols];
        //copy all elements in arr to scaled_arr
        for(int i=0; i<rows; i++) {
            for(int j=0; j<cols; j++) {
                scaled_arr[i][j] = arr[i][j];
            }
        }
        //find the maximum value in the row of cooficient matrix
        for(int j=s; j<rows; j++) {
            double max = Math.abs(arr[j][s]);
            for(int i=s+1; i+1<cols; i++) {
                if(max < Math.abs(arr[j][i])) {
                    max = Math.abs(arr[j][i]);
                }
            }
            for(int a=0; a<cols; a++) {
                if(max != 0) {
                    scaled_arr[j][a] = arr[j][a] / max;
                }
            }
        }//end of scaling the whole array
        //check if pivoting will be applied on the first column
        for(int i=s+1; i<rows; i++) {
            if(scaled_arr[s][s] < scaled_arr[i][s]) {
                flag = 1;
                break;
            }
        }
        System.out.println("the matrix after applying SCALING:");
        print_matrix(scaled_arr, rows, cols);
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
    }//end of function

    //function for partial pivoting
    static void pivoting(double[][] arr, int rows, int cols, int s) {
        long startTime = System.nanoTime();
        for(int j=s+1; j<rows; j++) {
            if(Math.abs(arr[s][s]) < Math.abs(arr[j][s])) {
                interchange(arr, cols, s, j);
            }
        }
        System.out.println("the new matrix after PIVOTING: ");
        print_matrix(arr, rows, cols);
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
    }//end of function
}