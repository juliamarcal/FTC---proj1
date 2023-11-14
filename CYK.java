import java.util.ArrayList;
import java.util.List;

public class CYK {
    private List<List<String>> regras;

    public CYK(List<List<String>> regras) {
        this.regras = regras;
    }

    public boolean cykParse(List<List<String>> regras, String palavra) {
        int tamanho = palavra.length();
        List<String>[][] matrizDeRegras = new ArrayList[tamanho][tamanho];

        for (int j = 0; j < tamanho; j++) {
            matrizDeRegras[0][j] = keysOfValue(String.valueOf(palavra.charAt(j)));
        }

        for (int i = 1; i < tamanho; i++) {
            for (int j = 0; j < (tamanho - i); j++) {
                List<String> conjuntoRegras = new ArrayList<String>();
                int linhaAtual = i, colunaAtual = j;

                for (int k = 0; k < i; k++) {
                    List<String> regraEsquerda = matrizDeRegras[k][j];
                    linhaAtual--;
                    colunaAtual++;
                    List<String> regraDireita = matrizDeRegras[linhaAtual][colunaAtual];

                    insereCasoNaoExista(concatenaVerificaRegras(regraEsquerda, regraDireita), conjuntoRegras);
                }

                matrizDeRegras[i][j] = conjuntoRegras;
            }
        }

        return matrizDeRegras[tamanho - 1][0].contains("L");
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

        for (List<String> rule : regras) {
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