package br.com.marisa.srv.geral.helper;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.marisa.srv.geral.constants.Constantes;

/**
 * Data de Criação: 18/06/2010
 * 
 * @author David José Ribeiro
 * @since XXX_vYYYYMMa
 * @version XXX_vYYYYMMa
 */
public class StringHelper {

	static char[] caracteresAcentuados = new char[] { 
		'à', 'á', 'â', 'ã', 'ä', 'å', 'ç', 'Ð', 'ð', 'è', 
		'é', 'ê', 'ë', 'ì', 'í', 'î', 'ï', 'ñ', 'ò', 'ó', 
		'ô', 'õ', 'ö', 'ù', 'ú', 'û', 'ü', 'ý', 'ÿ', 'À', 
		'Á', 'Â', 'Ã', 'Ä', 'È', 'É', 'Ê', 'Ë', 'Ì', 'Í', 
		'Î', 'Ï', 'Ñ', 'Ò', 'Ó', 'Ô', 'Õ', 'Ö', 'Ù', 'Ú', 
		'Û', 'Ü', 'Ý'};

	static String[] replyCaracteresAcentuados = new String[] { 
		"a", "a", "a", "a", "a", "a", "c", "d", "o", "e", 
		"e", "e", "e", "i", "i", "i", "i", "n", "o", "o", 
		"o", "o", "o", "u", "u", "u", "u", "y", "y", "A", 
		"A", "A", "A", "A", "E", "E", "E", "E", "I", "I", 
		"I", "I", "N", "O", "O", "O", "O", "O", "U", "U", 
		"U", "U", "Y"};

	/**
	 * Completa o número passado como parâmetro com 'pQtZeros' vZeros a esquerda
	 * 
	 * @author David José Ribeiro
	 * @since 2009-07-23
	 * @param pNumero
	 *            Número a ser modificado
	 * @param pTmFormato
	 * @return String
	 */
	public static String completarNumeroComZerosEsquerda(long pNumero, int pTmFormato) {

		DecimalFormat vFormato = null;
		String vZeros = Constantes.BLANK;

		for (int i = 0; i < pTmFormato; i++) {
			vZeros = vZeros + Constantes.STRING_ZERO;
		}

		vFormato = new DecimalFormat(vZeros);

		return vFormato.format(pNumero);
	}

	/**
	 * Completa string com caractere a esquerda
	 * 
	 * @param pString
	 * @param pCaractere
	 * @param pTamanho
	 * @return String
	 */
	public static String completarStringCaractereEsquerda(String pString, String pCaractere, int pTamanho) {

		String vString = pString;

		if (vString.length() == pTamanho) {
			return vString;
		}

		while (vString.length() < pTamanho) {
			vString = pCaractere + vString;
		}

		return vString;
	}

	/**
	 * -
	 * 
	 * @param pArrayBytes
	 * @return
	 */
	public static String getArrayBytesComoString(byte[] pArrayBytes) {
		int tamArrayBytes = pArrayBytes.length;

		ByteArrayOutputStream baos = new ByteArrayOutputStream(tamArrayBytes);
		baos.write(pArrayBytes, 0, tamArrayBytes);

		return baos.toString();
	}

	/**
	 * @param pTelefone
	 * @return String
	 */
	public static String getDDD(final String pTelefone) {

		String vDDD = Constantes.BLANK;

		if (pTelefone != null) {
			if (!pTelefone.equals(Constantes.BLANK)) {
				vDDD = pTelefone.substring(1, 3);
			}
		}

		return vDDD;
	}

	/**
	 * @param pCliMatrizDocIdent
	 * @param pCliMatrizDocEnd
	 * @param pCliMatrizDocRenda
	 * @param pCliMatrizDocEmancipado
	 * @return List
	 */
	public static List getDocumentosSelecionados(String pCliMatrizDocIdent, String pCliMatrizDocEnd, String pCliMatrizDocRenda, String pCliMatrizDocEmancipado) {

		List vLista = new ArrayList();

		String[] vSplit = null;

		if (pCliMatrizDocIdent != null) {

			vSplit = pCliMatrizDocIdent.split("#");

			for (int vI = 1; vI < vSplit.length; vI++) {
				vLista.add(vSplit[vI]);
			}

		}

		if (pCliMatrizDocEnd != null) {

			vSplit = pCliMatrizDocEnd.split("#");

			for (int vI = 1; vI < vSplit.length; vI++) {
				vLista.add(vSplit[vI]);
			}

		}

		if (pCliMatrizDocRenda != null) {

			vSplit = pCliMatrizDocRenda.split("#");

			for (int vI = 1; vI < vSplit.length; vI++) {
				vLista.add(vSplit[vI]);
			}

		}

		if (pCliMatrizDocEmancipado != null) {

			vSplit = pCliMatrizDocEmancipado.split("#");

			for (int vI = 1; vI < vSplit.length; vI++) {
				vLista.add(vSplit[vI]);
			}

		}

		return vLista;
	}

	/**
	 * @return String
	 */
	public static String getHoraMinuto() {

		Calendar vCalendar = Calendar.getInstance();
		int vHora = vCalendar.get(Calendar.HOUR);
		int vMinuto = vCalendar.get(Calendar.MINUTE);

		// Calendar.AM_PM = 1 (PM)
		if (vCalendar.get(Calendar.AM_PM) == 1) {
			vHora += 12;
		}

		if (vMinuto < 10) {
			return "[" + vHora + ":0" + vMinuto + "] ";
		}

		return "[" + vHora + ":" + vMinuto + "] ";

	}

