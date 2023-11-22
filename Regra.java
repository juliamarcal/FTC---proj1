import java.util.List;

public class Regra {
    private String simboloInicial;
    private List<List<String>> producoes;

    // Construtor
    public Regra() {
        this.simboloInicial = "L";
        this.producoes = initializeProducoes();
    }

    // Método para inicializar as produções
    private List<List<String>> initializeProducoes() {
        Archive archive = new Archive();
        Form2NF form2nf = new Form2NF();
        List<List<String>> elements = archive.FindGlcInArquive();
        return form2nf.To2NF(elements);
    }

    public String getSimboloInicial() {
        return simboloInicial;
    }

    public List<List<String>> getProducoes() {
        return producoes;
    }
}
