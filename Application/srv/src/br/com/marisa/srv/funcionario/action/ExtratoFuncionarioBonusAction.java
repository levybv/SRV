package br.com.marisa.srv.funcionario.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.bonus.business.ContratoMetaAceiteBusiness;
import br.com.marisa.srv.bonus.business.RealizFuncIndicadorBusiness;
import br.com.marisa.srv.bonus.engine.BonusAnualEngine;
import br.com.marisa.srv.bonus.vo.ContratoMetaAceiteVO;
import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.funcionario.business.FuncionarioBusiness;
import br.com.marisa.srv.funcionario.report.vo.RelatorioBonusAnualFuncionarioVO;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.enumeration.StatCalcRealzEnum;
import br.com.marisa.srv.geral.enumeration.StatusContratoMetaAceiteEnum;
import br.com.marisa.srv.geral.excecoes.MensagemException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.CNPJCPFHelper;
import br.com.marisa.srv.geral.helper.DataHelper;
import br.com.marisa.srv.geral.helper.FileHelper;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.indicador.vo.DetalheCalculoVO;
import br.com.marisa.srv.indicador.vo.IndicadorFuncionarioRealizadoVO;
import br.com.marisa.srv.indicador.vo.RelatorioBonusVO;
import br.com.marisa.srv.perfil.business.PerfilBusiness;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Action para lidar com requisicoes do modulo de bonus
 * 
 * @author Walter Fontes
 */
public class ExtratoFuncionarioBonusAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_PESQUISA_FUNCIONARIOS 	= "funcionario";
	private static final String FORWARD_EXTRATO_FUNCIONARIO 	= "extratoFuncionario";

