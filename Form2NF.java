import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Form2NF {
    static Chomsky chomsky = new Chomsky();

    public List<List<String>> To2NF(List<List<String>> elements) {
        List<List<String>> newElements = new ArrayList<>();
        
        System.out.println("======= Passo a passo - 2FN =======\n");
        System.out.println("Gramática original: " + elements);

        newElements = chomsky.eliminateVoid(elements);
        System.out.println("Elimina lambda: " + newElements);

        newElements = do2NF(newElements);
        System.out.println("Alteração para forma normal binária: " + newElements + "\n");

        return newElements;
    }


    public List<List<String>> do2NF(List<List<String>> elements) {
        List<List<String>> newElements = new ArrayList<>();
        for (List<String> innerList : elements) {
            List<String> newInnerList = new ArrayList<>(innerList);
            newElements.add(newInnerList);
        }
        int countNewVars = 1;
        String newValue = "";
        int subStringEndPoint = 2;
        Map<String, String> checkedElements = new HashMap<>();

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
                    
                    if (checkedElements.containsKey(substring)) {
                        newValue = rule.get(i).replaceAll(substring, checkedElements.get(substring));
                        rule.set(i, newValue);
                    } else {
                        checkedElements.put(substring, "X"+countNewVars);
                        countNewVars++;
                        newValue = rule.get(i).replaceAll(substring, checkedElements.get(substring));
                        rule.set(i, newValue);
                    }
                                
                }
            }
        }

        for (Map.Entry<String, String> entry : checkedElements.entrySet()) {
            List<String> newRule = new ArrayList<>();
            newRule.add(entry.getValue());
            newRule.add(entry.getKey());
            newElements.add(newRule);
        }
        
        return newElements;
    }

}


