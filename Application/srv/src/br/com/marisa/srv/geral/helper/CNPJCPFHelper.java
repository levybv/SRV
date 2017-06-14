package br.com.marisa.srv.geral.helper;

import br.com.marisa.srv.geral.constants.Constantes;



/**
 * Classe para realizar validações de CPF e CNPJ
 * 
 * @author Walter Fontes
 */
public class CNPJCPFHelper {

	//Construtor privado. Os métodos devem ser estáticos
	private CNPJCPFHelper() {}

	/**
	 * Valida CPF ou CNPJ
	 * 
	 * @param xDoc
	 * @return
	 */
	public static boolean validaCpfCnpj(String xDoc){
		return validaCpf(xDoc) || validaCnpj(xDoc);
	}

	/**
	 * Valida o digito do CPF
	 * 
	 * @param xCPF
	 * @return
	 */
	public static boolean validaCpf(String xCPF){

		try {
			if (xCPF == null) return false;
			xCPF = String.valueOf(retiraCaracteresAlpha(xCPF));
			while (xCPF.length() < 11) {
				xCPF = "0" + xCPF;
			}
			
			if ("00000000000".equals(xCPF) || "11111111111".equals(xCPF) || "22222222222".equals(xCPF) || "33333333333".equals(xCPF) ||
				"44444444444".equals(xCPF) || "55555555555".equals(xCPF) || "66666666666".equals(xCPF) || "77777777777".equals(xCPF) ||
				"88888888888".equals(xCPF) || "99999999999".equals(xCPF)) {
				return false;
			}
			
			//Testa se o CPF Ã© vÃ¡lido ou nÃ£o
			int d1,d4,xx,nCount,resto,digito1,digito2;
			String Check;
			String Separadores = "/-.";
			d1 = 0; d4 = 0; xx = 1;
			for (nCount = 0; nCount < xCPF.length() -2; nCount++){
				String s_aux = xCPF.substring(nCount, nCount+1);
				if (Separadores.indexOf(s_aux) == -1) {
					d1 = d1 + ( 11 - xx ) * Integer.valueOf (s_aux).intValue();
					d4 = d4 + ( 12 - xx ) * Integer.valueOf (s_aux).intValue();
					xx++;
				}
			}
			resto = (d1 % 11);

			if (resto < 2){
				digito1 = 0;
			}else{
				digito1 = 11 - resto;
			}

			d4 = d4 + 2 * digito1;
			resto = (d4 % 11);
			if (resto < 2){
				digito2 = 0;
			}else{
				digito2 = 11 - resto;
			}

			Check = String.valueOf(digito1) + String.valueOf(digito2);
			String s_aux2 = xCPF.substring (xCPF.length()-2, xCPF.length());
			if (s_aux2.compareTo (Check) != 0){
				return false;
			}
			return true;
		}catch (Exception e){
			return false;
		}
	}

	/**
	 * Valida o digito do CNPJ
	 * 
	 * @param xCNPJ
	 * @return
	 */
	public static boolean validaCnpj(String xCNPJ){
		try{
			//Testa se o CNPJ Ã© vÃ¡lido ou nÃ£o
			int d1,d4,xx,nCount,fator,resto,digito1,digito2;
			String Check, s_aux;
			String Separadores = "/-.";
			d1 = 0;
			d4 = 0;
			xx = 0;
			for (nCount = 0; nCount < xCNPJ.length()-2; nCount++){
				s_aux = xCNPJ.substring (nCount, nCount+1);
				if (Separadores.indexOf(s_aux) == -1){
					if (xx < 4){
						fator = 5 - xx;
					}else{
						fator = 13 - xx;
					}
					d1 = d1 + Integer.valueOf (s_aux).intValue() * fator;
					if (xx < 5){
						fator = 6 - xx;
					}else{
						fator = 14 - xx;
					}
					d4 += Integer.valueOf (s_aux).intValue() * fator;
					xx++;
				}
			}
			resto = (d1 % 11);
			if (resto < 2){
				digito1 = 0;
			}else{
				digito1 = 11 - resto;
			}
			d4 = d4 + 2 * digito1;
			resto = (d4 % 11);
			if (resto < 2){
				digito2 = 0;
			}else{
				digito2 = 11 - resto;
			}
			Check = String.valueOf(digito1) + String.valueOf(digito2);
			if (Check.compareTo(xCNPJ.substring(xCNPJ.length()-2, xCNPJ.length() ))!=0){
				return false;
			}
			return true;
		}catch (Exception e){
			return false;
		}
	}