	/**
	 * @param pTelefone
	 * @return String
	 */
	public static String getTelefone(final String pTelefone) {

		String vTelefone = Constantes.BLANK;

		if (pTelefone != null && !pTelefone.equals(Constantes.BLANK)) {
			vTelefone = StringHelper.retirarMascara(pTelefone);
			vTelefone = vTelefone.substring(4, 12);
		}

		return vTelefone;
	}

	/**
	 * @param pValor
	 * @return String
	 */
	public static String retirarMascara(final String pValor) {

		if ((pValor == null) || pValor.equals(Constantes.BLANK)) {
			return Constantes.BLANK;
		}

		return pValor.replaceAll("[_]", "").replaceAll("[.]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[,]", "").replaceAll("[R$ ]", "");
	}

	/**
	 * Retorna uma colecao de tokens separados pelo "pSeparador".
	 * 
	 * @param pConteudo
	 *            pConteudo Contem as chaves que devem ser separados
	 * @param pSeparador
	 *            pSeparador Sequência de caracteres que separam as chaves
	 * @return ArrayList Colecao de chaves encontradas.
	 */
	public static ArrayList split(String pConteudo, String pSeparador) {
		ArrayList lista = new ArrayList();
		int posAtual = 0;
		int posSeparador = 0;
		int contador = 0;
		boolean inConcluido = false;

		if ((pConteudo == null) || (pSeparador == null)) {
			return lista;
		}

		while (!inConcluido) {
			posSeparador = pConteudo.indexOf(pSeparador, posAtual);

			if (posSeparador == -1) {
				lista.add(pConteudo.substring(posAtual));
				inConcluido = true;
			} else {
				lista.add(pConteudo.substring(posAtual, posSeparador));
			}

			posAtual = posSeparador + pSeparador.length();
			contador++;
		}

		return lista;
	}

	/**
	 * @param pString
	 * @param pSeparador
	 * @param posicao
	 * @return String
	 */
	public static String split(final String pString, final String pSeparador, final int posicao) {

		if (pString != null) {

			if (pString.split(pSeparador).length > posicao) {
				return pString.split(pSeparador)[posicao];
			}

		}

		return pString;
	}

	/**
	 * In string <var>string</var>, replace all occurrences of <var>from</var> by <var>to</var> and return the resulting string.
	 * <p>
	 * If string <var>from</var> is empty, this will be considered to match no single occurrence of it in the target string, hence no replacements
	 * will be made. Note that <var>to</var> cannot be null. It can be the empty string, resulting in deletion of the substring; see also method
	 * delete(String,String);
	 * </p>
	 * <p>
	 * </p>
	 * 
	 * @param string
	 *            The string in which the replacements will be made
	 * @param from
	 *            String that, when it occurs in string, will be replaced
	 * @param to
	 *            The replacement of from in string
	 * @return A new string
	 * @see #delete(String,String)
	 */
	public static String substituir(String string, String from, String to) {

		if (from.equals(Constantes.BLANK)) {
			return string;
		}

		StringBuffer buf = new StringBuffer(2 * string.length());

		int previndex = 0;
		int index = 0;
		int flen = from.length();

		while (true) {
			index = string.indexOf(from, previndex);

			if (index == -1) {
				buf.append(string.substring(previndex));

				break;
			}
			buf.append(string.substring(previndex, index) + to);
			previndex = index + flen;
		}

		return buf.toString();
	}

	/**
	 * -
	 * 
	 * @param string
	 * @param from
	 * @param to
	 * @return
	 */
	public static String substituir(String string, String[] from, String to) {
		StringBuffer buf;
		String temp = string;

		int previndex = 0;
		int index = 0;
		int flen = 0;

		for (int i = 0; i < from.length; i++) {
			buf = new StringBuffer(2 * temp.length());

			if (from[i].equals(Constantes.BLANK)) {
				continue;
			}

			previndex = 0;
			index = 0;
			flen = from[i].length();

			while (true) {
				index = temp.indexOf(from[i], previndex);

				if (index == -1) {
					buf.append(temp.substring(previndex));

					break;
				}

				buf.append(temp.substring(previndex, index) + to);
				previndex = index + flen;
			}

			temp = buf.toString();
		}

		return temp;
	}

	/**
	 * @param pValor
	 * @param pTamanho
	 * @return String
	 */
	public static String substring(String pValor, int pTamanho) {

		if (pValor != null) {

			if (pValor.length() <= pTamanho) {
				return pValor;
			}

			return pValor.substring(0, pTamanho - 1);
		}

		return pValor;
	}

	/**
	 * @param pValor
	 * @return String
	 */
	public static String upperCase(String pValor) {

		if (pValor != null) {
			return pValor.toUpperCase().trim();
		}

		return pValor;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String limpaAcentuacao(String str) {
		StringBuffer strBff = new StringBuffer(str);
		for (int x = (strBff.length() - 1); x >= 0; x--) {
			
			String novaString = String.valueOf(strBff.charAt(x));
			for (int y = 0; y < caracteresAcentuados.length; y++) {
				if (caracteresAcentuados[y] == strBff.charAt(x)) {
					novaString = replyCaracteresAcentuados[y];
				}
			}			
			strBff.replace(x, (x + 1), novaString);
		}
		
		StringBuffer strBff2 = new StringBuffer("");
		
    	for(int i=0; i<strBff.toString().length(); i++){
    		if ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 -*={}[]:;,\\+_()/$%'.\"/".indexOf(strBff.toString().charAt(i)) > -1) {
    			strBff2.append(strBff.charAt(i));
    		}
      	}
		
		return strBff2.toString().trim();
	}

}