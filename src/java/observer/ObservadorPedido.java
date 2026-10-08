package observer;

/** Contrato Observer: recebe os dados da mudança (notificação por push). */
public interface ObservadorPedido {
    void atualizar(int numeroPedido, String novoStatus);
}
