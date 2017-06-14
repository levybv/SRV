package br.com.marisa.srv.tlmkt.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.marisa.srv.filial.realizado.dao.RealizadoFilialIndicDAO;
import br.com.marisa.srv.filial.realizado.vo.RealizadoFilialIndicVO;
import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.funcionario.dao.FuncionarioDAO;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.constants.ParametrosConstantes;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.indicador.business.IndicadorBusiness;
import br.com.marisa.srv.indicador.vo.DadosIndicadorVO;
import br.com.marisa.srv.indicador.vo.IndicadorVO;
import br.com.marisa.srv.meta.dao.MetaFilialDAO;
import br.com.marisa.srv.meta.vo.MetaFilialVO;
import br.com.marisa.srv.parametros.dao.ParametroDAO;
import br.com.marisa.srv.tlmkt.dao.TlmktElegivelDAO;
import br.com.marisa.srv.tlmkt.vo.TelemarketingIndicadorVO;
import br.com.marisa.srv.tlmkt.vo.TelemarketingVO;

/**
 * 
 * @author Levy Villar
 *
 */
public class TelemarketingBusiness {

	//private static final Logger log = Logger.getLogger(TlmktElegivelBusiness.class);

	private static TelemarketingBusiness instance = new TelemarketingBusiness();

	/**
	 * 
	 * @return
	 */
	public static final TelemarketingBusiness getInstance() {
		return instance;
	}

	/**
	 * 
	 */
	private TelemarketingBusiness() {
	}

	/**
	 * 
	 * @return
	 * @throws SRVException
	 */
	public List<FuncionarioVO> obtemListaFuncionariosDisponiveis() throws SRVException {

		List<FuncionarioVO> listaFuncionario = new ArrayList<FuncionarioVO>();
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
		ParametroDAO parametroDAO = new ParametroDAO();

		try {
			parametroDAO.setConn(funcionarioDAO.getConn());

			List<Integer> listaIdFilial = new ArrayList<Integer>();
			listaIdFilial.add(Constantes.CODIGO_FILIAL_EC);

			Map<String, String> parametroTlmkt = parametroDAO.obtemParametro(ParametrosConstantes.PRC_FUNC_ELEGIVEL_TLMKT, ParametrosConstantes.PRM_FUNC_ELEGIVEL_TLMKT);
			int qtdCargo = Integer.parseInt(parametroTlmkt.get(ParametrosConstantes.PRM_QTD_CARGO));

			for (int i = 1; i <= qtdCargo; i++) {
				int codCargo = Integer.parseInt(parametroTlmkt.get(ParametrosConstantes.PRM_COD_CARGO+i));
				listaFuncionario.addAll(funcionarioDAO.obtemListaFuncionario(null, null, null, null, codCargo, null, listaIdFilial, null));
			}

		} finally {
			funcionarioDAO.closeConnection();
		}
		return listaFuncionario;
	}

