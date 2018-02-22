package MathSpace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static java.lang.Integer.max;
class PwithH{
    ParseTree p;
    int height;
    PwithH(ParseTree p, int h){
        this.p = p;
        this.height = h;
    }
}
public class ParseTree {
    ParseTree left;
    ParseTree right;
    ParseTree parent;
    Object value;
    int level;
    ParseTree(Object o){
        this.value  = o;
        this.left = null;
        this.right = null;
        this.parent = null;
        level = 0;
    }
    ParseTree(Object o, ParseTree parent) {
        this.value = o;
        this.left = null;
        this.right = null;
        this.parent = parent;
        if(parent==null)
            level = 0;
        else
            level = parent.level +1;
    }

    public ParseTree getLeft(){
       return this.left;
    }

    public ParseTree getRight() {
        return right;
    }

    public Object getValue() {
        return value;
    }
    public ParseTree getRoot(){
        ParseTree p = this;
        while(p.parent!=null)
            p = p.parent;
        return p;
    }
    public double getCoeffecient(Literal l) throws Exception{
        if(this.value instanceof Number)
            return 0.0;
        if(this.value instanceof Literal && ((Literal) this.value).equals(l)){
            return 1;
        }
        else if(this.value instanceof Operator) {
            if(Utility.isLinearOperator((Operator)this.value))
                return ((Operator) this.value).operate(this.left.getCoeffecient(l), this.right.getCoeffecient(l));
            else{
                if(this.left.getCoeffecient(l)==0)
                    return ((Operator) this.value).operate(this.left.getConstant(), this.right.getCoeffecient(l));
                else if(this.right.getCoeffecient(l)==0)
                    return ((Operator) this.value).operate(this.left.getCoeffecient(l), this.right.getConstant());
                else{
                    throw new Exception("This Expression might not be linear in "+ l.toString());
                }

            }
        }
        else if(this.value instanceof Expression)
            return ((Expression)this.value).p.getCoeffecient(l);
        else
            return 0.0;
    }
    public double getConstant(){
        if(this.value instanceof Literal && ((Literal) this.value).hasValue()){
            return ((Literal) this.value).getValue();
        }
        else if(this.value instanceof Operator)
        return  ((Operator) this.value).operate(this.left.getConstant(),this.right.getConstant());
        else if(this.value instanceof Expression)
            return ((Expression)this.value).p.getConstant();
        else
            return 0.0;
    }
    public boolean containsUnknowns(){
         if(this.value instanceof Literal){
            if(!((Literal) this.value).hasValue())
                return true;
        } //else
        if(this.left == null)
            if(this.right == null)
                return false;
            else
                return this.right.containsUnknowns();
        else
            return this.right==null?this.left.containsUnknowns():this.left.containsUnknowns()||this.right.containsUnknowns();
        //return (this.left.containsUnknowns()&&this.right.containsUnknowns());

    }
    public double getLiteralInstances(Literal l){
        double a = 0.0;
        if(this==null)
            return 0.0;
        if(this.value instanceof Literal)
            if(!((Literal) this.value).hasValue())
            if(((Literal) this.value).equals(l)){
            a = this.getCoeffecient();

        }
            if(this.left!=null)
            a += this.left.getLiteralInstances(l);
        if(this.right!=null)
            a+= this.right.getLiteralInstances(l);

        return a;

    }
    public void simplify(){
        ParseTree p = this;
        if(!p.containsUnknowns()){
            Number n = new Number(Expression.result(p));
            p.left = null;
            p.right = null;
            p.value = n;
        }
        else{
            if(p.left!=null)
                p.left.simplify();
            if(p.right!=null)
                p.right.simplify();
        }

    }
//    public double getNumberCoeffecient(){
//
//    }
    public double getNumberInstances(){
        this.simplify();
        double a = 0.0;
        if(this==null)
            return 0.0;
        if(this.value instanceof Literal)
            if(((Literal) this.value).hasValue())
                    a = ((Literal) this.value).getValue();


        if(this.left!=null)
            a += this.left.getNumberInstances();
        if(this.right!=null)
            a+= this.right.getNumberInstances();

        return a;

    }
    public ParseTree getBrother(){
        if(this.parent!=null){
            if(this.parent.left == this)
                return this.parent.right;
            if(this.parent.right == this)
                return this.parent.left;
        }
        return null;
    }
    public double getCoeffecient(){
        double a = 1.0;
        ParseTree n = this;
        ParseTree p = this.parent;
        while(p!=null){
            if(p.value instanceof  Operator){
                if(((Operator) p.value).getOperator().equals("*")||((Operator) p.value).getOperator().equals("/")){
                    Expression.simplify(n.getBrother());
                    if(n.getBrother().value instanceof Number)
                    a = ((Operator) p.value).operate(a,((Number) n.getBrother().value).getValue());
                }
            }
            n = p;
            p = p.parent;
        }
        return a;
    }
    public static void printSpace(int n){
        for(int i = 0;i<n;i++)
            System.out.print(" ");
    }
    public void print(String[][] x) {
        int count[] = new int[2*((int)Math.pow(2,this.getHeight(0))+1)];
        for(int a:count)
            a=0;
        count[count.length/3-1] = 1;
        for(int i = 0;i<x.length;i++){
            int m = 0;
            for(int j = 0;j<count.length;j++){
                if(count[j]==0)
                    System.out.print(" ");
                if(count[j]==1){
                    System.out.print(x[i][m]);
                    count[j]=0;
                    if(Operator.isOperator(x[i][m++])){
                        count[j]=2;
                        count[j+1]=3;
                    }

                }
            }
            System.out.println();
            for(int j = 0;j<count.length;j++) {
                if(count[j]==0)
                    System.out.print(" ");
                if (count[j] == 2) {
                    count[j]=0;
                    System.out.print("/ ");
                    count[j-1]=1;
                }
                else if (count[j] == 3) {
                    System.out.print("\\");
                    count[j]=0;
                    count[j+1]=1;
                }
            }
            System.out.println();
        }
    }
    public String toString(){

        return this.value.toString();
    }
    public int getLeftOffset(){
        ParseTree p = this;
        int i = 0;
        while(p.left!=null) {
            i++;
            p = p.left;
        }
        return i+1;
    }
    public ArrayList<PwithH> printHelper(){
        ArrayList<PwithH> test = new ArrayList<>();
        this.stack(test, 0);
        int left = this.getLeftOffset();
        int count = 0;
        ArrayList<Integer> spots = new ArrayList<>();
        spots.add(left);
        Collections.sort(test, new Comparator<PwithH>() {
            @Override
            public int compare(PwithH o1, PwithH o2) {
                if (o1.height < o2.height)
                    return -1;
                else
                    return 1;
            }
        });
        return test;
//        ArrayList<Integer> above = new ArrayList<>();
//        int flag = 1;
//        int priority = test.get(test.size()-1).height;
//        int mark;
//        int j = 0;
//        int pos = left;
//        Utility.tabs(left);
//        while (priority>=0) {
//            j = 0;
//
//            int index = 0;
//            int lLeft=0;
//            int lRight;
//            for (; j < test.size(); j++) {
//                PwithH t = test.get(j);
//                if (t.height == priority) {
//                    mark = spots.get(0);
//                    spots.remove(0);
//                    Utility.tabs(mark-pos);
//                    if(index == 0){
//                        Utility.tabs(1);
//                        System.out.print(t.p.toString());
//                        spots.add(mark+2);
//                        index = 1;
//                        lLeft = pos;
//                    }
//                    else if(index == 1){
//                        System.out.print(t.p.toString());
//                        Utility.tabs(1);
//                        spots.add(mark+2);
//                        index = 0;
//                        above.add((pos+lLeft)/2);
//                        pos+=1;
//                    }
//                    //adding markers in the next line if Operator found
////                    if(t.p.value instanceof Operator){
////                        spots.add(mark-1);
////                        spots.add(mark+1);
////                    }
//                }
//                else if(t.height > priority)
//                    break;
//            }
//            pos = 0;
//            System.out.println("\n");
//            priority--;
//        }
//        System.out.println();
    }
    public void stack(ArrayList<PwithH> stack, int h){
        if(this.left!=null)
            this.left.stack(stack,h+1);
        if(this.right!=null)
            this.right.stack(stack,h+1);
        if(this.value instanceof Expression)
            ((Expression) this.value).p.stack(stack,h);
        else
        stack.add(new PwithH(this,h));
        //stack.add(this.right.toString());

        //this.left.stack(stack);
        //this.right.

    }
    public String[][] magic(String[][] str,int[] count){
        str[this.level][count[this.level]] = this.value.toString();
        count[this.level]++;
        if(this.left!=null)
            this.left.magic(str,count);
        if(this.right!=null)
            this.right.magic(str,count);
        return str;
    }
    public String[][] level(){
        int height = this.getHeight(0)+1;
        String[][] x = new String[height][(int)Math.pow(2,height-1)];
        int[] count = new int[height];
        x = this.magic(x,count);
        return x;
    }

    public int getHeight(int max){
        int a=max+1,b=max+1;
        if(this.left!=null)
           a=  this.left.getHeight(this.level);
        if(this.right!=null)
            b=this.right.getHeight(this.level);
        return max(a,b);
    }
    public static void main(String[] args) {
        String a = "3 + 3 * x + (4 - 3 * x + 7)";
        Expression expression = new Expression(a);
        //expression.p.simplify();
        int z  = expression.p.getHeight(0);
        String[][] x = new String[expression.p.getHeight(0)+1][(int)Math.pow(2,expression.p.getHeight(0))];
        int[] count = new int[expression.p.getHeight(0)+1];
        for(int e:count)
            e=0;
        x= expression.p.magic(x,count);
        expression.p.print(x);
        System.out.println("Done" + z);
    }
}
