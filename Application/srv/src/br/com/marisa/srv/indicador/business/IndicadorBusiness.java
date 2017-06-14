package br.com.marisa.srv.indicador.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.bonus.configuracao.vo.ConfiguracaoBonusVO;
import br.com.marisa.srv.bonus.engine.BonusAnualEngine;
import br.com.marisa.srv.funcionario.business.FuncionarioBusiness;
import br.com.marisa.srv.funcionario.business.SalarioBaseBusiness;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.indicador.dao.IndicadorDAO;
import br.com.marisa.srv.indicador.dao.IndicadorRelatoriosDAO;
import br.com.marisa.srv.indicador.vo.DadosIndicadorVO;
import br.com.marisa.srv.indicador.vo.DetalheCalculoLojaVO;
import br.com.marisa.srv.indicador.vo.DetalheCalculoVO;
import br.com.marisa.srv.indicador.vo.IndicadorFuncionarioRealizadoVO;
import br.com.marisa.srv.indicador.vo.IndicadorFuncionarioVO;

/**
 * Classe para conter os métodos de negócio do módulo de Indicadores 
 * 
 * @author Walter Fontes
 */
public class IndicadorBusiness {
    
	
    //Log4J
    private static final Logger log = Logger.getLogger(IndicadorBusiness.class);    

    //Instancia do Singleton
    private static IndicadorBusiness instance = new IndicadorBusiness();
    
    
    /**
     * Obtem uma instancia do objeto IndicadorBusiness
     * @return O objeto IndicadorBusiness
     */
    public static final IndicadorBusiness getInstance() {
        return instance;
    }
    

    /**
     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o método getInstance();
     */
    private IndicadorBusiness() {
    }


	/**
	 * Obtém os dados para o relatório por loja
	 * 
	 * @param idLider
	 * @param idLoja
	 * @param codEmpresa
	 * @param codIndicador
	 * @param periodo
	 * @param grupo
	 * @return
	 * @throws SRVException
	 */
	public List obtemRelatorioLoja(Integer idLider, Integer idLoja, Integer codEmpresa, Integer codIndicador, 
									Integer ano, Integer mes, Integer grupo, Long matricula) throws SRVException {

		IndicadorRelatoriosDAO indicadorDAO = new IndicadorRelatoriosDAO();
		try {
			
			List idsFiliais = null;
			if (matricula != null) {
				idsFiliais = SalarioBaseBusiness.getInstance().obtemIdsFiliais(matricula);
				if (ObjectHelper.isEmpty(idsFiliais)) {
					FuncionarioVO funcionarioVO = FuncionarioBusiness.getInstance().obtemFuncionario(matricula);
					if (funcionarioVO != null) {
						idsFiliais = new ArrayList();
						idsFiliais.add(funcionarioVO.getIdFilial());
					}
				}
			}
			
			return indicadorDAO.obtemRelatorioLoja(idLider, idLoja, ano, mes, codEmpresa, codIndicador, grupo, idsFiliais);

		} finally {
			indicadorDAO.closeConnection();
		}
	}


