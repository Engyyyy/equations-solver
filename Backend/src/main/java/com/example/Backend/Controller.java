package com.example.Backend;

import com.example.Backend.Model.Linear.*;
import com.example.Backend.Model.NonLinear.*;
import com.example.Backend.Responses.parameterObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class Controller {
    public static long runtime;

    @GetMapping("/gauss")
    public double[][] solveGauss(@RequestParam String augmented) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        parameterObject aug = objectMapper.readValue(augmented, parameterObject.class);
        System.out.println("GAUSS!!!");
        GaussElimination.gauss_steps(aug.getMatrix(), aug.getMatrix().length, aug.getMatrix()[0].length, aug.getPrecision());
        double[][] res =  GaussElimination.result_matrix(aug.getMatrix(), aug.getMatrix().length, aug.getMatrix()[0].length, aug.getPrecision());
        runtime = GaussElimination.totalTime;
        return res;
    }
    @GetMapping("/gauss-jordan")
    public double[][] solveGaussJordan(@RequestParam String augmented) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        parameterObject aug = objectMapper.readValue(augmented, parameterObject.class);
        System.out.println("GAUSS JORDAN!!!");
        GaussElimination.gauss_steps(aug.getMatrix(), aug.getMatrix().length, aug.getMatrix()[0].length, aug.getPrecision());
        GaussJordan.jordan_steps(aug.getMatrix(), aug.getMatrix().length, aug.getMatrix()[0].length, aug.getPrecision());
        double[][] res = GaussElimination.result_matrix(aug.getMatrix(), aug.getMatrix().length, aug.getMatrix()[0].length, aug.getPrecision());
        runtime = GaussJordan.totalTime;
        return res;
    }
    @GetMapping("/gauss-seidel")
    public double[] solveGaussSeidel(@RequestParam int iter, @RequestParam String parameterObject, @RequestParam double error) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        parameterObject parameters = objectMapper.readValue(parameterObject, parameterObject.class);
        System.out.println("GAUSS SEIDEL!!!");
        GuassSeidel2 seidel = new GuassSeidel2();
        double[] res = seidel.GuassSolution(parameters.getMatrix(), iter, error, parameters.getInitialGuess(), parameters.getPrecision());
        runtime = GuassSeidel2.totalTime;
        return res;
    }
    @GetMapping("/jacobi")
    public double[] solveJacobi(@RequestParam int iter, @RequestParam String parameterObject, @RequestParam double error) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        parameterObject parameters = objectMapper.readValue(parameterObject, parameterObject.class);
        System.out.println("JACOBI!!!");
        JacobiIterative jacobi = new JacobiIterative();
        double[] res = jacobi.GuassSolution(parameters.getMatrix(), iter, error, parameters.getInitialGuess(), parameters.getPrecision());
        runtime = JacobiIterative.totalTime;
        return res;
    }
    @GetMapping("/downlittle")
    public double[][] solveDownlittle(@RequestParam String augmented) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        parameterObject aug = objectMapper.readValue(augmented, parameterObject.class);
        System.out.println("DOWNLITTILE!!!");
        double[][] mat = LU_DownLittle.Amatrix(aug.getMatrix(), aug.getMatrix().length, aug.getMatrix()[0].length);
        double[][] B = LU_DownLittle.Bmatrix(aug.getMatrix(), aug.getMatrix().length, aug.getMatrix()[0].length);
        double[]L = LU_DownLittle.arrayL(mat, aug.getMatrix().length, aug.getPrecision());
        double[][] NewMat = LU_DownLittle.gaussMat(mat, aug.getMatrix().length, aug.getPrecision());
        double[][] Lmatrix = LU_DownLittle.Do_L(aug.getMatrix().length, L);
        double[][] Umatrix = LU_DownLittle.Do_U(mat, aug.getMatrix().length);
        double[][] Y = LU_DownLittle.forwardSub(Lmatrix, B, aug.getMatrix().length, aug.getPrecision());
        double[][] res =  LU_DownLittle.backwordSub(Umatrix, Y, aug.getMatrix().length, aug.getPrecision());
        runtime = LU_DownLittle.totalTime;
        return res;
    }
    @GetMapping("/crout")
    public double[][] solveCrout(@RequestParam String augmented) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        parameterObject aug = objectMapper.readValue(augmented, parameterObject.class);
        System.out.println("CROUT!!!");
        double[][] mat = LU_Crout.Amatrix(aug.getMatrix(), aug.getMatrix().length, aug.getMatrix()[0].length);
        double[][] B = LU_Crout.Bmatrix(aug.getMatrix(), aug.getMatrix().length, aug.getMatrix()[0].length);
        double[]L = LU_Crout.arrayL(mat, aug.getMatrix().length, aug.getPrecision());
        double[][] NewMat = LU_Crout.gaussMat(mat, aug.getMatrix().length, aug.getPrecision());
        double[][] Lmatrix = LU_Crout.Do_L(NewMat, aug.getMatrix().length);
        double[][] Umatrix = LU_Crout.Do_U(aug.getMatrix().length, L);
        double[][] Y = LU_Crout.forwardSub(Lmatrix, B, aug.getMatrix().length, aug.getPrecision());
        double[][] res = LU_Crout.backwordSub(Umatrix, Y, aug.getMatrix().length, aug.getPrecision());
        runtime = LU_Crout.totalTime;
        return res;
    }
    @GetMapping("/cholesky")
    public double[][] solveCholesky(@RequestParam String augmented) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        parameterObject aug = objectMapper.readValue(augmented, parameterObject.class);
        System.out.println("CHOLESKY!!!");
        LU_Cholesky.print_matrix(aug.getMatrix(), aug.getMatrix().length, aug.getMatrix()[0].length);
        double[][] mat = LU_Cholesky.Amatrix(aug.getMatrix(), aug.getMatrix().length, aug.getMatrix()[0].length);
        double[][] B = LU_Cholesky.Bmatrix(aug.getMatrix(), aug.getMatrix().length, aug.getMatrix()[0].length);
        double[][] Lmatrix = LU_Cholesky.Do_L(mat, aug.getPrecision());
        double[][] Umatrix = LU_Cholesky.Do_L_Trans(Lmatrix, aug.getMatrix().length);
        double[][] Y = LU_Cholesky.forwardSub(Lmatrix, B, aug.getMatrix().length);
        double[][] res = LU_Cholesky.backwordSub(Umatrix, Y, aug.getMatrix().length);
        runtime = LU_Cholesky.totalTime;
        return res;
    }
    @GetMapping("/runtime")
    public long getRuntime() {
        return runtime;
    }
