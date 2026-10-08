package observer;

/** Observer concreto: guarda a última atualização para exibição. */
public class PainelPedido implements ObservadorPedido {
    private String resumo = "Nenhum pedido atualizado.";

    @Override
    public void atualizar(int numeroPedido, String novoStatus) {
        resumo = "Pedido " + numeroPedido + ": " + novoStatus;
    }

    public String getResumo() {
        return resumo;
    }
}
