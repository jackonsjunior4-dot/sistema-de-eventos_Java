package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsável por salvar e carregar os eventos no arquivo events.data.
 *
 * IMPORTANTE:
 * A classe Evento (e suas subclasses) precisa implementar Serializable
 * para que a gravação por serialização funcione.
 */
public class PersistenciaEventos {
    private static final String ARQUIVO = "events.data";
static {
    System.out.println("Arquivo salvo em: " + new java.io.File(ARQUIVO).getAbsolutePath());
}

    public static void salvar(List<Evento> eventos) {
        try (ObjectOutputStream saida =
                     new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            saida.writeObject(new ArrayList<>(eventos));
            System.out.println("Eventos salvos com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar os eventos: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Evento> carregar() {
        File arquivo = new File(ARQUIVO);

        // Na primeira execução, o arquivo ainda não existe.
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream entrada =
                     new ObjectInputStream(new FileInputStream(arquivo))) {
            Object objeto = entrada.readObject();

            if (objeto instanceof List<?>) {
                return (List<Evento>) objeto;
            }

            System.out.println("O arquivo events.data possui um formato inválido.");
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            System.out.println("Não foi possível carregar os eventos: " + e.getMessage());
        }

        return new ArrayList<>();
    }
}
