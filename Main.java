import java.util.List;

public class Main {
    static Archive archive = new Archive();
    static Chomsky chomsky = new Chomsky();
    static Form2NF form2nf = new Form2NF();
    static Utils utils = new Utils();

    static CYK_Modified cyk_modified;
    static CYK cyk;

    public static void main(String[] args) {
        // Declarações de variáveis
        List<List<String>> elements;
        List<String> sentences;

        // Lê o arquivo e salva a gramatica
        elements = archive.FindGlcInArquive();
        sentences = archive.FindSentenceInArquive();

        if ( !utils.isValidGrammar(elements) ) {
            System.out.println("Erro: Gramatica invalida!");
            return;
        }
        List<List<String>> gramatica = chomsky.ToFNC(elements);;
        List<List<String>> gramatica2NF = form2nf.To2NF(elements);
        cyk = new CYK(gramatica);
        cyk_modified = new CYK_Modified();

        System.out.println("\n\n======= Resultados Finais =======\n");
        System.out.println("Gramática original: " + elements);
        System.out.println("Sentenças a validar: " + sentences);
        System.out.println("Gramática de Chomsky: " + gramatica);
        System.out.println("Gramática 2FN: " + gramatica2NF); 

        for (String sentence : sentences) {
            boolean validation = cyk.cykParse(gramatica, sentence);
            System.out.println("Validação CYK para a sentença \"" + sentence + "\": " + validation);
        }
        System.out.println("\n\n");
        List<List<String>> reversa = CYK_Modified.gerarRelacaoReversa(gramatica2NF);
        for (String sentence : sentences) {
            boolean validation_cykModified = CYK_Modified.cyk(reversa,sentence);
            System.out.println("Validação CYK Modificado para a sentença \"" + sentence + "\": " + validation_cykModified);
        }
    }
}