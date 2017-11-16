package MathSpace;

public class Operator {
    private String operation;
    double precedence; //higher the stronger
    public String getOperator(){
        return operation;

    }
    Operator(String operation){
        this.operation = operation;
        this.setPrecedence();
    }
    Operator(char operation){
        this.operation = Character.toString(operation);
        this.setPrecedence();
    }
    public void setPrecedence(double precedence) {
        this.precedence = precedence;
    }
    public void setPrecedence(){
        if(this.operation.equals("+"))
            this.precedence = 1;
        if(this.operation .equals("-"))
            this.precedence = 0;
        if(this.operation.equals("*"))
            this.precedence = 4;
        if(this.operation.equals("/"))
            this.precedence = 3.5;
        if(this.operation.equals("^"))
            this.precedence = 5;
        if(this.operation.equals("%"))
            this.precedence = 3;

    }
    public double getPrecedence() {
        return this.precedence;
    }
    public void setPrecedence(Operator higher, Operator lower){
        if(higher.precedence<lower.precedence)
            return;
        else this.precedence = (higher.precedence+lower.precedence)/2.0;
    }
    public static boolean isOperator(String op) {
        //initially just assume that its a length one string and hence access just the first index
        char ch = op.charAt(0);
        if(ch=='+'||ch=='-'||ch=='*'||ch=='/'||ch=='%'||ch=='E')
            return true;
        else
            return false;
    }
    public Operator reverse(){
        if(this.operation.equals("*"))
            return new Operator("/");
        else if(this.operation.equals("/"))
            return new Operator("*");
        else if(this.operation.equals("+"))
            return new Operator("-");
        else if(this.operation.equals("-"))
            return new Operator("+");
        else
            return this;
    }

    @Override
    public String toString() {
        return this.operation;
    }

    public static boolean isOperator(char ch) {
        //initially just assume that its a length one string and hence access just the first index
        if(ch=='+'||ch=='-'||ch=='*'||ch=='/'||ch=='%'||ch=='E')
            return true;
        else
            return false;
    }
//    public double operate(double lhs, double rhs){
//        if(this.operation.equals("+"))
//            return lhs+rhs;
//        else if(this.operation.equals("-"))
//            return lhs-rhs;
//        else if(this.operation.equals("*"))
//            return lhs*rhs;
//        else if(this.operation.equals("/"))
//            return lhs/rhs;
//        else if(this.operation.equals("^"))
//            return Math.pow(lhs,rhs);
//        else if(this.operation.equals("%"))
//            return lhs%rhs;
//
//        else
//            return 0.0;
//    }
    public double operate(double lhs, double rhs){
        if(this.operation.equals("+"))
            return lhs+rhs;
        else if(this.operation.equals("-"))
            return lhs-rhs;
        else if(this.operation.equals("*"))
            return lhs*rhs;
        else if(this.operation.equals("/")) {
            if(rhs!=0)
            return lhs/rhs;
        }
        else if(this.operation.equals("^"))
            return Math.pow(lhs,rhs);
        else if(this.operation.equals("%"))
            return lhs%rhs;
        else if(this.operation.equals("E"))
            return lhs*Math.pow(10,rhs);
        return 0.0;//default return -> needed for else case of divide by zero
    }
    public double operate(Literal lhs, Literal rhs){
        if(this.operation.equals("+"))
            return Literal.add(lhs,rhs);
        else if(this.operation.equals("-"))
            return Literal.subtract(lhs,rhs);
        else if(this.operation.equals("*"))
            return Literal.multiply(lhs,rhs);
        else if(this.operation.equals("/"))
            return Literal.divide(lhs,rhs);
        else if(this.operation.equals("^"))
            return Literal.exponent(lhs,rhs);
        else if(this.operation.equals("%"))
            return Literal.mod(lhs,rhs);
        else
            return 0.0;
    }
    public boolean comesBefore(Operator o){
        return this.precedence > o.precedence;
    }
}
