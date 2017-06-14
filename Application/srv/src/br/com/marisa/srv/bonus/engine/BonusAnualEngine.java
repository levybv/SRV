package br.com.marisa.srv.bonus.engine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import br.com.marisa.srv.bonus.business.RealizFuncIndicadorBusiness;
import br.com.marisa.srv.bonus.configuracao.business.ConfiguracaoBonusBusiness;
import br.com.marisa.srv.bonus.configuracao.vo.ConfiguracaoBonusVO;
import br.com.marisa.srv.cargo.business.CargoBusiness;
import br.com.marisa.srv.cargo.vo.CargoVO;
import br.com.marisa.srv.classehay.business.ClasseHayBusiness;
import br.com.marisa.srv.classehay.vo.ClasseHayVO;
import br.com.marisa.srv.escala.business.FaixaEscalaBusiness;
import br.com.marisa.srv.escala.vo.EscalaVO;
import br.com.marisa.srv.escala.vo.FaixaEscalaVO;
import br.com.marisa.srv.funcionario.business.FuncionarioBusiness;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.enumeration.StatCalcRealzEnum;
import br.com.marisa.srv.geral.excecoes.BonusException;
import br.com.marisa.srv.geral.excecoes.MensagemException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.indicador.business.IndicadorBusiness;
import br.com.marisa.srv.indicador.dao.IndicadorDAO;
import br.com.marisa.srv.indicador.dao.RealizFuncIndicadorDAO;
import br.com.marisa.srv.indicador.vo.DadosIndicadorVO;
import br.com.marisa.srv.indicador.vo.IndicadorFuncionarioRealizadoVO;

/**
 * 
 * 
 * @author Levy Villar
 * 
 */
public class BonusAnualEngine {

    private static final Logger log = Logger.getLogger(BonusAnualEngine.class);    

	private static BonusAnualEngine instance = new BonusAnualEngine();

	/**
	 * 
	 * @return
	 */
	public static final BonusAnualEngine getInstance() {
		return instance;
	}

	/**
	 * Singleton
	 */
	private BonusAnualEngine() {
	}

	/**
	 * 
	 * @param idFuncionario
	 * @param idIndicador
	 * @param ano
	 * @param mes
	 * @param realizadoXMeta
	 * @return
	 * @throws SRVException
	 */
//	public IndicadorFuncionarioRealizadoVO calculaAtingimentoByRealizXMeta(Long idFuncionario, Integer idIndicador, Integer ano, Integer mes, Double realizadoXMeta) throws SRVException {
//
//		FuncionarioVO funcionarioVO = FuncionarioBusiness.getInstance().obtemFuncionario(idFuncionario);
//		DadosIndicadorVO indicadorVO = IndicadorBusiness.getInstance().obtemIndicador(idIndicador);
//
//		IndicadorFuncionarioRealizadoVO pesquisaVO = RealizFuncIndicadorBusiness.getInstance().obtemRealizadoFuncIndicador(idFuncionario, idIndicador, Constantes.CODIGO_EMPRESA, funcionarioVO.getIdFilial(), ano, mes);
//		if (pesquisaVO != null) {
//			if (indicadorVO != null) {
//
//				FaixaEscalaVO faixaEscalaVO = null;
//				if (pesquisaVO.getIdEscala()==11) {
//					faixaEscalaVO = FaixaEscalaBusiness.getInstance().obtemFaixaEscala(77, realizadoXMeta);
//				} else {
//					faixaEscalaVO = FaixaEscalaBusiness.getInstance().obtemFaixaEscala(pesquisaVO.getIdEscala(), realizadoXMeta);
//				}
//				Double desempenho = faixaEscalaVO.getRealizado() == Constantes.ZERO.doubleValue() ? Constantes.ZERO.doubleValue() : ((pesquisaVO.getPeso()*faixaEscalaVO.getRealizado())/Constantes.CEM.doubleValue());
//				pesquisaVO.setRealizadoXMeta(realizadoXMeta);
//				pesquisaVO.setRealizadoPonderacao(desempenho);
//			}
//		}
//
//		return pesquisaVO;
//	}

