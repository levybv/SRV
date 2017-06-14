package br.com.marisa.srv.util.tools;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.GREY_25_PERCENT;
import org.apache.poi.hssf.util.HSSFColor.GREY_40_PERCENT;
import org.apache.poi.hssf.util.HSSFColor.INDIGO;
import org.apache.poi.hssf.util.HSSFColor.YELLOW;
import org.apache.poi.hssf.util.Region;

import br.com.marisa.srv.relatorios.data.CelulaVO;
import br.com.marisa.srv.relatorios.data.RelatorioVO;

public class PoiExcel {
	private static final int PARAGRAFO = 0;
	private static final String NOME = "NOME";
	private static final String VALOR = "VALOR";

	public static ByteArrayOutputStream montaPlanilha(String titulo, Map subTitulo, List listaCamposCabecalho, List dadosTabela, List listaChaveMap) throws Exception {
		RelatorioVO relatorioVO = new RelatorioVO();
		relatorioVO.setTitulo(titulo);
		relatorioVO.setSubTitulo(subTitulo);
		relatorioVO.setListaCamposCabecalho(listaCamposCabecalho);
		relatorioVO.setDadosTabela(dadosTabela);
		relatorioVO.setListaChaveMap(listaChaveMap);
		List listaRelatorios = new ArrayList();
		listaRelatorios.add(relatorioVO);
		return montaPlanilha(listaRelatorios, true);
	}

	public static ByteArrayOutputStream montaPlanilha(List listRelatorioVO, boolean isCamposTipados) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();

		ByteArrayOutputStream baos = null;

		for (int k = 0; k < listRelatorioVO.size(); k++) {
			RelatorioVO relatorioVO = (RelatorioVO) listRelatorioVO.get(k);

			String titulo = relatorioVO.getTitulo();
			Map subTitulo = relatorioVO.getSubTitulo();
			List listaCamposCabecalho = relatorioVO.getListaCamposCabecalho();
			List dadosTabela = relatorioVO.getDadosTabela();
			List listaChaveMap = relatorioVO.getListaChaveMap();

			HSSFSheet sheet = null;

			if (subTitulo != null) {
				String subTituloS = (String) subTitulo.get("NOME");
				if (subTituloS.length() > 31) {
					subTituloS = subTituloS.substring(0, 31);
				}
				sheet = workbook.createSheet(subTituloS);
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

			Region region = new Region(linha, (short) 0, linha, (short) (0 + listaCamposCabecalho.size() - 1));
			sheet.addMergedRegion(region);
			cell.setCellValue(new HSSFRichTextString(titulo));
			cell.setCellStyle(csTitulo);

			if ((subTitulo != null) && (subTitulo.size() > 0)) {
				linha++;
				row = sheet.createRow(linha);
				cell = row.createCell((short) 0);
				cell.setCellStyle(csCabecalhoGrid);
				cell.setCellValue(new HSSFRichTextString((String) subTitulo.get("NOME")));

				cell = row.createCell((short) 1);
				region = new Region(linha, (short) 1, linha, (short) (0 + listaCamposCabecalho.size() - 1));

				sheet.addMergedRegion(region);
				cell.setCellStyle(csTitulo);
				cell.setCellValue(new HSSFRichTextString((String) subTitulo.get("VALOR")));
			}

			linha++;
			row = sheet.createRow(linha);
			for (int i = 0; i < listaCamposCabecalho.size(); i++) {
				cell = row.createCell((short) (0 + i));
				cell.setCellStyle(csCabecalhoGrid);
				cell.setCellValue(new HSSFRichTextString((String) listaCamposCabecalho.get(i)));
			}

			Iterator it = dadosTabela.iterator();
			while (it.hasNext()) {
				linha++;
				row = sheet.createRow(linha);
				Map dados = (Map) it.next();

				HSSFCellStyle csEscolhido = workbook.createCellStyle();
				for (int j = 0; j < listaChaveMap.size(); j++) {
					cell = row.createCell((short) j);

					if (isCamposTipados) {
						CelulaVO celulaVO = (CelulaVO) dados.get(listaChaveMap.get(j));
						try {
							switch (celulaVO.getTipoCampo().intValue()) {
							case 91:
							case 93:
								Date data = (Date) celulaVO.getValorCampo();
								if (data != null) {
									cell.setCellValue((Date) celulaVO.getValorCampo());
								}

								cell.setCellStyle(cellData);

								break;
							case 4:
								Integer valor = (Integer) celulaVO.getValorCampo();
								if (valor != null) {
									cell.setCellValue(valor.doubleValue());
									continue;
								}
								cell.setCellValue(new HSSFRichTextString((String) celulaVO.getValorCampo()));

								break;
							case 12:
								cell.setCellValue(new HSSFRichTextString((String) celulaVO.getValorCampo()));
								break;
							case 2:
							case 8:
								Double valorD = (Double) celulaVO.getValorCampo();
								if (valorD != null) {
									csEscolhido.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
									cell.setCellValue(valorD.doubleValue());

									cell.setCellStyle(cellNumeric);
									continue;
								}
								cell.setCellValue(new HSSFRichTextString((String) celulaVO.getValorCampo()));

								break;
							default:
								cell.setCellValue(new HSSFRichTextString((String) celulaVO.getValorCampo()));
							}
						} catch (NullPointerException e) {
							cell.setCellValue(0.0D);
						}
					} else {
						cell.setCellValue(new HSSFRichTextString((String) dados.get(listaChaveMap.get(j))));
					}
				}
				it.remove();
			}

			for (int i = 0; i < listaCamposCabecalho.size(); i++) {
				sheet.autoSizeColumn((short) i);
			}

			baos = new ByteArrayOutputStream();
			workbook.write(baos);
		}
		return baos;
	}

