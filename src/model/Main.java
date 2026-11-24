import java.util.List;

/**
 * Ponto de entrada do sistema.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando o Sistema de Eventos...");

        // Recupera os eventos gravados na execução anterior.
        List<Evento> eventos = PersistenciaEventos.carregar();

        System.out.println(eventos.size() + " evento(s) carregado(s).");

        // Inicia o menu principal.
        MenuConsole menu = new MenuConsole(eventos);
        menu.executar();
    }
}
