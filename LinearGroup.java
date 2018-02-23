package MathSpace;

import org.apache.commons.math3.linear.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class LinearGroup extends Group {
    ArrayList<Equation> eqs;
    Set<Literal> vars;
    LinearGroup(ArrayList<Equation> eqs) {
        super(eqs);
        this.eqs = eqs;
        vars = getVars();
     //   if(vars.size() == eqs.size());

    }
    public int solve()throws Exception {
        vars = getVars();
        if (vars.size() == eqs.size()) {
            double[][] matrixData = new double[eqs.size()][eqs.size()];
            double[] constants = new double[eqs.size()];

            int i = 0;
            double l, r = 0;
            int eqId = 0;
            for (Equation e : eqs) {
                int litId = 0;
                l = e.left.p.getConstant();
                r = e.right.p.getConstant();
                //r-l as RHS
                constants[eqId] = r - l;
                for (Literal lit : vars) {
                    l = e.left.p.getCoeffecient(lit);
                    // System.out.println("\n\n");
                    r = e.right.p.getCoeffecient(lit);
                    matrixData[eqId][litId] = l - r;
                    //l = e.left.p.getCoeffecient(lit);
                    // System.out.println("\n\n");
                    // r = e.right.p.getCoeffecient(lit);
                    // matrixData[i][1] = l - r;
                    litId++;
                }
                eqId++;
            }


            RealMatrix m = MatrixUtils.createRealMatrix(matrixData);
            //RealMatrix coefficients = new Array2DRowRealMatrix(new double[][] { { 2, 3, -2 }, { -1, 7, 6 }, { 4, -3, -5 } }, false);
            DecompositionSolver solver = new LUDecomposition(m).getSolver();
            //double[] constant = new double[2];
            //constant[0] = -eqs.get(0).left.p.getConstant() + eqs.get(0).right.p.getConstant();
            //constant[1] = -eqs.get(1).left.p.getConstant() + eqs.get(1).right.p.getConstant();
            RealVector c = new ArrayRealVector(constants, false);
            RealVector solution = solver.solve(c);
            i = 0;
            for (Literal literal : vars) {
                literal.setValue(solution.getEntry(i++));
            }
            return 1;
        }
        if(vars.size() == 0)
            return 1;
        else
            return 0;
    }
    public Set<Literal> getVars(){
        Set<Literal> count = new HashSet<>();
        for(Equation eq:eqs){
            for(Literal l:eq.vars){
                if(!l.hasValue())
                    count.add(l);
            }
        }
        return count;
    }

}
