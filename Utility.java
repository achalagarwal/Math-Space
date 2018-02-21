package MathSpace;

import java.util.ArrayList;

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