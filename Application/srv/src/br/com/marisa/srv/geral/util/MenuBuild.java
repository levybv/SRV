package br.com.marisa.srv.geral.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.marisa.srv.acesso.business.FuncionalidadeBusiness;
import br.com.marisa.srv.acesso.business.ModuloBusiness;
import br.com.marisa.srv.acesso.vo.FuncionalidadeVO;
import br.com.marisa.srv.acesso.vo.ModuloVO;
import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * Classe utilizada para construção hierárquica do menu, baseado nos acessos às funcionalidades e menus.
 * 
 * @author Levy Villar
 * 
 */
public class MenuBuild {


	/**
	 * Código HTML inicial para criação do menu.
	 */
	private final StringBuffer MAIN_MENU_FIRST_PART_HTML = new StringBuffer("\n	<div id=\"menu\"> \n		<ul class=\"menu\"> \n");


	/**
	 * Código HTML final para criação do menu.
	 */
	private final StringBuffer MAIN_MENU_LAST_PART_HTML = new StringBuffer("			<li><a href=\"javascript:window.location='login.do?operacao=logoff';\" class=\"parent\"><span>Sair</span></a></li> \n		</ul> \n	</div> \n");


	/**
	 * Código HTML inicial da funcionalidade pai do menu.
	 */
	private final String HTML_FUNCIONALIDADE_PAI_FIRST_PART_HTML = " 			<li><a href=\"#\" class=\"parent\"><span>%s</span></a> \n";


	/**
	 * Código HTML final da funcionalidade pai do menu.
	 */
	private final String HTML_FUNCIONALIDADE_PAI_LAST_PART_HTML = " 			</li> \n";


	/**
	 * Código HTML da funcionalidade do menu.
	 */
	private final String HTML_FUNCIONALIDADE = " 			<li><a href=\"javascript:window.location='%s';\"><span>%s</span></a><li> \n";


	/**
	 * Código HTML inicial do item de menu.
	 */
	private final String HTML_ITEM_MENU_FIRST_PART = "<ul> \n";


	/**
	 * Código HTML final do item de menu.
	 */
	private final String HTML_ITEM_MENU_LAST_PART = "</ul> \n";


	/**
	 * Monta a estrutura do menu hierárquico e gera o código HTML para apresentação, baseado nos acessos recebidos como parâmetro.
	 *  
	 * @param acessos
	 * @return Código HTML para apresentação
	 * @throws SRVException
	 */
	public String getMenuHtml(Map acessos) throws SRVException {
		return buildMenu(acessos).toString();
	}

	/**
	 * 
	 * @param acessos
	 * @return
	 * @throws SRVException
	 */
	private StringBuffer buildMenu(Map acessos) throws SRVException {

		StringBuffer mainMenu = new StringBuffer(MAIN_MENU_FIRST_PART_HTML);

		StringBuffer menuItens = new StringBuffer();

		for (Iterator<ModuloVO> itModulo = ModuloBusiness.getInstance().obtemListaModulos(null).iterator(); itModulo.hasNext();) {
			ModuloVO moduloVO = itModulo.next();

			Integer idMenu = moduloVO.getCodModulo();

			StringBuffer sbMenu = buildMenuItem(acessos, FuncionalidadeBusiness.getInstance().obtemListaFuncionalidadesPai(idMenu), idMenu);
			if (!sbMenu.toString().trim().isEmpty()) {
				menuItens.append(String.format(HTML_FUNCIONALIDADE_PAI_FIRST_PART_HTML,new Object[]{moduloVO.getDescricao()}));
				menuItens.append(HTML_ITEM_MENU_FIRST_PART);
				menuItens.append(sbMenu);
				menuItens.append(HTML_ITEM_MENU_LAST_PART);
				menuItens.append(HTML_FUNCIONALIDADE_PAI_LAST_PART_HTML);
			}
		}

		mainMenu.append(menuItens);
		mainMenu.append(MAIN_MENU_LAST_PART_HTML);

		return mainMenu;
	}

	/**
	 * 
	 * @param acessos
	 * @param listaFuncionalidades
	 * @param idMenu
	 * @param nomeMenu
	 * @return
	 * @throws SRVException
	 */
	private StringBuffer buildMenuItem(Map acessos, List<FuncionalidadeVO> listaFuncionalidades, Integer idMenu) throws SRVException {

		StringBuffer menuItem = new StringBuffer();

		for (Iterator<FuncionalidadeVO> itFuncionalidade = listaFuncionalidades.iterator(); itFuncionalidade.hasNext();) {

				FuncionalidadeVO funcionalidadeVO = itFuncionalidade.next();
				Integer idFuncionalidade = funcionalidadeVO.getCodFuncionalidade();
				String descricaoFuncionalidade = funcionalidadeVO.getDescricao();

				List<FuncionalidadeVO> funcionalidadesFilha = FuncionalidadeBusiness.getInstance().obtemFuncionalidadesFilha(idFuncionalidade);

				if ( funcionalidadesFilha.isEmpty() ) {

					if ( acessos.containsKey(idFuncionalidade) ){
						menuItem.append(String.format(HTML_FUNCIONALIDADE, new Object[]{funcionalidadeVO.getUrl(), descricaoFuncionalidade}));
					}

				} else {
					StringBuffer sbItemMenu = buildMenuItem(acessos, funcionalidadesFilha, idMenu);
					if (!sbItemMenu.toString().trim().isEmpty()) {
						menuItem.append(String.format(HTML_FUNCIONALIDADE_PAI_FIRST_PART_HTML,new Object[]{descricaoFuncionalidade}));
						menuItem.append(HTML_ITEM_MENU_FIRST_PART);
						menuItem.append(sbItemMenu);
						menuItem.append(HTML_ITEM_MENU_LAST_PART);
						menuItem.append(HTML_FUNCIONALIDADE_PAI_LAST_PART_HTML);
					}
				}

		}

		return menuItem;
	}

}
