package model;

import java.time.LocalDateTime;

/**
 * Evento transmitido pela internet, mas ligado a Porto Alegre (organizadores ou
 * público da cidade). Além dos dados comuns, guarda o link de acesso.
 */
public class EventoOnline extends Evento {

    private final String link;

    /**
     * @param link endereço de acesso ao evento (obrigatório)
     * @throws IllegalArgumentException se o link estiver vazio
     */
    public EventoOnline(String nome, String endereco, Categoria categoria,
                        LocalDateTime inicio, int duracaoMin, String descricao,
                        String link) {
        super(nome, endereco, categoria, inicio, duracaoMin, descricao);
        this.link = textoObrigatorio(link, "link");
    }

    public String getLink() {
        return link;
    }

    @Override
    public String exibirDetalhes() {
        StringBuilder sb = new StringBuilder(detalhesComuns());
        sb.append("  Tipo: Online").append(System.lineSeparator());
        sb.append("  Organização/local de origem: ").append(getEndereco()).append(System.lineSeparator());
        sb.append("  Link de acesso: ").append(link).append(System.lineSeparator());
        return sb.toString();
    }

    @Override
    public String paraLinhaArquivo() {
        return montarLinha("ONLINE", link);
    }
}
