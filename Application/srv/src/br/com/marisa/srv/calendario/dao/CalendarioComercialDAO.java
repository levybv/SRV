package br.com.marisa.srv.calendario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.calendario.vo.PeriodoCalendarioVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.dao.ParametroSQL;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.indicador.vo.IndicadorPeriodoVO;


/**
 * Classe para tratar dos métodos de acesso a dados às calendário 
 * 
 * @author Walter Fontes
 */
public class CalendarioComercialDAO extends BasicDAO {

    //Log4J
    private static final Logger log = Logger.getLogger(CalendarioComercialDAO.class);
    
    /**
     * Obtém periodos de um calendário de um ano específico
     * 
     * @param ano
     * @return
     */
    public List<PeriodoCalendarioVO> obtemPeriodosCalendario(int ano) throws PersistenciaException {

        String query =  "SELECT COD_PERIODO, NUM_ANO, NUM_MES, DT_INI_PERIODO, DT_FIM_PERIODO " +
                        "  FROM SRV_CALENDARIO_COMERCIAL " +
                        " WHERE NUM_ANO =? " +
        				" ORDER BY COD_PERIODO";
            
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<PeriodoCalendarioVO> periodos = new ArrayList<PeriodoCalendarioVO>();
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            setInteger(stmt, ordemCampos++, ano);
            rs = stmt.executeQuery();
                        
            while (rs.next()) {
            	PeriodoCalendarioVO periodoCalendarioVO = new PeriodoCalendarioVO();
            	periodoCalendarioVO.setPeriodo(getInteger(rs, "COD_PERIODO"));
            	periodoCalendarioVO.setAno(getInteger(rs, "NUM_ANO"));
            	periodoCalendarioVO.setMes(getInteger(rs, "NUM_MES"));
            	periodoCalendarioVO.setDataInicial(getDate(rs, "DT_INI_PERIODO"));
            	periodoCalendarioVO.setDataFinal(getDate(rs, "DT_FIM_PERIODO"));
            	periodos.add(periodoCalendarioVO);
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter os periodos do calendario do ano " + ano, e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return periodos;
    }

    /**
     * 
     * @param ano
     * @param codPeriodo
     * @return
     * @throws PersistenciaException
     */
	public PeriodoCalendarioVO obtemPeriodoCalendario(Integer ano, Integer codPeriodo) throws PersistenciaException {

		String query = "SELECT COD_PERIODO, NUM_ANO, NUM_MES, DT_INI_PERIODO, DT_FIM_PERIODO, COD_USUARIO, DT_ULT_ALT "
				+ "  FROM SRV_CALENDARIO_COMERCIAL WHERE NUM_ANO = ? AND COD_PERIODO = ? ";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PeriodoCalendarioVO item = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);

			int ordemCampos = 1;
			setInteger(stmt, ordemCampos++, ano);
			setInteger(stmt, ordemCampos++, codPeriodo);

			rs = stmt.executeQuery();

			if (rs.next()) {
				item = new PeriodoCalendarioVO();
				item.setPeriodo(getInteger(rs, "COD_PERIODO"));
				item.setAno(getInteger(rs, "NUM_ANO"));
				item.setMes(getInteger(rs, "NUM_MES"));
				item.setDataInicial(getDate(rs, "DT_INI_PERIODO"));
				item.setDataFinal(getDate(rs, "DT_FIM_PERIODO"));
				item.setCodUsuario(getInteger(rs, "COD_USUARIO"));
				item.setDataAlteracao(getTimestamp(rs, "DT_ULT_ALT"));
			}

		} catch (Exception e) {
			throw new PersistenciaException(log,"Não foi possível obter o periodo do calendario do ano ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return item;
	}

    /**
     * obtem lista dos anos do calendario
     * @return
     * @throws PersistenciaException
     */
	public List<Integer> obtemAnosCalendario() throws PersistenciaException {
		String query =  "SELECT DISTINCT(NUM_ANO) NUM_ANO " +
				        "  FROM SRV_CALENDARIO_COMERCIAL " +
						" ORDER BY NUM_ANO DESC ";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Integer> lista  = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			rs = stmt.executeQuery();
			lista = new ArrayList<Integer>();        

			while (rs.next()) {
				lista.add(getInteger(rs, "NUM_ANO"));
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista dos anos" , e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return lista;

	}
	
	
    

    /**
     * Obtém periodos acessíveis
     * 
     * @return
     * @throws PersistenciaException
     */
	public List<IndicadorPeriodoVO> obtemListaPeriodos()  throws PersistenciaException {
		
		 StringBuffer query =  new StringBuffer(
				" SELECT   NUM_ANO, NUM_MES           " +
		 		" FROM     SRV_CALENDARIO_COMERCIAL   " +
		 		" ORDER BY NUM_ANO DESC, NUM_MES DESC " );

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IndicadorPeriodoVO> lista = new ArrayList<IndicadorPeriodoVO>();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			
			rs = stmt.executeQuery();
		         
			while (rs.next()) {
				IndicadorPeriodoVO indicadorPeriodoVO = new IndicadorPeriodoVO();
				indicadorPeriodoVO.setAno(getInteger(rs, "NUM_ANO"));
				indicadorPeriodoVO.setMes(getInteger(rs, "NUM_MES"));
				lista.add(indicadorPeriodoVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de periodos ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return lista;
	}	

	public List<PeriodoCalendarioVO> obtemListaPeriodoMesAno() throws PersistenciaException {

		List<PeriodoCalendarioVO> listaPeriodos = new ArrayList<PeriodoCalendarioVO>();

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		StringBuffer query = new StringBuffer();
		query.append(
				"select c.descr_mes || '/' || c.num_ano as periodo, " +
				"       c.num_mes as mes, " +
				"       c.num_ano as ano " +
				"  from srv_calendario_bonus c " +
				" order by num_ano ");

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			PeriodoCalendarioVO calendario;

			for (rs = stmt.executeQuery(); rs.next(); listaPeriodos.add(calendario)) {
				calendario = new PeriodoCalendarioVO();
				calendario.setPeriodoMesAno(getString(rs, "periodo"));
				calendario.setMesAno(getString(rs, "ano") + "00" + getString(rs, "mes"));
				calendario.setAno(getInteger(rs, "ano"));
				calendario.setMes(getInteger(rs, "mes"));
			}
		} catch(Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de periodos", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return listaPeriodos;

	}

	public List<IndicadorPeriodoVO> obtemCalendarioComercialListaMes(boolean isOrderByDesc) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
				" SELECT DISTINCT NUM_MES FROM SRV_CALENDARIO_COMERCIAL ORDER BY NUM_MES ");

		if (isOrderByDesc) {
			query.append(" DESC ");
		}

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IndicadorPeriodoVO>  lista = new ArrayList<IndicadorPeriodoVO> ();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());

			rs = stmt.executeQuery();

			while (rs.next()) {
				IndicadorPeriodoVO indicadorPeriodoVO = new IndicadorPeriodoVO();
				indicadorPeriodoVO.setMes(getInteger(rs, "NUM_MES"));
				lista.add(indicadorPeriodoVO);
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de meses (calendario comercial)  ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return lista;
	}

	public List<IndicadorPeriodoVO> obtemCalendarioComercialListaAno(boolean isOrderByDesc) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
				" SELECT DISTINCT NUM_ANO FROM SRV_CALENDARIO_COMERCIAL ORDER BY NUM_ANO ");

		if (isOrderByDesc) {
			query.append(" DESC ");
		}

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IndicadorPeriodoVO> lista = new ArrayList<IndicadorPeriodoVO>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());

