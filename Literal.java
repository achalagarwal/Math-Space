package MathSpace;

public class Literal {
    private String literal;
    private Double value;
    private boolean hasValue;
    Literal(String str){
        this.literal = str;
        this.hasValue= false;
    }
    Literal(Double a){
        this.value = a;
        this.hasValue = true;
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
}
