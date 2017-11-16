package MathSpace;

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
    public static void main(String[] args) {
        Workspace w = new Workspace();
        Scanner sc = new Scanner(System.in);
        while (true) {
            String abc = sc.nextLine();
            Expression prev;
            Expression current;
            StringTokenizer st = new StringTokenizer(new String(abc), "=");
            prev = new Expression(st.nextToken());
            w.expressions.add(prev);
            w.addVariables(prev);
            while (st.hasMoreTokens()) {
                current = new Expression(st.nextToken());
                Equation tmp = new Equation(prev, current);
                w.equations.add(tmp);
                prev = current;
                w.expressions.add(prev);
                w.addVariables(prev);
            }
        }
    }
}
