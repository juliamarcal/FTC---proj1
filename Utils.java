import java.util.ArrayList;
import java.util.List;

public class Utils {
    static Utils utils = new Utils();
    static Archive archive = new Archive();

    public int getRuleIndex(String value, List<List<String>> elements) {
        int index = 0;
        for (int i=0; i< elements.size(); i++) {
            if(elements.get(i).get(0).equals(value)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public boolean isUpperCase(String str) {
        return str.equals(str.toUpperCase());
    }

    public void printMatrix(String[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
    
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public String getFirstGrammarItem(){
        List<List<String>> elements;
        elements = archive.FindGlcInArquive(); 
        String firstVariable = elements.get(0).get(0);
        return firstVariable;
    }

    public boolean isValidGrammar(List<List<String>> grammar) {
        List<String> variables = new ArrayList<>();
        for (List<String> rule : grammar) {
            variables.add(rule.get(0));
        }
        for (List<String> rule : grammar) {
            for(int i=1; i<rule.size(); i++) {
                String word = rule.get(i);
                for(int j=0; j<word.length(); j++) {
                    char letter = word.charAt(j);
                    if ((letter >= 'A' && letter <= 'Z') && !variables.contains(String.valueOf(letter))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
