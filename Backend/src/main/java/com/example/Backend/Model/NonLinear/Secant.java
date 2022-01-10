package com.example.Backend.Model.NonLinear;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Scanner;

public class Secant {
    static double e = 2.71828182846;

    public static boolean flagsin = false;
    public static boolean flagcos = false;
    public static boolean flagexp = false;
    public static boolean falge = false;
    public static boolean flagsinex = false;
    public static boolean flagcosex = false;
    public static boolean flagexpex = false;
    public static boolean falgeex = false;
    public static long totalTime = 0;
    static long funTime = 0;

    //function for precision
    static double SignificantFig(double value, int prec) {
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
    public static String CalDerivativeTerm(String term){
        long startTime = System.nanoTime();
        String coefficient = "";
        String powerCoef = "";
        String exp = "";
        String extracoeff = "";
        if(!term.contains("x")){
            term+="x^0";
        }
        int i;
        for(i=0 ;term.charAt(i)!='x';i++){
            if(term.charAt(i)=='s'){
                flagsin = true;
                if(i!=0){if(term.charAt(i-1)=='*'){
                    extracoeff=term.substring(0,i-1);
                    coefficient=coefficient.substring(i);
                }}
                i+=2;
                continue;
            }
            else if(term.charAt(i)=='c'){
                flagcos = true;
                if(i!=0){if(term.charAt(i-1)=='*'){
                    extracoeff=term.substring(0,i-1);
                    coefficient=coefficient.substring(i);
                }}
                i+=2;
                continue;
            }
            else if (term.charAt(i)=='e'){
                falge = true;
                if(i!=0){if(term.charAt(i-1)=='*'){
                    extracoeff=term.substring(0,i-1);
                    coefficient=coefficient.substring(i);
                }}
                i+=2;
                continue;
            }
            else if(term.charAt(i)=='^'){
                flagexp = true;
                exp+=term.substring(0,i);
                if(exp.contains("*")){
                    String[] spli = exp.split("\\*");
                    extracoeff = spli[0];
                    exp = spli[1];
                }
                coefficient=coefficient.substring(i);
                continue;
            }
            coefficient +=  term.charAt(i) ;
        }
        for (i = i + 2; i != term.length() && term.charAt(i) != ' ' && term.contains("x"); i++)
        {
            powerCoef += term.charAt(i);
        }
        String derivExp = "";
        double xcoefficient = 1;
        if(coefficient.length()==1 && coefficient.contains("-")){xcoefficient = -1;}
        else if(coefficient!=""){xcoefficient = Double.parseDouble(coefficient);}
        double xpower = 0;
        if(powerCoef!=""){xpower = Double.parseDouble(powerCoef) ;}
        long expon=0;
        if(exp!="") {expon = Long.parseLong(exp);}
        double excoe = 1;
        if(extracoeff!=""){excoe = Double.parseDouble(extracoeff);}
        double resultDeriv=0,resultFunc=0;
        String result = "";
        if(flagsin){
            derivExp += String.valueOf(xcoefficient*xpower*excoe)+"x"+"^"+String.valueOf(xpower-1)+"cos("+String.valueOf(xcoefficient)+"x"+"^"+String.valueOf(xpower)+")";
            flagsin = false;
        }
        else if(flagcos){
            derivExp += String.valueOf(-1*xcoefficient*xpower*excoe)+"x"+"^"+String.valueOf(xpower-1)+"sin("+String.valueOf(xcoefficient)+"x"+"^"+String.valueOf(xpower)+")";
            flagcos=false;
        }
        else if(falge){
            derivExp += String.valueOf(xcoefficient*xpower*excoe)+"x"+"^"+String.valueOf(xpower-1)+"exp("+String.valueOf(xcoefficient)+"x"+"^"+String.valueOf(xpower)+")";
            falge=false;
        }
        else if(flagexp){
            derivExp += String.valueOf(xcoefficient*xpower*excoe)+"x"+"^"+String.valueOf(xpower-1)+String.valueOf(expon)+"("+String.valueOf(xcoefficient)+"x"+"^"+String.valueOf(xpower)+")";
            flagexp=false;
        }
        else {
            derivExp += String.valueOf(xcoefficient*xpower)+"x"+"^"+String.valueOf(xpower-1);
        }
        result = derivExp;
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return result;
    }
    public static String CalDerivativeExp(String exp){
        long startTime = System.nanoTime();
        String[] terms = exp.split("\\+") ;
        int i  = 0;
        String totalDeriv = "";
        double funct=0;
        double deriv=0;
        String[] finalResult = new String[3];
        String result = "";
        while (i<terms.length) {
            terms[i]=terms[i].replace("(","");
            terms[i]=terms[i].replace(")","");
            result = CalDerivativeTerm(terms[i]);
            if (i == 0) {
                totalDeriv = totalDeriv + result;
            } else {
                totalDeriv = totalDeriv + "+" + result;
            }
            i++;
        }
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return totalDeriv;
    }

    public static double F(String exp, double n, int prec) {
        long startTime = System.nanoTime();
        Expression e = new ExpressionBuilder(exp)
                .variables("x")
                .build()
                .setVariable("x", n);
        double result = e.evaluate();
        result = SignificantFig(result, prec);
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return result;
    }
    public static double secantRes(String exp, double x0, double x_1, int prec, int maxIter, double Ea) {
        long startTime = System.nanoTime();

        double fOfX0=F(exp,x0, prec);
        double fOfX_1=F(exp,x_1, prec);
        int i = 0;
        double error=100;
        double root = x0;
        while(error>Ea && i<maxIter) {
            root = x0 - ((fOfX0*(x0-x_1))/(fOfX0-fOfX_1));
            root = SignificantFig(root, prec);
            error = Math.abs((root-x0)/root)*100;
            error = SignificantFig(error, prec);

            System.out.println("Iteration : "+ i);
            System.out.println("Root:"+root);
            System.out.println("Error:"+error);

            x_1 = x0;
            x0 = root;
            fOfX0=F(exp,x0, prec);
            fOfX_1=F(exp,x_1, prec);

            i++;
        }
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return root;
    }

    public static void main(String[] args) {
        String exp;

        double x_1,x0,root,given=5,error, fOfX0, fOfX_1;
        boolean flag=true;
        int i=1;
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the equation: ");
        exp= sc.next();

        System.out.print("Enter value for x_1 : ");
        x_1 = sc.nextDouble();

        System.out.print("Enter value for x0 : ");
        x0 = sc.nextDouble();

        System.out.print("Enter value for precision : ");
        String deriv = CalDerivativeExp("-1*cos(x^2)+x^3");
        int prec = sc.nextInt();
        System.out.print("Enter value for iterations : ");
        int maxIter = sc.nextInt();
        System.out.print("Enter value for error : ");
        double Ea = sc.nextDouble();
        System.out.println(deriv);
        System.out.println(secantRes(exp,x0,x_1, prec, maxIter, Ea));
    }
}
