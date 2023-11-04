import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Hash {
    private int tamanho;
    private ListaEncadeada tabela[];
    private int nColisoes;
    private int nInsercoes;
    /*
     * 0 = hash pelo modulo
     * 1 = hash pelo xor de cada divisao de 10 em 10
     * 2 = hash pelo shift a direita de (DIGITO_MAIS_SIGNIFICATIVO) bits */
    private int metodo;
    public Hash(int tamanho, int metodo) {
        this.tamanho = tamanho;
        tabela = new ListaEncadeada[tamanho];
        for (int i = 0; i < tamanho; i++) {
            tabela[i] = new ListaEncadeada();
        }
        nColisoes = 0;
        nInsercoes = 0;
        this.metodo = metodo;
    }

    private void inserir(int chave) {
        int indice = hash(chave);
        nColisoes += tabela[indice].inserir(chave);
        nInsercoes += 1;
    }

    private int buscar(int chave) {
        int indice = hash(chave);
        return tabela[indice].buscar(chave);
    }

    private int hash(int chave) {
        long hash = 0;
        int div;
        switch (metodo) {
        case 0: /* MODULO */
            hash = chave % tamanho;
            return (int)hash;
        case 1: /* XOR */
            // 5234/1 -> 5234
            // 5234/10 -> 523
            // 5234/100 -> 52
            // 5234/1000 -> 5
            for (int d = 1; (div = chave / d) != 0; d *= 10) {
                hash ^= div;
            }
            /* java nao tem numeros `unsigned` */
            if (hash < 0) {
                hash *= -1;
            }
            hash %= tamanho;
            return (int)hash;
        case 2: /* SHIFT */
            int significativo = 0;
            for (int d = 1; (div = chave / d) != 0; d *= 10) {
                significativo = div;
            }
            hash = (chave >>> significativo);
            hash %= tamanho;
            return (int)hash;
        default:
            return (int)hash;
        }
    }

    public double desvioPadrao() {
        double media = (double)nInsercoes / this.tamanho;
        for (int i = 0; i < tamanho; i++) {
            ListaEncadeada lista = tabela[i];
            int t = lista.tamanho();
            lista.setDeviacao(Math.pow(media - t, 2));
        }
        double soma = 0.;
        for (int i = 0; i < tamanho; i++) {
            ListaEncadeada lista = tabela[i];
            soma += lista.getDeviacao();
        }
        return Math.sqrt(soma / this.tamanho);
    }

    public void imprimir() {
        for (int i = 0; i < tamanho; i++) {
            ListaEncadeada lista = tabela[i];
            System.out.print("Indice " + i + ": ");
            //lista.imprimir();
            int tamanho = lista.tamanho();
            System.out.println("Lista= Tamanho[" + tamanho + "]");
        }
    }

    private static int[] gerarArray(int tamanho, int max) {
        /* Sempre usar a mesma seed (1) */
        int array[] = new Random(1).ints(0, max)
          .distinct()
          .limit(tamanho)
          .toArray();
        return array;
    }

    private static double segundos(long inicio, long fim) {
        return (fim - inicio)/1000000000.0;
    }

    public static class Retorno {
        public double tempo_insercao, tempo_busca, desvioPadrao;
        public double media_comparacoes;
        public int nColisoes;
        public Retorno(double i, double b, double d, int n, double c) {
            tempo_insercao = i;
            tempo_busca = b;
            desvioPadrao = d;
            nColisoes = n;
            media_comparacoes = c;
        }
    }

    private static Retorno testar(int array[], int buscaArray[],
            int tamanho_tabela, int metodo) {
        /* Executar hash de todos os numeros */
        Hash h = new Hash(tamanho_tabela, metodo);
        long inicio = System.nanoTime();
        for (int numero : array) {
            h.inserir(numero);
        }
        long fim = System.nanoTime();
        double tempo_insercao = segundos(inicio, fim);
        /* Array de indices gerados aleatoriamente do array de numeros para
         * buscar na tabela hash */
        inicio = System.nanoTime();
        double media_comparacoes = 0.;
        for (int indice : buscaArray) {
            int comparacoes = h.buscar(array[indice]);
            media_comparacoes += comparacoes;
        }
        fim = System.nanoTime();
        media_comparacoes /= 5;
        double tempo_busca = segundos(inicio, fim)/5;
        double desvioPadrao = h.desvioPadrao();
        return new Retorno(tempo_insercao, tempo_busca, desvioPadrao,
                h.nColisoes, media_comparacoes);
    }

    public static void main(String args[]) throws IOException {
        int tamanho_tabela = Integer.parseInt(args[0]);
        int tamanho_numeros = Integer.parseInt(args[1]);
        int metodo = Integer.parseInt(args[2]);
        long inicio = System.nanoTime();
        /* Array de numeros gerados aleatoriamente para serem inseridos na tabela
         * hash */
        int array[] = Hash.gerarArray(tamanho_numeros, Integer.MAX_VALUE);
        long fim = System.nanoTime();
        System.out.println("Gerado " + tamanho_numeros + " números em " +
                 segundos(inicio, fim) + " segundos");
        /* Array de indices gerados aleatoriamente do array de numeros para
         * buscar na tabela hash */
        int buscaArray[] = Hash.gerarArray(5, tamanho_numeros);
        double media_insercao = 0., media_busca = 0.;
        double desvioPadrao = 0., media_comparacoes = 0.;
        int nColisoes = 0;
        for (int i = 0; i < 5; i++) {
            Retorno r = Hash.testar(array, buscaArray, tamanho_tabela, metodo);
            media_insercao += r.tempo_insercao;
            media_busca += r.tempo_busca;
            desvioPadrao = r.desvioPadrao;
            nColisoes = r.nColisoes;
            media_comparacoes = r.media_comparacoes;
        }
        media_insercao /= 5;
        media_busca /= 5;
        System.out.println("Inserção de " + tamanho_numeros +
                " números numa tabela de tamanho " + tamanho_tabela +
                " demorou em média " + media_insercao + " s");
        System.out.println("Busca de um número aleatório na tabela hash" +
                " demorou em média " + media_busca + " s");
        System.out.println("Desvio padrão da tabela: " + desvioPadrao);
        System.out.println("Número de colisões na tabela: " + nColisoes);
        System.out.println("Média de comparações para a busca de um elemento" +
                " aleatório " + media_comparacoes);
        /* Escrever resultado em arquivo */
        FileWriter f = new FileWriter(new File("resultado-" + metodo + ".csv"),
                true);
        String linha = String.join(",",
                       String.valueOf(tamanho_tabela),
                       String.valueOf(tamanho_numeros),
                       String.valueOf(media_insercao),
                       String.valueOf(media_busca),
                       String.valueOf(desvioPadrao),
                       String.valueOf(nColisoes),
                       String.valueOf(media_comparacoes));
        f.write(linha + "\n");
        f.close();
    }
}
