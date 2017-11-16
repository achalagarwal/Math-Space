package MathSpace;

import java.util.Scanner;

/**
 * Created by achal on 29/08/17.
 */
    public class GaussianElimination {
    int systemSize;
    private double[] scalingValues;
    private double[][] coefficientMatrix;
    private int[] pivotVector;
    private boolean isScaled;
    private Scanner sc = new Scanner(System.in);

    GaussianElimination(int sizeOfSystemOfEquations, double[][] matrix) {
        if (sizeOfSystemOfEquations <= 0)
            throw new IllegalArgumentException("size should be a positive number: " + sizeOfSystemOfEquations);

        this.systemSize = sizeOfSystemOfEquations;
        this.coefficientMatrix = matrix;
        this.pivotVector = new int[this.systemSize];
        this.scalingValues = new double[this.systemSize];
        this.isScaled = false;
        initializePivotVector();
    }

    GaussianElimination(int sizeOfSystemOfEquations) {
        if (sizeOfSystemOfEquations <= 0)
            throw new IllegalArgumentException("size should be a positive number: " + sizeOfSystemOfEquations);
        this.systemSize = sizeOfSystemOfEquations;
        this.coefficientMatrix = new double[this.systemSize][this.systemSize];
        this.pivotVector = new int[this.systemSize];
        this.isScaled = false;
        this.scalingValues = new double[this.systemSize];
    }
    private void initializePivotVector(){ //done when coefficient vector initialized
        for(int i = 0; i<this.systemSize; i++) {
            this.pivotVector[i] = i;
        }
    }
    public void initializeArray() {
        System.out.println("Start Inputting " + this.systemSize * this.systemSize + " real numbers for your " + this.systemSize + " x " + this.systemSize + " Matrix");
        if (this.coefficientMatrix == null)
            this.coefficientMatrix = new double[this.systemSize][this.systemSize];
        for (int i = 0; i < this.systemSize; i++) {
            for (int j = 0; j < this.systemSize; j++) {
                double temp = sc.nextDouble();
                this.coefficientMatrix[i][j] = temp;
            }
        }
        initializePivotVector();
    }

    public void scale() {
        this.calculateScalingValues();
        this.scaleMatrix();
        this.isScaled = true;
    }

    private void calculateScalingValues() {
        double max;
        for (int i = 0; i < this.systemSize; i++) {
            max = 0;
            for (int j = 0; j < this.systemSize; j++) {
                if (Math.abs(this.coefficientMatrix[i][j]) > max)
                    max = Math.abs(this.coefficientMatrix[i][j]);
            }
            if (max != 0)
                this.scalingValues[i] = max;
        }
    }
    private boolean helperFunction(int row, int col){ //add Comment
        for(int i = 0;i<col;i++){
            if(this.scalingValues[i] == row)
                return false;
        }
        return true;
    }
    private boolean pivotHelperFunction(int row, int col){ //add Comment
        for(int i = 0;i<col;i++){
            if(this.pivotVector[i] == row)
                return false;
        }
        return true;
    }
    private void scaleMatrix() {
        for (int i = 0; i < this.systemSize; i++) {
            for (int j = 0; j < this.systemSize; j++) {
                this.coefficientMatrix[i][j] = this.coefficientMatrix[i][j] / this.scalingValues[i];
            }
        }
    }
    private void partialPivotColumn(int column) {
        int maxRow = this.pivotVector[column]; //initialization purpose
        double max = 0;
        for (int row = 0; row < this.systemSize; row++) {
            if (Math.abs(this.coefficientMatrix[row][column]) > max && helperFunction(row, column)) {
                max = Math.abs(this.coefficientMatrix[row][column]);
                maxRow = row;
            }
        }
        int temp = this.pivotVector[column];
        this.pivotVector[column] = this.pivotVector[maxRow];
        this.pivotVector[maxRow] = temp;
    }
    private void generateMultiplierColumn(int column){
        double denominator = this.coefficientMatrix[this.pivotVector[column]][column];
        if (denominator == 0){
            return ;
        }
        for(int i = 0; i<this.systemSize;i++){
            if(this.pivotHelperFunction(i,column)&&i!=this.pivotVector[column]){
                this.coefficientMatrix[i][column] /= denominator;
                this.generateNewValues(i,column);
            }
        }
    }
    private void generateNewValues(int row,int column){
        for(int i = column+1;i<this.systemSize;i++){
            this.coefficientMatrix[row][i] -= this.coefficientMatrix[row][column]*this.coefficientMatrix[this.pivotVector[column]][i];
        }
    }
    public void display(){
        System.out.println("This is the 2D Matrix");
        for(int i =0; i < this.systemSize; i++) {
            for (int j = 0; j < this.systemSize; j++) {
                System.out.print("\t" + this.coefficientMatrix[i][j]);
            }
            System.out.println();
        }
        System.out.println("This is the Pivot Vector");
        for(int i =0; i < this.systemSize; i++) {
            System.out.print("\t"+this.pivotVector[i]);
        }
    }
    public static void main(String[] args){
        //double arr[][]={{3,-13,9,3},{-6,4,1,-18},{6,-2,2,4},{0,-4,2,2}};
        double arr[][]={{1,-3,7},{2,4,-3},{-3,1,2}};
        GaussianElimination ge = new GaussianElimination(3,arr);
        //ge.initializeArray();
        ge.display();
        ge.scale();
        ge.display();
        ge.partialPivotColumn(0);
        ge.display();
        ge.generateMultiplierColumn(0);
        ge.display();
        ge.partialPivotColumn(1);
        ge.display();
        ge.generateMultiplierColumn(1);
        ge.display();


    }
}
