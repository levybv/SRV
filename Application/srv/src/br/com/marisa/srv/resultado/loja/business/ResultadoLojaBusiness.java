package br.com.marisa.srv.resultado.loja.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.marisa.srv.filial.business.FilialBusiness;
import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.gerente.filial.business.GerenteFilialBusiness;
import br.com.marisa.srv.gerente.filial.vo.GerenteVO;
import br.com.marisa.srv.resultado.loja.dao.ResultadoLojaDAO;
import br.com.marisa.srv.resultado.loja.vo.ResultadoVO;

/**
 * 
 * @author levy.villar
 * 
 */
public class ResultadoLojaBusiness {

	//private static final Logger log = Logger.getLogger(GerenteFilialBusiness.class);

	private static ResultadoLojaBusiness instance = new ResultadoLojaBusiness();

	/**
	 * 
	 * @return
	 */
	public static final ResultadoLojaBusiness getInstance() {
		return instance;
	}

	/**
     * 
     */
	private ResultadoLojaBusiness() {
	}

	/**
	 * 
	 * @param numAno
	 * @param numMes
	 * @param idLoja
	 * @return
	 * @throws SRVException
	 */
	public List<ResultadoVO> obterResultadoPorLoja(Integer numAno, Integer numMes, Integer idLoja) throws SRVException {
		ResultadoLojaDAO premiacaoLojaDAO = new ResultadoLojaDAO();
		try {
			List<ResultadoVO> tempList = premiacaoLojaDAO.obterResultadoPorLoja(numAno, numMes, idLoja);
			List<ResultadoVO> lista = new ArrayList<ResultadoVO>();
			for (Iterator<ResultadoVO> it = tempList.iterator(); it.hasNext();) {
				ResultadoVO resultadoVO = it.next();
				if (resultadoVO.getCodIndic().intValue() == 1) {
					ResultadoVO grupoVendasVO = new ResultadoVO();
					grupoVendasVO.setDescIndic("VENDAS");
					lista.add(grupoVendasVO);
					lista.add(resultadoVO);
					ResultadoVO grupoPsfVO = new ResultadoVO();
					grupoPsfVO.setDescIndic("PSF");
					lista.add(grupoPsfVO);
				} else {
					lista.add(resultadoVO);
				}
			}
			return lista;
		} finally {
			premiacaoLojaDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param numAno
	 * @param numMes
	 * @param idLoja
	 * @param idFuncionario
	 * @return
	 * @throws SRVException
	 */
	public List<ResultadoVO> obterResultadoPorOperacional(Integer numAno, Integer numMes, Integer idLoja, Long idFuncionario) throws SRVException {
		ResultadoLojaDAO premiacaoLojaDAO = new ResultadoLojaDAO();
		try {
			return premiacaoLojaDAO.obterResultadoPorOperacional(numAno, numMes, idLoja, idFuncionario);
		} finally {
			premiacaoLojaDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param numAno
	 * @param numMes
	 * @param idLoja
	 * @param idFuncionario
	 * @return
	 * @throws SRVException
	 */
	public List<ResultadoVO> obterResultadoPorLider(Integer numAno, Integer numMes, Integer idLoja, Long idFuncionario) throws SRVException {
		ResultadoLojaDAO premiacaoLojaDAO = new ResultadoLojaDAO();
		try {
			return premiacaoLojaDAO.obterResultadoPorLider(numAno, numMes, idLoja, idFuncionario);
		} finally {
			premiacaoLojaDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param idFuncionario
	 * @return
	 * @throws SRVException
	 */
	public List<FilialVO> obtemListaFilialPorFuncionario(long idFuncionario) throws SRVException {
		try {
			List<FilialVO> listaFilial = new ArrayList<FilialVO>();
			Integer idFilialCadastro = GerenteFilialBusiness.getInstance().obtemLojaCadastro(idFuncionario);
			listaFilial.add(FilialBusiness.getInstance().obtemFilial(Constantes.CODIGO_EMPRESA, idFilialCadastro, true));

			GerenteVO gerenteVO = new GerenteVO();
			gerenteVO.setIdFuncionario(idFuncionario);
			List<GerenteVO> listaGerenteLoja = GerenteFilialBusiness.getInstance().obtemListaGerenteLoja(gerenteVO);
			for (Iterator<GerenteVO> it = listaGerenteLoja.iterator(); it.hasNext();) {
				GerenteVO itemGerenteVO = it.next();
				if (itemGerenteVO.getFilial() != null && idFilialCadastro != itemGerenteVO.getFilial().getCodFilial().intValue()) {
					listaFilial.add(FilialBusiness.getInstance().obtemFilial(Constantes.CODIGO_EMPRESA, itemGerenteVO.getFilial().getCodFilial(), null));
				}
			}
			return listaFilial;
		} finally {}
	}

	/**
	 * 
	 * @param numAno
	 * @param numMes
	 * @param idLoja
	 * @return
	 * @throws SRVException
	 */
	public List<FuncionarioVO> obterListaFuncionarioPorLider(Integer numAno, Integer numMes, Integer idLoja) throws SRVException {
		ResultadoLojaDAO premiacaoLojaDAO = new ResultadoLojaDAO();
		try {
			List<FuncionarioVO> listFuncLider = new ArrayList<FuncionarioVO>();
			List<ResultadoVO> listTemp = premiacaoLojaDAO.obterResultadoPorLider(numAno, numMes, idLoja, null);
			long codLastFunc = 0;
			for (Iterator<ResultadoVO> it = listTemp.iterator(); it.hasNext();) {
				ResultadoVO resultadoVO = it.next();
				if (resultadoVO.getCodFunc().longValue() != codLastFunc) {
					FuncionarioVO funcVO = new FuncionarioVO();
					funcVO.setIdFuncionario(resultadoVO.getCodFunc().longValue());
					funcVO.setNomeFuncionario(resultadoVO.getNomeFunc());
					codLastFunc = resultadoVO.getCodFunc().longValue();
					listFuncLider.add(funcVO);
				}
			}
			return listFuncLider;
		} finally {
			premiacaoLojaDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param numAno
	 * @param numMes
	 * @param idLoja
	 * @return
	 * @throws SRVException
	 */
	public List<FuncionarioVO> obterListaFuncionarioPorOperacional(Integer numAno, Integer numMes, Integer idLoja) throws SRVException {
		ResultadoLojaDAO premiacaoLojaDAO = new ResultadoLojaDAO();
		try {
			List<FuncionarioVO> listFuncLider = new ArrayList<FuncionarioVO>();
			List<ResultadoVO> listTemp = premiacaoLojaDAO.obterResultadoPorOperacional(numAno, numMes, idLoja, null);
			long codLastFunc = 0;
			for (Iterator<ResultadoVO> it = listTemp.iterator(); it.hasNext();) {
				ResultadoVO resultadoVO = it.next();
				if (resultadoVO.getCodFunc().longValue() != codLastFunc) {
					FuncionarioVO funcVO = new FuncionarioVO();
					funcVO.setIdFuncionario(resultadoVO.getCodFunc().longValue());
					funcVO.setNomeFuncionario(resultadoVO.getNomeFunc());
					codLastFunc = resultadoVO.getCodFunc().longValue();
					listFuncLider.add(funcVO);
				}
			}
			return listFuncLider;
		} finally {
			premiacaoLojaDAO.closeConnection();
		}
	}

}
