package br.com.marisa.srv.bonus.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.bonus.configuracao.vo.ConfiguracaoBonusVO;
import br.com.marisa.srv.bonus.engine.BonusAnualEngine;
import br.com.marisa.srv.filial.dao.FilialDAO;
import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.funcionario.dao.FuncionarioDAO;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.enumeration.StatCalcRealzEnum;
import br.com.marisa.srv.geral.excecoes.MensagemException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.indicador.dao.IndicadorDAO;
import br.com.marisa.srv.indicador.dao.RealizFuncIndicadorDAO;
import br.com.marisa.srv.indicador.vo.DadosIndicadorVO;
import br.com.marisa.srv.indicador.vo.DetalheCalculoVO;
import br.com.marisa.srv.indicador.vo.IndicadorFuncionarioRealizadoVO;
import br.com.marisa.srv.indicador.vo.RelatorioBonusVO;
import br.com.marisa.srv.parametros.dao.ParametroDAO;

/**
 * 
 * @author levy.villar
 * 
 */
public class RealizFuncIndicadorBusiness {

    private static final Logger log = Logger.getLogger(RealizFuncIndicadorBusiness.class);    

	private static RealizFuncIndicadorBusiness instance = new RealizFuncIndicadorBusiness();

	/**
	 * 
	 * @return
	 */
	public static final RealizFuncIndicadorBusiness getInstance() {
		return instance;
	}

	/**
	 * 
	 */
	private RealizFuncIndicadorBusiness() {
	}