	/**
	 * 
	 * @param idFuncionario
	 * @param desempenhoTotal
	 * @return
	 * @throws SRVException
	 */
//	public RelatorioBonusVO calculaDesempenho(Long idFuncionario, Double desempenhoTotal) throws SRVException {
//
//		FuncionarioVO funcionarioVO = FuncionarioBusiness.getInstance().obtemFuncionario(idFuncionario);
//		CargoVO cargoVO = CargoBusiness.getInstance().obtemCargo(funcionarioVO.getIdCargo());
//		ClasseHayVO classeHayVO = ClasseHayBusiness.getInstance().obtemClasseHay(cargoVO.getIdClasseHay());
////		Map<String, String> paramFunding = ParametroBusiness.getInstance().obtemParametro(ParametrosConstantes.PARAMETRO_FUNDING, String.valueOf(ano));
////		Double funding = Double.parseDouble(paramFunding.get(ParametrosConstantes.FUNDING_PERCENTUAL));
//
//		Double desempenhoTotalLimitado = desempenhoTotal > Constantes.CEM.doubleValue() ? Constantes.CEM.doubleValue() : desempenhoTotal;
//		boolean isAtingiu = desempenhoTotalLimitado >= 80 ? true : false; 
//		Double salarioTarget = classeHayVO.getSalarioMinimo() / 2;
////		Double qtdSalario = (salarioTarget * (desempenhoTotalLimitado/Constantes.CEM.doubleValue()) * funding);
//		Double qtdSalario = 0.0D;
//
//		RelatorioBonusVO relatorioBonusVO = new RelatorioBonusVO();
//		relatorioBonusVO.setValorDesempenho(desempenhoTotalLimitado);
//		relatorioBonusVO.setSalarioTarget(salarioTarget);
//		relatorioBonusVO.setQtdSalario(qtdSalario);
//		relatorioBonusVO.setIsAtingiu(isAtingiu);
//
//		return relatorioBonusVO;
//	}

	/**
	 * 
	 * @param idFuncionario
	 * @param idFilial
	 * @param idEmpresa
	 * @param ano
	 * @param mes
	 * @param novoStatus
	 * @throws SRVException 
	 */
	public void alteraStatusBonusCorporativo(Long idFuncionario, Integer idFilial, Integer idEmpresa, Integer ano, Integer mes, StatCalcRealzEnum novoStatus) throws SRVException {
		if (novoStatus == null) {
			throw new MensagemException("Novo status é inválido!");
		} else {
			IndicadorFuncionarioRealizadoVO indicadorCorporativoVO = null;
			List<IndicadorFuncionarioRealizadoVO> listaSemCorporativoVO = new ArrayList<IndicadorFuncionarioRealizadoVO>();

			List<IndicadorFuncionarioRealizadoVO> listaRealizIndicFuncVO = RealizFuncIndicadorBusiness.getInstance().obtemListaRealizadoFuncIndicador(idFuncionario, idFilial, idEmpresa, ano, mes, Constantes.DESCR_TIPO_REM_VAR_CORPORATIVO);
			if (listaRealizIndicFuncVO.isEmpty()) {
				throw new MensagemException("Bônus não configurado para o funcionário: " + idFuncionario);
			} else {
				for (IndicadorFuncionarioRealizadoVO indicadorFuncionarioRealizadoVO : listaRealizIndicFuncVO) {
					if (indicadorFuncionarioRealizadoVO.getIdIndicador().intValue() == Constantes.COD_INDIC_AGRUPAMENTO_CORPORATIVO.intValue()) {
						indicadorCorporativoVO = indicadorFuncionarioRealizadoVO;
					} else {
						listaSemCorporativoVO.add(indicadorFuncionarioRealizadoVO);
					}
				}
				if (indicadorCorporativoVO == null) {
					throw new MensagemException("Indicador corporativo não foi encontrado!");
				} else {
					if (indicadorCorporativoVO.getPeso() != null && indicadorCorporativoVO.getPeso().doubleValue() != Constantes.CEM.doubleValue()) {
						throw new MensagemException("O total de peso não totaliza 100% para o funcionário: " + idFuncionario);
					}
				}
			}
			for (IndicadorFuncionarioRealizadoVO indicadorFuncionarioRealizadoVO : listaSemCorporativoVO) {
				if (novoStatus.getCodigo() > StatCalcRealzEnum.INICIADO.getCodigo()) {
					if (indicadorFuncionarioRealizadoVO.getPeso() == null) {
						throw new MensagemException("O peso do indicador não foi definido! Cod. Indic: " + indicadorFuncionarioRealizadoVO.getIdIndicador());
					}
					if (indicadorFuncionarioRealizadoVO.getMeta() == null) {
						throw new MensagemException("A meta do indicador não foi definida! Cod. Indic: " + indicadorFuncionarioRealizadoVO.getIdIndicador());
					}
				}
			}
			//grava status

		}
	}

