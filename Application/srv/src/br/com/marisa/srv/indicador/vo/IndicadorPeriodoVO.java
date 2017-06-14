package br.com.marisa.srv.indicador.vo;

import java.io.Serializable;

import br.com.marisa.srv.geral.helper.DataHelper;

public class IndicadorPeriodoVO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Integer ano;
	private Integer mes;
	private String mesStr;
	private String dataFormatada;
	private String mesFormatado;

	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public Integer getMes() {
		return mes;
	}
	public void setMes(Integer mes) {
		this.mes = mes;
        if(mes != null)
        {
            mesStr = DataHelper.obtemMesExtenso(mes.intValue(), "C");
        }
	}
	public String getDataFormatada() {
		return dataFormatada;
	}
	public void setDataFormatada(String dataFormatada) {
		this.dataFormatada = dataFormatada;
	}
	public String getMesFormatado() {
		return mesFormatado;
	}
	public void setMesFormatado(String mesFormatado) {
		this.mesFormatado = mesFormatado;
	}
	public String getMesStr() {
		return mesStr;
	}
	public void setMesStr(String mesStr) {
		this.mesStr = mesStr;
	}
}
