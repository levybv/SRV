package br.com.marisa.srv.calendario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.calendario.vo.CalendarioBonusVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.dao.ParametroSQL;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.helper.ObjectHelper;


/**
 * 
 * @author levy.villar
 *
 */
public class CalendarioBonusDAO extends BasicDAO {

    private static final Logger log = Logger.getLogger(CalendarioBonusDAO.class);
    
    /**
     * 
     * @param paramVO
     * @return
     * @throws PersistenciaException
     */
    public List<CalendarioBonusVO> obtemCalendarioBonus(CalendarioBonusVO paramVO) throws PersistenciaException {

        StringBuffer query = new StringBuffer(" SELECT NUM_ANO, NUM_MES, DESCR_MES, DT_INI_PERIODO, DT_FIM_PERIODO, COD_USUARIO, DT_ULT_ALT FROM SRV_CALENDARIO_BONUS WHERE (1=1) ");

        if ( ObjectHelper.isNotNull(paramVO) ) {
        	if ( ObjectHelper.isNotEmpty(paramVO.getAno()) ) {
        		query.append(" AND NUM_ANO = ? ");
        	}
        	if ( ObjectHelper.isNotEmpty(paramVO.getMes()) ) {
        		query.append(" AND NUM_MES = ? ");
        	}
        	if ( ObjectHelper.isNotEmpty(paramVO.getDescricaoMes()) ) {
        		query.append(" AND UPPER(DESCR_MES) LIKE ? ");
        	}
        	if ( ObjectHelper.isNotEmpty(paramVO.getDataInicial()) ) {
        		query.append(" AND DT_INI_PERIODO = ? ");
        	}
        	if ( ObjectHelper.isNotEmpty(paramVO.getDataFinal()) ) {
        		query.append(" AND DT_FIM_PERIODO = ? ");
        	}
        }

        query.append(" ORDER BY NUM_ANO, NUM_MES ");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<CalendarioBonusVO> lista = new ArrayList<CalendarioBonusVO>();
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query.toString());

            int i = 1;
            if ( ObjectHelper.isNotNull(paramVO) ) {
            	if ( ObjectHelper.isNotEmpty(paramVO.getAno()) ) {
            		setInteger(stmt, i++, paramVO.getAno());
            	}
            	if ( ObjectHelper.isNotEmpty(paramVO.getMes()) ) {
            		setInteger(stmt, i++, paramVO.getMes());
            	}
            	if ( ObjectHelper.isNotEmpty(paramVO.getDescricaoMes()) ) {
            		setString(stmt, i++, "%"+paramVO.getDescricaoMes().toUpperCase()+"%");
            	}
            	if ( ObjectHelper.isNotEmpty(paramVO.getDataInicial()) ) {
            		setDate(stmt, i++, paramVO.getDataInicial());
            	}
            	if ( ObjectHelper.isNotEmpty(paramVO.getDataFinal()) ) {
            		setDate(stmt, i++, paramVO.getDataFinal());
            	}
            }
            rs = stmt.executeQuery();
                        
            while (rs.next()) {
            	CalendarioBonusVO vo = new CalendarioBonusVO();
            	vo.setAno(getInteger(rs, "NUM_ANO"));
            	vo.setMes(getInteger(rs, "NUM_MES"));
            	vo.setDataInicial(getDate(rs, "DT_INI_PERIODO"));
            	vo.setDataFinal(getDate(rs, "DT_FIM_PERIODO"));
            	vo.setDescricaoMes(getString(rs, "DESCR_MES"));
            	vo.setCodUsuario(getInteger(rs, "COD_USUARIO"));
            	vo.setDataAlteracao(getDate(rs, "DT_ULT_ALT"));
            	lista.add(vo);
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter a lista do calendario de bonus.", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return lista;
    }

