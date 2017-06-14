package br.com.marisa.srv.geral.enumeration;

/**
 * 
 * @author Levy Villar
 *
 */
public enum StatusContratoMetaAceiteEnum {

	/**
	 * 
	 */
	EM_ABERTO(1,"Em Aberto"),
	ELETRONICO(2,"Eletrônico"),
	AUTOMATICO(3,"Automático");

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
	StatusContratoMetaAceiteEnum(int pCodigo, String pDescricao) {
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
