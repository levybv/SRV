package br.com.marisa.srv.funcionario.action;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import br.com.marisa.srv.bonus.business.RealizFuncIndicadorBusiness;
import br.com.marisa.srv.bonus.configuracao.vo.ConfiguracaoBonusVO;
import br.com.marisa.srv.bonus.engine.BonusAnualEngine;
import br.com.marisa.srv.escala.business.EscalaBusiness;
import br.com.marisa.srv.escala.vo.EscalaVO;
import br.com.marisa.srv.funcionario.business.FuncionarioBusiness;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.enumeration.ExcelColunasEnum;
import br.com.marisa.srv.geral.enumeration.StatCalcRealzEnum;
import br.com.marisa.srv.geral.excecoes.ExcelCamposException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.geral.helper.StringHelper;
import br.com.marisa.srv.geral.vo.UsuarioBean;
import br.com.marisa.srv.indicador.business.IndicadorBusiness;
import br.com.marisa.srv.indicador.vo.DadosIndicadorVO;
import br.com.marisa.srv.indicador.vo.DetalheCalculoVO;
import br.com.marisa.srv.indicador.vo.IndicadorFuncionarioRealizadoVO;
import br.com.marisa.srv.unidade.business.UnidadeBusiness;
import br.com.marisa.srv.unidade.vo.UnidadeVO;

/**
 * 
 * @author Levy Villar
 *
 */
public class UploadBonusFuncionarioServlet extends HttpServlet {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9073929125791791571L;

	//Log4J
	private static final Logger log = Logger.getLogger(UploadBonusFuncionarioServlet.class);    

	//Mapeamento das colunas para importacao
	private final ExcelColunasEnum NUM_COLUNA_COD_FUNC = ExcelColunasEnum.D;
	private final ExcelColunasEnum NUM_COLUNA_DESC_INDICADOR = ExcelColunasEnum.E;
	private final ExcelColunasEnum NUM_COLUNA_SENTIDO_INDICADOR = ExcelColunasEnum.F;
	private final ExcelColunasEnum NUM_COLUNA_COD_ESCALA = ExcelColunasEnum.H;
	private final ExcelColunasEnum NUM_COLUNA_DESC_FORMULA = ExcelColunasEnum.I;
	private final ExcelColunasEnum NUM_COLUNA_COD_PESO = ExcelColunasEnum.G;
	private final ExcelColunasEnum NUM_COLUNA_FLG_IMPORTA = ExcelColunasEnum.BO;
	private final ExcelColunasEnum NUM_COLUNA_ATINGIMENTO = ExcelColunasEnum.BP;
	private final ExcelColunasEnum NUM_COLUNA_DESEMPENHO = ExcelColunasEnum.BQ;

	private final int COD_GRUPO_INDICADOR_CORPORATIVO = 1;
	private final int COD_GRUPO_INDICADOR_INDIVIDUAL = 7;

	private String DESCR_SENTIDO_INDICADOR_CIMA = "MELHOR PARA CIMA";
	private String DESCR_SENTIDO_INDICADOR_BAIXO = "MELHOR PARA BAIXO";

	private String FLG_SENTIDO_INDICADOR_CIMA = "C";
	private String FLG_SENTIDO_INDICADOR_BAIXO = "B";

	static {
		Locale.setDefault(new Locale("pt", "BR"));
	}

