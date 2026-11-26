package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Responsável pela ordenação e consulta de eventos por horário.
 *
 */
public class OrdenacaoEventos {

    /**
     * Ordena os eventos pelo horário de início.
     */
    public static List<Evento> ordenarPorHorario(List<Evento> eventos) {

        List<Evento> ordenados = new ArrayList<>(eventos);

        ordenados.sort(Comparator.comparing(Evento::getInicio));

        return ordenados;
    }

    /**
     * Retorna os eventos que estão acontecendo agora.
     */
    public static List<Evento> eventosAcontecendoAgora(List<Evento> eventos) {

        List<Evento> resultado = new ArrayList<>();

        for (Evento evento : eventos) {
            if (evento.emAndamento()) {
                resultado.add(evento);
            }
        }

        return resultado;
    }

    /**
     * Retorna os eventos que já ocorreram.
     */
    public static List<Evento> eventosJaOcorreram(List<Evento> eventos) {

        List<Evento> resultado = new ArrayList<>();

        for (Evento evento : eventos) {
            if (evento.jaOcorreu()) {
                resultado.add(evento);
            }
        }

        return resultado;
    }

    /**
     * Retorna os eventos que ainda vão acontecer.
     */
    public static List<Evento> eventosFuturos(List<Evento> eventos) {

        List<Evento> resultado = new ArrayList<>();

        LocalDateTime agora = LocalDateTime.now();

        for (Evento evento : eventos) {
            if (evento.getInicio().isAfter(agora)) {
                resultado.add(evento);
            }
        }

        return resultado;
    }
}
