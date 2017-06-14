package br.com.marisa.srv.demissao.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import br.com.marisa.srv.demissao.vo.MotivoDemissaoVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;

public class MotivoDemissaoDAO extends BasicDAO {

	private static final Logger log = Logger.getLogger(MotivoDemissaoDAO.class);

	public MotivoDemissaoVO obtemMotivoDemissao(Integer codDemissao) throws PersistenciaException {

		StringBuffer query = new StringBuffer("SELECT * FROM SRV_MOT_DEMISSAO WHERE COD_MOT_DEMISSAO = ? ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		MotivoDemissaoVO motivoDemissaoVO = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 0;
			setInteger(stmt, ++i, codDemissao);
			rs = stmt.executeQuery();

			while (rs.next()) {
				motivoDemissaoVO = new MotivoDemissaoVO();
				motivoDemissaoVO.setCodMotDemissao(getLong(rs, "COD_MOT_DEMISSAO"));
				motivoDemissaoVO.setDscMotDemissao(getString(rs, "DESCR_MOT_DEMISSAO"));
				motivoDemissaoVO.setOrigemDemissao(getString(rs, "ORIGEM_DEMISSAO"));
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter o motivo de demissao ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return motivoDemissaoVO;
	}

}