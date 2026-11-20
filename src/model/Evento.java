package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Classe abstrata que representa um evento em Porto Alegre.
 *
 * Guarda os atributos comuns a todo evento e define dois métodos abstratos
 * que cada subclasse implementa do seu jeito (polimorfismo):
 * {@link #exibirDetalhes()} e {@link #paraLinhaArquivo()}.
 *
 * Formato da linha no events.data (campos separados por ponto e vírgula):
 * tipo;nome;endereco;categoria;dd/MM/yyyy HH:mm;duracaoMin;descricao;extra
 */
public abstract class Evento {

    /** Formato de data e hora usado no console e no events.data. */
    public static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final String nome;
    private final String endereco;
    private final Categoria categoria;
    private final LocalDateTime inicio;
    private final int duracaoMin;
    private final String descricao;

    /**
     * @throws IllegalArgumentException se algum campo obrigatório estiver vazio
     *                                  ou se a duração não for positiva
     */
    protected Evento(String nome, String endereco, Categoria categoria,
                     LocalDateTime inicio, int duracaoMin, String descricao) {
        this.nome = textoObrigatorio(nome, "nome");
        this.endereco = textoObrigatorio(endereco, "endereço");
        if (categoria == null) {
            throw new IllegalArgumentException("A categoria é obrigatória.");
        }
        this.categoria = categoria;
        if (inicio == null) {
            throw new IllegalArgumentException("O horário é obrigatório.");
        }
        this.inicio = inicio;
        if (duracaoMin <= 0) {
            throw new IllegalArgumentException("A duração deve ser maior que zero (em minutos).");
        }
        this.duracaoMin = duracaoMin;
        this.descricao = textoObrigatorio(descricao, "descrição");
    }

    // ------------------------------------------------------------------
    // Getters (encapsulamento: os atributos são privados e imutáveis)
    // ------------------------------------------------------------------

    public String getNome() { return nome; }

    public String getEndereco() { return endereco; }

    public Categoria getCategoria() { return categoria; }

    public LocalDateTime getInicio() { return inicio; }

    public int getDuracaoMin() { return duracaoMin; }

    public String getDescricao() { return descricao; }

    // ------------------------------------------------------------------
    // Controle de horário
    // ------------------------------------------------------------------

    /** Horário de término = início + duração. */
    public LocalDateTime getFim() {
        return inicio.plusMinutes(duracaoMin);
    }

    /** Verdadeiro se o evento está acontecendo neste momento. */
    public boolean emAndamento() {
        return emAndamento(LocalDateTime.now());
    }

    /** Versão com horário informado (útil para testes). */
    public boolean emAndamento(LocalDateTime agora) {
        return !agora.isBefore(inicio) && agora.isBefore(getFim());
    }

    /** Verdadeiro se o evento já terminou. */
    public boolean jaOcorreu() {
        return jaOcorreu(LocalDateTime.now());
    }

    /** Versão com horário informado (útil para testes). */
    public boolean jaOcorreu(LocalDateTime agora) {
        return !agora.isBefore(getFim());
    }

    /** Texto do status atual: "Em breve", "Em andamento" ou "Já ocorreu". */
    public String getStatus() {
        return getStatus(LocalDateTime.now());
    }

    public String getStatus(LocalDateTime agora) {
        if (jaOcorreu(agora)) {
            return "Já ocorreu";
        }
        return emAndamento(agora) ? "Em andamento" : "Em breve";
    }

    // ------------------------------------------------------------------
    // Métodos abstratos (cada subclasse implementa o seu)
    // ------------------------------------------------------------------

    /** Texto com os detalhes do evento, pronto para exibir no console. */
    public abstract String exibirDetalhes();

    /** Linha que representa o evento no arquivo events.data. */
    public abstract String paraLinhaArquivo();

    // ------------------------------------------------------------------
    // Apoio para as subclasses
    // ------------------------------------------------------------------

    /** Parte dos detalhes que é igual para qualquer tipo de evento. */
    protected String detalhesComuns() {
        return nome + " [" + getStatus() + "]" + System.lineSeparator()
                + "  Categoria: " + categoria.getRotulo() + System.lineSeparator()
                + "  Início: " + inicio.format(FORMATO_DATA)
                + " | Término: " + getFim().format(FORMATO_DATA) + System.lineSeparator()
                + "  Descrição: " + descricao + System.lineSeparator();
    }

    /** Monta a linha do events.data com os campos comuns mais tipo e campo extra. */
    protected String montarLinha(String tipo, String extra) {
        return String.join(";",
                tipo,
                limpar(nome),
                limpar(endereco),
                categoria.name(),
                inicio.format(FORMATO_DATA),
                String.valueOf(duracaoMin),
                limpar(descricao),
                limpar(extra));
    }

    /** Remove ponto e vírgula e quebras de linha para não quebrar o formato do arquivo. */
    protected static String limpar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace(';', ',').replaceAll("[\\r\\n]+", " ").trim();
    }

    /** Valida e devolve um texto obrigatório (sem espaços nas pontas). */
    protected static String textoObrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O campo " + campo + " é obrigatório.");
        }
        return valor.trim();
    }

    // ------------------------------------------------------------------
    // Leitura de uma linha do events.data
    // ------------------------------------------------------------------

    /**
     * Cria o evento (da subclasse correta) a partir de uma linha do events.data.
     * Pode ser usada pela classe ArquivoEventos ao carregar o arquivo.
     *
     * @throws IllegalArgumentException se a linha estiver fora do formato
     */
    public static Evento deLinhaArquivo(String linha) {
        if (linha == null || linha.isBlank()) {
            throw new IllegalArgumentException("Linha vazia.");
        }
        String[] c = linha.split(";", -1);
        if (c.length != 8) {
            throw new IllegalArgumentException("Linha inválida (esperados 8 campos): " + linha);
        }
        try {
            Categoria categoria = Categoria.deTexto(c[3]);
            LocalDateTime inicio = LocalDateTime.parse(c[4].trim(), FORMATO_DATA);
            int duracao = Integer.parseInt(c[5].trim());
            switch (c[0].trim().toUpperCase()) {
                case "PRESENCIAL":
                    return new EventoPresencial(c[1], c[2], categoria, inicio, duracao, c[6], c[7]);
                case "ONLINE":
                    return new EventoOnline(c[1], c[2], categoria, inicio, duracao, c[6], c[7]);
                default:
                    throw new IllegalArgumentException("Tipo de evento desconhecido: " + c[0]);
            }
        } catch (DateTimeParseException | NumberFormatException e) {
            throw new IllegalArgumentException("Linha com dado inválido: " + linha, e);
        }
    }
}
