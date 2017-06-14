package br.com.marisa.srv.usuario.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * VO para manter os dados do Usuário
 * 
 * @author Walter Fontes
 */
public class UsuarioVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7036069536285657730L;

	private Integer idUsuario;
	private String	nome;
	private String	matricula;
	private Boolean ativo;
	private Boolean autenticaAD;
	private String	login;
	private String	senha;
	private Integer idPerfil;
	private String  descricaoPerfil;
	private Date    dataUltimaAlteracao;
	private Integer idUsuarioUltimaAlteracao;
	
	public UsuarioVO() {
		super();
	}

	public UsuarioVO(Integer idUsuario) {
		super();
		this.idUsuario = idUsuario;
	}

	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	public Date getDataUltimaAlteracao() {
		return dataUltimaAlteracao;
	}
	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}
	public Integer getIdPerfil() {
		return idPerfil;
	}
	public void setIdPerfil(Integer idPerfil) {
		this.idPerfil = idPerfil;
	}
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	public Integer getIdUsuarioUltimaAlteracao() {
		return idUsuarioUltimaAlteracao;
	}
	public void setIdUsuarioUltimaAlteracao(Integer idUsuarioUltimaAlteracao) {
		this.idUsuarioUltimaAlteracao = idUsuarioUltimaAlteracao;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescricaoPerfil() {
		return descricaoPerfil;
	}
	public void setDescricaoPerfil(String descricaoPerfil) {
		this.descricaoPerfil = descricaoPerfil;
	}
	public Boolean getAutenticaAD() {
		return autenticaAD;
	}
	public void setAutenticaAD(Boolean autenticaAD) {
		this.autenticaAD = autenticaAD;
	}

	
	
}