//=====================================================================================================

    @GetMapping("/bisection")
    public double findByBisection(@RequestParam String fx, @RequestParam double x0, @RequestParam double x1, @RequestParam double es, @RequestParam int maxIter, @RequestParam int sf) throws Exception {
        fx = fx.replace(" ", "+");
        double res = Bisection.bisection(fx, x0, x1, es, maxIter, sf);
        runtime = Bisection.totalTime;
        return res;
    }
    @GetMapping("/getbisectionvalues")
    public ArrayList<Double> getbisectionvalues() {
        return Bisection.values;
    }
    @GetMapping("/fixedpoint")
    public double findByFixedPoint(@RequestParam  String fx, @RequestParam double x0, @RequestParam double es, @RequestParam int maxIter, @RequestParam int sf) {
        fx = fx.replace(" ", "+");
        double res = FixedPoint.fixedPoint(fx, x0, es, maxIter, sf);
        runtime = FixedPoint.totalTime;
        return res;
    }

    @GetMapping("/newtonraphson")
    public double findByNewtonRaphson(@RequestParam  String fx, @RequestParam double x0, @RequestParam double es, @RequestParam int maxIter, @RequestParam int sf) {
        fx = fx.replace(" ", "+");
        System.out.println(fx);
        NewtonRaphson newtonRaphson = new NewtonRaphson();
        double res = newtonRaphson.NewtonRaphsonImp(fx, x0, maxIter, es, sf);
        runtime = NewtonRaphson.totalTime;
        return  res;
    }
}