	/**
	 * Executa o processamento do bônus anual corporativo baseado no mês e ano. Parâmetros idFuncionario, idFilial e idEmpresa são opcionais para filtragem do processamento.
	 * 
	 * @param idFuncionario
	 * @param idFilial
	 * @param idEmpresa
	 * @param ano
	 * @param mes
	 * @throws SRVException
	 */
	public void calculaBonusCorporativo(Long idFuncionario, Integer idFilial, Integer idEmpresa, Integer ano, Integer mes) throws SRVException {

		RealizFuncIndicadorDAO realizFuncIndicadorDAO = new RealizFuncIndicadorDAO();

		try {

			Double funding = this.obtemFunding(ano);

			realizFuncIndicadorDAO.beginTrans();

			List<IndicadorFuncionarioRealizadoVO> listaRealizFuncIndicTemp = realizFuncIndicadorDAO.obtemListaRealizadoFuncIndicador(idFuncionario, idFilial, idEmpresa, ano, mes, Constantes.DESCR_TIPO_REM_VAR_CORPORATIVO);
			Map<Long, Map<Integer, List<IndicadorFuncionarioRealizadoVO>>> mapRealizFuncIndic = this.obtemMapOrdenadaRealizadoFuncionario(listaRealizFuncIndicTemp);
			listaRealizFuncIndicTemp = null;

			Set<Long> idFuncMap = mapRealizFuncIndic.keySet();
			if (!idFuncMap.isEmpty()) {
				for (Long idFuncProc : idFuncMap) {

					Map<Integer, List<IndicadorFuncionarioRealizadoVO>> mapFilial = mapRealizFuncIndic.get(idFuncProc);
					Set<Integer> idFilialMap = mapFilial.keySet();
					for (Integer idFilialProc : idFilialMap) {

						List<IndicadorFuncionarioRealizadoVO> listaNovaRealizFuncIndic = new ArrayList<IndicadorFuncionarioRealizadoVO>();

						List<IndicadorFuncionarioRealizadoVO> listaRealizFuncIndic = mapFilial.get(idFilialProc);
						for (IndicadorFuncionarioRealizadoVO itemRealizFuncIndic : listaRealizFuncIndic) {

							if (itemRealizFuncIndic.getIdIndicador().intValue() != Constantes.COD_INDIC_AGRUPAMENTO_CORPORATIVO.intValue()) {
								DadosIndicadorVO indicadorVO = IndicadorBusiness.getInstance().obtemIndicador(itemRealizFuncIndic.getIdIndicador());
								IndicadorFuncionarioRealizadoVO novoIndicFuncRealizVO = this.criaNovoIndicadorFuncionarioRealizadoVO(itemRealizFuncIndic, indicadorVO);

								this.calculaRealizadoXMeta(novoIndicFuncRealizVO, indicadorVO);
								this.calculaRealizadoPonderacao(novoIndicFuncRealizVO);

								listaNovaRealizFuncIndic.add(novoIndicFuncRealizVO);
							}

						}

						if (listaNovaRealizFuncIndic.isEmpty()) {
							realizFuncIndicadorDAO.excluiRealizadoFuncIndicador(idFuncProc, Constantes.COD_INDIC_AGRUPAMENTO_CORPORATIVO, Constantes.CODIGO_EMPRESA, idFilialProc, ano, mes);
						} else {
							IndicadorFuncionarioRealizadoVO agrupamentoVO = this.criaIndicadorCorporativo(listaNovaRealizFuncIndic);
							this.calculaPremio(agrupamentoVO, funding);
							listaNovaRealizFuncIndic.add(agrupamentoVO);
						}

						//insere lista de indicadores
						for (IndicadorFuncionarioRealizadoVO processaVO : listaNovaRealizFuncIndic) {
							realizFuncIndicadorDAO.incluiRealizadoFuncIndicador(processaVO);
						}
						realizFuncIndicadorDAO.commitTrans();
					}
				}
			}
			realizFuncIndicadorDAO.commitTrans();

		} catch (BonusException bex) {
			throw bex;
		} catch (Exception ex) {
			realizFuncIndicadorDAO.rollbackTrans();
			ex.printStackTrace();
			throw new MensagemException(ex.getMessage());
		} finally {
			realizFuncIndicadorDAO.closeConnection();
		}
	}