	/**
	 * 
	 * @param numAno
	 * @param numMes
	 * @param codUsuario
	 * @return
	 * @throws SRVException
	 */
	public boolean geraIndicadorCorporativoEmAberto(int numAno, int numMes, int codUsuario) throws SRVException {

		boolean isSucesso = false;

		//Inicializa DAOs para uso na transacao
		ParametroDAO parametroDAO = new ParametroDAO();
		IndicadorDAO indicadorDAO = new IndicadorDAO();
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
		RealizFuncIndicadorDAO realizFuncIndicadorDAO = new RealizFuncIndicadorDAO();

		try {

			//Controle de conexao e transacao
			parametroDAO.beginTrans();
			indicadorDAO.setConn(parametroDAO.getConn());
			funcionarioDAO.setConn(parametroDAO.getConn());
			realizFuncIndicadorDAO.setConn(parametroDAO.getConn());
			
			//Obtem parametros de indicadores configurado para o ano selecionado
			ConfiguracaoBonusVO configuracaoBonusVO = BonusAnualEngine.getInstance().obtemConfiguracaoBonus(numAno);

			//Obtem indicadores corporativos com base no presidente
			List<DadosIndicadorVO> listaIndicCorp = indicadorDAO.obtemListaIndicadorCorporativo(Constantes.COD_GRUPO_INDIC_CORPORATIVO, configuracaoBonusVO.getCodIndicIni(), configuracaoBonusVO.getCodIndicFim());
			if (listaIndicCorp.isEmpty()) {
				throw new MensagemException("Nenhum indicador corporativo foi encontrado!");
			}

			//Obtem lista de peso dos indicadores corporativos do presidente
			List<IndicadorFuncionarioRealizadoVO> listaPesoIndicCorp = new ArrayList<IndicadorFuncionarioRealizadoVO>();
			for (Iterator<DadosIndicadorVO> itCodIndicCorp = listaIndicCorp.iterator(); itCodIndicCorp.hasNext();) {
				DadosIndicadorVO indicTempVO = itCodIndicCorp.next();
				IndicadorFuncionarioRealizadoVO realFuncIndicTempVO = realizFuncIndicadorDAO.obtemPesoMeta(numAno, numMes, indicTempVO.getIdIndicador(), configuracaoBonusVO.getIdFuncionarioCorporativo());
				if (realFuncIndicTempVO == null) {
					throw new MensagemException("Indicadores do presidente não existe!");
				}
				realFuncIndicTempVO.setIdEscala(indicTempVO.getIdEscala());
				listaPesoIndicCorp.add(realFuncIndicTempVO);
			}

			//Obtem lista dos funcionarios para gravacao dos indicadores corporativos
			List<FuncionarioVO> listaFuncionario = realizFuncIndicadorDAO.obtemListaFuncionarioBonusEmAberto(numAno, numMes, configuracaoBonusVO.getIdFuncionarioCorporativo(), configuracaoBonusVO.getCodIndicIni(), configuracaoBonusVO.getCodIndicFim());

			//Percorre lista localizando dados do funcionario
			for (Iterator<FuncionarioVO> itFuncionario = listaFuncionario.iterator(); itFuncionario.hasNext();) {
				FuncionarioVO funcProcessamento = itFuncionario.next();
				FuncionarioVO funcionarioVO = funcionarioDAO.obtemFuncionario(funcProcessamento.getIdFuncionario());

				if (funcionarioVO.getIdFuncionario().longValue() != configuracaoBonusVO.getIdFuncionarioCorporativo().longValue()) {

					//Carrega total de peso dos indicadores individuais do funcionario, calculando o total corporativo
					double totalPesoIndiv = realizFuncIndicadorDAO.obtemSomaPesoIndicadorFuncionarioBonus(numAno, numMes, funcionarioVO.getIdFuncionario(),
																											funcProcessamento.getIdEmpresa(),
																											funcProcessamento.getIdFilial(),
																											Constantes.COD_GRUPO_INDIC_INDIVIDUAL, 
																											Constantes.ZERO.intValue());
					double totalPesoCorp = Constantes.CEM.doubleValue() - totalPesoIndiv;

					//Configura valores e grava realizado
					for (Iterator<IndicadorFuncionarioRealizadoVO> itCorp = listaPesoIndicCorp.iterator(); itCorp.hasNext();) {
						IndicadorFuncionarioRealizadoVO indicCorpVO = itCorp.next();
						IndicadorFuncionarioRealizadoVO realizFuncIndicVO = new IndicadorFuncionarioRealizadoVO();
						realizFuncIndicVO.setAno(numAno);
						realizFuncIndicVO.setMes(numMes);
						realizFuncIndicVO.setIdFuncionario(funcionarioVO.getIdFuncionario());
						realizFuncIndicVO.setIdEmpresa(funcProcessamento.getIdEmpresa());//Empresa do processamento
						realizFuncIndicVO.setIdFilial(funcProcessamento.getIdFilial());//Filial do processamento
						realizFuncIndicVO.setIdIndicador(indicCorpVO.getIdIndicador());
						realizFuncIndicVO.setIdEscala(indicCorpVO.getIdEscala());
						realizFuncIndicVO.setPeso(indicCorpVO.getPeso() * (totalPesoCorp/Constantes.CEM.doubleValue()));
						realizFuncIndicVO.setUnidadePeso(indicCorpVO.getUnidadePeso());
						realizFuncIndicVO.setMeta(indicCorpVO.getMeta());
						realizFuncIndicVO.setUnidadeMeta(indicCorpVO.getUnidadeMeta());
						realizFuncIndicVO.setIdUsuarioAlteracao(codUsuario);

			        	//Inclui realizado indicador funcionario
			        	realizFuncIndicadorDAO.incluiRealizadoFuncIndicador(realizFuncIndicVO);
					}

				}

				//Grava agrupamento corporativo com totalizador dos pesos
				IndicadorFuncionarioRealizadoVO realizFuncIndicVO = new IndicadorFuncionarioRealizadoVO();
				realizFuncIndicVO.setAno(numAno);
				realizFuncIndicVO.setMes(numMes);
				realizFuncIndicVO.setIdFuncionario(funcionarioVO.getIdFuncionario());
				realizFuncIndicVO.setIdEmpresa(funcProcessamento.getIdEmpresa());
				realizFuncIndicVO.setIdFilial(funcProcessamento.getIdFilial());
				realizFuncIndicVO.setIdIndicador(Constantes.COD_INDIC_AGRUPAMENTO_CORPORATIVO);
				realizFuncIndicVO.setIdStatusCalcRelz(StatCalcRealzEnum.INICIADO.getCodigo());
				realizFuncIndicVO.setIdEscala(null);
				realizFuncIndicVO.setPeso(realizFuncIndicadorDAO.obtemSomaPesoIndicadorFuncionarioBonus(numAno, numMes, funcionarioVO.getIdFuncionario(), 
																										funcProcessamento.getIdEmpresa(),
																										funcProcessamento.getIdFilial(),
																										Constantes.COD_GRUPO_INDIC_INDIVIDUAL, 
																										Constantes.COD_GRUPO_INDIC_CORPORATIVO));
				realizFuncIndicVO.setUnidadePeso(Constantes.UNIDADE_PERCENTUAL);
				realizFuncIndicVO.setIdUsuarioAlteracao(codUsuario);

				//Inclui realizado indicador funcionario
				realizFuncIndicadorDAO.incluiRealizadoFuncIndicador(realizFuncIndicVO);

			}

			//Efetiva toda a transacao
			parametroDAO.commitTrans();
			isSucesso = true;

		} catch (MensagemException msgEx) {
			parametroDAO.rollbackTrans();
			throw msgEx;
		} catch (Exception ex) {
			parametroDAO.rollbackTrans();
			throw new SRVException(log, "Erro ao processar indicadores do bonus anual em aberto", ex);
		} finally {
			parametroDAO.closeConnection();			
		}

		return isSucesso;
	}

