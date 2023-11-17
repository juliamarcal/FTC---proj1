import java.util.ArrayList;
import java.util.List;

public class CYK {
    private List<List<String>> elements;
    static Utils utils = new Utils();

    public CYK(List<List<String>> elements) {
        this.elements = elements;
    }

    public boolean cykParse(List<List<String>> elements, String sentence) {
        int sentenceSize = sentence.length();
        List<String>[][] table = new ArrayList[sentenceSize][sentenceSize];

        for (int j = 0; j < sentenceSize; j++) {
            table[0][j] = keysOfValue(String.valueOf(sentence.charAt(j)));
        }

        for (int i = 1; i < sentenceSize; i++) {
            for (int j = 0; j < (sentenceSize - i); j++) {
                List<String> conjuntoRegras = new ArrayList<String>();
                int rows = i, columns = j;

                for (int k = 0; k < i; k++) {
                    List<String> regraEsquerda = table[k][j];
                    rows--;
                    columns++;
                    List<String> regraDireita = table[rows][columns];

                    insereCasoNaoExista(concatenaVerificaRegras(regraEsquerda, regraDireita), conjuntoRegras);
                }

                table[i][j] = conjuntoRegras;
            }
        }

        return table[sentenceSize - 1][0].contains(utils.getFirstGrammarItem());
    }

    private List<String> concatenaVerificaRegras(List<String> regraEsquerda, List<String> regraDireita) {
        List<String> regrasExistentes = new ArrayList<String>();

        if (!regraEsquerda.isEmpty() && !regraDireita.isEmpty()) {
            for (String regraEsq : regraEsquerda) {
                for (String regraDir : regraDireita) {
                    String regraFormada = regraEsq + regraDir;
                    List<String> aux = keysOfValue(regraFormada);
                    insereCasoNaoExista(aux, regrasExistentes);
                }
            }
        }

        return regrasExistentes;
    }

    private List<String> keysOfValue(String value) {
        List<String> keys = new ArrayList<String>();

        for (List<String> rule : elements) {
            if (rule.contains(value)) {
                keys.add(rule.get(0));
            }
        }

        return keys;
    }

    private void insereCasoNaoExista(List<String> aux, List<String> listFinal) {
        for (String key : aux) {
            if (!listFinal.contains(key)) {
                listFinal.add(key);
            }
        }
    }
    
}