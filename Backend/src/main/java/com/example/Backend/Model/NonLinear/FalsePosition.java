package com.example.Backend.Model.NonLinear;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Scanner;

public class FalsePosition {
    public static long totalTime = 0;
    static long funTime = 0;
    //public static ArrayList<Double> values;

    //function for precision
    static double precision(double value, int prec) {
        long startTime = System.nanoTime();
        if(prec != -1) {
            double rounded;
            BigDecimal bd = new BigDecimal(value);
            bd = bd.round(new MathContext(prec));
            rounded = bd.doubleValue();
            long endTime = System.nanoTime();
            funTime = endTime - startTime;
            totalTime = totalTime + funTime;
            return rounded;
        }
        else {
            long endTime = System.nanoTime();
            funTime = endTime - startTime;
            totalTime = totalTime + funTime;
            return value;
        }
    }//end of function

    static double func(String exp, double n, int prec)
    {
		long startTime = System.nanoTime();
		Expression e = new ExpressionBuilder(exp)
                .variables("x")
                .build()
                .setVariable("x", n);
        double result = e.evaluate();
        result = precision(result, prec);
        long endTime = System.nanoTime();
 		funTime = endTime - startTime;
 		totalTime = totalTime + funTime;
        return result;
    }

    public static double falseP(String fX, int prec, int maxIter, double xL, double xU, double Ea) throws Exception {
        long startTime = System.nanoTime();
        double[] xR = new double[100];
        double E=100;
        int i = 0;
        if(func(fX, xU, prec) * func(fX, xL, prec) > 0) {
            System.out.println("No Brackets");
            throw new Exception();
        }
        if(func(fX, xU, prec) == 0) return xU;
        if(func(fX, xL, prec) == 0) return xL;
        while(E>Ea && i<maxIter) {
            xR[i] = xU - ( func(fX, xU, prec) / (func(fX, xL, prec) - func(fX, xU, prec)) * (xL - xU) );
            xR[i] = precision(xR[i], prec);
            System.out.println("xR: " + xR[i]);
            if(i-1 > -1) {
                E = Math.abs((xR[i] - xR[i-1])/xR[i])*100;
                E = precision(E, prec);
                System.out.println("E: " + E);
            }
            if(func(fX, xR[i], prec)>0) {
                if(func(fX, xL, prec)>0) {
                    xL = precision(xR[i], prec);
                }
                else {
                    xU = precision(xR[i], prec);
                }
            }
            else {
                if(func(fX, xL, prec)<0) {
                    xL = precision(xR[i], prec);
                }
                else {
                    xU = precision(xR[i], prec);
                }
            }
            System.out.println("xL b3d1: " + xL);
            System.out.println("xU b3d1: " + xU);
            i++;
        }

        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return precision(xR[i-1], prec);
    }



    public static void main(String[] args) throws Exception {
        String fX;
        int prec, iterations;
        double xL, xU, epsilon;

        Scanner MyInput = new Scanner(System.in);
        fX = MyInput.next();
        xL = MyInput.nextDouble();
        xU = MyInput.nextDouble();
        epsilon = MyInput.nextDouble();
        prec = MyInput.nextInt();
        iterations = MyInput.nextInt();
        MyInput.close();

        System.out.println("\nFalse Position method: ");
        System.out.print(falseP(fX, prec, iterations, xL, xU, epsilon));

    }
}
