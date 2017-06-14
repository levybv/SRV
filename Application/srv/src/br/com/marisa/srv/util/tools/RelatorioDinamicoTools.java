package br.com.marisa.srv.util.tools;

import java.io.ByteArrayOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

import br.com.marisa.srv.relatorio.vo.RelatorioVO;
import br.com.marisa.srv.util.tools.vo.ColunaVO;
import br.com.marisa.srv.util.tools.vo.LinhaVO;

/**
 * 
 * @author levy.villar
 *
 */
public final class RelatorioDinamicoTools {

	public static List<LinhaVO> montaRelatorioDinamico(ResultSet rs) throws SQLException {

		ResultSetMetaData rsmd = rs.getMetaData();
		int numColumns = rsmd.getColumnCount();
		List<LinhaVO> linhas = new ArrayList<LinhaVO>();

		while (rs.next()) {
			List<ColunaVO> colunas = new ArrayList<ColunaVO>();
			for (int i = 0; i < numColumns; i++) {
				ColunaVO coluna = new ColunaVO();
				coluna.setTipo(new Integer(rsmd.getColumnType(i + 1)));
				coluna.setNome(rsmd.getColumnName(i + 1));

				switch (rsmd.getColumnType(i + 1)) {
					case Types.DATE:
					case Types.TIMESTAMP:
						coluna.setValor(rs.getTimestamp(i + 1) != null ? new Date(rs.getTimestamp(i + 1).getTime()) : null);
						break;
					case Types.INTEGER:
						int result = rs.getInt(i + 1);
						coluna.setValor(rs.wasNull() ? null : new Integer(result));
						break;
					case Types.CHAR:
					case Types.VARCHAR:
						coluna.setValor(rs.getString(i + 1));
						break;
					case Types.NUMERIC:
						if (rsmd.getScale(i+1) > 0) {
							double result1 = rs.getDouble(i + 1);
							coluna.setValor(rs.wasNull() ? null : new Double(result1));
						} else {
							long resultLong = rs.getLong(i + 1);
							coluna.setValor(rs.wasNull() ? null : new Long(resultLong));
						}
						break;
					case Types.DOUBLE:
						double result1 = rs.getDouble(i + 1);
						coluna.setValor(rs.wasNull() ? null : new Double(result1));
						break;
					default:
						coluna.setValor(rs.getString(i + 1));
				}
				colunas.add(coluna);
			}
			LinhaVO linha = new LinhaVO();
			linha.setColunas(colunas);
			linhas.add(linha);
		}
		return linhas;
	}

