package br.com.marisa.srv.geral.dao;

/**
 * Abriga um valor e seu tipo de par�metro do Statement. Esses valores s�o constantes e est�o 
 * definidos na classe <code>BasicDAO</code>. 
 * Auxilia no preenchimento de par�metros de um Statement.
 * 
 * @author Walter Fontes
 */
public class ParametroSQL {

   private int tipoParametro;
   private Object valor;
   
   /**
    * 
    * Construtor da classe. Configura os dois par�metros.
    * 
    * @param tipoParametro
    * @param valor
    */
   public ParametroSQL(int tipoParametro, Object valor) {
       this.tipoParametro = tipoParametro;
       this.valor = valor;
   }
	/**
	 * @return Returns the tipoParametro.
	 */
	public final int getTipoParametro() {
	    return tipoParametro;
	}
	/**
	 * @param tipoParametro The tipoParametro to set.
	 */
	public final void setTipoParametro(int tipoParametro) {
	    this.tipoParametro = tipoParametro;
	}
	/**
	 * @return Returns the valor.
	 */
	public final Object getValor() {
	    return valor;
	}
	/**
	 * @param valor The valor to set.
	 */
	public final void setValor(Object valor) {
	    this.valor = valor;
	}
}
