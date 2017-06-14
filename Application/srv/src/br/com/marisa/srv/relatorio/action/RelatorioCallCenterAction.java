package br.com.marisa.srv.relatorio.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.relatorio.business.RelatorioBusiness;
import br.com.marisa.srv.relatorio.vo.RelatorioTipoVO;
import br.com.marisa.srv.relatorio.vo.RelatorioVO;
import br.com.marisa.srv.util.tools.Csv;
import br.com.marisa.srv.util.tools.RelatorioDinamicoTools;

/**
 * 
 * @author levy.villar
 *
 */
public class RelatorioCallCenterAction extends BasicAction {

	private static final String FORWARD_RELATORIO_CALL_CENTER = "relatorio";

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
		RelatorioVO pesquisaVO = new RelatorioVO();
		RelatorioTipoVO relatorioTipoVO = new RelatorioTipoVO();
		relatorioTipoVO.setCodigo(Constantes.RELATORIO_TIPO_CALL_CENTER_OUTROS);
		pesquisaVO.setRelatorioTipoVO(relatorioTipoVO);
		pesquisaVO.setIsAtivo(true);
		pRequest.setAttribute("listaRelatorioCallCenterOutros", RelatorioBusiness.getInstance().obtemRelatorio(pesquisaVO));
		return pMapping.findForward(FORWARD_RELATORIO_CALL_CENTER);
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
	public ActionForward geraRelatorioCallCenter(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {

		try {
			int codigoRelatorio = getIntegerParam(pRequest, "codigoRelatorio");
			int ano = getIntegerParam(pRequest, "ano");
			int mes = getIntegerParam(pRequest, "mes");

			List<String> nomeCampos = new ArrayList<String>();
			List<String> listaChaveMap = new ArrayList<String>();
			List<Map<Object,Object>> listaRelatorio = new ArrayList<Map<Object,Object>>();

			RelatorioVO relatorioDinamicoVO = RelatorioBusiness.getInstance().obtemRelatorio(codigoRelatorio, ano, mes);

			if (listaRelatorio.size() > Constantes.EXCEL_MAX_REGISTROS) {
		
				Csv csv = new Csv();
				String texto = csv.montaTxt(nomeCampos, listaRelatorio, listaChaveMap);
		
				FileOutputStream fileOut = new FileOutputStream(relatorioDinamicoVO.getNomeArquivo().concat(".txt"));
				fileOut.write(texto.getBytes());
				fileOut.flush();
				fileOut.close();
		
				pResponse.setContentType("application/octet-stream");
				String arq = "attachment;filename=" + relatorioDinamicoVO.getNomeArquivo().concat(".txt");
				pResponse.setHeader("Content-Disposition", arq);
		
				ServletOutputStream os = pResponse.getOutputStream();
				os.write(texto.getBytes());
				os.flush();
				os.close();
		
			} else {
		
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
			}
		} catch (Exception ex) {
			throw new SRVException("Erro ao gerar o relatorio Call Center (Outros)", ex);
		}

		return null;
	}

}