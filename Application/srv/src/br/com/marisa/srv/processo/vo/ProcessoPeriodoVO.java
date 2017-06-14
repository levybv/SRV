package br.com.marisa.srv.processo.vo;

import java.io.Serializable;
import java.util.Date;

import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.helper.DataHelper;

/**
 * Vo para armazenar os dados de Processos por Período
 * 
 * @author Walter Fontes
 */
public class ProcessoPeriodoVO implements Serializable{

	private Integer idProcesso;
	private String  descricaoProcesso;
	private Integer mes;
	private Integer ano;
	private Integer status;
	private Date 	dataUltimaAlteracao;
	private Integer idUsuarioUltimaAlteracao;
	
	
	
	public String getPeriodoFormatado() {
		return DataHelper.obtemMesExtenso(mes.intValue(), Constantes.CAPTALIZADO_CASE)+" / "+ano;
	}
	public String getStatusFormatado() {
		switch (status.intValue()) {
			case 1: //Constantes.STATUS_PROCESSO_PERIODO_ABERTO.intValue():
				return "Período Aberto";
	
			case 2: //Constantes.STATUS_PROCESSO_PERIODO_FECHADO.intValue():
				return "Período Fechado";
	
			case 3: //Constantes.STATUS_PROCESSO_PERIODO_REABERTO.intValue():
				return "Período Reaberto";
	
			case 4: //Constantes.STATUS_PROCESSO_PERIODO_FECHAR.intValue():
				return "Período para Fechar";
	
			default:
				return "Situação desconhecida";
		}
	}
	
	
	
	public String getDescricaoProcesso() {
		return descricaoProcesso;
	}
	public void setDescricaoProcesso(String descricaoProcesso) {
		this.descricaoProcesso = descricaoProcesso;
	}
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public Date getDataUltimaAlteracao() {
		return dataUltimaAlteracao;
	}
	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}
	public Integer getIdProcesso() {
		return idProcesso;
	}
	public void setIdProcesso(Integer idProcesso) {
		this.idProcesso = idProcesso;
	}
	public Integer getIdUsuarioUltimaAlteracao() {
		return idUsuarioUltimaAlteracao;
	}
	public void setIdUsuarioUltimaAlteracao(Integer idUsuarioUltimaAlteracao) {
		this.idUsuarioUltimaAlteracao = idUsuarioUltimaAlteracao;
	}
	public Integer getMes() {
		return mes;
	}
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}