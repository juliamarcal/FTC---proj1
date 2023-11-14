import java.util.List;

public class Main {
    static Archive archive = new Archive();
    static Chomsky chomsky = new Chomsky();
    static CYK cyk;

    public static void main(String[] args) {
        // Declarações de variáveis
        List<List<String>> elements;
        List<String> sentences;

        // Lê o arquivo e salva a gramatica
        elements = archive.FindGlcInArquive();
        sentences = archive.FindSentenceInArquive();
        List<List<String>> gramatica = chomsky.ToFNC(elements);

        System.out.println("\n\n======= Resultados Finais =======\n");
        System.out.println("Gramática original: " + elements);
        System.out.println("Sentenças a validar: " + sentences);
        System.out.println("Gramática de Chomsky: " + gramatica);

        cyk = new CYK(gramatica); // Criando uma instância de CYK com as regras de Chomsky

        for (String sentence : sentences) {
            boolean validation = cyk.cykParse(gramatica, sentence);
            System.out.println("Validação CYK para a sentença \"" + sentence + "\": " + validation);
        }
    }
}