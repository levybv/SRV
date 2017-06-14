package br.com.marisa.srv.relatorio.vo;

import java.io.Serializable;
import java.util.List;

import br.com.marisa.srv.util.tools.vo.LinhaVO;

/**
 * 
 * @author levy.villar
 *
 */
public class RelatorioVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2134054254059963896L;

	private Integer codigo;
	private String nome;
	private String descricao;
	private String titulo;
	private String nomeArquivo;
	private String descricaoColunas;
	private String nomeTabela;
	private String comandoSQL;
	private Boolean isAtivo;
	private Boolean isPeriodo;
	private Integer qtdRepetirParametro;

	private Integer idUsuario;

	private RelatorioTipoVO relatorioTipoVO;
	private List<LinhaVO> linhas;

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
	/**
	 * @return the titulo
	 */
	public String getTitulo() {
		return titulo;
	}
	/**
	 * @param titulo the titulo to set
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	/**
	 * @return the nomeArquivo
	 */
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	/**
	 * @param nomeArquivo the nomeArquivo to set
	 */
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	/**
	 * @return the descricaoColunas
	 */
	public String getDescricaoColunas() {
		return descricaoColunas;
	}
	/**
	 * @param descricaoColunas the descricaoColunas to set
	 */
	public void setDescricaoColunas(String descricaoColunas) {
		this.descricaoColunas = descricaoColunas;
	}
	/**
	 * @return the nomeTabela
	 */
	public String getNomeTabela() {
		return nomeTabela;
	}
	/**
	 * @param nomeTabela the nomeTabela to set
	 */
	public void setNomeTabela(String nomeTabela) {
		this.nomeTabela = nomeTabela;
	}
	/**
	 * @return the comandoSQL
	 */
	public String getComandoSQL() {
		return comandoSQL;
	}
	/**
	 * @param comandoSQL the comandoSQL to set
	 */
	public void setComandoSQL(String comandoSQL) {
		this.comandoSQL = comandoSQL;
	}
	/**
	 * @return the isAtivo
	 */
	public Boolean getIsAtivo() {
		return isAtivo;
	}
	/**
	 * @param isAtivo the isAtivo to set
	 */
	public void setIsAtivo(Boolean isAtivo) {
		this.isAtivo = isAtivo;
	}
	/**
	 * @return the isPeriodo
	 */
	public Boolean getIsPeriodo() {
		return isPeriodo;
	}
	/**
	 * @param isPeriodo the isPeriodo to set
	 */
	public void setIsPeriodo(Boolean isPeriodo) {
		this.isPeriodo = isPeriodo;
	}
	/**
	 * @return the relatorioTipoVO
	 */
	public RelatorioTipoVO getRelatorioTipoVO() {
		return relatorioTipoVO;
	}
	/**
	 * @param relatorioTipoVO the relatorioTipoVO to set
	 */
	public void setRelatorioTipoVO(RelatorioTipoVO relatorioTipoVO) {
		this.relatorioTipoVO = relatorioTipoVO;
	}
	/**
	 * @return the linhas
	 */
	public List<LinhaVO> getLinhas() {
		return linhas;
	}
	/**
	 * @param linhas the linhas to set
	 */
	public void setLinhas(List<LinhaVO> linhas) {
		this.linhas = linhas;
	}
	/**
	 * @return the idUsuario
	 */
	public Integer getIdUsuario() {
		return idUsuario;
	}
	/**
	 * @param idUsuario the idUsuario to set
	 */
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	/**
	 * @return the qtdRepetirParametro
	 */
	public Integer getQtdRepetirParametro() {
		return qtdRepetirParametro;
	}
	/**
	 * @param qtdRepetirParametro the qtdRepetirParametro to set
	 */
	public void setQtdRepetirParametro(Integer qtdRepetirParametro) {
		this.qtdRepetirParametro = qtdRepetirParametro;
	}

}