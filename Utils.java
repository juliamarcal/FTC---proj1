import java.util.List;

public class Utils {

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

}
