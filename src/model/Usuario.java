
package model;

public class Usuario {

    private String nome;
    private String email;
    private int idade;
    private String bairro;

    public Usuario(String nome, String email, int idade, String bairro) {
        setNome(nome);
        setEmail(email);
        setIdade(idade);
        setBairro(bairro);
    }

    // Validação do nome
    public void setNome(String nome) {
        if (nome == null || nome.trim().length() < 2) {
            throw new IllegalArgumentException(
                "O nome deve possuir pelo menos 2 caracteres."
            );
        }

        this.nome = nome.trim();
    }

    // Validação do e-mail
    public void setEmail(String email) {
        if (email == null ||
            !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {

            throw new IllegalArgumentException(
                "Informe um e-mail válido."
            );
        }

        this.email = email.trim().toLowerCase();
    }

    // Validação da idade
    public void setIdade(int idade) {
        if (idade <= 0 || idade > 120) {
            throw new IllegalArgumentException(
                "Informe uma idade válida."
            );
        }

        this.idade = idade;
    }

    // Validação do bairro
    public void setBairro(String bairro) {
        if (bairro == null || bairro.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "O bairro não pode ficar vazio."
            );
        }

        this.bairro = bairro.trim();
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public int getIdade() {
        return idade;
    }

    public String getBairro() {
        return bairro;
    }

    // Exibição dos dados do usuário
    @Override
    public String toString() {
        return "Usuario{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", idade=" + idade +
                ", bairro='" + bairro + '\'' +
                '}';
    }
}
