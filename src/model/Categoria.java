package model;

import java.text.Normalizer;

/**
 * Categorias permitidas para os eventos do sistema.
 * Usar um enum garante que só existam as categorias definidas pelo grupo.
 */
public enum Categoria {
    MUSICA("Música"),
    ESPORTE("Esporte"),
    CULTURA("Cultura"),
    GASTRONOMIA("Gastronomia"),
    TECNOLOGIA("Tecnologia"),
    EDUCACAO("Educação");

    private final String rotulo;

    Categoria(String rotulo) {
        this.rotulo = rotulo;
    }

    /** Nome amigável, usado nas telas do console. */
    public String getRotulo() {
        return rotulo;
    }

    /**
     * Converte um texto em categoria, aceitando o nome do enum (MUSICA)
     * ou o rótulo (Música), sem diferenciar maiúsculas e acentos.
     *
     * @throws IllegalArgumentException se o texto não corresponder a nenhuma categoria
     */
    public static Categoria deTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("A categoria é obrigatória.");
        }
        String procurado = semAcento(texto.trim());
        for (Categoria c : values()) {
            if (semAcento(c.name()).equalsIgnoreCase(procurado)
                    || semAcento(c.rotulo).equalsIgnoreCase(procurado)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Categoria inválida: " + texto);
    }

    private static String semAcento(String s) {
        return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }

    @Override
    public String toString() {
        return rotulo;
    }
}
