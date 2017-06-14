package br.com.marisa.srv.bonus.configuracao.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.bonus.configuracao.vo.ConfiguracaoBonusVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.helper.ObjectHelper;

/**
 * 
 * @author levy.villar
 * 
 */
public class ConfiguracaoBonusDAO extends BasicDAO {

    private static final Logger log = Logger.getLogger(ConfiguracaoBonusDAO.class);

    /**
     * 
     * @param ano
     * @return
     * @throws PersistenciaException
     */
	public ConfiguracaoBonusVO obtemConfiguracaoBonus(Integer ano) throws PersistenciaException {		

		StringBuffer query = new StringBuffer(
			" SELECT NUM_ANO, " +
			"        COD_INDIC_INI, " +
			"        COD_INDIC_FIM, " +
			"        FLG_FUNDING, " +
			"        FUNDING, " +
			"        FLG_CONTRATO_META, " +
			"        DT_LIMITE_ACEITE, " +
			"        TEXTO_CONSENTIMENTO, " +
			"        PERIODO_DISPONIVEL, " +
			"        ID_FUNC_CORP, " +
			"        FLG_ENCERRADO, " +
			"        DT_INI_SIT_SRV, " +
			"        COD_USUARIO " +
			"   FROM SRV_CONFIG_BONUS " +
			"  WHERE NUM_ANO = ? ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ConfiguracaoBonusVO configBonusVO = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());

			int i = 0;
			setInteger(stmt, ++i, ano);
			rs = stmt.executeQuery();

			if (rs.next()) {
				configBonusVO = new ConfiguracaoBonusVO();
				configBonusVO.setAno(getInteger(rs, "NUM_ANO"));
				configBonusVO.setCodIndicIni(getInteger(rs, "COD_INDIC_INI"));
				configBonusVO.setCodIndicFim(getInteger(rs, "COD_INDIC_FIM"));
				configBonusVO.setIsFunding(getBoolean(rs, "FLG_FUNDING"));
				configBonusVO.setFunding(getDouble(rs, "FUNDING"));
				configBonusVO.setIsContratoMeta(getBoolean(rs, "FLG_CONTRATO_META"));
				configBonusVO.setDataLimiteAceite(getDate(rs, "DT_LIMITE_ACEITE"));
				configBonusVO.setTextoConsentimento(getString(rs, "TEXTO_CONSENTIMENTO"));
				configBonusVO.setPeriodoDisponivel(getString(rs, "PERIODO_DISPONIVEL"));
				configBonusVO.setIsEncerrado(getBoolean(rs, "FLG_ENCERRADO"));
				configBonusVO.setIdFuncionarioCorporativo(getLong(rs, "ID_FUNC_CORP"));
				configBonusVO.setDtIniSitSrv(getTimestamp(rs, "DT_INI_SIT_SRV"));
				configBonusVO.setCodUsuario(getInteger(rs, "COD_USUARIO"));
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a configuracao do bonus", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return configBonusVO;
	}

	/**
	 * 
	 * @param configuracaoBonusVO
	 * @return
	 * @throws PersistenciaException
	 */
	public List<ConfiguracaoBonusVO> obtemListaConfiguracaoBonus(ConfiguracaoBonusVO configuracaoBonusVO) throws PersistenciaException {		

		StringBuffer query = new StringBuffer(
			" SELECT NUM_ANO, " +
			"        COD_INDIC_INI, " +
			"        COD_INDIC_FIM, " +
			"        FLG_FUNDING, " +
			"        FUNDING, " +
			"        FLG_CONTRATO_META, " +
			"        DT_LIMITE_ACEITE, " +
			"        TEXTO_CONSENTIMENTO, " +
			"        PERIODO_DISPONIVEL, " +
			"        FLG_ENCERRADO, " +
			"        ID_FUNC_CORP, " +
			"        DT_INI_SIT_SRV, " +
			"        COD_USUARIO " +
			"   FROM SRV_CONFIG_BONUS ");

		if (ObjectHelper.isNotNull(configuracaoBonusVO)) {
			if (ObjectHelper.isNotEmpty(configuracaoBonusVO.getAno())) {
				query.append(getWhereAnd(query)).append(" NUM_ANO = ? ");
			}
			if (ObjectHelper.isNotNull(configuracaoBonusVO.getIsEncerrado())) {
				query.append(getWhereAnd(query)).append(" FLG_ENCERRADO = ? ");
			}
		}
		query.append(" ORDER BY NUM_ANO ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<ConfiguracaoBonusVO> listaConfigBonusVO = new ArrayList<ConfiguracaoBonusVO>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());

			int i = 0;
			if (ObjectHelper.isNotNull(configuracaoBonusVO)) {
				if (ObjectHelper.isNotEmpty(configuracaoBonusVO.getAno())) {
					setInteger(stmt, ++i, configuracaoBonusVO.getAno());
				}
				if (ObjectHelper.isNotNull(configuracaoBonusVO.getIsEncerrado())) {
					setBoolean(stmt, ++i, configuracaoBonusVO.getIsEncerrado());
				}
			}
			rs = stmt.executeQuery();

			while (rs.next()) {
				ConfiguracaoBonusVO configBonusVO = new ConfiguracaoBonusVO();
				configBonusVO.setAno(getInteger(rs, "NUM_ANO"));
				configBonusVO.setCodIndicIni(getInteger(rs, "COD_INDIC_INI"));
				configBonusVO.setCodIndicFim(getInteger(rs, "COD_INDIC_FIM"));
				configBonusVO.setIsFunding(getBoolean(rs, "FLG_FUNDING"));
				configBonusVO.setFunding(getDouble(rs, "FUNDING"));
				configBonusVO.setIsContratoMeta(getBoolean(rs, "FLG_CONTRATO_META"));
				configBonusVO.setDataLimiteAceite(getDate(rs, "DT_LIMITE_ACEITE"));
				configBonusVO.setTextoConsentimento(getString(rs, "TEXTO_CONSENTIMENTO"));
				configBonusVO.setPeriodoDisponivel(getString(rs, "PERIODO_DISPONIVEL"));
				configBonusVO.setIsEncerrado(getBoolean(rs, "FLG_ENCERRADO"));
				configBonusVO.setIdFuncionarioCorporativo(getLong(rs, "ID_FUNC_CORP"));
				configBonusVO.setDtIniSitSrv(getTimestamp(rs, "DT_INI_SIT_SRV"));
				configBonusVO.setCodUsuario(getInteger(rs, "COD_USUARIO"));
				listaConfigBonusVO.add(configBonusVO);
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de configuracao do bonus", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return listaConfigBonusVO;
	}

	/**
	 * 
	 * @param configuracaoBonusVO
	 * @throws PersistenciaException
	 */
	public void incluiConfiguracaoBonus(ConfiguracaoBonusVO configuracaoBonusVO) throws PersistenciaException {

		String query =  
			" INSERT INTO SRV_CONFIG_BONUS " +
			"   (NUM_ANO, " +
			"    COD_INDIC_INI, " +
			"    COD_INDIC_FIM, " +
			"    FLG_FUNDING, " +
			"    FUNDING, " +
			"    FLG_CONTRATO_META, " +
			"    DT_LIMITE_ACEITE, " +
			"    TEXTO_CONSENTIMENTO, " +
			"    PERIODO_DISPONIVEL, " +
			"    FLG_ENCERRADO, " +
			"    ID_FUNC_CORP, " +
			"    DT_INI_SIT_SRV, " +
			"    COD_USUARIO) " +
			" VALUES " +
			"   (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?) ";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			int i = 0;
			setInteger(stmt, ++i, configuracaoBonusVO.getAno());
			setInteger(stmt, ++i, configuracaoBonusVO.getCodIndicIni());
			setInteger(stmt, ++i, configuracaoBonusVO.getCodIndicFim());
			setBoolean(stmt, ++i, configuracaoBonusVO.getIsFunding());
			setDouble(stmt, ++i, configuracaoBonusVO.getFunding());
			setBoolean(stmt, ++i, configuracaoBonusVO.getIsContratoMeta());
			setDate(stmt, ++i, configuracaoBonusVO.getDataLimiteAceite());
			setString(stmt, ++i, configuracaoBonusVO.getTextoConsentimento());
			setString(stmt, ++i, configuracaoBonusVO.getPeriodoDisponivel());
			setBoolean(stmt, ++i, configuracaoBonusVO.getIsEncerrado());
			setLong(stmt, ++i, configuracaoBonusVO.getIdFuncionarioCorporativo());
			setInteger(stmt, ++i, configuracaoBonusVO.getCodUsuario());
			stmt.execute();
		} catch(Exception e) {
			throw new PersistenciaException(log, "Nao foi possível incluir a configuracao do bonus ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

	/**
	 * 
	 * @param configuracaoBonusVO
	 * @throws PersistenciaException
	 */
	public void excluiConfiguracaoBonus(ConfiguracaoBonusVO configuracaoBonusVO) throws PersistenciaException {

		String query = " DELETE FROM SRV_CONFIG_BONUS WHERE NUM_ANO = ? ";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			int i = 0;
			setInteger(stmt, ++i, configuracaoBonusVO.getAno());
			stmt.execute();
		} catch(Exception e) {
			throw new PersistenciaException(log, "Nao foi possivel excluir a configuracao do bonus", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

	/**
	 * 
	 * @param configuracaoBonusVO
	 * @throws PersistenciaException
	 */
	public void incluiConfiguracaoBonusHistorico(ConfiguracaoBonusVO configuracaoBonusVO) throws PersistenciaException {

		String query =  
			" INSERT INTO SRV_CONFIG_BONUS_HIST " +
			"   (NUM_ANO, " +
			"    NUM_SEQ, " +
			"    COD_INDIC_INI, " +
			"    COD_INDIC_FIM, " +
			"    FLG_FUNDING, " +
			"    FUNDING, " +
			"    FLG_CONTRATO_META, " +
			"    DT_LIMITE_ACEITE, " +
			"    TEXTO_CONSENTIMENTO, " +
			"    PERIODO_DISPONIVEL, " +
			"    FLG_ENCERRADO, " +
			"    ID_FUNC_CORP, " +
			"    DT_INI_SIT_SRV, " +
			"    COD_USUARIO) " +
			" VALUES " +
			"   (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?) ";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			int i = 0;
			setInteger(stmt, ++i, configuracaoBonusVO.getAno());
			setInteger(stmt, ++i, this.obtemProximaSequenciaHistorio(configuracaoBonusVO.getAno()));
			setInteger(stmt, ++i, configuracaoBonusVO.getCodIndicIni());
			setInteger(stmt, ++i, configuracaoBonusVO.getCodIndicFim());
			setBoolean(stmt, ++i, configuracaoBonusVO.getIsFunding());
			setDouble(stmt, ++i, configuracaoBonusVO.getFunding());
			setBoolean(stmt, ++i, configuracaoBonusVO.getIsContratoMeta());
			setDate(stmt, ++i, configuracaoBonusVO.getDataLimiteAceite());
			setString(stmt, ++i, configuracaoBonusVO.getTextoConsentimento());
			setString(stmt, ++i, configuracaoBonusVO.getPeriodoDisponivel());
			setBoolean(stmt, ++i, configuracaoBonusVO.getIsEncerrado());
			setLong(stmt, ++i, configuracaoBonusVO.getIdFuncionarioCorporativo());
			setInteger(stmt, ++i, configuracaoBonusVO.getCodUsuario());
			stmt.execute();
		} catch(Exception e) {
			throw new PersistenciaException(log, "Nao foi possível incluir o historico de configuracao do bonus ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

	/**
	 * 
	 * @param ano
	 * @return
	 * @throws PersistenciaException
	 */
	private int obtemProximaSequenciaHistorio(int ano) throws PersistenciaException {

		String query = "SELECT MAX(NUM_SEQ) FROM SRV_CONFIG_BONUS_HIST WHERE NUM_ANO = ? ";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int proximaSequencia = 1;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			int ordemCampos = 1;
			setInteger(stmt, ordemCampos++, ano);
			rs = stmt.executeQuery();

			if (rs.next()) {
				proximaSequencia = getInt(rs, 1) + 1;
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter a proxima sequencia de historico de configuracao bonus " + ano, e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return proximaSequencia;
	}

}