	/**
	 * Método para retirar qualquer caractere não numérico do CPF ou CNPJ
	 * 
	 * @param cpfCnpj
	 * @return
	 */
	public static final long retiraCaracteresAlpha(String cpfCnpj) {
		return Long.parseLong(cpfCnpj.replaceAll("\\D+", ""));
	}
	
	/**
	 * Realiza a formatação do CPF recebido
	 * 
	 * @param cpf
	 * @return
	 */
	public static String formataCPF(long cpf) {
		return formataCPF(String.valueOf(cpf));
	}

	/**
	 * Realiza a formatação do CPF recebido
	 * 
	 * @param cpf
	 * @return
	 */
	public static String formataCPF(String cpf) {

		if (cpf == null || cpf.length() < 3) {
			return "";
		}

		cpf = String.valueOf(retiraCaracteresAlpha(cpf));
		cpf=colocaZeros(cpf,11);
		int leng = 0;

		String digito = "";
		String numero = "";
		String tmp = "";

		digito = cpf.substring(cpf.length() - 2);
		tmp = cpf.substring(0, cpf.length() - 2);
		while (tmp.length() > 0) {
			if ((tmp.length() - 3) > 0) {
				leng = tmp.length() - 3;
			} else {
				leng = tmp.length();
			}

			numero = tmp.substring(leng) + numero;
			tmp = tmp.substring(0, leng);

			if ((tmp.length() - 3) > 0) {
				numero = "." + numero;
			} else {
				numero = tmp + "." + numero;
				tmp = "";
			}
		}

		return numero + "-" + digito;
	}
	
	
	/**
	 * Formata CNPJ
	 * 
	 * @param pCNPJ
	 * @return String
	 */
	public static String formataCnpj(String pCNPJ) {
		String vCNPJ = pCNPJ;
		if ((pCNPJ == null) || (pCNPJ.equals(Constantes.BLANK))) {
			return pCNPJ;
		}

		while (vCNPJ.length() < 14) {
			vCNPJ = Constantes.STRING_ZERO + vCNPJ;
		}
		return formata(vCNPJ, "##.###.###/####-##");
	}	
	
	
	/**
	 * Realiza a formatação do valor de acordo com a mascara enviada
	 * 
	 * @param valor
	 * @param mascara
	 * @return
	 */
	public static String formata(String valor, String mascara) {

		if ((valor == null) || valor.equals(Constantes.BLANK)) {
			return valor;
		}

		String dado = Constantes.BLANK;
		// remove caracteres nao numericos
		for (int i = 0; i < valor.length(); i++) {
			char c = valor.charAt(i);
			if (Character.isDigit(c)) {
				dado += c;
			}
		}

		int indMascara = mascara.length();
		int indCampo = dado.length();

		for (; (indCampo > 0) && (indMascara > 0);) {
			if (mascara.charAt(--indMascara) == '#') {
				indCampo--;
			}
		}

		String saida = Constantes.BLANK;

		for (; indMascara < mascara.length(); indMascara++) {
			saida += ((mascara.charAt(indMascara) == '#') ? dado.charAt(indCampo++) : mascara.charAt(indMascara));
		}

		return saida;
	}	
	
	

	/**
	 * Inclui zeros na frente para formação de CPF
	 * @param cpf
	 * @param qtde
	 * @return
	 */
	private static String colocaZeros(String cpf,int qtde) {
		if (ObjectHelper.isNotNull(cpf)) {
			if(cpf.length() < 11){
				int zero = 11 - cpf.length();
				String szero="";
				for(int i=1;i <=zero;i++){
					szero+="0";
				}
				return szero+cpf;
			}
		}
		return cpf;
	}
}
