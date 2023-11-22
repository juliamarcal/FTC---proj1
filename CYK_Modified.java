import java.util.ArrayList;
import java.util.List;

public class CYK_Modified {

    private List<String> relacaoUnitaria;
    private List<String> relacaoUnitariaReversa;
    List<Regra> regras;

    public CYK_Modified() {
        // Inicializa a lista de regras
        this.regras = new ArrayList<>();

        // Adiciona regras à lista
        Regra regra1 = new Regra();
        this.regras.add(regra1);

        // Inicializa as listas relacaoUnitaria e relacaoUnitariaReversa
        this.relacaoUnitaria = new ArrayList<>();
        this.relacaoUnitariaReversa = new ArrayList<>();

        // Chama os métodos que dependem da lista de regras
        relacaoUnitaria();
        relacaoUnitariaReversa();
    }

    public List<String> criarFechoUnitario(String a, List<String> relacaoUnitariaReversa) {
        List<String> fecho = new ArrayList<String>();
        fecho.add(a);
        List<String> tmp = new ArrayList<String>();
        tmp.add("" + a);
        while (tmp.size() != 0) {
            String var = tmp.get(0);
            tmp.remove(0);
            for (String s : relacaoUnitariaReversa) {
                if ((s.charAt(1) + "").equals(var)) {
                    if (!fecho.contains(s.charAt(3) + "")) {
                        tmp.add(s.charAt(3) + "");
                        fecho.add(s.charAt(3) + "");
                    }
                }
            }
        }
        return fecho;
    }

    public void relacaoUnitaria() {
        // Implementação do método relacaoUnitaria
        for (Regra regra : regras) {
            for (List<String> producao : regra.getProducoes()) {
                String simboloInicial = regra.getSimboloInicial();
                for (int i = 0; i < producao.size(); i++) {
                    String simbolo = producao.get(i);
                    if (!simbolo.equals("#")) {
                        // Crio a relação unitária e a reversa
                        String novaRelacao = "(" + simboloInicial + "," + simbolo + ")";
                        String novaRelacaoReversa = "(" + simbolo + "," + simboloInicial + ")";
    
                        if (!relacaoUnitaria.contains(novaRelacao))
                            relacaoUnitaria.add(novaRelacao);
                        if (!relacaoUnitariaReversa.contains(novaRelacaoReversa))
                            relacaoUnitariaReversa.add(novaRelacaoReversa);
                    }
                }
            }
        }
    
        System.out.println("Ug: " + relacaoUnitaria);
        System.out.println("Ûg: " + relacaoUnitariaReversa);
    }
    
    public void relacaoUnitariaReversa() {
        // Implementação do método relacaoUnitariaReversa
        for (Regra regra : regras) {
            for (List<String> producao : regra.getProducoes()) {
                String simboloInicial = regra.getSimboloInicial();
                for (int i = 0; i < producao.size(); i++) {
                    String simbolo = producao.get(i);
                    if (!simbolo.equals("#")) {
                        // Crio a relação unitária reversa
                        String novaRelacaoReversa = "(" + simbolo + "," + simboloInicial + ")";
                        if (!relacaoUnitariaReversa.contains(novaRelacaoReversa))
                            relacaoUnitariaReversa.add(novaRelacaoReversa);
                    }
                }
            }
        }
    }

    public List<String> criarFechoUnitario(String s) {
        String[] split = s.split(" ");
        List<String> resposta = new ArrayList<>();
        for (String t : split) {
            resposta.addAll(criarFechoUnitario(t, relacaoUnitariaReversa));
        }
        return resposta;
    }

    public String listToString(List<String> str) {
        StringBuilder tmp = new StringBuilder();
        for (String s : str) {
            tmp.append(s).append(",");
        }
        return tmp.substring(0, tmp.length() - 1);
    }

    public void CYK(String palavra) {
        palavra = "ac";
        String[][] T = new String[palavra.length()][palavra.length()];
        String[][] Tt = new String[palavra.length()][palavra.length()];

        for (int i = 0; i < palavra.length(); i++) {
            for (int j = 0; j < palavra.length(); j++) {
                T[i][j] = "";
                Tt[i][j] = "";
            }
        }

        for (int i = 0; i < palavra.length(); i++) {
            for (Regra regra : regras) {
                for (List<String> producao : regra.getProducoes()) {
                    if (producao.size() == 1 && producao.get(0).equals(String.valueOf(palavra.charAt(i)))) {
                        T[i][i] = listToString(criarFechoUnitario(producao.get(0), relacaoUnitariaReversa));
                    }
                }
            }
        }

        for (int j = 1; j <= palavra.length() - 1; j++) {
            for (int i = j - 1; i >= 0; i--) {
                for (int h = i; h <= j - 1; h++) {
                    for (Regra r : regras) {
                        for (List<String> producao : r.getProducoes()) {
                            if (producao.size() == 2) {
                                String c1 = producao.get(0);
                                String c2 = producao.get(1);
                                if (T[i][h].contains(c1) && T[h + 1][j].contains(c2)) {
                                    Tt[i][j] += r.getSimboloInicial() + " ";
                                }
                            }
                        }
                    }
                    T[i][j] = listToString(criarFechoUnitario(Tt[i][j], relacaoUnitariaReversa));
                }
            }
        }

        for (int i = 0; i < palavra.length(); i++) {
            for (int j = 0; j < palavra.length(); j++) {
                System.out.print(T[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        if ((T[0][palavra.length() - 1]).contains("L")) {
            System.out.println("sim");
        } else {
            System.out.println("nao");
        }
    }
}