	/**
	 * Obtém dados para o relatório operacional
	 * 
	 * @param idLider
	 * @param idLoja
	 * @param idEmpresa
	 * @param grupoRemuneracao
	 * @param codIndicador
	 * @param periodo
	 * @param grupos
	 * @param matricula
	 * @return
	 * @throws SRVException
	 */
	public List obtemRelatorioOperacional(Integer idLider, Integer idLoja, Integer idEmpresa, String grupoRemuneracao,
										  Integer codIndicador, Integer ano, Integer mes, Integer grupos, Long matricula) throws SRVException {
		
		IndicadorRelatoriosDAO indicadorDAO = new IndicadorRelatoriosDAO();
		try {

			List idsFiliais = null;
			if (matricula != null) {
				idsFiliais = SalarioBaseBusiness.getInstance().obtemIdsFiliais(matricula);
				if (ObjectHelper.isEmpty(idsFiliais)) {
					FuncionarioVO funcionarioVO = FuncionarioBusiness.getInstance().obtemFuncionario(matricula);
					if (funcionarioVO != null) {
						idsFiliais = new ArrayList();
						idsFiliais.add(funcionarioVO.getIdFilial());
					}
				}
			}			
			
			return indicadorDAO.obtemRelatorioOperacional(idLider, idLoja, ano, mes, idEmpresa, grupoRemuneracao, codIndicador, grupos, idsFiliais);
			
		} finally {
			indicadorDAO.closeConnection();
		}
	}
	
	
	/**
	 * Obtém dados para o relatório operacional por funcionário 
	 * @param idFuncionario
	 * @param periodo
	 * @return
	 * @throws SRVException
	 */
	public List obtemRelatorioOperacionalPorFuncionario(Integer idFuncionario, Integer mes, Integer ano) throws SRVException {
		
		IndicadorRelatoriosDAO indicadorDAO = new IndicadorRelatoriosDAO();
		try {

			List indicadores =  indicadorDAO.obtemRelatorioOperacionalPorFuncionario(idFuncionario, ano, mes);
			
			if (ObjectHelper.isNotEmpty(indicadores)) {
				DadosIndicadorVO dadosIndicadorAgrupamentoVO = obtemIndicador(Constantes.INDICADOR_AGRUPAMENTO_LOJA_LIDER);

				if (ObjectHelper.isNotEmpty(dadosIndicadorAgrupamentoVO)) {

					Iterator itIndicadores = indicadores.iterator();
					while (itIndicadores.hasNext()) {
						
						IndicadorFuncionarioVO indicadorFuncionarioVO = (IndicadorFuncionarioVO)itIndicadores.next();
						if (indicadorFuncionarioVO.getIdIndicador().intValue() == dadosIndicadorAgrupamentoVO.getIdIndicador().intValue()) {
							itIndicadores.remove();
						}
					}
				}
			}
			

			return indicadores;
		}finally {
			indicadorDAO.closeConnection();
		}
	}	

	/**
	 * Obtém lista de indicadores
	 * 
	 * @param codGrupo
	 * @return
	 * @throws SRVException
	 */
	public List obtemListaIndicadores(Integer codGrupo) throws SRVException {
		IndicadorDAO indicadorDAO = new IndicadorDAO();
		try {
			return indicadorDAO.obtemListaIndicadores(codGrupo);
		} finally {
			indicadorDAO.closeConnection();
		}
	}
	
	
	/**
	 * Obtém lista de indicadores
	 * utilizar o metodo que controla a conexao
	 * @param idIndicador
	 * @param descricaoIndicador
	 * @return
	 * @throws SRVException
	 */
	@Deprecated
	public List<DadosIndicadorVO> obtemListaIndicadores(DadosIndicadorVO pesquisaVO, List<Integer> tiposGrupo, boolean isPesquisaSubIndicador) throws SRVException {
		return obtemListaIndicadores(pesquisaVO, tiposGrupo, isPesquisaSubIndicador, Boolean.FALSE);
		
	}
	public List<DadosIndicadorVO> obtemListaIndicadores(DadosIndicadorVO pesquisaVO, List<Integer> tiposGrupo, boolean isPesquisaSubIndicador, Boolean fechaConexao) throws SRVException {		
		IndicadorDAO indicadorDAO = new IndicadorDAO();
		try {
			return indicadorDAO.obtemListaIndicadores(pesquisaVO, tiposGrupo, isPesquisaSubIndicador,fechaConexao);
		} finally {
			indicadorDAO.closeConnection();
		}
	}
	