	public static ByteArrayOutputStream montaPlanilha(String titulo, Map subTitulo, List listaCamposCabecalho, List dadosTabela, List listaChaveMap, Map totais) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();

		HSSFSheet sheet = workbook.createSheet();

		HSSFRow row = null;

		HSSFCell cell = null;

		HSSFCellStyle csTitulo = workbook.createCellStyle();
		HSSFCellStyle csCabecalhoGrid = workbook.createCellStyle();
		HSSFCellStyle csPar = workbook.createCellStyle();
		HSSFCellStyle csImpar = workbook.createCellStyle();

		HSSFFont fonteTitulo = workbook.createFont();
		fonteTitulo.setColor((short) 1);
		fonteTitulo.setBoldweight((short) 700);

		HSSFFont fonteCabecalho = workbook.createFont();
		fonteCabecalho.setFontName("Verdana");
		fonteCabecalho.setBoldweight((short) 700);

		csCabecalhoGrid.setFont(fonteCabecalho);
		csCabecalhoGrid.setFillPattern((short) 1);
		csCabecalhoGrid.setFillForegroundColor(new GREY_40_PERCENT().getIndex());

		HSSFFont fonteGeral = workbook.createFont();
		fonteGeral.setColor((short) 32767);

		csTitulo.setFillPattern((short) 1);
		csTitulo.setFillForegroundColor(new INDIGO().getIndex());
		csTitulo.setFont(fonteTitulo);

		csImpar.setFont(fonteGeral);
		csImpar.setFillPattern((short) 1);
		csImpar.setFillForegroundColor(new GREY_25_PERCENT().getIndex());

		csPar.setFont(fonteGeral);
		csPar.setFillPattern((short) 1);
		csPar.setFillForegroundColor(new YELLOW().getIndex());

		short linha = 0;
		linha = (short) (linha + 1);
		linha = (short) (linha + 1);
		row = sheet.createRow(linha);

		cell = row.createCell((short) 0);

		Region region = new Region(linha, (short) 0, linha, (short) (0 + listaCamposCabecalho.size() - 1));
		sheet.addMergedRegion(region);
		cell.setCellValue(new HSSFRichTextString(titulo));
		cell.setCellStyle(csTitulo);

		if ((subTitulo != null) && (subTitulo.size() > 0)) {
			row = sheet.createRow(linha = (short) (linha + 1));
			cell = row.createCell((short) 0);
			cell.setCellStyle(csCabecalhoGrid);
			cell.setCellValue(new HSSFRichTextString((String) subTitulo.get("NOME")));

			cell = row.createCell((short) 1);
			region = new Region(linha, (short) 1, linha, (short) (0 + listaCamposCabecalho.size() - 1));

			sheet.addMergedRegion(region);
			cell.setCellStyle(csTitulo);
			cell.setCellValue(new HSSFRichTextString((String) subTitulo.get("VALOR")));
		}

		row = sheet.createRow(linha = (short) (linha + 1));
		for (int i = 0; i < listaCamposCabecalho.size(); i++) {
			cell = row.createCell((short) (0 + i));
			cell.setCellStyle(csCabecalhoGrid);
			cell.setCellValue(new HSSFRichTextString((String) listaCamposCabecalho.get(i)));
		}

		for (int i = 0; i < dadosTabela.size(); i++) {
			row = sheet.createRow(linha = (short) (linha + 1));
			Map dados = (Map) dadosTabela.get(i);

			for (int j = 0; j < listaChaveMap.size(); j++) {
				cell = row.createCell((short) (0 + j));
				if (linha % 2 == 0)
					cell.setCellStyle(csImpar);
				else {
					cell.setCellStyle(csPar);
				}
				cell.setCellValue(new HSSFRichTextString((String) dados.get(listaChaveMap.get(j))));
			}
		}

		if ((totais != null) && (totais.size() > 0)) {
			row = sheet.createRow(linha = (short) (linha + 1));
			row = sheet.createRow(linha = (short) (linha + 1));
			cell = row.createCell((short) 0);
			cell.setCellStyle(csCabecalhoGrid);
			cell.setCellValue(new HSSFRichTextString("Totais"));
			cell = row.createCell((short) 1);
			cell.setCellStyle(csCabecalhoGrid);

			Set chaves = totais.entrySet();
			Iterator indiceMap = chaves.iterator();
			int total = 0;
			HSSFCellStyle cs = null;
			while (indiceMap.hasNext()) {
				Map.Entry entrada = (Map.Entry) indiceMap.next();
				if (linha % 2 == 0)
					cs = csImpar;
				else {
					cs = csPar;
				}
				row = sheet.createRow(linha = (short) (linha + 1));
				cell = row.createCell((short) 0);
				cell.setCellStyle(cs);
				cell.setCellValue(new HSSFRichTextString((String) entrada.getKey()));
				cell = row.createCell((short) 1);
				cell.setCellStyle(cs);
				Integer valor = (Integer) entrada.getValue();
				total += valor.intValue();
				cell.setCellValue(new HSSFRichTextString(valor.toString()));
			}

			row = sheet.createRow(linha = (short) (linha + 1));
			cell = row.createCell((short) 0);
			cell.setCellStyle(csCabecalhoGrid);
			cell.setCellValue(new HSSFRichTextString("Total"));
			cell = row.createCell((short) 1);
			cell.setCellStyle(csCabecalhoGrid);
			Integer vTotal = new Integer(total);
			cell.setCellValue(new HSSFRichTextString(vTotal.toString()));
		}

		for (int i = 0; i < listaCamposCabecalho.size(); i++) {
			sheet.autoSizeColumn((short) i);
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		workbook.write(baos);
		return baos;
	}
}