	/**
	 * Busca o cargo do funcionário.
	 * 
	 * @param idCargo
	 * @param idFuncionario
	 * @return
	 * @throws SRVException
	 */
	private CargoVO obtemCargo(Integer idCargo, Long idFuncionario) throws SRVException {
		CargoVO cargoVO = null;
		if (idCargo != null) {
			cargoVO = CargoBusiness.getInstance().obtemCargo(idCargo);
			if (cargoVO == null) {
				throw new BonusException("Cargo não cadastrado no SRV: " + idCargo);
			}
		} else {
			FuncionarioVO funcionarioVO = FuncionarioBusiness.getInstance().obtemFuncionario(idFuncionario);
			if (funcionarioVO != null && funcionarioVO.getIdCargo() != null) {
				cargoVO = this.obtemCargo(funcionarioVO.getIdCargo(), idFuncionario);
			} else {
				throw new BonusException("Cargo não informado para o funcionário: " + idFuncionario);
			}
		}
		return cargoVO;
	}

	/**
	 * Busca a ClasseHay do cargo.
	 * 
	 * @param idClasseHay
	 * @param idCargo
	 * @return
	 * @throws SRVException
	 */
	private ClasseHayVO obtemClasseHay(Integer idClasseHay, Integer idCargo) throws SRVException {
		ClasseHayVO classeHayVO = null;
		if (idClasseHay != null) {
			classeHayVO = ClasseHayBusiness.getInstance().obtemClasseHay(idClasseHay);
			if (classeHayVO == null) {
				throw new BonusException("Classe Hay não cadastrada no SRV: " + idClasseHay);
			}
		} else {
			throw new BonusException("Classe Hay não informada para o cargo: " + idCargo);
		}
		return classeHayVO;
	}

	/**
	 * Calcula o valor do prêmio considerando o funding.
	 * 
	 * @param agrupamentoVO
	 * @param funding
	 * @throws SRVException
	 */
	private void calculaPremio(IndicadorFuncionarioRealizadoVO agrupamentoVO, Double funding) throws SRVException {

		if (agrupamentoVO != null) {

			CargoVO cargoVO = this.obtemCargo(agrupamentoVO.getIdCargo(), agrupamentoVO.getIdFuncionario());
			ClasseHayVO classeHayVO = this.obtemClasseHay(cargoVO.getIdClasseHay(), cargoVO.getIdCargo());

			Double realizadoPonderacaoLimitado = agrupamentoVO.getRealizadoPonderacao() > Constantes.CEM.doubleValue() ? Constantes.CEM.doubleValue() : agrupamentoVO.getRealizadoPonderacao();
			boolean isAtingiu = realizadoPonderacaoLimitado >= Constantes.OITENTA.doubleValue() ? true : false;
			Double salarioTarget = classeHayVO.getSalarioMinimo() / 2;
			Double qtdSalario = (salarioTarget * (realizadoPonderacaoLimitado / Constantes.CEM.doubleValue()) * funding);
			qtdSalario = isAtingiu ? qtdSalario : Constantes.ZERO.doubleValue();

			agrupamentoVO.setValorPremio(salarioTarget);
			agrupamentoVO.setValorPremioCalculado(qtdSalario);
			agrupamentoVO.setUnidadeValorPremioCalculado(Constantes.UNIDADE_UNIDADE);

		}

	}

