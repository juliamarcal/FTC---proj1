import java.util.ArrayList;
import java.util.List;

public class Main {
    static Archive archive = new Archive();

    public static void main(String[] args) {
        // Declarações de variáveis
        List<List<String>> elements;
        List<String> sentences;
        
        // Lê o arquivo e salva a gramatica
        elements = archive.FindGlcInArquive();
        sentences = archive.FindSentenceInArquive();
        System.out.println("elements: " + elements);
        System.out.println("sentence: " + sentences);
        
    }
}