	/**
	 * Obtém lista de indicadores
	 * 
	 * @param idTipoGrupo
	 * @return
	 * @throws SRVException
	 */
	public List<DadosIndicadorVO> obtemListaIndicadoresPorTipo(List idTipoGrupo, boolean isPesquisaSubIndicador) throws SRVException {
		IndicadorDAO indicadorDAO = new IndicadorDAO();
		try {
			return indicadorDAO.obtemListaIndicadores(null, idTipoGrupo, isPesquisaSubIndicador);
		} finally {
			indicadorDAO.closeConnection();
		}
	}	
	
	
	/**
	 * Obtém lista de indicadores
	 * 
	 * @param idIndicador
	 * @param descricaoIndicador
	 * @param idTipoGrupo
	 * @return
	 * @throws SRVException
	 */
	public List<DadosIndicadorVO> obtemListaIndicadoresPorTipo(DadosIndicadorVO pesquisaVO, List tiposGrupo, boolean isPesquisaSubIndicador) throws SRVException {
		IndicadorDAO indicadorDAO = new IndicadorDAO();
		try {
			return indicadorDAO.obtemListaIndicadores(pesquisaVO, tiposGrupo, isPesquisaSubIndicador);
		} finally {
			indicadorDAO.closeConnection();
		}
	}		

	
	/**
	 * Obtém detalhe do cálculo do indicador para um funcionário
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
		IndicadorRelatoriosDAO indicadorDAO = new IndicadorRelatoriosDAO();
		try {
			return indicadorDAO.obtemDetalheCalculo(idFuncionario, idIndicador, idEmpresa, idFilial, ano, mes);
		} finally {
			indicadorDAO.closeConnection();
		}
	}
	
	
	/**
	 * Obtém detalhe do cálculo do indicador para uma loja
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
		IndicadorRelatoriosDAO indicadorDAO = new IndicadorRelatoriosDAO();
		try {
			return indicadorDAO.obtemDetalheCalculoLoja(idIndicador, idEmpresa, idFilial, ano, mes);
		} finally {
			indicadorDAO.closeConnection();
		}
	}	
	
	
	/**
	 * Obtém dados de um indicador
	 * 
	 * @param idIndicador
	 * @return
	 * @throws SRVException
	 */
	public DadosIndicadorVO obtemIndicador(int idIndicador) throws SRVException {
		IndicadorDAO indicadorDAO = new IndicadorDAO();
		try {
			return indicadorDAO.obtemIndicador(idIndicador);
		} finally {
			indicadorDAO.closeConnection();
		}
	}
	

	
	/**
	 * Obtém dados de um indicador
	 * 
	 * @param idSistemico
	 * @return
	 * @throws SRVException
	 */
	public DadosIndicadorVO obtemIndicador(String idSistemico) throws SRVException {
		IndicadorDAO indicadorDAO = new IndicadorDAO();
		try {
			return indicadorDAO.obtemIndicador(idSistemico);
		} finally {
			indicadorDAO.closeConnection();
		}
	}
	
	
    /**
     * Realiza alteração do indicador
     * 
     * @param dadosIndicadorVO
     * @return
     */
    public void alteraIndicador(DadosIndicadorVO dadosIndicadorVO) throws SRVException {
    	IndicadorDAO indicadorDAO = new IndicadorDAO();
        try {
        	indicadorDAO.beginTrans();
        	
        	//Grava historico da situacao anterior
        	DadosIndicadorVO indicadorAnteriorVO = indicadorDAO.obtemIndicador(dadosIndicadorVO.getIdIndicador().intValue());
        	if (indicadorAnteriorVO != null) {
        		indicadorDAO.incluiIndicadorHistorico(indicadorAnteriorVO);
        	}
        	
        	//Efetiva a alteração
        	indicadorDAO.alteraIndicador(dadosIndicadorVO);

        	indicadorDAO.commitTrans();
        	
        } catch (Exception e) {
        	indicadorDAO.rollbackTrans();
			throw new SRVException(log, "Ocorreu erro na alteracao do indicador", e);
        } finally {
        	indicadorDAO.closeConnection();
        }    		
    }
    
    
    /**
     * Realiza inclusao do indicador
     * 
     * @param dadosIndicadorVO
     * @param ponderacaoVO
     * @return
     */
    public void incluiIndicador(DadosIndicadorVO dadosIndicadorVO) throws SRVException {
    	IndicadorDAO indicadorDAO = new IndicadorDAO();
        try {
        	indicadorDAO.beginTrans();
           	indicadorDAO.incluiIndicador(dadosIndicadorVO);
        	indicadorDAO.commitTrans();
        	
        } catch (Exception e) {
        	indicadorDAO.rollbackTrans();
			throw new SRVException(log, "Ocorreu erro na inclusao do indicador", e);
        } finally {
        	indicadorDAO.closeConnection();
        }
    }   
    
    /**
     * 
     * @param dadosIndicadorVO
     * @throws SRVException
     */
    public void incluiIndicadorBonusImportacao(DadosIndicadorVO dadosIndicadorVO) throws SRVException {
    	IndicadorDAO indicadorDAO = new IndicadorDAO();
        try {
        	indicadorDAO.beginTrans();
           	indicadorDAO.incluiIndicadorBonusImportacao(dadosIndicadorVO);
        	indicadorDAO.commitTrans();
        	
        } catch (Exception e) {
        	indicadorDAO.rollbackTrans();
			throw new SRVException(log, "Ocorreu erro na inclusao do indicador do bonus anual", e);
        } finally {
        	indicadorDAO.closeConnection();
        }
    }   

