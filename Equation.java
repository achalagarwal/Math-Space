package MathSpace;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Equation {
    Expression left;
    Expression right;
    Equation(Expression l, Expression r){
        this.left = l;
        this.right = r;

    }
    public static boolean isEquation(String a){
        int l = a.lastIndexOf('=');
        int f = a.indexOf('=');
        if(l==f && l!=-1)
            return true;
        else
            return false;

    }
    public static RealMatrix coeffecients(ArrayList<Equation> eqList){ //takes a linear equation and transforms
        //int size = eqList.size();
        ArrayList<Literal> all = new ArrayList<>();
       // double[][] arr = new double[eqList.size()][];
        for(Equation e:eqList){
            for(Literal l:e.left.literals){
                if(!all.contains(l)&&!l.hasValue())
                    all.add(l);
            }
            for(Literal l:e.right.literals){
                if(!all.contains(l)&&!l.hasValue())
                    all.add(l);
            }
        }
        double[][] arr = new double[eqList.size()][all.size()];

        int eqCount = 0;
        for(Equation e:eqList){
            int varCount = 0;
            for(Literal l:all) {
                arr[eqCount][varCount] = e.left.p.getLiteralInstances(l);
                arr[eqCount][varCount++] -=e.right.p.getLiteralInstances(l);
            }
                eqCount++;

        }
        RealMatrix coefficients = new Array2DRowRealMatrix(arr, false);
        return coefficients;
    }
    public  double getConstant(){
        double a = 0.0;
        double l = this.left.p.getNumberInstances();
        double r = this.right.p.getNumberInstances();
        a = r-l;
        return a;
    }


    public boolean solve(){ //returns the character for which the equation was solved, returns null for no solves. Could implement it with a boolean : true for solved. or int index of variable (with -1 for not solved)
        if (this.left.hasOneUnknown()&&!this.right.p.containsUnknowns()){
            return this.solves();
        }
        else{
            return(new Equation(this.right,this.left).solves());
        }
    }
    private boolean solves(){
        ParseTree p = this.left.p;
        ParseTree left = this.left.p.left;
        ParseTree right = this.left.p.right;
        while(p!=null) {
            if (p.value instanceof Operator) {
                ParseTree n = new ParseTree(((Operator) p.value).reverse(),p);
                if(p.left.containsUnknowns()&&!p.right.containsUnknowns()) { //p.left contains exactly 1 unknown
                    n.left = this.right.p;
                    n.right = p.right;
                    p = p.left;
                }
                else if(p.right.containsUnknowns()&&!p.left.containsUnknowns()){
                    n.left = this.right.p;
                    n.right = p.left;
                    p = p.right;

                }
                this.right.p = n;
            }
            else {
                assert p.value instanceof Literal;
                this.right.simplify(this.right.p);
                ((Literal) p.value).setValue(((Number)this.right.p.value).getValue());
                return true;
            }
        }
        return false;
    }
    public static Object nextChain(Object token){
        return Expression.checkChain(token);
    }
    public static Object checkChain(Object token){
        int l = token.toString().lastIndexOf('=');
        int f = token.toString().indexOf('=');
        if(l==f && l!=-1)
            return (new Equation(new Expression(token.toString().substring(0,l)),new Expression(token.toString().substring(l+1,token.toString().length()))));
        else
            return nextChain(token);
    }
    public static void main(String[] args){
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Enter the Sentence");
//        String a = sc.nextLine();
//        int i = a.indexOf('=');
//        Expression e1=new Expression(a.substring(0,i));
//        Expression e2 = new Expression(a.substring(i+1));
//        Equation eq = new Equation(e1,e2);
//        boolean woo = eq.solve();
//        System.out.println("opop");
        String a  = "(3+7)*x + 4*y";
        String b = "4*x + 3*y ";
        Equation e1 = new Equation(new Expression(a),new Expression("8"));
        Equation e2 = new Equation(new Expression(b),new Expression("9"));
        ArrayList<Equation> z= new ArrayList<>();
        z.add(e1);
        z.add(e2);
        RealMatrix r = coeffecients(z);
        double t = e1.getConstant();
        double u = e2.getConstant();
        System.out.println(coeffecients(z));
    }
}
