import java.util.List;

public class Main {
    static Archive archive = new Archive();
    static Chomsky chomsky = new Chomsky();
    static CYK cyk = new CYK();


    public static void main(String[] args) {
        // Declarações de variáveis
        List<List<String>> originalElements;
        List<String> sentences;
        
        // Lê o arquivo e salva a gramatica
        originalElements = archive.FindGlcInArquive();
        sentences = archive.FindSentenceInArquive();
        List<List<String>> gramatica = chomsky.ToFNC(originalElements);

        System.out.println("\n\n======= Resultados Finais =======\n");
        System.out.println("Gramática: " + originalElements);
        System.out.println("Sentenças à validar: " + sentences);
        System.out.println("Gramática de Chomsky: " + gramatica);


        for (String sentence : sentences) {
            boolean validation = cyk.cykParse(gramatica, sentence);
            System.out.println("Validação CYK: " + validation);
        }
        
    }
}