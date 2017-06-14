package br.com.marisa.srv.bonus.configuracao.escala.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.bonus.configuracao.vo.ConfiguracaoBonusVO;
import br.com.marisa.srv.escala.vo.EscalaVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;

/**
 * 
 * @author levy.villar
 * 
 */
public class ConfiguracaoBonusEscalaDAO extends BasicDAO {

    private static final Logger log = Logger.getLogger(ConfiguracaoBonusEscalaDAO.class);

    /**
     * 
     * @param ano
     * @return
     * @throws PersistenciaException
     */
	public List<EscalaVO> obtemListaConfiguracaoBonusEscala(Integer ano) throws PersistenciaException {		

		String query = " SELECT E.COD_ESCALA, E.NUM_ESCALA, E.DESCR_ESCALA FROM SRV_CONFIG_BONUS_ESCALA BE, SRV_ESCALA E WHERE BE.COD_ESCALA = E.COD_ESCALA AND NUM_ANO = ? ";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<EscalaVO> listaEscalaVO = new ArrayList<EscalaVO>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());

			int i = 0;
			setInteger(stmt, ++i, ano);
			rs = stmt.executeQuery();

			while (rs.next()) {
				EscalaVO escalaVO = new EscalaVO();
				escalaVO.setIdEscala(getInteger(rs, "COD_ESCALA"));
				escalaVO.setNumEscala(getInteger(rs, "NUM_ESCALA"));
				escalaVO.setDescricaoEscala(getString(rs, "DESCR_ESCALA"));
				listaEscalaVO.add(escalaVO);
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista escala da configuracao do bonus", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return listaEscalaVO;
	}

	/**
	 * 
	 * @param configuracaoBonusVO
	 * @throws PersistenciaException
	 */
	public void incluiConfiguracaoBonusEscala(ConfiguracaoBonusVO configuracaoBonusVO) throws PersistenciaException {

		String query = " INSERT INTO SRV_CONFIG_BONUS_ESCALA (NUM_ANO, COD_ESCALA, DT_INI_SIT_SRV, COD_USUARIO) VALUES (?, ?, SYSDATE, ?) ";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConn();
			for (EscalaVO escalaVO : configuracaoBonusVO.getListaEscala()) {
				stmt = conn.prepareStatement(query);
				int i = 0;
				setInteger(stmt, ++i, configuracaoBonusVO.getAno());
				setInteger(stmt, ++i, escalaVO.getIdEscala());
				setInteger(stmt, ++i, configuracaoBonusVO.getCodUsuario());
				stmt.execute();
			}
		} catch(Exception e) {
			throw new PersistenciaException(log, "Não foi possível incluir a escala na configuracao do bonus", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

}