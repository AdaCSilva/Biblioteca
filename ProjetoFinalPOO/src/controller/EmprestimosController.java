package controller;

import model.Emprestimo;
import model.Livro;
import model.Aluno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.DatabaseConnection;

public class EmprestimosController {

    public static boolean adicionarEmprestimo(Emprestimo emprestimo) {
        String sql = "INSERT INTO emprestimos (livroId, alunoId, dataEmprestimo, dataDevolucao) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, emprestimo.getLivro().getId());
            stmt.setInt(2, emprestimo.getAluno().getId());
            stmt.setDate(3, new java.sql.Date(emprestimo.getDataEmprestimo().getTime()));
            stmt.setDate(4, new java.sql.Date(emprestimo.getDataDevolucao().getTime()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Emprestimo buscarPorId(int id) {
        Emprestimo emprestimo = null;

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT e.id, e.dataEmprestimo, e.dataDevolucao, l.id AS livroId, l.titulo, a.id AS alunoId, a.nome " +
                         "FROM emprestimos e " +
                         "INNER JOIN livros l ON e.livro_id = l.id " +
                         "INNER JOIN alunos a ON e.aluno_id = a.id " +
                         "WHERE e.id = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                emprestimo = new Emprestimo(
                    rs.getInt("id"),
                    new Livro(rs.getInt("livroId"), rs.getString("titulo"), null, true),
                    new Aluno(rs.getInt("alunoId"), rs.getString("nome"), rs.getString("matricula")),
                    rs.getDate("data_emprestimo"),
                    rs.getDate("data_devolucao")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return emprestimo;
    }
    
    public static List<Emprestimo> listarEmprestimos() {
        String sql = "SELECT e.id, l.id AS livroId, l.titulo, a.id AS alunoId, a.nome, e.dataEmprestimo, e.dataDevolucao " +
                     "FROM emprestimos e " +
                     "JOIN livros l ON e.livroId = l.id " +
                     "JOIN alunos a ON e.alunoId = a.id";
        List<Emprestimo> emprestimos = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Livro livro = new Livro(rs.getInt("livroId"), rs.getString("titulo"), null, true);
                Aluno aluno = new Aluno(rs.getInt("alunoId"), rs.getString("nome"), null);
                Emprestimo emprestimo = new Emprestimo(
                    rs.getInt("id"),
                    livro,
                    aluno,
                    rs.getDate("dataEmprestimo"),
                    rs.getDate("dataDevolucao")
                );
                emprestimos.add(emprestimo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emprestimos;
    }

    public static boolean atualizarEmprestimo(Emprestimo emprestimo) {
        String sql = "UPDATE emprestimos SET livroId = ?, alunoId = ?, dataEmprestimo = ?, dataDevolucao = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, emprestimo.getLivro().getId());
            stmt.setInt(2, emprestimo.getAluno().getId());
            stmt.setDate(3, new java.sql.Date(emprestimo.getDataEmprestimo().getTime()));
            stmt.setDate(4, new java.sql.Date(emprestimo.getDataDevolucao().getTime()));
            stmt.setInt(5, emprestimo.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean removerEmprestimo(int id) {
        String sql = "DELETE FROM emprestimos WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
