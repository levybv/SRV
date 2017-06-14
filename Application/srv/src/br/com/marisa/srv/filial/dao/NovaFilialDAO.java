package br.com.marisa.srv.filial.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.filial.vo.NovoFilialVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;

public class NovaFilialDAO extends BasicDAO {

	private static final Logger log = Logger.getLogger(br.com.marisa.srv.filial.dao.NovaFilialDAO.class);

    public NovaFilialDAO() {
    	super();
	}

	public List montaTelaInicial(int codEmpresa, Integer codFilial, Integer codTipoFilial, String isAtivo) throws PersistenciaException {

		List listaFiliais;
		listaFiliais = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer(
				"SELECT FIL.COD_EMP, " +
				"       FIL.COD_FIL, " +
				"       FIL.DESCR_FIL, " +
				"       FIL.NUM_CNPJ, " +
				"       FIL.COD_UF, " +
				"       FIL.FLG _ATIV, " +
				"       FIL.FLG_META_100_PCT_REALZ, " +
				"       FIL.DT_INI_SIT_SRV, " +
				"       FIL.COD_USUARIO, " +
				"       TPFIL.DESCR_TIPO_FIL " +
				"  FROM SRV_FILIAL FIL, SRV_TIPO_FILIAL TPFIL " +
				" WHERE TPFIL.COD_TIP O_FIL = FIL.COD_TIPO_FIL " +
				"   AND COD_EMP = ? ");
		if(codFilial != null) {
			query.append(" AND COD_FIL = ? ");
		}
		if(codTipoFilial != null) {
			query.append(" AND COD_TIPO_FIL = ? ");
		}
		if(isAtivo != null) {
			query.append(" AND FLG_ATIV = ? ");
		}
		query.append(" ORDER BY COD_FIL ");
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			setInteger(stmt, 1, codEmpresa != -1 ? codEmpresa : 1);
			if(codFilial != null) {
				setInteger(stmt, 2, codFilial);
			}
			if(codTipoFilial != null) {
				setInteger(stmt, 3, codTipoFilial);
			}
			if(isAtivo != null) {
				setBoolean(stmt, 4, isAtivo);
			}
			for(rs = stmt.executeQuery(); rs.next(); listaFiliais.add(listaFiliais)) {
				NovoFilialVO filialVO = new NovoFilialVO();
				filialVO.setCodEmpresa(getInteger(rs, "COD_EMP"));
				filialVO.setCodFilial(getInteger(rs, "COD_FIL"));
				filialVO.setDescricao(getString(rs, "DESCR_FIL"));
				filialVO.setCnpj(getString(rs, "NUM_CNPJ"));
				filialVO.setUf(getString(rs, "COD_UF"));
				filialVO.setFlagAtivo(getBoolean(rs, "FLG_ATIV"));
				filialVO.setFlagMeta100(getBoolean(rs, "FLG_META_100_PCT_REALZ"));
				filialVO.setDataUltimaAlteracao(getDate(rs, "DT_INI_SIT_SRV"));
				filialVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
				filialVO.setDescricaoTpFil(getString(rs, "DESCR_TIPO_FIL"));
			}
		
		} catch(Exception e) {
			throw new PersistenciaException(log, "N\343o foi poss\355vel obter a lista de filiais ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return listaFiliais;
	}

}
