package observerTest;

import observer.HistoricoPedido;
import observer.PainelPedido;
import observer.Pedido;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PedidoTest {
    @Test
    void iniciaComoCriado() {
        Pedido pedido = new Pedido(101);

        assertEquals(101, pedido.getNumero());
        assertEquals("CRIADO", pedido.getStatus());
    }

    @Test
    void rejeitaNumeroInvalido() {
        assertThrows(IllegalArgumentException.class, () -> new Pedido(0));
        assertThrows(IllegalArgumentException.class, () -> new Pedido(-1));
    }

    @Test
    void alteraSemObservadores() {
        Pedido pedido = new Pedido(101);

        pedido.alterarStatus("PAGO");

        assertEquals("PAGO", pedido.getStatus());
    }

    @Test
    void notificaUmObservador() {
        Pedido pedido = new Pedido(101);
        PainelPedido painel = new PainelPedido();
        pedido.adicionarObservador(painel);
        assertEquals("Nenhum pedido atualizado.", painel.getResumo());

        pedido.alterarStatus("PAGO");

        assertEquals("Pedido 101: PAGO", painel.getResumo());
    }

    @Test
    void notificaTodosOsObservadores() {
        Pedido pedido = new Pedido(101);
        PainelPedido painel = new PainelPedido();
        HistoricoPedido historico = new HistoricoPedido();
        pedido.adicionarObservador(painel);
        pedido.adicionarObservador(historico);

        pedido.alterarStatus("CANCELADO");

        assertEquals("Pedido 101: CANCELADO", painel.getResumo());
        assertEquals(List.of("Pedido 101: CANCELADO"), historico.getRegistros());
    }

    @Test
    void naoNotificaStatusRepetido() {
        Pedido pedido = new Pedido(101);
        HistoricoPedido historico = new HistoricoPedido();
        pedido.adicionarObservador(historico);

        pedido.alterarStatus("CRIADO");
        assertTrue(historico.getRegistros().isEmpty());

        pedido.alterarStatus("PAGO");
        // Mesmo conteúdo em outra instância: a comparação deve usar equals.
        pedido.alterarStatus(new String("PAGO"));

        assertEquals(List.of("Pedido 101: PAGO"), historico.getRegistros());
    }

    @Test
    void naoDuplicaInscricao() {
        Pedido pedido = new Pedido(101);
        HistoricoPedido historico = new HistoricoPedido();
        pedido.adicionarObservador(historico);
        pedido.adicionarObservador(historico);

        pedido.alterarStatus("PAGO");

        assertEquals(List.of("Pedido 101: PAGO"), historico.getRegistros());
    }

    @Test
    void deixaDeNotificarObservadorRemovido() {
        Pedido pedido = new Pedido(101);
        PainelPedido painel = new PainelPedido();
        HistoricoPedido historico = new HistoricoPedido();
        pedido.adicionarObservador(painel);
        pedido.adicionarObservador(historico);
        pedido.alterarStatus("PAGO");

        pedido.removerObservador(painel);
        pedido.alterarStatus("ENVIADO");

        assertEquals("Pedido 101: PAGO", painel.getResumo());
        assertEquals(List.of("Pedido 101: PAGO", "Pedido 101: ENVIADO"), historico.getRegistros());
    }

    @Test
    void removerNaoInscritoPreservaInscritos() {
        Pedido pedido = new Pedido(101);
        HistoricoPedido inscrito = new HistoricoPedido();
        pedido.adicionarObservador(inscrito);

        pedido.removerObservador(new PainelPedido());
        pedido.alterarStatus("PAGO");

        assertEquals(List.of("Pedido 101: PAGO"), inscrito.getRegistros());
    }

    @Test
    void registraSequenciaDeAtualizacoes() {
        Pedido pedido = new Pedido(101);
        HistoricoPedido historico = new HistoricoPedido();
        pedido.adicionarObservador(historico);

        pedido.alterarStatus("PAGO");
        pedido.alterarStatus("ENVIADO");
        pedido.alterarStatus("ENTREGUE");

        assertEquals(List.of("Pedido 101: PAGO", "Pedido 101: ENVIADO", "Pedido 101: ENTREGUE"),
                historico.getRegistros());
        assertEquals("ENTREGUE", pedido.getStatus());
    }

    @Test
    void rejeitaStatusNuloSemNotificar() {
        Pedido pedido = new Pedido(101);
        HistoricoPedido historico = new HistoricoPedido();
        pedido.adicionarObservador(historico);

        assertThrows(IllegalArgumentException.class, () -> pedido.alterarStatus(null));

        assertEquals("CRIADO", pedido.getStatus());
        assertTrue(historico.getRegistros().isEmpty());
    }

    @Test
    void rejeitaStatusEmBrancoSemNotificar() {
        Pedido pedido = new Pedido(101);
        HistoricoPedido historico = new HistoricoPedido();
        pedido.adicionarObservador(historico);

        assertThrows(IllegalArgumentException.class, () -> pedido.alterarStatus(""));
        assertThrows(IllegalArgumentException.class, () -> pedido.alterarStatus(" \t "));

        assertEquals("CRIADO", pedido.getStatus());
        assertTrue(historico.getRegistros().isEmpty());
    }

    @Test
    void rejeitaObservadorNulo() {
        Pedido pedido = new Pedido(101);

        assertThrows(NullPointerException.class, () -> pedido.adicionarObservador(null));
    }

    @Test
    void protegeRegistrosDoHistorico() {
        Pedido pedido = new Pedido(101);
        HistoricoPedido historico = new HistoricoPedido();
        pedido.adicionarObservador(historico);
        pedido.alterarStatus("PAGO");
        List<String> copia = historico.getRegistros();

        assertThrows(UnsupportedOperationException.class, () -> copia.add("Registro externo"));
        pedido.alterarStatus("ENVIADO");

        assertEquals(List.of("Pedido 101: PAGO"), copia);
        assertEquals(List.of("Pedido 101: PAGO", "Pedido 101: ENVIADO"), historico.getRegistros());
    }
}
