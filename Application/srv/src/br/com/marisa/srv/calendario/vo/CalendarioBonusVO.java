package br.com.marisa.srv.calendario.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.marisa.srv.geral.helper.DataHelper;

public class CalendarioBonusVO {

	private Integer ano;
	private Integer mes;
	private String mesStr;
	private Date dataInicial;
	private String dataInicialStr;
	private Date dataFinal;
	private String dataFinalStr;
	private String descricaoMes;
	private Integer codUsuario;
	private Date dataAlteracao;

	public CalendarioBonusVO() {
		super();
	}

	public CalendarioBonusVO(Integer ano, Integer mes) {
		setAno(ano);
		setMes(mes);
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
        if(mes != null){
            mesStr = DataHelper.obtemMesExtenso(mes.intValue(), "C");
        }
	}

	public String getMesStr() {
		return mesStr;
	}

	public void setMesStr(String mesStr) {
		this.mesStr = mesStr;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
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

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
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

	public String getDescricaoMes() {
		return descricaoMes;
	}

	public void setDescricaoMes(String descricaoMes) {
		this.descricaoMes = descricaoMes;
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

}
