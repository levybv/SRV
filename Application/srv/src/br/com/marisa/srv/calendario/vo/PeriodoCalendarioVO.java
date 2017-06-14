package br.com.marisa.srv.calendario.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.marisa.srv.geral.helper.DataHelper;

public class PeriodoCalendarioVO {

	private Integer numSeq;
	private Integer periodo;
	private Integer ano;
	private Integer mes;
	private Date dataInicial;
	private String dataInicialStr;
	private Date dataFinal;
	private String dataFinalStr;
	private String mesStr;
	private String periodoMesAno;
	private String mesAno;
	private Integer id;
	private Integer codUsuario;
	private Date dataAlteracao;

	public PeriodoCalendarioVO() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMesStr() {
		return mesStr;
	}

	public void setMesStr(String mesStr) {
		this.mesStr = mesStr;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
		if (mes != null) {
			mesStr = DataHelper.obtemMesExtenso(mes.intValue(), "C");
		}
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	public String getPeriodoMesAno() {
		return periodoMesAno;
	}

	public void setPeriodoMesAno(String periodoMesAno) {
		this.periodoMesAno = periodoMesAno;
	}

	public String getMesAno() {
		return mesAno;
	}

	public void setMesAno(String mesAno) {
		this.mesAno = mesAno;
	}

	public String getDataInicialStr() {
		dataInicialStr = "";
		if (getDataInicial() != null) {
			dataInicialStr = new SimpleDateFormat("dd/MM/yyyy").format(getDataInicial());
		}
		return dataInicialStr;
	}

	public void setDataInicialStr(String dataInicialStr) {
		this.dataInicialStr = dataInicialStr;
	}

	public String getDataFinalStr() {
		dataFinalStr = "";
		if (getDataFinal() != null) {
			dataFinalStr = new SimpleDateFormat("dd/MM/yyyy").format(getDataFinal());
		}
		return dataFinalStr;
	}

	public void setDataFinalStr(String dataFinalStr) {
		this.dataFinalStr = dataFinalStr;
	}

	public Integer getCodUsuario() {
		return codUsuario;
	}

	public void setCodUsuario(Integer codUsuario) {
		this.codUsuario = codUsuario;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public Integer getNumSeq() {
		return numSeq;
	}

	public void setNumSeq(Integer numSeq) {
		this.numSeq = numSeq;
	}
}
