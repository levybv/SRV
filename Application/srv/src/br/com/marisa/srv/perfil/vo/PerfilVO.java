package br.com.marisa.srv.perfil.vo;

import java.io.Serializable;

/**
 * Armazena os dados de perfil de um usuário
 * 
 * @author Walter Fontes
 */
public class PerfilVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8628463269638386661L;

	private Integer idPerfil;
	private String descricao;
	private Boolean isAtivo;
	private String isAtivoStr;
	private Boolean isExibeBonus;
	private String isExibeBonusStr;
	private Boolean isValidaFaixaEscala;
	private String isValidaFaixaEscalaStr;
	private Boolean isReabreResultado;
	private String isReabreResultadoStr;
	private String isAtivaIndicadorStr;

	public Boolean getIsValidaFaixaEscala() {
		return isValidaFaixaEscala;
	}

	public void setIsValidaFaixaEscala(Boolean isValidaFaixaEscala) {
		this.isValidaFaixaEscala = isValidaFaixaEscala;
		this.isValidaFaixaEscalaStr = isValidaFaixaEscala!=null&&isValidaFaixaEscala.booleanValue() ? "Sim" : "Não";
	}

	public Boolean getIsAtivo() {
		return isAtivo;
	}

	public void setIsAtivo(Boolean isAtivo) {
		this.isAtivo = isAtivo;
		this.isAtivoStr = isAtivo!=null&&isAtivo.booleanValue() ? "Sim" : "Não";
	}

	public Boolean getIsExibeBonus() {
		return isExibeBonus;
	}

	public void setIsExibeBonus(Boolean isExibeBonus) {
		this.isExibeBonus = isExibeBonus;
		this.isExibeBonusStr = isExibeBonus!=null&&isExibeBonus.booleanValue() ? "Sim" : "Não";
	}

	/**
	 * @return Returns the descricao.
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao
	 *            The descricao to set.
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return Returns the idPerfil.
	 */
	public Integer getIdPerfil() {
		return idPerfil;
	}

	/**
	 * @param idPerfil
	 *            The idPerfil to set.
	 */
	public void setIdPerfil(Integer idPerfil) {
		this.idPerfil = idPerfil;
	}

	public String getIsAtivoStr() {
		return isAtivoStr;
	}

	public void setIsAtivoStr(String isAtivoStr) {
		this.isAtivoStr = isAtivoStr;
	}

	public String getIsExibeBonusStr() {
		return isExibeBonusStr;
	}

	public void setIsExibeBonusStr(String isExibeBonusStr) {
		this.isExibeBonusStr = isExibeBonusStr;
	}

	public String getIsAtivaIndicadorStr() {
		return isAtivaIndicadorStr;
	}

	public void setIsAtivaIndicadorStr(String isAtivaIndicadorStr) {
		this.isAtivaIndicadorStr = isAtivaIndicadorStr;
	}

	public String getIsValidaFaixaEscalaStr() {
		return isValidaFaixaEscalaStr;
	}

	public void setIsValidaFaixaEscalaStr(String isValidaFaixaEscalaStr) {
		this.isValidaFaixaEscalaStr = isValidaFaixaEscalaStr;
	}

	public String getIsReabreResultadoStr() {
		return isReabreResultadoStr;
	}

	public void setIsReabreResultadoStr(String isReabreResultadoStr) {
		this.isReabreResultadoStr = isReabreResultadoStr;
	}

	public Boolean getIsReabreResultado() {
		return isReabreResultado;
	}

	public void setIsReabreResultado(Boolean isReabreResultado) {
		this.isReabreResultado = isReabreResultado;
		this.isReabreResultadoStr = isReabreResultado!=null&&isReabreResultado.booleanValue() ? "Sim" : "Não";
	}

}
