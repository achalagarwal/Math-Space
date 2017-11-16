package MathSpace;

public class ParseTree {
    ParseTree left;
    ParseTree right;
    Object value;

    ParseTree(Object o) {
        this.value = o;
        this.left = null;
        this.right = null;
    }

    public ParseTree getLeft(){
       return this.left;
    }

    public ParseTree getRight() {
        return right;
    }

    public Object getValue() {
        return value;
    }

    public boolean containsUnknowns(){
         if(this.value instanceof Literal){
            if(!((Literal) this.value).hasValue())
                return true;
        } //else
        if(this.left == null)
            if(this.right == null)
                return false;
            else
                return this.right.containsUnknowns();
        else
            return this.right==null?this.left.containsUnknowns():this.left.containsUnknowns()||this.right.containsUnknowns();
        //return (this.left.containsUnknowns()&&this.right.containsUnknowns());
    }
}