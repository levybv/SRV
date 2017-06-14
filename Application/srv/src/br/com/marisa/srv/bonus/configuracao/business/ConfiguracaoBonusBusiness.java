package br.com.marisa.srv.bonus.configuracao.business;

import java.util.ArrayList;
import java.util.List;

import br.com.marisa.srv.bonus.configuracao.dao.ConfiguracaoBonusDAO;
import br.com.marisa.srv.bonus.configuracao.escala.dao.ConfiguracaoBonusEscalaDAO;
import br.com.marisa.srv.bonus.configuracao.vo.ConfiguracaoBonusVO;
import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * 
 * @author levy.villar
 * 
 */
public class ConfiguracaoBonusBusiness {

	private static ConfiguracaoBonusBusiness instance = new ConfiguracaoBonusBusiness();

	/**
	 * 
	 */
	private ConfiguracaoBonusBusiness() {
	}

	/**
	 * 
	 * @return
	 */
	public static final ConfiguracaoBonusBusiness getInstance() {
		return instance;
	}

	/**
	 * 
	 * @param ano
	 * @return
	 * @throws SRVException
	 */
	public ConfiguracaoBonusVO obtemConfiguracaoBonus(Integer ano) throws SRVException {
		ConfiguracaoBonusVO configuracaoBonusVO = null;

		ConfiguracaoBonusDAO configuracaoBonusDAO = new ConfiguracaoBonusDAO();
		ConfiguracaoBonusEscalaDAO configuracaoBonusEscalaDAO = new ConfiguracaoBonusEscalaDAO();

		try {

			configuracaoBonusEscalaDAO.setConn(configuracaoBonusDAO.getConn());

			configuracaoBonusVO = configuracaoBonusDAO.obtemConfiguracaoBonus(ano);
			if (configuracaoBonusVO!=null) {
				configuracaoBonusVO.setListaEscala(configuracaoBonusEscalaDAO.obtemListaConfiguracaoBonusEscala(ano));
			}
			
		} catch (Exception ex) {
			throw new SRVException("Ocorreu um erro ao obter a configuracao do bonus");
		} finally {
			configuracaoBonusDAO.closeConnection();
		}
		return configuracaoBonusVO;
	}

	/**
	 * 
	 * @param codFuncionario
	 * @param anoBonus
	 * @return
	 * @throws SRVException
	 */
	public List<ConfiguracaoBonusVO> obtemListaConfiguracaoBonus(ConfiguracaoBonusVO configuracaoBonusVO) throws SRVException {

		List<ConfiguracaoBonusVO> listaConfiguracaoBonus = new ArrayList<ConfiguracaoBonusVO>();
		ConfiguracaoBonusDAO configuracaoBonusDAO = new ConfiguracaoBonusDAO();
		ConfiguracaoBonusEscalaDAO configuracaoBonusEscalaDAO = new ConfiguracaoBonusEscalaDAO();

		try {

			configuracaoBonusEscalaDAO.setConn(configuracaoBonusDAO.getConn());

			listaConfiguracaoBonus = configuracaoBonusDAO.obtemListaConfiguracaoBonus(configuracaoBonusVO);
			for (ConfiguracaoBonusVO configuracaoBonusTemp : listaConfiguracaoBonus) {
				configuracaoBonusTemp.setListaEscala(configuracaoBonusEscalaDAO.obtemListaConfiguracaoBonusEscala(configuracaoBonusTemp.getAno()));
			}

		} catch (Exception ex) {
			throw new SRVException("Ocorreu um erro ao obter a lista de configuracao do bonus");
		} finally {
			configuracaoBonusDAO.closeConnection();
		}

		return listaConfiguracaoBonus;
	}

	/**
	 * 
	 * @param configuracaoBonusVO
	 * @throws SRVException
	 */
	public void incluiConfiguracaoBonus(ConfiguracaoBonusVO configuracaoBonusVO) throws SRVException {
		ConfiguracaoBonusDAO configuracaoBonusDAO = new ConfiguracaoBonusDAO();
		ConfiguracaoBonusEscalaDAO configuracaoBonusEscalaDAO = new ConfiguracaoBonusEscalaDAO();
		try {
			configuracaoBonusEscalaDAO.setConn(configuracaoBonusDAO.getConn());

			configuracaoBonusDAO.beginTrans();
			configuracaoBonusDAO.incluiConfiguracaoBonus(configuracaoBonusVO);
			configuracaoBonusEscalaDAO.incluiConfiguracaoBonusEscala(configuracaoBonusVO);
			configuracaoBonusDAO.commitTrans();

		} catch (Exception ex) {
			configuracaoBonusDAO.rollbackTrans();
			throw new SRVException("Ocorreu um erro ao inserir a configuracao do bonus");
		} finally {
			configuracaoBonusDAO.closeConnection();
		}
	}

	public void alteraConfiguracaoBonus(ConfiguracaoBonusVO configuracaoBonusVO) throws SRVException {
		ConfiguracaoBonusDAO configuracaoBonusDAO = new ConfiguracaoBonusDAO();
		ConfiguracaoBonusEscalaDAO configuracaoBonusEscalaDAO = new ConfiguracaoBonusEscalaDAO();
		try {
			configuracaoBonusEscalaDAO.setConn(configuracaoBonusDAO.getConn());

			configuracaoBonusDAO.beginTrans();
			configuracaoBonusDAO.incluiConfiguracaoBonusHistorico(configuracaoBonusDAO.obtemConfiguracaoBonus(configuracaoBonusVO.getAno()));
			configuracaoBonusDAO.excluiConfiguracaoBonus(configuracaoBonusVO);
			configuracaoBonusDAO.incluiConfiguracaoBonus(configuracaoBonusVO);
			configuracaoBonusEscalaDAO.incluiConfiguracaoBonusEscala(configuracaoBonusVO);
			configuracaoBonusDAO.commitTrans();

		} catch (Exception ex) {
			configuracaoBonusDAO.rollbackTrans();
			throw new SRVException("Ocorreu um erro ao inserir a configuracao do bonus");
		} finally {
			configuracaoBonusDAO.closeConnection();
		}
	}

}