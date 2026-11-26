
package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CadastroUsuario {

    private final List<Usuario> usuarios = new ArrayList<>();
    private final Scanner scanner;

    public CadastroUsuario(Scanner scanner) {
        this.scanner = scanner;
    }

    public Usuario cadastrarUsuario() {

        System.out.println("\n===== CADASTRO DE USUÁRIO =====");

        String nome = lerTexto("Nome: ");
        String email = lerEmail();
        int idade = lerIdade();
        String bairro = lerTexto("Bairro: ");

        try {
            Usuario usuario = new Usuario(nome, email, idade, bairro);

            usuarios.add(usuario);

            System.out.println("\nCadastro realizado com sucesso!");
            System.out.println("Bem-vindo(a), " + usuario.getNome() + "!");

            return usuario;

        } catch (IllegalArgumentException e) {
            System.out.println("\nErro no cadastro: " + e.getMessage());
            return null;
        }
    }

    // Validação de campos de texto
    private String lerTexto(String mensagem) {
        String texto;

        do {
            System.out.print(mensagem);
            texto = scanner.nextLine().trim();

            if (texto.isEmpty()) {
                System.out.println("Este campo não pode ficar vazio.");
            }

        } while (texto.isEmpty());

        return texto;
    }

    // Validação e verificação de e-mail duplicado
    private String lerEmail() {
        String email;

        do {
            System.out.print("E-mail: ");
            email = scanner.nextLine().trim().toLowerCase();

            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                System.out.println("E-mail inválido. Tente novamente.");
                email = "";
                continue;
            }

            if (emailExiste(email)) {
                System.out.println("Este e-mail já está cadastrado.");
                email = "";
            }

        } while (email.isEmpty());

        return email;
    }

    // Validação de idade
    private int lerIdade() {
        int idade;

        while (true) {
            System.out.print("Idade: ");
            String entrada = scanner.nextLine();

            try {
                idade = Integer.parseInt(entrada);

                if (idade > 0 && idade <= 120) {
                    return idade;
                }

                System.out.println("Informe uma idade entre 1 e 120.");

            } catch (NumberFormatException e) {
                System.out.println("Digite uma idade numérica válida.");
            }
        }
    }

    // Verifica se o e-mail já está cadastrado
    private boolean emailExiste(String email) {
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }

        return false;
    }

    // Retorna a lista de usuários cadastrados
    public List<Usuario> getUsuarios() {
        return usuarios;
    }
}
