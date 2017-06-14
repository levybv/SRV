package br.com.marisa.srv.grupoindicador.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.grupoindicador.vo.GrupoIndicadorVO;


/**
 * Classe para tratar dos métodos de acesso a dados de grupos de indicadores 
 * 
 * @author Walter Fontes
 */
public class GrupoIndicadorDAO extends BasicDAO {

    //Log4J
    private static final Logger log = Logger.getLogger(GrupoIndicadorDAO.class);


	/**
	 * Obtém todos grupos de indicadores de um tipo de remuneração (se informado)
	 * 
	 * @param tipoRemuneracao
	 * @return
	 * @throws PersistenciaException
	 */
	public List obtemGruposIndicadores(String tipoRemuneracao) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" SELECT A.COD_GRP_INDIC, A.DESCR_GRP_INDIC, A.COD_TIPO_REM_VAR, B.DESCR_TIPO_REM_VAR, A.COD_USUARIO, A.DT_INI_SIT_SRV " +
				"   FROM SRV_GRUPO_INDICADOR A, SRV_TIPO_REM_VAR B    " +
				"  WHERE A.COD_TIPO_REM_VAR = B.COD_TIPO_REM_VAR      ");
				
		if (ObjectHelper.isNotEmpty(tipoRemuneracao)) {
			query.append(" AND B.DESCR_TIPO_REM_VAR = ? ");
		}
		query.append(" ORDER BY A.DESCR_GRP_INDIC ");
		
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List lista = new ArrayList();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			if (ObjectHelper.isNotEmpty(tipoRemuneracao)) {
				setString(stmt, 1, tipoRemuneracao);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				GrupoIndicadorVO grupoIndicadorVO =  new GrupoIndicadorVO();
				grupoIndicadorVO.setIdGrupoIndicador(getInteger(rs, "COD_GRP_INDIC"));
				grupoIndicadorVO.setDescricaoGrupoIndicador(getString(rs, "DESCR_GRP_INDIC"));
				grupoIndicadorVO.setIdTipoRemuneracao(getInteger(rs, "COD_TIPO_REM_VAR"));
				grupoIndicadorVO.setDescricaoTipoRemuneracao(getString(rs, "DESCR_TIPO_REM_VAR"));
				grupoIndicadorVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
				grupoIndicadorVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				lista.add(grupoIndicadorVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter lista de grupos de indicadores ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return lista;
	}	
}