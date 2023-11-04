public class Registro {
  private int valor;
  private Registro proximo;
  public Registro(int valor) {
    this.valor = valor;
    proximo = null;
  }

  public void setProximo(Registro n) {
    this.proximo = n;
  }

  public Registro getProximo() {
    return this.proximo;
  }

  public void setValor(int valor) {
      this.valor = valor;
  }

  public int getValor() {
      return this.valor;
  }

  public String toString() {
    return String.valueOf(valor);
  }
}
