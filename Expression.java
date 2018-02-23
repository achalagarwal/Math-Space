package MathSpace;
import com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptor;
import org.apache.commons.math3.analysis.function.Exp;
import org.apache.commons.math3.linear.*;

import javax.rmi.CORBA.Util;
import javax.sound.sampled.Line;
import java.util.*;
public class Expression extends Token{ //equation has variables, numbers and operators
    //private Object contents[];
    Scanner sc = new Scanner(System.in);
    ArrayList<Literal> literals;
    private LinkedList<Object> contents = new LinkedList<>();
    //private boolean arr[]; //copy.length
    String e;
    boolean hasUnknowns;
    ParseTree p;
    Tokenizer t;
    ArrayList<Object> tokens;
    ArrayList<Operator> outerOps;
    boolean hasResult = false;
    double result = 0.0;
    Expression(String a){
        e = a;
//         if(a.charAt(0)=='(' && a.charAt(a.length()-1)==')')
//             a = a.substring(1,a.length()-1);
         t = new Tokenizer(a);
         tokens = new ArrayList<>();
         tokenify();
        outerOps = new ArrayList<>();
        updateOperators();
        literals = new ArrayList<Literal>();
        updateLiterals();
        linkify();
        getResult();

//clean your tokens and keep only subexpressions because all usages of tokens except the recursive call have an if token.getElement is Expression


//        literals = new ArrayList<Literal>();
//        String copy = a.replaceAll("\\s","");
//        this.e = copy;
//        p = new ParseTree(a,null);
//        generate(p);
//        //simplify(p);
//        getUnknowns();
//        // arr = new boolean[copy.length()];
    }

    public void recurseLiterals() {
        for(Object exp:tokens){
            if(exp instanceof Expression){
                for(int i = 0;i<((Expression) exp).literals.size();i++){
                    Literal l1= ((Expression) exp).literals.get(i);
                    for(int j =  0;j<this.literals.size();j++){
                        Literal l2 = this.literals.get(j);
                        if(l1.equals(l2)){
                            ((Expression) exp).literals.remove(i);
                            ((Expression) exp).literals.add(i,l2);
                        }
                    }
                }
            }
        }
        for(Literal l:literals){
            p.updateLiteral(l);
        }
    }

