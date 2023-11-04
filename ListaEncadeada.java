public class ListaEncadeada {
  private Registro lista;
  private double deviacao;
  public ListaEncadeada() {
    lista = null;
    deviacao = 0.;
  }

  public boolean vazia() {
      return lista == null;
  }

  public int inserir(int dado) {
    if (lista == null) {
      lista = new Registro(dado);
      return 0;
    } else {
      // 3
      // 10 => 1, 5
      //
      // ultimo = 10
      // proximo = 1
      // proximo = 5, ultimo = 1
      Registro ultimo = lista;
      Registro proximo = ultimo.getProximo();
      while (proximo != null && proximo.getValor() < dado) {
        ultimo = proximo;
        proximo = proximo.getProximo();
      }
      Registro novo = new Registro(dado);
      ultimo.setProximo(novo);
      novo.setProximo(proximo);
      return 1;
    }
  }

  public int buscar(int dado) {
    if (vazia()) return 0;
    Registro atual = lista;
    if (atual.getValor() == dado) {
      return 1;
    } else {
      int comparacoes = 1;
      while (atual != null && atual.getValor() != dado) {
        atual = atual.getProximo();
        comparacoes += 1;
      }
      return comparacoes;
    }
  }

  public void imprimir() {
    if (this.vazia()) {
      System.out.println("Lista= Vazia");
    } else {
      System.out.print("Lista= ");
      for (Registro n = lista; n != null; n = n.getProximo()) {
        System.out.print(n + " ");
      }
      System.out.println();
    }
  }

  public int tamanho() {
      int tamanho = 0;
      Registro atual = lista;
      if (atual == null) {
          return 0;
      }
      while (atual.getProximo() != null) {
          atual = atual.getProximo();
          tamanho += 1;
      }
      tamanho += 1;
      return tamanho;
  }

  public void setDeviacao(double d) {
      deviacao = d;
  }

  public double getDeviacao() {
      return this.deviacao;
  }
}
