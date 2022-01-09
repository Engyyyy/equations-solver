//package com.example.Backend.Model.NonLinear;
//
//public class FalsePosition {
//    static double falseP() {
//        String fx;
//        double a;
//        double b;
//        double epsilon;
//        int iteration;
//        double m;
//        int i=1;
//        while( (b-a) > epsilon && i<iteration ){
//            m = b - ( funcb / ( funcb - funca ) ) * (b - a);
//
//            Exprission e=new ExprissionBuilder(fx).variables("x").build().setVariables("x" , b);
//            double funcb=e.evaluate();
//
//            Exprission v=new ExprissionBuilder(fx).variables("x").build().setVariables("x" , a);
//            double funca=v.evaluate();
//
//            Exprission g=new ExprissionBuilder(fx).variables("x").build().setVariables("x" , m);
//            double funcm=g.evaluate();
//
//            if ( (funcm) > 0 && funca < 0) || (funcm < 0 && funca > 0) )
//            {  // f(a) and f(m) have different signs: move b
//                b = m;
//            }
//	   	    else
//            {  // f(a) and f(m) have same signs: move a
//                a = m;
//            }
//            i++;
//        }
//        System.out.printf("Approximate solution = %.5f" , (a+b)/2 );
//        System.out.println("");
//        j = i-1;
//        System.out.println("Number of iterations = "+ j);
//    }
//}