	/**
	 * 
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		List<String> erros = new ArrayList<String>();
		String ano = null;
		String mes = null;
		//String tipoImportacao = null;
		InputStream uploadedStream = null;

		try {
			String contentType = request.getContentType();
			if ((contentType != null) && (contentType.indexOf("multipart/form-data") >= 0)) {

				ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
				List<FileItem> fileItemsList = servletFileUpload.parseRequest(request);

				Iterator<FileItem> it = fileItemsList.iterator();
				while (it.hasNext()){
					FileItem fileItem = (FileItem)it.next();
					if (fileItem.isFormField()){
						//CAMPOS
						if ( fileItem.getFieldName().equalsIgnoreCase("periodoImport") ) {
							mes = StringHelper.completarNumeroComZerosEsquerda(Integer.valueOf(fileItem.getString().substring(5)).intValue(),2);
							ano = StringHelper.completarNumeroComZerosEsquerda(Integer.valueOf(fileItem.getString().substring(0, 4)).intValue(),4);
						}
					} else {
						//ARQUIVO
						if (fileItem.getName().toUpperCase().endsWith(".XLS") ) {
							uploadedStream = fileItem.getInputStream();
							
							processaArquivoBonus(uploadedStream, ano, mes, erros, obtemUsuarioDaSessao(request).getUsuarioVO().getIdUsuario());
						} else {
							erros.add("Formato de arquivo inválido. Envie apenas arquivos com extensão .XLS");
							request.getSession().setAttribute("erros", erros);
							response.sendRedirect("/srv/bonus.do?operacao=inicio");
							return;
						}
					}
				}
			}

			if (erros != null && erros.size() > 0) {
				request.getSession().setAttribute("erros", erros);
			} else {
				request.getSession().setAttribute("mensagem", "Importação realizada com sucesso.");
			}			

		} catch (Exception e) {
			erros.add("Ocorreu erro não esperado no processo de importação: " + e.getMessage());
			request.getSession().setAttribute("erros", erros);
			log.error("Ocorreu erro ao importar bonus funcionario.", e);
			e.printStackTrace();
		}
		
		response.sendRedirect("/srv/bonus.do?operacao=inicio");
		return;
	}

	/**
	 * 
	 * @param uploadedStream
	 * @param ano
	 * @param mes
	 * @param erros
	 * @throws SRVException
	 * @throws IOException
	 */
	private void processaArquivoBonus(InputStream uploadedStream, String ano, String mes, List<String> erros, int idUsuario) throws SRVException, IOException {

		HSSFWorkbook workbook = null;

		try {
			int numAbaPlanilha = 0; //Sempre importa somente a primeira aba do arquivo
			int numLinhaInicioProcessamento = 4; //Ignora as primeiras 4 linhas, consideradas cabecalho
			int numLinhaFimProcessamento = 0; //Sera configurado abaixo, conforme ultima linha na planilha

			//Configuracao para leitura da planilha
			workbook = new HSSFWorkbook(uploadedStream);
			HSSFSheet sheet = workbook.getSheetAt(numAbaPlanilha);
			numLinhaFimProcessamento = sheet.getLastRowNum();

			//Obtem parametros de indicadores configurado para o ano selecionado
			ConfiguracaoBonusVO configuracaoBonusVO = BonusAnualEngine.getInstance().obtemConfiguracaoBonus(Integer.parseInt(ano));

			//Processa linhas da planilha
			for (int numLinha = numLinhaInicioProcessamento; numLinha <= numLinhaFimProcessamento; numLinha++) {

				try {

					HSSFRow row = sheet.getRow(numLinha);

					//Verifica se linha deve ser ignorada
					String ignoraLinha = String.valueOf(row.getCell((short)NUM_COLUNA_FLG_IMPORTA.getNumeroColuna()));
					if (ObjectHelper.isNotEmpty(ignoraLinha) && ignoraLinha.equalsIgnoreCase("X")) {
						continue;
					}

					//Obtem funcionario na base de dados
					FuncionarioVO excelFuncVO = getFuncionarioVO(row);
					FuncionarioVO databaseFuncVO = FuncionarioBusiness.getInstance().obtemFuncionario(excelFuncVO.getIdFuncionario());
					if (databaseFuncVO == null) {
						erros.add("Funcionario '" + excelFuncVO.getIdFuncionario() + "' não encontrado na base de dados! Linha: " + (numLinha+1));
						continue;
					} else {
						excelFuncVO.setIdEmpresa(databaseFuncVO.getIdEmpresa());
						excelFuncVO.setIdFilial(databaseFuncVO.getIdFilial());
						excelFuncVO.setIdCargo(databaseFuncVO.getIdCargo());
					}

					//Verifica se o bonus do funcionario pode ser importado pelo status
					DetalheCalculoVO detalheVO = RealizFuncIndicadorBusiness.getInstance().obterStatusCalculoRealizado(excelFuncVO.getIdFuncionario(), Integer.parseInt(ano), Integer.parseInt(mes), excelFuncVO.getIdEmpresa(), excelFuncVO.getIdFilial());
					if (detalheVO != null && detalheVO.getStatusCalculoRealizado() > StatCalcRealzEnum.INICIADO.getCodigo()) {
						erros.add("Funcionario '" + excelFuncVO.getIdFuncionario() + "' não importado. Status não permitido. Linha: " + (numLinha+1));
						continue;
					}

					//Obtem indicador de acordo com o tipo
					DadosIndicadorVO excelIndicVO = null;
					if (excelFuncVO.getIdFuncionario().longValue() == configuracaoBonusVO.getIdFuncionarioCorporativo().longValue()) {
						excelIndicVO = getIndicadorVO(row, COD_GRUPO_INDICADOR_CORPORATIVO, configuracaoBonusVO);
					} else {
						excelIndicVO = getIndicadorVO(row, COD_GRUPO_INDICADOR_INDIVIDUAL, configuracaoBonusVO);
					}

					//Obtem indicador existente na base de dados, caso contrario, grava novo indicador
					DadosIndicadorVO databaseIndicVO = IndicadorBusiness.getInstance().obtemIndicadorPorNome(excelIndicVO.getDescricaoIndicador(), excelIndicVO.getIdGrupoIndicador(), configuracaoBonusVO.getCodIndicIni(), configuracaoBonusVO.getCodIndicFim());
					if (databaseIndicVO != null && databaseIndicVO.getIdIndicador() > 0) {
						if (BonusAnualEngine.getInstance().isEscalaPermitidaBonus(configuracaoBonusVO, databaseIndicVO.getIdEscala())) {
							excelIndicVO.setIdIndicador(databaseIndicVO.getIdIndicador());
						} else {
							erros.add("Escala do indicador '" + databaseIndicVO.getDescricaoIndicador() + "' não permitida para uso no período bônus. Linha: " + (numLinha+1));
							continue;
						}
					} else {
						//throw new ExcelCamposException("Indicador não existe: " + excelIndicVO.getDescricaoIndicador());
						excelIndicVO.setIdIndicador(IndicadorBusiness.getInstance().obtemProximoCodIndicadorPorRange(configuracaoBonusVO.getCodIndicIni(), configuracaoBonusVO.getCodIndicFim()));
						excelIndicVO.setIdUsuarioUltimaAlteracao(idUsuario);
						IndicadorBusiness.getInstance().incluiIndicadorBonusImportacao(excelIndicVO);
					}

					IndicadorFuncionarioRealizadoVO databaseFuncIndicVO = IndicadorBusiness.getInstance().obtemRealizadoIndicadorBonus(excelFuncVO.getIdFuncionario(), excelIndicVO.getIdIndicador(), excelFuncVO.getIdEmpresa(), excelFuncVO.getIdFilial(), Integer.parseInt(ano), Integer.parseInt(mes));

					//Configura realizado e grava na base de dados
					IndicadorFuncionarioRealizadoVO realizFuncIndicVO = getRealizFuncIndicVO(row, Integer.parseInt(mes));
					realizFuncIndicVO.setAno(Integer.parseInt(ano));
					realizFuncIndicVO.setMes(Integer.parseInt(mes));
					realizFuncIndicVO.setIdFuncionario(excelFuncVO.getIdFuncionario());
					realizFuncIndicVO.setIdEmpresa(excelFuncVO.getIdEmpresa());
					realizFuncIndicVO.setIdFilial(excelFuncVO.getIdFilial());
					realizFuncIndicVO.setIdCargo(excelFuncVO.getIdCargo());
					realizFuncIndicVO.setIdIndicador(excelIndicVO.getIdIndicador());
					realizFuncIndicVO.setIdEscala(excelIndicVO.getIdEscala());
					realizFuncIndicVO.setIdUsuarioAlteracao(idUsuario);

					if (databaseFuncIndicVO == null) {
						RealizFuncIndicadorBusiness.getInstance().incluiRealizadoFuncIndicador(realizFuncIndicVO);
					} else {
						RealizFuncIndicadorBusiness.getInstance().alteraRealizadoFuncIndicador(realizFuncIndicVO);
					}

				} catch (ExcelCamposException eie) {
					erros.add(eie.getMessage());
				} catch (Exception ex) {
					erros.add("Erro linha " + (numLinha+1) + ": " + ex.getMessage());
				}

			}

		} catch (Exception ex) {
			System.out.println("Erro ao PROCESSAR a planilha: " + ex.getMessage());
			ex.printStackTrace();
		}

	}

