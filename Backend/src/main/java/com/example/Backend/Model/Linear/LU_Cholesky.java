package com.example.Backend.Model.Linear;

import com.example.Backend.Model.Linear.Gauss;

import java.util.Scanner;

public class LU_Cholesky extends Gauss {
    //do matrix of coeff
    public static double[][] Amatrix(double[][] Aug, int r , int c){
        long startTime = System.nanoTime();
        double [][]mat = new double[r][r];
        for(int i=0; i<r; i++)
            for(int j=0; j<r; j++)
                mat[i][j] = Aug[i][j];
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return mat;
    }
    //matrix B
    public static double[][] Bmatrix(double[][] Aug, int r , int c){
        long startTime = System.nanoTime();
        double [][]mat = new double[r][1];
        for(int i=0; i<r; i++)
            mat[i][0] = Aug[i][r];
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return mat;
    }
    // check symmetric

    //generate L
    public static double[][] Do_L(double[][] A, int prec) {
        long startTime = System.nanoTime();
        int N  = A.length;
        double[][] L = new double[N][N];

        for (int i = 0; i < N; i++)  {
            for (int j = 0; j <= i; j++) {
                double sum = 0.0;
                for (int k = 0; k < j; k++) {
                    sum += L[i][k] * L[j][k];
                }
                if (i == j) L[i][i] = precision(Math.sqrt(A[i][i] - sum),prec);
                else        L[i][j] = precision(1.0 / L[j][j] * (A[i][j] - sum),prec);
            }
            if (L[i][i] <= 0) {
                throw new RuntimeException("Matrix not positive definite");
            }
        }
        print_matrix(L, N, N);
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return L;
    }

    //generate L_Transpose
    public static double[][] Do_L_Trans( double[][]Lmat,  int n) {
        long startTime = System.nanoTime();
        double[][] Umat = new double [n][n];
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                if (i <= j) {
                    Umat[i][j] = Lmat[j][i];
                }
                else {
                    Umat[i][j] = 0.0;
                }
            }
        }
        print_matrix(Umat, n, n);
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return Umat;
    }
    //forward substitution to LY=B
    public static double[][]forwardSub (double[][]mat ,double[][] B,  int n) {
        long startTime = System.nanoTime();
        int m=0,i,j;
        double[][] Y = new double [n][1];
        double[][]NewAug=new double[n][n+1];
        for( i=0;i<n;i++) {
            for(j=0; j<n ; j++) {
                NewAug[i][j]=mat[i][j];
            }
        }
        for(i=0;i<n;i++)  {
            NewAug[i][n]=B[i][0];
        }
        double[] L=new double[1000] ;
        for(int k=0; k<n; k++) {
            for (i=k; i<n-1; i++) {
                L[m]=NewAug[i+1][k]/ NewAug[k][k];
                for( j=0; j<n+1; j++) {
                    NewAug[i+1][j]= (-1*L[m]*NewAug[k][j])+NewAug[i+1][j];
                }
                m++;
            }
        }
        for(int r=0;r<n;r++) {
            for(int c=0 ; c<n;c++) {
                if(c==r) {
                    Y[r][0]=NewAug[r][n]/NewAug[r][c];
                }
            }
        }
        System.out.println("Y matrix:");
        print_matrix(Y,n,1);
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return Y;
    }
    //backward substitution for AX=B
    public static double[][] backwordSub (double[][]mat ,double[][] B,  int n) {
        long startTime = System.nanoTime();
        int m=0,i,j;
        double[][] X = new double [n][1];
        double[][]NewAug=new double[n][n+1];
        for(i=0;i<n;i++) {
            for( j=0; j<n ; j++) {
                NewAug[i][j]=mat[i][j];
            }
        }
        for( i=0;i<n;i++)  {
            NewAug[i][n]=B[i][0];
        }
        double[] L=new double[1000] ;
        for(int k=n-1; k>0; k--) {
            for (i=k; i>0; i--) {
                L[m]=NewAug[i-1][k]/ NewAug[k][k];
                for( j=0; j<n+1; j++) {
                    NewAug[i-1][j]= (-1*L[m]*NewAug[k][j])+NewAug[i-1][j];
                }
                m++;
            }
        }

        for(int r=0;r<n;r++) {
            for(int c=0 ; c<n;c++) {
                if(c==r) {
                    X[r][0]=NewAug[r][n]/NewAug[r][c];
                }
            }
        }
        System.out.println("X matrix:");
        print_matrix(X,n,1);
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return X;
    }

    public static void main(String[] args) {
        System.out.println("Enter the dimension of the matrix:");
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        System.out.println("Enter the augmented matrix");
        double [][]matrix = new double[n][n+1];
        double[][] Lmatrix=new double[n][n];
        double[][] Umatrix=new double[n][n];
        for(int i=0; i<n; i++)
            for(int j=0; j<n+1; j++)
                matrix[i][j] = sc.nextDouble();

        double [][]mat = Amatrix(matrix,n,n+1);
        double [][] B = Bmatrix(matrix,n,n+1);
        System.out.println("matrix B:");
        print_matrix(B,n,1);
        System.out.println("matrix of coeff:");
        print_matrix(mat,n,n);

        //print L
        System.out.println("L matrix:");
        Lmatrix=Do_L(mat,3);
        //print U
        System.out.println("");
        System.out.println("U matrix( L traspose ):");
        Umatrix=Do_L_Trans(Lmatrix,n);
        double[][] Y=new double[n][1];
        Y= forwardSub(Lmatrix,B,n);
        backwordSub(Umatrix, Y,n);

    }
}