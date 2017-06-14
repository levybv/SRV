package br.com.marisa.srv.unidade.business;

import java.util.ArrayList;
import java.util.List;

import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.unidade.dao.UnidadeDAO;
import br.com.marisa.srv.unidade.vo.UnidadeVO;

/**
 * Classe para conter os métodos de negócio de unidades
 * 
 * @author Walter Fontes
 */
public class UnidadeBusiness {

	//Unidades
	public static final int COD_UNIDADE_VALOR = 1;
	public static final int COD_UNIDADE_PERCENTUAL = 2;
	public static final int COD_UNIDADE_UNIDADE = 3;
	public static final int COD_UNIDADE_PONTOS = 4;
	public static final int COD_UNIDADE_ORCAMENTO = 5;
	public static final int COD_UNIDADE_PECAS_HOMEM_HORA = 6;
	public static final int COD_UNIDADE_DIAS = 7;
	public static final int COD_UNIDADE_INDICE = 8;
	public static final int COD_UNIDADE_PA = 9;
	public static final int COD_UNIDADE_PEDIDOS_HORA = 10;
	public static final int COD_UNIDADE_RETORNO_CUSTO = 11;
	public static final int COD_UNIDADE_PECAS_HORA = 12;
	public static final int COD_UNIDADE_PECAS_PEDIDO = 13;
	public static final int COD_UNIDADE_PECAS_TKT = 14;
	public static final int COD_UNIDADE_AUDITORIA_DIA = 15;
	public static final int COD_UNIDADE_VOLUMES_HOMEM_HORA = 16;
	public static final int COD_UNIDADE_SEGUNDOS = 17;

	// Instancia do Singleton
	private static UnidadeBusiness instance = new UnidadeBusiness();

	/**
	 * Obtem uma instancia do objeto UnidadeBusiness
	 * 
	 * @return O objeto UnidadeBusiness
	 */
	public static final UnidadeBusiness getInstance() {
		return instance;
	}

	/**
	 * Construtor default. Foi escondido como private por cargo do singleton.
	 * Utilizar o método getInstance();
	 */
	private UnidadeBusiness() {
	}

	/**
	 * Obtém unidades
	 * 
	 * @return
	 * @throws NumberFormatException
	 * @throws SRVException
	 */
	public List obtemUnidades() throws Exception {
		UnidadeDAO unidadeDAO = new UnidadeDAO();
		try {
			return unidadeDAO.obtemUnidades();
		} finally {
			unidadeDAO.closeConnection();
		}
	}

	/**
	 * Obtém unidades agregadas
	 * 
	 * @return
	 * @throws Exception
	 */
	public List obtemUnidadesAgregadas() throws Exception {

		List unidades = new ArrayList();
		UnidadeVO unidadeVO = null;

		for (int i = 0; i < 5; i++) {
			switch (i) {
			case 1:
				unidadeVO = new UnidadeVO();
				unidadeVO.setIdUnidade(new Integer(i));
				unidadeVO.setDescricaoUnidade("VALOR");
				unidadeVO.setSimbolo("");
				unidades.add(unidadeVO);
				break;
			case 2:
				unidadeVO = new UnidadeVO();
				unidadeVO.setIdUnidade(new Integer(i));
				unidadeVO.setDescricaoUnidade("PERCENTUAL");
				unidadeVO.setSimbolo("");
				unidades.add(unidadeVO);
				break;
			case 3:
				unidadeVO = new UnidadeVO();
				unidadeVO.setSimbolo("");
				unidadeVO.setIdUnidade(new Integer(i));
				unidadeVO.setDescricaoUnidade("UNIDADE");
				unidades.add(unidadeVO);
				break;
			case 4:
				unidadeVO = new UnidadeVO();
				unidadeVO.setSimbolo("%");
				unidadeVO.setIdUnidade(new Integer(i));
				unidadeVO.setDescricaoUnidade("PONTOS");
				unidades.add(unidadeVO);
				break;
			default:
				break;
			}

		}
		return unidades;
	}

	/**
	 * 
	 * @param simboloUnidadeParam
	 * @return
	 * @throws SRVException
	 */
	public UnidadeVO obtemUnidadePorSimbolo(String simboloUnidadeParam) throws SRVException {
		UnidadeDAO unidadeDAO = new UnidadeDAO();
		try {
			return unidadeDAO.obtemUnidadePorSimbolo(simboloUnidadeParam.replaceAll(" ", ""));
		} finally {
			unidadeDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param descricaoUnidadeParam
	 * @return
	 * @throws SRVException
	 */
	public UnidadeVO obtemUnidadePorDescricao(String descricaoUnidadeParam) throws SRVException {
		UnidadeDAO unidadeDAO = new UnidadeDAO();
		try {
			return unidadeDAO.obtemUnidadePorDescricao(descricaoUnidadeParam.replaceAll(" ", ""));
		} finally {
			unidadeDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param unidadeParam
	 * @return
	 * @throws SRVException
	 */
	public UnidadeVO obtemUnidade(String unidadeParam) throws SRVException {
		UnidadeDAO unidadeDAO = new UnidadeDAO();
		try {
			UnidadeVO unidadeVO = null;
			unidadeVO = unidadeDAO.obtemUnidadePorSimbolo(unidadeParam.replaceAll(" ", ""));
			if (unidadeVO != null) {
				return unidadeVO;
			} else {
				unidadeVO = unidadeDAO.obtemUnidadePorDescricao(unidadeParam.replaceAll(" ", ""));
				return unidadeVO;
			}
		} finally {
			unidadeDAO.closeConnection();
		}
	}
	
}