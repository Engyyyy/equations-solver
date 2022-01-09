package com.example.Backend.Model.NonLinear;

import java.math.BigDecimal;
import java.math.MathContext;

public class NewtonRaphson {
    public static long totalTime = 0;
    static long funTime = 0;
    public boolean flagsin = false;
    public boolean flagcos = false;
    public boolean flagexp = false;
    public boolean falge = false;
    public double SignificantFig(double value ,int prec){
        long startTime = System.nanoTime();
        double rounded;
        if(prec!=-1){
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
    }
    public String[] CalDerivativeTerm(String term ,double value ,int prec){
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
            double sinval = SignificantFig(Math.sin(angel),prec);
            double cosval = SignificantFig(Math.cos(angel),prec);
            if (flagsin){
                resultDeriv = excoe * xcoefficient * xpower * SignificantFig(Math.pow(value,xpower-1),prec) * cosval;
                resultFunc = sinval * excoe;
                flagsin=false;
            }
            else if (flagcos){
                resultDeriv = excoe * -xcoefficient * xpower * SignificantFig(Math.pow(value,xpower-1),prec) * sinval;
                resultFunc = cosval * excoe;
                flagcos=false;
            }
        }
        else if(falge){
            double eval = SignificantFig(Math.exp(xcoefficient*Math.pow(value,xpower)),prec) * excoe ;
            resultDeriv = xcoefficient * SignificantFig(Math.pow(value,xpower-1),prec) * xpower * eval;
            resultFunc = eval;
            falge=false;
        }
        else if(flagexp){
            double expval = SignificantFig(Math.pow(expon,xcoefficient*Math.pow(value,xpower)),prec) * excoe;
            resultDeriv = Math.log(expon) * expval * xcoefficient * xpower * SignificantFig(Math.pow(value,xpower-1),prec);
            resultFunc = expval;
            flagexp=false;
        }
        else{
            resultDeriv = xcoefficient * xpower * SignificantFig(Math.pow(value,xpower-1),prec);
            resultFunc = xcoefficient * SignificantFig(Math.pow(value,xpower),prec);
        }
        result[0]=derivExp;
        double x=SignificantFig(resultFunc,prec);
        result[1]=String.valueOf(x);
        double y=SignificantFig(resultDeriv,prec);
        result[2]=String.valueOf(y);
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return result;
    }
    public String[] CalDerivativeExp(String exp ,double value ,int prec){
        long startTime = System.nanoTime();
        String[] terms = exp.split("\\+") ;
        int i  = 0;
        String totalDeriv = "";
        double funct=0;
        double deriv=0;
        String[] finalResult = new String[3];
        String[] result ;
        while (i<terms.length) {
            result = CalDerivativeTerm(terms[i], value, prec);
            funct = SignificantFig(funct, prec) + Double.parseDouble(result[1]);
            deriv = SignificantFig(deriv, prec) + Double.parseDouble(result[2]);
            if (i == 0) {
                totalDeriv = totalDeriv + result[0];
            } else {
                totalDeriv = totalDeriv + "+" + result[0];
            }
            i++;
        }
        finalResult[0] = totalDeriv;
        finalResult[1] = String.valueOf(SignificantFig(funct, prec));
        finalResult[2] = String.valueOf(SignificantFig(deriv, prec));
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return finalResult;
    }
    public double NewtonRaphsonImp (String exp ,double initislGuess ,int numOfIterations ,double relativeError,int signFig){
        long startTime = System.nanoTime();
        double x = SignificantFig(initislGuess,signFig);
        int iterations = 0;
        String[] res = CalDerivativeExp(exp,initislGuess,signFig);
        double funct = Double.parseDouble(res[1]);
        double deriv = Double.parseDouble(res[2]);
        double h = SignificantFig(funct/deriv,signFig);
        while ((numOfIterations > iterations) && (relativeError < Math.abs(h))){
            res = CalDerivativeExp(exp,x,signFig);
            funct = Double.parseDouble(res[1]);
            deriv = Double.parseDouble(res[2]);
            h =  SignificantFig(funct/deriv,signFig);
            x = SignificantFig(x - h,signFig);
            iterations++;
        }
        long endTime = System.nanoTime();
        funTime = endTime - startTime;
        totalTime = totalTime + funTime;
        return SignificantFig(x,signFig);
    }
    public static void main (String[] args) {
        String str = "exp(-x^1)+-x^1";
        double val = 0;
        NewtonRaphson res = new NewtonRaphson();
        double finalResult = res.NewtonRaphsonImp(str, val,10,.0000001,9);
        String[] resu = res.CalDerivativeExp(str,2,6);
        System.out.println(finalResult);
        for (int i=0;i<3;i++){
            System.out.println(resu[i]);
        }
    }
}
