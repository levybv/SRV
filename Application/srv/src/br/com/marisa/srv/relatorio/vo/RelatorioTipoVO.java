package br.com.marisa.srv.relatorio.vo;

import java.io.Serializable;

/**
 * 
 * @author levy.villar
 *
 */
public class RelatorioTipoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6754841765506011348L;

	private Integer codigo;
	private String nome;
	private String descricao;
	/**
	 * @return the codigo
	 */
	public Integer getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}
	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}
	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}