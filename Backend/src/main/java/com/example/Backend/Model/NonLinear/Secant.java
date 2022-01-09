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
    static long totalTime = 0;
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

    public static double F(String exp, double n, int prec) {
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
    public static void secantRes( double fOfX0, double fOfX_1, double x0, double x_1, int prec) {
        long startTime = System.nanoTime();
        boolean flag= true;
        int i = 0;
        double root,given=5,error;
        while(flag){
            root = x0 - ((fOfX0*(x0-x_1))/(fOfX0-fOfX_1));
            root = precision(root, prec);
            error = Math.abs((root-x0)/root)*100;
            error = precision(error, prec);

            System.out.println("Iteration : "+ i++);
            System.out.println("Root:"+root);
            System.out.println("Error:"+error);

            x_1 = x0;
            x0 = root;

            if(given>error)
            {
                flag=false;
            }
        }
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
    }
    public static String[] CalDerivativeTerm(String term ,double value ,int prec){
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
                i+=3;
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
        powerCoef = powerCoef.replace(")","");
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
        String[] result = new String[3];
        if(flagsin){
            derivExp += String.valueOf(xcoefficient*xpower*excoe)+"x"+"^"+String.valueOf(xpower-1)+"sin"+String.valueOf(xcoefficient)+"x"+"^"+String.valueOf(xpower);
        }
        else if(flagcos){
            derivExp += String.valueOf(xcoefficient*xpower*excoe)+"x"+"^"+String.valueOf(xpower-1)+"cos"+String.valueOf(xcoefficient)+"x"+"^"+String.valueOf(xpower);
        }
        else if(falge){
            derivExp += String.valueOf(xcoefficient*xpower*excoe)+"x"+"^"+String.valueOf(xpower-1)+"exp("+String.valueOf(xcoefficient)+"x"+"^"+String.valueOf(xpower)+")";
        }
        else if(flagexp){
            derivExp += String.valueOf(xcoefficient*xpower*excoe)+"x"+"^"+String.valueOf(xpower-1)+String.valueOf(expon)+"("+String.valueOf(xcoefficient)+"x"+"^"+String.valueOf(xpower)+")";
        }
        else {
            derivExp += String.valueOf(xcoefficient*xpower)+"x"+"^"+String.valueOf(xpower);
        }
        if(flagsin || flagcos){
            double angel = Math.toRadians(xcoefficient*Math.pow(value,xpower));
            double sinval = precision(Math.sin(angel),prec);
            double cosval = precision(Math.cos(angel),prec);
            if (flagsin){
                resultDeriv = excoe * xcoefficient * xpower * precision(Math.pow(value,xpower-1),prec) * cosval;
                resultFunc = sinval * excoe;
                flagsin=false;
            }
            else if (flagcos){
                resultDeriv = excoe * -xcoefficient * xpower * precision(Math.pow(value,xpower-1),prec) * sinval;
                resultFunc = cosval * excoe;
                flagcos=false;
            }
        }
        else if(falge){
            double eval = precision(Math.exp(xcoefficient*Math.pow(value,xpower)),prec) * excoe ;
            resultDeriv = xcoefficient * precision(Math.pow(value,xpower-1),prec) * xpower * eval;
            resultFunc = eval;
            falge=false;
        }
        else if(flagexp){
            double expval = precision(Math.pow(expon,xcoefficient*Math.pow(value,xpower)),prec) * excoe;
            resultDeriv = Math.log(expon) * expval * xcoefficient * xpower * precision(Math.pow(value,xpower-1),prec);
            resultFunc = expval;
            flagexp=false;
        }
        else{
            resultDeriv = xcoefficient * xpower * precision(Math.pow(value,xpower-1),prec);
            resultFunc = xcoefficient * precision(Math.pow(value,xpower),prec);
        }
        result[0]=derivExp;
        double x=precision(resultFunc,prec);
        result[1]=String.valueOf(x);
        double y=precision(resultDeriv,prec);
        result[2]=String.valueOf(y);
        return result;
    }
    public static String[] CalDerivativeExp(String exp ,double value ,int prec){
        String[] terms = exp.split("\\+") ;
        int i  = 0;
        String totalDeriv = "";
        double funct=0;
        double deriv=0;
        String[] finalResult = new String[3];
        String[] result ;
        while (i<terms.length) {
            result = CalDerivativeTerm(terms[i], value, prec);
            funct = precision(funct, prec) + Double.parseDouble(result[1]);
            deriv = precision(deriv, prec) + Double.parseDouble(result[2]);
            if (i == 0) {
                totalDeriv = totalDeriv + result[0];
            } else {
                totalDeriv = totalDeriv + "+" + result[0];
            }
            i++;
        }
        finalResult[0] = totalDeriv;
        finalResult[1] = String.valueOf(precision(funct, prec));
        finalResult[2] = String.valueOf(precision(deriv, prec));
        return finalResult;
    }
    public static void main(String[] args) {
        String exp;

        double x_1,x0,root,given=5,error, fOfX0, fOfX_1;
        boolean flag=true;
        int i=1;
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the equation: ");
        exp= sc.next();

//        System.out.print("Enter value for x_1 : ");
//        x_1 = sc.nextDouble();
//
//        System.out.print("Enter value for x0 : ");
//        x0 = sc.nextDouble();
//
//        System.out.print("Enter value for precision : ");
//        int prec = sc.nextInt();

//        fOfX0=F(exp,x0, prec);
//        fOfX_1=F(exp,x_1, prec);

        //secantRes(fOfX0,fOfX_1,x0,x_1, prec);

        String[] res = CalDerivativeExp(exp, 0, 5);
        System.out.println(res[0]);
    }
}
