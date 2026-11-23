package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Gerencia as participações dos usuários nos eventos.
 * O participante deve ser identificado por um ID único,
 * fornecido pelo sistema de usuários.
 */
public class GerenciadorParticipacoes {

    private final List<Participacao> participacoes;

    public GerenciadorParticipacoes() {
        this.participacoes = new ArrayList<>();
    }

    /**
     * Inscreve um participante em um evento.
     * @param participante ID único do participante.
     */
    public boolean participar(String participante, Evento evento) {
        if (participante == null || participante.trim().isEmpty()
                || evento == null) {
            return false;
        }

        if (jaParticipa(participante, evento)) {
            return false;
        }

        participacoes.add(new Participacao(participante.trim(), evento));
        return true;
    }

    /**
     * Cancela a participação de um participante em um evento.
     */
    public boolean cancelarParticipacao(String participante, Evento evento) {
        if (participante == null || evento == null) {
            return false;
        }

        return participacoes.removeIf(p ->
                p.getParticipante().equals(participante.trim())
                && p.getEvento() == evento
        );
    }

    /**
     * Retorna os eventos de um participante.
     * @param participante ID único do participante.
     */
    public List<Evento> listarMeusEventos(String participante) {
        List<Evento> meusEventos = new ArrayList<>();

        if (participante == null || participante.trim().isEmpty()) {
            return meusEventos;
        }

        for (Participacao p : participacoes) {
            if (p.getParticipante().equals(participante.trim())) {
                meusEventos.add(p.getEvento());
            }
        }

        return meusEventos;
    }

    private boolean jaParticipa(String participante, Evento evento) {
        for (Participacao p : participacoes) {
            if (p.getParticipante().equals(participante.trim())
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