	/**
	 * 
	 * @param idFuncionario
	 * @param mes
	 * @param ano
	 * @return
	 * @throws SRVException
	 */
	public List<FilialVO> obtemListaFilialRealizadoFuncIndicador(Long idFuncionario, Integer mes, Integer ano, Integer codGrpRemVar) throws SRVException {

		List<FilialVO> listaFilial = new ArrayList<FilialVO>();
		RealizFuncIndicadorDAO realizFuncIndicadorDAO = new RealizFuncIndicadorDAO();
		FilialDAO filialDAO = new FilialDAO();

		try {
			filialDAO.setConn(realizFuncIndicadorDAO.getConn());
			List<Integer> listaIdFilial = realizFuncIndicadorDAO.obtemListaIdFilialRealizadoFuncIndicador(idFuncionario, ano, mes, codGrpRemVar);
			for (Integer idFilial : listaIdFilial) {
				listaFilial.add(filialDAO.obtemFilial(Constantes.CODIGO_EMPRESA, idFilial));
			}
		} catch (Exception ex) {
			throw new SRVException(log, "Erro ao obter a lista de filial do funcionario bonus anual", ex);
		} finally {
			realizFuncIndicadorDAO.closeConnection();
		}

		return listaFilial;
	}

	/**
	 * 
	 * @param idFuncionario
	 * @param ano
	 * @param mes
	 * @param idEmpresa
	 * @param idFilial
	 * @return
	 * @throws SRVException
	 */
	public RelatorioBonusVO obtemResultadoRealizadoFuncIndicador(Long idFuncionario, Integer ano, Integer mes, Integer idEmpresa, Integer idFilial) throws SRVException {

		RelatorioBonusVO relatorioBonusVO = null;
		RealizFuncIndicadorDAO realizFuncIndicadorDAO = new RealizFuncIndicadorDAO();

		try {

			List<IndicadorFuncionarioRealizadoVO> indicadores =  new ArrayList<IndicadorFuncionarioRealizadoVO>();
			IndicadorFuncionarioRealizadoVO totalCorporativoRealizadoVO = new IndicadorFuncionarioRealizadoVO();
			IndicadorFuncionarioRealizadoVO totalIndividualRealizadoVO = new IndicadorFuncionarioRealizadoVO();

			List<IndicadorFuncionarioRealizadoVO> indicadoresCorp = new ArrayList<IndicadorFuncionarioRealizadoVO>();
			List<IndicadorFuncionarioRealizadoVO> indicadoresCorpTemp = realizFuncIndicadorDAO.obtemResultadoRealizadoFuncIndicador(idFuncionario, ano, mes, idEmpresa, idFilial, 
																																		Constantes.COD_GRUPO_INDIC_CORPORATIVO, null);
			for (Iterator<IndicadorFuncionarioRealizadoVO> it = indicadoresCorpTemp.iterator(); it.hasNext();) {
				IndicadorFuncionarioRealizadoVO itemCorp = it.next();
				if (itemCorp.getIdIndicador().intValue() != Constantes.COD_INDIC_AGRUPAMENTO_CORPORATIVO) {
					indicadoresCorp.add(itemCorp);
				}
			}
			indicadores.addAll(indicadoresCorpTemp);

			List<IndicadorFuncionarioRealizadoVO> indicadoresIndivComFilhos =  new ArrayList<IndicadorFuncionarioRealizadoVO>();

			List<IndicadorFuncionarioRealizadoVO> indicadoresIndiv =  realizFuncIndicadorDAO.obtemResultadoRealizadoFuncIndicador(idFuncionario, ano, mes, idEmpresa, idFilial, 
																																	Constantes.COD_GRUPO_INDIC_INDIVIDUAL, null);
			for (Iterator<IndicadorFuncionarioRealizadoVO> itIndiv = indicadoresIndiv.iterator(); itIndiv.hasNext();) {
				IndicadorFuncionarioRealizadoVO itemIndiv = itIndiv.next();
				indicadoresIndivComFilhos.add(itemIndiv);
				indicadoresIndivComFilhos.addAll(realizFuncIndicadorDAO.obtemResultadoRealizadoFuncIndicador(idFuncionario, ano, mes, idEmpresa, idFilial, 
																												Constantes.COD_GRUPO_INDIC_INDIVIDUAL, itemIndiv.getIdIndicador()));
			}
			indicadores.addAll(indicadoresIndivComFilhos);

			List<IndicadorFuncionarioRealizadoVO> novaListaIndicadores = null;
			IndicadorFuncionarioRealizadoVO totalRealizadoVO = null;
			
			boolean encontrouAgrupamentoCorporativo = false;
			boolean encontrouAgrupamentoAdministrativo = false;
			
			if (ObjectHelper.isNotEmpty(indicadores)) {
				
				int idGrupo = -1;
				novaListaIndicadores = new ArrayList<IndicadorFuncionarioRealizadoVO>();
				
				double totalPeso = 0;
				double totalPesoXResultado = 0;
				double totalPesoPorGrupo = 0;
				
				Iterator<IndicadorFuncionarioRealizadoVO> itIndicador = indicadores.iterator();
				while (itIndicador.hasNext()) {
					IndicadorFuncionarioRealizadoVO indicadorFuncionarioRealizadoVO = itIndicador.next();
					
					if (indicadorFuncionarioRealizadoVO.getIdIndicador().intValue() == Constantes.COD_INDIC_AGRUPAMENTO_CORPORATIVO) {
						encontrouAgrupamentoCorporativo = true;
						continue;
					}
					if (indicadorFuncionarioRealizadoVO.getIdIndicador().intValue() == Constantes.COD_INDIC_AGRUPAMENTO_ADMINISTRATIVO) {
						encontrouAgrupamentoAdministrativo = true;
						continue;
					}
					
					if (idGrupo != indicadorFuncionarioRealizadoVO.getIdGrupoIndicador().intValue()) {
						
						if (idGrupo > 0) {
							IndicadorFuncionarioRealizadoVO indicadorFuncionarioGrupoVO = new IndicadorFuncionarioRealizadoVO();
							indicadorFuncionarioGrupoVO.setDescricaoIndicador("Subtotal");
							indicadorFuncionarioGrupoVO.setUnidadePeso(new Integer(Constantes.UNIDADE_PERCENTUAL));
							indicadorFuncionarioGrupoVO.setPeso(new Double(totalPesoPorGrupo));
							novaListaIndicadores.add(indicadorFuncionarioGrupoVO);
							totalCorporativoRealizadoVO = indicadorFuncionarioGrupoVO;
						}

						IndicadorFuncionarioRealizadoVO indicadorFuncionarioGrupoVO = new IndicadorFuncionarioRealizadoVO();
						indicadorFuncionarioGrupoVO.setDescricaoIndicador(indicadorFuncionarioRealizadoVO.getDescricaoGrupoIndicador());
						novaListaIndicadores.add(indicadorFuncionarioGrupoVO);
						
						idGrupo = indicadorFuncionarioRealizadoVO.getIdGrupoIndicador().intValue();
						totalPesoPorGrupo = 0;
					}
					
					if (indicadorFuncionarioRealizadoVO.getPeso() != null) {
						totalPeso += indicadorFuncionarioRealizadoVO.getPeso().doubleValue();
						totalPesoPorGrupo += indicadorFuncionarioRealizadoVO.getPeso().doubleValue();
					}
					if (indicadorFuncionarioRealizadoVO.getRealizadoPonderacao() != null) {
						totalPesoXResultado += indicadorFuncionarioRealizadoVO.getRealizadoPonderacao().doubleValue();
					}
					novaListaIndicadores.add(indicadorFuncionarioRealizadoVO);
				}
				if (idGrupo > 0) {
					IndicadorFuncionarioRealizadoVO indicadorFuncionarioGrupoVO = new IndicadorFuncionarioRealizadoVO();
					indicadorFuncionarioGrupoVO.setDescricaoIndicador("Subtotal");
					indicadorFuncionarioGrupoVO.setUnidadePeso(new Integer(Constantes.UNIDADE_PERCENTUAL));
					indicadorFuncionarioGrupoVO.setPeso(new Double(totalPesoPorGrupo));
					novaListaIndicadores.add(indicadorFuncionarioGrupoVO);
					totalIndividualRealizadoVO = indicadorFuncionarioGrupoVO;
				}
				
				if (encontrouAgrupamentoCorporativo) {
					totalRealizadoVO = new IndicadorFuncionarioRealizadoVO();
					totalRealizadoVO.setDescricaoIndicador("Total");
					totalRealizadoVO.setUnidadePeso(new Integer(Constantes.UNIDADE_PERCENTUAL));
					totalRealizadoVO.setPeso(new Double(totalPeso));
					totalRealizadoVO.setUnidadeRealizadoPonderacao(new Integer(Constantes.UNIDADE_PERCENTUAL));
					totalRealizadoVO.setRealizadoPonderacao(new Double(totalPesoXResultado));
					totalRealizadoVO.setIdFuncionario(idFuncionario);
					totalRealizadoVO.setIdIndicador(new Integer(Constantes.COD_INDIC_AGRUPAMENTO_CORPORATIVO));
					totalRealizadoVO.setAno(ano);
					totalRealizadoVO.setMes(mes);
				}
				if (encontrouAgrupamentoAdministrativo) {
					totalRealizadoVO = new IndicadorFuncionarioRealizadoVO();
					totalRealizadoVO.setDescricaoIndicador("Total");
					totalRealizadoVO.setUnidadePeso(new Integer(Constantes.UNIDADE_PERCENTUAL));
					totalRealizadoVO.setPeso(new Double(totalPeso));
					totalRealizadoVO.setUnidadeRealizadoPonderacao(new Integer(Constantes.UNIDADE_PERCENTUAL));
					totalRealizadoVO.setRealizadoPonderacao(new Double(totalPesoXResultado));
					totalRealizadoVO.setIdFuncionario(idFuncionario);
					totalRealizadoVO.setIdIndicador(new Integer(Constantes.COD_INDIC_AGRUPAMENTO_ADMINISTRATIVO));
					totalRealizadoVO.setAno(ano);
					totalRealizadoVO.setMes(mes);
				}
			}
			
			relatorioBonusVO = new RelatorioBonusVO();
			relatorioBonusVO.setIndicadoresRealizados(novaListaIndicadores);
			relatorioBonusVO.setIndicadoresCorporativos(indicadoresCorp);
			relatorioBonusVO.setIndicadoresIndividuais(indicadoresIndivComFilhos);
			relatorioBonusVO.setTotalRealizadoVO(totalRealizadoVO);
			relatorioBonusVO.setTotalCorporativoRealizadoVO(totalCorporativoRealizadoVO);
			relatorioBonusVO.setTotalIndividualRealizadoVO(totalIndividualRealizadoVO);
			
		} finally {
			realizFuncIndicadorDAO.closeConnection();
		}

		return relatorioBonusVO;
	}