	/**
	 * 
	 * @param ano
	 * @param mes
	 * @param codFuncionario
	 * @return
	 * @throws SRVException
	 */
	public TelemarketingVO obtemTlmktElegivel(Integer ano, Integer mes, Long codFuncionario) throws SRVException {

		TelemarketingVO tlmktElegivelVO = null;
		TlmktElegivelDAO tlmktElegivelDAO = new TlmktElegivelDAO();
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
		MetaFilialDAO metaFilialDAO = new MetaFilialDAO();
		RealizadoFilialIndicDAO realizadoFilialIndicDAO = new RealizadoFilialIndicDAO();

		try {
			funcionarioDAO.setConn(tlmktElegivelDAO.getConn());
			metaFilialDAO.setConn(tlmktElegivelDAO.getConn());
			realizadoFilialIndicDAO.setConn(tlmktElegivelDAO.getConn());

			tlmktElegivelVO = tlmktElegivelDAO.obtemTlmktElegivel(ano, mes, codFuncionario);
			List<FuncionarioVO> listaNewFuncionario = new ArrayList<FuncionarioVO>();
			for (FuncionarioVO funcionarioVO : tlmktElegivelVO.getListaFuncionarioVO()) {
				FuncionarioVO newFuncVO = funcionarioDAO.obtemFuncionario(funcionarioVO.getIdFuncionario());
				listaNewFuncionario.add(newFuncVO);
			}
			tlmktElegivelVO.setListaFuncionarioVO(listaNewFuncionario);

			DadosIndicadorVO pesquisaVO = new DadosIndicadorVO();
			pesquisaVO.setAtivo(Constantes.CD_VERDADEIRO);
			pesquisaVO.setIdGrupoIndicador(Constantes.COD_GRUPO_INDIC_TLMKT);
			List<DadosIndicadorVO> listaIndicadores = IndicadorBusiness.getInstance().obtemListaIndicadores(pesquisaVO, null, Boolean.FALSE, Boolean.FALSE);
			List<TelemarketingIndicadorVO> listaTlmktIndicadorVO = new ArrayList<TelemarketingIndicadorVO>();
			for (DadosIndicadorVO dadosIndicadorVO : listaIndicadores) {
				TelemarketingIndicadorVO telemarketingIndicadorVO = new TelemarketingIndicadorVO();

				MetaFilialVO metaFilialVO = metaFilialDAO.obtemMetaFilial(mes, ano, Constantes.CODIGO_EMPRESA, Constantes.CODIGO_FILIAL_EC, dadosIndicadorVO.getIdIndicador());
				if (metaFilialVO != null) {
					telemarketingIndicadorVO.setCodIndicador(dadosIndicadorVO.getIdIndicador());
					telemarketingIndicadorVO.setNomeIndicador(dadosIndicadorVO.getDescricaoIndicador());
					telemarketingIndicadorVO.setNumMeta(metaFilialVO.getValorMeta());
					telemarketingIndicadorVO.setCodUnMeta(metaFilialVO.getIdUnidadeMeta());
				}

				RealizadoFilialIndicVO realizadoFilialIndicVO = realizadoFilialIndicDAO.obtemRealizadoFilialIndic(dadosIndicadorVO.getIdIndicador(), Constantes.CODIGO_FILIAL_EC, Constantes.CODIGO_EMPRESA, ano, mes);
				if (realizadoFilialIndicVO != null) {
					telemarketingIndicadorVO.setCodIndicador(dadosIndicadorVO.getIdIndicador());
					telemarketingIndicadorVO.setNomeIndicador(dadosIndicadorVO.getDescricaoIndicador());
					telemarketingIndicadorVO.setNumRealizado(realizadoFilialIndicVO.getNumRealizado());
					telemarketingIndicadorVO.setCodUnRealizado(Constantes.UNIDADE_UNIDADE);
				}
				if (metaFilialVO != null || realizadoFilialIndicVO != null) {
					listaTlmktIndicadorVO.add(telemarketingIndicadorVO);
				}
			}
			tlmktElegivelVO.setListaIndicadorTlmktVO(listaTlmktIndicadorVO);

		} finally {
			tlmktElegivelDAO.closeConnection();
		}
		return tlmktElegivelVO;
	}