	/**
	 * Cria e retorna um novo indicador corporativo baseado na lista enviada, totalizando os pesos e realizado ponderação. 
	 * 
	 * @param listaRealizado
	 * @return
	 * @throws SRVException
	 */
	private IndicadorFuncionarioRealizadoVO criaIndicadorCorporativo(List<IndicadorFuncionarioRealizadoVO> listaRealizado) throws SRVException {

		//numRealiz (total indic 32/33)
		//uniRealiz
		//numRealizXMeta (atingimento indic 32/33)
		//uniRealizXMeta
		//qtdMesesProp
		IndicadorFuncionarioRealizadoVO agrupamentoVO = null;

		if (listaRealizado != null && !listaRealizado.isEmpty()) {

			IndicadorFuncionarioRealizadoVO tmpIndicFuncRealizVO = listaRealizado.get(0);

			FuncionarioVO funcionarioVO = FuncionarioBusiness.getInstance().obtemFuncionario(tmpIndicFuncRealizVO.getIdFuncionario());

			agrupamentoVO = new IndicadorFuncionarioRealizadoVO();
			agrupamentoVO.setIdIndicador(Constantes.COD_INDIC_AGRUPAMENTO_CORPORATIVO);
			agrupamentoVO.setAno(tmpIndicFuncRealizVO.getAno());
			agrupamentoVO.setMes(tmpIndicFuncRealizVO.getMes());
			agrupamentoVO.setIdFuncionario(tmpIndicFuncRealizVO.getIdFuncionario());
			agrupamentoVO.setIdFilial(tmpIndicFuncRealizVO.getIdFilial());
			agrupamentoVO.setIdEmpresa(tmpIndicFuncRealizVO.getIdEmpresa());
			agrupamentoVO.setIdCargo(tmpIndicFuncRealizVO.getIdCargo());
			agrupamentoVO.setIdStatusCalcRelz(StatCalcRealzEnum.INICIADO.getCodigo());
			agrupamentoVO.setPeso(Constantes.ZERO.doubleValue());
			agrupamentoVO.setUnidadePeso(Constantes.UNIDADE_PERCENTUAL);
			agrupamentoVO.setRealizadoPonderacao(Constantes.ZERO.doubleValue());
			agrupamentoVO.setUnidadeRealizadoPonderacao(Constantes.UNIDADE_PERCENTUAL);
			agrupamentoVO.setIdUsuarioAlteracao(Constantes.SRV_ID_USER_ADMIN);
			agrupamentoVO.setCodSitCalcRealzFunc(funcionarioVO.getIdSituacaoRH());

			for (IndicadorFuncionarioRealizadoVO indicadorFuncionarioRealizadoVO : listaRealizado) {
				if (indicadorFuncionarioRealizadoVO.getPeso() != null) {
					agrupamentoVO.setPeso(agrupamentoVO.getPeso() + indicadorFuncionarioRealizadoVO.getPeso());
				}
				if (indicadorFuncionarioRealizadoVO.getRealizadoPonderacao() != null) {
					agrupamentoVO.setRealizadoPonderacao(agrupamentoVO.getRealizadoPonderacao() + indicadorFuncionarioRealizadoVO.getRealizadoPonderacao());
				}
			}

		}

		return agrupamentoVO;
	}

	/**
	 * Calcula realizado ponderação do indicador enviado.
	 * 
	 * @param indicFuncRealizVO
	 * @throws SRVException
	 */
	public void calculaRealizadoPonderacao(IndicadorFuncionarioRealizadoVO indicFuncRealizVO) throws SRVException {

		if (indicFuncRealizVO.getRealizadoXMeta() != null) {

			if (indicFuncRealizVO.getIdEscala() == null) {
				throw new MensagemException("Escala nao configurada para o indicador: " + indicFuncRealizVO.getIdIndicador());
			} else {
				indicFuncRealizVO.setIdEscala(indicFuncRealizVO.getIdEscala());
			}

			try {

				Double realizadoXMeta = indicFuncRealizVO.getRealizadoXMeta() < Constantes.ZERO.doubleValue() ? Constantes.ZERO.doubleValue() : indicFuncRealizVO.getRealizadoXMeta();
				realizadoXMeta = realizadoXMeta > Constantes.DEZ_MIL.doubleValue() ? Constantes.DEZ_MIL.doubleValue() : realizadoXMeta;
				realizadoXMeta = BigDecimal.valueOf(realizadoXMeta).setScale(2, RoundingMode.FLOOR).doubleValue();

				FaixaEscalaVO faixaEscalaVO = FaixaEscalaBusiness.getInstance().obtemFaixaEscala(indicFuncRealizVO.getIdEscala() == 11 ? 77 : indicFuncRealizVO.getIdEscala(), realizadoXMeta);
				indicFuncRealizVO.setSequencialEscala(faixaEscalaVO.getSequencial());
				indicFuncRealizVO.setRealizadoFaixa(faixaEscalaVO.getRealizado());
				indicFuncRealizVO.setUnidadeRealizadoFaixa(faixaEscalaVO.getIdUnidadeFaixa());
	
				if (faixaEscalaVO.getRealizado() == Constantes.ZERO.doubleValue()) {
					indicFuncRealizVO.setRealizadoPonderacao(Constantes.ZERO.doubleValue());
				} else {
					indicFuncRealizVO.setRealizadoPonderacao((indicFuncRealizVO.getPeso()*faixaEscalaVO.getRealizado())/Constantes.CEM.doubleValue());
				}
				indicFuncRealizVO.setUnidadeRealizadoPonderacao(Constantes.UNIDADE_PERCENTUAL);
	
			} catch (Exception ex) {
				throw new SRVException(log,"Erro ao calcular o realizado ponderacao para o funcionario: " + indicFuncRealizVO.getIdFuncionario() + " codIndic: " + indicFuncRealizVO.getIdIndicador(),ex);
			}
		}

	}

