package br.com.marisa.srv.indicador.business;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import br.com.marisa.srv.bonus.business.RealizFuncIndicadorBusiness;
import br.com.marisa.srv.bonus.engine.BonusAnualEngine;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.indicador.vo.DadosIndicadorVO;
import br.com.marisa.srv.indicador.vo.DetalheCalculoLojaVO;
import br.com.marisa.srv.indicador.vo.DetalheCalculoVO;
import br.com.marisa.srv.indicador.vo.IndicadorFuncionarioRealizadoVO;


public class IndicadorBusinessAjax {
    
	
	/**
	 * Obtém lista de indicadores
	 * 
	 * @return
	 * @throws NumberFormatException
	 * @throws SRVException
	 */
    public List obtemListaIndicadores() throws NumberFormatException, SRVException {
    	return IndicadorBusiness.getInstance().obtemListaIndicadores(null);
    }	
	
	/**
	 * Obtém lista de indicadores
	 * 
	 * @param codGrupo
	 * @return
	 * @throws NumberFormatException
	 * @throws SRVException
	 */
    public List obtemListaIndicadoresPorGrupo(String codGrupo) throws NumberFormatException, SRVException {
    	return IndicadorBusiness.getInstance().obtemListaIndicadores(new Integer(codGrupo));
    }
    
    
	/**
	 * Obtém lista de indicadores por tipo de remuneracao variavel
	 * 
	 * @param idTipoRemuneracaoVariavel
	 * @return
	 * @throws NumberFormatException
	 * @throws SRVException
	 */
    public List obtemListaIndicadoresPorTipo(int idTipoRemuneracaoVariavel) throws NumberFormatException, SRVException {
    	
    	List tiposRemuneracao = new ArrayList();
    	tiposRemuneracao.add(new Integer(idTipoRemuneracaoVariavel));
    	
    	return IndicadorBusiness.getInstance().obtemListaIndicadoresPorTipo(tiposRemuneracao, false);
    }
    
    public List obtemListaIndicadoresPorTipoAtivo(int idTipoRemuneracaoVariavel) throws NumberFormatException, SRVException {
    	List<Integer> tiposRemuneracao = new ArrayList<Integer>();
    	tiposRemuneracao.add(new Integer(1));
    	DadosIndicadorVO filtroIndicadorVO = new DadosIndicadorVO();
    	filtroIndicadorVO.setAtivo("S");
    	return IndicadorBusiness.getInstance().obtemListaIndicadoresPorTipo(filtroIndicadorVO, tiposRemuneracao, false);
    }
    
	/**
	 * Obtém lista de indicadores de loja
	 * 
	 * @return
	 * @throws NumberFormatException
	 * @throws SRVException
	 */
    public List obtemListaIndicadoresLoja() throws NumberFormatException, SRVException {
    	
    	List tiposRemuneracao = new ArrayList();
    	tiposRemuneracao.add(Constantes.ID_TIPO_REM_VAR_REMUNERACAO_LOJA);
    	tiposRemuneracao.add(Constantes.ID_TIPO_REM_VAR_CALL_CENTER);
    	DadosIndicadorVO pesquisaVO = new DadosIndicadorVO();
    	pesquisaVO.setAtivo(Constantes.CD_VERDADEIRO);
    	return IndicadorBusiness.getInstance().obtemListaIndicadores(pesquisaVO, tiposRemuneracao, false);
    }    
	
    
    /**
     * Obtém detalhe do cálculo do indicador do funcionário
     * 
     * @param idFuncionario
     * @param idIndicador
     * @param idEmpresa
     * @param idFilial
     * @param ano
     * @param mes
     * @return
     * @throws SRVException
     */
    public DetalheCalculoVO obtemDetalheCalculo(int idFuncionario, int idIndicador, int idEmpresa, int idFilial, int ano, int mes) throws SRVException {
    	return IndicadorBusiness.getInstance().obtemDetalheCalculo(idFuncionario, idIndicador, idEmpresa, idFilial, ano, mes);
    }    
    
    
    /**
     * Obtém detalhe do cálculo do indicador da loja
     * 
     * @param idIndicador
     * @param idEmpresa
     * @param idFilial
     * @param ano
     * @param mes
     * @return
     * @throws SRVException
     */
    public DetalheCalculoLojaVO obtemDetalheCalculoLoja(int idIndicador, int idEmpresa, int idFilial, int ano, int mes) throws SRVException {
    	return IndicadorBusiness.getInstance().obtemDetalheCalculoLoja(idIndicador, idEmpresa, idFilial, ano, mes);
    }      
    
    
    /**
     * Obtém informações sobre um indicador
     * 
     * @param idIndicador
     * @return
     * @throws SRVException
     */
    public DadosIndicadorVO obtemIndicador(int idIndicador) throws SRVException {
    	return IndicadorBusiness.getInstance().obtemIndicador(idIndicador);
    }      
    
    
    /**
     * Obtém informações sobre um indicador
     * 
     * @param idIndicador
     * @return
     * @throws SRVException
     */
    public DadosIndicadorVO obtemIndicadorRealizado(int idIndicador) throws SRVException {
    	return IndicadorBusiness.getInstance().obtemIndicador(idIndicador);
    }    
    
    
    /**
     * Obtém realizado do indicador
     * 
     * @param idFuncionario
     * @param idIndicador
     * @param idEmpresa
     * @param idFilial
     * @param ano
     * @param mes
     * @return
     * @throws SRVException
     */
    public IndicadorFuncionarioRealizadoVO obtemRealizadoIndicadorBonus(String chave) throws SRVException {
    	
    	StringTokenizer st = new StringTokenizer(chave, ";");
    	int idIndicador 	= Integer.parseInt(st.nextToken());
    	int idFuncionario 	= Integer.parseInt(st.nextToken());
    	int idEmpresa 		= Integer.parseInt(st.nextToken());
    	int idFilial 		= Integer.parseInt(st.nextToken());
    	int ano 			= Integer.parseInt(st.nextToken());
    	int mes 			= Integer.parseInt(st.nextToken());
    	
    	return RealizFuncIndicadorBusiness.getInstance().obtemRealizadoFuncIndicador(idFuncionario, idIndicador, idEmpresa, idFilial, ano, mes);
    }
    