	/**
	 * 
	 * @param row
	 * @return
	 * @throws ExcelCamposException
	 */
	public FuncionarioVO getFuncionarioVO(HSSFRow row) throws ExcelCamposException {
		FuncionarioVO vo = new FuncionarioVO();
		try {
			vo.setIdFuncionario((long)row.getCell((short)NUM_COLUNA_COD_FUNC.getNumeroColuna()).getNumericCellValue());
		} catch (Exception ex) {
			throw new ExcelCamposException("[ERRO CAMPO] 'MATRICULA' - [LINHA/COLUNA: " + (row.getRowNum()+1) + "/" + NUM_COLUNA_COD_FUNC.getDescricaoColuna() + "]: " + ex.getMessage(), ex);
		}
		return vo;
	}

	/**
	 * 
	 * @param row
	 * @param codGrupoIndicador
	 * @param configuracaoBonusVO
	 * @return
	 * @throws ExcelCamposException
	 */
	@SuppressWarnings("deprecation")
	public DadosIndicadorVO getIndicadorVO(HSSFRow row, int codGrupoIndicador, ConfiguracaoBonusVO configuracaoBonusVO) throws ExcelCamposException {
		DadosIndicadorVO vo = new DadosIndicadorVO();
		vo.setIdGrupoIndicador(codGrupoIndicador);
		try {
			vo.setDescricaoIndicador(StringHelper.limpaAcentuacao(row.getCell((short)NUM_COLUNA_DESC_INDICADOR.getNumeroColuna()).toString().trim()));
		} catch (Exception ex) {
			throw new ExcelCamposException("[ERRO CAMPO] 'INDICADOR' - [LINHA/COLUNA: " + (row.getRowNum()+1) + "/" + NUM_COLUNA_DESC_INDICADOR.getDescricaoColuna() + "]: " + ex.getMessage(), ex);
		}
		try {
			String sentidoIndic = StringHelper.limpaAcentuacao(row.getCell((short)NUM_COLUNA_SENTIDO_INDICADOR.getNumeroColuna()).toString().trim());
			if (sentidoIndic.equalsIgnoreCase(DESCR_SENTIDO_INDICADOR_CIMA)) {
				vo.setFlgSentido(FLG_SENTIDO_INDICADOR_CIMA);
			} else if (sentidoIndic.equalsIgnoreCase(DESCR_SENTIDO_INDICADOR_BAIXO)) {
				vo.setFlgSentido(FLG_SENTIDO_INDICADOR_BAIXO);
			} else {
				throw new Exception("Sentido do indicador é inválido");
			}
		} catch (Exception ex) {
			throw new ExcelCamposException("[ERRO CAMPO] 'SENTIDO DO INDICADOR' - [LINHA/COLUNA: " + (row.getRowNum()+1) + "/" + NUM_COLUNA_SENTIDO_INDICADOR.getDescricaoColuna() + "]: " + ex.getMessage(), ex);
		}
		try {
			int idEscala = (int)row.getCell((short)NUM_COLUNA_COD_ESCALA.getNumeroColuna()).getNumericCellValue();
			EscalaVO escalaVO = EscalaBusiness.getInstance().obtemEscala(idEscala);
			if (escalaVO != null) {
				if (BonusAnualEngine.getInstance().isEscalaPermitidaBonus(configuracaoBonusVO, idEscala)) {
					vo.setIdEscala(idEscala);
				} else {
					throw new Exception("Escala não permitida para o período de bônus");
				}
			} else {
				throw new Exception("Escala inválida");
			}
		} catch (Exception ex) {
			throw new ExcelCamposException("[ERRO CAMPO] 'ESCALA' - [LINHA/COLUNA: " + (row.getRowNum()+1) + "/" + NUM_COLUNA_COD_ESCALA.getDescricaoColuna() + "]: " + ex.getMessage(), ex);
		}
		try {
			vo.setFormulaIndicador(StringHelper.limpaAcentuacao(row.getCell((short)NUM_COLUNA_DESC_FORMULA.getNumeroColuna()).getStringCellValue().trim()));
		} catch (Exception ex) {
			throw new ExcelCamposException("[ERRO CAMPO] 'FORMULA' - [LINHA/COLUNA: " + (row.getRowNum()+1) + "/" + NUM_COLUNA_DESC_FORMULA.getDescricaoColuna() + "]: " + ex.getMessage(), ex);
		}
		//Pre-valida a unidade, para evitar gravacao do indicador sem a unidade configurada
		try {
			String uniIndicStr = StringHelper.limpaAcentuacao(row.getCell((short)NUM_COLUNA_DESC_INDICADOR.getNumeroColuna()).toString().trim());
			uniIndicStr = uniIndicStr.substring(uniIndicStr.lastIndexOf("(")+1, uniIndicStr.lastIndexOf(")"));
			UnidadeVO unidadeVO = UnidadeBusiness.getInstance().obtemUnidade(uniIndicStr);
			if (unidadeVO == null) {
				throw new ExcelCamposException("[ERRO CAMPO] 'UNIDADE' - [LINHA/COLUNA: " + (row.getRowNum()+1) + "/" + NUM_COLUNA_DESC_INDICADOR.getDescricaoColuna() + "]: UNIDADE NAO CADASTRADA NO SISTEMA");
			}
		} catch (Exception ex) {
			if (ex.getMessage().contains("String index out of range")) {
				throw new ExcelCamposException("[ERRO CAMPO] 'UNIDADE' - [LINHA/COLUNA: " + (row.getRowNum()+1) + "/" + NUM_COLUNA_DESC_INDICADOR.getDescricaoColuna() + "]: UNIDADE NAO INFORMADO ENTRE PARENTESES", ex);				
			} else{
				throw new ExcelCamposException("[ERRO CAMPO] 'UNIDADE' - [LINHA/COLUNA: " + (row.getRowNum()+1) + "/" + NUM_COLUNA_DESC_INDICADOR.getDescricaoColuna() + "]: " + ex.getMessage(), ex);			}
		}

		return vo;
	}

