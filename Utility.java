package MathSpace;
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
    public static Object getType(Object token){
        return Operator.checkChain(token);
    }


    //else
}