			rs = stmt.executeQuery();

			while (rs.next()) {
				IndicadorPeriodoVO indicadorPeriodoVO = new IndicadorPeriodoVO();
				indicadorPeriodoVO.setAno(getInteger(rs, "NUM_ANO"));
				lista.add(indicadorPeriodoVO);
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de anos (calendario comercial)  ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return lista;
	}

    /**
     * 
     * @param codPeriodo
     * @param ano
     * @return
     * @throws PersistenciaException
     */
    public PeriodoCalendarioVO obtemCalendarioComercial(Integer codPeriodo, Integer ano) throws PersistenciaException {

        String query =  "SELECT COD_PERIODO, NUM_ANO, NUM_MES, DT_INI_PERIODO, DT_FIM_PERIODO, COD_USUARIO, DT_ULT_ALT " +
                        "  FROM SRV_CALENDARIO_COMERCIAL " +
                        " WHERE NUM_ANO = ? AND COD_PERIODO = ? ";
            
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PeriodoCalendarioVO item = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            setInteger(stmt, ordemCampos++, ano);
            setInteger(stmt, ordemCampos++, codPeriodo);
            rs = stmt.executeQuery();

            if (rs.next()) {
            	item = new PeriodoCalendarioVO();
            	item.setPeriodo(getInteger(rs, "COD_PERIODO"));
            	item.setAno(getInteger(rs, "NUM_ANO"));
            	item.setMes(getInteger(rs, "NUM_MES"));
            	item.setDataInicial(getDate(rs, "DT_INI_PERIODO"));
            	item.setDataFinal(getDate(rs, "DT_FIM_PERIODO"));
            	item.setDataAlteracao(getTimestamp(rs, "DT_ULT_ALT"));
            	item.setCodUsuario(getInteger(rs, "COD_USUARIO"));
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter o calendario comercial", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return item;
    }

	/**
	 */
	public void alteraCalendarioComercial(PeriodoCalendarioVO paramVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				"	UPDATE SRV_CALENDARIO_COMERCIAL SET " +
				"		NUM_MES = ?, " +
				"		DT_INI_PERIODO = ?, " +
				"		DT_FIM_PERIODO = ?, " +
				"		COD_USUARIO = ?, " +
				"		DT_ULT_ALT = SYSDATE " +
				"	WHERE " +
				"		COD_PERIODO = ? " +
				"	AND NUM_ANO = ? ");
				 
		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getMes()));
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE, paramVO.getDataInicial()));
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE, paramVO.getDataFinal()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getCodUsuario()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getPeriodo()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getAno()));

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível alterar o calendário comercial.", e);
		} finally {
			closeStatement(stmt);
		}
	}		

	/**
	 */
	public void incluiCalendarioComercial(PeriodoCalendarioVO paramVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				"	INSERT INTO SRV_CALENDARIO_COMERCIAL (NUM_MES, NUM_ANO, DT_INI_PERIODO, DT_FIM_PERIODO, COD_PERIODO, COD_USUARIO, DT_ULT_ALT) " +
				"	VALUES (?, ?, ?, ?, ?, ?, SYSDATE) ");
				 
		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getMes()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getAno()));
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE, paramVO.getDataInicial()));
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE, paramVO.getDataFinal()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getPeriodo()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getCodUsuario()));

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível incluir o calendário comercial.", e);
		} finally {
			closeStatement(stmt);
		}
	}		

	/**
	 */
	public void excluiCalendarioComercial(PeriodoCalendarioVO paramVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				"	DELETE FROM SRV_CALENDARIO_COMERCIAL " +
				"	WHERE " +
				"		COD_PERIODO = ? " +
				"	AND NUM_ANO = ? ");
				 
		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getPeriodo()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getAno()));

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível excluir o calendário comercial.", e);
		} finally {
			closeStatement(stmt);
		}
	}		

	/**
	 * Gera historico de calendario comercial
	 * @param ano
	 * @param codPeriodo
	 * @param codUsuario
	 * @throws PersistenciaException
	 */
	public void incluiCalendarioComercialHistorico(Integer ano, Integer codPeriodo, Integer codUsuario) throws PersistenciaException {

		PeriodoCalendarioVO paramVO = obtemCalendarioComercial(codPeriodo, ano);

		StringBuffer query =  new StringBuffer(
				"	INSERT INTO SRV_CALENDARIO_COMERCIAL_HIST (NUM_SEQ, COD_PERIODO, NUM_ANO, NUM_MES, DT_INI_PERIODO, DT_FIM_PERIODO, COD_USUARIO, DT_ULT_ALT, COD_USUARIO_HIST, DT_PROC_HIST) " +
				"	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE) ");
				 
		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, obtemProximaSequenciaHistorio(codPeriodo, ano)));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getPeriodo()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getAno()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getMes()));
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE, paramVO.getDataInicial()));
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE, paramVO.getDataFinal()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getCodUsuario()));
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE, paramVO.getDataAlteracao()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, codUsuario));

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível incluir o historico de calendário comercial.", e);
		} finally {
			closeStatement(stmt);
		}

	}

	/**
	 * 
	 * @param codPeriodo
	 * @param ano
	 * @return
	 * @throws PersistenciaException
	 */
	private int obtemProximaSequenciaHistorio(Integer codPeriodo, Integer ano) throws PersistenciaException {

		String query = " SELECT MAX(NUM_SEQ) FROM SRV_CALENDARIO_COMERCIAL_HIST WHERE COD_PERIODO = ? AND NUM_ANO = ? ";

		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, codPeriodo));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, ano));

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
			throw new PersistenciaException(log, "Não foi possível obter a proxima sequencia de historico do calendario comercial", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return proximaSequencia;
	}

}