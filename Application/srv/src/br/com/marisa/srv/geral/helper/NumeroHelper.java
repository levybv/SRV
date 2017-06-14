package br.com.marisa.srv.geral.helper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import br.com.marisa.srv.geral.constants.Constantes;

/**
 * Classe auxiliar para tratamento de números
 * 
 * @author Walter Fontes
 */
public class NumeroHelper {
	
	
	/**
	 * Formata valor
	 * 
	 * @return String
	 */
	public static String formataValor(Double valor) {

		if (valor == null) {
			return "";
		}
		
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setGroupingSeparator('.');
		dfs.setDecimalSeparator(',');
		
		DecimalFormat df = new DecimalFormat("#,###,###,###,###,###,##0.00");
		df.setDecimalFormatSymbols(dfs);
		return df.format(valor.doubleValue());
	}
	
	public static String formataValor(Double valor, Integer qtdDecimais, Integer unidade) {

		if (valor == null) {
			return "";
		}
		
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setGroupingSeparator('.');
		dfs.setDecimalSeparator(',');
		
		DecimalFormat df1 = new DecimalFormat("#,###,###,###,###,###,##0.####");
		DecimalFormat df2 = new DecimalFormat("#,###,###,###,###,###,##0.00##");

		df1.setDecimalFormatSymbols(dfs);
		String value = df1.format(valor.doubleValue());

		if (unidade != null) {
			switch (unidade.intValue()) {
				
				case Constantes.UNIDADE_VALOR_SEM_RS:
					df1.setDecimalFormatSymbols(dfs);
					return df1.format(valor.doubleValue());
				
				case Constantes.UNIDADE_VALOR:
					df2.setDecimalFormatSymbols(dfs);
					return "R$ " + df2.format(valor.doubleValue());
					
				case Constantes.UNIDADE_UNIDADE:
					df1.setDecimalFormatSymbols(dfs);
					return df1.format(valor.doubleValue());
					
				case Constantes.UNIDADE_PERCENTUAL:
					df2.setDecimalFormatSymbols(dfs);
					return df2.format(valor.doubleValue()) + "%";
					
				case Constantes.UNIDADE_PONTOS:
					df1.setDecimalFormatSymbols(dfs);
					return df1.format(valor.doubleValue());
			}
		}

		return value;

	}

	
	/**
	 * Formata valor
	 * 
	 * @return String
	 */
	public static String formataNumero(Integer valor) {

		if (valor == null) {
			return "";
		}
		
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setGroupingSeparator('.');
		dfs.setDecimalSeparator(',');
		
		DecimalFormat df = new DecimalFormat("#,###,###,###,###,###,##0");
		df.setDecimalFormatSymbols(dfs);
		return df.format(valor.doubleValue());
	}
	
	
	/**
	 * Formata valor
	 * 
	 * @return String
	 */
	public static String formataNumero(Double valor, Integer unidade) {

		if (unidade != null && unidade == Constantes.UNIDADE_ORCAMENTO) {
			return "Orçamento";
		}

		if (valor == null) {
			return "";
		}
		
		if (unidade == null) {
			return formataValor(valor);
		}
		
		switch (unidade.intValue()) {
		
			case Constantes.UNIDADE_VALOR_SEM_RS:
				return formataValor(valor);
			case Constantes.UNIDADE_VALOR:
				return "R$ " + formataValor(valor);
				//return  formataValor(valor);
			case Constantes.UNIDADE_UNIDADE:
				return formataNumero(new Integer(valor.intValue()));
			case Constantes.UNIDADE_PERCENTUAL:
				return formataValor(valor) + "%";
			case Constantes.UNIDADE_PONTOS:
				return formataNumero(new Integer(valor.intValue()));
			case Constantes.UNIDADE_PECAS_HOMEM_HORA:
				return formataValor(valor);
			case Constantes.UNIDADE_DIAS:
				return formataValor(valor);
			case Constantes.UNIDADE_INDICE:
				return formataValor(valor);
			case Constantes.UNIDADE_PA:
				return formataValor(valor);
			case Constantes.UNIDADE_PEDIDOS_HORA:
				return formataValor(valor);
			case Constantes.UNIDADE_RETORNO_CUSTO:
				return formataValor(valor);
			case Constantes.UNIDADE_PECAS_HORA:
				return formataValor(valor);
			case Constantes.UNIDADE_PECAS_PEDIDO:
				return formataValor(valor);
			case Constantes.UNIDADE_PECAS_TKT:
				return formataValor(valor);
			case Constantes.UNIDADE_AUDITORIA_DIA:
				return formataValor(valor);
		}
		
		return "";
	}

	public static String formataValores(Double valor, Integer unidade) {

		if (valor == null) {
			return "";
		}
		
		if (unidade == null) {
			return formataValor(valor);
		}
		
		switch (unidade.intValue()) {
		
			case Constantes.UNIDADE_VALOR_SEM_RS:
				return formataValor(valor);
			
			case Constantes.UNIDADE_VALOR:
				return "R$ " + formataValor(valor);
				
			case Constantes.UNIDADE_UNIDADE:
				return formataValor(valor) + " UN";
				
			case Constantes.UNIDADE_PERCENTUAL:
				return formataValor(valor) + " %";
				
			case Constantes.UNIDADE_PONTOS:
				return formataValor(valor) + " PT";
		}
		
		return "";
	}

}