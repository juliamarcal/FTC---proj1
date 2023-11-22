import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CYK_Modified {

   public static boolean cyk(List<List<String>> reversa, String sentence) {

        if (isPalavraAssociadaAoSimboloInicial(reversa, sentence)) {
            return true;
        }
        
        int sentenceSize = sentence.length();
        List<String>[][] table = new ArrayList[sentenceSize][sentenceSize];

        for (int j = 0; j < sentenceSize; j++) {
            table[0][j] = keysOfValue(reversa, String.valueOf(sentence.charAt(j)));
        }

        for (int i = 1; i < sentenceSize; i++) {
            for (int j = 0; j < (sentenceSize - i); j++) {
                List<String> conjuntoRegras = new ArrayList<>();
                int rows = i, columns = j;

                for (int k = 0; k < i; k++) {
                    List<String> regraEsquerda = table[k][j];
                    rows--;
                    columns++;
                    List<String> regraDireita = table[rows][columns];

                    insereCasoNaoExista(concatenaVerificaRegras(regraEsquerda, regraDireita,reversa), conjuntoRegras, reversa);
                }

                table[i][j] = conjuntoRegras;
            }
        }
        return table[sentenceSize - 1][0].contains("L");
    }

    private static boolean isPalavraAssociadaAoSimboloInicial(List<List<String>> reversa, String sentence) {
        for (List<String> rule : reversa) {
            if (rule.size() > 1 && rule.get(0).equals(sentence) && rule.get(1).equals("L")) {
                return true;
            }
        }
        return false;
    }

    private static void insereCasoNaoExista(List<String> aux, List<String> listFinal, List<List<String>> reversa) {
        for (String key : aux) {
            if (!listFinal.contains(key)) {
                listFinal.add(key);
            }
        }
    }

    private static List<String> concatenaVerificaRegras(List<String> regraEsquerda, List<String> regraDireita, List<List<String>> reversa) {
        List<String> regrasExistentes = new ArrayList<>();

        if (!regraEsquerda.isEmpty() && !regraDireita.isEmpty()) {
            for (String regraEsq : regraEsquerda) {
                for (String regraDir : regraDireita) {
                    String regraFormada = regraEsq + regraDir;
                    List<String> aux = keysOfValue(reversa, regraFormada);
                    insereCasoNaoExista(aux, regrasExistentes, reversa);
                }
            }
        }

        return regrasExistentes;
    }

    private static List<String> keysOfValue(List<List<String>> reversa, String value) {
        List<String> keys = new ArrayList<>();

        for (List<String> rule : reversa) {
            if (rule.contains(value)){
                keys.add(rule.get(0));
            }
        }

        return keys;
    }

    public static List<List<String>> gerarRelacaoReversa(List<List<String>> gramatica) {
        List<List<String>> reversa = new ArrayList<>();

        for (List<String> regra : gramatica) {
            String simboloOrigem = regra.get(0);

            for (int i = 1; i < regra.size(); i++) {
                adicionarRegraReversa(reversa, regra.get(i), simboloOrigem);
            }
        }

        return reversa;
    }

    public static void adicionarRegraReversa(List<List<String>> reversa, String simboloDestino, String simboloOrigem) {
        Optional<List<String>> destinoEncontrado = reversa.stream()
                .filter(regra -> regra.get(0).equals(simboloDestino))
                .findFirst();

        // Se encontrarmos a linha, adicionamos simboloOrigem ao conjunto associado ao símboloDestino
        destinoEncontrado.ifPresent(regra -> regra.add(simboloOrigem));

        // Se não encontrarmos a linha, criamos uma nova linha para simboloDestino
        if (!destinoEncontrado.isPresent()) {
            List<String> novaRegra = new ArrayList<>();
            novaRegra.add(simboloDestino);
            novaRegra.add(simboloOrigem);
            reversa.add(novaRegra);
        }
    }
}
