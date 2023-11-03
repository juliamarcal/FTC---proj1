import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Archive {
    public List<List<String>> FindGlcInArquive() {
        
        List<List<String>> elements = new ArrayList<>();

        try {
            FileInputStream stream = new FileInputStream("gramatica.txt");
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader br = new BufferedReader(reader);

            String linha = br.readLine();

            while (linha != null && !linha.equals("----")) {
                //System.out.println(linha);

                String[] linhaSplited = linha.split(",");

                elements.add(Arrays.asList(linhaSplited));
                linha = br.readLine();

            }

        } catch (Exception e) {
            System.out.println("Erro ao ler o arquivo!!");
        }

        return elements;
    }

    public List<String> FindSentenceInArquive() {
        
        List<String> sentences = new ArrayList<>();

        try {
            FileInputStream stream = new FileInputStream("gramatica.txt");
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader br = new BufferedReader(reader);

            String linha = br.readLine();

            while (linha != null) {
                //System.out.println(linha);

                if (linha.equals("----")) {
                    linha = br.readLine();
                    while (linha != null) {
                        sentences.add(linha);
                        linha = br.readLine();
                    }
                     
                    break;
                }

                linha = br.readLine();

            }

        } catch (Exception e) {
            System.out.println("Erro ao ler o arquivo!!");
        }

        return sentences;
    }
}