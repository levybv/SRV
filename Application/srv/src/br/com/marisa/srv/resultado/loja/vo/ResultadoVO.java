package br.com.marisa.srv.resultado.loja.vo;

import java.io.Serializable;

import br.com.marisa.srv.geral.helper.NumeroHelper;

/**
 * 
 * @author levy.villar
 *
 */
public class ResultadoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6966510802702304930L;

	//Comum
	private Integer numAno;
	private Integer numMes;
	private Integer idFilial;
	private Integer codIndic;
	private String descIndic;
	private Double numMeta;
	private Integer codUniMeta;
	private String metaFormatado;
	private Double numRealz;
	private Integer codUniRealz;
	private String realzFormatado;
	private Double numRealzFil;
	private Integer codUniRealzFil;
	private String realzFilFormatado;
	private Integer qtdRealz;
	private Double numRealzXMeta;
	private Integer codUniRealzXMeta;
	private String realzXMetaFormatado;

	//Funcionario
	private Long codFunc;
	private String nomeFunc;
	private Integer codCargo;
	private String descrCargo;

	public Integer getNumAno() {
		return numAno;
	}

	public void setNumAno(Integer numAno) {
		this.numAno = numAno;
	}

	public Integer getNumMes() {
		return numMes;
	}

	public void setNumMes(Integer numMes) {
		this.numMes = numMes;
	}

	public Integer getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(Integer idFilial) {
		this.idFilial = idFilial;
	}

	public Integer getCodIndic() {
		return codIndic;
	}

	public void setCodIndic(Integer codIndic) {
		this.codIndic = codIndic;
	}

	public String getDescIndic() {
		return descIndic;
	}

	public void setDescIndic(String descIndic) {
		this.descIndic = descIndic;
	}

	public Double getNumMeta() {
		return numMeta;
	}

	public void setNumMeta(Double numMeta) {
		this.numMeta = numMeta;
	}

	public Double getNumRealz() {
		return numRealz;
	}

	public void setNumRealz(Double numRealz) {
		this.numRealz = numRealz;
	}

	public Integer getQtdRealz() {
		return qtdRealz;
	}

	public void setQtdRealz(Integer qtdRealz) {
		this.qtdRealz = qtdRealz;
	}

	public Double getNumRealzXMeta() {
		return numRealzXMeta;
	}

	public void setNumRealzXMeta(Double numRealzXMeta) {
		this.numRealzXMeta = numRealzXMeta;
	}

	public Integer getCodUniMeta() {
		return codUniMeta;
	}

	public void setCodUniMeta(Integer codUniMeta) {
		this.codUniMeta = codUniMeta;
	}

	public Integer getCodUniRealz() {
		return codUniRealz;
	}

	public void setCodUniRealz(Integer codUniRealz) {
		this.codUniRealz = codUniRealz;
	}

	public Integer getCodUniRealzXMeta() {
		return codUniRealzXMeta;
	}

	public void setCodUniRealzXMeta(Integer codUniRealzXMeta) {
		this.codUniRealzXMeta = codUniRealzXMeta;
	}

	public Long getCodFunc() {
		return codFunc;
	}

	public void setCodFunc(Long codFunc) {
		this.codFunc = codFunc;
	}

	public String getNomeFunc() {
		return nomeFunc;
	}

	public void setNomeFunc(String nomeFunc) {
		this.nomeFunc = nomeFunc;
	}

	public Integer getCodCargo() {
		return codCargo;
	}

	public void setCodCargo(Integer codCargo) {
		this.codCargo = codCargo;
	}

	public String getDescrCargo() {
		return descrCargo;
	}

	public void setDescrCargo(String descrCargo) {
		this.descrCargo = descrCargo;
	}

	public Double getNumRealzFil() {
		return numRealzFil;
	}

	public void setNumRealzFil(Double numRealzFil) {
		this.numRealzFil = numRealzFil;
	}

	public Integer getCodUniRealzFil() {
		return codUniRealzFil;
	}

	public void setCodUniRealzFil(Integer codUniRealzFil) {
		this.codUniRealzFil = codUniRealzFil;
	}


	/**************	 FORMATADOS  **************/
	public String getMetaFormatado() {
		metaFormatado = NumeroHelper.formataNumero(numMeta, codUniMeta);
		return metaFormatado;
	}

	public String getRealzFormatado() {
		realzFormatado = NumeroHelper.formataNumero(numRealz, codUniRealz);
		return realzFormatado;
	}

	public String getRealzFilFormatado() {
		realzFilFormatado = NumeroHelper.formataNumero(numRealzFil, codUniRealzFil);
		return realzFilFormatado;
	}

	public String getRealzXMetaFormatado() {
		realzXMetaFormatado = NumeroHelper.formataNumero(numRealzXMeta, codUniRealzXMeta);
		return realzXMetaFormatado;
	}
}