//	private static final String PROCESSAR_BONUS_ANUAL_INDICADOR_CORPORATIVO_EM_ABERTO = "1";
//	private static final String PROCESSAR_BONUS_ANUAL_ENVIAR_ACEITE_EM_ABERTO = "2";
//	private static final String PROCESSAR_BONUS_ANUAL_FINALIZAR_EM_ANDAMENTO = "3";
	private static final String PROCESSAR_BONUS_ANUAL_CALCULA_RESULTADO = "4";

	/**
	 * Cria um novo objeto ExtratoFuncionarioAction
	 * 
	 * @throws Exception
	 */
	public ExtratoFuncionarioBonusAction() throws Exception {
		//
	}


	/**
	 * Inicia pagina de extrato de funcionarios
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward inicio(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		return montaTelaFuncionario(pMapping, pForm, pRequest, pResponse);
	}
	
	
	/**
	 * Monta os dados da tela
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @throws SRVException 
	 */
	private ActionForward montaTelaFuncionario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {

		Long idFuncionario = getLongParam(pRequest, "idFuncionario");
		String nomeFuncionario = getStringParam(pRequest, "nomeFuncionario");
		String cracha = getStringParam(pRequest, "cracha");
		String cpfFuncionario = getStringParam(pRequest, "cpfFuncionario");

		pRequest.setAttribute("idFuncionario", idFuncionario);
		pRequest.setAttribute("nomeFuncionario", nomeFuncionario);
		pRequest.setAttribute("cracha", cracha);
		pRequest.setAttribute("cpfFuncionario", cpfFuncionario);

		Long cpfFuncionarioL = null;
		if (cpfFuncionario != null && cpfFuncionario.length() > 0) {
			cpfFuncionarioL = new Long(CNPJCPFHelper.retiraCaracteresAlpha(cpfFuncionario));
		}

		
		List<FuncionarioVO> funcionarios = FuncionarioBusiness.getInstance().obtemListaFuncionario(idFuncionario, nomeFuncionario, cracha, cpfFuncionarioL, null, null, null);
		pRequest.setAttribute("funcionarios", funcionarios);

		for (Iterator<FuncionarioVO> it = funcionarios.iterator(); it.hasNext();) {
			FuncionarioVO funcVO = it.next();
			pRequest.setAttribute("situacaoColaborador", funcVO.getSituacaoColaborador());
		}

		return pMapping.findForward(FORWARD_PESQUISA_FUNCIONARIOS);
	}
	
	
	/**
	 * Monta os dados da tela
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @throws SRVException 
	 */
	public ActionForward pesquisaExtratoFuncionario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {

		pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH, Boolean.TRUE);
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_IMPRIMIR, Boolean.FALSE);
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_CALCULAR, Boolean.FALSE);
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_INCLUIR, Boolean.FALSE);
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_ALTERAR, Boolean.FALSE);
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_REMOVER, Boolean.FALSE);
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_STATUS, Boolean.FALSE);

		// Tela atual
		Long idFuncionarioSelecionado = getLongParam(pRequest, "idFuncionarioSelecionado");
		String nomeFuncionarioSelecionado = getStringParam(pRequest, "nomeFuncionarioSelecionado");
		String cargoFuncionarioSelecionado = getStringParam(pRequest, "cargoFuncionarioSelecionado");
		String centroCustoFuncionarioSelecionado = getStringParam(pRequest, "centroCustoFuncionarioSelecionado");
		Integer idFilialSelecionada = getIntegerParam(pRequest, "idFilialSelecionada");
		if (pRequest.getAttribute("idFilialSelecionada") != null) {
			idFilialSelecionada = (Integer)pRequest.getAttribute("idFilialSelecionada");
		}
		String periodoFiltro = getStringParam(pRequest, "periodos");

		// Tela Anterior
		Long idFuncionarioFiltro = getLongParam(pRequest, "idFuncionario");
		String nomeFuncionarioFiltro = getStringParam(pRequest, "nomeFuncionario");
		String crachaFiltro = getStringParam(pRequest, "cracha");
		String cpfFuncionarioFiltro = getStringParam(pRequest, "cpfFuncionario");

		pRequest.setAttribute("situacaoColaborador", getStringParam(pRequest, "situacaoColaborador"));
		pRequest.setAttribute("idFuncionarioSelecionado", idFuncionarioSelecionado);
		pRequest.setAttribute("nomeFuncionarioSelecionado", nomeFuncionarioSelecionado);
		pRequest.setAttribute("cargoFuncionarioSelecionado", cargoFuncionarioSelecionado);
		pRequest.setAttribute("centroCustoFuncionarioSelecionado", centroCustoFuncionarioSelecionado);
		pRequest.setAttribute("idFilialSelecionada", idFilialSelecionada);
		pRequest.setAttribute("idFuncionario", idFuncionarioFiltro);
		pRequest.setAttribute("nomeFuncionario", nomeFuncionarioFiltro);
		pRequest.setAttribute("cracha", crachaFiltro);
		pRequest.setAttribute("cpfFuncionario", cpfFuncionarioFiltro);
		pRequest.setAttribute("periodoSelecionado", periodoFiltro);

		if (ObjectHelper.isNotEmpty(periodoFiltro)) {

			//UsuarioBean usuarioBean = obtemUsuarioDaSessao(pRequest);
			int ano = DataHelper.obtemNumAno(periodoFiltro, DataHelper.PATTERN_DATE_ANO_MES);
			int mes = DataHelper.obtemNumMes(periodoFiltro, DataHelper.PATTERN_DATE_ANO_MES);

			List<FilialVO> listaFilial = RealizFuncIndicadorBusiness.getInstance().obtemListaFilialRealizadoFuncIndicador(idFuncionarioSelecionado, mes, ano, Constantes.ID_TIPO_REM_VAR_CORPORATIVO);
			pRequest.setAttribute("listaFilial", listaFilial);
			if (ObjectHelper.isEmpty(listaFilial)) {
				pRequest.setAttribute(ConstantesRequestSession.BOTAO_INCLUIR, Boolean.TRUE);
				pRequest.setAttribute(ConstantesRequestSession.BOTAO_ALTERAR, Boolean.TRUE);
				pRequest.setAttribute(ConstantesRequestSession.BOTAO_REMOVER, Boolean.TRUE);
				idFilialSelecionada = null;
				pRequest.setAttribute("idFilialSelecionada", idFilialSelecionada);
			}

			if (ObjectHelper.isNotEmpty(idFilialSelecionada)) {

				RelatorioBonusVO relatorioBonusVO = RealizFuncIndicadorBusiness.getInstance().obtemResultadoRealizadoFuncIndicador(idFuncionarioSelecionado, ano, mes, Constantes.CODIGO_EMPRESA, idFilialSelecionada);
				DetalheCalculoVO detalheCalculoVO = RealizFuncIndicadorBusiness.getInstance().obterStatusCalculoRealizado(idFuncionarioSelecionado, ano, mes, Constantes.CODIGO_EMPRESA, idFilialSelecionada);
				pRequest.setAttribute("lista", relatorioBonusVO.getIndicadoresRealizados());
				pRequest.setAttribute("total", relatorioBonusVO.getTotalRealizadoVO());
				pRequest.setAttribute("detalheCalculo", detalheCalculoVO);

				if (detalheCalculoVO.getStatusCalculoRealizado() == StatCalcRealzEnum.NENHUM.getCodigo()) {
					pRequest.setAttribute("statusRalz", StatCalcRealzEnum.NENHUM.getDescricao());
				} else if (detalheCalculoVO.getStatusCalculoRealizado() == StatCalcRealzEnum.INICIADO.getCodigo()) {
					pRequest.setAttribute("statusRalz", StatCalcRealzEnum.INICIADO.getDescricao());
					pRequest.setAttribute(ConstantesRequestSession.BOTAO_CALCULAR, Boolean.TRUE);
					pRequest.setAttribute(ConstantesRequestSession.BOTAO_INCLUIR, Boolean.TRUE);
					pRequest.setAttribute(ConstantesRequestSession.BOTAO_ALTERAR, Boolean.TRUE);
					pRequest.setAttribute(ConstantesRequestSession.BOTAO_REMOVER, Boolean.TRUE);
					pRequest.setAttribute(ConstantesRequestSession.BOTAO_STATUS, Boolean.TRUE);
				} else if (detalheCalculoVO.getStatusCalculoRealizado() == StatCalcRealzEnum.EM_ACEITE.getCodigo()) {
					pRequest.setAttribute("statusRalz", StatCalcRealzEnum.EM_ACEITE.getDescricao());
					pRequest.setAttribute(ConstantesRequestSession.BOTAO_IMPRIMIR, Boolean.TRUE);
					pRequest.setAttribute(ConstantesRequestSession.BOTAO_STATUS, Boolean.TRUE);
				} else if (detalheCalculoVO.getStatusCalculoRealizado() == StatCalcRealzEnum.EM_ANDAMENTO.getCodigo()) {
					pRequest.setAttribute("statusRalz", StatCalcRealzEnum.EM_ANDAMENTO.getDescricao());
					pRequest.setAttribute(ConstantesRequestSession.BOTAO_IMPRIMIR, Boolean.TRUE);
					pRequest.setAttribute(ConstantesRequestSession.BOTAO_STATUS, Boolean.TRUE);
				} else if (detalheCalculoVO.getStatusCalculoRealizado() == StatCalcRealzEnum.FINALIZADO.getCodigo()) {
					pRequest.setAttribute("statusRalz", StatCalcRealzEnum.FINALIZADO.getDescricao());
					pRequest.setAttribute(ConstantesRequestSession.BOTAO_IMPRIMIR, Boolean.TRUE);
					pRequest.setAttribute(ConstantesRequestSession.BOTAO_STATUS, Boolean.TRUE);
//					pRequest.setAttribute(ConstantesRequestSession.BOTAO_REABRIR, new Boolean(PerfilBusiness.getInstance().isUsuarioPodeReabrirBonus(usuarioBean.getUsuarioVO().getIdPerfil())));
				}

				if (ano >= Constantes.NUM_ANO_BASE_ACEITE_ELETRONICO) {
					//Obtem status aceite
					ContratoMetaAceiteVO pesquisaAceiteVO = new ContratoMetaAceiteVO();
					pesquisaAceiteVO.setCodFuncionario(idFuncionarioSelecionado);
					pesquisaAceiteVO.setIdEmpresa(Constantes.CODIGO_EMPRESA);
					pesquisaAceiteVO.setIdFilial(idFilialSelecionada);
					pesquisaAceiteVO.setNumAno(ano);
					pesquisaAceiteVO.setNumMes(mes);
					ContratoMetaAceiteVO bonusAceiteVO = ContratoMetaAceiteBusiness.getInstance().obtemContratoMetaAceite(pesquisaAceiteVO);
					pRequest.setAttribute("statusAceite", bonusAceiteVO.getDescStatusAceite());
				}

			}

		}

		pRequest.setAttribute("exibeBonus", new Boolean(PerfilBusiness.getInstance().obtemPerfilByCod(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdPerfil()).getIsExibeBonus()));

		return pMapping.findForward(FORWARD_EXTRATO_FUNCIONARIO);
	}
	
	/**
	 * Inclui indicador para o funcionario
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @throws SRVException 
	 */
	public ActionForward incluiIndicadorFuncionario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {
		
		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {

			IndicadorFuncionarioRealizadoVO indicadorFuncionarioRealizadoVO = new IndicadorFuncionarioRealizadoVO();
			indicadorFuncionarioRealizadoVO.setIdFuncionario(getLongParam(pRequest, "idFuncionarioID"));
			indicadorFuncionarioRealizadoVO.setIdIndicador(getIntegerParam(pRequest, "idIndicadorID"));
			Integer idFilial = getIntegerParam(pRequest, "idFilialSelecionada");
			if (ObjectHelper.isEmpty(idFilial)) {
				idFilial = FuncionarioBusiness.getInstance().obtemFuncionario(indicadorFuncionarioRealizadoVO.getIdFuncionario()).getIdFilial();
				pRequest.setAttribute("idFilialSelecionada", idFilial);
			}
			indicadorFuncionarioRealizadoVO.setIdFilial(idFilial);
			indicadorFuncionarioRealizadoVO.setIdEmpresa(Constantes.CODIGO_EMPRESA);
			indicadorFuncionarioRealizadoVO.setAno(getIntegerParam(pRequest, "anoID"));
			indicadorFuncionarioRealizadoVO.setMes(getIntegerParam(pRequest, "mesID"));
			indicadorFuncionarioRealizadoVO.setUnidadePeso(Constantes.UNIDADE_PERCENTUAL);
			indicadorFuncionarioRealizadoVO.setPeso(getDoubleParam(pRequest, "pesoID", true));
			indicadorFuncionarioRealizadoVO.setUnidadeMeta(getIntegerParam(pRequest, "idUnidadeMetaID"));
			indicadorFuncionarioRealizadoVO.setMeta(getDoubleParam(pRequest, "metaID", true));
			indicadorFuncionarioRealizadoVO.setUnidadeRealizado(getIntegerParam(pRequest, "idUnidadeRealizadoID"));
			indicadorFuncionarioRealizadoVO.setRealizado(getDoubleParam(pRequest, "realizadoID", true));
			indicadorFuncionarioRealizadoVO.setDataUltimaAlteracao(new Date());
			indicadorFuncionarioRealizadoVO.setIdUsuarioAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

			IndicadorFuncionarioRealizadoVO pesquisaVO = RealizFuncIndicadorBusiness.getInstance().obtemRealizadoFuncIndicador(indicadorFuncionarioRealizadoVO.getIdFuncionario(), 
																																indicadorFuncionarioRealizadoVO.getIdIndicador(), 
																																indicadorFuncionarioRealizadoVO.getIdEmpresa(), 
																																indicadorFuncionarioRealizadoVO.getIdFilial(),
																																indicadorFuncionarioRealizadoVO.getAno(), 
																																indicadorFuncionarioRealizadoVO.getMes());
			if (pesquisaVO != null) {
				setMensagem(pRequest, "Indicador já cadastrado, selecione outro!");
			} else {
				RealizFuncIndicadorBusiness.getInstance().incluiRealizadoFuncIndicador(indicadorFuncionarioRealizadoVO);
				BonusAnualEngine.getInstance().calculaBonusCorporativo(indicadorFuncionarioRealizadoVO.getIdFuncionario(), 
																		indicadorFuncionarioRealizadoVO.getIdFilial(), 
																		indicadorFuncionarioRealizadoVO.getIdEmpresa(),
																		indicadorFuncionarioRealizadoVO.getAno(), 
																		indicadorFuncionarioRealizadoVO.getMes());
				setMensagem(pRequest, "Indicador incluído com sucesso para o funcionário.");
			}
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}

		return pesquisaExtratoFuncionario(pMapping, pForm, pRequest, pResponse);
	}	
	
	
	
	/**
	 * Altera indicador para o funcionario
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @throws SRVException 
	 */
	public ActionForward alteraIndicadorFuncionario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {

			IndicadorFuncionarioRealizadoVO indicadorFuncionarioRealizadoVO = new IndicadorFuncionarioRealizadoVO();
			indicadorFuncionarioRealizadoVO.setIdFuncionario(getLongParam(pRequest, "idFuncionarioID"));
			indicadorFuncionarioRealizadoVO.setIdIndicador(getIntegerParam(pRequest, "idIndicadorID"));
			indicadorFuncionarioRealizadoVO.setIdEmpresa(getIntegerParam(pRequest, "idEmpresaID"));
			indicadorFuncionarioRealizadoVO.setIdFilial(getIntegerParam(pRequest, "idFilialID"));
			indicadorFuncionarioRealizadoVO.setAno(getIntegerParam(pRequest, "anoID"));
			indicadorFuncionarioRealizadoVO.setMes(getIntegerParam(pRequest, "mesID"));
			indicadorFuncionarioRealizadoVO.setUnidadePeso(Constantes.UNIDADE_PERCENTUAL);
			indicadorFuncionarioRealizadoVO.setPeso(getDoubleParam(pRequest, "pesoID", true));
			indicadorFuncionarioRealizadoVO.setUnidadeMeta(getIntegerParam(pRequest, "idUnidadeMetaID"));
			indicadorFuncionarioRealizadoVO.setMeta(getDoubleParam(pRequest, "metaID", true));
			indicadorFuncionarioRealizadoVO.setUnidadeRealizado(getIntegerParam(pRequest, "idUnidadeRealizadoID"));
			indicadorFuncionarioRealizadoVO.setRealizado(getDoubleParam(pRequest, "realizadoID", true));
			indicadorFuncionarioRealizadoVO.setIdUsuarioAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

			RealizFuncIndicadorBusiness.getInstance().alteraRealizadoFuncIndicador(indicadorFuncionarioRealizadoVO);
			BonusAnualEngine.getInstance().calculaBonusCorporativo(indicadorFuncionarioRealizadoVO.getIdFuncionario(), 
																	indicadorFuncionarioRealizadoVO.getIdFilial(), 
																	indicadorFuncionarioRealizadoVO.getIdEmpresa(),
																	indicadorFuncionarioRealizadoVO.getAno(), 
																	indicadorFuncionarioRealizadoVO.getMes());
			setMensagem(pRequest, "Indicador alterado com sucesso para o funcionário.");

			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		return pesquisaExtratoFuncionario(pMapping, pForm, pRequest, pResponse);
	}

