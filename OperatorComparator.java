package MathSpace;

import java.util.Comparator;

public class OperatorComparator implements Comparator<Operator> {
   public int compare(Operator a, Operator b){
        if(a.precedence<b.precedence)
            return -1;
        else
            return 1;

    }
}
