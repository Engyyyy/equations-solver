package com.example.Backend.Model.Linear;

import com.example.Backend.Model.Linear.Gauss;

import java.util.Scanner;

public class LU_DownLittle extends Gauss {

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
    //apply gauss elimination
    public static double[][] gaussMat(double[][] mat, int n , int prec){
        long startTime = System.nanoTime();
        int j,i,m=0 , count=0;
        double l ;
        double []L = new double[1000];
        for(int k=0; k<n; k++) {
            for (i=k; i<n-1; i++) {
                l= precision(mat[i+1][k],prec)/ precision(mat[k][k] , prec);
                l=precision(l,prec);
                L[m]=l;
                count++;
                for( j=0; j<n; j++) {
                    mat[i+1][j] = precision((-1*L[m]*mat[k][j]),prec)+ precision(mat[i+1][j], prec);
                    mat[i+1][j]= precision(mat[i+1][j],prec);
                }
                m++;
            }
            System.out.println("");
        }
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return mat;
    }
    // array of L
    public static double[] arrayL(double[][] mat, int n, int prec) {
        long startTime = System.nanoTime();
        int j,i,m=0 , count=0;
        double l ;
        double []L = new double[1000];
        for(int k=0; k<n; k++) {
            for (i=k; i<n-1; i++) {
                l= precision(mat[i+1][k],prec)/ precision(mat[k][k] , prec);
                l=precision(l,prec);
                L[m]=l;
                count++;
                System.out.println(l);
                for( j=0; j<n; j++) {
                    mat[i+1][j] = precision((-1*L[m]*mat[k][j]),prec)+ precision(mat[i+1][j], prec);
                    mat[i+1][j]= precision(mat[i+1][j],prec);
                }
                m++;
            }
            print_matrix(mat,n,n);
            System.out.println("");
        }

        for( j=0; j<count; j++)
        {
            System.out.print(L[j] + " ");
        }
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return L;
    }


    //generate L
    public static double[][]Do_L( int n , double []L) {
        long startTime = System.nanoTime();
        double[][] Lmat = new double [n][n];
        int m=0;
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                if (i > j) {
                    Lmat[i][j] = L[m];
                    m++;
                } else if (i == j) {
                    Lmat[i][j] = 1.0;
                } else {
                    Lmat[i][j] = 0.0;
                }
            }
        }
        print_matrix(Lmat, n, n);
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return Lmat;
    }
    //generate U
    public static double [][] Do_U(double[][] mat, int n) {
        long startTime = System.nanoTime();
        double[][] Umat = new double [n][n];
        for ( int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i <= j) {
                    Umat[i][j] = mat[i][j];
                } else {
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

    public static double[][]forwardSub (double[][]mat ,double[][] B,  int n , int prec) {
        long startTime = System.nanoTime();
        int m=0,i,j;
        double[][] Y = new double [n][1];
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
        for(int k=0; k<n; k++) {
            for (i=k; i<n-1; i++) {
                L[m]=NewAug[i+1][k]/ NewAug[k][k];
                for( j=0; j<n+1; j++) {
                    NewAug[i+1][j]= -1*L[m]*NewAug[k][j]+NewAug[i+1][j];
                }
                m++;
            }
        }
        for(int r=0;r<n;r++) {
            Y[r][0]=precision(NewAug[r][n],prec);
        }
        System.out.println("Y matrix:");
        print_matrix(Y,n,1);
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return Y;
    }

    public static double[][] backwordSub (double[][]mat ,double[][] B,  int n, int prec) {
        long startTime = System.nanoTime();
        int m=0,i,j;
        double[][] X = new double [n][1];
        double[][]NewAug=new double[n][n+1];
        for(i=0;i<n;i++) {
            for(j=0; j<n ; j++) {
                NewAug[i][j]=mat[i][j];
            }
        }
        for(i=0;i<n;i++)  {
            NewAug[i][n]=B[i][0];
        }
        double[] L=new double[1000] ;
        for(int k=n-1; k>0; k--) {
            for (i=k; i>0; i--) {
                L[m]=precision(NewAug[i-1][k]/ NewAug[k][k],prec);
                for( j=0; j<n+1; j++) {
                    NewAug[i-1][j]= (-1*L[m]*NewAug[k][j])+NewAug[i-1][j];
                }
                m++;
            }
        }
        for(int r=0;r<n;r++) {
            for(int c=0 ; c<n;c++) {
                if(c==r) {
                    X[r][0]=precision(NewAug[r][n]/NewAug[r][c],prec);
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
        double l;
        int m=0,i,j,count=0, prec;
        System.out.println("Enter the dimension of the matrix:");
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        System.out.println("Enter the augmented matrix");
        double [][]matrix = new double[n][n+1];
        for( i=0; i<n; i++)
            for( j=0; j<n+1; j++)
                matrix[i][j] = sc.nextDouble();
        double [][]mat = Amatrix(matrix,n,n+1);
        double [][] B = Bmatrix(matrix,n,n+1);
        System.out.println("matrix B:");
        print_matrix(B,n,1);
        System.out.println("matrix of coeff:");
        print_matrix(mat,n,n);
        double[]L=arrayL(mat,n,3);


        double[][] NewMat=gaussMat(mat,n,3);
        print_matrix(NewMat,n,n);

        System.out.println("");
        double[][] Lmatrix=new double[n][n];
        double[][] Umatrix=new double[n][n];
        //print L
        System.out.println("L matrix:");
        Lmatrix=Do_L(n,L);
        //print U
        System.out.println("");
        System.out.println("U matrix:");
        Umatrix=Do_U(mat,n);
        double[][] Y=new double[n][1];
        Y= forwardSub(Lmatrix,B,n,3);
        backwordSub(Umatrix, Y,n,3);
    }
}