package br.com.marisa.srv.campanha.vo;

import java.io.Serializable;

/**
 * 
 * @author levy.villar
 *
 */
public class CampanhaVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6616112029427564676L;

	private Integer idIndicador;
	private String descricaoIndicador;
	private String tabelaFuncionario;

	public Integer getIdIndicador() {
		return idIndicador;
	}

	public void setIdIndicador(Integer idIndicador) {
		this.idIndicador = idIndicador;
	}

	public String getDescricaoIndicador() {
		return descricaoIndicador;
	}

	public void setDescricaoIndicador(String descricaoIndicador) {
		this.descricaoIndicador = descricaoIndicador;
	}

	public String getTabelaFuncionario() {
		return tabelaFuncionario;
	}

	public void setTabelaFuncionario(String tabelaFuncionario) {
		this.tabelaFuncionario = tabelaFuncionario;
	}

}