    /**
     * Realiza exclusão de realizado do indicador
     * 
     * @param idFuncionario
     * @param idIndicador
     * @param idEmpresa
     * @param idFilial
     * @param ano
     * @param mes
     * @return
     */
    public void excluiRealizadoIndicadorBonus(int idFuncionario, int idIndicador, int idEmpresa, int idFilial, int ano, int mes) throws SRVException {
    	
    	IndicadorDAO indicadorDAO = new IndicadorDAO();
        try {
        	indicadorDAO.beginTrans();
        	
        	IndicadorFuncionarioRealizadoVO indicadorFuncionarioRealizadoVO = indicadorDAO.obtemIndicadorFuncionarioRealizado(idFuncionario, idIndicador, idEmpresa, idFilial, ano, mes);
        	if (indicadorFuncionarioRealizadoVO != null) {
        		indicadorDAO.incluiHistoricoRealizado(indicadorFuncionarioRealizadoVO);
        		indicadorDAO.excluiIndicadorFuncionarioRealizado(idFuncionario, idIndicador, idEmpresa, idFilial, ano, mes);
        		DadosIndicadorVO dadosIndicadorVO = new DadosIndicadorVO();
        		dadosIndicadorVO.setIdIndicadorPai(idIndicador);
        		List<DadosIndicadorVO> lista = indicadorDAO.obtemListaIndicadores(dadosIndicadorVO, null, true);
        		for (Iterator<DadosIndicadorVO> it = lista.iterator(); it.hasNext();) {
            		indicadorDAO.excluiIndicadorFuncionarioRealizado(idFuncionario, it.next().getIdIndicador(), idEmpresa, idFilial, ano, mes);
				}
        	}

        	indicadorDAO.commitTrans();
        	
        } catch (Exception e) {
        	indicadorDAO.rollbackTrans();
			throw new SRVException(log, "Ocorreu erro na exclusão de realizado de indicador de bonus", e);
        } finally {
        	indicadorDAO.closeConnection();
        }    		
    }  

    /**
     * Obtém realizado do indicador do funcionário
     * 
     * @param idFuncionario
     * @param idIndicador
     * @param idEmpresa
     * @param idFilial
     * @param ano
     * @param mes
     * @return
     */
    public IndicadorFuncionarioRealizadoVO obtemRealizadoIndicadorBonus(long idFuncionario, int idIndicador, int idEmpresa, int idFilial, int ano, int mes) throws SRVException {
    	
    	IndicadorDAO indicadorDAO = new IndicadorDAO();
        try {
        	return indicadorDAO.obtemIndicadorFuncionarioRealizado(idFuncionario, idIndicador, idEmpresa, idFilial, ano, mes);
        } finally {
        	indicadorDAO.closeConnection();
        }    		
    }

    public boolean isDoisIndicadoresRestantes(int codFunc, int mes, int ano) throws SRVException {
        IndicadorDAO indicadorDAO;
        boolean status;
        indicadorDAO = new IndicadorDAO();
        status = false;
        try {
            status = indicadorDAO.isDoisIndicadoresRestantes(codFunc, mes, ano);
        } catch(Exception e) {
            indicadorDAO.rollbackTrans();
            throw new SRVException(log, "Nao foi possível obter quantidade de registros da tabela.", e);
        } finally {
            indicadorDAO.closeConnection();
        }
        return status;
    }