	/**
	 * Calcula realizado x meta do indicador enviado.
	 * 
	 * @param indicFuncRealizVO
	 * @param indicadorVO
	 * @throws MensagemException
	 */
	private void calculaRealizadoXMeta(IndicadorFuncionarioRealizadoVO indicFuncRealizVO, DadosIndicadorVO indicadorVO) throws MensagemException {

		if (indicFuncRealizVO.getMeta() != null && indicFuncRealizVO.getRealizado() != null) {
			Double realizadoXMeta = null;
			if (indicadorVO.getFlgSentido().equalsIgnoreCase(Constantes.SENTIDO_INDICADOR_MELHOR_BAIXO)) {
				if (indicFuncRealizVO.getMeta().doubleValue() == Constantes.ZERO.doubleValue()) {
					realizadoXMeta = Constantes.CEM.doubleValue();
				} else {
					realizadoXMeta = ((indicFuncRealizVO.getMeta() - (indicFuncRealizVO.getRealizado() - indicFuncRealizVO.getMeta())) / indicFuncRealizVO.getMeta()) * Constantes.CEM.doubleValue();
				}
			} else if (indicadorVO.getFlgSentido().equalsIgnoreCase(Constantes.SENTIDO_INDICADOR_MELHOR_CIMA)) {
				if (indicFuncRealizVO.getMeta().doubleValue() == Constantes.ZERO.doubleValue()) {
					realizadoXMeta = Constantes.ZERO.doubleValue();
				} else {
					if (indicFuncRealizVO.getMeta().doubleValue() < Constantes.ZERO.doubleValue() && indicFuncRealizVO.getRealizado().doubleValue() < Constantes.ZERO.doubleValue()) {
						realizadoXMeta = (indicFuncRealizVO.getMeta()/indicFuncRealizVO.getRealizado()) * Constantes.CEM.doubleValue();
					} else if (indicFuncRealizVO.getMeta().doubleValue() < Constantes.ZERO.doubleValue() && indicFuncRealizVO.getRealizado().doubleValue() > Constantes.ZERO.doubleValue()) {
						realizadoXMeta = (indicFuncRealizVO.getRealizado()/indicFuncRealizVO.getMeta()) * Constantes.CEM.doubleValue();
						realizadoXMeta = realizadoXMeta * Constantes.MENOS_UM.doubleValue();
					} else {
						realizadoXMeta = (indicFuncRealizVO.getRealizado()/indicFuncRealizVO.getMeta()) * Constantes.CEM.doubleValue();
					}
				}
			} else {
				//Sentido do indicador nao esperado
			}

			realizadoXMeta = BigDecimal.valueOf(realizadoXMeta).setScale(2, RoundingMode.FLOOR).doubleValue();

			indicFuncRealizVO.setRealizadoXMeta(realizadoXMeta);
			indicFuncRealizVO.setUnidadeRealizadoXMeta(Constantes.UNIDADE_PERCENTUAL);
		}

	}

	/**
	 * Ordena a lista de realizado (por funcionário/filial) e retorna uma Map.
	 * 
	 * @param listaRealizadoParam
	 * @return
	 */
	private Map<Long, Map<Integer, List<IndicadorFuncionarioRealizadoVO>>> obtemMapOrdenadaRealizadoFuncionario(List<IndicadorFuncionarioRealizadoVO> listaRealizadoParam) {

		Long idFuncAnterior = new Long(0);
		Integer idFilialAnterior = new Integer(0);

		Map<Long, Map<Integer, List<IndicadorFuncionarioRealizadoVO>>> mapFuncionario = new HashMap<Long, Map<Integer, List<IndicadorFuncionarioRealizadoVO>>>();
		Map<Integer, List<IndicadorFuncionarioRealizadoVO>> mapFilial = new HashMap<Integer, List<IndicadorFuncionarioRealizadoVO>>();
		List<IndicadorFuncionarioRealizadoVO> listRealizFunc = new ArrayList<IndicadorFuncionarioRealizadoVO>();

		for (IndicadorFuncionarioRealizadoVO indicadorFuncionarioRealizadoVO : listaRealizadoParam) {

			if (idFuncAnterior == Constantes.ZERO.longValue() && idFilialAnterior == Constantes.ZERO.intValue()) {
				listRealizFunc.add(indicadorFuncionarioRealizadoVO);
				idFilialAnterior = indicadorFuncionarioRealizadoVO.getIdFilial();
				idFuncAnterior = indicadorFuncionarioRealizadoVO.getIdFuncionario();
			} else {
				if (idFuncAnterior.longValue() == indicadorFuncionarioRealizadoVO.getIdFuncionario().longValue()) {
					if (idFilialAnterior.intValue() == indicadorFuncionarioRealizadoVO.getIdFilial().intValue()) {
						listRealizFunc.add(indicadorFuncionarioRealizadoVO);
					} else {
						mapFilial.put(idFilialAnterior, listRealizFunc);
						idFilialAnterior = indicadorFuncionarioRealizadoVO.getIdFilial();
						listRealizFunc = new ArrayList<IndicadorFuncionarioRealizadoVO>();
						listRealizFunc.add(indicadorFuncionarioRealizadoVO);
					}
				} else {
					mapFilial.put(idFilialAnterior, listRealizFunc);
					mapFuncionario.put(idFuncAnterior, mapFilial);
					mapFilial = new HashMap<Integer, List<IndicadorFuncionarioRealizadoVO>>();
					listRealizFunc =  new ArrayList<IndicadorFuncionarioRealizadoVO>();
					listRealizFunc.add(indicadorFuncionarioRealizadoVO);
					idFuncAnterior = indicadorFuncionarioRealizadoVO.getIdFuncionario();
					idFilialAnterior = indicadorFuncionarioRealizadoVO.getIdFilial();
				}
			}

		}
		if (!listRealizFunc.isEmpty()) {
			mapFilial.put(idFilialAnterior, listRealizFunc);
			mapFuncionario.put(idFuncAnterior, mapFilial);
		}

		return mapFuncionario;
	}

