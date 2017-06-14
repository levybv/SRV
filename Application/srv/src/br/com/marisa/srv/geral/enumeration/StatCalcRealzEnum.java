package br.com.marisa.srv.geral.enumeration;

/**
 * 
 * @author Levy Villar
 *
 */
public enum StatCalcRealzEnum {

	/**
	 * 
	 */
	NENHUM(0,"Nenhum"),
	INICIADO(1,"Iniciado"),
	EM_ACEITE(2,"Em Aceite"),
	EM_ANDAMENTO(3,"Em Andamento"),
	FINALIZADO(9,"Finalizado");

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
	StatCalcRealzEnum(int pCodigo, String pDescricao) {
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
