package model;

import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Exibe o menu principal e recebe as opções digitadas pelo usuário.
 *
 * As opções 1 a 5 são pontos de integração com as funcionalidades
 * desenvolvidas pelos outros integrantes do grupo.
 */
public class MenuConsole {
    private final Scanner scanner;
    private final List<Evento> eventos;
    private final CadastroUsuario cadastroUsuario;

    public MenuConsole(List<Evento> eventos) {
        this.eventos = eventos;
        this.scanner = new Scanner(System.in);
        this.cadastroUsuario = new CadastroUsuario(scanner);
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
                    cadastrarEvento();
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
            System.out.println((i + 1) + " - " + eventos.get(i).exibirDetalhes());
        }
    }

    private void informarIntegracao(String funcionalidade) {
        System.out.println(funcionalidade
                + ": esta opção precisa ser conectada à classe responsável "
                + "por essa funcionalidade.");
    }
 
private void cadastrarEvento() {
    System.out.println("\n========== CADASTRO DE EVENTO ==========");

    String nome = lerTextoObrigatorio("Nome do evento: ");
    String endereco = lerTextoObrigatorio("Endereço ou local de origem: ");

    Categoria categoria;
    while (true) {
        System.out.println("Categorias: Música, Esporte, Cultura, Gastronomia, Tecnologia, Educação");
        System.out.print("Categoria: ");
        try {
            categoria = Categoria.deTexto(scanner.nextLine());
            break;
        } catch (IllegalArgumentException e) {
            System.out.println("Categoria inválida. Tente novamente.");
        }
    }

    LocalDateTime inicio;
    while (true) {
        System.out.print("Data e horário (dd/MM/yyyy HH:mm): ");
        String data = scanner.nextLine().trim();

        try {
            inicio = LocalDateTime.parse(data, Evento.FORMATO_DATA);

            if (!inicio.format(Evento.FORMATO_DATA).equals(data)) {
                throw new IllegalArgumentException();
            }
            break;
        } catch (DateTimeParseException | IllegalArgumentException e) {
            System.out.println("Data inválida! Exemplo: 30/11/2026 14:30");
        }
    }

    int duracaoMin;
    while (true) {
        System.out.print("Duração em minutos: ");
        try {
            duracaoMin = Integer.parseInt(scanner.nextLine().trim());
            if (duracaoMin > 0) {
                break;
            }
            System.out.println("A duração deve ser maior que zero.");
        } catch (NumberFormatException e) {
            System.out.println("Digite um número válido.");
        }
    }

    String descricao = lerTextoObrigatorio("Descrição: ");

    Evento evento;

    while (true) {
        System.out.println("Tipo de evento:");
        System.out.println("1 - Presencial");
        System.out.println("2 - Online");
        System.out.print("Escolha: ");

        String tipo = scanner.nextLine().trim();

        if (tipo.equals("1")) {
            System.out.print("Ponto de referência (opcional): ");
            String ponto = scanner.nextLine();

            evento = new EventoPresencial(
                    nome, endereco, categoria, inicio,
                    duracaoMin, descricao, ponto
            );
            break;

        } else if (tipo.equals("2")) {
            String link = lerTextoObrigatorio("Link de acesso: ");

            evento = new EventoOnline(
                    nome, endereco, categoria, inicio,
                    duracaoMin, descricao, link
            );
            break;

        } else {
            System.out.println("Opção inválida. Escolha 1 ou 2.");
        }
    }

    eventos.add(evento);
    PersistenciaEventos.salvar(eventos);

    System.out.println("\nEvento cadastrado e salvo com sucesso!");
}

private String lerTextoObrigatorio(String mensagem) {
    while (true) {
        System.out.print(mensagem);
        String texto = scanner.nextLine().trim();

        if (!texto.isEmpty()) {
            return texto;
        }

        System.out.println("Este campo é obrigatório. Tente novamente.");
    }
}
   
}
