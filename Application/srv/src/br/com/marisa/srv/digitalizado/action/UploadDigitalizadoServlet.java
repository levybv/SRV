package br.com.marisa.srv.digitalizado.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import br.com.marisa.srv.digitalizado.business.DigitalizadoBusiness;
import br.com.marisa.srv.digitalizado.vo.DigitalizadoVO;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.StringHelper;

public class UploadDigitalizadoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(UploadDigitalizadoServlet.class);

	private final short PLANILHA = 0;
	private final short CELL_SAX_COD_CONTRATO = 1;
	private final short CELL_SAX_CLI_CPF = 2;
	private final short CELL_SAX_COD_FILIAL = 4;

	private final short CELL_PL_COD_FILIAL = 0;
	private final short CELL_PL_CPF_VENDEDOR = 1;
	private final short CELL_PL_CLI_CPF = 2;

	private final String IMPORTACAO_CARTAO_PL = "PL";
	private final String IMPORTACAO_SAX = "SAX";

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		importa(request,response);
	}

	private void importa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		List erros = new ArrayList();
		String ano = null;
		String mes = null;
		String tipoImportacao = null;
		InputStream uploadedStream = null;

		try {
			String contentType = request.getContentType();
			if ((contentType != null) && (contentType.indexOf("multipart/form-data") >= 0)) {

				ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
				List fileItemsList = servletFileUpload.parseRequest(request);

				Iterator it = fileItemsList.iterator();
				while (it.hasNext()){
					FileItem fileItem = (FileItem)it.next();
					if (fileItem.isFormField()){
						//CAMPOS
						if ( fileItem.getFieldName().equalsIgnoreCase("mes") ) {
							mes = StringHelper.completarNumeroComZerosEsquerda(Integer.valueOf(fileItem.getString()).intValue(),2);
						}
						if ( fileItem.getFieldName().equalsIgnoreCase("ano") ) {
							ano = StringHelper.completarNumeroComZerosEsquerda(Integer.valueOf(fileItem.getString()).intValue(),4);
						}
						if ( fileItem.getFieldName().equalsIgnoreCase("tipo") ) {
							if ( fileItem.getString().equalsIgnoreCase(IMPORTACAO_CARTAO_PL) ) {
								tipoImportacao = IMPORTACAO_CARTAO_PL;
							} else if ( fileItem.getString().equalsIgnoreCase(IMPORTACAO_SAX) ) {
								tipoImportacao = IMPORTACAO_SAX;
							} else {
								erros.add("Tipo de arquivo importado não informado!");
								request.getSession().setAttribute("erros", erros);
								response.sendRedirect("/srv/digitalizado.do?operacao=inicio");
								return;
							}
						}
					} else {
						//ARQUIVO
						if (tipoImportacao != null && tipoImportacao.equalsIgnoreCase(IMPORTACAO_SAX)) {
							if ( DigitalizadoBusiness.getInstance().existeTabelaPeriodoSax(ano, mes) ) {
								if (fileItem.getName().toUpperCase().endsWith(".XLS") ) {
									uploadedStream = fileItem.getInputStream();
									processaArquivoSAX(uploadedStream, ano, mes, erros);
								} else {
									erros.add("Formato de arquivo inválido. Envie apenas arquivos com extensão .XLS");
									request.getSession().setAttribute("erros", erros);
									response.sendRedirect("/srv/digitalizado.do?operacao=inicio");
									return;
								}
							} else {
								erros.add("Período selecionado '" + mes + "/" + ano + "' inválido!");
								request.getSession().setAttribute("erros", erros);
								response.sendRedirect("/srv/digitalizado.do?operacao=inicio");
								return;
							}
						} else if (tipoImportacao != null && tipoImportacao.equalsIgnoreCase(IMPORTACAO_CARTAO_PL)) {
							if ( DigitalizadoBusiness.getInstance().existeTabelaPeriodoPL(ano, mes) ) {
								if (fileItem.getName().toUpperCase().endsWith(".XLS") ) {
									uploadedStream = fileItem.getInputStream();
									processaArquivoPL(uploadedStream, ano, mes, erros);
								} else {
									erros.add("Formato de arquivo inválido. Envie apenas arquivos com extensão .XLS");
									request.getSession().setAttribute("erros", erros);
									response.sendRedirect("/srv/digitalizado.do?operacao=inicio");
									return;
								}
							} else {
								erros.add("Período selecionado '" + mes + "/" + ano + "' inválido!");
								request.getSession().setAttribute("erros", erros);
								response.sendRedirect("/srv/digitalizado.do?operacao=inicio");
								return;
							}
						}
					}
				}
			}

			if (erros != null && erros.size() > 0) {
				request.getSession().setAttribute("erros", erros);
			} else {
				request.getSession().setAttribute("mensagem", "Importação realizada com sucesso.");
			}			

		} catch (Exception e) {
			erros.add("Ocorreu erro não esperado no processo de importação: " + e.getMessage());
			request.getSession().setAttribute("erros", erros);
			log.error("Ocorreu erro ao importar Digitalizados.", e);
			e.printStackTrace();
		}
		
		response.sendRedirect("/srv/digitalizado.do?operacao=inicio");
		return;
	}

	private void processaArquivoPL(InputStream uploadedStream, String ano, String mes, List erros) throws SRVException, IOException {
		HSSFWorkbook wb = new HSSFWorkbook(uploadedStream);
		HSSFSheet mySheet = wb.getSheetAt(PLANILHA);
		Iterator rowIter = mySheet.rowIterator();
		int rowNum = 0;
		while(rowIter.hasNext()){
			HSSFRow myRow = (HSSFRow) rowIter.next();
			if (rowNum != 0) {
				DigitalizadoVO vo = new DigitalizadoVO();
				vo.setFilCod((int)myRow.getCell(CELL_PL_COD_FILIAL).getNumericCellValue());
				vo.setCpfVendedor((long)myRow.getCell(CELL_PL_CPF_VENDEDOR).getNumericCellValue());
				vo.setCliCpf((long)myRow.getCell(CELL_PL_CLI_CPF).getNumericCellValue());
				vo.setAno(ano);
				vo.setMes(mes);
				if ( DigitalizadoBusiness.getInstance().existeDigitalizadoPL(vo) ) {
					DigitalizadoBusiness.getInstance().alteraFlgDocDigitalizadoPL(vo);
				} else {
					erros.add("Linha " + (rowNum+1) + ": Dados não encontrado - Filial: " + vo.getFilCod() + " Vendedor: " + vo.getCpfVendedor() + " CPF: " + vo.getCliCpf());
				}
			}
			rowNum++;
		}

	}

	private void processaArquivoSAX(InputStream uploadedStream, String ano, String mes, List erros) throws SRVException, IOException {
		HSSFWorkbook wb = new HSSFWorkbook(uploadedStream);
		HSSFSheet mySheet = wb.getSheetAt(PLANILHA);
		Iterator rowIter = mySheet.rowIterator();
		int rowNum = 0;
		while(rowIter.hasNext()){
			HSSFRow myRow = (HSSFRow) rowIter.next();
			if (rowNum != 0) {
				DigitalizadoVO vo = new DigitalizadoVO();
				vo.setFilCod((int)myRow.getCell(CELL_SAX_COD_FILIAL).getNumericCellValue());
				vo.setCodContrato(StringHelper.completarNumeroComZerosEsquerda((long)myRow.getCell(CELL_SAX_COD_CONTRATO).getNumericCellValue(),12));
				vo.setCliCpf((long)myRow.getCell(CELL_SAX_CLI_CPF).getNumericCellValue());
				vo.setAno(ano);
				vo.setMes(mes);
				if ( DigitalizadoBusiness.getInstance().existeDigitalizadoSax(vo) ) {
					DigitalizadoBusiness.getInstance().alteraFlgDocDigitalizadoSAX(vo);
				} else {
					erros.add("Linha " + (rowNum+1) + ": Dados não encontrado - Filial: " + vo.getFilCod() + " Contrato: " + vo.getCodContrato() + " CPF: " + vo.getCliCpf());
				}
			}
			rowNum++;
		}

	}

}