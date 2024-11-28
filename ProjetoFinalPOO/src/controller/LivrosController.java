package controller;

import model.Livro;
import util.DatabaseConnection;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Autor;

public class LivrosController {

    public static boolean adicionarLivro(Livro livro) {
        String sql = "INSERT INTO livros (titulo, autorId, disponivel) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, livro.getTitulo());
            stmt.setInt(2, livro.getAutor().getId());
            stmt.setBoolean(3, livro.isDisponivel());

            return stmt.executeUpdate() > 0; // Retorna true se a inserção for bem-sucedida

        } catch (SQLException e) {
            System.err.println("Erro ao adicionar livro: " + e.getMessage());
        }
        return false;
    }

    public static List<Livro> listarLivros() {
        String sql = "SELECT * FROM livros";
        List<Livro> livros = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Livro livro = new Livro(
                        rs.getInt("id"),
                        rs.getString("titulo"), 
                        AutoresController.buscarAutor(rs.getInt("autorId")),
                        rs.getBoolean("disponivel")
                );
                livros.add(livro);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar livros: " + e.getMessage());
        }

        return livros;
    }
    
    // Método para atualizar um aluno no banco de dados
    public static boolean atualizarLivro(Livro livro) {
        String sql = "UPDATE livros SET titulo = ?, autorId = ?, disponivel = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, livro.getTitulo());
            stmt.setInt(2, livro.getAutor().getId()); // Obtendo o ID do autor
            stmt.setBoolean(3, livro.isDisponivel());
            stmt.setInt(4, livro.getId());
            stmt.executeUpdate();

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean removerLivro(int id) {
        String sql = "DELETE FROM livros WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}