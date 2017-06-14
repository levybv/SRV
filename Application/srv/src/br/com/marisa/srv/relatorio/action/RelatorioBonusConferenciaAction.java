package br.com.marisa.srv.relatorio.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.calendario.business.CalendarioComercialBusiness;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.relatorio.business.RelatorioBusiness;
import br.com.marisa.srv.relatorio.business.RelatorioGenericBusiness;
import br.com.marisa.srv.relatorio.vo.RelatorioTipoVO;
import br.com.marisa.srv.relatorio.vo.RelatorioVO;
import br.com.marisa.srv.util.tools.RelatorioDinamicoTools;

public class RelatorioBonusConferenciaAction extends BasicAction {

	private static final String FORWARD_RELATORIO_BONUS_CONFERENCIA = "relatorio";

	/**
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws SRVException
	 */
	public ActionForward inicio(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {
		pRequest.getSession().setAttribute("sessionTokenRefresh", Boolean.TRUE);
		return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse);
	}

	/**
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws SRVException
	 */
	public ActionForward montaTelaPrincipal(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {
		List listaRelatorio = new ArrayList();
		List listaPeriodos = CalendarioComercialBusiness.getInstance().obtemListaPeriodoMesAno();
		pRequest.setAttribute("periodosMesAno", listaPeriodos);

		String status = getStringParam(pRequest, "status");
		String periodos = getStringParam(pRequest, "periodos");

		pRequest.setAttribute("periodos", periodos);
		pRequest.setAttribute("status", status);

		if (periodos != null) {
			Integer ano = new Integer(
					Integer.parseInt(periodos.substring(0, 4)));
			Integer mes = new Integer(Integer.parseInt(periodos.substring(periodos.length() - 2, periodos.length())));

			listaRelatorio = RelatorioGenericBusiness.getInstance().pesquisaRelatorioBonusConferencia(status, mes, ano, false);

			pRequest.setAttribute("listaRelatorioBonus", listaRelatorio);
		} else {
			listaRelatorio = RelatorioGenericBusiness.getInstance().pesquisaRelatorioBonusConferencia(status, null, null, false);
			pRequest.setAttribute("listaRelatorioBonus", listaRelatorio);
		}

		return pMapping.findForward(FORWARD_RELATORIO_BONUS_CONFERENCIA);
	}

	/**
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws SRVException
	 */
	public ActionForward exportaRelatorioExcel(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {

		try {

			String periodos = getStringParam(pRequest, "periodosS");
			Integer ano = null;
			Integer mes = null;

			if (periodos != null) {
				ano = new Integer(Integer.parseInt(periodos.substring(0, 4)));
				mes = new Integer(Integer.parseInt(periodos.substring(periodos.length() - 2, periodos.length())));
			}

			RelatorioVO pesquisaVO = new RelatorioVO();
			RelatorioTipoVO tipoPesquisaVO = new RelatorioTipoVO();
			tipoPesquisaVO.setCodigo(Constantes.RELATORIO_TIPO_BONUS_POR_CONFERENCIA);
			pesquisaVO.setRelatorioTipoVO(tipoPesquisaVO);
			List<RelatorioVO> listaBonus = RelatorioBusiness.getInstance().obtemRelatorio(pesquisaVO);
			RelatorioVO relatorioDinamicoVO = RelatorioBusiness.getInstance().obtemRelatorio(listaBonus.get(0).getCodigo(), ano, mes);

			ByteArrayOutputStream baos = null;
			baos = RelatorioDinamicoTools.montaPlanilha(relatorioDinamicoVO);
	
			int length = 0;
			ServletOutputStream op = pResponse.getOutputStream();
			pResponse.setContentType("application/octet-stream");
			pResponse.setContentLength(baos.size());
	
			pResponse.setHeader("Content-Disposition", "attachment; filename=\"" + relatorioDinamicoVO.getNomeArquivo() + ".xls" + "\"");
	
			byte[] bbuf = new byte[Constantes.BUFFER_SIZE_1024];
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
	
			while ((bais != null) && ((length = bais.read(bbuf)) != -1)) {
				op.write(bbuf, 0, length);
			}
	
			bais.close();
			op.flush();
			op.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}