        public void tokenify (){
        for(Object o:t.tokens){
            Object temp = null;
            try {
                temp = Utility.getType(o);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            tokens.add(temp);
        }
    }
    public void getResult(){
        hasUnknowns = false;
        for(Literal l:literals){
            if(!l.hasValue()){
                hasUnknowns = true;
                break;
            }
        }
        if(!hasUnknowns) {
            for (Object o : tokens) {
                if (o instanceof Expression) {
                    if (!((Expression) o).hasResult) {
                        this.hasResult = false;
                        return;
                    }
                }
            }

            this.hasResult = true;
            result = result(p);
        }
        else
            this.hasResult = false;
        //parse and get result;
    }

    public void updateLiterals(){
        for(Object t:tokens) {
            if (t instanceof Expression){
                // this.literals.addAll(((Expression) t).literals);
                for(int j = 0;j<((Expression) t).literals.size();j++){
                    Literal il = ((Expression) t).literals.get(j);
                    boolean flag = true;
//                    for(Literal l:literals){
//                        if(il.equals(l))
//                            flag = false;
//                    }
                    for(int i = 0;i<literals.size();i++){
                        Literal l = literals.get(i);
                        if(il.equals(l)) {
                            flag = false;
                            ((Expression) t).literals.remove(j);
                            ((Expression) t).literals.add(j,l);
                            break;
                        }
                    }
                    if(flag)
                        literals.add(il);
                }
            }
        }
        for(Object t:tokens){
             if(t instanceof Literal){
                if(!(t instanceof Number)){
                    boolean flag= true;
                    for(Literal l:literals){
                        if(l.equals(t))
                            flag = false;
                    }
                    if(flag)
                    literals.add((Literal)t);
                }
            }
        }
        for(Object o:tokens){
            if(o instanceof Expression){
                for(int i = 0;i<((Expression) o).literals.size();i++){
                    Literal il = ((Expression) o).literals.get(i);
                    for(Literal l:literals){
                        if(il.equals(l)){
                            ((Expression) o).literals.remove(il);
                            ((Expression) o).literals.add(i,l);
                        }
                    }
                }
            }
        }
    }

    public void linkify(){
        ParseTree parray[] = new ParseTree[tokens.size()];
        for(Operator o:outerOps){
            int index = tokens.indexOf(o);
            p = new ParseTree(o);
            parray[index]=p;
            if(parray[index-1]!=null){
                p.left = parray[index-1].getRoot();
                parray[index-1].getRoot().parent = p;
            }
            else {
                p.left = new ParseTree(tokens.get(index - 1), p);
                parray[index - 1] = p.left;
            }
            if(parray[index+1]!=null){
                p.right = parray[index+1].getRoot();
                parray[index+1].getRoot().parent = p;
            }
            else {
                p.right = new ParseTree(tokens.get(index+1), p);
                parray[index+1] = p.right;
            }
        }
        if(p==null){
            p = new ParseTree(tokens.get(0));
        }

    }
    public void updateOperators(){

        for(Object t:tokens){
            if(t instanceof Operator)
                outerOps.add((Operator) t);
        }
        outerOps.sort(new OperatorComparator());
    }
    private boolean hasObject(int i) {
        if (contents.size() <= i)
            return false;
        else if (contents.get(i) == null)
            return false;
        else
            return true;
    }
    public double getCoeffecient(Literal l) {
        if(literals.contains(l)){
           // p.
        }
        return 0.0;
    }
    public static void simplify(ParseTree p){
        if(!p.containsUnknowns()){
            Number n = new Number(Expression.result(p));
            p.left = null;
            p.right = null;
            p.value = n;
        }
        else{
            if(p.left!=null)
            simplify(p.left);
            if(p.right!=null)
            simplify(p.right);
        }

    }

    @Override
    public String toString() {
        return this.e;
    }

    private static int parse(String c){
        double min = -1;
        int save = -1;
        int count = 0;
        for (int j = 0; j < c.length(); j++) {
            if(c.charAt(j)=='(')
                count++;
            else if(c.charAt(j)==')')
                count--;
            else if (count==0 && Operator.isOperator(c.charAt(j))) {

                    Operator o = new Operator(c.charAt(j));

                    if (min == -1) {
                        min = o.getPrecedence();
                        save = j;
                    } else if (o.getPrecedence() < min) {
                        min = o.getPrecedence();
                        save = j;
                    }

            }
        }
        return save;
    }
    public ArrayList<Literal> getUnknownLiterals(){
        ArrayList<Literal> list = this.literals;
        for(Literal l :list){
            if(l.hasValue())
                list.remove(l);
        }
        return list;
    }
    public boolean hasResult() {
        for(Literal l:literals){
            if(!l.hasValue())
                return false;
        }
        return true;
    }


    private void insertObject(int i, Object o) {
        if (i < contents.size())
            contents.add(i, o);
    }//at position i

    public void mergeNumber() {
        int i = 0;
        int flag = 0;
        while (contents.size() > i) {
            Object o = contents.get(i++);
            String temp = "";
            int pos = i - 1;
            while (o instanceof Integer || o.toString() == ".") {
                flag = 1;
                temp += o.toString();
                contents.remove(i - 1);
                if (contents.size() > i)
                    o = contents.get(i - 1);
            }
            if (flag == 1) {
                Integer a = Integer.valueOf(temp);
                contents.set(i - 1, a);
                flag = 0;
            }
        }
    }

    public boolean isExpression(String a) {
        boolean status = true;
        a = a.replaceAll("\\s", "");
        String copy = a;
        if (Literal.isLiteral(a)) {
            return true;
        }
        int count = 0;
        int arr[] = new int[a.length()]; //array storing indices of all occurrences
        int temp = 0;
        for (int i = 0; i < a.length(); i++) {
            temp += a.indexOf('+');
            if (a.indexOf('+') == -1) {
                break;
            }
            if (a.indexOf('+') != -1) {
                arr[count++] = temp;
                a = copy.substring(temp + 1);
                i = 0;
                temp++;
            }
        }
        //if(count>0) //x+1+3+y=5
        int index = 0;//of string -> copy
        for (int i = 0; i <= count; i++) {
            if (i == count)
                status = status && isExpression(copy.substring(index));
            else
                status = status && isExpression(copy.substring(index, arr[i]));
            index = arr[i] + 1;
        }

        return status;
    }

    public Literal getNextVariable() {
        Iterator Iterator = contents.iterator();
        while (Iterator.hasNext()) {
            Object o = Iterator.next();
            if (o instanceof Literal) {
                Literal l = Literal.class.cast(o);
                return l;
            }
        }
        return null;
    }
    public void generate( ParseTree n){

        if(n.value == null)
            return;
        String str = n.value.toString();
        int pos = parse(str);
        if(pos ==-1){
            if(Number.isNumber(str))
                n.value = new Number(Double.parseDouble(str));
            else if(Literal.isLiteral(str)) {
                n.value = new Literal(str);
                boolean flag=false;
                for(Literal l:literals) {
                    if (l.toString().equals(n.value.toString())) {
                        flag = true;
                        break;
                    }
                }
                   if(!flag)
                       literals.add((Literal) n.value);
                }


            return;
        }

        Object left,right;
        String str_left = str.substring(0,pos);
        String str_right = str.substring(pos+1);
        while(str_left.charAt(0)=='('&&str_left.charAt(str_left.length()-1)==')')
            str_left = str_left.substring(1,str_left.length()-1);
        while(str_right.charAt(0)=='('&&str_right.charAt(str_right.length()-1)==')')
            str_right = str_right.substring(1,str_right.length()-1);
        if(Operator.isOperator(str_left))
            left = new Operator(str_left);
        else if(Number.isNumber(str_left))
            left = new Number(Double.parseDouble(str_left));
        else if(Literal.isLiteral(str_left)) {
            boolean flag = false;
            left = new Literal(str_left);
            for(Literal l:literals){
                if(l.toString().equals(left.toString())) {
                    left = l;
                    flag = true;
                }
            }
            if(!flag)
                literals.add((Literal)left);
        }
        else
            left = str_left;
        if(Operator.isOperator(str_right))
            right = new Operator(str_right);
        else if(Number.isNumber(str_right))
            right = new Number(Double.parseDouble(str_right));
        else if(Literal.isLiteral(str_right)) {
            boolean flag = false;
            right = new Literal(str_right);
            for(Literal l:literals){
                if(l.toString().equals(right.toString())) {
                    right = l;
                    flag = true;
                }
            }
            if(!flag)
                literals.add((Literal)right);
        }
         //   right = new Literal(str.substring(pos+1));
        else
            right =str_right;
        n.left = new ParseTree(left,n);
        n.right = new ParseTree(right,n);
        n.value= new Operator(str.charAt(pos)) ;
        if(!(left instanceof Literal)&&!(left instanceof Number))
        generate(n.left);
        if(!(right instanceof Literal)&&!(right instanceof Number))
        generate(n.right);
        return;
    }

    public void getUnknowns() {
        for(Literal l:literals){
            if(!l.hasValue()){
                System.out.println("Enter the value of " + l.toString() +" enter a non number preferably - to keep variable");
                try {
                    l.setValue(sc.nextDouble());
                }
                catch(InputMismatchException e){
                    System.out.println(l.toString() + " is still a variable");
                    sc.nextLine();
                }
            }
        }
    }
    public boolean hasOneUnknown(){
        boolean flag = false;
        for(Literal l:literals){
            if(!l.hasValue()&&flag) {
                return false;
            }
            else if(!l.hasValue())
                flag = true;

        }
        return flag;
    }

//    public double getLiteralCoeff(){ //assumes linear
//       if(p.value instanceof Operator)
//
//        }
//        return 0;
//    }
    public static double result(ParseTree p){ //assuming contains no unknowns
        double result = 0;

        if(p.value instanceof Operator)
            return ((Operator)p.value).operate(result(p.left),result(p.right));
        else if(p.value instanceof Literal){
//            if(!((Literal) p.value).hasValue()) {
//                System.out.println("Enter the value of " + p.value);
//                ((Literal) p.value).setValue(sc.nextDouble());
//            }
            return ((Literal) p.value).getValue();
        }
        else if(p.value instanceof Number)
            return ((Number) p.value).getValue();
        else if(p.value instanceof Expression){
            return ((Expression) p.value).result;
        }
        else return 0.0;
    }
    public static Object nextChain(Object str){
        return null; //equation
    }
    public static Object checkChain(Object str){
        return new Expression(str.toString());
    }

    public static void main(String[] args) throws Exception {
        // Scanner sc = new Scanner(System.in);
        String a = "(x + z * 7) - (3 * (x - 1)) = 5";
        Object f = Operator.checkChain(a);
        String a2 = "(2 * 4 - 7) * (3 + (4 - z)) + (3 * (z - 6)) - 1 = 7 * (x - 2 + z)";
        Object f2 = Operator.checkChain(a2);
        //  Expression e = new Expression(a2);
        System.out.println("Done");
        assert f instanceof Equation;
        assert f2 instanceof Equation;
        ArrayList<Equation> eqs = new ArrayList<Equation>();
        eqs.add((Equation) f);
        eqs.add((Equation) f2);
        RealMatrix rm = Equation.coeffecients(eqs);
        System.out.println("aja");
        double abc=0,def=0;
        double[][] matrixData = new double[2][2];
        int i = 0;
        for(Equation e:eqs){
            //abc = e.left.p.printHelper();
            Object arr[] =  e.vars.toArray();
            Literal l = (Literal)arr[0];
            abc = e.left.p.getCoeffecient(l);
            System.out.println("\n\n");
            def = e.right.p.getCoeffecient(l);
            matrixData[i][0] = abc-def;
             l = (Literal)arr[1];
            abc = e.left.p.getCoeffecient(l);
            System.out.println("\n\n");
            def = e.right.p.getCoeffecient(l);
            matrixData[i][1] = abc-def;
            i++;
        }

        System.out.println(abc);
        System.out.println(def);
        //assume 2 equations 2 unknowns

        RealMatrix m = MatrixUtils.createRealMatrix(matrixData);
        //RealMatrix coefficients = new Array2DRowRealMatrix(new double[][] { { 2, 3, -2 }, { -1, 7, 6 }, { 4, -3, -5 } }, false);
        DecompositionSolver solver = new LUDecomposition(m).getSolver();
        double[] constant = new double[2];
        constant[0] = -eqs.get(0).left.p.getConstant() + eqs.get(0).right.p.getConstant();
        constant[1] = -eqs.get(1).left.p.getConstant() + eqs.get(1).right.p.getConstant();
        RealVector constants = new ArrayRealVector(constant, false);
        RealVector solution = solver.solve(constants);
        System.out.println(solution);
        /*
            this is test code for parse tree print
         */

        //LinearGroup lg = new LinearGroup(eqs);
        Set set = Utility.literalUnify(eqs.get(0),eqs.get(1));
        System.out.println("a");
    }
//    public static void tester(ArrayList<Object> stack,int pos){
//        int buffer = 0;
//        ArrayList<Object> tetet = new ArrayList<>();
//        tetet.add(stack.get(pos));
//        int i=0;
//        if(stack.get(pos) instanceof Operator){
//            i = pos;
//            tetet.add(stack.get(i-1));
//
//        }
//        for(;i>=0;i--){
//            if(stack.get(i) instanceof Literal)
//                buffer--;
//            if(buffer == -1){
//                stack.add(stack.get(i));
//            }
//            if(stack.get(i) instanceof Operator)
//                buffer++;
//        }
//
//    }

//    public static void main(String[] args){
//        Scanner sc = new Scanner(System.in);
//        String a = sc.nextLine();
//        a = a.replaceAll("\\s", "");
//        Expression e = new Expression(a);
//
////        a += " ";
////        //System.out.println(e.isExpression(a));
////        int index = 0;
////        String temp = "";
////        for (int i = 0; i < a.length(); i++) {
////            if (a.charAt(i) == ' ') {
////                temp = a.substring(index, i);
////                index = i + 1;
////                if (Literal.isLiteral(temp)) {
////                    Literal l = new Literal(temp);
////                    e.contents.add(l);
////                } else if (Operator.isOperator(temp)) {
////                    Operator o = new Operator(temp);
////                    e.contents.add(o);
////                } else if (Number.isNumber(temp)) {
////                    Number n = new Number(Double.parseDouble(temp));
////                    e.contents.add(n);
////                } else
////                    System.out.println("What is This? " + temp);
////                temp = "";
////            }
////            temp += a.charAt(i);
////        }
////
////        for (int i = 0; i < e.contents.size(); i++) {
////            Literal literal;
////            if (e.contents.get(i) instanceof Literal) {
////                literal = (Literal) e.contents.get(i);
////                if (!literal.hasValue()) {
////                    System.out.println("we need the value of " + e.contents.get(i).toString());
////                    literal.setValue(sc.nextDouble());
////                }
////            } else
////                System.out.println(e.contents.get(i));
////
////        }
////        int p = -1;
////        int i = 1;
////        while (true) {
////            if (e.contents.get(i) instanceof Operator) {
////                Operator o = (Operator) e.contents.get(i);
////                if (o.getPrecedence() >= p) {
////                    if ((e.contents.get(i + 2) != null && e.contents.get(i + 2) instanceof Operator && ((Operator) e.contents.get(i + 2)).getPrecedence() <= o.getPrecedence()) || e.contents.get(i + 2) == null) {//replace with get next operator
////                        Number n = new Number(((Operator) e.contents.get(i)).operate(((Literal) e.contents.get(i - 1)), ((Literal) e.contents.get(i + 1))));
////                        e.contents.set(i, n);
////                        e.contents.remove(i - 1);
////                        e.contents.remove(i);
////                        if (e.contents.get(i - 1) instanceof Operator) {
////                            i = i - 1;
////                        }
////
////                    }
////                }
////            }
////            break;
////        }
////        System.out.println(e.contents.get(0));
//        //*****************
//        //FIX THIS SEMANTIC / PRAGMATIC
////        double result=0;
////        for(int i = 0;i<e.contents.size();i++){
////            if(e.contents.get(i) instanceof Operator){
////                result += ((Operator) e.contents.get(i)).operate(((Literal)e.contents.get(i-1)).getValue(),((Literal)e.contents.get(i+1)).getValue());
////            }
////        }
//
//        ParseTree start = new ParseTree(e.e,null);
//        e.generate(start);
//        e.p = start;
//        e.simplify(start);
//        //final double result = e.result(start);
//       // System.out.println(result);
////        System.out.println(result);
//        //a + b * c + d = 12
//        //Double d = new Double(((Operator)e.contents.get(3)).operate(((Literal)e.contents.get(2)).getValue(),((Literal)e.contents.get(4)).getValue()));
//        //String copy = a.replaceAll("\\s", "");
//
//
//    }
}

