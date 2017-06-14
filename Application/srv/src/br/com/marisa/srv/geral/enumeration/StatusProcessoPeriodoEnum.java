package br.com.marisa.srv.geral.enumeration;

/**
 * 
 * @author Levy Villar
 *
 */
public enum StatusProcessoPeriodoEnum {

	/**
	 * 
	 */
	ABERTO(1,"ABERTO"),
	FECHADO(2,"FECHADO"),
	REABERTO(3,"REABERTO"),
	FECHAR(4,"FECHAR");

	/**
	 * 
	 */
	private int codigo;
	private String descricao;

	/**
	 * 
	 * @param pCodigo
	 * @param pDescricao
	 */
	StatusProcessoPeriodoEnum(int pCodigo, String pDescricao) {
		codigo = pCodigo;
		descricao = pDescricao;
	}

	/**
	 * 
	 * @return
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescricao() {
		return descricao;
	}

}