//	public ActionForward finalizaIndicadorFuncionario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {
//
//		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
//			String periodoSelecionado = getStringParam(pRequest, "periodoSelecionado");
//
//			int ano = DataHelper.obtemNumAno(periodoSelecionado, DataHelper.PATTERN_DATE_ANO_MES);
//			int mes = DataHelper.obtemNumMes(periodoSelecionado, DataHelper.PATTERN_DATE_ANO_MES);
//			Long IdFuncionario = getLongParam(pRequest, "idFuncionarioSelecionado");
//			Integer idFilial = getIntegerParam(pRequest, "idFilialSelecionada");
//			Integer idUsuario = obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario();
//
//			//BonusAnualBusiness.getInstance().finalizaIndicadorCorporativoRealizadoFuncIndicador(ano, mes, idFilial, null, IdFuncionario, idUsuario);
//			IndicadorFuncionarioRealizadoVO indicadorFuncionarioBonusVO = new IndicadorFuncionarioRealizadoVO();
//			indicadorFuncionarioBonusVO.setIdFuncionario(IdFuncionario);
//			indicadorFuncionarioBonusVO.setIdEmpresa(Constantes.CODIGO_EMPRESA);
//			indicadorFuncionarioBonusVO.setIdFilial(idFilial);
//			indicadorFuncionarioBonusVO.setAno(ano);
//			indicadorFuncionarioBonusVO.setMes(mes);
//			indicadorFuncionarioBonusVO.setIdUsuarioAlteracao(idUsuario);
//			RealizFuncIndicadorBusiness.getInstance().alteraStatusCalculoCorporativoRealizadoFuncIndicador(indicadorFuncionarioBonusVO, StatCalcRealzEnum.FINALIZADO.getCodigo());
//
//
//			setMensagem(pRequest, "Bônus finalizado com sucesso!");
//			pRequest.setAttribute("periodoSelecionado", periodoSelecionado);
//			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
//		}
//
//		return pesquisaExtratoFuncionario(pMapping, pForm, pRequest, pResponse);
//	}

	/**
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws SRVException
	 */
	public ActionForward calculaIndicadorFuncionario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			String periodoSelecionado = getStringParam(pRequest, "periodoSelecionado");

			int ano = DataHelper.obtemNumAno(periodoSelecionado, DataHelper.PATTERN_DATE_ANO_MES);
			int mes = DataHelper.obtemNumMes(periodoSelecionado, DataHelper.PATTERN_DATE_ANO_MES);
			Long idFuncionario = getLongParam(pRequest, "idFuncionarioSelecionado");
			Integer idFilial = getIntegerParam(pRequest, "idFilialSelecionada");
			//Integer idUsuario = obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario();

			BonusAnualEngine.getInstance().calculaBonusCorporativo(idFuncionario, idFilial, Constantes.CODIGO_EMPRESA, ano, mes);
			setMensagem(pRequest, "Bônus calculado com sucesso!");
			pRequest.setAttribute("periodoSelecionado", periodoSelecionado);
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}

		return pesquisaExtratoFuncionario(pMapping, pForm, pRequest, pResponse);
	}

	  /**
	 * Exclui indicador para o funcionario
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponseaW
	 * @throws SRVException 
	 */
	public ActionForward excluiIndicadorFuncionario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {

			IndicadorFuncionarioRealizadoVO indicadorFuncionarioBonusVO = new IndicadorFuncionarioRealizadoVO();
			indicadorFuncionarioBonusVO.setIdFuncionario(getLongParam(pRequest, "idFuncionarioID"));
			indicadorFuncionarioBonusVO.setIdIndicador(getIntParam(pRequest, "idIndicadorID"));
			indicadorFuncionarioBonusVO.setIdEmpresa(getIntParam(pRequest, "idEmpresaID"));
			indicadorFuncionarioBonusVO.setIdFilial(getIntParam(pRequest, "idFilialID"));
			indicadorFuncionarioBonusVO.setAno(getIntParam(pRequest, "anoID"));
			indicadorFuncionarioBonusVO.setMes(getIntParam(pRequest, "mesID"));
			indicadorFuncionarioBonusVO.setIdUsuarioAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

			RealizFuncIndicadorBusiness.getInstance().excluiRealizadoFuncIndicador(indicadorFuncionarioBonusVO);
			BonusAnualEngine.getInstance().calculaBonusCorporativo(indicadorFuncionarioBonusVO.getIdFuncionario(), 
																	indicadorFuncionarioBonusVO.getIdFilial(), 
																	indicadorFuncionarioBonusVO.getIdEmpresa(),
																	indicadorFuncionarioBonusVO.getAno(), 
																	indicadorFuncionarioBonusVO.getMes());
			setMensagem(pRequest, "Indicador excluído com sucesso para o funcionário.");

			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		return pesquisaExtratoFuncionario(pMapping, pForm, pRequest, pResponse);
	}

  	/**
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward imprimiIndicadores(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH, Boolean.TRUE);

		String periodo = getStringParam(pRequest, "periodos");
		int ano = DataHelper.obtemNumAno(periodo, DataHelper.PATTERN_DATE_ANO_MES);
		int mes = DataHelper.obtemNumMes(periodo, DataHelper.PATTERN_DATE_ANO_MES);

		if (ano < Constantes.NUM_ANO_BASE_ACEITE_ELETRONICO) {
			setMensagemErro(pRequest, "Somente para ano 2016 em diante.");
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		} else {
			// Tela atual
			Long idFuncionarioSelecionado = getLongParam(pRequest, "idFuncionarioSelecionado");
			String nomeFuncionarioSelecionado = getStringParam(pRequest, "nomeFuncionarioSelecionado");
			String cargoFuncionarioSelecionado = getStringParam(pRequest, "cargoFuncionarioSelecionado");
			Integer idFilialSelecionada = getIntegerParam(pRequest, "idFilialSelecionada");
			String centroCustoFuncionarioSelecionado = getStringParam(pRequest, "centroCustoFuncionarioSelecionado");

			// Tela Anterior
			Long idFuncionarioFiltro = getLongParam(pRequest, "idFuncionario");
			String nomeFuncionarioFiltro = getStringParam(pRequest, "nomeFuncionario");
			String crachaFiltro = getStringParam(pRequest, "cracha");
			String cpfFuncionarioFiltro = getStringParam(pRequest, "cpfFuncionario");

			pRequest.setAttribute("situacaoColaborador", FuncionarioBusiness.getInstance().obtemFuncionario(idFuncionarioSelecionado).getSituacaoColaborador());
			pRequest.setAttribute("idFuncionarioSelecionado", idFuncionarioSelecionado);
			pRequest.setAttribute("nomeFuncionarioSelecionado", nomeFuncionarioSelecionado);
			pRequest.setAttribute("cargoFuncionarioSelecionado", cargoFuncionarioSelecionado);
			pRequest.setAttribute("centroCustoFuncionarioSelecionado", centroCustoFuncionarioSelecionado);
			pRequest.setAttribute("idFilialSelecionada", idFilialSelecionada);
			pRequest.setAttribute("idFuncionario", idFuncionarioFiltro);
			pRequest.setAttribute("nomeFuncionario", nomeFuncionarioFiltro);
			pRequest.setAttribute("cracha", crachaFiltro);
			pRequest.setAttribute("cpfFuncionario", cpfFuncionarioFiltro);
			pRequest.setAttribute("periodoAtual", Constantes.NUM_MES_DEZEMBRO.intValue() + "/" + ano);

			RelatorioBonusVO relatorioBonusVO = RealizFuncIndicadorBusiness.getInstance().obtemResultadoRealizadoFuncIndicador(idFuncionarioSelecionado, ano, mes, Constantes.CODIGO_EMPRESA, idFilialSelecionada);

			RelatorioBonusAnualFuncionarioVO vo = new RelatorioBonusAnualFuncionarioVO();
			vo.setNomeFuncionario(nomeFuncionarioSelecionado);
			vo.setAnoBase(ano);
			vo.setIdFuncionario(idFuncionarioSelecionado);
			vo.setCargo(cargoFuncionarioSelecionado);
			vo.setPeriodoRecebimento(DataHelper.obtemMesAnoPorExtenso(new Date()));
			vo.setTextoConsentimento(BonusAnualEngine.getInstance().obtemConfiguracaoBonus(ano).getTextoConsentimento());

			//Obtem status aceite
			ContratoMetaAceiteVO pesquisaAceiteVO = new ContratoMetaAceiteVO();
			pesquisaAceiteVO.setCodFuncionario(idFuncionarioSelecionado);
			pesquisaAceiteVO.setIdEmpresa(Constantes.CODIGO_EMPRESA);
			pesquisaAceiteVO.setIdFilial(idFilialSelecionada);
			pesquisaAceiteVO.setNumAno(ano);
			pesquisaAceiteVO.setNumMes(mes);
			ContratoMetaAceiteVO bonusAceiteVO = ContratoMetaAceiteBusiness.getInstance().obtemContratoMetaAceite(pesquisaAceiteVO);

			vo.setTextoAprovacao(bonusAceiteVO.getDescStatusAceite());
			vo.setIndicadoresRealizados(relatorioBonusVO.getIndicadoresRealizados());
			vo.setIndicadoresCorporativos(relatorioBonusVO.getIndicadoresCorporativos());
			vo.setIndicadoresIndividuais(relatorioBonusVO.getIndicadoresIndividuais());
			vo.setTotalRealizadoVO(relatorioBonusVO.getTotalRealizadoVO());
			vo.setTotalCorporativoRealizadoVO(relatorioBonusVO.getTotalCorporativoRealizadoVO());
			vo.setTotalIndividualRealizadoVO(relatorioBonusVO.getTotalIndividualRealizadoVO());

			HashMap<String, Object> parameterReport = new HashMap<String, Object>();
			parameterReport.put(JRParameter.REPORT_LOCALE, new Locale("PT","BR"));
			parameterReport.put("RelatorioBonusAnualFuncionarioVO", vo);
			parameterReport.put("pathImages", pRequest.getSession().getServletContext().getRealPath("jasperReport/images/logo_marisa.jpg"));

			String pathArquivo = FileHelper.obterPathArquivoTemporario("bonus_anual_" + idFuncionarioSelecionado + "_", ".pdf");
			String pathJasper = pRequest.getSession().getServletContext().getRealPath("jasperReport/bonus_anual_funcionario.jasper");

			JasperPrint print = JasperFillManager.fillReport(pathJasper, parameterReport, new JREmptyDataSource());
			JasperExportManager.exportReportToPdfFile(print, pathArquivo);


			pRequest.setAttribute("lista", relatorioBonusVO.getIndicadoresRealizados());
			pRequest.setAttribute("total", relatorioBonusVO.getTotalRealizadoVO());

			if (relatorioBonusVO != null && relatorioBonusVO.getTotalRealizadoVO() != null) {
				if (ObjectHelper.isNotEmpty(relatorioBonusVO.getTotalRealizadoVO().getPeso()) && relatorioBonusVO.getTotalRealizadoVO().getPeso().doubleValue() == 100) {
					pRequest.setAttribute(ConstantesRequestSession.BOTAO_IMPRIMIR, "true");
				}
			}

			DetalheCalculoVO detalheCalculoVO = RealizFuncIndicadorBusiness.getInstance().obterStatusCalculoRealizado(idFuncionarioSelecionado, ano, mes, Constantes.CODIGO_EMPRESA, idFilialSelecionada);
			if (detalheCalculoVO.getIdFuncionario() != null) {
				if (detalheCalculoVO.getStatusCalculoRealizado() == 1) {
					pRequest.setAttribute("statusRalz", "Não Iniciado");
				} else if (detalheCalculoVO.getStatusCalculoRealizado() == 2) {
					pRequest.setAttribute("statusRalz", "Em Andamento");
				} else if (detalheCalculoVO.getStatusCalculoRealizado() == 3) {
					pRequest.setAttribute("statusRalz", "Finalizado");
				} else if (detalheCalculoVO.getStatusCalculoRealizado() == 0) {
					pRequest.setAttribute("statusRalz", "Não Definido");
				}
			} else {
				pRequest.setAttribute("statusRalz", "Não Iniciado");
			}

			pRequest.setAttribute("statusAceite", bonusAceiteVO.getDescStatusAceite());

			if (bonusAceiteVO.getIdStatusAceite() == StatusContratoMetaAceiteEnum.EM_ABERTO.getCodigo()) {
				if (relatorioBonusVO != null && relatorioBonusVO.getTotalRealizadoVO() != null) {
					if (ObjectHelper.isNotEmpty(relatorioBonusVO.getTotalRealizadoVO().getPeso()) && relatorioBonusVO.getTotalRealizadoVO().getPeso().doubleValue() == Constantes.CEM.doubleValue()) {
						pRequest.setAttribute(ConstantesRequestSession.BOTAO_ACEITE, Boolean.TRUE);
					}
				}
			} else  if (bonusAceiteVO.getIdStatusAceite() == StatusContratoMetaAceiteEnum.ELETRONICO.getCodigo() || 
							bonusAceiteVO.getIdStatusAceite() == StatusContratoMetaAceiteEnum.AUTOMATICO.getCodigo()) {
				if (relatorioBonusVO != null && relatorioBonusVO.getTotalRealizadoVO() != null) {
					if (ObjectHelper.isNotEmpty(relatorioBonusVO.getTotalRealizadoVO().getPeso()) && relatorioBonusVO.getTotalRealizadoVO().getPeso().doubleValue() == Constantes.CEM.doubleValue()) {
						pRequest.setAttribute(ConstantesRequestSession.BOTAO_IMPRIMIR, Boolean.TRUE);
					}
				}
			}

			File file = new File(pathArquivo);
	      FileInputStream fis = new FileInputStream(file);
	      ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      byte[] buf = new byte[1024];
	      for (int readNum; (readNum = fis.read(buf)) != -1;) {
	      	baos.write(buf, 0, readNum);
	      }
	      fis.close();

			int length = 0;
			ServletOutputStream op = pResponse.getOutputStream();
			pResponse.setContentType("application/octet-stream");
			pResponse.setContentLength(baos.size());


			pResponse.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "" + "\"");

			byte[] bbuf = new byte[1024];
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

			while ((bais != null) && ((length = bais.read(bbuf)) != -1)) {
				op.write(bbuf, 0, length);
			}

			bais.close();
			op.flush();
			op.close();
			FileHelper.apagaArquivo(file);

		}
		return pesquisaExtratoFuncionario(pMapping, pForm, pRequest, pResponse);
	}

	/**
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward processarBonusAnual(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {

		String periodo = getStringParam(pRequest, "periodoImport");
		int numAno = DataHelper.obtemNumAno(periodo, DataHelper.PATTERN_DATE_ANO_MES);
		int numMes = DataHelper.obtemNumMes(periodo, DataHelper.PATTERN_DATE_ANO_MES);
		String tipoProcessamento = getStringParam(pRequest, "tipoProcessamento");
		//int codUsuario = obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario();

		try {

			//Tipo do processamento a ser executado
//			if (tipoProcessamento.equalsIgnoreCase(PROCESSAR_BONUS_ANUAL_INDICADOR_CORPORATIVO_EM_ABERTO)) {
//
//				//Distribui os indicadores corporativos com base no presidente
//				RealizFuncIndicadorBusiness.getInstance().geraIndicadorCorporativoEmAberto(numAno, numMes, codUsuario);
//				setMensagem(pRequest, "Indicadores corporativos gerados com sucesso!");
//
//			} else if (tipoProcessamento.equalsIgnoreCase(PROCESSAR_BONUS_ANUAL_ENVIAR_ACEITE_EM_ABERTO)) {
//
//				//Envia para aceite todos os contratos com status em aberto com peso 100%
//				RealizFuncIndicadorBusiness.getInstance().alteraIndicadorCorporativoIniciadoParaAceite(numAno, numMes, codUsuario);
//				setMensagem(pRequest, "Os contratos foram enviados para aceite!");
//
//			} else if (tipoProcessamento.equalsIgnoreCase(PROCESSAR_BONUS_ANUAL_FINALIZAR_EM_ANDAMENTO)) {
//
//				//BonusAnualBusiness.getInstance().alteraIndicadorCorporativoIniciadoParaAceite(numAno, numMes, codUsuario);
//				setMensagem(pRequest, "Os contratos em andamento foram finalizados!");
//
//			} else if (tipoProcessamento.equalsIgnoreCase(PROCESSAR_BONUS_ANUAL_CALCULA_RESULTADO)) {
			if (tipoProcessamento.equalsIgnoreCase(PROCESSAR_BONUS_ANUAL_CALCULA_RESULTADO)) {
				BonusAnualEngine.getInstance().calculaBonusCorporativo(null, null, null, numAno, numMes);
				setMensagem(pRequest, "Cálculo de resultados processado com sucesso!");

			} else {
				setMensagemErro(pRequest, "Tipo de processamento inválido!");
			}

		} catch (MensagemException msgEx) {
			setMensagemErro(pRequest, msgEx.getMessage());
		}

		return montaTelaFuncionario(pMapping, pForm, pRequest, pResponse);
	}

	public ActionForward alteraStatusCalculoBonus(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {

		String periodo = getStringParam(pRequest, "periodos");
		int numAno = DataHelper.obtemNumAno(periodo, DataHelper.PATTERN_DATE_ANO_MES);
		int numMes = DataHelper.obtemNumMes(periodo, DataHelper.PATTERN_DATE_ANO_MES);
		Integer novoStatus = getIntegerParam(pRequest, "novoStatus");
		Long idFuncionario = getLongParam(pRequest, "idFuncionarioSelecionado");
		Integer idFilial = getIntegerParam(pRequest, "idFilialSelecionada");
		
		//int codUsuario = obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario();

//		try {
//
//			//Tipo do processamento a ser executado
//			if (tipoProcessamento.equalsIgnoreCase(PROCESSAR_BONUS_ANUAL_INDICADOR_CORPORATIVO_EM_ABERTO)) {
//
//				//Distribui os indicadores corporativos com base no presidente
//				RealizFuncIndicadorBusiness.getInstance().geraIndicadorCorporativoEmAberto(numAno, numMes, codUsuario);
//				setMensagem(pRequest, "Indicadores corporativos gerados com sucesso!");
//
//			} else if (tipoProcessamento.equalsIgnoreCase(PROCESSAR_BONUS_ANUAL_ENVIAR_ACEITE_EM_ABERTO)) {
//
//				//Envia para aceite todos os contratos com status em aberto com peso 100%
//				RealizFuncIndicadorBusiness.getInstance().alteraIndicadorCorporativoIniciadoParaAceite(numAno, numMes, codUsuario);
//				setMensagem(pRequest, "Os contratos foram enviados para aceite!");
//
//			} else if (tipoProcessamento.equalsIgnoreCase(PROCESSAR_BONUS_ANUAL_FINALIZAR_EM_ANDAMENTO)) {
//
//				//BonusAnualBusiness.getInstance().alteraIndicadorCorporativoIniciadoParaAceite(numAno, numMes, codUsuario);
//				setMensagem(pRequest, "Os contratos em andamento foram finalizados!");
//
//			} else if (tipoProcessamento.equalsIgnoreCase(PROCESSAR_BONUS_ANUAL_CALCULA_RESULTADO)) {
//			if (tipoProcessamento.equalsIgnoreCase(PROCESSAR_BONUS_ANUAL_CALCULA_RESULTADO)) {
//				BonusAnualEngine.getInstance().processaBonusCorporativo(null, null, null, numAno, numMes);
//				setMensagem(pRequest, "Cálculo de resultados processado com sucesso!");
//
//			} else {
//				setMensagemErro(pRequest, "Tipo de processamento inválido!");
//			}
//
//		} catch (MensagemException msgEx) {
//			setMensagemErro(pRequest, msgEx.getMessage());
//		}

		setMensagem(pRequest, "Status do cálculo alterado com sucesso!");
		return pesquisaExtratoFuncionario(pMapping, pForm, pRequest, pResponse);
	}

}