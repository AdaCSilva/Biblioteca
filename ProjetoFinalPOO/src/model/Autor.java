package model;

// Classe que representa um autor
public class Autor extends Pessoa {
    private String nacionalidade;

    public Autor(int id, String nome, String nacionalidade) {
        super(id, nome);
        this.nacionalidade = nacionalidade;
    }

    public Autor() {
        
    }    
    
    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    @Override
    public String getDescricao() {
        return "Autor: " + getNome() + " - Nacionalidade: " + nacionalidade;
    }
    
    @Override
    public String toString() {
        return getNome(); // Exibe apenas o nome do autor
    }
    
}