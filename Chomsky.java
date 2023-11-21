import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chomsky {
    static Utils utils = new Utils();

    public List<List<String>> ToFNC(List<List<String>> elements) {
        long tempoInicial = System.currentTimeMillis();
        System.out.println("======= Passo a passo - Gramática de Chomsky =======\n");
        System.out.println("Gramática original: " + elements);
        List<List<String>> newElements = new ArrayList<>();

        newElements = eliminateVoid(elements);
        System.out.println("Elimina lambda: " + newElements);

        newElements = eliminateUnitaryProductions(newElements);
        System.out.println("Substitui variáveis: " + newElements);
        
        newElements = doChomsky(newElements);
        System.out.println("Alteração para forma normal: " + newElements);

        if (acceptVoid(elements)) {
           newElements.get(0).add("#"); 
           System.out.println("Adiciona lambda a primeira regra se a gramatica aceita vazio: " + newElements);
        }
        
        long tempoFinal = System.currentTimeMillis();
        long tempoTotal = tempoFinal - tempoInicial;
        System.out.println("Tempo de execução: " + tempoTotal +" segundos\n");

        newElements = removeUnusedRules(newElements);
        System.out.println("Remoção de variaveis não ultilizadas: " + newElements);

        return newElements;
    }

    public boolean acceptVoid(List<List<String>> grammar) {
        String lambda = "#";

        for(int i=1; i< grammar.get(0).size(); i++) { // go trough all rules in element
            if (grammar.get(0).get(i).equals(lambda)) { // first rule contains lambda
                return true;

            } else if (grammar.get(0).get(i).length() == 1 && utils.isUpperCase(grammar.get(0).get(i))) { // first rule contains unitary element that contains lambda
                List<String> unitaryEl = grammar.get(utils.getRuleIndex(grammar.get(0).get(i), grammar));
                
                for(int j=1; j<unitaryEl.size(); j++) { // go trough unitaryEl to see if finds lambda
                    if (unitaryEl.get(j).equals(lambda)) { // first rule contains lambda
                        return true; 
                    }
                }
            }

        }

        return false;
    }

    public List<List<String>> eliminateVoid(List<List<String>> elements) {
        String lambda = "#";
        List<String> rulesWithLambda = new ArrayList<>();

        for (int i=0; i< elements.size(); i++) {
            boolean containsLambdaElement = false;
            for (String lambdaElement : rulesWithLambda) {
                if (elements.get(i).contains(lambdaElement)) {
                    containsLambdaElement = true;
                    break;
                }
            }

            if ((elements.get(i).contains(lambda) || containsLambdaElement) && !rulesWithLambda.contains(elements.get(i).get(0))) {
                rulesWithLambda.add(elements.get(i).get(0));
                i = 0;
            }
        }
        List<List<String>> newElements = new ArrayList<>();

        for (List<String> rule : elements) {// for all the rules in elements
            List<String> newRule = new ArrayList<>();
            for(String symbol : rule) { // go trough all symbles
                newRule.add(symbol);

                for(String verifica : rulesWithLambda){ // foreach element that has lambda
                    String replaced = symbol.replaceAll(verifica, "");
                    if (!replaced.equals(symbol)  && !replaced.equals("") ) {
                        newRule.add(replaced);
                    }
                }

                if (symbol.equals(lambda)){ // if has lambda removes
                    newRule.remove(lambda); 
                }
            }
            newElements.add(newRule);
            
        }
        return newElements;
    }

    public List<List<String>> eliminateUnitaryProductions(List<List<String>> elements) {
        List<List<String>> newElements = new ArrayList<>();
        for (List<String> innerList : elements) {
            List<String> newInnerList = new ArrayList<>(innerList);
            newElements.add(newInnerList);
        }
        boolean redoUnitary;
        do {
            redoUnitary = false;
            for (int i=0; i<newElements.size(); i++) {
                List<String> rule = newElements.get(i);
                int indexUnitary = indexOfUnitaryProduction(rule);
                
                if (indexUnitary > 0) {
                    if (indexOfUnitaryProduction(newElements.get(utils.getRuleIndex(rule.get(indexUnitary), newElements))) > 0) {
                        i = utils.getRuleIndex(rule.get(indexUnitary), newElements) -1;
                        redoUnitary = true;
                    } else {
                        List<String> ruleFromUnitary = newElements.get(utils.getRuleIndex(rule.get(indexUnitary), newElements));
                        for (int j=1; j<ruleFromUnitary.size(); j++) {
                            newElements.get(i).add(ruleFromUnitary.get(j));
                        }
                        newElements.get(i).remove(indexUnitary);
                    }
                }
            }
        } while (redoUnitary);
    
        return newElements;
    }

    public int indexOfUnitaryProduction(List<String> rule) {
        for(int i=1; i<rule.size(); i++) {
            if (rule.get(i).length() == 1 && utils.isUpperCase(rule.get(i))) {
                return i; // returns position of unitary production
            }
        }
        return 0; // rule doesnt have unitary production
    }

    public List<List<String>> removeUnusedRules(List<List<String>> elements) {
        List<List<String>> newElements = new ArrayList<>();
        for (List<String> innerList : elements) {
            List<String> newInnerList = new ArrayList<>(innerList);
            newElements.add(newInnerList);
        }
        List<String> calls = new ArrayList<>();
        boolean checkAgain;

        do {
            checkAgain = false;
            calls.clear();
            calls.add(newElements.get(0).get(0));
            for (List<String> rule : newElements) {
                for(int j=1; j<rule.size(); j++) { // go trough words
                    String word = rule.get(j);
                    for(int k=0; k<word.length(); k++) { // go trough every letter
                        if(word.charAt(k) == 'X') {
                            if (!calls.contains(String.valueOf(word.charAt(k))+String.valueOf(word.charAt(k+1)))) {
                                calls.add(String.valueOf(word.charAt(k))+String.valueOf(word.charAt(k+1))); 
                            }

                        } else if (word.charAt(k) >= 'A' && word.charAt(k) <= 'Z') {
                            if (!calls.contains(String.valueOf(word.charAt(k)))) {
                                calls.add(String.valueOf(word.charAt(k)));
                            }
                        }

                    }
                }
            }

            for (int i=0; i<newElements.size(); i++) {
                List<String> rule = newElements.get(i); 
                if (!calls.contains(rule.get(0))) {
                    newElements.remove(rule);
                    checkAgain = true;
                    i--;
                }
            }

        } while (checkAgain);
        
        return newElements;
    }

    public List<List<String>> doChomsky(List<List<String>> elements) {
        List<List<String>> newElements = new ArrayList<>();
        for (List<String> innerList : elements) {
            List<String> newInnerList = new ArrayList<>(innerList);
            newElements.add(newInnerList);
        }
        int countNewVars = 1;
        String newValue = "";
        int subStringEndPoint = 2;
        Map<Character, String> checkedElements = new HashMap<>();
        // change variables in lowercase to variables Xi
        for (List<String> rule : newElements) {
            for(int i=1; i<rule.size();i++) {
                String symble = rule.get(i);

                for(int j=0; j<symble.length(); j++) {
                    
                    if (!(symble.charAt(j) >= 'A' && symble.charAt(j) <= 'Z') && symble.length() >= 2) {

                        if (checkedElements.containsKey(symble.charAt(j))) {
                            newValue = rule.get(i).replaceAll(String.valueOf(symble.charAt(j)), checkedElements.get(symble.charAt(j)));
                            rule.set(i, newValue);
                        } else {
                            checkedElements.put(symble.charAt(j), "X"+countNewVars);
                            countNewVars++;
                            newValue = rule.get(i).replaceAll(String.valueOf(symble.charAt(j)), checkedElements.get(symble.charAt(j)));
                            rule.set(i, newValue);
                        }
                                 
                    }
                }
            }
        }

        for (Map.Entry<Character, String> entry : checkedElements.entrySet()) {
            List<String> newRule = new ArrayList<>();
            newRule.add(entry.getValue());
            newRule.add(String.valueOf(entry.getKey()));
            newElements.add(newRule);
        }

        // take out variables followed by 2 or more variables in uppercase to crate new Xi wiht the union of each
        Map<String, String> checkedElementsDoubleVar = new HashMap<>();
        for (List<String> rule : newElements) {
            for(int i=1; i<rule.size();i++) {
                String symble = rule.get(i);
                subStringEndPoint = 2;
  
                if (symble.length() >= 3) {
                    for(int j=0; j<subStringEndPoint; j++) {
                        if (symble.charAt(j) == 'X') {
                            subStringEndPoint++;
                        }
                    }
                    if (symble.length() == subStringEndPoint) {
                        break;
                    }
                    String substring = symble.substring(0, subStringEndPoint);
                    
                    if (checkedElementsDoubleVar.containsKey(substring)) {
                        newValue = rule.get(i).replaceAll(substring, checkedElementsDoubleVar.get(substring));
                        rule.set(i, newValue);
                    } else {
                        checkedElementsDoubleVar.put(substring, "X"+countNewVars);
                        countNewVars++;
                        newValue = rule.get(i).replaceAll(substring, checkedElementsDoubleVar.get(substring));
                        rule.set(i, newValue);
                    }
                                
                }
            }
        }

        for (Map.Entry<String, String> entry : checkedElementsDoubleVar.entrySet()) {
            List<String> newRule = new ArrayList<>();
            newRule.add(entry.getValue());
            newRule.add(entry.getKey());
            newElements.add(newRule);
        }
        long tempoFinal = System.currentTimeMillis();
        return newElements;
    }

}
