package br.com.marisa.srv.agendamento.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.marisa.srv.geral.helper.DataHelper;

public class AgendamentoVO implements Serializable{

	private static final long serialVersionUID = 8989656851961775315L;

	private Integer codigoCarga;
	private String descricaoCarga;
	private Date dataAgendamento;
	private String dataAgendamentoStr;
	private Date dataUltimoProcessamento;
	private String dataUltimoProcessamentoStr;
	private Date dataLimiteProcessamento;
	private String dataLimiteProcessamentoStr;
	private Integer ano;
	private Integer mes;
	private String mesStr;
	private Integer numeroArquivo;
	private String nomeArquivo;
	private String diretorioOrigem;
	private String diretorioDestino;
	private Integer flagAtiva;

	public Integer getCodigoCarga() {
		return codigoCarga;
	}

	public void setCodigoCarga(Integer codigoCarga) {
		this.codigoCarga = codigoCarga;
	}

	public String getDescricaoCarga() {
		return descricaoCarga;
	}

	public void setDescricaoCarga(String descricaoCarga) {
		this.descricaoCarga = descricaoCarga;
	}

	public Date getDataAgendamento() {
		return dataAgendamento;
	}

	public void setDataAgendamento(Date dataAgendamento) {
		this.dataAgendamento = dataAgendamento;
	}

	public Date getDataUltimoProcessamento() {
		return dataUltimoProcessamento;
	}

	public void setDataUltimoProcessamento(Date dataUltimoProcessamento) {
		this.dataUltimoProcessamento = dataUltimoProcessamento;
	}

	public Date getDataLimiteProcessamento() {
		return dataLimiteProcessamento;
	}

	public void setDataLimiteProcessamento(Date dataLimiteProcessamento) {
		this.dataLimiteProcessamento = dataLimiteProcessamento;
	}

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

	public Integer getNumeroArquivo() {
		return numeroArquivo;
	}

	public void setNumeroArquivo(Integer numeroArquivo) {
		this.numeroArquivo = numeroArquivo;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getDiretorioOrigem() {
		return diretorioOrigem;
	}

	public void setDiretorioOrigem(String diretorioOrigem) {
		this.diretorioOrigem = diretorioOrigem;
	}

	public String getDiretorioDestino() {
		return diretorioDestino;
	}

	public void setDiretorioDestino(String diretorioDestino) {
		this.diretorioDestino = diretorioDestino;
	}

	public Integer getFlagAtiva() {
		return flagAtiva;
	}

	public void setFlagAtiva(Integer flagAtiva) {
		this.flagAtiva = flagAtiva;
	}

	public String getMesStr() {
		return mesStr;
	}

	public void setMesStr(String mesStr) {
		this.mesStr = mesStr;
	}

	public String getDataAgendamentoStr() {
		dataAgendamentoStr = "";
		if ( getDataAgendamento() != null ) {
			dataAgendamentoStr = new SimpleDateFormat("dd/MM/yyyy").format(getDataAgendamento());
		}
		return dataAgendamentoStr;
	}

	public void setDataAgendamentoStr(String dataAgendamentoStr) {
		this.dataAgendamentoStr = dataAgendamentoStr;
	}

	public String getDataUltimoProcessamentoStr() {
		dataUltimoProcessamentoStr = "";
		if ( getDataUltimoProcessamento() != null ) {
			dataUltimoProcessamentoStr = new SimpleDateFormat("dd/MM/yyyy").format(getDataUltimoProcessamento());
		}
		return dataUltimoProcessamentoStr;
	}

	public void setDataUltimoProcessamentoStr(String dataUltimoProcessamentoStr) {
		this.dataUltimoProcessamentoStr = dataUltimoProcessamentoStr;
	}

	public String getDataLimiteProcessamentoStr() {
		dataLimiteProcessamentoStr = "";
		if ( getDataLimiteProcessamento() != null ) {
			dataLimiteProcessamentoStr = new SimpleDateFormat("dd/MM/yyyy").format(getDataLimiteProcessamento());
		}
		return dataLimiteProcessamentoStr;
	}

	public void setDataLimiteProcessamentoStr(String dataLimiteProcessamentoStr) {
		this.dataLimiteProcessamentoStr = dataLimiteProcessamentoStr;
	}

}
