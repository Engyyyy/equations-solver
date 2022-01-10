package com.example.Backend.Model.NonLinear;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Scanner;

public class Bisection {
    public static long totalTime = 0;
    static long funTime = 0;
    public static ArrayList<Double> values;

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

    public static double bisection(String fX, double a, double b, double Ea, double maxIter, int prec) throws Exception {
        values = new ArrayList<>();
        long startTime = System.nanoTime();
        ExpressionBuilder builder = new ExpressionBuilder(fX).variables("x");
        int i = 0;
        double fl = builder.build().setVariable("x", a).evaluate();
        double fu = builder.build().setVariable("x", b).evaluate();
        if(fl*fu > 0) {
            System.out.println("No Brackets");
            throw new Exception();
        }
        if(fl == 0) return a;
        if(fu == 0) return b;
        double ea = 100;
        double xr = 100, xr_old, fr;
        while ( ea > Ea && i<maxIter){
            xr_old = xr;
            xr= precision((a+b)/2, prec);           // Mid point
            values.add(xr);
            fr = precision(builder.build().setVariable("x", xr).evaluate(), prec);
            if(i > 0 && xr != 0) {
                ea = precision(Math.abs((xr-xr_old)/xr)*100, prec);
            }
            if(fl*fr > 0) {
                a = xr;
                fl = fr;
            }
            else {
                b = xr;
            }
            i++;
        }
        System.out.println("Approximate solution = " + xr );
        System.out.println("iter = " + i);
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return xr;
    }//end of function
    public static void main(String[] args) throws Exception {
        Scanner MyInput = new Scanner(System.in);
        System.out.print("Enter the equation: ");
        String fX = MyInput.next();
        System.out.print("Enter a: ");
        double a = MyInput.nextDouble();
        System.out.print("Enter b: ");
        double b = MyInput.nextDouble();
        System.out.print("Enter Epsilon: ");
        double Ea = MyInput.nextDouble();
        System.out.print("Enter number of iterations: ");
        int iterations = MyInput.nextInt();
        System.out.print("Enter precision: ");
        int precision = MyInput.nextInt();
        bisection(fX, a, b, Ea, iterations, precision);
    }
}
