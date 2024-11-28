package view;

import controller.AutoresController;
import controller.LivrosController;
import model.Autor;
import model.Livro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LivrosDialog extends JDialog {

    private JTable tabelaLivros;
    private DefaultTableModel modeloTabela;
    private JComboBox<Autor> comboAutores; // JComboBox para seleção de autores

    public LivrosDialog(JFrame parent, boolean modal) {
        super(parent, "Gerenciar Livros", modal);
        setSize(600, 400);
        setLocationRelativeTo(parent);

        // Configuração da tabela
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Título", "Autor", "Disponível"}, 0);
        tabelaLivros = new JTable(modeloTabela);
        tabelaLivros.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(tabelaLivros);
        add(scrollPane, BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton btnAdicionar = new JButton("Adicionar Livro");
        JButton btnAtualizar = new JButton("Atualizar Lista");
        JButton btnRemover = new JButton("Remover Livro");
        JButton btnAlterar = new JButton("Alterar Livro");

        btnAdicionar.addActionListener(e -> adicionarLivro());
        btnAtualizar.addActionListener(e -> atualizarLista());
        btnRemover.addActionListener(e -> removerLivro());
        btnAlterar.addActionListener(e -> alterarLivro());

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
        List<Livro> livros = LivrosController.listarLivros();

        for (Livro livro : livros) {
            modeloTabela.addRow(new Object[]{
                    livro.getId(),
                    livro.getTitulo(),
                    livro.getAutor(), // Armazena o objeto Autor
                    livro.isDisponivel() ? "Sim" : "Não"
            });
        }
    }

    private void adicionarLivro() {
        JTextField campoTitulo = new JTextField();
        comboAutores = new JComboBox<>();
        carregarAutoresNoComboBox(comboAutores); // Popula o combo box com autores existentes

        Object[] mensagem = {
                "Título do Livro:", campoTitulo,
                "Selecione o Autor:", comboAutores
        };

        int opcao = JOptionPane.showConfirmDialog(
                this, mensagem, "Adicionar Livro", JOptionPane.OK_CANCEL_OPTION);

        if (opcao == JOptionPane.OK_OPTION) {
            String titulo = campoTitulo.getText().trim();
            Autor autorSelecionado = (Autor) comboAutores.getSelectedItem();

            if (titulo.isEmpty() || autorSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Livro novoLivro = new Livro(0, titulo, autorSelecionado, true);
            if (LivrosController.adicionarLivro(novoLivro)) {
                JOptionPane.showMessageDialog(this, "Livro adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarLista();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar livro.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removerLivro() {
        int linhaSelecionada = tabelaLivros.getSelectedRow(); // Obtém a linha selecionada

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um livro para remover.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obter o ID do livro da tabela
        int idLivro = (int) modeloTabela.getValueAt(linhaSelecionada, 0);

        // Confirmar a remoção
        int confirmacao = JOptionPane.showConfirmDialog(
                this, "Deseja realmente remover este livro?", "Confirmar Remoção", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            if (LivrosController.removerLivro(idLivro)) {
                JOptionPane.showMessageDialog(this, "Livro removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarLista(); // Atualizar tabela
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao remover livro.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void alterarLivro() {
        int linhaSelecionada = tabelaLivros.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um livro para alterar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obter o ID do livro da tabela
        int idLivro = (int) modeloTabela.getValueAt(linhaSelecionada, 0);

        // Obter os dados do livro atual
        String tituloAtual = (String) modeloTabela.getValueAt(linhaSelecionada, 1);
        Autor autorAtual = (Autor) modeloTabela.getValueAt(linhaSelecionada, 2);
        boolean disponivelAtual = "Sim".equals(modeloTabela.getValueAt(linhaSelecionada, 3));

        // Criar campos para edição
        JTextField campoTitulo = new JTextField(tituloAtual);
        JComboBox<Autor> comboAutores = new JComboBox<>();
        carregarAutoresNoComboBox(comboAutores); // Popula o comboBox com autores existentes
        comboAutores.setSelectedItem(autorAtual); // Seleciona o autor atual no comboBox
        JCheckBox checkDisponivel = new JCheckBox("Disponível", disponivelAtual);

        Object[] mensagem = {
                "Título do Livro:", campoTitulo,
                "Selecione o Autor:", comboAutores,
                "Disponível:", checkDisponivel
        };

        int opcao = JOptionPane.showConfirmDialog(
                this, mensagem, "Alterar Livro", JOptionPane.OK_CANCEL_OPTION);

        if (opcao == JOptionPane.OK_OPTION) {
            String novoTitulo = campoTitulo.getText().trim();
            Autor novoAutor = (Autor) comboAutores.getSelectedItem();
            int autorId = novoAutor.getId();
            boolean novoDisponivel = checkDisponivel.isSelected();

            if (novoTitulo.isEmpty() || novoAutor == null) {
                JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Livro livroAlterado = new Livro(idLivro, novoTitulo, novoAutor, novoDisponivel);

            if (LivrosController.atualizarLivro(livroAlterado)) {
                JOptionPane.showMessageDialog(this, "Livro alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarLista(); // Atualizar tabela
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar livro.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void carregarAutoresNoComboBox(JComboBox<Autor> comboBox) {
        List<Autor> autores = AutoresController.listarAutores();
        comboBox.removeAllItems();

        if (autores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum autor encontrado. Cadastre um autor primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
        } else {
            for (Autor autor : autores) {
                comboBox.addItem(autor);
            }
        }
    }

}