	/**
	 * Cria e retorna um novo indicador com base no enviado.
	 * 
	 * @param indicFuncRealizParam
	 * @param indicadorVO
	 * @return
	 */
	private IndicadorFuncionarioRealizadoVO criaNovoIndicadorFuncionarioRealizadoVO(IndicadorFuncionarioRealizadoVO indicFuncRealizParam, DadosIndicadorVO indicadorVO) {
		IndicadorFuncionarioRealizadoVO novoIndicFuncRealizVO = new IndicadorFuncionarioRealizadoVO();
		novoIndicFuncRealizVO.setAno(indicFuncRealizParam.getAno());
		novoIndicFuncRealizVO.setMes(indicFuncRealizParam.getMes());
		novoIndicFuncRealizVO.setIdFuncionario(indicFuncRealizParam.getIdFuncionario());
		novoIndicFuncRealizVO.setIdFilial(indicFuncRealizParam.getIdFilial());
		novoIndicFuncRealizVO.setIdEmpresa(indicFuncRealizParam.getIdEmpresa());
		novoIndicFuncRealizVO.setIdIndicador(indicadorVO.getIdIndicador());
		novoIndicFuncRealizVO.setIdUsuarioAlteracao(Constantes.SRV_ID_USER_ADMIN);
		novoIndicFuncRealizVO.setIdEscala(indicadorVO.getIdEscala());
		novoIndicFuncRealizVO.setPeso(indicFuncRealizParam.getPeso());
		novoIndicFuncRealizVO.setUnidadePeso(indicFuncRealizParam.getUnidadePeso());
		novoIndicFuncRealizVO.setMeta(indicFuncRealizParam.getMeta());
		novoIndicFuncRealizVO.setUnidadeMeta(indicFuncRealizParam.getUnidadeMeta());
		novoIndicFuncRealizVO.setRealizado(indicFuncRealizParam.getRealizado());
		novoIndicFuncRealizVO.setUnidadeRealizado(indicFuncRealizParam.getUnidadeRealizado());
		novoIndicFuncRealizVO.setIdCargo(indicFuncRealizParam.getIdCargo());
		return novoIndicFuncRealizVO;
	}

	/**
	 * Busca o valor do funding configurado para o ano enviado.
	 * 
	 * @param ano
	 * @return
	 * @throws BonusException
	 */
	private Double obtemFunding(Integer ano) throws BonusException {
		Double funding = null;
		try {
			
			ConfiguracaoBonusVO configuracaoBonusVO = this.obtemConfiguracaoBonus(ano);
			if (configuracaoBonusVO.getIsFunding()) {
				funding = configuracaoBonusVO.getFunding();
			} else {
				funding = Constantes.UM.doubleValue();
			}
		} catch (BonusException bex) {
			throw bex;
		} catch (Exception ex) {
			log.error("Erro ao obter o FUNDING para ano: " + ano, ex);
			throw new BonusException("Erro ao obter o FUNDING para ano: " + ano, ex);
		}
		return funding;
	}

