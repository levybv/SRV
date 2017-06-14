package br.com.marisa.srv.util.tools;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.com.marisa.srv.relatorios.data.CelulaVO;

public final class Tools {

	public static List<Map<Object,Object>> montaRelatorio(ResultSet rs) throws SQLException {
		return montaRelatorio(rs, false);
	}

	public static List<Map<Object,Object>> montaRelatorio(ResultSet rs, boolean isTipado) throws SQLException {
		return montaRelatorio(rs, isTipado, false);
	}

	public static List<Map<Object,Object>> montaRelatorio(ResultSet rs, boolean isTipado, boolean isPlanilhaExcel) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		List<Map<Object,Object>> lista = new ArrayList<Map<Object,Object>>();

		int numColumns = rsmd.getColumnCount();
		Map<Object,Object> mapa = null;
		while (rs.next()) {
			mapa = new LinkedHashMap<Object,Object>();
			for (int i = 0; i < numColumns; i++) {
				if (isTipado) {
					if (isPlanilhaExcel) {
						CelulaVO celulaVO = new CelulaVO();
						celulaVO.setTipoCampo(new Integer(rsmd.getColumnType(i + 1)));
						celulaVO.setNomeCampo(rsmd.getColumnName(i + 1));

						switch (rsmd.getColumnType(i + 1)) {
						case 91:
						case 93:
							celulaVO.setValorCampo(rs.getTimestamp(i + 1) != null ? new Date(rs.getTimestamp(i + 1).getTime()) : null);
							break;
						case 4:
							int result = rs.getInt(i + 1);
							celulaVO.setValorCampo(rs.wasNull() ? null : new Integer(result));
							break;
						case 12:
							celulaVO.setValorCampo(rs.getString(i + 1));
							break;
						case 2:
						case 8:
							double result1 = rs.getDouble(i + 1);
							celulaVO.setValorCampo(rs.wasNull() ? null : new Double(result1));
							break;
						default:
							celulaVO.setValorCampo(rs.getString(i + 1));
						}
						mapa.put(rsmd.getColumnName(i + 1), celulaVO);
					} else {
						switch (rsmd.getColumnType(i + 1)) {
						case 91:
						case 93:
							mapa.put(rsmd.getColumnName(i + 1), rs.getTimestamp(i + 1) != null ? new Date(rs.getTimestamp(i + 1).getTime()) : null);
							break;
						case 4:
							int result = rs.getInt(i + 1);
							mapa.put(rsmd.getColumnName(i + 1), rs.wasNull() ? null : new Integer(result));
							break;
						case 12:
							mapa.put(rsmd.getColumnName(i + 1), rs.getString(i + 1));
							break;
						case 2:
						case 8:
							double result1 = rs.getDouble(i + 1);
							mapa.put(rsmd.getColumnName(i + 1), rs.wasNull() ? null : new Double(result1));
							break;
						default:
							mapa.put(rsmd.getColumnName(i + 1), rs.getString(i + 1));
							break;
						}
					}
				} else
					mapa.put(rsmd.getColumnName(i + 1), rs.getString(i + 1));
			}

			lista.add(mapa);
		}

		return lista;
	}
}