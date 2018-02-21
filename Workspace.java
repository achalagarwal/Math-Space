package MathSpace;

import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
public class Workspace {
    ArrayList<Expression> expressions = new ArrayList<>();
    ArrayList<Equation> equations = new ArrayList<>();
    ArrayList<Literal> variables = new ArrayList<>();
    public void addVariables(Expression e){
        for(Literal l:e.literals){
            if(!variables.contains(l)){
                variables.add(l);
            }
        }
    }
    public static void main(String[] args) throws Exception {
        Workspace w = new Workspace();
        Scanner sc = new Scanner(System.in);
        while (true) {
            String abc = sc.nextLine();
            Expression prev;
            Expression current;
            StringTokenizer st = new StringTokenizer(new String(abc), "=");
            prev = new Expression(st.nextToken());
            w.expressions.add(prev);
            for(Literal l:prev.literals){
                l.addLiteral(w.variables);
            }
            while (st.hasMoreTokens()) {
                current = new Expression(st.nextToken());
                Equation tmp = new Equation(prev, current);
                tmp.solve(); // what is this error! debug!! -> Found Error, the assumption that an equation has one variable.
                w.equations.add(tmp);
                prev = current;
                w.expressions.add(prev);
                w.addVariables(prev);
            }
        }
    }
}
