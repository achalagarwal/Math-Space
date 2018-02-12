package MathSpace;

import java.util.ArrayList;

public class Tokenizer {
    String sentence;
    boolean status; //for multi threading capabilities later
    ArrayList<String> tokens;
    Tokenizer(String str){
        sentence = str;
        status = false;
        tokens = new ArrayList<>();
        tokenize();
        status = true;
    }
    public void tokenize() {
        int count = 0;
        boolean flag = false;
        int j = 0;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < sentence.length(); i++) {
            char ch = sentence.charAt(i);
            if (ch == '(') {
                count++;
                for (j = i + 1; j < sentence.length(); j++) {
                    ch = sentence.charAt(j);
                    if (ch == '(')
                        count++;
                    else if (ch == ')' && count == 1) {
                        count = 0;
                        break;
                    }
                    else if (ch == ')' && count > 1)
                        count--;
                    buffer.append(sentence.charAt(j));
                }
                i = j;
                tokens.add(buffer.toString());
                buffer.setLength(0);
            } else if (ch != ' ') {
                for (j = i; j < sentence.length(); j++) {
                    ch = sentence.charAt(j);
                    if (ch != ' ')
                        buffer.append(ch);
                    else
                        break;
                }
                i = j;
                tokens.add(buffer.toString());
                buffer.setLength(0);
            }
        }
    }

}
