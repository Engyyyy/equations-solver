package com.example.Backend.Responses;

public class parameterObject {
    private double[][] matrix;
    private double[] initialGuess;
    private int precision;

    public double[][] getMatrix() {
        return matrix;
    }

    public double[] getInitialGuess() {
        return initialGuess;
    }

    public int getPrecision() {
        return precision;
    }
}
