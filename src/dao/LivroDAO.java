package dao;

import model.Livro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;

public class LivroDAO {

	public void inserir(Livro livro) {
		String sql = "INSERT INTO livros (titulo, autor, preco, qtd, data_lancamento, categoria, ativo) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = Conexao.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, livro.getTitulo());
			stmt.setString(2, livro.getAutor());
			stmt.setDouble(3, livro.getPreco());
			stmt.setInt(4, livro.getQtd());
			stmt.setDate(5, livro.getDataLancamento() != null ? Date.valueOf(livro.getDataLancamento()) : null);
			stmt.setString(6, livro.getCategoria().toString());
			stmt.setBoolean(7, livro.isAtivo());

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			new Alert(Alert.AlertType.ERROR, "Erro ao inserir: " + e.getMessage()).showAndWait();
		}
	}

	public void atualizar(Livro livro) {
		String sql = "UPDATE livros SET titulo=?, autor=?, preco=?, qtd=?, data_lancamento=?, categoria=?, ativo=? "
				+ "WHERE id=?";
		try (Connection conn = Conexao.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, livro.getTitulo());
			stmt.setString(2, livro.getAutor());
			stmt.setDouble(3, livro.getPreco());
			stmt.setInt(4, livro.getQtd());
			stmt.setDate(5, livro.getDataLancamento() != null ? Date.valueOf(livro.getDataLancamento()) : null);
			stmt.setString(6, livro.getCategoria().toString());
			stmt.setBoolean(7, livro.isAtivo());
			stmt.setInt(8, livro.getId());

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			new Alert(Alert.AlertType.ERROR, "Erro ao atualizar: " + e.getMessage()).showAndWait();
		}
	}

	public void excluir(int id) {
		String sql = "DELETE FROM livros WHERE id=?";
		try (Connection conn = Conexao.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			new Alert(Alert.AlertType.ERROR, "Erro ao carregar livros: " + e.getMessage()).showAndWait();
		}
	}

	public List<Livro> listarTodos() {
		List<Livro> lista = new ArrayList<>();
		String sql = "SELECT * FROM livros";
		try (Connection conn = Conexao.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				Livro l = new Livro();
				l.setId(rs.getInt("id"));
				l.setTitulo(rs.getString("titulo"));
				l.setAutor(rs.getString("autor"));
				l.setPreco(rs.getDouble("preco"));
				l.setQtd(rs.getInt("qtd"));

				Date data = rs.getDate("data_lancamento");
				if (data != null) {
					l.setDataLancamento(data.toLocalDate());
				}

				l.setCategoria(Enum.valueOf(model.Categoria.class, rs.getString("categoria")));
				l.setAtivo(rs.getBoolean("ativo"));
				lista.add(l);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			new Alert(Alert.AlertType.ERROR, "Erro ao carregar livros: " + e.getMessage()).showAndWait();
		}
		return lista;
	}
}