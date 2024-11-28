package model;

// Classe que representa um aluno
public class Aluno extends Pessoa {
    private String matricula;

    public Aluno(int id, String nome, String matricula) {
        super(id, nome);
        this.matricula = matricula;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    @Override
    public String getDescricao() {
        return "Aluno: " + getNome() + " - Matrícula: " + matricula;
    }
    
    @Override
    public String toString() {
        return getNome(); // Exibe apenas o nome do autor
    }
    
}

