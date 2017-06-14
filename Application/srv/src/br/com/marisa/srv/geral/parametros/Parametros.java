package br.com.marisa.srv.geral.parametros;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * Classe para manter os parametros do sistema
 *  
 * @author Walter Fontes
 */
public class Parametros {
	
	//Log4J
	private static final Logger log = Logger.getLogger(Parametros.class);

	/**
	 * Obtém o arquivo de parâmetros
	 * 
	 * @return
	 */
	private static ResourceBundle getResourceBundle() throws SRVException {
		try {
			return ResourceBundle.getBundle("br/com/marisa/srv/geral/parametros/parametros");
		} catch (Exception e) {
			throw new SRVException(log, "Parametros.properties nao localizado.", e);
		}
	}
	
	
	/**
	 * Obtém obtem o value da key no arquivo de propriedades
	 * 
	 * @return
	 */
	public static String getValue(String key) throws SRVException {
		try {
			return getResourceBundle().getString(key);
		} catch (Exception e) {
			throw new SRVException(log, "Nao foi possivel obter o parametro " + key + " no parametros.properties.", e);
		}
			 
	}	
}