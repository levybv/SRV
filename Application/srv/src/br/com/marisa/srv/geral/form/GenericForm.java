package br.com.marisa.srv.geral.form;

import org.apache.struts.action.ActionForm;

/**
 * Data de Criação: 16/06/2010
 * 
 * @author David José Ribeiro
 * @since XXX_vYYYYMMa
 * @version XXX_vYYYYMMa
 */
public class GenericForm extends ActionForm {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;
	private String aMethod;
	private boolean aSearch = false;
	private boolean aResetList = true;

	/**
	 * @return
	 */
	public String getMethod() {
		return this.aMethod;
	}

	/**
	 * @return
	 */
	public boolean isResetList() {
		return this.aResetList;
	}

	/**
	 * @return
	 */
	public boolean isSearch() {
		return this.aSearch;
	}

	/**
	 * @param method
	 */
	public void setMethod(String method) {
		this.aMethod = method;
	}

	/**
	 * @param resetList
	 */
	public void setResetList(boolean resetList) {
		this.aResetList = resetList;
	}

	/**
	 * @param pSearch
	 */
	public void setSearch(boolean pSearch) {
		this.aSearch = pSearch;
	}
}