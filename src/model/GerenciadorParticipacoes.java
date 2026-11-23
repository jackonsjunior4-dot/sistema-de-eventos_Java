package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Gerencia as participações dos usuários nos eventos.
 */
public class GerenciadorParticipacoes {

    private final List<Participacao> participacoes;

    public GerenciadorParticipacoes() {
        this.participacoes = new ArrayList<>();
    }

    /**
     * Inscreve um participante em um evento.
     */
    public boolean participar(String participante, Evento evento) {
        if (participante == null || participante.isBlank() || evento == null) {
            return false;
        }

        if (jaParticipa(participante, evento)) {
            return false;
        }

        participacoes.add(new Participacao(participante.trim(), evento));
        return true;
    }

    /**
     * Cancela a participação em um evento.
     */
    public boolean cancelarParticipacao(String participante, Evento evento) {
        if (participante == null || evento == null) {
            return false;
        }

        return participacoes.removeIf(p ->
                p.getParticipante().equalsIgnoreCase(participante.trim())
                && p.getEvento() == evento
        );
    }

    /**
     * Retorna os eventos de um participante.
     */
    public List<Evento> listarMeusEventos(String participante) {
        List<Evento> meusEventos = new ArrayList<>();

        if (participante == null || participante.isBlank()) {
            return meusEventos;
        }

        for (Participacao p : participacoes) {
            if (p.getParticipante().equalsIgnoreCase(participante.trim())) {
                meusEventos.add(p.getEvento());
            }
        }

        return meusEventos;
    }

    private boolean jaParticipa(String participante, Evento evento) {
        for (Participacao p : participacoes) {
            if (p.getParticipante().equalsIgnoreCase(participante.trim())
                    && p.getEvento() == evento) {
                return true;
            }
        }
        return false;
    }

    /**
     * Representa uma inscrição de um participante em um evento.
     */
    private static class Participacao {
        private final String participante;
        private final Evento evento;

        public Participacao(String participante, Evento evento) {
            this.participante = participante;
            this.evento = evento;
        }

        public String getParticipante() {
            return participante;
        }

        public Evento getEvento() {
            return evento;
        }
    }
}
