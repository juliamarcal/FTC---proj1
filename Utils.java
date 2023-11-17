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

}
