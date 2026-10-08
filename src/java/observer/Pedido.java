package observer;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/** Subject: mantém os observadores e avisa quando seu status muda. */
public class Pedido {
    private final int numero;
    private String status;
    private final Set<ObservadorPedido> observadores = new LinkedHashSet<>();

    public Pedido(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("O número do pedido deve ser positivo.");
        }
        this.numero = numero;
        this.status = "CRIADO";
    }

    public void adicionarObservador(ObservadorPedido observador) {
        observadores.add(Objects.requireNonNull(observador, "Observador não pode ser nulo."));
    }

    public void removerObservador(ObservadorPedido observador) {
        observadores.remove(observador);
    }

    public void alterarStatus(String novoStatus) {
        if (novoStatus == null) {
            throw new IllegalArgumentException("Status não pode ser nulo.");
        }
        if (novoStatus.isBlank()) {
            throw new IllegalArgumentException("Status não pode ser vazio ou conter apenas espaços.");
        }
        if (!status.equals(novoStatus)) {
            status = novoStatus;
            notificarObservadores();
        }
    }

    private void notificarObservadores() {
        for (ObservadorPedido observador : observadores) {
            observador.atualizar(numero, status);
        }
    }

    public int getNumero() {
        return numero;
    }

    public String getStatus() {
        return status;
    }
}
