import java.util.ArrayList;
import java.util.List;

public class Main {
    static Archive archive = new Archive();
    static Forms forms = new Forms();

    public static void main(String[] args) {
        // Declarações de variáveis
        List<List<String>> elements;
        List<String> sentences;
        
        // Lê o arquivo e salva a gramatica
        elements = archive.FindGlcInArquive();
        sentences = archive.FindSentenceInArquive();
        List<List<String>> lambda = forms.ToFNC(elements);
        System.out.println("elements: " + elements);
        System.out.println("sentence: " + sentences);
        System.out.println("lambda: " + lambda);
        
    }
}