	/**
	 * 
	 * @param row
	 * @return
	 * @throws ExcelCamposException
	 */
	public IndicadorFuncionarioRealizadoVO getRealizFuncIndicVO(HSSFRow row, int numMes) throws ExcelCamposException {
		IndicadorFuncionarioRealizadoVO vo = new IndicadorFuncionarioRealizadoVO();
		try {
			String pesoStr = row.getCell((short)NUM_COLUNA_COD_PESO.getNumeroColuna()).toString().trim();
			if (!pesoStr.equalsIgnoreCase("") && !pesoStr.equalsIgnoreCase("-")) {
				//vo.setPeso(row.getCell((short)NUM_COLUNA_COD_PESO.getNumeroColuna()).getNumericCellValue()*100);
				vo.setPeso((row.getCell((short)NUM_COLUNA_COD_PESO.getNumeroColuna()).getNumericCellValue()*100)*2);
				vo.setUnidadePeso(Constantes.UNIDADE_PERCENTUAL);
			}
		} catch (Exception ex) {
			throw new ExcelCamposException("[ERRO CAMPO] 'PESO' - [LINHA/COLUNA: " + (row.getRowNum()+1) + "/" + NUM_COLUNA_COD_PESO.getDescricaoColuna() + "]: " + ex.getMessage(), ex);
		}
		try {
			String uniIndicStr = StringHelper.limpaAcentuacao(row.getCell((short)NUM_COLUNA_DESC_INDICADOR.getNumeroColuna()).toString().trim());
			uniIndicStr = uniIndicStr.substring(uniIndicStr.lastIndexOf("(")+1, uniIndicStr.lastIndexOf(")"));
			UnidadeVO unidadeVO = UnidadeBusiness.getInstance().obtemUnidade(uniIndicStr);
			if (unidadeVO == null) {
				throw new ExcelCamposException("[ERRO CAMPO] 'UNIDADE' - [LINHA/COLUNA: " + (row.getRowNum()+1) + "/" + NUM_COLUNA_DESC_INDICADOR.getDescricaoColuna() + "]: UNIDADE NAO CADASTRADA NO SISTEMA");
			} else {
				vo.setUnidadeMeta(UnidadeBusiness.getInstance().obtemUnidade(uniIndicStr).getIdUnidade());
				vo.setUnidadeRealizado(UnidadeBusiness.getInstance().obtemUnidade(uniIndicStr).getIdUnidade());
			}
		} catch (Exception ex) {
			if (ex.getMessage().contains("String index out of range")) {
				throw new ExcelCamposException("[ERRO CAMPO] 'UNIDADE' - [LINHA/COLUNA: " + (row.getRowNum()+1) + "/" + NUM_COLUNA_DESC_INDICADOR.getDescricaoColuna() + "]: UNIDADE NAO INFORMADO ENTRE PARENTESES", ex);				
			} else{
				throw new ExcelCamposException("[ERRO CAMPO] 'UNIDADE' - [LINHA/COLUNA: " + (row.getRowNum()+1) + "/" + NUM_COLUNA_DESC_INDICADOR.getDescricaoColuna() + "]: " + ex.getMessage(), ex);			}
		}
		try {
			String metaStr = row.getCell((short)obtemColunaOrcamento(numMes).getNumeroColuna()).toString().trim();
			if (vo.getUnidadeMeta() != Constantes.UNIDADE_ORCAMENTO && !metaStr.equalsIgnoreCase("") && !metaStr.equalsIgnoreCase("-")) {
				if (vo.getUnidadeMeta() == Constantes.UNIDADE_PERCENTUAL) {
					vo.setMeta(row.getCell((short)obtemColunaOrcamento(numMes).getNumeroColuna()).getNumericCellValue()*100);
				} else {
					vo.setMeta(row.getCell((short)obtemColunaOrcamento(numMes).getNumeroColuna()).getNumericCellValue());
				}
			}
		} catch (Exception ex) {
			throw new ExcelCamposException("[ERRO CAMPO] 'META' - [LINHA/COLUNA: " + (row.getRowNum()+1) + "/" + obtemColunaOrcamento(numMes) + "]: " + ex.getMessage(), ex);
		}
		try {
			double realizado = 0.0D;
			String realzStr = row.getCell((short)obtemColunaRealizado(numMes).getNumeroColuna()).toString().trim();
			if (vo.getUnidadeRealizado() != Constantes.UNIDADE_ORCAMENTO && !realzStr.equalsIgnoreCase("") && !realzStr.equalsIgnoreCase("-")) {
				if (vo.getUnidadeRealizado() == Constantes.UNIDADE_PERCENTUAL) {
					realizado = row.getCell((short)obtemColunaRealizado(numMes).getNumeroColuna()).getNumericCellValue()*100;
				} else {
					realizado = row.getCell((short)obtemColunaRealizado(numMes).getNumeroColuna()).getNumericCellValue();
				}
				DecimalFormat df = new DecimalFormat("#,###.0000");
				realizado = Double.parseDouble(df.format(realizado).replaceAll("\\.", "").replaceAll(",", "."));
				vo.setRealizado(realizado);
			} else {
				vo.setUnidadeRealizado(null);
			}
		} catch (Exception ex) {
			throw new ExcelCamposException("[ERRO CAMPO] 'REALIZADO' - [LINHA/COLUNA: " + (row.getRowNum()+1) + "/" + obtemColunaRealizado(numMes) + "]: " + ex.getMessage(), ex);
		}
		try {
			double atingimento = 0.0D;
			String atingimentoStr = row.getCell((short)NUM_COLUNA_ATINGIMENTO.getNumeroColuna()).toString().trim();
			if (!atingimentoStr.equalsIgnoreCase("") && !atingimentoStr.equalsIgnoreCase("-")) {
				atingimento = row.getCell((short)NUM_COLUNA_ATINGIMENTO.getNumeroColuna()).getNumericCellValue()*100;
				vo.setRealizadoXMeta(atingimento);
				vo.setUnidadeRealizadoXMeta(Constantes.UNIDADE_PERCENTUAL);
			}
		} catch (Exception ex) {
			throw new ExcelCamposException("[ERRO CAMPO] 'ATINGIMENTO' - [LINHA/COLUNA: " + (row.getRowNum()+1) + "/" + NUM_COLUNA_ATINGIMENTO.getDescricaoColuna() + "]: " + ex.getMessage(), ex);
		}
		try {
			double desempenho = 0.0D;
			String desempenhoStr = row.getCell((short)NUM_COLUNA_DESEMPENHO.getNumeroColuna()).toString().trim();
			if (!desempenhoStr.equalsIgnoreCase("") && !desempenhoStr.equalsIgnoreCase("-")) {
				desempenho = row.getCell((short)NUM_COLUNA_DESEMPENHO.getNumeroColuna()).getNumericCellValue()*100;
				DecimalFormat df = new DecimalFormat("#,###.0000");
				desempenho = Double.parseDouble(df.format(desempenho).replaceAll("\\.", "").replaceAll(",", "."));
				vo.setRealizadoPonderacao(desempenho);
				vo.setUnidadeRealizadoPonderacao(Constantes.UNIDADE_PERCENTUAL);
			}
		} catch (Exception ex) {
			throw new ExcelCamposException("[ERRO CAMPO] 'DESEMPENHO' - [LINHA/COLUNA: " + (row.getRowNum()+1) + "/" + NUM_COLUNA_DESEMPENHO.getDescricaoColuna() + "]: " + ex.getMessage(), ex);
		}
		return vo;
	}

