package controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import dao.Conexao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

// Imports do JasperReports
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class PrincipalController {

	@FXML
	private AnchorPane centroPane;

	@FXML
	public void initialize() {
		abrirHome(); // Carrega a tela inicial assim que o sistema abre
	}

	@FXML
	private void abrirHome() {
		carregarTela("/view/HomeView.fxml");
	}

	@FXML
	private void abrirLivros() {
		carregarTela("/view/LivrosView.fxml");
	}

	@FXML
	private void abrirSobre() {
		carregarTela("/view/SobreView.fxml");
	}

	@FXML
	private void fecharAplicacao() {
		System.exit(0);
	}

	private void carregarTela(String fxml) {
		try {
			Node node = FXMLLoader.load(getClass().getResource(fxml));
			centroPane.getChildren().setAll(node);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void relLiv() {
		try {
			// parâmetros
			Map<String, Object> map = new HashMap<>();
			
			// Se você tiver a imagem logo.png na pasta imagens, ela carrega aqui. 
			// Caso prefira evitar problemas com a imagem por enquanto, pode comentar esta linha:
			URL urlLogo = getClass().getResource("/imagens/logo.png");
			if (urlLogo != null) {
				map.put("logo", new javax.swing.ImageIcon(urlLogo).getImage());
			}

			// busca do arquivo .jasper na pasta Relatorios
			URL arquivo = getClass().getResource("/Relatorios/livros.jasper");
			if (arquivo == null) {
				throw new JRException("Arquivo livros.jasper não encontrado na pasta Relatorios.");
			}

			// Carrega e preenche o relatório diretamente pelo arquivo (evita o erro de NoClassDefFoundError)
			File fileRelatorio = new File(arquivo.toURI());
			JasperPrint jp = JasperFillManager.fillReport(fileRelatorio.getAbsolutePath(), map, Conexao.getConnection());

			// Visualizador do Jasper
			JasperViewer jv = new JasperViewer(jp, false);
			jv.setVisible(true);

			// Geração e abertura do PDF
			String caminhoPdf = "C:/Users/Public/livros.pdf";
			File filePdf = new File(caminhoPdf);
			if (filePdf.exists()) {
				filePdf.delete();
			}

			JasperExportManager.exportReportToPdfFile(jp, caminhoPdf);
			filePdf = new File(caminhoPdf);
			
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(filePdf);
			}

		} catch (Exception e) {
			e.printStackTrace();
			new Alert(Alert.AlertType.ERROR, "Erro ao gerar o relatório: " + e.getMessage()).showAndWait();
		}
	}

}