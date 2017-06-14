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

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.acesso.business.AcessoBusiness;
import br.com.marisa.srv.bonus.business.ContratoMetaAceiteBusiness;
import br.com.marisa.srv.bonus.business.RealizFuncIndicadorBusiness;
import br.com.marisa.srv.bonus.configuracao.vo.ConfiguracaoBonusVO;
import br.com.marisa.srv.bonus.engine.BonusAnualEngine;
import br.com.marisa.srv.bonus.vo.ContratoMetaAceiteVO;
import br.com.marisa.srv.funcionario.business.FuncionarioBusiness;
import br.com.marisa.srv.funcionario.report.vo.RelatorioBonusAnualFuncionarioVO;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.constants.ConstantesFuncionalidades;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.enumeration.StatCalcRealzEnum;
import br.com.marisa.srv.geral.enumeration.StatusContratoMetaAceiteEnum;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.CNPJCPFHelper;
import br.com.marisa.srv.geral.helper.DataHelper;
import br.com.marisa.srv.geral.helper.FileHelper;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.indicador.vo.DetalheCalculoVO;
import br.com.marisa.srv.indicador.vo.RelatorioBonusVO;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * 
 * @author levy.villar
 *
 */
public class FuncionarioBonusAnualAction extends BasicAction {

	//Log4J
	private static final Logger log = Logger.getLogger(FuncionarioBonusAnualAction.class);

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_BONUS_ANUAL_FUNCIONARIO = "funcionario";
	private static final String FORWARD_BONUS_ANUAL_INDICADOR_FUNCIONARIO = "indicadorFuncionario";
	private static final String FORWARD_BONUS_ANUAL_INDICADOR_FUNCIONARIO_SIMULACAO = "indicadorFuncionarioSimulacao";

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
		return montaTelaFuncionario(pMapping, pForm, pRequest, pResponse);
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
	private ActionForward montaTelaFuncionario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {

		Long    idFuncionario   = getLongParam  (pRequest, "idFuncionario");
		String  nomeFuncionario = getStringParam(pRequest, "nomeFuncionario");
		String  cracha 			= getStringParam(pRequest, "cracha");
		String  cpfFuncionario  = getStringParam(pRequest, "cpfFuncionario");

		pRequest.setAttribute("idFuncionario", idFuncionario);
		pRequest.setAttribute("nomeFuncionario", nomeFuncionario);
		pRequest.setAttribute("cracha", cracha);
		pRequest.setAttribute("cpfFuncionario", cpfFuncionario);

		Long cpfFuncionarioL = null;
		if (cpfFuncionario  != null && cpfFuncionario.length()  > 0) {
			cpfFuncionarioL = new Long(CNPJCPFHelper.retiraCaracteresAlpha(cpfFuncionario));				
		}

		if (AcessoBusiness.getInstance().obtemAcessoPerfilFuncionalidadeTipoAcesso(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdPerfil(),
																							ConstantesFuncionalidades.FUNCIONALIDADE_BONUS_ANUAL_FUNCIONARIO, 
																							ConstantesFuncionalidades.TIPO_ACESSO_BONUS_ANUAL_FUNCIONARIO_MASTER)) {
			try {
				List<FuncionarioVO> funcionarios = FuncionarioBusiness.getInstance().obtemListaFuncionario(idFuncionario, nomeFuncionario, cracha, cpfFuncionarioL, null, null, null);
				pRequest.setAttribute("funcionarios", funcionarios);
				for (Iterator<FuncionarioVO> it = funcionarios.iterator(); it.hasNext();) {
					FuncionarioVO funcVO = it.next();
					pRequest.setAttribute("situacaoColaborador",funcVO.getSituacaoColaborador());
				}
			} catch (Exception e) {
				log.warn("Erro ao obter a matrícula do usuário " + obtemUsuarioDaSessao(pRequest).getUsuarioVO().getNome(), e);
			}
		} else if (obtemUsuarioDaSessao(pRequest).getUsuarioVO().getMatricula() != null) {
			try {
				Long matricula = new Long(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getMatricula());
				List<FuncionarioVO> funcionarios = FuncionarioBusiness.getInstance().obtemListaFuncionario(matricula, null, null, null, null, null, null);
				pRequest.setAttribute("funcionarios", funcionarios);
				for (Iterator<FuncionarioVO> it = funcionarios.iterator(); it.hasNext();) {
					FuncionarioVO funcVO = it.next();
					pRequest.setAttribute("situacaoColaborador",funcVO.getSituacaoColaborador());
				}
			} catch (Exception e) {
				log.warn("Erro ao obter a matrícula do usuário " + obtemUsuarioDaSessao(pRequest).getUsuarioVO().getNome(), e);
			}
		}

		return pMapping.findForward(FORWARD_BONUS_ANUAL_FUNCIONARIO);
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
	public ActionForward pesquisaIndicadorFuncionario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {

		pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH, Boolean.TRUE);
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_IMPRIMIR, Boolean.FALSE);
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_ACEITE, Boolean.FALSE);

		ConfiguracaoBonusVO configuracaoBonusVO = BonusAnualEngine.getInstance().obtemParametroAtivoBonus();

		// Tela atual
		Long idFuncionarioSelecionado = getLongParam(pRequest, "idFuncionarioSelecionado");
		String nomeFuncionarioSelecionado = getStringParam(pRequest, "nomeFuncionarioSelecionado");
		String cargoFuncionarioSelecionado = getStringParam(pRequest, "cargoFuncionarioSelecionado");
		String centroCustoFuncionarioSelecionado = getStringParam(pRequest, "centroCustoFuncionarioSelecionado");

		// Tela Anterior
		Long idFuncionarioFiltro = getLongParam(pRequest, "idFuncionario");
		String nomeFuncionarioFiltro = getStringParam(pRequest, "nomeFuncionario");
		String crachaFiltro = getStringParam(pRequest, "cracha");
		String cpfFuncionarioFiltro = getStringParam(pRequest, "cpfFuncionario");

		Integer ano = DataHelper.obtemNumAno(configuracaoBonusVO.getPeriodoDisponivel(), DataHelper.PATTERN_DATE_MES_ANO);
		Integer mes = DataHelper.obtemNumMes(configuracaoBonusVO.getPeriodoDisponivel(), DataHelper.PATTERN_DATE_MES_ANO);

		FuncionarioVO funcionarioVO = FuncionarioBusiness.getInstance().obtemFuncionario(idFuncionarioSelecionado);

		pRequest.setAttribute("situacaoColaborador", funcionarioVO.getSituacaoColaborador());
		pRequest.setAttribute("idFuncionarioSelecionado", idFuncionarioSelecionado);
		pRequest.setAttribute("nomeFuncionarioSelecionado", nomeFuncionarioSelecionado);
		pRequest.setAttribute("cargoFuncionarioSelecionado", cargoFuncionarioSelecionado);
		pRequest.setAttribute("centroCustoFuncionarioSelecionado", centroCustoFuncionarioSelecionado);
		pRequest.setAttribute("idFuncionario", idFuncionarioFiltro);
		pRequest.setAttribute("nomeFuncionario", nomeFuncionarioFiltro);
		pRequest.setAttribute("cracha", crachaFiltro);
		pRequest.setAttribute("cpfFuncionario", cpfFuncionarioFiltro);
		pRequest.setAttribute("periodoAtual", configuracaoBonusVO.getPeriodoDisponivel());
		if (mes == Constantes.NUM_MES_DEZEMBRO) {
			pRequest.setAttribute("isExibeMetaRealizado", Boolean.TRUE);
		} else {
			//Cargos de gerente de loja não exibe coluna meta e realizado
			if (funcionarioVO.getIdCargo() == 1162 || funcionarioVO.getIdCargo() == 1163 || funcionarioVO.getIdCargo() == 1164) {
				pRequest.setAttribute("isExibeMetaRealizado", Boolean.FALSE);
			} else {
				pRequest.setAttribute("isExibeMetaRealizado", Boolean.TRUE);
			}
		}

		RelatorioBonusVO relatorioBonusVO = RealizFuncIndicadorBusiness.getInstance().obtemResultadoRealizadoFuncIndicador(idFuncionarioSelecionado, ano, mes, Constantes.CODIGO_EMPRESA, funcionarioVO.getIdFilial());
		DetalheCalculoVO detalheCalculoVO = RealizFuncIndicadorBusiness.getInstance().obterStatusCalculoRealizado(idFuncionarioSelecionado, ano, mes, Constantes.CODIGO_EMPRESA, funcionarioVO.getIdFilial());
		pRequest.setAttribute("lista", relatorioBonusVO.getIndicadoresRealizados());
		pRequest.setAttribute("total", relatorioBonusVO.getTotalRealizadoVO());
		pRequest.setAttribute("detalheCalculo", detalheCalculoVO);

		if (detalheCalculoVO.getStatusCalculoRealizado() < StatCalcRealzEnum.EM_ACEITE.getCodigo()) {
			pRequest.setAttribute("statusRalz", StatCalcRealzEnum.NENHUM.getDescricao());
			pRequest.setAttribute("lista", null);
			pRequest.setAttribute("total", null);
			pRequest.setAttribute("detalheCalculo", null);
			return pMapping.findForward(FORWARD_BONUS_ANUAL_INDICADOR_FUNCIONARIO);
		} else if (detalheCalculoVO.getStatusCalculoRealizado() == StatCalcRealzEnum.EM_ACEITE.getCodigo()) {
			pRequest.setAttribute("statusRalz", StatCalcRealzEnum.EM_ACEITE.getDescricao());
		} else if (detalheCalculoVO.getStatusCalculoRealizado() == StatCalcRealzEnum.EM_ANDAMENTO.getCodigo()) {
			pRequest.setAttribute("statusRalz", StatCalcRealzEnum.EM_ANDAMENTO.getDescricao());
		} else if (detalheCalculoVO.getStatusCalculoRealizado() == StatCalcRealzEnum.FINALIZADO.getCodigo()) {
			pRequest.setAttribute("statusRalz", StatCalcRealzEnum.FINALIZADO.getDescricao());
		}

		//Obtem status aceite
		ContratoMetaAceiteVO pesquisaAceiteVO = new ContratoMetaAceiteVO();
		pesquisaAceiteVO.setCodFuncionario(idFuncionarioSelecionado);
		pesquisaAceiteVO.setIdEmpresa(Constantes.CODIGO_EMPRESA);
		pesquisaAceiteVO.setIdFilial(funcionarioVO.getIdFilial());
		pesquisaAceiteVO.setNumAno(ano);
		pesquisaAceiteVO.setNumMes(mes);
		ContratoMetaAceiteVO bonusAceiteVO = ContratoMetaAceiteBusiness.getInstance().obtemContratoMetaAceite(pesquisaAceiteVO);

		pRequest.setAttribute("statusAceite", bonusAceiteVO.getDescStatusAceite());

		if (bonusAceiteVO.getIdStatusAceite() == StatusContratoMetaAceiteEnum.EM_ABERTO.getCodigo()) {
			if (relatorioBonusVO != null && relatorioBonusVO.getTotalRealizadoVO() != null) {
				if (ObjectHelper.isNotEmpty(relatorioBonusVO.getTotalRealizadoVO().getPeso()) && relatorioBonusVO.getTotalRealizadoVO().getPeso().doubleValue() == Constantes.CEM.doubleValue()) {
					if (mes == Constantes.NUM_MES_DEZEMBRO) {
						pRequest.setAttribute(ConstantesRequestSession.BOTAO_ACEITE, Boolean.TRUE);
					}
				}
			}
		} else  if (bonusAceiteVO.getIdStatusAceite() == StatusContratoMetaAceiteEnum.ELETRONICO.getCodigo() || 
					bonusAceiteVO.getIdStatusAceite() == StatusContratoMetaAceiteEnum.AUTOMATICO.getCodigo()) {
			if (relatorioBonusVO != null && relatorioBonusVO.getTotalRealizadoVO() != null) {
				if (ObjectHelper.isNotEmpty(relatorioBonusVO.getTotalRealizadoVO().getPeso()) && relatorioBonusVO.getTotalRealizadoVO().getPeso().doubleValue() == Constantes.CEM.doubleValue()) {
					if (mes == Constantes.NUM_MES_DEZEMBRO) {
						pRequest.setAttribute(ConstantesRequestSession.BOTAO_IMPRIMIR, Boolean.TRUE);
					}
				}
			}
		}

		String acaoTela = getStringParam(pRequest, "acaoTela");
		if (acaoTela != null && acaoTela.equalsIgnoreCase("simulaBonus")) {
			return pMapping.findForward(FORWARD_BONUS_ANUAL_INDICADOR_FUNCIONARIO_SIMULACAO);
		} else {
			return pMapping.findForward(FORWARD_BONUS_ANUAL_INDICADOR_FUNCIONARIO);
		}
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

		ConfiguracaoBonusVO configuracaoBonusVO = BonusAnualEngine.getInstance().obtemParametroAtivoBonus();

		// Tela atual
		Long idFuncionarioSelecionado = getLongParam(pRequest, "idFuncionarioSelecionado");
		String nomeFuncionarioSelecionado = getStringParam(pRequest, "nomeFuncionarioSelecionado");
		String cargoFuncionarioSelecionado = getStringParam(pRequest, "cargoFuncionarioSelecionado");
		String centroCustoFuncionarioSelecionado = getStringParam(pRequest, "centroCustoFuncionarioSelecionado");

		// Tela Anterior
		Long idFuncionarioFiltro = getLongParam(pRequest, "idFuncionario");
		String nomeFuncionarioFiltro = getStringParam(pRequest, "nomeFuncionario");
		String crachaFiltro = getStringParam(pRequest, "cracha");
		String cpfFuncionarioFiltro = getStringParam(pRequest, "cpfFuncionario");

		FuncionarioVO funcionarioVO = FuncionarioBusiness.getInstance().obtemFuncionario(idFuncionarioSelecionado);

		pRequest.setAttribute("situacaoColaborador", funcionarioVO.getSituacaoColaborador());
		pRequest.setAttribute("idFuncionarioSelecionado", idFuncionarioSelecionado);
		pRequest.setAttribute("nomeFuncionarioSelecionado", nomeFuncionarioSelecionado);
		pRequest.setAttribute("cargoFuncionarioSelecionado", cargoFuncionarioSelecionado);
		pRequest.setAttribute("centroCustoFuncionarioSelecionado", centroCustoFuncionarioSelecionado);
		pRequest.setAttribute("idFuncionario", idFuncionarioFiltro);
		pRequest.setAttribute("nomeFuncionario", nomeFuncionarioFiltro);
		pRequest.setAttribute("cracha", crachaFiltro);
		pRequest.setAttribute("cpfFuncionario", cpfFuncionarioFiltro);
		pRequest.setAttribute("periodoAtual", configuracaoBonusVO.getPeriodoDisponivel());

		Integer ano = DataHelper.obtemNumAno(configuracaoBonusVO.getPeriodoDisponivel(), DataHelper.PATTERN_DATE_MES_ANO);
		Integer mes = DataHelper.obtemNumMes(configuracaoBonusVO.getPeriodoDisponivel(), DataHelper.PATTERN_DATE_MES_ANO);

		//Obtem status aceite
		ContratoMetaAceiteVO pesquisaAceiteVO = new ContratoMetaAceiteVO();
		pesquisaAceiteVO.setCodFuncionario(idFuncionarioSelecionado);
		pesquisaAceiteVO.setIdEmpresa(Constantes.CODIGO_EMPRESA);
		pesquisaAceiteVO.setIdFilial(funcionarioVO.getIdFilial());
		pesquisaAceiteVO.setNumAno(ano);
		pesquisaAceiteVO.setNumMes(mes);
		ContratoMetaAceiteVO bonusAceiteVO = ContratoMetaAceiteBusiness.getInstance().obtemContratoMetaAceite(pesquisaAceiteVO);

		//Obtem resultado
		RelatorioBonusVO relatorioBonusVO = RealizFuncIndicadorBusiness.getInstance().obtemResultadoRealizadoFuncIndicador(idFuncionarioSelecionado, ano, mes, Constantes.CODIGO_EMPRESA, funcionarioVO.getIdFilial());

		RelatorioBonusAnualFuncionarioVO vo = new RelatorioBonusAnualFuncionarioVO();
		vo.setNomeFuncionario(nomeFuncionarioSelecionado);
		vo.setAnoBase(ano);
		vo.setIdFuncionario(idFuncionarioSelecionado);
		vo.setCargo(cargoFuncionarioSelecionado);
		vo.setPeriodoRecebimento(DataHelper.obtemMesAnoPorExtenso(new Date()));
		vo.setTextoConsentimento(configuracaoBonusVO.getTextoConsentimento());
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
				pRequest.setAttribute("stBtImprimir", "true");
			}
		}

		DetalheCalculoVO detalheCalculoVO = RealizFuncIndicadorBusiness.getInstance().obterStatusCalculoRealizado(idFuncionarioSelecionado, ano, mes, Constantes.CODIGO_EMPRESA, funcionarioVO.getIdFilial());
		if (detalheCalculoVO.getStatusCalculoRealizado() < StatCalcRealzEnum.EM_ACEITE.getCodigo()) {
			pRequest.setAttribute("statusRalz", StatCalcRealzEnum.NENHUM.getDescricao());
			pRequest.setAttribute("lista", null);
			pRequest.setAttribute("total", null);
			return pMapping.findForward(FORWARD_BONUS_ANUAL_INDICADOR_FUNCIONARIO);
		} else if (detalheCalculoVO.getStatusCalculoRealizado() == StatCalcRealzEnum.EM_ACEITE.getCodigo()) {
			pRequest.setAttribute("statusRalz", StatCalcRealzEnum.EM_ACEITE.getDescricao());
		} else if (detalheCalculoVO.getStatusCalculoRealizado() == StatCalcRealzEnum.EM_ANDAMENTO.getCodigo()) {
			pRequest.setAttribute("statusRalz", StatCalcRealzEnum.EM_ANDAMENTO.getDescricao());
		} else if (detalheCalculoVO.getStatusCalculoRealizado() == StatCalcRealzEnum.FINALIZADO.getCodigo()) {
			pRequest.setAttribute("statusRalz", StatCalcRealzEnum.FINALIZADO.getDescricao());
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

		return pMapping.findForward(FORWARD_BONUS_ANUAL_INDICADOR_FUNCIONARIO);
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
	public ActionForward aceiteIndicadorFuncionario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {
		
		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {

			Long idFuncionarioSelecionado = getLongParam(pRequest, "idFuncionarioSelecionado");
			FuncionarioVO funcionarioVO = FuncionarioBusiness.getInstance().obtemFuncionario(idFuncionarioSelecionado);

			ConfiguracaoBonusVO configuracaoBonusVO = BonusAnualEngine.getInstance().obtemParametroAtivoBonus();

			ContratoMetaAceiteVO bonusAceiteVO = new ContratoMetaAceiteVO();
			bonusAceiteVO.setCodFuncionario(idFuncionarioSelecionado);
			bonusAceiteVO.setNumAno(DataHelper.obtemNumAno(configuracaoBonusVO.getPeriodoDisponivel(), DataHelper.PATTERN_DATE_MES_ANO));
			bonusAceiteVO.setNumMes(DataHelper.obtemNumMes(configuracaoBonusVO.getPeriodoDisponivel(), DataHelper.PATTERN_DATE_MES_ANO));
			bonusAceiteVO.setIdEmpresa(Constantes.CODIGO_EMPRESA);
			bonusAceiteVO.setIdFilial(funcionarioVO.getIdFilial());
			bonusAceiteVO.setCodUsuario(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
			ContratoMetaAceiteBusiness.getInstance().incluiContratoMetaAceite(bonusAceiteVO);
			setMensagem(pRequest, "Aceite realizado com sucesso.");
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);

		}

		return pesquisaIndicadorFuncionario(pMapping, pForm, pRequest, pResponse);
	}	

/**********************************************************************************************************************************************/

	/**
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
//	public ActionForward imprimiIndicadores(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
//		pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH, Boolean.TRUE);
//
//		String periodoAtual = ParametroBusiness.getInstance().obtemParametroBonusAnualFuncionarioPeriodo();
//
//		List<Integer> listaGeracao = new ArrayList<Integer>();//IndicadorBusiness.getInstance().obtemListaFuncionarioRelatorio();
//		listaGeracao.add(	14608	);
//		listaGeracao.add(	903160	);
//		listaGeracao.add(	908764	);
//		listaGeracao.add(	911512	);
//		listaGeracao.add(	912251	);
//		listaGeracao.add(	930913	);
//		listaGeracao.add(	930926	);
//		listaGeracao.add(	933665	);
//		listaGeracao.add(	934236	);
//		listaGeracao.add(	935465	);
//		listaGeracao.add(	938505	);
//		listaGeracao.add(	940731	);
//		listaGeracao.add(	943610	);
//		listaGeracao.add(	945897	);
//		listaGeracao.add(	946787	);
//		listaGeracao.add(	956855	);
//		listaGeracao.add(	981986	);
//		listaGeracao.add(	983704	);
//		listaGeracao.add(	999043	);
//		listaGeracao.add(	1008646	);
//		listaGeracao.add(	1011447	);
//		listaGeracao.add(	1012217	);
//		listaGeracao.add(	1012317	);
//		listaGeracao.add(994766);
//		//listaGeracao.add(	1012213	);
//		for (Iterator<Integer> it = listaGeracao.iterator(); it.hasNext();) {
//			long idFuncionarioSelecionado = it.next();
//			FuncionarioVO funcionarioVO = FuncionarioBusiness.getInstance().obtemFuncionario(idFuncionarioSelecionado);
//			RelatorioBonusVO relatorioBonusVO = IndicadorBusiness.getInstance().obtemBonusAnualPorFuncionario(funcionarioVO.getIdFuncionario(), periodoAtual);
//
//			RelatorioBonusAnualFuncionarioVO vo = new RelatorioBonusAnualFuncionarioVO();
//			vo.setNomeFuncionario(funcionarioVO.getNomeFuncionario());
//			vo.setAnoBase(new Integer(periodoAtual.substring(0, 4)));
//			vo.setIdFuncionario(funcionarioVO.getIdFuncionario());
//			vo.setCargo(funcionarioVO.getDescricaoCargo());
//			vo.setPeriodoRecebimento(DataHelper.obtemMesAnoPorExtenso(new Date()));
//			vo.setTextoConsentimento(ParametroBusiness.getInstance().obtemParametroBonusAnualFuncionarioTextoConsentimentoPDF());
//			vo.setIndicadoresRealizados(relatorioBonusVO.getIndicadoresRealizados());
//			vo.setIndicadoresCorporativos(relatorioBonusVO.getIndicadoresCorporativos());
//			vo.setIndicadoresIndividuais(relatorioBonusVO.getIndicadoresIndividuais());
//			vo.setTotalRealizadoVO(relatorioBonusVO.getTotalRealizadoVO());
//			vo.setTotalCorporativoRealizadoVO(relatorioBonusVO.getTotalCorporativoRealizadoVO());
//			vo.setTotalIndividualRealizadoVO(relatorioBonusVO.getTotalIndividualRealizadoVO());
//
//			HashMap<String, Object> parameterReport = new HashMap<String, Object>();
//			parameterReport.put(JRParameter.REPORT_LOCALE, new Locale("PT","BR"));
//			parameterReport.put("RelatorioBonusAnualFuncionarioVO", vo);
//			parameterReport.put("pathImages", pRequest.getSession().getServletContext().getRealPath("jasperReport/images/logo_marisa.jpg"));
//
//			String pathArquivo = "C:/TEMP/BONUS_ANUAL_2015/GERADOS/" + funcionarioVO.getDescDiretoria().trim().replaceAll(" ", "_").replaceAll("\\.", "") + " - " + funcionarioVO.getDescricaoCargo().trim().replaceAll(" ", "_").replaceAll("\\.", "") + " - " + funcionarioVO.getIdFuncionario() + ".pdf";
//			String pathJasper = pRequest.getSession().getServletContext().getRealPath("jasperReport/bonus_anual_funcionario.jasper");
//
//			JasperPrint print = JasperFillManager.fillReport(pathJasper,parameterReport, new JREmptyDataSource());
//			JasperExportManager.exportReportToPdfFile(print, pathArquivo);
//
////			File file = new File("C:/TEMP/BONUS_ANUAL_2015/GERADOS/teste01.png");
////			OutputStream ouputStream = null;
////			try {
////				ouputStream = new FileOutputStream(file);
////				DefaultJasperReportsContext.getInstance();
////				JasperPrintManager printManager = JasperPrintManager.getInstance(DefaultJasperReportsContext.getInstance());
////
////				BufferedImage rendered_image = null;
////				rendered_image = (BufferedImage) printManager.printPageToImage(print, 0, 4.0f);
////				ImageIO.write(rendered_image, "png", ouputStream);
////				ouputStream.flush();
////				ouputStream.close();
////
////			} catch (Exception e) {
////				e.printStackTrace();
////			}
//		}
//
//		return pMapping.findForward(FORWARD_BONUS_ANUAL_INDICADOR_FUNCIONARIO);
//	}

	public ActionForward testeProcessamento(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		try {
			//BonusAnualEngine.getInstance().processaBonusCorporativo(new Long(1014156), new Integer(909), Constantes.CODIGO_EMPRESA, 2016, 6);
			//BonusAnualEngine.getInstance().processaBonusCorporativo(new Long(1011442), new Integer(900), Constantes.CODIGO_EMPRESA, 2016, 6);
			//BonusAnualEngine.getInstance().processaBonusCorporativo(new Long(25342), new Integer(900), Constantes.CODIGO_EMPRESA, 2016, 6);
			//BonusAnualEngine.getInstance().processaBonusCorporativo(new Long(1008718), new Integer(607), Constantes.CODIGO_EMPRESA, 2016, 6);
			//BonusAnualEngine.getInstance().processaBonusCorporativo(new Long(1014746), new Integer(900), Constantes.CODIGO_EMPRESA, 2016, 6);
			//BonusAnualEngine.getInstance().processaBonusCorporativo(new Long(1013075), new Integer(664), Constantes.CODIGO_EMPRESA, 2016, 6);
			BonusAnualEngine.getInstance().calculaBonusCorporativo(null, null, Constantes.CODIGO_EMPRESA, 2016, 6);
			setMensagem(pRequest, "Cálculo Bônus Realizado com Sucesso!");
			 
		} catch (Exception e) {
			setMensagemErro(pRequest, e.getMessage());
		}
		return pMapping.findForward(FORWARD_BONUS_ANUAL_INDICADOR_FUNCIONARIO);
	}

}