	public List obtemRelatorioExcelBonusOperacionalPorFuncionario(Long idFuncionario, String periodo) throws SRVException {
		IndicadorRelatoriosDAO indicadorDAO = new IndicadorRelatoriosDAO();
		List listaRelatorio = new ArrayList();
		try {
			Integer ano = new Integer(periodo.substring(0, 4));
			Integer mes = new Integer(periodo.substring(periodo.length() - 2, periodo.length()));
			listaRelatorio = indicadorDAO.obtemRelatorioExcelBonusOperacionalPorFuncionario(idFuncionario, ano, mes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listaRelatorio;
	}

	public void finalizaIndicadorCorporativo(IndicadorFuncionarioRealizadoVO indicadorFuncionarioBonusVO) throws SRVException {
		IndicadorDAO indicadorDAO = new IndicadorDAO();
		try {
			int ano = indicadorFuncionarioBonusVO.getAno().intValue();
			int mes = indicadorFuncionarioBonusVO.getMes().intValue();
			int idCargo = -1;
			int idFuncionario = indicadorFuncionarioBonusVO.getIdFuncionario().intValue();
			int idUsuario = indicadorFuncionarioBonusVO.getIdUsuarioAlteracao().intValue();
			
			indicadorDAO.finalizaIndicadorCorporativo(ano, mes, idCargo, idFuncionario, idUsuario);
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível finalizar indicador corporativo", e);
		} finally {
			indicadorDAO.closeConnection();
		}
	}

    public List obtemListaIndicadoresAjusteFuncionario(Long idFunc, Integer mes, Integer ano) throws SRVException {
		IndicadorDAO indicadorDAO = new IndicadorDAO();
		try {
			return indicadorDAO.obtemListaIndicadoresAjusteFuncionario(idFunc, mes, ano);
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível encontrar o indicador de ajuste por funcionario.", e);
		} finally {
			indicadorDAO.closeConnection();
		}
    }

	public List obtemListaIndicadoresAjuste(Integer codGrupo) throws SRVException {
		IndicadorDAO indicadorDAO = new IndicadorDAO();
		try {
			return indicadorDAO.obtemListaIndicadoresAjuste(codGrupo);
		} finally {
			indicadorDAO.closeConnection();
		}
	}

	public void incluiRealizadoAjuste(IndicadorFuncionarioRealizadoVO indicadorFuncionarioBonusVO) throws SRVException {
    	IndicadorDAO indicadorDAO = new IndicadorDAO();
        try {
        	indicadorDAO.beginTrans();
        	indicadorDAO.incluiRealizadoAjuste(indicadorFuncionarioBonusVO);
        	indicadorDAO.commitTrans();
        } catch (Exception e) {
        	indicadorDAO.rollbackTrans();
			throw new SRVException(log, "Ocorreu erro na inclusao do indicador de ajuste", e);
        } finally {
        	indicadorDAO.closeConnection();
        }    		
	}
	
	public void alteraRealizadoAjuste(IndicadorFuncionarioRealizadoVO indicadorFuncionarioBonusVO) throws SRVException {
    	IndicadorDAO indicadorDAO = new IndicadorDAO();
        try {
        	indicadorDAO.beginTrans();
        	indicadorDAO.alteraRealizadoAjuste(indicadorFuncionarioBonusVO);
        	indicadorDAO.commitTrans();
        } catch (Exception e) {
        	indicadorDAO.rollbackTrans();
			throw new SRVException(log, "Ocorreu erro na alteracao do indicador de ajuste", e);
        } finally {
        	indicadorDAO.closeConnection();
        }    		
	}

	public boolean existeRealizadoAjuste(IndicadorFuncionarioRealizadoVO indicadorFuncionarioBonusVO) throws SRVException {
    	IndicadorDAO indicadorDAO = new IndicadorDAO();
        try {
        	return indicadorDAO.existeRealizadoAjuste(indicadorFuncionarioBonusVO);
        } catch (Exception e) {
			throw new SRVException(log, "Ocorreu erro na inclusao do indicador de ajuste", e);
        } finally {
        	indicadorDAO.closeConnection();
        }    		
	}

	/**
	 * 
	 * @return
	 * @throws SRVException
	 */
	public List<String> obtemListaFonte() throws SRVException {
		IndicadorDAO indicadorDAO = new IndicadorDAO();
		try {
			return indicadorDAO.obtemListaFonte();
		} finally {
			indicadorDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @return
	 * @throws SRVException
	 */
	public List<String> obtemListaDiretoria() throws SRVException {
		IndicadorDAO indicadorDAO = new IndicadorDAO();
		try {
			return indicadorDAO.obtemListaDiretoria();
		} finally {
			indicadorDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param nome
	 * @return
	 * @throws SRVException
	 */
	public boolean existeIndicadorMesmoNome(String nome) throws SRVException {
		IndicadorDAO indicadorDAO = new IndicadorDAO();
		try {
			return indicadorDAO.existeIndicadorMesmoNome(nome);
		} finally {
			indicadorDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param idFuncionario
	 * @param numAno
	 * @param numMes
	 * @param idUsuario
	 * @throws SRVException
	 */
    public void atualizaAceiteBonusAnual(long idFuncionario, int numAno, int numMes, int idUsuario) throws SRVException {
    	
    	IndicadorDAO indicadorDAO = new IndicadorDAO();
        try {
        	indicadorDAO.beginTrans();
        	indicadorDAO.atualizaAceiteBonusAnual(idFuncionario, numAno, numMes, idUsuario);
        	indicadorDAO.commitTrans();
        } catch (Exception e) {
        	indicadorDAO.rollbackTrans();
			throw new SRVException(log, "Ocorreu erro no aceite do bonus anual", e);
        } finally {
        	indicadorDAO.closeConnection();
        }    		
    }  

    /**
     * 
     * @param pesquisaVO
     * @param tipoGrupo
     * @param numAno
     * @return
     * @throws SRVException
     */
	public List<DadosIndicadorVO> obtemListaIndicadoresAtivosPorPeriodo(DadosIndicadorVO pesquisaVO, int tipoGrupo, int numAno) throws SRVException {
		IndicadorDAO indicadorDAO = new IndicadorDAO();
		List<DadosIndicadorVO> lista = null;
		try {
			
			ConfiguracaoBonusVO configuracaoBonusVO = BonusAnualEngine.getInstance().obtemConfiguracaoBonus(numAno);
			int codIndicInicio = 0;
			int codIndicFim = 0;
			if (numAno != 0 && configuracaoBonusVO != null) {
				codIndicInicio = configuracaoBonusVO.getCodIndicIni();
				codIndicFim = configuracaoBonusVO.getCodIndicFim();
			}
			lista = indicadorDAO.obtemListaIndicadoresAtivosPorPeriodo(pesquisaVO, tipoGrupo, codIndicInicio, codIndicFim);
		} finally {
			indicadorDAO.closeConnection();
		}
		return lista;
	}

	/**
	 * 
	 * @param nomeIndicador
	 * @param codGrupoIndicador
	 * @param codIndicInicio
	 * @param codIndicFim
	 * @return
	 * @throws SRVException
	 */
	public DadosIndicadorVO obtemIndicadorPorNome(String nomeIndicador, int codGrupoIndicador, int codIndicInicio, int codIndicFim) throws SRVException {
		IndicadorDAO indicadorDAO = new IndicadorDAO();
		DadosIndicadorVO indicadorVO = null;
		try {
			indicadorVO = indicadorDAO.obtemIndicadorPorNome(nomeIndicador, codGrupoIndicador, codIndicInicio, codIndicFim);
		} finally {
			indicadorDAO.closeConnection();
		}
		return indicadorVO;
	}

	/**
	 * 
	 * @param codIndicIni
	 * @param codIndicFim
	 * @return
	 * @throws PersistenciaException
	 */
	public int obtemProximoCodIndicadorPorRange(int codIndicIni, int codIndicFim) throws SRVException {
		IndicadorDAO indicadorDAO = new IndicadorDAO();
		try {
			return indicadorDAO.obtemProximoCodIndicadorPorRange(codIndicIni, codIndicFim);
        } catch (Exception e) {
			throw new SRVException(log, "Ocorreu erro obter próximo código de indicador", e);
        } finally {
			indicadorDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @return
	 * @throws SRVException
	 */
	public Integer obtemMaiorCodIndicador() throws SRVException {
		IndicadorDAO indicadorDAO = new IndicadorDAO();
		try {
			return indicadorDAO.obtemMaiorCodIndicador();
        } catch (Exception e) {
			throw new SRVException(log, "Ocorreu erro obter o maior código de indicador: ", e);
        } finally {
			indicadorDAO.closeConnection();
		}
	}

    /**
     * 
     * @param codIndicInicio
     * @param codIndicFim
     * @return
     * @throws SRVException
     */
	public List<DadosIndicadorVO> obtemListaIndicadorCorporativo(int codIndicInicio, int codIndicFim) throws SRVException {
		IndicadorDAO indicadorDAO = new IndicadorDAO();
		try {
			return indicadorDAO.obtemListaIndicadorCorporativo(Constantes.COD_GRUPO_INDIC_CORPORATIVO, codIndicInicio, codIndicFim);
		} finally {
			indicadorDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param ano
	 * @param mes
	 * @param codigoPresidente
	 * @return
	 * @throws SRVException
	 */
	public List<Long> obtemListaFuncionarioBonusAnual(int ano, int mes, long codigoPresidente) throws SRVException {
		IndicadorDAO indicadorDAO = new IndicadorDAO();
		try {
			return indicadorDAO.obtemListaFuncionarioBonusAnual(ano, mes, codigoPresidente, Constantes.COD_GRUPO_INDIC_INDIVIDUAL);
		} finally {
			indicadorDAO.closeConnection();
		}
	}

/**********************************************************************************************************************************************/

	/**
	 * 
	 * @return
	 * @throws SRVException
	 */
	public List<Integer> obtemListaFuncionarioRelatorio() throws SRVException {
		IndicadorDAO indicadorDAO = new IndicadorDAO();
		try {
			return indicadorDAO.obtemListaFuncionarioRelatorio();
		} finally {
			indicadorDAO.closeConnection();
		}
	}

}