package model;

import java.time.LocalDate;

public class Livro {
	private int id;
	private String titulo;
	private double preco;
	private int qtd;
	private LocalDate dataLancamento;
	private String autor;
	private boolean ativo;
	private Categoria categoria;

	// Construtor Padrão
	public Livro() {
	}

	// Construtor Personalizado (sem o id)
	public Livro(String titulo, double preco, int qtd, LocalDate dataLancamento, String autor, boolean ativo,
			Categoria categoria) {
		this.titulo = titulo;
		this.preco = preco;
		this.qtd = qtd;
		this.dataLancamento = dataLancamento;
		this.autor = autor;
		this.ativo = ativo;
		this.categoria = categoria;
	}

	// Getters e Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public int getQtd() {
		return qtd;
	}

	public void setQtd(int qtd) {
		this.qtd = qtd;
	}

	public LocalDate getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(LocalDate dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
}