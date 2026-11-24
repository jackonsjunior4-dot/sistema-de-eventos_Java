import java.util.List;
import java.util.Scanner;

/**
 * Exibe o menu principal e recebe as opções digitadas pelo usuário.
 *
 * As opções 1 a 5 são pontos de integração com as funcionalidades
 * desenvolvidas pelos outros integrantes do grupo.
 */
public class MenuConsole {
    private final Scanner scanner;
    private final List<Evento> eventos;

    public MenuConsole(List<Evento> eventos) {
        this.eventos = eventos;
        this.scanner = new Scanner(System.in);
    }

    public void executar() {
        int opcao;

        do {
            exibirMenu();
            opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    listarEventos();
                    break;
                case 2:
                    informarIntegracao("Cadastrar usuário");
                    break;
                case 3:
                    informarIntegracao("Cadastrar evento");
                    break;
                case 4:
                    informarIntegracao("Participar de evento");
                    break;
                case 5:
                    informarIntegracao("Cancelar participação");
                    break;
                case 6:
                    informarIntegracao("Listar meus eventos");
                    break;
                case 0:
                    PersistenciaEventos.salvar(eventos);
                    System.out.println("Sistema encerrado. Até mais!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

            System.out.println();
        } while (opcao != 0);
    }

    private void exibirMenu() {
        System.out.println("======================================");
        System.out.println("          SISTEMA DE EVENTOS");
        System.out.println("======================================");
        System.out.println("1 - Listar todos os eventos");
        System.out.println("2 - Cadastrar usuário");
        System.out.println("3 - Cadastrar evento");
        System.out.println("4 - Participar de evento");
        System.out.println("5 - Cancelar participação");
        System.out.println("6 - Listar meus eventos");
        System.out.println("0 - Salvar e sair");
        System.out.println("--------------------------------------");
        System.out.print("Escolha uma opção: ");
    }

    private int lerOpcao() {
        while (!scanner.hasNextInt()) {
            System.out.print("Digite um número válido: ");
            scanner.next();
        }
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Consome o ENTER pendente.
        return opcao;
    }

    private void listarEventos() {
        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado.");
            return;
        }

        System.out.println("========== EVENTOS CADASTRADOS ==========");
        for (int i = 0; i < eventos.size(); i++) {
            System.out.println((i + 1) + " - " + eventos.get(i));
        }
    }

    private void informarIntegracao(String funcionalidade) {
        System.out.println(funcionalidade
                + ": esta opção precisa ser conectada à classe responsável "
                + "por essa funcionalidade.");
    }
}