	public static ByteArrayOutputStream montaPlanilha(RelatorioVO vo) throws Exception {

		HSSFWorkbook workbook = new HSSFWorkbook();
		ByteArrayOutputStream baos = null;

		String titulo = "Relatorio";
		String subTitulo = vo.getTitulo();

		List<String> listaCamposCabecalho = new ArrayList<String>();
		if (vo.getDescricaoColunas() != null) {
			StringTokenizer st = new StringTokenizer(vo.getDescricaoColunas(), ";");
			while (st.hasMoreTokens()) {
				listaCamposCabecalho.add(st.nextToken());
			}
		} else {
			List<ColunaVO> colunas = vo.getLinhas().get(0).getColunas();
			for (Iterator<ColunaVO> itColunas = colunas.iterator(); itColunas.hasNext();) {
				listaCamposCabecalho.add(itColunas.next().getNome());
			}
		}

		HSSFSheet sheet = null;

		if (subTitulo != null) {
			if (subTitulo.length() > 31) {
				sheet = workbook.createSheet(vo.getTitulo().substring(0, 31));
			} else {
				sheet = workbook.createSheet(vo.getTitulo());
			}
		} else {
			sheet = workbook.createSheet();
		}

		HSSFRow row = null;
		HSSFCell cell = null;

		HSSFCellStyle csTitulo = workbook.createCellStyle();
		HSSFCellStyle csCabecalhoGrid = workbook.createCellStyle();

		HSSFCellStyle cellData = workbook.createCellStyle();
		cellData.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));

		HSSFCellStyle cellNumeric = workbook.createCellStyle();
		cellNumeric = workbook.createCellStyle();

		HSSFFont fonteTitulo = workbook.createFont();
		fonteTitulo.setColor((short) 32767);
		fonteTitulo.setBoldweight((short) 700);

		HSSFFont fonteCabecalho = workbook.createFont();
		fonteCabecalho.setFontName("Verdana");
		fonteCabecalho.setBoldweight((short) 700);

		csCabecalhoGrid.setFont(fonteCabecalho);

		HSSFFont fonteGeral = workbook.createFont();
		fonteGeral.setColor((short) 32767);

		csTitulo.setFont(fonteTitulo);

		int linha = 0;

		linha++;
		row = sheet.createRow(linha);
		cell = row.createCell((short) 0);

		if (listaCamposCabecalho.size() > 0) {

			Region region = new Region(linha, (short) 0, linha, (short) (0 + listaCamposCabecalho.size() - 1));
			sheet.addMergedRegion(region);
			cell.setCellValue(new HSSFRichTextString(titulo));
			cell.setCellStyle(csTitulo);

			if ((subTitulo != null)) {
				linha++;
				row = sheet.createRow(linha);
				cell = row.createCell((short) 0);
				cell.setCellStyle(csCabecalhoGrid);
				cell.setCellValue(new HSSFRichTextString(subTitulo));

				cell = row.createCell((short) 1);
				region = new Region(linha, (short) 0, linha, (short) (0 + listaCamposCabecalho.size() - 1));

				sheet.addMergedRegion(region);
				cell.setCellStyle(csTitulo);
				cell.setCellValue(new HSSFRichTextString(subTitulo));
			}

			linha++;
			row = sheet.createRow(linha);
			for (int i = 0; i < listaCamposCabecalho.size(); i++) {
				cell = row.createCell((short) (0 + i));
				cell.setCellStyle(csCabecalhoGrid);
				cell.setCellValue(new HSSFRichTextString((String) listaCamposCabecalho.get(i)));
			}
			
		} else {

			cell.setCellValue(new HSSFRichTextString(titulo));
			cell.setCellStyle(csTitulo);

			if ((subTitulo != null)) {
				linha++;
				row = sheet.createRow(linha);
				cell = row.createCell((short) 0);
				cell.setCellStyle(csCabecalhoGrid);
				cell.setCellValue(new HSSFRichTextString(subTitulo));

				cell = row.createCell((short) 1);
				cell.setCellStyle(csTitulo);
				cell.setCellValue(new HSSFRichTextString(subTitulo));
			}

			linha++;
			row = sheet.createRow(linha);
		}

		Iterator<LinhaVO> it = vo.getLinhas().iterator();
		while (it.hasNext()) {
			linha++;
			row = sheet.createRow(linha);
			LinhaVO dadosLinha = it.next();

			HSSFCellStyle csEscolhido = workbook.createCellStyle();
			for (int j = 0; j < dadosLinha.getColunas().size(); j++) {
				cell = row.createCell((short) j);

				ColunaVO coluna = dadosLinha.getColunas().get(j);
				try {
					switch (coluna.getTipo().intValue()) {
					case Types.DATE:
					case Types.TIMESTAMP:
						Date data = (Date) coluna.getValor();
						if (data != null) {
							cell.setCellValue((Date) coluna.getValor());
						}
						cell.setCellStyle(cellData);
						break;
					case Types.INTEGER:
						Integer valor = (Integer) coluna.getValor();
						if (valor != null) {
							cell.setCellValue(valor.doubleValue());
							continue;
						}
						cell.setCellValue(new HSSFRichTextString((String) coluna.getValor()));
						break;
					case Types.CHAR:
					case Types.VARCHAR:
						cell.setCellValue(new HSSFRichTextString((String) coluna.getValor()));
						break;
					case Types.NUMERIC:
						if (coluna.getValor() instanceof Double) {
							Double valorD = (Double) coluna.getValor();
							if (valorD != null) {
								csEscolhido.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
								cell.setCellValue(valorD.doubleValue());
								cell.setCellStyle(cellNumeric);
								continue;
							}
							cell.setCellValue(new HSSFRichTextString((String) coluna.getValor()));
						} else {
							Long valorLong = (Long) coluna.getValor();
							if (valorLong != null) {
								cell.setCellValue(valorLong.longValue());
								continue;
							}
							cell.setCellValue(new HSSFRichTextString((String) coluna.getValor()));
						}
						break;
					case Types.DOUBLE:
						Double valorD = (Double) coluna.getValor();
						if (valorD != null) {
							csEscolhido.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
							cell.setCellValue(valorD.doubleValue());

							cell.setCellStyle(cellNumeric);
							continue;
						}
						cell.setCellValue(new HSSFRichTextString((String) coluna.getValor()));
						break;
					default:
						cell.setCellValue(new HSSFRichTextString((String) coluna.getValor()));
					}
				} catch (NullPointerException e) {
					cell.setCellValue(0.0D);
				}
			}
			it.remove();
		}

		for (int i = 0; i < listaCamposCabecalho.size(); i++) {
			sheet.autoSizeColumn((short) i);
		}

		baos = new ByteArrayOutputStream();
		workbook.write(baos);

		return baos;
	}

}