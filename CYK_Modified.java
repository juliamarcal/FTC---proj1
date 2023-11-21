import java.util.*;

public class CYK_Modified {
    static Form2NF form2nf = new Form2NF();
    static Archive archive = new Archive();

    public Map<String, Set<String>> identifyTerminalsAndNonterminals(List<List<String>> rules, String startSymbol) {
        List<String> nonterminals = new ArrayList<>();
        List<String> terminals = new ArrayList<>();

        for (List<String> ruleList : rules) {
            String leftHandSide = ruleList.get(0);

            // Adicionar o lado esquerdo aos não-terminais
            if (!nonterminals.contains(leftHandSide)) {
                nonterminals.add(leftHandSide);
            }

            // Adicionar o lado direito aos terminais
            for (int i = 1; i < ruleList.size(); i++) {
                for (char symbol : ruleList.get(i).toCharArray()) {
                    String symbolStr = String.valueOf(symbol);
                    if (!Character.isUpperCase(symbol) && !terminals.contains(symbolStr)) {
                        terminals.add(symbolStr);
                    }
                }
            }
        }

        Map<String, Set<String>> inverseUnitRelation = buildInverseUnitRelation(rules, nonterminals, terminals);

        // Imprimir relações inversas
        System.out.println("Relação Inversa:");
        for (Map.Entry<String, Set<String>> entry : inverseUnitRelation.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        graphDFS(nonterminals);

        return inverseUnitRelation;
    }

    public Map<String, Set<String>> buildInverseUnitRelation(List<List<String>> rules, List<String> nonterminals, List<String> terminals) {
        Map<String, Set<String>> inverseUnitRelation = new HashMap<>();

        // Construir a relação de unidade inversa com base nas produções da gramática
        for (List<String> ruleList : rules) {
            String leftHandSide = ruleList.get(0);

            // Verificar se o símbolo no lado direito é não terminal
            for (int i = 1; i < ruleList.size(); i++) {
                String symbol = ruleList.get(i);

                // Adicionar à relação de unidade inversa
                if (!inverseUnitRelation.containsKey(symbol)) {
                    inverseUnitRelation.put(symbol, new HashSet<>());
                }

                inverseUnitRelation.get(symbol).add(leftHandSide);
            }
        }
        return inverseUnitRelation;
    }

    public static void graphDFS(List<String> nonterminals) {
        Map<String, List<String>> graph = new HashMap<>();

        for (String nonTerminal : nonterminals) {
            graph.put(nonTerminal, new ArrayList<>());
        }

        Map<String, Set<String>> transitiveClosure = new HashMap<>();
        for (String vertex : graph.keySet()) {
            Set<String> closure = new HashSet<>();
            dfs(graph, vertex, closure);
            transitiveClosure.put(vertex, closure);
        }

        // Imprimir fechos transitivos
        System.out.println("\nFecho transitivo:");
        for (Map.Entry<String, Set<String>> entry : transitiveClosure.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    private static void dfs(Map<String, List<String>> graph, String start, Set<String> closure) {
        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        stack.push(start);

        while (!stack.isEmpty()) {
            String current = stack.pop();

            if (!visited.contains(current)) {
                visited.add(current);
                closure.add(current);

                for (String neighbor : graph.get(current)) {
                    stack.push(neighbor);
                }
            }
        }
    }

    public static void main(String[] args) {
        CYK_Modified cykModified = new CYK_Modified();
        List<List<String>> elements;
        elements = archive.FindGlcInArquive();

        List<List<String>> gramatica2NF = form2nf.To2NF(elements);

        String seuSimboloInicial = "L";

        // Chamar o método para identificar terminais, não-terminais e calcular os fechos transitivos
        Map<String, Set<String>> result = cykModified.identifyTerminalsAndNonterminals(gramatica2NF, seuSimboloInicial);
        
        for (Map.Entry<String, Set<String>> entry : result.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}