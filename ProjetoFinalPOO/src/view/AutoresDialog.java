package view;

import controller.AutoresController;
import model.Autor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import model.Livro;

public class AutoresDialog extends JDialog {

    private JTable tabelaAutores;
    private DefaultTableModel modeloTabela;

    public AutoresDialog(JFrame parent, boolean modal) {
        super(parent, "Gerenciar Autores", modal);
        setSize(600, 400);
        setLocationRelativeTo(parent);

        // Configuração da tabela
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "Nacionalidade"}, 0);
        tabelaAutores = new JTable(modeloTabela);
        tabelaAutores.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(tabelaAutores);
        add(scrollPane, BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton btnAdicionar = new JButton("Adicionar Autor");
        JButton btnAtualizar = new JButton("Atualizar Lista");
        JButton btnRemover = new JButton("Remover Autor");
        JButton btnAlterar = new JButton("Alterar Autor");

        btnAdicionar.addActionListener(e -> adicionarAutor());
        btnAtualizar.addActionListener(e -> atualizarLista());
        btnRemover.addActionListener(e -> removerAutor());
        btnAlterar.addActionListener(e -> alterarAutor());

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnAlterar);
        add(painelBotoes, BorderLayout.SOUTH);

        // Atualiza a tabela ao abrir a interface
        atualizarLista();
    }

    private void atualizarLista() {
        modeloTabela.setRowCount(0); // Limpa os dados da tabela
        List<Autor> autores = AutoresController.listarAutores();

        for (Autor autor : autores) {
            modeloTabela.addRow(new Object[]{
                    autor.getId(),
                    autor.getNome(),
                    autor.getNacionalidade()
            });
        }
    }

    private void adicionarAutor() {
        String nome = JOptionPane.showInputDialog(this, "Nome do Autor:");
        if (nome == null || nome.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome não pode ser vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nacionalidade = JOptionPane.showInputDialog(this, "Nacionalidade do Autor:");
        if (nacionalidade == null || nacionalidade.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nacionalidade não pode ser vazia.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Autor novoAutor = new Autor(0, nome, nacionalidade);
        if (AutoresController.adicionarAutor(novoAutor)) {
            JOptionPane.showMessageDialog(this, "Autor adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            atualizarLista();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar aluno.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void removerAutor() {
        int linhaSelecionada = tabelaAutores.getSelectedRow(); // Obtém a linha selecionada

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um autor para remover.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obter o ID do autor da tabela
        int idAutor = (int) modeloTabela.getValueAt(linhaSelecionada, 0);

        // Confirmar a remoção
        int confirmacao = JOptionPane.showConfirmDialog(
                this, "Deseja realmente remover este autor?", "Confirmar Remoção", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            if (AutoresController.removerAutor(idAutor)) {
                JOptionPane.showMessageDialog(this, "Autor removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarLista(); // Atualizar tabela
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao remover autor.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void alterarAutor() {
        int linhaSelecionada = tabelaAutores.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um autor para alterar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obter o ID do livro da tabela
        int idAutor = (int) modeloTabela.getValueAt(linhaSelecionada, 0);

        // Obter os dados do livro atual
        String nomeAtual = (String) modeloTabela.getValueAt(linhaSelecionada, 1);
        String nacionalidadeAtual = (String) modeloTabela.getValueAt(linhaSelecionada, 2);

        // Criar campos para edição
        JTextField campoNome = new JTextField(nomeAtual);
        JTextField campoNacionalidade = new JTextField(nacionalidadeAtual);

        Object[] mensagem = {
                "Nome do autor:", campoNome,
                "Nacionalidade do Autor:", campoNacionalidade
        };

        int opcao = JOptionPane.showConfirmDialog(
                this, mensagem, "Alterar Autor", JOptionPane.OK_CANCEL_OPTION);

        if (opcao == JOptionPane.OK_OPTION) {
            String novoNome = campoNome.getText().trim();
            String novaNacionalidade = campoNacionalidade.getText().trim();

            if (novoNome.isEmpty() || novaNacionalidade.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Autor autorAlterado = new Autor(idAutor, novoNome, novaNacionalidade);

            if (AutoresController.atualizarAutor(autorAlterado)) {
                JOptionPane.showMessageDialog(this, "Autor alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarLista(); // Atualizar tabela
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar autor.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
}