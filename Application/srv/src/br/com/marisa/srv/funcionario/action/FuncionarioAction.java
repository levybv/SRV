package br.com.marisa.srv.funcionario.action;

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

import br.com.marisa.srv.acesso.business.AcessoBusiness;
import br.com.marisa.srv.funcionario.business.FuncionarioBusiness;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.constants.ConstantesFuncionalidades;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.CNPJCPFHelper;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.util.tools.Csv;
import br.com.marisa.srv.util.tools.PoiExcel;

/**
 * Action para lidar com requisicoes do crud de funcionários
 * 
 * @author Walter Fontes
 */
public class FuncionarioAction extends BasicAction {

	private static final Logger log = Logger.getLogger(FuncionarioAction.class);	
	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_CRUD_FUNCIONARIO 	= "crudFuncionario";

	
	/**
	 * Cria um novo objeto FuncionarioAction
	 * 
	 * @throws Exception
	 */
	public FuncionarioAction() throws Exception {
		//
	}


	/**
	 * Inicia página de funcionários
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward inicio(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
		pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH, Boolean.TRUE);
		
		return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse);
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
	private ActionForward montaTelaPrincipal(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {

		Map<Integer, List<Integer>> listaFuncionalidades = AcessoBusiness.getInstance().obtemAcessoPerfil(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdPerfil());
		List<Integer> listaAcessos = listaFuncionalidades.get(ConstantesFuncionalidades.FUNCIONALIDADE_FUNCIONARIO);

		pRequest.setAttribute(ConstantesRequestSession.BOTAO_INCLUIR, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_FUNCIONARIO_ALTERACAO));
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_ALTERAR, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_FUNCIONARIO_ALTERACAO));
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_REMOVER, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_FUNCIONARIO_ALTERACAO));
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_IMPORTAR, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_FUNCIONARIO_ALTERACAO));
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_EXPORTAR, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_FUNCIONARIO_ALTERACAO));

		Long    idFuncionario   = getLongParam  (pRequest, "idFuncionario");
		String  nomeFuncionario = getStringParam(pRequest, "nomeFuncionario");
		String  cracha 			= getStringParam(pRequest, "cracha");
		String  cpfFuncionario  = getStringParam(pRequest, "cpfFuncionario");
		
		pRequest.setAttribute("idFuncionario",   idFuncionario);
		pRequest.setAttribute("nomeFuncionario", nomeFuncionario);
		pRequest.setAttribute("cracha", 		 cracha);
		pRequest.setAttribute("cpfFuncionario",  cpfFuncionario);
		
		if ((idFuncionario   != null && idFuncionario.intValue() > 0) ||
			(nomeFuncionario != null && nomeFuncionario.length() > 0) ||
			(cracha          != null && cracha.length()          > 0) ||
			(cpfFuncionario  != null && cpfFuncionario.length()  > 0)) {
			
			Long cpfFuncionarioL = null;
			if (cpfFuncionario  != null && cpfFuncionario.length()  > 0) {
				cpfFuncionarioL = new Long(CNPJCPFHelper.retiraCaracteresAlpha(cpfFuncionario));				
			}
			
			List<FuncionarioVO> funcionarios = FuncionarioBusiness.getInstance().obtemListaFuncionario(idFuncionario, nomeFuncionario, cracha, cpfFuncionarioL, null, null, null);
			pRequest.setAttribute("funcionarios", funcionarios);
		}
		
		return pMapping.findForward(FORWARD_CRUD_FUNCIONARIO);
	}
	
	
	/**
	 * Efetiva alteração
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward alteraFuncionario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
		
			FuncionarioVO funcionarioVO = new FuncionarioVO();
			funcionarioVO.setIdFuncionario(getLongParam(pRequest, "idFuncionarioF"));
			funcionarioVO.setIdEmpresa(getIntegerParam(pRequest, "idEmpresaF"));
			funcionarioVO.setIdFilial(getIntegerParam(pRequest, "idFilialF"));
			funcionarioVO.setIdFilialRH(getStringParam(pRequest, "idFilialRHF"));
			funcionarioVO.setDescricaoFilialRH(getStringParam(pRequest, "descricaoFilialRHF"));
			funcionarioVO.setIdCargo(getIntegerParam(pRequest, "idCargoF"));
			funcionarioVO.setCracha(getStringParam(pRequest, "crachaF"));
			funcionarioVO.setCpfFuncionario(new Long(CNPJCPFHelper.retiraCaracteresAlpha(getStringParam(pRequest, "cpfFuncionarioF"))));
			funcionarioVO.setNomeFuncionario(getStringParam(pRequest, "nomeFuncionarioF"));
			funcionarioVO.setIdSituacaoRH(getIntegerParam(pRequest, "idSituacaoRHF"));
			funcionarioVO.setDescricaoSituacaoRH(getStringParam(pRequest, "descricaoSituacaoRHF"));
			funcionarioVO.setDataSituacaoRH(getDateParam(pRequest, "dataSituacaoRHF"));
			funcionarioVO.setIdEmpresaRH(getIntegerParam(pRequest, "idEmpresaRHF"));
			funcionarioVO.setDescricaoEmpresaRH(getStringParam(pRequest, "descricaoEmpresaRHF"));
			funcionarioVO.setIdCentroCusto(getStringParam(pRequest, "idCentroCustoF"));
			funcionarioVO.setDescricaoCentroCusto(getStringParam(pRequest, "descricaoCentroCustoF"));
			funcionarioVO.setIdFuncionarioAvaliador(getLongParam(pRequest, "idFuncionarioAvaliadorF"));
			funcionarioVO.setIdFuncionarioSuperior(getLongParam(pRequest, "idFuncionarioSuperiorF"));
			funcionarioVO.setDataAdmissao(getDateParam(pRequest, "dataAdmissaoF"));
			funcionarioVO.setDataDemissao(getDateParam(pRequest, "dataDemissaoF"));
			funcionarioVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
			funcionarioVO.setDataUltimaAlteracao(new Date());
			
      funcionarioVO.setIdSituacaoAnterior(getIntegerParam(pRequest, "idSituacaoAnteriorRHF"));
      funcionarioVO.setQtdDiasTrabalhadosMes(getIntegerParam(pRequest, "idQtdDiasF"));
      funcionarioVO.setDescricaoSituacaoAnterior(getStringParam(pRequest, "descricaoSituacaoAnteriorRHF"));
      funcionarioVO.setDataInicioSituacaoAnterior(getDateParam(pRequest, "dataInicioSituacaoAnteriorRHF"));

      funcionarioVO.setCodigoMotivoDEmissao(getIntegerParam(pRequest, "idMotivoDemissao"));
      funcionarioVO.setQuantidadeDiasAfastamento(getIntegerParam(pRequest, "idQuantidadeAfast"));
      funcionarioVO.setFlagIndicadorCentroCusto(getStringParam(pRequest, "idCentroCusto"));
      funcionarioVO.setDataPromocaoElegivel(getDateParam(pRequest, "idPromocaoElegivel"));

			FuncionarioBusiness.getInstance().alteraFuncionario(funcionarioVO);
			setMensagem(pRequest,"Funcionário alterado com sucesso");
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return pMapping.findForward(SalarioBaseAction.PAGINA_INICIAL);		
	}

	
	/**
	 * Efetiva inclusão
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward incluiFuncionario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			
			FuncionarioVO funcionarioVO = new FuncionarioVO();
			funcionarioVO.setIdFuncionario(getLongParam(pRequest, "idFuncionarioF"));
			funcionarioVO.setIdEmpresa(new Integer(Constantes.CODIGO_EMPRESA));
			funcionarioVO.setIdFilial(getIntegerParam(pRequest, "idFilialF"));
			funcionarioVO.setIdFilialRH(getStringParam(pRequest, "idFilialRHF"));
			funcionarioVO.setDescricaoFilialRH(getStringParam(pRequest, "descricaoFilialRHF"));
			funcionarioVO.setIdCargo(getIntegerParam(pRequest, "idCargoF"));
			funcionarioVO.setCracha(getStringParam(pRequest, "crachaF"));
			funcionarioVO.setCpfFuncionario(new Long(CNPJCPFHelper.retiraCaracteresAlpha(getStringParam(pRequest, "cpfFuncionarioF"))));
			funcionarioVO.setNomeFuncionario(getStringParam(pRequest, "nomeFuncionarioF"));
			funcionarioVO.setIdSituacaoRH(getIntegerParam(pRequest, "idSituacaoRHF"));
			funcionarioVO.setDescricaoSituacaoRH(getStringParam(pRequest, "descricaoSituacaoRHF"));
			funcionarioVO.setDataSituacaoRH(getDateParam(pRequest, "dataSituacaoRHF"));
			funcionarioVO.setIdEmpresaRH(getIntegerParam(pRequest, "idEmpresaRHF"));
			funcionarioVO.setDescricaoEmpresaRH(getStringParam(pRequest, "descricaoEmpresaRHF"));
			funcionarioVO.setIdCentroCusto(getStringParam(pRequest, "idCentroCustoF"));
			funcionarioVO.setDescricaoCentroCusto(getStringParam(pRequest, "descricaoCentroCustoF"));
			funcionarioVO.setIdFuncionarioAvaliador(getLongParam(pRequest, "idFuncionarioAvaliadorF"));
			funcionarioVO.setIdFuncionarioSuperior(getLongParam(pRequest, "idFuncionarioSuperiorF"));
			funcionarioVO.setDataAdmissao(getDateParam(pRequest, "dataAdmissaoF"));
			funcionarioVO.setDataDemissao(getDateParam(pRequest, "dataDemissaoF"));
			funcionarioVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
			funcionarioVO.setDataUltimaAlteracao(new Date());
			
      funcionarioVO.setIdSituacaoAnterior(getIntegerParam(pRequest, "idSituacaoAnteriorRHF"));
      funcionarioVO.setQtdDiasTrabalhadosMes(getIntegerParam(pRequest, "idQtdDiasF"));
      funcionarioVO.setDescricaoSituacaoAnterior(getStringParam(pRequest, "descricaoSituacaoAnteriorRHF"));
      funcionarioVO.setDataInicioSituacaoAnterior(getDateParam(pRequest, "dataInicioSituacaoAnteriorRHF"));

      funcionarioVO.setCodigoMotivoDEmissao(getIntegerParam(pRequest, "idMotivoDemissao"));
      funcionarioVO.setQuantidadeDiasAfastamento(getIntegerParam(pRequest, "idQuantidadeAfast"));
      funcionarioVO.setFlagIndicadorCentroCusto(getStringParam(pRequest, "idCentroCusto"));
      funcionarioVO.setDataPromocaoElegivel(getDateParam(pRequest, "idPromocaoElegivel"));

			FuncionarioBusiness.getInstance().incluiFuncionario(funcionarioVO);
			setMensagem(pRequest,"Funcionário incluído com sucesso");		
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return pMapping.findForward(SalarioBaseAction.PAGINA_INICIAL);		
	}
	
	

	/**
	 * Efetiva exclusão
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward excluiFuncionario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			Long idFuncionario = getLongParam(pRequest, "idFuncionarioF");
			String mensagem = FuncionarioBusiness.getInstance().excluiFuncionario(idFuncionario);
			if (ObjectHelper.isNotEmpty(mensagem)) {
				setMensagem(pRequest, mensagem);		
			} else {
				setMensagem(pRequest, "Funcionário excluído com sucesso");		
			}
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return pMapping.findForward(SalarioBaseAction.PAGINA_INICIAL);		
	}

	public ActionForward geraRelatorioFuncionariosExcel(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws IOException, ServletException, SRVException {
		try {
			List listaFuncionarios = new ArrayList();
			listaFuncionarios = FuncionarioBusiness.getInstance().geraRelatorioFuncionarios();

			List nomeCampos = new ArrayList();
			nomeCampos.add("Codigo Filial");
			nomeCampos.add("Codigo Funcionario");
			nomeCampos.add("Cpf");
			nomeCampos.add("Codigo Cargo");
			nomeCampos.add("Cargo");
			nomeCampos.add("Funcionario");
			nomeCampos.add("Codigo Situação RH");
			nomeCampos.add("Situação RH");
			nomeCampos.add("Qtd Dias Trabalhados");
			nomeCampos.add("Codigo Situação Anterior RH");
			nomeCampos.add("Situação Anterior");
			nomeCampos.add("Data Situação Anterior RH");
			nomeCampos.add("Data Admissão");
			nomeCampos.add("Data Demissão");

			List listaChaveMap = new ArrayList();
			listaChaveMap.add("COD_FIL");
			listaChaveMap.add("ID");
			listaChaveMap.add("CPF");
			listaChaveMap.add("COD_CARGO");
			listaChaveMap.add("DESCR_CARGO");
			listaChaveMap.add("NOME_FUNC");
			listaChaveMap.add("COD_SIT_RH");
			listaChaveMap.add("DESCR_SIT_RH");
			listaChaveMap.add("QTD_DIAS_TRAB_PER");
			listaChaveMap.add("COD_SIT_ANT");
			listaChaveMap.add("SIT_ANT");
			listaChaveMap.add("DT_SIT_ANT");
			listaChaveMap.add("DT_ADMISSAO");
			listaChaveMap.add("DT_DEMISSAO");

			if (listaFuncionarios.size() > 20000) {
				Csv csv = new Csv();
				String texto = csv.montaTxt(nomeCampos, listaFuncionarios, listaChaveMap);
				String nomeArquivo = "relatorio_funcionarios.txt";

				FileOutputStream fileOut = new FileOutputStream(nomeArquivo);
				fileOut.write(texto.getBytes());
				fileOut.flush();
				fileOut.close();

				pResponse.setContentType("application/octet-stream");
				String arq = "attachment;filename=" + nomeArquivo;
				pResponse.setHeader("Content-Disposition", arq);

				ServletOutputStream os = pResponse.getOutputStream();
				os.write(texto.getBytes());
				os.flush();
				os.close();
			} else {
				Map subTitulo = new HashMap();
				subTitulo.put("NOME", "Relatório de Funcionários");

				String nomeArquivo = "relatorio_funcionarios.xls";

				ByteArrayOutputStream baos = PoiExcel.montaPlanilha("Relatório", subTitulo, nomeCampos, listaFuncionarios, listaChaveMap);

				int length = 0;
				ServletOutputStream op = pResponse.getOutputStream();
				pResponse.setContentType("application/octet-stream");
				pResponse.setContentLength(baos.size());
				
				if ((nomeArquivo == null) || (nomeArquivo.equals(""))) {
					nomeArquivo = "Relatorio.xml";
				}

				pResponse.setHeader("Content-Disposition", "attachment; filename=\"" + nomeArquivo + "\"");
				byte[] bbuf = new byte[1024];
				ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
				
				while ((bais != null) && ((length = bais.read(bbuf)) != -1)) {
					op.write(bbuf, 0, length);
				}

				bais.close();
				op.flush();
				op.close();
			}
		} catch (Exception e) {
			throw new SRVException(log, "Não foi possível gerar relatório de Funcionários em excel ou csv.", e);
		}
		return null;
	}

}