    /**
     * 
     * @param ano
     * @param mes
     * @return
     * @throws PersistenciaException
     */
    public CalendarioBonusVO obtemCalendarioBonus(Integer ano, Integer mes) throws PersistenciaException {

        StringBuffer query = new StringBuffer(
        		" SELECT NUM_ANO, NUM_MES, DESCR_MES, DT_INI_PERIODO, DT_FIM_PERIODO, COD_USUARIO, DT_ULT_ALT " +
        		" FROM SRV_CALENDARIO_BONUS WHERE NUM_ANO = ? AND NUM_MES = ? ");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        CalendarioBonusVO item = null;

        try {
            conn = getConn();
            stmt = conn.prepareStatement(query.toString());

            int i = 1;
            setInteger(stmt, i++, ano);
      		setInteger(stmt, i++, mes);

            rs = stmt.executeQuery();
                        
            if (rs.next()) {
            	item = new CalendarioBonusVO();
            	item.setAno(getInteger(rs, "NUM_ANO"));
            	item.setMes(getInteger(rs, "NUM_MES"));
            	item.setDataInicial(getDate(rs, "DT_INI_PERIODO"));
            	item.setDataFinal(getDate(rs, "DT_FIM_PERIODO"));
            	item.setDescricaoMes(getString(rs, "DESCR_MES"));
            	item.setCodUsuario(getInteger(rs, "COD_USUARIO"));
            	item.setDataAlteracao(getTimestamp(rs, "DT_ULT_ALT"));
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter o calendario de bonus.", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return item;
    }

    /**
     * 
     * @return
     * @throws PersistenciaException
     */
	public List<Integer> obtemAnosCalendarioBonus() throws PersistenciaException {
		String query =  " SELECT DISTINCT NUM_ANO FROM SRV_CALENDARIO_BONUS ORDER BY NUM_ANO DESC ";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Integer> lista  = new ArrayList<Integer>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			rs = stmt.executeQuery();

			while (rs.next()) {
				lista.add(getInteger(rs, "NUM_ANO"));
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista dos anos do calendario bonus." , e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return lista;

	}

    /**
     * 
     * @return
     * @throws PersistenciaException
     */
	public List<CalendarioBonusVO> obtemMesesCalendarioBonus() throws PersistenciaException {
		String query =  " SELECT DISTINCT NUM_MES FROM SRV_CALENDARIO_BONUS ORDER BY NUM_MES ";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<CalendarioBonusVO> lista  = new ArrayList<CalendarioBonusVO>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			rs = stmt.executeQuery();

			while (rs.next()) {
				CalendarioBonusVO vo = new CalendarioBonusVO();
				vo.setMes(getInteger(rs, "NUM_MES"));
				lista.add(vo);
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista dos meses do calendario bonus." , e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return lista;

	}

    /**
     * 
     * @param paramDate
     * @param ano
     * @param mes
     * @return
     * @throws PersistenciaException
     */
    public Boolean existeIntervaloCalendarioBonus(Date paramDate, Integer ano, Integer mes) throws PersistenciaException {

        StringBuffer query = new StringBuffer(" SELECT NUM_ANO, NUM_MES, DESCR_MES, DT_INI_PERIODO, DT_FIM_PERIODO FROM SRV_CALENDARIO_BONUS WHERE ? BETWEEN DT_INI_PERIODO AND DT_FIM_PERIODO ");

    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Boolean existe = false;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query.toString());

            int i = 1;
       		setDate(stmt, i++, paramDate);
            rs = stmt.executeQuery();
                        
            while (rs.next()) {
        		existe = true;
            	if ( ObjectHelper.isNotEmpty(ano) && ObjectHelper.isNotEmpty(mes) ) {
                	Integer rsAno = getInteger(rs, "NUM_ANO");
                	Integer rsMes = getInteger(rs, "NUM_MES");
                	if ( rsAno.intValue() == ano.intValue() && rsMes.intValue() == mes.intValue() ) {
                		existe = false;
                	}
            	}
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível validar o período do calendario bonus.", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return existe;
    }

	/**
	 * 
	 * @param paramVO
	 * @throws PersistenciaException
	 */
	public void incluiCalendarioBonus(CalendarioBonusVO paramVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				"	INSERT INTO SRV_CALENDARIO_BONUS (NUM_MES, NUM_ANO, DT_INI_PERIODO, DT_FIM_PERIODO, DESCR_MES, COD_USUARIO, DT_ULT_ALT) " +
				"	VALUES (?, ?, ?, ?, ?, ?, SYSDATE) ");

		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getMes()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getAno()));
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE, paramVO.getDataInicial()));
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE, paramVO.getDataFinal()));
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, paramVO.getDescricaoMes().toUpperCase()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getCodUsuario()));

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível incluir o calendário bonus.", e);
		} finally {
			closeStatement(stmt);
		}
	}		

	/**
	 * 
	 * @param paramVO
	 * @throws PersistenciaException
	 */
	public void alteraCalendarioBonus(CalendarioBonusVO paramVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				"	UPDATE SRV_CALENDARIO_BONUS SET " +
				"		DESCR_MES = ?, " +
				"		DT_INI_PERIODO = ?, " +
				"		DT_FIM_PERIODO = ?,  " +
				"		COD_USUARIO = ?,  " +
				"		DT_ULT_ALT = SYSDATE  " +
				"	WHERE " +
				"		NUM_MES = ? " +
				"	AND NUM_ANO = ? ");
				 
		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, paramVO.getDescricaoMes().toUpperCase()));
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE, paramVO.getDataInicial()));
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE, paramVO.getDataFinal()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getCodUsuario()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getMes()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getAno()));

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível alterar o calendário bonus.", e);
		} finally {
			closeStatement(stmt);
		}
	}		

	/**
	 * 
	 * @param paramVO
	 * @throws PersistenciaException
	 */
	public void excluiCalendarioBonus(CalendarioBonusVO paramVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				"	DELETE FROM SRV_CALENDARIO_BONUS " +
				"	WHERE NUM_MES = ? " +
				"	AND NUM_ANO = ? ");

		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getMes()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getAno()));

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível excluir o calendário bonus.", e);
		} finally {
			closeStatement(stmt);
		}
	}		

	/**
	 * Gera historico de calendario bonus
	 * @param ano
	 * @param mes
	 * @param codUsuario
	 * @throws PersistenciaException
	 */
	public void incluiCalendarioBonusHistorico(Integer ano, Integer mes, Integer codUsuario) throws PersistenciaException {

		CalendarioBonusVO calBonusVO = obtemCalendarioBonus(ano, mes);

		StringBuffer query =  new StringBuffer(
				" INSERT INTO SRV_CALENDARIO_BONUS_HIST " +
				"	(NUM_SEQ, NUM_ANO, NUM_MES, DESCR_MES, DT_INI_PERIODO, DT_FIM_PERIODO, COD_USUARIO, DT_ULT_ALT, COD_USUARIO_HIST, DT_PROC_HIST)	" +
				" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE) ");

		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, obtemProximaSequenciaHistorio(ano, mes)));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, calBonusVO.getAno()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, calBonusVO.getMes()));
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, calBonusVO.getDescricaoMes().toUpperCase()));
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE, calBonusVO.getDataInicial()));
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE, calBonusVO.getDataFinal()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, calBonusVO.getCodUsuario()));
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE, calBonusVO.getDataAlteracao()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, codUsuario));

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível incluir o historico de calendario bonus.", e);
		} finally {
			closeStatement(stmt);
		}

	}

	/**
	 * 
	 * @param ano
	 * @param mes
	 * @return
	 * @throws PersistenciaException
	 */
	private int obtemProximaSequenciaHistorio(Integer ano, Integer mes) throws PersistenciaException {

		String query = " SELECT MAX(NUM_SEQ) FROM SRV_CALENDARIO_BONUS_HIST WHERE NUM_ANO = ? AND NUM_MES = ? ";

		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, ano));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, mes));

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int proximaSequencia = 1;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			preencheParametros(stmt, parametros);
			rs = stmt.executeQuery();

			if (rs.next()) {
				proximaSequencia = getInt(rs, 1) + 1;
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a proxima sequencia de historico do calendário bonus", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return proximaSequencia;
	}

}