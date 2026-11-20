package model;

import java.time.LocalDateTime;

/**
 * Evento que acontece em um local físico de Porto Alegre.
 * Além dos dados comuns, guarda um ponto de referência para facilitar a chegada.
 */
public class EventoPresencial extends Evento {

    private final String pontoReferencia;

    /**
     * @param pontoReferencia ponto de referência do local (opcional, pode ser vazio)
     */
    public EventoPresencial(String nome, String endereco, Categoria categoria,
                            LocalDateTime inicio, int duracaoMin, String descricao,
                            String pontoReferencia) {
        super(nome, endereco, categoria, inicio, duracaoMin, descricao);
        this.pontoReferencia = pontoReferencia == null ? "" : pontoReferencia.trim();
    }

    public String getPontoReferencia() {
        return pontoReferencia;
    }

    @Override
    public String exibirDetalhes() {
        StringBuilder sb = new StringBuilder(detalhesComuns());
        sb.append("  Tipo: Presencial").append(System.lineSeparator());
        sb.append("  Endereço: ").append(getEndereco()).append(System.lineSeparator());
        if (!pontoReferencia.isEmpty()) {
            sb.append("  Ponto de referência: ").append(pontoReferencia).append(System.lineSeparator());
        }
        return sb.toString();
    }

    @Override
    public String paraLinhaArquivo() {
        return montarLinha("PRESENCIAL", pontoReferencia);
    }
}
