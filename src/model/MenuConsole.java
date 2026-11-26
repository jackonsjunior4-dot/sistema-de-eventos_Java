package model;

import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Exibe o menu principal e recebe as opções digitadas pelo usuário.
 */
public class MenuConsole {
    private final Scanner scanner;
    private final List<Evento> eventos;
    private final CadastroUsuario cadastroUsuario;
    private final GerenciadorParticipacoes gerenciadorParticipacoes;
    private Usuario usuarioAtual;

    public MenuConsole(List<Evento> eventos) {
        this.eventos = eventos;
        this.scanner = new Scanner(System.in);
        this.cadastroUsuario = new CadastroUsuario(scanner);
        this.gerenciadorParticipacoes = new GerenciadorParticipacoes();
        if (!this.cadastroUsuario.getUsuarios().isEmpty()) {
            this.usuarioAtual = this.cadastroUsuario.getUsuarios().get(0);
        }
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
                    cadastrarUsuario();
                    break;
                case 3:
                    cadastrarEvento();
                    break;
                case 4:
                    participarDeEvento();
                    break;
                case 5:
                    cancelarParticipacao();
                    break;
                case 6:
                    listarMeusEventos();
                    break;
                case 7:
                    listarEventosOrdenados();
                    break;
                case 8:
                    listarEventosAcontecendoAgora();
                    break;
                case 9:
                    listarEventosJaOcorreram();
                    break;
                case 10:
                    selecionarUsuarioAtivo();
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
        if (usuarioAtual != null) {
            System.out.println("Usuário ativo: " + usuarioAtual.getNome()
                    + " (" + usuarioAtual.getEmail() + ")");
        } else {
            System.out.println("Usuário ativo: Nenhum (cadastre ou selecione)");
        }
        System.out.println("======================================");
        System.out.println("1 - Listar todos os eventos");
        System.out.println("2 - Cadastrar usuário");
        System.out.println("3 - Cadastrar evento");
        System.out.println("4 - Participar de evento");
        System.out.println("5 - Cancelar participação");
        System.out.println("6 - Listar meus eventos");
        System.out.println("7 - Listar eventos ordenados por horário");
        System.out.println("8 - Listar eventos acontecendo agora");
        System.out.println("9 - Listar eventos que já ocorreram");
        System.out.println("10 - Selecionar usuário ativo");
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

    private void cadastrarUsuario() {
        Usuario novoUsuario = cadastroUsuario.cadastrarUsuario();
        if (novoUsuario != null) {
            usuarioAtual = novoUsuario;
        }
    }

    private void selecionarUsuarioAtivo() {
        List<Usuario> usuarios = cadastroUsuario.getUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado ainda.");
            return;
        }

        System.out.println("\n========== USUÁRIOS CADASTRADOS ==========");
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            System.out.println((i + 1) + " - " + u.getNome()
                    + " (" + u.getEmail() + ") - Bairro: " + u.getBairro());
        }

        int escolha = lerIndice(1, usuarios.size(), "Escolha o número do usuário: ");
        if (escolha != -1) {
            usuarioAtual = usuarios.get(escolha - 1);
            System.out.println("Usuário ativo alterado para: " + usuarioAtual.getNome());
        }
    }

    private Usuario garantirUsuarioAtivo() {
        if (usuarioAtual != null) {
            return usuarioAtual;
        }

        List<Usuario> usuarios = cadastroUsuario.getUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println("É necessário cadastrar um usuário primeiro.");
            cadastrarUsuario();
            return usuarioAtual;
        }

        selecionarUsuarioAtivo();
        return usuarioAtual;
    }

    private void listarEventos() {
        exibirListaEventos(eventos, "EVENTOS CADASTRADOS", "Nenhum evento cadastrado.");
    }

    private void listarEventosOrdenados() {
        List<Evento> ordenados = OrdenacaoEventos.ordenarPorHorario(eventos);
        exibirListaEventos(ordenados, "EVENTOS ORDENADOS POR HORÁRIO", "Nenhum evento cadastrado.");
    }

    private void listarEventosAcontecendoAgora() {
        List<Evento> emAndamento = OrdenacaoEventos.eventosAcontecendoAgora(eventos);
        exibirListaEventos(emAndamento, "EVENTOS ACONTECENDO AGORA", "Nenhum evento acontecendo no momento.");
    }

    private void listarEventosJaOcorreram() {
        List<Evento> passados = OrdenacaoEventos.eventosJaOcorreram(eventos);
        exibirListaEventos(passados, "EVENTOS QUE JÁ OCORRERAM", "Nenhum evento ocorreu até o momento.");
    }

    private void exibirListaEventos(List<Evento> lista, String titulo, String vazioMsg) {
        if (lista.isEmpty()) {
            System.out.println(vazioMsg);
            return;
        }

        System.out.println("========== " + titulo + " ==========");
        for (int i = 0; i < lista.size(); i++) {
            System.out.println((i + 1) + " - " + lista.get(i).exibirDetalhes());
        }
    }

    private void participarDeEvento() {
        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento disponível para participação.");
            return;
        }

        Usuario usuario = garantirUsuarioAtivo();
        if (usuario == null) {
            return;
        }

        listarEventos();
        int escolha = lerIndice(1, eventos.size(), "Digite o número do evento para participar: ");
        if (escolha == -1) {
            return;
        }

        Evento eventoSelecionado = eventos.get(escolha - 1);
        if (gerenciadorParticipacoes.participar(usuario.getEmail(), eventoSelecionado)) {
            System.out.println("Participação confirmada no evento: " + eventoSelecionado.getNome());
        } else {
            System.out.println("Você já está participando deste evento ou não foi possível confirmar.");
        }
    }

    private void cancelarParticipacao() {
        Usuario usuario = garantirUsuarioAtivo();
        if (usuario == null) {
            return;
        }

        List<Evento> meusEventos = gerenciadorParticipacoes.listarMeusEventos(usuario.getEmail());
        if (meusEventos.isEmpty()) {
            System.out.println("Você não possui participações para cancelar.");
            return;
        }

        exibirListaEventos(meusEventos, "MEUS EVENTOS", "Você não possui participações.");
        int escolha = lerIndice(1, meusEventos.size(), "Digite o número do evento para cancelar participação: ");
        if (escolha == -1) {
            return;
        }

        Evento eventoSelecionado = meusEventos.get(escolha - 1);
        if (gerenciadorParticipacoes.cancelarParticipacao(usuario.getEmail(), eventoSelecionado)) {
            System.out.println("Participação cancelada no evento: " + eventoSelecionado.getNome());
        } else {
            System.out.println("Não foi possível cancelar a participação.");
        }
    }

    private void listarMeusEventos() {
        Usuario usuario = garantirUsuarioAtivo();
        if (usuario == null) {
            return;
        }

        List<Evento> meusEventos = gerenciadorParticipacoes.listarMeusEventos(usuario.getEmail());
        exibirListaEventos(meusEventos, "MEUS EVENTOS (" + usuario.getNome() + ")",
                "Você ainda não confirmou participação em nenhum evento.");
    }

    private int lerIndice(int min, int max, String mensagem) {
        while (true) {
            System.out.print(mensagem + "(ou 0 para voltar): ");
            String entrada = scanner.nextLine().trim();
            if (entrada.equals("0")) {
                return -1;
            }
            try {
                int valor = Integer.parseInt(entrada);
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println("Digite um número entre " + min + " e " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido.");
            }
        }
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
