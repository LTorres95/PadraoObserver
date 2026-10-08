package observer;

public class Main {
    public static void main(String[] args) {
        Pedido pedido = new Pedido(101);
        PainelPedido painel = new PainelPedido();
        HistoricoPedido historico = new HistoricoPedido();

        pedido.adicionarObservador(painel);
        pedido.adicionarObservador(historico);

        pedido.alterarStatus("PAGO");
        System.out.println(painel.getResumo());

        pedido.alterarStatus("ENVIADO");
        System.out.println(painel.getResumo());

        pedido.removerObservador(painel);
        pedido.alterarStatus("ENTREGUE");

        System.out.println("Painel após remoção: " + painel.getResumo());
        System.out.println("Histórico: " + historico.getRegistros());
    }
}
