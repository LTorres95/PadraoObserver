package observer;

import java.util.ArrayList;
import java.util.List;

/** Observer concreto: registra todas as atualizações recebidas. */
public class HistoricoPedido implements ObservadorPedido {
    private final List<String> registros = new ArrayList<>();

    @Override
    public void atualizar(int numeroPedido, String novoStatus) {
        registros.add("Pedido " + numeroPedido + ": " + novoStatus);
    }

    public List<String> getRegistros() {
        return List.copyOf(registros);
    }
}
