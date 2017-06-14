package br.com.marisa.srv.gpremuneracaovariavel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.gpremuneracaovariavel.vo.GrupoRemuneracaoVariavelVO;


/**
 * Classe para tratar dos métodos de acesso a dados de Grupos de Remuneração Variável
 * 
 * @author Walter Fontes
 */
public class GrupoRemuneracaoVariavelDAO extends BasicDAO {

    //Log4J
	private static final Logger log = Logger.getLogger(GrupoRemuneracaoVariavelDAO.class);

	/**
	 * 
	 * @param pesquisaVO
	 * @return
	 * @throws PersistenciaException
	 */
	public List<GrupoRemuneracaoVariavelVO> obtemListaGrupoRemuneracaoVariavel(GrupoRemuneracaoVariavelVO pesquisaVO) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
			" SELECT COD_GRP_REM_VAR, " +
			"        DESCR_GRP_REM_VAR, " +
			"        DESCR_GRP_REM_VAR_ONLINE, " +
			"        QTD_SALARIO_MIN, " +
			"        QTD_SALARIO_MAX, " +
			"        PCT_MIN_ATING, " +
			"        COD_USUARIO, " +
			"        DT_INI_SIT_SRV " +
			"   FROM SRV_GRUPO_REM_VARIAVEL " +
			"  WHERE 1 = 1 ");

		if (ObjectHelper.isNotNull(pesquisaVO)) {
			if (ObjectHelper.isNotEmpty(pesquisaVO.getIdGrupoRemuneracao())) {
				query.append("    AND COD_GRP_REM_VAR = ? ");
			}
			if (ObjectHelper.isNotEmpty(pesquisaVO.getDescricao())) {
				query.append("    AND TRIM(UPPER(DESCR_GRP_REM_VAR)) LIKE '%'||?||'%' ");
			}
			if (ObjectHelper.isNotEmpty(pesquisaVO.getDescricaoOnline())) {
				query.append("    AND TRIM(UPPER(DESCR_GRP_REM_VAR_ONLINE)) LIKE '%'||?||'%' ");
			}
		}
		query.append("  ORDER BY DESCR_GRP_REM_VAR ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<GrupoRemuneracaoVariavelVO> listaGrpRemVar = new ArrayList<GrupoRemuneracaoVariavelVO>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 1;
			if (ObjectHelper.isNotNull(pesquisaVO)) {
				if (ObjectHelper.isNotEmpty(pesquisaVO.getIdGrupoRemuneracao())) {
					setInteger(stmt, i++, pesquisaVO.getIdGrupoRemuneracao());
				}
				if (ObjectHelper.isNotEmpty(pesquisaVO.getDescricao())) {
					setString(stmt, i++, pesquisaVO.getDescricao().trim().toUpperCase());
				}
				if (ObjectHelper.isNotEmpty(pesquisaVO.getDescricaoOnline())) {
					setString(stmt, i++, pesquisaVO.getDescricaoOnline().trim().toUpperCase());
				}
			}
			rs = stmt.executeQuery();

			while (rs.next()) {
				GrupoRemuneracaoVariavelVO grupoRemuneracaoVariavelVO = new GrupoRemuneracaoVariavelVO();
				grupoRemuneracaoVariavelVO.setIdGrupoRemuneracao(getInteger(rs, "COD_GRP_REM_VAR"));
				grupoRemuneracaoVariavelVO.setDescricao(getString(rs, "DESCR_GRP_REM_VAR").trim());
				grupoRemuneracaoVariavelVO.setDescricaoOnline(getString(rs, "DESCR_GRP_REM_VAR_ONLINE").trim());
				grupoRemuneracaoVariavelVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				grupoRemuneracaoVariavelVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
				listaGrpRemVar.add(grupoRemuneracaoVariavelVO);
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista dos grupos de remuneração variável: ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return listaGrpRemVar;
	}

    /**
     * Obtém grupos de remuneração variável
     * 
     * @param idGrupoRemuneracao
     * @param descricao
     * @return
     */
    public List<GrupoRemuneracaoVariavelVO> obtemGruposRemuneracaoVariavel(int idGrupoRemuneracao, String descricao) throws PersistenciaException {

        String query =  "SELECT COD_GRP_REM_VAR, DESCR_GRP_REM_VAR, DESCR_GRP_REM_VAR_ONLINE, DT_INI_SIT_SRV, COD_USUARIO " +
                        "  FROM SRV_GRUPO_REM_VARIAVEL              ";
        
        if (idGrupoRemuneracao > 0 || !ObjectHelper.isEmpty(descricao)) {
        	query += " WHERE ";
        }
        if (idGrupoRemuneracao > 0) {
        	query += " COD_GRP_REM_VAR = ? ";
        }
        if (!ObjectHelper.isEmpty(descricao)) {
        	 if (idGrupoRemuneracao > 0) {
        		query += " AND ";
        	}
        	query += " trim(upper(DESCR_GRP_REM_VAR_ONLINE)) LIKE '%'||?||'%' ";
        }
        query += " ORDER BY DESCR_GRP_REM_VAR";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<GrupoRemuneracaoVariavelVO> gruposRemuneracao = new ArrayList<GrupoRemuneracaoVariavelVO>();
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int nroCampo = 1;
            if (idGrupoRemuneracao > 0) {
            	setInteger(stmt, nroCampo++, idGrupoRemuneracao);
            }
            if (!ObjectHelper.isEmpty(descricao)) {
            	setString(stmt, nroCampo++, descricao.toUpperCase().trim());
            }
            rs = stmt.executeQuery();
            GrupoRemuneracaoVariavelVO grupoRemuneracaoVariavelVO = null;
            
            while (rs.next()) {
            	grupoRemuneracaoVariavelVO = new GrupoRemuneracaoVariavelVO();
            	grupoRemuneracaoVariavelVO.setIdGrupoRemuneracao(getInteger(rs, "COD_GRP_REM_VAR"));
            	grupoRemuneracaoVariavelVO.setDescricao(getString(rs, "DESCR_GRP_REM_VAR").trim());
            	grupoRemuneracaoVariavelVO.setDescricaoOnline(getString(rs, "DESCR_GRP_REM_VAR_ONLINE").trim());
            	grupoRemuneracaoVariavelVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
            	grupoRemuneracaoVariavelVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
            	gruposRemuneracao.add(grupoRemuneracaoVariavelVO);
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter os grupos de remuneração variável.", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return gruposRemuneracao;
    }
    
    
    /**
     * Obtém grupo de remuneração variável
     * 
     * @param idGrupoRemuneracao
     * @return
     */
    public GrupoRemuneracaoVariavelVO obtemGrupoRemuneracaoVariavel(int idGrupoRemuneracao) throws PersistenciaException {

        String query =  "SELECT COD_GRP_REM_VAR, DESCR_GRP_REM_VAR, DESCR_GRP_REM_VAR_ONLINE, DT_INI_SIT_SRV, COD_USUARIO " +
                        "  FROM SRV_GRUPO_REM_VARIAVEL    " +
                        " WHERE COD_GRP_REM_VAR = ?       ";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        GrupoRemuneracaoVariavelVO grupoRemuneracaoVariavelVO = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            setInteger(stmt, 1, idGrupoRemuneracao);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
            	grupoRemuneracaoVariavelVO = new GrupoRemuneracaoVariavelVO();
            	grupoRemuneracaoVariavelVO.setIdGrupoRemuneracao(getInteger(rs, "COD_GRP_REM_VAR"));
            	grupoRemuneracaoVariavelVO.setDescricao(getString(rs, "DESCR_GRP_REM_VAR").trim());
            	grupoRemuneracaoVariavelVO.setDescricaoOnline(getString(rs, "DESCR_GRP_REM_VAR_ONLINE").trim());
            	grupoRemuneracaoVariavelVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
            	grupoRemuneracaoVariavelVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter o grupo de remuneracao " + idGrupoRemuneracao, e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return grupoRemuneracaoVariavelVO;
    }
    
    
    /**
     * Altera o grupo de remuneração variável
     * 
     * @param grupoRemuneracaoVariavelVO
     * @return
     */
    public void alteraGrupoRemuneracaoVariavel(GrupoRemuneracaoVariavelVO grupoRemuneracaoVariavelVO) throws PersistenciaException {

        String query =  "UPDATE SRV_GRUPO_REM_VARIAVEL         " +
        				"   SET DESCR_GRP_REM_VAR_ONLINE  = ?, " +
        				"		DT_INI_SIT_SRV 		      = ?, " +
        				"       COD_USUARIO 		      = ?  " +
        				" WHERE COD_GRP_REM_VAR           = ?  ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setString 	(stmt, ordemCampos++, grupoRemuneracaoVariavelVO.getDescricaoOnline().trim());
            setTimestamp(stmt, ordemCampos++, grupoRemuneracaoVariavelVO.getDataUltimaAlteracao());
            setInteger	(stmt, ordemCampos++, grupoRemuneracaoVariavelVO.getIdUsuarioUltimaAlteracao());
            setInteger  (stmt, ordemCampos++, grupoRemuneracaoVariavelVO.getIdGrupoRemuneracao());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível alterar o grupo de remuneração variável.", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }
    

    /**
     * Inclui a classe hay
     * 
     * @param classeHayVO
     * @return
     */
    public void incluiGrupoRemuneracaoVariavelHistorico(GrupoRemuneracaoVariavelVO grupoRemuneracaoVariavelVO) throws PersistenciaException {

        String query =  "INSERT INTO SRV_GRUPO_REM_VARIAVEL_HIST " +
						"      (COD_GRP_REM_VAR                , " +
						"       SEQ_GRP_REM_VAR 		       , " +
						"       DESCR_GRP_REM_VAR 		       , " +
						"       DESCR_GRP_REM_VAR_ONLINE       , " +
						"		DT_INI_SIT_SRV 		           , " +
						"		COD_USUARIO)  		             " +
				        " VALUES (?, ?, ?, ?, ?)                 ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setInteger 	(stmt, ordemCampos++, grupoRemuneracaoVariavelVO.getIdGrupoRemuneracao());
            setInteger	(stmt, ordemCampos++, obtemProximaSequenciaHistorio(grupoRemuneracaoVariavelVO.getIdGrupoRemuneracao().intValue()));
            setString 	(stmt, ordemCampos++, grupoRemuneracaoVariavelVO.getDescricao().trim());
            setString 	(stmt, ordemCampos++, grupoRemuneracaoVariavelVO.getDescricaoOnline().trim());
            setTimestamp(stmt, ordemCampos++, grupoRemuneracaoVariavelVO.getDataUltimaAlteracao());
            setInteger  (stmt, ordemCampos++, grupoRemuneracaoVariavelVO.getIdUsuarioUltimaAlteracao());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível incluir o historico do grupo de remuneracao variavel", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }      

    
    /**
     * Obtém próxima sequencia do histórico
     * 
     * @param idGrupoRemuneracao
     * @return
     */
    private int obtemProximaSequenciaHistorio(int idGrupoRemuneracao) throws PersistenciaException {

        String query =  "SELECT MAX(SEQ_GRP_REM_VAR)          " +
                        "  FROM SRV_GRUPO_REM_VARIAVEL_HIST   " +
                        " WHERE COD_GRP_REM_VAR =?            ";
            
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int proximaSequencia = 1;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            setInteger(stmt, ordemCampos++, idGrupoRemuneracao);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
            	proximaSequencia = getInt(rs, 1) + 1;
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter a proxima sequencia de historico do grupo de remuneracao " + idGrupoRemuneracao, e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return proximaSequencia;
    }
}