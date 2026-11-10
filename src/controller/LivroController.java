package controller;

import java.util.List;
import dao.LivroDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import model.Categoria;
import model.Livro;

public class LivroController {

    private Livro livroEmEdicao = null;
    private LivroDAO dao;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnExcluir;

    @FXML
    private Button btnNovo;

    @FXML
    private Button btnSalvar;

    @FXML
    private CheckBox chkAtivo;

    @FXML
    private ComboBox<Categoria> cmbCategoria;

    @FXML
    private TableColumn<Livro, String> colAtivo;

    @FXML
    private TableColumn<Livro, String> colAutor;

    @FXML
    private TableColumn<Livro, Categoria> colCategoria;

    @FXML
    private TableColumn<Livro, Double> colPreco;

    @FXML
    private TableColumn<Livro, Integer> colQtd;

    @FXML
    private TableColumn<Livro, String> colTitulo;

    @FXML
    private DatePicker dpDataLancamento;

    @FXML
    private GridPane formPane;

    @FXML
    private TableView<Livro> tblLivros;

    @FXML
    private TextField txtAutor;

    @FXML
    private TextField txtPreco;

    @FXML
    private TextField txtQtd;

    @FXML
    private TextField txtTitulo;

    @FXML
    public void initialize() {
        // Configuração das colunas
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colQtd.setCellValueFactory(new PropertyValueFactory<>("qtd"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colAtivo.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().isAtivo() ? "Sim" : "Não"));

        // Inicializa o DAO e lista os livros do banco
        dao = new LivroDAO();
        List<Livro> listaLivros = dao.listarTodos();
        ObservableList<Livro> lista = FXCollections.observableArrayList(listaLivros);
        tblLivros.setItems(lista);

        // Listener da seleção da tabela
        tblLivros.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                preencherFormulario(newValue);
            }
        });

        // Popula o ComboBox com todos os valores do enum Categoria
        cmbCategoria.getItems().addAll(Categoria.values());
        
        // Deixa o primeiro item selecionado por padrão
        cmbCategoria.getSelectionModel().selectFirst();

        // Carrega os ícones nos botões
        try {
            defineIcones();
        } catch (Exception e) {
            // Ignora se as imagens não estiverem presentes na pasta /imagens
        }
    }

    private void defineIcones() {
        btnNovo.setGraphic(new ImageView(getClass().getResource("/imagens/adicionar.png").toExternalForm()));
        btnEditar.setGraphic(new ImageView(getClass().getResource("/imagens/editar.png").toExternalForm()));
        btnSalvar.setGraphic(new ImageView(getClass().getResource("/imagens/salvar.png").toExternalForm()));
        btnCancelar.setGraphic(new ImageView(getClass().getResource("/imagens/cancelar.png").toExternalForm()));
        btnExcluir.setGraphic(new ImageView(getClass().getResource("/imagens/lixo.png").toExternalForm()));
    }

    private void preencherFormulario(Livro livro) {
        txtTitulo.setText(livro.getTitulo());
        txtAutor.setText(livro.getAutor());
        txtPreco.setText(String.valueOf(livro.getPreco()));
        txtQtd.setText(String.valueOf(livro.getQtd()));
        dpDataLancamento.setValue(livro.getDataLancamento());
        cmbCategoria.setValue(livro.getCategoria());
        chkAtivo.setSelected(livro.isAtivo());
    }

    @FXML
    private void novoLivro() {
        // Limpa o formulário
        txtTitulo.clear();
        txtAutor.clear();
        txtPreco.clear();
        txtQtd.clear();
        dpDataLancamento.setValue(null);
        cmbCategoria.setValue(null);
        chkAtivo.setSelected(false);

        // Habilita o formulário e trava a tabela
        tblLivros.setDisable(true);
        formPane.setDisable(false);

        // Troca o estado dos botões
        btnSalvar.setDisable(false);
        btnCancelar.setDisable(false);
        btnNovo.setDisable(true);
        btnEditar.setDisable(true);
        btnExcluir.setDisable(true);

        // Reseta flag para novo registro
        livroEmEdicao = null;
    }

    @FXML
    private void editarLivro() {
        Livro selecionado = tblLivros.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            new Alert(Alert.AlertType.WARNING, "Selecione um livro para editar!").showAndWait();
            return;
        }

        // Habilita o formulário e trava a tabela
        tblLivros.setDisable(true);
        formPane.setDisable(false);

        // Troca o estado dos botões
        btnSalvar.setDisable(false);
        btnCancelar.setDisable(false);
        btnNovo.setDisable(true);
        btnEditar.setDisable(true);
        btnExcluir.setDisable(true);

        // Marca que estamos editando esse livro
        livroEmEdicao = selecionado;
    }

    @FXML
    private void salvarLivro() {
        if (livroEmEdicao == null) {
            // Criar novo
            Livro novo = new Livro();
            preencherObjeto(novo);
            dao.inserir(novo);
        } else {
            // Atualizar existente
            preencherObjeto(livroEmEdicao);
            tblLivros.refresh();
            dao.atualizar(livroEmEdicao);
        }

        // Busca os dados novamente no banco
        tblLivros.setItems(FXCollections.observableArrayList(dao.listarTodos()));

        // Trava formulário e libera a tabela
        formPane.setDisable(true);
        tblLivros.setDisable(false);

        // Restaura botões
        btnSalvar.setDisable(true);
        btnCancelar.setDisable(true);
        btnNovo.setDisable(false);
        btnEditar.setDisable(false);
        btnExcluir.setDisable(false);
    }

    @FXML
    private void cancelarEdicao() {
        // Limpa seleção e formulário
        tblLivros.getSelectionModel().clearSelection();
        limparFormulario();

        // Desabilita formulário e libera a tabela
        formPane.setDisable(true);
        tblLivros.setDisable(false);

        // Restaura estado dos botões
        btnSalvar.setDisable(true);
        btnCancelar.setDisable(true);
        btnNovo.setDisable(false);
        btnEditar.setDisable(false);
        btnExcluir.setDisable(false);

        // Zera flag de edição
        livroEmEdicao = null;
    }

    @FXML
    private void excluirLivro() {
        Livro selecionado = tblLivros.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            new Alert(Alert.AlertType.WARNING, "Selecione um livro para excluir!").showAndWait();
            return;
        }

        // Confirmação
        Alert alert = new Alert(
            Alert.AlertType.CONFIRMATION, 
            "Deseja realmente excluir o livro \"" + selecionado.getTitulo() + "\"?", 
            ButtonType.YES, 
            ButtonType.NO
        );
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            dao.excluir(selecionado.getId());
            // Busca os dados novamente no banco
            tblLivros.setItems(FXCollections.observableArrayList(dao.listarTodos()));
            limparFormulario();
            formPane.setDisable(true);
        }
    }

    private void limparFormulario() {
        txtTitulo.clear();
        txtAutor.clear();
        txtPreco.clear();
        txtQtd.clear();
        dpDataLancamento.setValue(null);
        cmbCategoria.setValue(null);
        chkAtivo.setSelected(false);
    }

    private void preencherObjeto(Livro livro) {
        livro.setTitulo(txtTitulo.getText());
        livro.setAutor(txtAutor.getText());
        livro.setPreco(Double.parseDouble(txtPreco.getText()));
        livro.setQtd(Integer.parseInt(txtQtd.getText()));
        livro.setDataLancamento(dpDataLancamento.getValue());
        livro.setCategoria(cmbCategoria.getValue());
        livro.setAtivo(chkAtivo.isSelected());
    }
}