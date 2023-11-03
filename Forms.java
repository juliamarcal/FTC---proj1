import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Forms {
    public List<List<String>> ToFNC(List<List<String>> elements) {
        elements = eliminateVoid(elements);
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

        System.out.println(rulesWithLambda);

        for (List<String> rule : elements) {
            for (int i=1; i< rule.size(); i++) {
                if (rule.get(i).equals(rulesWithLambda)) {
                    elements.set(i,"");
                    if() {

                    } 
                }
            }

                
        }


        return elements;
    }

}
