import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.rmi.CORBA.Util;

public class Forms {
    static Utils utils = new Utils();

    public List<List<String>> ToFNC(List<List<String>> elements) {
        System.out.println(elements);

        elements = eliminateVoid(elements);
        System.out.println(elements);

        elements = eliminateUnitaryProductions(elements);
        System.out.println(elements);
        
        elements = doChomsky(elements);
        System.out.println(elements);



        return elements;
    }

    public List<List<String>> To2NF(List<List<String>> elements) {

        return null;
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

        for (List<String> rule : elements) {
            List<String> newRule = new ArrayList<>();
            for(String symbol : rule) {
                for(String verifica : rulesWithLambda){
                    newRule.add(symbol);
                    String replaced = symbol.replaceAll(verifica, "");
                    if (!replaced.equals(symbol)  && !replaced.equals("") ) {
                        newRule.add(replaced);
                    }
                }
                if (symbol.equals(lambda)){
                    newRule.remove(lambda); 
                }
            }
            newElements.add(newRule);
            
        }
        elements.clear();
        elements.addAll(newElements);


        return elements;
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

    public List<List<String>> doChomsky(List<List<String>> elements) {
        List<List<String>> newElements = new ArrayList<>();
        for (List<String> innerList : elements) {
            List<String> newInnerList = new ArrayList<>(innerList);
            newElements.add(newInnerList);
        }
        int countNewVars = 1;
        String newValue = "";
        Map<Character, String> checkedElements = new HashMap<>();
        
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
        
        return newElements;
    }

}
