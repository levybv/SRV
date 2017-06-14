package br.com.marisa.srv.ajuste.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
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

import br.com.marisa.srv.funcionario.business.FuncionarioBusiness;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.indicador.business.IndicadorBusiness;
import br.com.marisa.srv.indicador.vo.IndicadorFuncionarioRealizadoVO;

public class UploadAjusteServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(UploadAjusteServlet.class);

	private final short PLANILHA = 0;
	private final short CELL_ID_FILIAL = 0;
	private final short CELL_ID_FUNCIONARIO = 1;
	private final short CELL_ID_INDICADOR = 2;
	private final short CELL_VALOR = 3;
	private final short CELL_MES = 4;
	private final short CELL_ANO = 5;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		importar(request,response);
	}

	private void importar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		List erros = new ArrayList();
		String ano = null;
		String mes = null;
		InputStream uploadedStream = null;

		try {
			String contentType = request.getContentType();
			if ((contentType != null) && (contentType.indexOf("multipart/form-data") >= 0)) {

				ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
				List fileItemsList = servletFileUpload.parseRequest(request);

				Iterator it = fileItemsList.iterator();
				while (it.hasNext()){
					FileItem fileItem = (FileItem)it.next();

					if (!fileItem.isFormField()){
						if (fileItem.getName().toUpperCase().endsWith(".XLS") ) {
							uploadedStream = fileItem.getInputStream();
							processaAjuste(uploadedStream);
						} else {
							erros.add("Formato de arquivo inválido. Envie apenas arquivos com extensão .XLS");
							request.getSession().setAttribute("erros", erros);
							response.sendRedirect("/srv/ajusteFuncionario.do?operacao=inicio");
							return;
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
		
		response.sendRedirect("/srv/ajusteFuncionario.do?operacao=inicio");
		return;
	}

	private void processaAjuste(InputStream uploadedStream) throws SRVException, IOException {
		HSSFWorkbook wb = new HSSFWorkbook(uploadedStream);
		HSSFSheet mySheet = wb.getSheetAt(PLANILHA);
		Iterator rowIter = mySheet.rowIterator();
		int rowNum = 0;
		while(rowIter.hasNext()){
			HSSFRow myRow = (HSSFRow) rowIter.next();
			if (rowNum != 0) {
				Long idFuncionario = new Long((long)myRow.getCell(CELL_ID_FUNCIONARIO).getNumericCellValue());
				FuncionarioVO funcionarioVO = FuncionarioBusiness.getInstance().obtemFuncionario(idFuncionario);

				IndicadorFuncionarioRealizadoVO vo = new IndicadorFuncionarioRealizadoVO();
				vo.setIdEmpresa(funcionarioVO.getIdEmpresa());
				vo.setIdFilial(new Integer((int)myRow.getCell(CELL_ID_FILIAL).getNumericCellValue()));
				vo.setIdFuncionario(idFuncionario);
				vo.setIdIndicador(new Integer((int)myRow.getCell(CELL_ID_INDICADOR).getNumericCellValue()));
				vo.setValorPremioCalculado(new Double(myRow.getCell(CELL_VALOR).getNumericCellValue()));
				vo.setDataUltimaAlteracao(new Date());
				vo.setIdUsuarioAlteracao(new Integer(1));
				vo.setAno(new Integer((int)myRow.getCell(CELL_ANO).getNumericCellValue()));
				vo.setMes(new Integer((int)myRow.getCell(CELL_MES).getNumericCellValue()));
				if (IndicadorBusiness.getInstance().existeRealizadoAjuste(vo)) {
					IndicadorBusiness.getInstance().alteraRealizadoAjuste(vo);
				} else {
					IndicadorBusiness.getInstance().incluiRealizadoAjuste(vo);
				}
			}
			rowNum++;
		}
	}
}