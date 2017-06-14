package br.com.marisa.srv.geral.constants;

import java.math.BigDecimal;

/**
 * Data de Criação: 05/08/2011
 * Constantes do sistema SRV
 * 
 * @author Walter Fontes
 */
public class Constantes {
	
	// logs
	public static final String SYS_PROPERTIES = "br/com/marisa/srv/geral/parametros/parametros";
	
	public static final String DOMAIN_MARISA = "central.marisa.com.br";
	//public static final String DOMAIN_MARISA = "homol.marisa.com.br";
	//public static final String DOMAIN_MARISA = "128.1.1.44";
	

	//situacao RH tb funcionarios
	public static final int  DEMITIDOS_EM_MESES_ANTERIORES = 2;

	public static final Integer SRV_ID_USER_ADMIN = new Integer(1);
	public static final Integer SRV_ID_USER_PADRAO_SISTEMA = new Integer(2);
	public static final String SRV_USER_ADMIN = "admin";

	public static final int NUM_ANO_SUBINDICADOR = 2015;
	public static final int NUM_ANO_BASE_ACEITE_ELETRONICO = 2016;

	public static final Integer NUM_MES_DEZEMBRO = new Integer(12);

	//grupo de REMUNERACAO VARIAVEL
	public static final int VM = 4;
	public static final int PSF = 3;

	//Unidades
	public static final int UNIDADE_VALOR_SEM_RS = 0;
	public static final int UNIDADE_VALOR		 = 1;
	public static final int UNIDADE_PERCENTUAL	 = 2;
	public static final int UNIDADE_UNIDADE		 = 3;
	public static final int UNIDADE_PONTOS		 = 4;
	public static final int UNIDADE_ORCAMENTO	 = 5;
	public static final int UNIDADE_PECAS_HOMEM_HORA = 6;
	public static final int UNIDADE_DIAS = 7;
	public static final int UNIDADE_INDICE = 8;
	public static final int UNIDADE_PA = 9;
	public static final int UNIDADE_PEDIDOS_HORA = 10;
	public static final int UNIDADE_RETORNO_CUSTO = 11;
	public static final int UNIDADE_PECAS_HORA = 12;
	public static final int UNIDADE_PECAS_PEDIDO = 13;
	public static final int UNIDADE_PECAS_TKT = 14;
	public static final int UNIDADE_AUDITORIA_DIA = 15;

	//Sentido do Indicador
	public static final String SENTIDO_INDICADOR_MELHOR_CIMA = "C";
	public static final String SENTIDO_INDICADOR_MELHOR_BAIXO = "B";

	//Relatorios
	public static final int RELATORIO_TIPO_LOJA = 1;
	public static final int RELATORIO_TIPO_SAX = 2;
	public static final int RELATORIO_TIPO_VM = 3;
	public static final int RELATORIO_TIPO_CALL_CENTER_OUTROS = 4;
	public static final int RELATORIO_TIPO_BONUS_POR_CONFERENCIA = 5;
	public static final int RELATORIO_TIPO_BONUS_POR_PAGAMENTO = 6;
	public static final int RELATORIO_TIPO_BONUS_DESEMPENHO_POR_COLABORADOR = 7;
	public static final int RELATORIO_TIPO_BONUS_POR_PAGAMENTO_PROPORCIONALIDADE = 8;

	public static final int EXCEL_MAX_REGISTROS = 20000;
	public static final int BUFFER_SIZE_1024 = 1024;


	//token 
	//facilita para achar o token na sessão
	public static final String PRE_TOKEN= "PREFIXO_SESSION_TOKEN_"; 
	public static final String SU_TOKEN = "_EXECUTANDO";	
	
    //Tipos de Remuneração Variável
	public static final String DESCR_TIPO_REM_VAR_CORPORATIVO = "CORPORATIVO";
	public static final Integer ID_TIPO_REM_VAR_CORPORATIVO = new Integer(1);
	public static final Integer ID_TIPO_REM_VAR_REMUNERACAO_LOJA = new Integer(2);
	public static final Integer ID_TIPO_REM_VAR_CALL_CENTER = new Integer(3);


	//Grupos de Remuneração Variável
	public static final Integer COD_GRP_REM_VAR_CORPORATIVO = new Integer(1);
	public static final Integer COD_GRP_REM_VAR_LIDERANCA_LOJA = new Integer(3);
	public static final Integer COD_GRP_REM_VAR_OPERACIONAL_LOJA = new Integer(5);
	
	//Indicadores
	public static final Integer COD_GRUPO_INDIC_CORPORATIVO = 1;
	public static final Integer COD_GRUPO_INDIC_INDIVIDUAL = 7;
	public static final Integer COD_GRUPO_INDIC_TLMKT = 10;
	public static final Integer COD_INDIC_AGRUPAMENTO_CORPORATIVO = 48;
	public static final Integer COD_INDIC_AGRUPAMENTO_ADMINISTRATIVO = 52;
	public static final String INDICADOR_AGRUPAMENTO_LOJA_LIDER = "AGRUPA_REM_LOJA_LID";

	
	/**
	 */
	public static final String CD_VERDADEIRO = "S";

	/**
	 */
	public static final String CD_FALSO = "N";

	/**
	 */
	public static final String PONTO_VIRGULA = ";";

	/**
	 */
	public static final String MASCARA_CEP = "#####-###";

	/**
	 */
	public static final String BLANK = "";

	public static final BigDecimal MENOS_UM = new BigDecimal("-1");

	/**
	 */
	public static final BigDecimal ZERO = new BigDecimal("0");

	/**
	 * 
	 */
	public static final BigDecimal UM = new BigDecimal("1");

	/**
	 * 
	 */
	public static final BigDecimal OITENTA = new BigDecimal("80");

	/**
	 * 
	 */
	public static final BigDecimal CEM = new BigDecimal("100");

	/**
	 * 
	 */
	public static final BigDecimal DEZ_MIL = new BigDecimal("10000");

	/**
	 */
	public static final String STRING_ZERO = "0";
	
	/** 
	 */ 
	public static final String CD_PENDENCIAMENTO = "10";
	
	/**
	 * CASES
	 */
	public static final String UPPER_CASE = "U";
	public static final String LOW_CASE = "L";
	public static final String CAPTALIZADO_CASE = "C";
	
	public static final String[] MESES = {"Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};
	
	// configuracao de pagina
	public static final String REQUEST_QUANTIDADE = "quantidade";
	public static final int QTD_OBJETOS_POR_PAGINA_DEFAUT = -1;
	public static final String REQUEST_NUMERO_PAGINA = "numeroPagina";

	public static final int TIPO_INT_NULO = -1;
	public static final int CODIGO_EMPRESA = 1;
	public static final int CODIGO_FILIAL_EC = 900;

}