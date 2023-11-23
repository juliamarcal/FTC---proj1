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

        System.out.println("\n\n======= Parametros iniciais =======\n");
        System.out.println("Gramática original: " + elements);
        System.out.println("Sentenças a validar: " + sentences+"\n\n");

        // CYK --------------------------------------------------------------

        long tempoInicialCYK = System.currentTimeMillis();
        List<List<String>> gramatica = chomsky.ToFNC(elements);
        System.out.println("\n\nGramática de Chomsky: " + gramatica+"\n");

        cyk = new CYK(gramatica);
        System.out.println("\n\n======= Validações =======\n");
        for (String sentence : sentences) {
            boolean validation = cyk.cykParse(gramatica, sentence);
            System.out.println("Validação CYK para a sentença \"" + sentence + "\": " + validation);
        }

        long tempoFinalCYK = System.currentTimeMillis();
        long tempoTotalCYK = tempoFinalCYK - tempoInicialCYK;
        System.out.println("\nTempo de execução CYK: " + tempoTotalCYK +" segundos\n");
        
        // CYK - Modificado -------------------------------------------------

        long tempoInicialCYKMod = System.currentTimeMillis();
        List<List<String>> gramatica2NF = form2nf.To2NF(elements);

        cyk_modified = new CYK_Modified(gramatica2NF);
        System.out.println("Gramática 2FN: " + gramatica2NF); 

        List<List<String>> reversa = CYK_Modified.gerarRelacaoReversa(gramatica2NF);
        System.out.println("Ûg da gramática: " + reversa+"\n");

        System.out.println("\n\n======= Validações =======\n");
        for (String sentence : sentences) {
            boolean validation_cykModified = CYK_Modified.cyk(reversa,sentence);
            System.out.println("Validação CYK Modificado para a sentença \"" + sentence + "\": " + validation_cykModified);
        }
        
        long tempoFinalCYKMod = System.currentTimeMillis();
        long tempoTotalCYKMod = tempoFinalCYKMod - tempoInicialCYKMod;
        System.out.println("\nTempo de execução CYK Modificado: " + tempoTotalCYKMod +" segundos\n");
    }
}