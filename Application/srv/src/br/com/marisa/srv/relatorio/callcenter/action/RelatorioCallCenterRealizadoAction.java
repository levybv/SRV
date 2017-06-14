package br.com.marisa.srv.relatorio.callcenter.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.excecoes.ApresentacaoException;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.relatorio.business.RelatorioCallCenterBusiness;
import br.com.marisa.srv.util.tools.Csv;
import br.com.marisa.srv.util.tools.PoiExcel;

/**
 * 
 * @author levy.villar
 *
 */
public class RelatorioCallCenterRealizadoAction extends BasicAction {

	private static final Logger log = Logger.getLogger(RelatorioCallCenterRealizadoAction.class);

	private static final String FORWARD_RELATORIO_CALL_CENTER_REALIZADO = "relatorio";

	private static final String FILENAME = "Relatorio.xml";
	private static final int BUFSIZE = 1024;
	private static final int MAX_REGISTROS = 20000;

	/**
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward inicio(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		return pMapping.findForward(FORWARD_RELATORIO_CALL_CENTER_REALIZADO);
	}

	/**
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SRVException
	 */
	public ActionForward geraRelatorioCallCenterRealizado(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws IOException, ServletException, SRVException {

		try {

			String mes = getStringParam(pRequest, "mes");
			String ano = getStringParam(pRequest, "ano");
			Date inicio = getDateParam(pRequest, "inicioF");
			Date fim = getDateParam(pRequest, "fimF");

			List<Map<Object,Object>> listaRelatorio = new ArrayList<Map<Object,Object>>();

			List<String> nomeCampos = new ArrayList<String>();
			List<String> listaChaveMap = new ArrayList<String>();
			Map<String,String> subTitulo = new HashMap<String,String>();

			String nomeArquivo = null;

			listaRelatorio = RelatorioCallCenterBusiness.getInstance().obtemRelatorioCallCenterRealizado(ano, mes, inicio, fim);

			nomeArquivo = "relatorio_call_center_realizado";
			subTitulo.put("NOME", "Relatório de Premiação Call Center (Realizado)");

			nomeCampos.add("CPF Vendedor");
			nomeCampos.add("Filial RH");
			nomeCampos.add("Matrícula");
			nomeCampos.add("Nome Vendedor");
			nomeCampos.add("Cargo");
			nomeCampos.add("Data Admissão");
			nomeCampos.add("Data Demissão");
			nomeCampos.add("Status do Funcionário");
			nomeCampos.add("Data da Venda");
			nomeCampos.add("Código do Indicador");
			nomeCampos.add("Descrição do Indicador");
			nomeCampos.add("Quantidade");

			listaChaveMap.add("CPF_VENDEDOR");
			listaChaveMap.add("FILIAL_RH");
			listaChaveMap.add("MATRICULA");
			listaChaveMap.add("NOME_VENDEDOR");
			listaChaveMap.add("CARGO");
			listaChaveMap.add("DT_ADMISSAO");
			listaChaveMap.add("DT_DEMISSAO");
			listaChaveMap.add("STATUS_FUNCIONARIO");
			listaChaveMap.add("DT_VENDA");
			listaChaveMap.add("COD_INDIC");
			listaChaveMap.add("DESCR_INDIC");
			listaChaveMap.add("QT_VENDAS");

			if (listaRelatorio.size() > MAX_REGISTROS) {

				Csv csv = new Csv();
				String texto = csv.montaTxt(nomeCampos, listaRelatorio,listaChaveMap);

				FileOutputStream fileOut = new FileOutputStream(nomeArquivo.concat(".txt"));
				fileOut.write(texto.getBytes());
				fileOut.flush();
				fileOut.close();

				pResponse.setContentType("application/octet-stream");
				String arq = "attachment;filename=" + nomeArquivo.concat(".txt");
				pResponse.setHeader("Content-Disposition", arq);

				ServletOutputStream os = pResponse.getOutputStream();
				os.write(texto.getBytes());
				os.flush();
				os.close();

			} else {

				ByteArrayOutputStream baos = PoiExcel.montaPlanilha("Relatorio", subTitulo, nomeCampos, listaRelatorio, listaChaveMap);

				int length = 0;
				ServletOutputStream op = pResponse.getOutputStream();
				pResponse.setContentType("application/octet-stream");
				pResponse.setContentLength(baos.size());

				if ((nomeArquivo == null) || (nomeArquivo.equals(""))) {
					nomeArquivo = FILENAME;
				}

				pResponse.setHeader("Content-Disposition", "attachment; filename=\"" + nomeArquivo + ".xls" + "\"");

				byte[] bbuf = new byte[BUFSIZE];
				ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

				while ((bais != null) && ((length = bais.read(bbuf)) != -1)) {
					op.write(bbuf, 0, length);
				}

				bais.close();
				op.flush();
				op.close();
			}

		} catch (ApresentacaoException ex) {
			setMensagemErro(pRequest, ex.getMessage());
			return  pMapping.findForward(RelatorioCallCenterRealizadoAction.FORWARD_RELATORIO_CALL_CENTER_REALIZADO);
		} catch (PersistenciaException e) {
			throw new ApresentacaoException(log, "Não foi possível gerar relatório Call Center (Realizado) em excel ou csv.", e);
		} catch (Exception e) {
			throw new ApresentacaoException(log, "Não foi possível gerar relatório Call Center (Realizado) em excel ou csv.", e);
		}

		return null;
	}

}