	/**
	 * 
	 * @param numMes
	 * @return
	 */
	private ExcelColunasEnum obtemColunaRealizado(int numMes) {
		switch (numMes) {
		case 1:
			return ExcelColunasEnum.BB;
		case 2:
			return ExcelColunasEnum.BC;
		case 3:
			return ExcelColunasEnum.BD;
		case 4:
			return ExcelColunasEnum.BE;
		case 5:
			return ExcelColunasEnum.BF;
		case 6:
			return ExcelColunasEnum.BG;
		case 7:
			return ExcelColunasEnum.BH;
		case 8:
			return ExcelColunasEnum.BI;
		case 9:
			return ExcelColunasEnum.BJ;
		case 10:
			return ExcelColunasEnum.BK;
		case 11:
			return ExcelColunasEnum.BL;
		case 12:
			return ExcelColunasEnum.BM;
		default:
			return ExcelColunasEnum.BM;
		}
	}

	/**
	 * 
	 * @param numMes
	 * @return
	 */
	private ExcelColunasEnum obtemColunaOrcamento(int numMes) {
		switch (numMes) {
		case 1:
			return ExcelColunasEnum.AB;
		case 2:
			return ExcelColunasEnum.AC;
		case 3:
			return ExcelColunasEnum.AD;
		case 4:
			return ExcelColunasEnum.AE;
		case 5:
			return ExcelColunasEnum.AF;
		case 6:
			return ExcelColunasEnum.AG;
		case 7:
			return ExcelColunasEnum.AH;
		case 8:
			return ExcelColunasEnum.AI;
		case 9:
			return ExcelColunasEnum.AJ;
		case 10:
			return ExcelColunasEnum.AK;
		case 11:
			return ExcelColunasEnum.AL;
		case 12:
			return ExcelColunasEnum.N;
		default:
			return ExcelColunasEnum.N;
		}
	}

	/**
	 * 
	 * @param req
	 * @return
	 */
	public UsuarioBean obtemUsuarioDaSessao(HttpServletRequest req) {
		HttpSession session = req.getSession();
		if (session==null) {
			return null;
		}
		return (UsuarioBean) session.getAttribute(ConstantesRequestSession.SESSION_USUARIO_BEAN);
	}


}