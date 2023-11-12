import java.util.ArrayList;
import java.util.List;

public class CYK {
    static Utils utils = new Utils();

    public boolean cykParse(List<List<String>> grammar, String word) {
        int wordLen = word.length();
        int grammarLen = grammar.size();

        if (word.equals("#")) {
            return (grammar.get(0).contains("#"));
        }

        // Inicializa a tabela CYK
        String[][] table = new String[wordLen][wordLen];

        // fill main diagonal
        for(int i=0; i<wordLen; i++) { 
            for (List<String> variable : grammar) {
                if (table[i][i] == null) {
                    table[i][i] = "";
                }
                if (variable.contains(String.valueOf(word.charAt(i)))) {
                    if (table[i][i].length() > 0) {
                        table[i][i] += ",";
                    }
                    table[i][i] += variable.get(0);
                }
            }
        }
        utils.printMatrix(table);

        //fill the rest of the table
        /* 

        w -> word
        n -> length of w
        G -> grammar
        S -> start state of grammar

            For l = 2 to n:
                For i = 1 to n-l+1:
                    j = i+l-1
                        For k = i to j-1:
                        For each rule A -> BC: 
                        We check if (i, k) cell contains B and (k + 1, j) cell contains C:
                            If so, we put A in cell (i, j) of our table.
        */


        // check if table[0][wordLen-1] is filled
        return (table[0][wordLen-1] != null);
    }

    public List<String> doCombinations(String group1, String group2) {
        List<String> combinations = new ArrayList<>();

        String[] elements1 = group1.split(",");
        String[] elements2 = group2.split(",");

        for (String elem1 : elements1) {
            for (String elem2 : elements2) {
                combinations.add(elem1 + elem2);
            }
        }

        return combinations;
    }
}
