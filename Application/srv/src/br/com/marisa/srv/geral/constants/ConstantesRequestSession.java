package br.com.marisa.srv.geral.constants;

/**
 * 
 * Constantes de todas as variáveis de request e de sessão do sistema 
 * 
 * @author Walter Fontes
 * @since 08/08/2011
 */
public class ConstantesRequestSession {

	/**
	 * Calendario
	 */
	public static final String REQUEST_PERIODOS_CALENDARIO = "menuCadastro";

	//Botoes
	public static final String BOTAO_IMPRIMIR = "stBtImprimir";
	public static final String BOTAO_INCLUIR = "stBtIncluir";
	public static final String BOTAO_ALTERAR = "stBtAlterar";
	public static final String BOTAO_REMOVER = "stBtRemover";
	public static final String BOTAO_CALCULAR = "stBtCalcular";
	public static final String BOTAO_STATUS = "stBtStatus";
	public static final String BOTAO_ACEITE = "stBtAceite";
	public static final String BOTAO_IMPORTAR = "stBtImportar";
	public static final String BOTAO_EXPORTAR = "stBtExportar";

	/**
	 * Menu
	 */
	public static final String SESSION_MENU_CONTROLE_ACESSO 		= "menuControleAcesso";
	public static final String SESSION_SUBMENU_USUARIO 				= "submenuUsuario";
	public static final String SESSION_SUBMENU_PERFIL 				= "submenuPerfil";

	public static final String SESSION_MENU_PARAMETROS 				= "menuParametros";
	public static final String SESSION_SUBMENU_CALENDARIO_COMERCIAL	= "submenuCalendarioComercial";
	public static final String SESSION_SUBMENU_CLASSE_HAY 			= "submenuClasseHay";
	public static final String SESSION_SUBMENU_CARGO 				= "submenuCargo";
	public static final String SESSION_SUBMENU_FILIAL 				= "submenuFilial";
	public static final String SESSION_SUBMENU_INDICADOR			= "submenuIndicador";
	public static final String SESSION_SUBMENU_METAS_FILIAL			= "submenuMetasFilial";
	public static final String SESSION_SUBMENU_METAS_FUNCIONARIO	= "submenuMetasFuncionario";
	public static final String SESSION_SUBMENU_ESCALA				= "submenuEscala";
	public static final String SESSION_SUBMENU_FAIXA_ESCALA			= "submenuFaixaEscala";
	public static final String SESSION_SUBMENU_PONDERACAO			= "submenuPonderacao";
	public static final String SESSION_SUBMENU_SALARIO_BASE			= "submenuSalarioBase";
	public static final String SESSION_SUBMENU_AGRUPA_FILIAL		= "submenuAgrupaFilial";
	public static final String SESSION_SUBMENU_FUNCIONARIO			= "submenuFuncionario";
	public static final String SESSION_SUBMENU_ACOMP_META_FILIAL	= "submenuAcompMetaFilial";
	public static final String SESSION_SUBMENU_PROCESSO_PERIODO 	= "submenuProcessoPeriodo";
	public static final String SESSION_SUBMENU_DIGITALIZADO			= "submenuDigitalizado";
	public static final String SESSION_SUBMENU_AJUSTE_FUNCIONARIO	= "submenuAjusteFuncionario";
	public static final String SESSION_SUBMENU_AGENDAMENTO_CARGA	= "submenuAgendamentoCarga";
	public static final String SESSION_SUBMENU_CALENDARIO_BONUS		= "submenuCalendarioBonus";

	public static final String SESSION_MENU_RELATORIO 								= "menuRelatorio";
	public static final String SESSION_MENU_PAI_RELATORIO_BONUS						= "subMenuPaiRelatorioBonus";
	public static final String SESSION_MENU_PAI_RELATORIO_CAMPANHA					= "subMenuPaiRelatorioCampanha";
	
	public static final String SESSION_SUBMENU_RELATORIO_LOJA						= "submenuRelatorioLoja";
	public static final String SESSION_SUBMENU_RELATORIO_VM							= "submenuRelatorioVM";
	public static final String SESSION_SUBMENU_RELATORIO_SAX						= "submenuRelatorioSAX";
	public static final String SESSION_SUBMENU_RELATORIO_BONUS_STATUS				= "submenuRelatorioBonusStatus";
	public static final String SESSION_SUBMENU_RELATORIO_BONUS_PAGAMENTO			= "submenuRelatorioBonusPagamento";
	public static final String SESSION_SUBMENU_RELATORIO_BONUS_PAGAMENTO_PROP		= "submenuRelatorioBonusPagamentoProp";
	public static final String SESSION_SUBMENU_RELATORIO_BONUS_CONFERENCIA			= "submenuRelatorioBonusConferencia";
	public static final String SESSION_SUBMENU_RELATORIO_DESEMPENHO_COLABORADOR		= "submenuRelatorioDesempenhoColaborador";

	public static final String SESSION_SUBMENU_REL_INDICADOR 		= "submenuRelIndicador";
	public static final String SESSION_SUBMENU_EXTRATO_FUNCIONARIO 	= "submenuExtratoFuncionario";
	public static final String SESSION_SUBMENU_BONUS 				= "submenuBonus";
	public static final String SESSION_SUBMENU_BONUS_CARGO 			= "submenuBonusCargo";
	public static final String SESSION_SUBMENU_BONUS_GRUPO_REM_VAR	= "submenuBonusGrupoRemVar";
	
	//Alexandre Pincerato
	public static final String SESSION_SUBMENU_BONUS_CONFERENCIA 			= "submenuBonusConferencia";
	public static final String SESSION_SUBMENU_BONUS_PAGAMENTO 				= "submenuBonusPagamentoBonus";
	public static final String SESSION_SUBMENU_BONUS_COLABORADOR 			= "submenuBonusDesempenhoColaboradorBonus";
	
	public static final String SESSION_MENU_CONSULTA 				= "menuConsultas";
	

	/**
	 * Dados do usuário logado e suas permissões
	 */
	public static final String SESSION_USUARIO_BEAN = "usuarioBean";

	/**
	 * Controle de Token para evitar problema de refresh
	 */
	public static final String SESSION_TOKEN_REFRESH = "sessionTokenRefresh";

}