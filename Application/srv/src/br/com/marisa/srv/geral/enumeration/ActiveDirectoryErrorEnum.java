package br.com.marisa.srv.geral.enumeration;

/**
 * 
 * @author Levy Villar
 *
 */
public enum ActiveDirectoryErrorEnum {

	/**
	 * 
	 */
	INVALID_USER_OR_PASS("52E","DATA 52E","The credentials (username and password) are invalid","Usu�rio/Senha Inv�lido(a)"),
	USER_NOT_FOUND("525","DATA 525","The user could not be found","Usu�rio n�o encontrado"),
	PASSWORD_EXPIRED("532","DATA 532","The user's password has expired","Senha do usu�rio expirada"),
	USER_DISABLED("533","DATA 533","The user's account has been disabled","Conta de usu�rio desativada"),
	USER_EXPIRED("701","DATA 701","The user's account has expired","Conta de usu�rio expirada"),
	PASSWORD_RESET("773","DATA 773","The user account must have it's password reset","Senha do usu�rio deve ser alterada"),
	USER_LOCKED("775","DATA 775","The user account is locked","Usu�rio est� bloqueado");

	/**
	 * 
	 */
	private String subCodigoAd;
	private String strErroAd;
	private String descErroAd;
	private String descErro;

	/**
	 * 
	 * @param subCodigoAd
	 * @param strErroAd
	 * @param descErroAd
	 * @param descErro
	 */
	ActiveDirectoryErrorEnum(String subCodigoAd, String strErroAd, String descErroAd, String descErro) {
		this.subCodigoAd = subCodigoAd;
		this.strErroAd = strErroAd;
		this.descErroAd = descErroAd;
		this.descErro = descErro;
	}

	/**
	 * 
	 * @return
	 */
	public String getSubCodigoAd() {
		return subCodigoAd;
	}

	/**
	 * 
	 * @return
	 */
	public String getStrErroAd() {
		return strErroAd;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescErroAd() {
		return descErroAd;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescErro() {
		return descErro;
	}

}
