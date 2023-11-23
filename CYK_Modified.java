import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CYK_Modified {
    private static List<List<String>> elements;
    static Utils utils = new Utils();

    public CYK_Modified(List<List<String>> elements) {
        this.elements = elements;
    }

    public static List<List<String>> gerarRelacaoReversa(List<List<String>> gramatica) {
        List<List<String>> reversa = new ArrayList<>();
        for (List<String> rule : gramatica) {
            for(int i=1; i<rule.size(); i++) {
                if (rule.get(i).length() == 1) {
                    reversa.add(new ArrayList<>());
                    reversa.get(reversa.size()-1).add(rule.get(i));
                    reversa.get(reversa.size()-1).add(rule.get(0));
                }
            }
        }

        System.out.println("Ug da gramática: "+reversa);

        return combine(reversa);
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


    public static List<List<String>> combine(List<List<String>> inputList) {
        List<List<String>> reachableLists = new ArrayList<>();
    
        for (List<String> edge : inputList) {
            String source = edge.get(0);
            String destination = edge.get(1);
    
            List<String> reachableNodes = new ArrayList<>();
            Set<String> visited = new HashSet<>();  // Cria um novo conjunto visited para cada chamada
            dfsRecursive(inputList, source, visited, reachableNodes);
            reachableLists.add(reachableNodes);
        }
    
        return reachableLists;
    }

    private static void dfsRecursive(List<List<String>> inputList, String currentNode,
        Set<String> visited, List<String> reachableNodes) {
        visited.add(currentNode);
        reachableNodes.add(currentNode);

        for (List<String> edge : inputList) {
            String source = edge.get(0);
            String destination = edge.get(1);

            if (source.equals(currentNode) && !visited.contains(destination)) {
                dfsRecursive(inputList, destination, visited, reachableNodes);
            }
        }
    }

    // funções para fazer o cky modificado
    
    public static boolean cyk(List<List<String>> reversa, String sentence) {

        /* if (isPalavraAssociadaAoSimboloInicial(reversa, sentence)) {
            return true;
        } */
        
        int sentenceSize = sentence.length();
        List<String>[][] table = new ArrayList[sentenceSize][sentenceSize];

        // diagonal principal
        for (int j = 0; j < sentenceSize; j++) {
            // letra + todos os elementos do reverça que começam com letra
            // ex. {a} + {E, T, F}
            /* table[0][j] = keysOfValue(reversa, String.valueOf(sentence.charAt(j))); */
            for (List<String> rev : reversa) {
                if(rev.get(0).equals(String.valueOf(sentence.charAt(j)))) {
                    table[0][j] = rev;
                }
            }
            if (table[0][j] == null) {
                table[0][j] = new ArrayList<>();
                table[0][j].add(String.valueOf(sentence.charAt(j)));
            }
        }

        // preenche o resto da tabela
        for (int i = 1; i < sentenceSize; i++) {
            for (int j = 0; j < (sentenceSize - i); j++) {
                List<String> conjuntoRegras = new ArrayList<>();
                int rows = i, columns = j;

                for (int k = 0; k < i; k++) {
                    List<String> regraEsquerda = table[k][j];
                    rows--;
                    columns++;
                    List<String> regraDireita = table[rows][columns];

                    insereCasoNaoExista(concatenaVerificaRegras(regraEsquerda, regraDireita, reversa), conjuntoRegras, reversa);
                }

                table[i][j] = conjuntoRegras;
            }
        }
        return table[sentenceSize-1][0].contains(utils.getFirstGrammarItem());
    }

    private static boolean isPalavraAssociadaAoSimboloInicial(List<List<String>> reversa, String sentence) {
        for (List<String> rule : reversa) {
            if (rule.size() > 1 && rule.get(0).equals(sentence) && rule.get(1).equals(utils.getFirstGrammarItem())) {
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
                    List<String> keys = keysOfValue(regraFormada);
                    List<String> aux = new ArrayList<>();
                    for (String key : keys) {
                        for (List<String> rev : reversa) {
                            if(rev.get(0).equals(key)) {
                                for (String element : rev) {
                                    aux.add(element);
                                }
                            }
                        }
                        if (!aux.contains(key)) {
                            aux.add(key);
                        }
                    }
                    insereCasoNaoExista(aux, regrasExistentes, reversa);
                }
            }
        }

        return regrasExistentes;
    }

    private static List<String> keysOfValue(String value) {
        List<String> keys = new ArrayList<String>();

        for (List<String> rule : elements) {
            if (rule.contains(value)) {
                keys.add(rule.get(0));
            }
        }

        return keys;
    }


}