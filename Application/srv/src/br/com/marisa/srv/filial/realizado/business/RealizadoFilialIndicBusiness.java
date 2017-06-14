package br.com.marisa.srv.filial.realizado.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import br.com.marisa.srv.filial.business.FilialBusiness;
import br.com.marisa.srv.filial.realizado.dao.RealizadoFilialIndicDAO;
import br.com.marisa.srv.filial.realizado.vo.RealizadoFilialIndicVO;
import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.indicador.business.IndicadorBusiness;
import br.com.marisa.srv.indicador.vo.DadosIndicadorVO;
import br.com.marisa.srv.indicador.vo.IndicadorVO;

/**
 * 
 * @author Levy Villar
 *
 */
public class RealizadoFilialIndicBusiness {

	private static final Logger log = Logger.getLogger(RealizadoFilialIndicBusiness.class);

	private static RealizadoFilialIndicBusiness instance = new RealizadoFilialIndicBusiness();

	/**
	 * 
	 * @return
	 */
	public static final RealizadoFilialIndicBusiness getInstance() {
		return instance;
	}

	/**
	 * 
	 */
	private RealizadoFilialIndicBusiness() {
	}

	/**
	 * 
	 * @param incluiVO
	 * @throws SRVException
	 */
	public void incluiRealizadoFilialIndic(RealizadoFilialIndicVO incluiVO) throws SRVException {

		RealizadoFilialIndicDAO realizFilialDAO = new RealizadoFilialIndicDAO();

		try {
			realizFilialDAO.beginTrans();

			RealizadoFilialIndicVO consultaVO = realizFilialDAO.obtemRealizadoFilialIndic(incluiVO.getIndicadorVO().getCodIndicador(), incluiVO.getFilialVO().getCodFilial(), incluiVO.getFilialVO().getCodEmpresa(), incluiVO.getAno(), incluiVO.getMes());
			if (consultaVO != null) {
				realizFilialDAO.incluiRealizadoFilialIndicHist(consultaVO);
				realizFilialDAO.excluiRealizadoFilialIndic(incluiVO);
			}

			realizFilialDAO.incluiRealizadoFilialIndic(incluiVO);
			realizFilialDAO.commitTrans();

		} catch (Exception ex) {
			realizFilialDAO.rollbackTrans();
			throw new SRVException(log, "Nao foi possivel incluir o realizado filial indic: ", ex);
		} finally {
			realizFilialDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param excluiVO
	 * @throws SRVException
	 */
	public void excluiRealizadoFilialIndic(RealizadoFilialIndicVO excluiVO) throws SRVException {

		RealizadoFilialIndicDAO realizFilialIndic = new RealizadoFilialIndicDAO();

		try {
			realizFilialIndic.beginTrans();

			RealizadoFilialIndicVO consultaVO = realizFilialIndic.obtemRealizadoFilialIndic(excluiVO.getIndicadorVO().getCodIndicador(), excluiVO.getFilialVO().getCodFilial(), excluiVO.getFilialVO().getCodEmpresa(), excluiVO.getAno(), excluiVO.getMes());
			if (consultaVO != null) {
				realizFilialIndic.incluiRealizadoFilialIndicHist(consultaVO);
			}

			realizFilialIndic.excluiRealizadoFilialIndic(excluiVO);
			realizFilialIndic.commitTrans();

		} catch (Exception ex) {
			realizFilialIndic.rollbackTrans();
			throw new SRVException(log, "Nao foi possivel excluir o realizado filial indic: ", ex);
		} finally {
			realizFilialIndic.closeConnection();
		}
	}

	/**
	 * 
	 * @param alteraVO
	 * @throws SRVException
	 */
	public void alteraRealizadoFilialIndic(RealizadoFilialIndicVO alteraVO) throws SRVException {
		try {
			this.incluiRealizadoFilialIndic(alteraVO);
		} catch (Exception ex) {
			throw new SRVException(log, "Nao foi possivel alterar o realizado filial indic: ", ex);
		}
	}

	/**
	 * 
	 * @param pesquisaVO
	 * @return
	 * @throws SRVException
	 */
	public List<RealizadoFilialIndicVO> obtemListaRealizadoFilialIndic(RealizadoFilialIndicVO pesquisaVO) throws SRVException {
		List<RealizadoFilialIndicVO> listaVO = new ArrayList<RealizadoFilialIndicVO>();
		RealizadoFilialIndicDAO realizFilialIndic = new RealizadoFilialIndicDAO();

		try {
			listaVO = realizFilialIndic.obtemListaRealizadoFilialIndic(pesquisaVO);
		} catch (Exception ex) {
			throw new SRVException(log, "Nao foi possivel obterm a lista de realizado filial indic: ", ex);
		} finally {
			realizFilialIndic.closeConnection();
		}
		return listaVO;
	}

	public List<String> importaRealizadoFilialIndic(List<String> linhasArquivo, Integer idUsuario) throws SRVException {

		RealizadoFilialIndicDAO dao = new RealizadoFilialIndicDAO();
		dao.beginTrans();

		List<String> erros = new ArrayList<String>();
		List<RealizadoFilialIndicVO> listaRealizado = new ArrayList<RealizadoFilialIndicVO>();

		if (ObjectHelper.isNotEmpty(linhasArquivo)) {

			int numLinha = 1;
			for (Iterator<String> it = linhasArquivo.iterator(); it.hasNext();) {
				String linha = it.next();
				
				try {
					StringTokenizer st = new StringTokenizer(linha, Constantes.PONTO_VIRGULA);
					RealizadoFilialIndicVO vo = new RealizadoFilialIndicVO();
					IndicadorVO indicadorVO = new IndicadorVO();
					vo.setIndicadorVO(indicadorVO);
					FilialVO filialVO = new FilialVO();
					vo.setFilialVO(filialVO);

					int mes = new Integer(st.nextToken());
					if (mes < 1 || mes > 12) {
						erros.add("Mês informado é inválido. Linha: " + numLinha);
						continue;
					} else {
						vo.setMes(mes);
					}

					vo.setAno(new Integer(st.nextToken()));

					int idFilial = new Integer(st.nextToken());
					if (!FilialBusiness.getInstance().obterFilial(Constantes.CODIGO_EMPRESA, idFilial)) {
						vo.getFilialVO().setCodFilial(idFilial);
						vo.getFilialVO().setCodEmpresa(Constantes.CODIGO_EMPRESA);
					} else {
						erros.add("Filial informada é inválida. Linha: " + numLinha);
						continue;
					}

					int idIndicador = new Integer(st.nextToken());
					DadosIndicadorVO dadosIndicadorVO = IndicadorBusiness.getInstance().obtemIndicador(idIndicador);
					if (ObjectHelper.isNotNull(dadosIndicadorVO) && ObjectHelper.isNotEmpty(dadosIndicadorVO.getIdIndicador())) {
						vo.getIndicadorVO().setCodIndicador(dadosIndicadorVO.getIdIndicador());
					} else {
						erros.add("Cód. Indicador informado é inválido. Linha: " + numLinha);
						continue;
					}

					String valor = st.nextToken();
					if (valor.indexOf(".") > -1) {
						erros.add("Valor realizado é inválido. Linha: " + numLinha);
						continue;
					}
					vo.setNumRealizado(new Double(valor.replaceAll(",", ".")));
					vo.setCodUsuario(idUsuario);
					listaRealizado.add(vo);

				} catch (Exception ex) {
					erros.add("Erro de layout ao importar linha: " + numLinha);
				} finally {
					numLinha++;
				}
			}

			if (erros.size() == 0) {
				try {
					for (Iterator<RealizadoFilialIndicVO> it = listaRealizado.iterator(); it.hasNext();) {
						RealizadoFilialIndicVO realizFilIndicVO = it.next();
						RealizadoFilialIndicVO antigoVO = dao.obtemRealizadoFilialIndic(realizFilIndicVO.getIndicadorVO().getCodIndicador(), realizFilIndicVO.getFilialVO().getCodFilial(), realizFilIndicVO.getFilialVO().getCodEmpresa(),realizFilIndicVO.getAno(), realizFilIndicVO.getMes());
						if (antigoVO != null) {
							dao.incluiRealizadoFilialIndicHist(antigoVO);
							dao.excluiRealizadoFilialIndic(realizFilIndicVO);
						}
						dao.incluiRealizadoFilialIndic(realizFilIndicVO);
					}
					dao.commitTrans();
				} catch (Exception ex) {
					dao.rollbackTrans();
					throw new SRVException(ex.getMessage(), ex);
				} finally {
					dao.closeConnection();
				}
			}
		} else {
			erros.add("Nenhum registro foi enviado no arquivo!");
		}
		
		return erros;
	}

}
