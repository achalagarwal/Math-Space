package MathSpace;

import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;
import jdk.nashorn.internal.runtime.ECMAException;
import org.apache.commons.math3.analysis.function.Exp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

final class Utility{

    public static boolean isAlphabet(char token){
        if((token<=90&&token>=65)||(token>=97&&token<=122))
            return true;
        //else
        return false;

    }
    private static boolean isValidCharacter(char token){
        if(token=='_')
            return true;
        return false;
    }
    //validates strings beginning with an alphabet and containing alphabets or underscores thereafter
    public static boolean isValidName(String token){
        //empty string check
        if(token.length()==0)
            return false;
        int i = 0;
        //check first char
        if(!isAlphabet(token.charAt(i)))
            return false;
        //check subsequent characters
        for( ;i<token.length();i++){
            char ch = token.charAt(i);
            if(!(isAlphabet(ch)||isValidCharacter(ch)))
                return false;
        }
        return true;
    }
    public static Set literalUnify(Equation a, Equation b) throws Exception {
       return literalUnify(a.vars,b.vars);
    }
        public static void literalUnify(Expression a, Expression b) throws Exception {
        literalUnify(a.literals,b.literals);
    }
    public static Set literalUnify(Set<Literal> a, Set<Literal> b)throws Exception{
        Set<Literal> c = new HashSet<>();
        Iterator i = a.iterator();
        Iterator j ;
        ArrayList<String> t = new ArrayList<>();
        //do for both sets. create temp arrays for each set containing strings that were removed while creating new set. then reinsert
        while(i.hasNext()){
            Literal l = (Literal) i.next();
            j = b.iterator();
            while(j.hasNext()) {
                Literal l2 = (Literal) j.next();
                if (l2.equals(l)) {
                    if(l2.hasValue()){
                        if(l.hasValue()){
                            if(l.getValue()!=l2.getValue())
                                throw new Exception("Variables Value Mismatch");
                        }
                        else{
                            l.setValue(l2.getValue());
                        }
                    }
                    j.remove();
                    t.add(l.toString());
                    break;
                }
            }
            c.add(l);
        }
        j = b.iterator();
        while(j.hasNext()) {
            c.add((Literal)j.next());
        }
        i = c.iterator();
        for(String str:t){
            for(Literal l:c){
                if(l.toString().equals(str)){
                    b.add(l);
                    break;
                }
            }
        }
        return c;
        }
    public static void literalUnify(ArrayList<Literal> list1, ArrayList<Literal> list2) throws Exception {
        for(int j = 0;j<list1.size();j++){
            Literal l = list1.get(j);
            for(int i = 0;i<list2.size();i++){
                Literal l2 = list2.get(i);
                if(l.equals(l2)){
                    if(l2.hasValue()&&l.hasValue()){
                        if(l2.getValue()!=l.getValue())
                            throw new Exception("Value Mismatch Exception");
                        else {
                            list2.remove(i);
                            list2.add(i,l);
                            break;
                        }
                    }
                    if(l2.hasValue()){
                        list1.remove(j);
                        list1.add(j,l2);
                    }
                    else {
                    //Literal t = l2.hasValue() ? l : l2;
                        list2.remove(i);
                        list2.add(i, l);
                    }
                }
            }
        }
    }
    public static boolean isValidOperator(char token){
        if(token=='-'||token=='+'||token=='/'||token=='*'||token=='%'||token=='^')
            return true;
        return false;
    }
    public static boolean isValidBrace(char token){
        if(token == '('||token ==')')
            return true;
        //else
        return false;
    }
    public static boolean isLinearOperator(Operator op){
        if(op.getOperator().equals("+")||op.getOperator().equals("-")){
            return true;
        }
        else return false;
    }
    public static void tabs(int n){
        while(n-->0)
            System.out.print("\t");
    }
    public static boolean isNumber(char token){
        if(token<=57&&token>=48)
            return true;
        //else
        return false;
    }
    public static boolean isNumber(String token) {
        boolean dotFlag = false;
        token = token.trim();
        //check empty string
        if (token.length() == 0)
            return false;
        for (int i = 0; i < token.length(); i++) {
            if (token.charAt(i) == '.') {
                if (dotFlag)
                    return false;
                dotFlag = true;
            } else if (!isNumber(token.charAt(i)))
                return false;
        }
        return true;
    }
    public static Object getType(Object token) throws Exception{
        return Operator.checkChain(token);
    }


    //else
}