	/**
	 * 
	 * @return
	 * @throws BonusException
	 */
	public ConfiguracaoBonusVO obtemParametroAtivoBonus() throws BonusException {
		ConfiguracaoBonusVO configBonusVO = null;
		try {
			ConfiguracaoBonusVO pesquisaConfigBonusVO = new ConfiguracaoBonusVO();
			pesquisaConfigBonusVO.setIsEncerrado(Boolean.FALSE);
			List<ConfiguracaoBonusVO> listaConfigBonusVO = ConfiguracaoBonusBusiness.getInstance().obtemListaConfiguracaoBonus(pesquisaConfigBonusVO);
			if (listaConfigBonusVO == null || listaConfigBonusVO.size() == 0) {
				throw new BonusException("Nenhuma configuracao de BONUS ANUAL esta ativa");
			} else if (listaConfigBonusVO.size() > 1) {
				throw new BonusException("Mais de uma configuracao de BONUS ANUAL esta ativa");
			} else {
				configBonusVO = listaConfigBonusVO.get(0);
			}
		} catch (BonusException bex) {
			throw bex;
		} catch (Exception ex) {
			log.error("Erro ao obter a configuracao do BONUS ANUAL", ex);
			throw new BonusException("Erro ao obter a configuracao do BONUS ANUAL: " + ex.getMessage());
		}
		return configBonusVO;
	}

	/**
	 * 
	 * @param ano
	 * @return
	 * @throws BonusException
	 */
	public ConfiguracaoBonusVO obtemConfiguracaoBonus(Integer ano) throws BonusException {
		ConfiguracaoBonusVO configuracaoBonusVO = null;
		try {
			configuracaoBonusVO = ConfiguracaoBonusBusiness.getInstance().obtemConfiguracaoBonus(ano);
			if (configuracaoBonusVO == null) {
				throw new BonusException("Nenhuma configuracao de BONUS ANUAL foi encontrado para o ano " + ano);
			}
		} catch (BonusException bex) {
			throw bex;
		} catch (Exception ex) {
			log.error("Erro ao obter configuracao BONUS ANUAL para o ano " + ano, ex);
			throw new BonusException("Erro ao obter configuracao BONUS ANUAL para o ano " + ano + ": " + ex.getMessage());
		}
		return configuracaoBonusVO;
	}

	/**
	 * 
	 * @param configuracaoBonusVO
	 * @param idEscala
	 * @return
	 */
	public boolean isEscalaPermitidaBonus(ConfiguracaoBonusVO configuracaoBonusVO, Integer idEscala) {
		boolean isEscalaPermitidaBonus = false;
		try {
			for (EscalaVO escalaBonusVO : configuracaoBonusVO.getListaEscala()) {
				if (escalaBonusVO.getIdEscala().intValue() == idEscala.intValue()) {
					isEscalaPermitidaBonus = true;
				}
			}
		} catch (Exception ex) {
			isEscalaPermitidaBonus = false;
		}
		return isEscalaPermitidaBonus;
	}

	/**
	 * 
	 * @param ano
	 * @param codGrpIndic
	 * @return
	 * @throws BonusException
	 * @throws SRVException
	 */
	public List<DadosIndicadorVO> obtemListaIndicadorPeriodoBonus(Integer ano, Integer codGrpIndic) throws BonusException, SRVException {
		IndicadorDAO indicadorDAO = new IndicadorDAO();
		List<DadosIndicadorVO> listaIndicVO = new ArrayList<DadosIndicadorVO>();
		try {
			ConfiguracaoBonusVO configuracaoBonusVO = this.obtemConfiguracaoBonus(ano);
        	DadosIndicadorVO pesquisaVO = new DadosIndicadorVO();
			if (ObjectHelper.isNotEmpty(codGrpIndic)) {
	    		pesquisaVO.setIdGrupoIndicador(codGrpIndic);
			}
			List<DadosIndicadorVO> listaIndicTmp = indicadorDAO.obtemListaIndicadoresAtivosPorPeriodo(pesquisaVO, Constantes.ID_TIPO_REM_VAR_CORPORATIVO, configuracaoBonusVO.getCodIndicIni(), configuracaoBonusVO.getCodIndicFim());
			for (DadosIndicadorVO dadosIndicadorTmp : listaIndicTmp) {
				if (this.isEscalaPermitidaBonus(configuracaoBonusVO, dadosIndicadorTmp.getIdEscala())) {
					listaIndicVO.add(dadosIndicadorTmp);
				}
			}
		} finally {
			indicadorDAO.closeConnection();
		}
		return listaIndicVO;
	}
}