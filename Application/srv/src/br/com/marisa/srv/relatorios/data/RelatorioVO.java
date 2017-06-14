package br.com.marisa.srv.relatorios.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class RelatorioVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8381459544853619522L;

	private String titulo;
	private Map subTitulo;
	private List listaCamposCabecalho;
	private List dadosTabela;
	private List listaChaveMap;

	public List getListaCamposCabecalho() {
		return this.listaCamposCabecalho;
	}

	public void setListaCamposCabecalho(List listaCamposCabecalho) {
		this.listaCamposCabecalho = listaCamposCabecalho;
	}

	public List getListaChaveMap() {
		return this.listaChaveMap;
	}

	public void setListaChaveMap(List listaChaveMap) {
		this.listaChaveMap = listaChaveMap;
	}

	public Map getSubTitulo() {
		return this.subTitulo;
	}

	public void setSubTitulo(Map subTitulo) {
		this.subTitulo = subTitulo;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List getDadosTabela() {
		return this.dadosTabela;
	}

	public void setDadosTabela(List dadosTabela) {
		this.dadosTabela = dadosTabela;
	}
}