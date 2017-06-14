package br.com.marisa.srv.relatorio.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.relatorio.business.RelatorioBusiness;
import br.com.marisa.srv.relatorio.business.RelatorioTipoBusiness;
import br.com.marisa.srv.relatorio.vo.RelatorioTipoVO;
import br.com.marisa.srv.relatorio.vo.RelatorioVO;

public class RelatorioDinamicoAction extends BasicAction {

	private static final String FORWARD_RELATORIO_DINAMICO = "relatorioDinamico";

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
		pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH, Boolean.TRUE);
		return montaTelaInicial(pMapping, pForm, pRequest, pResponse);
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
	public ActionForward incluiRelatorioDinamico(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			RelatorioVO incluiVO = new RelatorioVO();
			incluiVO.setNome(getStringParam(pRequest, "nomeRelatorioDinamicoI"));
			incluiVO.setDescricao(getStringParam(pRequest, "descricaoRelatorioDinamicoI"));
			RelatorioTipoVO incluiTipoVO = new RelatorioTipoVO();
			incluiTipoVO.setCodigo(getIntegerParam(pRequest, "tipoRelatorioDinamicoI"));
			incluiVO.setRelatorioTipoVO(incluiTipoVO);
			incluiVO.setNomeArquivo(getStringParam(pRequest, "nomeArquivoRelatorioDinamicoI"));
			incluiVO.setTitulo(getStringParam(pRequest, "tituloRelatorioDinamicoI"));
			incluiVO.setDescricaoColunas(getStringParam(pRequest, "colunasRelatorioDinamicoI"));
			incluiVO.setIsAtivo(getBoolParam(pRequest, "ativoRelatorioDinamicoI"));
			incluiVO.setIsPeriodo(getBoolParam(pRequest, "periodoRelatorioDinamicoI"));
			incluiVO.setNomeTabela(getStringParam(pRequest, "tabelaRelatorioDinamicoI"));
			incluiVO.setComandoSQL(getStringParam(pRequest, "comandoRelatorioDinamicoI"));
			incluiVO.setQtdRepetirParametro(getIntegerParam(pRequest, "qtdRepParamRelatorioDinamicoI"));
			incluiVO.setIdUsuario(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

			RelatorioBusiness.getInstance().incluiRelatorio(incluiVO);
			setMensagem(pRequest, "Relatório incluído com sucesso");

			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}

		return pMapping.findForward(PAGINA_INICIAL);
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
	public ActionForward alteraRelatorioDinamico(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			RelatorioVO alteraVO = new RelatorioVO();
			alteraVO.setCodigo(getIntegerParam(pRequest, "idRelatorioA"));
			alteraVO.setNome(getStringParam(pRequest, "nomeRelatorioDinamicoA"));
			alteraVO.setDescricao(getStringParam(pRequest, "descricaoRelatorioDinamicoA"));
			RelatorioTipoVO incluiTipoVO = new RelatorioTipoVO();
			incluiTipoVO.setCodigo(getIntegerParam(pRequest, "tipoRelatorioDinamicoA"));
			alteraVO.setRelatorioTipoVO(incluiTipoVO);
			alteraVO.setNomeArquivo(getStringParam(pRequest, "nomeArquivoRelatorioDinamicoA"));
			alteraVO.setTitulo(getStringParam(pRequest, "tituloRelatorioDinamicoA"));
			alteraVO.setDescricaoColunas(getStringParam(pRequest, "colunasRelatorioDinamicoA"));
			alteraVO.setIsAtivo(getBoolParam(pRequest, "ativoRelatorioDinamicoA"));
			alteraVO.setIsPeriodo(getBoolParam(pRequest, "periodoRelatorioDinamicoA"));
			alteraVO.setNomeTabela(getStringParam(pRequest, "tabelaRelatorioDinamicoA"));
			alteraVO.setComandoSQL(getStringParam(pRequest, "comandoRelatorioDinamicoA"));
			alteraVO.setQtdRepetirParametro(getIntegerParam(pRequest, "qtdRepParamRelatorioDinamicoA"));
			alteraVO.setIdUsuario(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

			RelatorioBusiness.getInstance().alteraRelatorio(alteraVO);
			setMensagem(pRequest, "Relatório alterado com sucesso");

			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}

		return pMapping.findForward(PAGINA_INICIAL);
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
	public ActionForward excluiRelatorioDinamico(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			RelatorioVO excluiVO = new RelatorioVO();
			excluiVO.setCodigo(getIntegerParam(pRequest, "idRelatorioE"));
			excluiVO.setIdUsuario(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

			RelatorioBusiness.getInstance().excluiRelatorio(excluiVO);
			setMensagem(pRequest, "Relatório excluído com sucesso");

			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}

		return pMapping.findForward(PAGINA_INICIAL);
	}

	/**
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @throws SRVException
	 */
	private ActionForward montaTelaInicial(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {

		RelatorioVO pesquisaVO = new RelatorioVO();
		RelatorioTipoVO relatorioTipoVO = new RelatorioTipoVO();

		pesquisaVO.setCodigo(getIntegerParam(pRequest, "idRelatorioDinamicoF"));
		relatorioTipoVO.setCodigo(getIntegerParam(pRequest, "tipoRelatorioDinamicoF"));
		pesquisaVO.setRelatorioTipoVO(relatorioTipoVO);
		pesquisaVO.setNome(getStringParam(pRequest, "nomeRelatorioDinamicoF"));
		pesquisaVO.setDescricao(getStringParam(pRequest, "descricaoRelatorioDinamicoF"));
		pesquisaVO.setIsAtivo(getStringParam(pRequest, "ativoRelatorioDinamicoF")==null?null:getBooleanParam(pRequest, "ativoRelatorioDinamicoF"));
		pesquisaVO.setIsPeriodo(getStringParam(pRequest, "periodoRelatorioDinamicoF")==null?null:getBooleanParam(pRequest, "periodoRelatorioDinamicoF"));

		pRequest.setAttribute("listaRelatorioDinamico", RelatorioBusiness.getInstance().obtemRelatorio(pesquisaVO));
		pRequest.setAttribute("listaTipoRelatorio", RelatorioTipoBusiness.getInstance().obtemRelatorioTipo(null));
		pRequest.setAttribute("pesquisaRelatorioDinamicoVO",  pesquisaVO);

		return pMapping.findForward(FORWARD_RELATORIO_DINAMICO);
	}

}