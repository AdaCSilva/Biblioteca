package controller;

import model.Autor;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutoresController {

    public static boolean adicionarAutor(Autor autor) {
        String sql = "INSERT INTO autores (nome, nacionalidade) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, autor.getNome());
            stmt.setString(2, autor.getNacionalidade());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para buscar um único autor
    public static Autor buscarAutor(int id) {
        Autor autor = null;
        String sql = "SELECT * FROM autores WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    String nacionalidade = rs.getString("nacionalidade");
                    autor = new Autor(id, nome, nacionalidade);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return autor;
    }
    
    // Método para listar todos os autores no banco de dados
    public static List<Autor> listarAutores() {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT * FROM autores";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String nacionalidade = rs.getString("nacionalidade");

                autores.add(new Autor(id, nome, nacionalidade));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return autores;
    }

    // Método para atualizar um autor no banco de dados
    public static boolean atualizarAutor(Autor autor) {
        String sql = "UPDATE autores SET nome = ?, nacionalidade = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, autor.getNome());
            stmt.setString(2, autor.getNacionalidade());
            stmt.setInt(3, autor.getId());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para remover um autor do banco de dados
    public static boolean removerAutor(int id) {
        String sql = "DELETE FROM autores WHERE id = ?";

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
