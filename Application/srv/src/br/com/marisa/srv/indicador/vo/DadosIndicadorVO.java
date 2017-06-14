package br.com.marisa.srv.indicador.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Vo para armazenar os dados de um indicador
 * 
 * @author Walter Fontes
 */
public class DadosIndicadorVO implements Serializable {

	private static final long serialVersionUID = -860962048897841956L;

	private Integer idIndicador;
	private String descricaoIndicador;
	private String formulaIndicador;
	private Integer idGrupoIndicador;
	private String descricaoGrupo;
	private String ativo;
	private String formulaConceito;
	private Integer verbaRH;
	private String idSistemico;
	private Integer idEscala;
	private String descricaoEscala;
	private Date dataUltimaAlteracao;
	private Integer idUsuarioUltimaAlteracao;
	private String flgPreenchAtingIgualRealz;
	private String descricaoFonte;
	private String descricaoDiretoria;
	private Integer idIndicadorPai;
	private String flgSentido;

	//Para consulta de range de indicadores
	private boolean isUtilizaRange;
	private Integer codIndicInicio;
	private Integer codIndicFim;

	public String getFlgPreenchAtingIgualRealz() {
		return flgPreenchAtingIgualRealz;
	}

	public void setFlgPreenchAtingIgualRealz(String flgPreenchAtingIgualRealz) {
		this.flgPreenchAtingIgualRealz = flgPreenchAtingIgualRealz;
	}

	public String getDescricaoEscala() {
		return descricaoEscala;
	}

	public void setDescricaoEscala(String descricaoEscala) {
		this.descricaoEscala = descricaoEscala;
	}

	public Integer getIdEscala() {
		return idEscala;
	}

	public void setIdEscala(Integer idEscala) {
		this.idEscala = idEscala;
	}

	public String getIdSistemico() {
		return idSistemico;
	}

	public void setIdSistemico(String idSistemico) {
		this.idSistemico = idSistemico;
	}

	public Date getDataUltimaAlteracao() {
		return dataUltimaAlteracao;
	}

	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}

	public Integer getIdUsuarioUltimaAlteracao() {
		return idUsuarioUltimaAlteracao;
	}

	public void setIdUsuarioUltimaAlteracao(Integer idUsuarioUltimaAlteracao) {
		this.idUsuarioUltimaAlteracao = idUsuarioUltimaAlteracao;
	}

	public Integer getIdGrupoIndicador() {
		return idGrupoIndicador;
	}

	public void setIdGrupoIndicador(Integer idGrupoIndicador) {
		this.idGrupoIndicador = idGrupoIndicador;
	}

	public Integer getVerbaRH() {
		return verbaRH;
	}

	public void setVerbaRH(Integer verbaRH) {
		this.verbaRH = verbaRH;
	}

	public String getAtivo() {
		return ativo;
	}

	public String getAtivoFormatado() {
		if (ativo == null) {
			return "";
		} else if (ativo.equalsIgnoreCase("S")) {
			return "Sim";
		} else if (ativo.equalsIgnoreCase("N")) {
			return "Não";
		} else if (ativo.equalsIgnoreCase("A")) {
			return "Aguardando";
		} else {
			return "-";
		}
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public String getDescricaoGrupo() {
		return descricaoGrupo;
	}

	public void setDescricaoGrupo(String descricaoGrupo) {
		this.descricaoGrupo = descricaoGrupo;
	}

	public String getDescricaoIndicador() {
		return descricaoIndicador;
	}

	public void setDescricaoIndicador(String descricaoIndicador) {
		this.descricaoIndicador = descricaoIndicador;
	}

	public String getFormulaConceito() {
		return formulaConceito;
	}

	public void setFormulaConceito(String formulaConceito) {
		this.formulaConceito = formulaConceito;
	}

	public Integer getIdIndicador() {
		return idIndicador;
	}

	public void setIdIndicador(Integer idIndicador) {
		this.idIndicador = idIndicador;
	}

	public String getFormulaIndicador() {
		return formulaIndicador;
	}

	public void setFormulaIndicador(String formulaIndicador) {
		this.formulaIndicador = formulaIndicador;
	}

	public String getDescricaoFonte() {
		return descricaoFonte;
	}

	public void setDescricaoFonte(String descricaoFonte) {
		this.descricaoFonte = descricaoFonte;
	}

	public String getDescricaoDiretoria() {
		return descricaoDiretoria;
	}

	public void setDescricaoDiretoria(String descricaoDiretoria) {
		this.descricaoDiretoria = descricaoDiretoria;
	}

	public Integer getIdIndicadorPai() {
		return idIndicadorPai;
	}

	public void setIdIndicadorPai(Integer idIndicadorPai) {
		this.idIndicadorPai = idIndicadorPai;
	}

	public String getFlgSentido() {
		return flgSentido;
	}

	public void setFlgSentido(String flgSentido) {
		this.flgSentido = flgSentido;
	}

	public boolean isUtilizaRange() {
		return isUtilizaRange;
	}

	public void setUtilizaRange(boolean isUtilizaRange) {
		this.isUtilizaRange = isUtilizaRange;
	}

	public Integer getCodIndicInicio() {
		return codIndicInicio;
	}

	public void setCodIndicInicio(Integer codIndicInicio) {
		this.codIndicInicio = codIndicInicio;
	}

	public Integer getCodIndicFim() {
		return codIndicFim;
	}

	public void setCodIndicFim(Integer codIndicFim) {
		this.codIndicFim = codIndicFim;
	}

}