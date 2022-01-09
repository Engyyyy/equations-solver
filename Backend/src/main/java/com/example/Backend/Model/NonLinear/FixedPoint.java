package com.example.Backend.Model.NonLinear;


import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Scanner;

public class FixedPoint {
    static String gX;  //entered g(x)
    static double E;   //error after each step
    static double res; //g(x) after each step

    public static long totalTime = 0;
    static long funTime = 0;

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

    public static double fixedPoint(String fX, double x0, double Ea, double maxIter, int prec) {
        long startTime = System.nanoTime();
        gX = fX + "+x";
        Expression expression = new ExpressionBuilder(gX).variables("x").build().setVariable("x", x0);
        res = expression.evaluate();
        res = precision(res, prec);
        E = Math.abs((res - x0)/res)*100;
        E = precision(E, prec);
        x0 = res;
        x0 = precision(x0, prec);
        int iter = 1;
        while(E>Ea && iter < maxIter) {
            System.out.println("es= "+E);
            System.out.println("x0= "+x0);
            System.out.println("res= "+res);
            Expression exp = new ExpressionBuilder(gX).variables("x").build().setVariable("x", x0);
            res = exp.evaluate();
            res = precision(res, prec);
            E = Math.abs((res - x0)/res)*100;
            E = precision(E, prec);
            x0 = res;
            x0 = precision(x0, prec);
            iter++;
        }
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return res;
    }

    public static void main(String[] args) {
        Scanner MyInput = new Scanner(System.in);
        System.out.print("Enter f(x): ");
        String fX = MyInput.next();
        System.out.print("Enter X0: ");
        double x0 = MyInput.nextDouble();
        System.out.print("Enter Ea: ");
        double Ea = MyInput.nextDouble();
        System.out.print("Enter maximum number of iterations: ");
        int maxIter = MyInput.nextInt();
        //System.out.println("the root is: " + fixedPoint(fX, x0, Ea, maxIter));
        System.out.println("Error: " + E);
    }
}
