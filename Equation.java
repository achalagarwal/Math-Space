package MathSpace;

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
                ParseTree n = new ParseTree(((Operator) p.value).reverse());
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
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the Sentence");
        String a = sc.nextLine();
        int i = a.indexOf('=');
        Expression e1=new Expression(a.substring(0,i));
        Expression e2 = new Expression(a.substring(i+1));
        Equation eq = new Equation(e1,e2);
        boolean woo = eq.solve();
        System.out.println("opop");
    }
}
