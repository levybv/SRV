package br.com.marisa.srv.geral.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Métodos auxiliares para objetos
 * 
 * @author Walter Fontes
 */
public class ObjectHelper {
	
	
	/**
	 * Verifica se um objeto é não nulo
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isNotNull(Object o){
		return o != null;
	}

	/**
	 * Verifica se um objeto não está vazio
	 * 
	 * @param o
	 * @return
	 */	
	public static boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}
	

	/**
	 * Verifica se um objeto está vazio
	 * 
	 * @param o
	 * @return
	 */		
	public static boolean isEmpty(Object o){
		if(isNull(o)){
			return true;
		}
		if (o.getClass().equals(Integer.class)){
			return isEmpty(Integer.parseInt(String.valueOf(o)));

		} else if (o.getClass().equals(Long.class)){
			return isEmpty(Long.parseLong(String.valueOf(o)));

		} else if (o.getClass().equals(Double.class)){
			return isEmpty(Double.parseDouble(String.valueOf(o)));

		} else if (o.getClass().equals(Boolean.class)){
			return isEmpty(Boolean.parseBoolean(String.valueOf(o)));

		} else if (o.getClass().equals(Collection.class)){
			return ((Collection) o).isEmpty();

		} else if (o.getClass().equals(List.class) || o.getClass().equals(ArrayList.class)){
			return ((List) o).isEmpty();

		} else if (o.getClass().equals(String.class)){
			return ((String) o).trim().length() == 0;
		}
		return false;
	}

	
	/**
	 * verifica se o int é vazio, bem no caso do int verifica se ele é diferente de zero
	 * @param i
	 * @return	true, se o int for 0.
	 */
	public static boolean isEmpty(int i) {
		return i==0;
	}


	/**
	 * verifica se o long é vazio, bem no caso do long verifica se ele é diferente de zero
	 * @param i
	 * @return	true, se o long for 0.
	 */
	public static boolean isEmpty(long i) {
		return i==0;
	}

	/**
	 * verifica se o objeto é nulo
	 * 
	 * @param o 
	 * @return	true se objecto for nulo.
	 */
	public static boolean isNull(Object o) {
		return o == null;
	}

	public static boolean isEmpty(double d) {
		return d==0.0D;
	}

}