    public List obtemListaIndicadoresAjuste() throws SRVException {
    	return IndicadorBusiness.getInstance().obtemListaIndicadoresAjuste(null);
    }

    /**
     * 
     * @return
     * @throws NumberFormatException
     * @throws SRVException
     */
    public List<DadosIndicadorVO> obtemListaIndicadoresCorporativosBonusAnual() throws NumberFormatException, SRVException {
    	List<Integer> tiposRemuneracao = new ArrayList<Integer>();
    	tiposRemuneracao.add(new Integer(1));
    	DadosIndicadorVO filtroIndicadorVO = new DadosIndicadorVO();
    	filtroIndicadorVO.setAtivo("S");
    	filtroIndicadorVO.setIdGrupoIndicador(7);
    	return IndicadorBusiness.getInstance().obtemListaIndicadoresPorTipo(filtroIndicadorVO, tiposRemuneracao, false);
    }

    /**
     * 
     * @param diretoria
     * @return
     * @throws NumberFormatException
     * @throws SRVException
     */
    public List<DadosIndicadorVO> obtemListaIndicadoresCorporativosBonusAnualDiretoria(String diretoria) throws NumberFormatException, SRVException {
    	List<Integer> tiposRemuneracao = new ArrayList<Integer>();
    	tiposRemuneracao.add(new Integer(1));
    	DadosIndicadorVO filtroIndicadorVO = new DadosIndicadorVO();
    	filtroIndicadorVO.setAtivo("S");
    	filtroIndicadorVO.setIdGrupoIndicador(7);
    	filtroIndicadorVO.setDescricaoDiretoria(diretoria);
    	return IndicadorBusiness.getInstance().obtemListaIndicadoresPorTipo(filtroIndicadorVO, tiposRemuneracao, false);
    }

    /**
     * 
     * @param diretoria
     * @return
     * @throws NumberFormatException
     * @throws SRVException
     */
    public List<DadosIndicadorVO> obtemListaIndicadoresCorporativosBonusAnualSubindicador(int codIndicador) throws NumberFormatException, SRVException {
    	List<Integer> tiposRemuneracao = new ArrayList<Integer>();
    	tiposRemuneracao.add(new Integer(1));
    	DadosIndicadorVO filtroIndicadorVO = new DadosIndicadorVO();
    	filtroIndicadorVO.setAtivo("S");
    	filtroIndicadorVO.setIdGrupoIndicador(7);
    	filtroIndicadorVO.setIdIndicadorPai(codIndicador);
    	return IndicadorBusiness.getInstance().obtemListaIndicadoresPorTipo(filtroIndicadorVO, tiposRemuneracao, true);
    }

    /**
     * 
     * @return
     * @throws SRVException
     */
    public List<String> obtemListaFonte() throws SRVException {
    	return IndicadorBusiness.getInstance().obtemListaFonte();
    }	

    /**
     * 
     * @return
     * @throws SRVException
     */
    public List<String> obtemListaDiretoria() throws SRVException {
    	return IndicadorBusiness.getInstance().obtemListaDiretoria();
    }	

    /**
     * 
     * @param periodo
     * @param codGrpIndic
     * @param codGrpRemVar
     * @return
     * @throws NumberFormatException
     * @throws SRVException
     * @throws ParseException 
     */
    public List<DadosIndicadorVO> obtemListaIndicadoresAtivosPorPeriodo(int ano, int codGrpIndic, int codGrpRemVar) throws SRVException, ParseException {
    	List<DadosIndicadorVO> lista = BonusAnualEngine.getInstance().obtemListaIndicadorPeriodoBonus(ano, codGrpIndic);
    	return lista;
    }

    /**
     * 
     * @param codIndicIni
     * @return
     */
    public boolean isCodigoIndicadorJaUtilizado(Integer codIndicIni) {
    	boolean isCodigoIndicadorJaUtilizado = false;
    	try {
			Integer maxCodIndic = IndicadorBusiness.getInstance().obtemMaiorCodIndicador();
			if (ObjectHelper.isNotEmpty(maxCodIndic) && maxCodIndic.intValue() >= codIndicIni) {
				isCodigoIndicadorJaUtilizado = true;
			}
		} catch (SRVException e) {
			e.printStackTrace();
		}
    	return isCodigoIndicadorJaUtilizado;
    }

}