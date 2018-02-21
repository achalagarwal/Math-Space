package MathSpace;


import java.util.ArrayList;

public class Literal  {
    private String literal;
    private Double value;
    private boolean hasValue;
    Literal(String str){
        str = str.replaceAll("\\s","");
        this.literal = str;
        this.hasValue= false;
    }
    Literal(Double a){
        this.value = a;
        this.hasValue = true;
    }
    public  boolean addLiteral(ArrayList<Literal> list){
        boolean flag = true;
        for(Literal l:list){
            if(l.toString().equals(this.toString())){
                flag = false;
                break;
            }
        }
        if(flag) {
            list.add(this);
        }
        return flag;


    }
    @Override
    public String toString(){
        return this.literal;
    }
    public boolean equals(Literal l){
        if(this.literal.equals(l.literal))
            return true;
        else
            return false;
    }
    public static double add(Literal a, Literal b){
        return a.value.doubleValue()+b.value.doubleValue();

    }
    public static double multiply(Literal a, Literal b){
        return a.value.doubleValue()*b.value.doubleValue();

    }
    public static double subtract(Literal a, Literal b){
        return a.value.doubleValue()-b.value.doubleValue();

    }
    public static double divide(Literal a, Literal b){
        return a.value.doubleValue()/b.value.doubleValue();

    }
    public static double mod(Literal a, Literal b){
        return a.value.doubleValue()%b.value.doubleValue();

    }
    public static double exponent(Literal a, Literal b){
        return Math.pow(a.value.doubleValue(),b.value.doubleValue());

    }
    public boolean hasValue(){
        if(this.hasValue)
            return true;
        else
            return false;
    }
    public double getValue(){
        return this.value.doubleValue();
    }

    public static boolean isLiteral(String a){
//        if(a.length()>1)
//            return false;
//        else{
        a = a.replaceAll("\\s","");
            for(int i = 0;i<a.length();i++) {
                char ch = a.charAt(i);
                if(!(ch!='e'&&ch!='E'&&((ch>=65&&ch<=90)||(ch>=97&&ch<=122))))
                    return false;
            }
            return true;
    }
    public void setValue(double value) {
        this.value = new Double(value);
        this.hasValue = true;
    }

    public static Object nextChain(Object token) throws Exception{
        return Number.checkChain(token);
    }

    public static Object checkChain(Object token) throws Exception {
        String str = token.toString();
        if(isLiteral(str)) {
            Literal l = new Literal(str);
            return l;
        }
        else
            return nextChain(token);
    }
    public boolean equals(Object obj) {
        if(obj instanceof Literal){
            return ((Literal) obj).literal.equals(this.literal);
        }
        return (this == obj);
    }

}