	/**
	 * 
	 * @param tlmktElegivelVO
	 * @throws SRVException
	 */
	public void incluiTlmktElegivel(TelemarketingVO tlmktElegivelVO) throws SRVException {
		TlmktElegivelDAO tlmktElegivelDAO = new TlmktElegivelDAO();
		RealizadoFilialIndicDAO realizadoFilialIndicDAO = new RealizadoFilialIndicDAO();
		MetaFilialDAO metaFilialDAO = new MetaFilialDAO();

		try {
			realizadoFilialIndicDAO.setConn(tlmktElegivelDAO.getConn());
			metaFilialDAO.setConn(tlmktElegivelDAO.getConn());

			tlmktElegivelDAO.beginTrans();

			//inclui funcionario elegivel
			for (FuncionarioVO funcionarioVO : tlmktElegivelVO.getListaFuncionarioVO()) {
				tlmktElegivelDAO.incluiTlmktElegivel(tlmktElegivelVO.getAno(), tlmktElegivelVO.getMes(), funcionarioVO.getIdFuncionario(), tlmktElegivelVO.getUsuarioVO().getIdUsuario());
			}

			List<TelemarketingIndicadorVO> listaTlmktIndicVO = tlmktElegivelVO.getListaIndicadorTlmktVO();
			for (TelemarketingIndicadorVO telemarketingIndicadorVO : listaTlmktIndicVO) {

				//inclui meta
				MetaFilialVO metaFilialVO = new MetaFilialVO();
				metaFilialVO.setAno(tlmktElegivelVO.getAno());
				metaFilialVO.setMes(tlmktElegivelVO.getMes());
				metaFilialVO.setIdEmpresa(Constantes.CODIGO_EMPRESA);
				metaFilialVO.setIdFilial(Constantes.CODIGO_FILIAL_EC);
				metaFilialVO.setIdIndicador(telemarketingIndicadorVO.getCodIndicador());
				metaFilialVO.setValorMeta(telemarketingIndicadorVO.getNumMeta());
				metaFilialVO.setIdUnidadeMeta(3);
				metaFilialVO.setValorPremioFilial(Constantes.ZERO.doubleValue());
				metaFilialVO.setIdUsuario(tlmktElegivelVO.getUsuarioVO().getIdUsuario());
				metaFilialDAO.incluiMetaFilial(metaFilialVO);

				//inclui realizado
				RealizadoFilialIndicVO realizadoFilialIndicVO = new RealizadoFilialIndicVO();
				realizadoFilialIndicVO.setAno(tlmktElegivelVO.getAno());
				realizadoFilialIndicVO.setMes(tlmktElegivelVO.getMes());
				FilialVO filialVO = new FilialVO();
				filialVO.setCodFilial(Constantes.CODIGO_FILIAL_EC);
				filialVO.setCodEmpresa(Constantes.CODIGO_EMPRESA);
				realizadoFilialIndicVO.setFilialVO(filialVO);
				IndicadorVO indicadorVO = new IndicadorVO();
				indicadorVO.setCodIndicador(telemarketingIndicadorVO.getCodIndicador());
				realizadoFilialIndicVO.setIndicadorVO(indicadorVO);
				realizadoFilialIndicVO.setNumRealizado(telemarketingIndicadorVO.getNumRealizado());
				realizadoFilialIndicVO.setCodUsuario(tlmktElegivelVO.getUsuarioVO().getIdUsuario());
				realizadoFilialIndicDAO.incluiRealizadoFilialIndic(realizadoFilialIndicVO);

			}

			tlmktElegivelDAO.commitTrans();

		} catch (Exception ex) {
			tlmktElegivelDAO.rollbackTrans();
			throw new SRVException(ex.getMessage(), ex);
		} finally {
			tlmktElegivelDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param ano
	 * @param mes
	 * @throws SRVException
	 */
	public void excluiTlmktElegivel(Integer ano, Integer mes) throws SRVException {
		TlmktElegivelDAO tlmktElegivelDAO = new TlmktElegivelDAO();
		RealizadoFilialIndicDAO realizadoFilialIndicDAO = new RealizadoFilialIndicDAO();
		MetaFilialDAO metaFilialDAO = new MetaFilialDAO();

		try {
			realizadoFilialIndicDAO.setConn(tlmktElegivelDAO.getConn());
			metaFilialDAO.setConn(tlmktElegivelDAO.getConn());
			tlmktElegivelDAO.beginTrans();

			//Exclui funcionarios elegiveis
			TelemarketingVO antigoTlmktVO = tlmktElegivelDAO.obtemTlmktElegivel(ano, mes, null);
			for (FuncionarioVO funcVO : antigoTlmktVO.getListaFuncionarioVO()) {
				tlmktElegivelDAO.incluiTlmktElegivelHist(ano, mes, funcVO.getIdFuncionario());
				tlmktElegivelDAO.excluiTlmktElegivel(ano, mes, funcVO.getIdFuncionario());
			}

			//Busca lista de indicadores tlmkt
			DadosIndicadorVO pesquisaVO = new DadosIndicadorVO();
			pesquisaVO.setAtivo(Constantes.CD_VERDADEIRO);
			pesquisaVO.setIdGrupoIndicador(Constantes.COD_GRUPO_INDIC_TLMKT);
			List<DadosIndicadorVO> listaIndicadores = IndicadorBusiness.getInstance().obtemListaIndicadores(pesquisaVO, null, Boolean.FALSE, Boolean.FALSE);
			for (DadosIndicadorVO dadosIndicadorVO : listaIndicadores) {

				//Exclui meta indicador
				MetaFilialVO metaFilialVO = metaFilialDAO.obtemMetaFilial(mes, ano, Constantes.CODIGO_EMPRESA, Constantes.CODIGO_FILIAL_EC, dadosIndicadorVO.getIdIndicador());
				if (metaFilialVO != null) {
					metaFilialDAO.incluiMetaFilialHistorico(metaFilialVO);
					metaFilialDAO.excluiMetaFilial(mes, ano, Constantes.CODIGO_EMPRESA, Constantes.CODIGO_FILIAL_EC, dadosIndicadorVO.getIdIndicador());
				}

				//Exclui realizado indicador
				RealizadoFilialIndicVO realizadoFilialIndicVO = realizadoFilialIndicDAO.obtemRealizadoFilialIndic(dadosIndicadorVO.getIdIndicador(), Constantes.CODIGO_FILIAL_EC, Constantes.CODIGO_EMPRESA, ano, mes);
				if (realizadoFilialIndicVO != null) {
					realizadoFilialIndicDAO.incluiRealizadoFilialIndicHist(realizadoFilialIndicVO);
					realizadoFilialIndicDAO.excluiRealizadoFilialIndic(realizadoFilialIndicVO);
				}
			}

			tlmktElegivelDAO.commitTrans();

		} catch (Exception ex) {
			tlmktElegivelDAO.rollbackTrans();
			throw new SRVException(ex.getMessage(), ex);
		} finally {
			tlmktElegivelDAO.closeConnection();
		}
	}

}