	/**
	 * 
	 * @param codFunc
	 * @param ano
	 * @param mes
	 * @param idEmpresa
	 * @param idFilial
	 * @return
	 * @throws SRVException
	 */
	public DetalheCalculoVO obterStatusCalculoRealizado(Long codFunc, Integer ano, Integer mes, Integer idEmpresa, Integer idFilial) throws SRVException {

		DetalheCalculoVO detalheCalculoVO = new DetalheCalculoVO();
		RealizFuncIndicadorDAO dao = new RealizFuncIndicadorDAO();

		try {
			detalheCalculoVO = dao.obterStatusCalculoRealizadoFuncIndicador(codFunc, mes, ano, idEmpresa, idFilial);
		} catch (Exception e) {
			throw new SRVException(log, "Nao foi possivel obter status de calculo realizado indicador funcionario", e);
		} finally {
			dao.closeConnection();
		}

		return detalheCalculoVO;

	}

	/**
	 * 
	 * @param indicadorFuncionarioBonusVO
	 * @throws SRVException
	 */
	public void incluiRealizadoFuncIndicador(IndicadorFuncionarioRealizadoVO indicadorFuncionarioBonusVO) throws SRVException {

		RealizFuncIndicadorDAO realizFuncIndicadorDAO = new RealizFuncIndicadorDAO();
		IndicadorDAO indicadorDAO = new IndicadorDAO();

		try {
			realizFuncIndicadorDAO.beginTrans();
			indicadorDAO.setConn(realizFuncIndicadorDAO.getConn());

			DadosIndicadorVO indicadorVO = indicadorDAO.obtemIndicador(indicadorFuncionarioBonusVO.getIdIndicador());
			indicadorFuncionarioBonusVO.setIdEscala(indicadorVO.getIdEscala());
			realizFuncIndicadorDAO.incluiRealizadoFuncIndicador(indicadorFuncionarioBonusVO);

			realizFuncIndicadorDAO.commitTrans();

		} catch (Exception e) {
			realizFuncIndicadorDAO.rollbackTrans();
			throw new SRVException(log, "Ocorreu erro na inclusao de realizado de indicador de bonus", e);
		} finally {
			realizFuncIndicadorDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param indicadorFuncionarioBonusVO
	 * @throws SRVException
	 */
	public void alteraRealizadoFuncIndicador(IndicadorFuncionarioRealizadoVO indicadorFuncionarioBonusVO) throws SRVException {
		RealizFuncIndicadorDAO realizFuncIndicadorDAO = new RealizFuncIndicadorDAO();
		IndicadorDAO indicadorDAO = new IndicadorDAO();

		try {
			realizFuncIndicadorDAO.beginTrans();
			indicadorDAO.setConn(realizFuncIndicadorDAO.getConn());

			DadosIndicadorVO indicadorVO = indicadorDAO.obtemIndicador(indicadorFuncionarioBonusVO.getIdIndicador());
			indicadorFuncionarioBonusVO.setIdEscala(indicadorVO.getIdEscala());
			realizFuncIndicadorDAO.alteraRealizadoFuncIndicador(indicadorFuncionarioBonusVO);

			realizFuncIndicadorDAO.commitTrans();

		} catch (Exception e) {
			realizFuncIndicadorDAO.rollbackTrans();
			throw new SRVException(log, "Ocorreu erro na alteracao de realizado de indicador de bonus", e);
		} finally {
			realizFuncIndicadorDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param indicadorFuncionarioBonusVO
	 * @param isProcessaCorportivo
	 * @throws SRVException
	 */
	public void excluiRealizadoFuncIndicador(IndicadorFuncionarioRealizadoVO indicadorFuncionarioBonusVO) throws SRVException {

		RealizFuncIndicadorDAO realizFuncIndicadorDAO = new RealizFuncIndicadorDAO();

		try {
			realizFuncIndicadorDAO.beginTrans();
			realizFuncIndicadorDAO.excluiRealizadoFuncIndicador(indicadorFuncionarioBonusVO.getIdFuncionario(), indicadorFuncionarioBonusVO.getIdIndicador(), indicadorFuncionarioBonusVO.getIdEmpresa(), indicadorFuncionarioBonusVO.getIdFilial(), indicadorFuncionarioBonusVO.getAno(), indicadorFuncionarioBonusVO.getMes());
			realizFuncIndicadorDAO.commitTrans();
		} catch (Exception e) {
			throw new SRVException(log, "Ocorreu erro na exclusao de realizado de indicador de bonus", e);
		} finally {
			realizFuncIndicadorDAO.closeConnection();
		}
	}

	/**
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
	public IndicadorFuncionarioRealizadoVO obtemRealizadoFuncIndicador(long idFuncionario, int idIndicador, int idEmpresa, int idFilial, int ano, int mes) throws SRVException {

		IndicadorFuncionarioRealizadoVO vo = null;
		RealizFuncIndicadorDAO realizFuncIndicadorDAO = new RealizFuncIndicadorDAO();

		try {
			vo = realizFuncIndicadorDAO.obtemRealizadoFuncIndicador(idFuncionario, idIndicador, idEmpresa, idFilial, ano, mes);
		} catch (Exception e) {
			realizFuncIndicadorDAO.rollbackTrans();
			throw new SRVException(log, "Ocorreu erro na inclusão de realizado de indicador de bonus", e);
		} finally {
			realizFuncIndicadorDAO.closeConnection();
		}
		return vo;
	}

	public List<IndicadorFuncionarioRealizadoVO> obtemListaRealizadoFuncIndicador(Long idFuncionario, Integer idFilial, Integer idEmpresa, Integer ano, Integer mes, String descrTipoRemVar) throws SRVException {

		RealizFuncIndicadorDAO realizFuncIndicadorDAO = new RealizFuncIndicadorDAO();
		List<IndicadorFuncionarioRealizadoVO> lista = null;

		try {
			lista = realizFuncIndicadorDAO.obtemListaRealizadoFuncIndicador(idFuncionario, idFilial, idEmpresa, ano, mes, descrTipoRemVar);
		} catch (Exception e) {
			throw new SRVException(log, "Ocorreu erro na pesquisa realizado funcionario indicador mes/ano", e);
		} finally {
			realizFuncIndicadorDAO.closeConnection();
		}
		return lista;
	}

}