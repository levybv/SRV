package br.com.marisa.srv.agendamento.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.agendamento.vo.AgendamentoVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.dao.ParametroSQL;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.helper.ObjectHelper;


/**
 * @author Levy Villar
 */
public class AgendamentoDAO extends BasicDAO {

    //Log4J
    private static final Logger log = Logger.getLogger(AgendamentoDAO.class);
    
    /**
     */
    public List<AgendamentoVO> obtemAgendamento(AgendamentoVO paramVO) throws PersistenciaException {

        StringBuffer query = new StringBuffer(" SELECT * FROM SRV_AGENDAMENTO WHERE (1=1) ");

        if ( ObjectHelper.isNotNull(paramVO) ) {
        	if ( ObjectHelper.isNotEmpty(paramVO.getCodigoCarga()) ) {
        		query.append(" AND COD_CARGA = ? ");
        	}
        	if ( ObjectHelper.isNotEmpty(paramVO.getDescricaoCarga()) ) {
        		query.append(" AND UPPER(DESCR_CARGA) LIKE ? ");
        	}
        	if ( ObjectHelper.isNotEmpty(paramVO.getDataAgendamento()) ) {
        		query.append(" AND DT_AGENDAMENTO = ? ");
        	}
        	if ( ObjectHelper.isNotEmpty(paramVO.getDataUltimoProcessamento()) ) {
        		query.append(" AND DT_ULT_PROC = ? ");
        	}
        	if ( ObjectHelper.isNotEmpty(paramVO.getMes()) ) {
        		query.append(" AND NUM_MES = ? ");
        	}
        	if ( ObjectHelper.isNotEmpty(paramVO.getAno()) ) {
        		query.append(" AND NUM_ANO = ? ");
        	}
        	if ( ObjectHelper.isNotNull(paramVO.getFlagAtiva()) ) {
        		query.append(" AND FLG_ATIVA = ? ");
        	}
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<AgendamentoVO> lista = new ArrayList<AgendamentoVO>();

        try {

            conn = getConn();
            stmt = conn.prepareStatement(query.toString());

            int i = 1;
            if ( ObjectHelper.isNotNull(paramVO) ) {
            	if ( ObjectHelper.isNotEmpty(paramVO.getCodigoCarga()) ) {
            		setInteger(stmt, i++, paramVO.getCodigoCarga());
            	}
            	if ( ObjectHelper.isNotEmpty(paramVO.getDescricaoCarga()) ) {
            		setString(stmt, i++, "%"+paramVO.getDescricaoCarga().toUpperCase()+"%");
            	}
            	if ( ObjectHelper.isNotEmpty(paramVO.getDataAgendamento()) ) {
            		setDate(stmt, i++, paramVO.getDataAgendamento());
            	}
            	if ( ObjectHelper.isNotEmpty(paramVO.getDataUltimoProcessamento()) ) {
            		setDate(stmt, i++, paramVO.getDataUltimoProcessamento());
            	}
            	if ( ObjectHelper.isNotEmpty(paramVO.getMes()) ) {
            		setInteger(stmt, i++, paramVO.getMes());
            	}
            	if ( ObjectHelper.isNotEmpty(paramVO.getAno()) ) {
            		setInteger(stmt, i++, paramVO.getAno());
            	}
            	if ( ObjectHelper.isNotNull(paramVO.getFlagAtiva()) ) {
            		setInteger(stmt, i++, paramVO.getFlagAtiva());
            	}
            }

            rs = stmt.executeQuery();
            
            while (rs.next()) {
            	AgendamentoVO vo = new AgendamentoVO();
            	vo.setCodigoCarga(getInteger(rs, "COD_CARGA"));
            	vo.setDescricaoCarga(getString(rs, "DESCR_CARGA"));
            	vo.setDataAgendamento(getTimestamp(rs, "DT_AGENDAMENTO"));
            	vo.setDataUltimoProcessamento(getTimestamp(rs, "DT_ULT_PROC"));
            	//vo.setDataLimiteProcessamento(getTimestamp(rs, "DT_LIMITE_PROC"));
            	vo.setAno(getInteger(rs, "NUM_ANO"));
            	vo.setMes(getInteger(rs, "NUM_MES"));
            	vo.setNumeroArquivo(getInteger(rs, "NUM_ARQ"));
            	vo.setNomeArquivo(getString(rs, "NOME_ARQ"));
            	vo.setDiretorioDestino(getString(rs, "DIR_DESTINO"));
            	vo.setDiretorioOrigem(getString(rs, "DIR_ORIGEM"));
            	vo.setFlagAtiva(getInteger(rs, "FLG_ATIVA"));
            	lista.add(vo);
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter a lista de agendamentos ", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return lista;
    }

	/**
	 */
	public void alteraAgendamento(AgendamentoVO paramVO) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
				"	UPDATE SRV_AGENDAMENTO SET " +
				"		DT_AGENDAMENTO = ?, " +
				//"		DT_LIMITE_PROC = (? + 3), " +
				"		FLG_ATIVA = ?, " +
				"		NUM_ANO = ?, " +
				"		NUM_MES = ? " +
				"	WHERE " +
				"		COD_CARGA = ? ");

		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
		parametros.add(new ParametroSQL(PARAMTYPE_DATE, paramVO.getDataAgendamento()));
		//parametros.add(new ParametroSQL(PARAMTYPE_DATE, paramVO.getDataAgendamento()));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getFlagAtiva()));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getAno()));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getMes()));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, paramVO.getCodigoCarga()));

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível alterar o agendamento de carga.", e);
		} finally {
			closeStatement(stmt);
		}
	}

	/**
	 * 
	 * @throws PersistenciaException
	 */
	public void reprocessaAgendamento() throws PersistenciaException {

		Connection conn = null;
		CallableStatement ctmt = null;

		try {
			conn = getConn();
			ctmt = conn.prepareCall("{ call prc_srv_processamento() }");
			ctmt.executeUpdate();

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível reprocessar o agendamento de carga.", e);
		} finally {
			closeStatement(ctmt);
		}
	}

	/**
	 * 
	 * @throws PersistenciaException
	 */
	public void reprocessaCargaArquivos() throws PersistenciaException {

		Connection conn = null;
		CallableStatement ctmt = null;

		try {
			conn = getConn();
			ctmt = conn.prepareCall("{ call pkg_srv_carga_base.prc_executa_job }");
			ctmt.executeUpdate();

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível efetuar a carga de arquivos.", e);
		} finally {
			closeStatement(ctmt);
		}
	}

}