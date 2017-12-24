package MathSpace;

public class LevelTree extends ParseTree {
    int level;
    LevelTree(Object o, LevelTree parent) {
        super(o, parent);
        level = parent.level+1;
    }
}
