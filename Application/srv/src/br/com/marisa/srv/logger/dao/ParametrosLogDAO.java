package br.com.marisa.srv.logger.dao;





import java.io.File;
import java.util.ResourceBundle;

import br.com.marisa.srv.geral.constants.Constantes;




/**
 * Data de Criação: 13/06/2013
 * 
 * @author Ivens Fernando
 * @since XXX_vYYYYMMa
 * @version XXX_vYYYYMMa
 */
public class ParametrosLogDAO {
	private static ParametrosLogDAO aParametros = new ParametrosLogDAO();
	
	private static File       logFile;
	
    private static long       lastModifiedFile;
	
	/**
	 * -
	 * 
	 * @return
	 */
	public static ParametrosLogDAO getInstancia() {
		if (ParametrosLogDAO.aParametros == null) {
			ParametrosLogDAO.aParametros = new ParametrosLogDAO();
		}
		
		return ParametrosLogDAO.aParametros;
	}
	
	/**
	 * Cria um novo objeto Parametros.
	 */
	private ParametrosLogDAO() {
		super();
		
	}
	
	/**
	 * -
	 * 
	 * @return
	 */
	public String getDriverJDBC() {
		String str = "Driver não Informado";
		
		try {
			str = this.getResourceBundle().getString("DriverJDBC");
		} catch (Exception e) {
			// NUtils.registrarLogErro(this.getClass().getName(),
			// "Erro obtendo parâmetro [DriverJDBC] no arquivo parametros.properties. Utilizando valor default ("
			// + str + ").", e);
//			NUtils.registrarLogErro(this.getClass().getName(),
//					"Erro obtendo parâmetro [DriverJDBC] no arquivo de configuração. Utilizando valor default (" + str + ").");
		}
		
		return str;
	}
	
	/**
	 * /** -
	 * 
	 * @return
	 */
	/**
	 * -
	 * 
	 * @return
	 */
	public String getNmConexaoJNDI() {
		String str = "jdbc/ccm";
		
		try {
			str = this.getResourceBundle().getString("NmConexaoJNDI");
		} catch (Exception e) {
			// NUtils.registrarLogErro(this.getClass().getName(),
			// "Erro obtendo parâmetro [NmConexaoJNDI] no arquivo parametros.properties. Utilizando valor default ("
			// + str + ").", e);
//			NUtils.registrarLogErro(this.getClass().getName(),
//					"Erro obtendo parâmetro [NmConexaoJNDI] no arquivo de configuração. Utilizando valor default (" + str + ").");
		}
		
		return str;
	}
	
	/**
	 * -
	 * 
	 * @return
	 */
	public String getNmProviderURLConexaoBD() {
		String str = "-";
		
		try {
			str = this.getResourceBundle().getString("URLConexaoJDBC");
		} catch (Exception e) {
			// NUtils.registrarLogErro(this.getClass().getName(),
			// "Erro obtendo parâmetro [URLConexaoJDBC] no arquivo parametros.properties. Utilizando valor default ("
			// + str + ").", e);
//			NUtils.registrarLogErro(this.getClass().getName(),
//					"Erro obtendo parâmetro [URLConexaoJDBC] no arquivo de configuração. Utilizando valor default (" + str + ").");
		}
		
		return str;
	}
	
	/**
	 * -
	 * 
	 * @return
	 */
	private static ResourceBundle getResourceBundle() {
		
		return ResourceBundle.getBundle(Constantes.SYS_PROPERTIES);
	}
	
	
	/**
	 * -
	 * 
	 * @return
	 */
	public String getSenhaJDBC() {
		String str = "Senha não Informada";
		
		try {
			str = this.getResourceBundle().getString("SenhaJDBC");
		} catch (Exception e) {
			// NUtils.registrarLogErro(this.getClass().getName(),
			// "Erro obtendo parâmetro [SenhaJDBC] no arquivo parametros.properties. Utilizando valor default ("
			// + str + ").", e);
//			NUtils.registrarLogErro(this.getClass().getName(),
//					"Erro obtendo parâmetro [SenhaJDBC] no arquivo de configuração. Utilizando valor default (" + str + ").");
		}
		
		return str;
	}
	
	/**
	 * -
	 * 
	 * @return
	 */
	public String getServidorAplicacao() {
		String str = "TOMCAT";
		
		try {
			str = this.getResourceBundle().getString("ServidorAplicacao");
		} catch (Exception e) {
			// NUtils.registrarLogErro(this.getClass().getName(),
			// "Erro obtendo parâmetro [ServidorAplicacao] no arquivo parametros.properties. Utilizando valor default ("
			// + str + ").", e);
//			NUtils.registrarLogErro(this.getClass().getName(),
//					"Erro obtendo parâmetro [ServidorAplicacao] no arquivo de configuração. Utilizando valor default (" + str + ").");
		}
		
		return str;
	}
	
	
	/**
	 * -
	 * 
	 * @return
	 */
	public String getUsuarioJDBC() {
		String str = "Usuário não Informado";
		
		try {
			str = this.getResourceBundle().getString("UsuarioJDBC");
		} catch (Exception e) {
			// NUtils.registrarLogErro(this.getClass().getName(),
			// "Erro obtendo parâmetro [UsuarioJDBC] no arquivo parametros.properties. Utilizando valor default ("
			// + str + ").", e);
//			NUtils.registrarLogErro(this.getClass().getName(),
//					"Erro obtendo parâmetro [UsuarioJDBC] no arquivo de configuração. Utilizando valor default (" + str + ").");
		}
		
		return str;
	}
	
	
	private static void valorizaArquivoLog() throws Exception{
		System.out.println("LogHelperAutomacaoScorm - VAI PEGA O LOG");
		System.out.println("LogHelperAutomacaoScorm - " + getCaminhoLog());
		try{
		logFile = new File("LogHelperAutomacaoScorm - " + getCaminhoLog());
		
        lastModifiedFile = logFile.lastModified();
		}catch (RuntimeException e) {
			System.out.println("LogHelperAutomacaoScorm - ERRO  PEGA O LOG");
			e.printStackTrace();
		}
	}
	
	
	public File getLogFile(){
		if (logFile == null || (logFile != null && logFile.lastModified() > lastModifiedFile)){
			try {
				valorizaArquivoLog();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return this.logFile;
	}
	
	 public static boolean ocorreuAlteracao() {
		 System.out.println("LogHelperAutomacaoScorm - ocorreuAlteracao ");
		 System.out.println("LogHelperAutomacaoScorm - " + logFile.lastModified());
		 System.out.println("LogHelperAutomacaoScorm - " + lastModifiedFile);
		 boolean ret = ((logFile == null || (logFile != null && logFile.lastModified() > lastModifiedFile)));
		 System.out.println("LogHelperAutomacaoScorm - ocorreuAlteracao "+ret);
	        return ret ;
	    }
	
	public static java.lang.String getCaminhoLog() throws Exception {
		String str = "";
		
		try {
			str = getResourceBundle().getString("LOG4J");
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		if ("".equals(str))
			throw new Exception("Arquivo de configuracao nao encontrado");
			
		return str;
	}

	public static java.lang.String getCaminhoLogIMP() throws Exception {
		String str = "";
		
		try {
			str = getResourceBundle().getString("LOGIMP");
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		if ("".equals(str))
			throw new Exception("Arquivo de configuracao nao encontrado");
			
		return str;
	}
	
	
	
}

