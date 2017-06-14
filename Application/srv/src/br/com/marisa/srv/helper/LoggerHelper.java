package br.com.marisa.srv.helper;

import java.io.IOException;
import java.util.Formatter;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import br.com.marisa.srv.logger.business.LogBusiness;
import br.com.marisa.srv.logger.dao.ParametrosLogDAO;

public class LoggerHelper {
	
	static private FileHandler fileTxt;
	static private SimpleFormatter formatterTxt;
	
	static private FileHandler fileCardTxt;
	static private SimpleFormatter formatterCardTxt;

//	static private FileHandler fileHTML;
//	static private Formatter formatterHTML;
	
	private static LoggerHelper instance;// = new AutomacaoLoggerHelper();
	
	private  LoggerHelper() {}
	
	public static LoggerHelper getInstance() {
//    	System.out.println("AutomacaoLoggerHelper - instanciando 1 " + classe.getName());
		if (instance == null){
			instance = new LoggerHelper();
		}
    	return instance;
    }

	
	public Logger getLogger(String nameClass){
		
		Logger lolRetorno = setup(nameClass);
		LogBusiness(lolRetorno);
		return lolRetorno;
		
	}
	static void LogBusiness(Logger lolRetorno) {
		// TODO Auto-generated method stub
		LogBusiness.getInstance().setLoginApp(lolRetorno);
	}
	
//	handlers= java.util.logging.ConsoleHandler
	public static Logger setup(String nameClass) {
        
		
	    // Get the global logger to configure it
		Logger log = Logger.getLogger(nameClass);
		log.setLevel(LogBusiness.getInstance().getNVLlog());

		Handler[] handlers =  log.getHandlers();
		
		if (handlers.length == 0){
			System.out.println(" Criar novo Logger - "+nameClass);
			if (fileTxt == null){
				try {
					fileTxt = new FileHandler(ParametrosLogDAO.getCaminhoLog(),LogBusiness.getInstance().tamMaxArqLog(),LogBusiness.getInstance().qtdMaxArqLog(),true);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				formatterTxt = new SimpleFormatter();
				fileTxt.setFormatter(formatterTxt);
				log.addHandler(fileTxt);
				log.logp(Level.FINE, LoggerHelper.class.getName(), "setup()", "Testando arquivo!");
			}else{
				log.addHandler(fileTxt);
				log.logp(Level.FINE, LoggerHelper.class.getName(), "setup()", "Usando file existente!");
			}
		}else{
			System.out.println("nao tinha mas  "+nameClass);
			for (int i = 0 ; i < handlers.length ; i++){
				Handler tmp = handlers[i];
				System.out.println("removendo handler "+tmp.toString());
				log.removeHandler(tmp);
			}
			if (fileTxt == null){
				try {
					fileTxt = new FileHandler(ParametrosLogDAO.getCaminhoLog(),LogBusiness.getInstance().tamMaxArqLog(),LogBusiness.getInstance().qtdMaxArqLog(),true);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				formatterTxt = new SimpleFormatter();
				fileTxt.setFormatter(formatterTxt);
				log.addHandler(fileTxt);
				log.logp(Level.FINE, LoggerHelper.class.getName(), "setup()", "Testando arquivo!");
			}else{
				log.addHandler(fileTxt);
				log.logp(Level.FINE, LoggerHelper.class.getName(), "setup()", "Usando file existente!");
			}
		}
	    
	    // Create HTML Formatter
//	    formatterHTML = new MyHtmlFormatter();
//	    fileHTML.setFormatter(formatterHTML);
//	    log.addHandler(fileHTML);
//	    log.info("testando arquivo ssssssssssssss "); 
//	    log.severe("sera arquivo");  
//	    log.severe("testan3d3o3 sera");
		return log;
	  }
	
	public void error(String msg) {
		Logger log = setup("LoggerHelper");
		log.removeHandler(fileTxt);
		log.addHandler(fileTxt);
		log.severe(msg);
//		log.removeHandler(fileHTML);
//		log.addHandler(fileHTML);
//		log.severe(msg);
    }

	public Logger getLogger(String name, String string) {
		// TODO Auto-generated method stub
		if("LOGIMPRE".equals(string)){
			return setup(name, "");
		}else{
			return getLogger(name);
		}
	}

	private Logger setup(String nameClass, String string) {
		
		Logger log = Logger.getLogger(nameClass);
		log.setLevel(LogBusiness.getInstance().getNVLlog());

		Handler[] handlers =  log.getHandlers();
		
		if (handlers.length == 0){
			System.out.println(" Criar novo Logger - "+nameClass);
			if (fileTxt == null){
				try {
					fileTxt = new FileHandler(ParametrosLogDAO.getCaminhoLog(),LogBusiness.getInstance().tamMaxArqLog(),LogBusiness.getInstance().qtdMaxArqLog(),true);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				formatterTxt = new SimpleFormatter();
				fileTxt.setFormatter(formatterTxt);
				log.addHandler(fileTxt);
				log.logp(Level.FINE, LoggerHelper.class.getName(), "setup()", "Testando arquivo!");
			}else{
				log.addHandler(fileTxt);
				log.logp(Level.FINE, LoggerHelper.class.getName(), "setup()", "Usando file existente!");
			}
			if (fileCardTxt == null){
				try {
					fileCardTxt = new FileHandler(ParametrosLogDAO.getCaminhoLogIMP(),LogBusiness.getInstance().tamMaxArqLog(),LogBusiness.getInstance().qtdMaxArqLog(),true);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				formatterCardTxt = new SimpleFormatter();
				fileTxt.setFormatter(formatterCardTxt);
				log.addHandler(fileCardTxt);
				log.logp(Level.FINE, LoggerHelper.class.getName(), "setup()", "Testando IMP arquivo!");
			}else{
				log.addHandler(fileCardTxt);
				log.logp(Level.FINE, LoggerHelper.class.getName(), "setup()", "Usando IMP file existente!");
			}
			
			
		}else{
			System.out.println("nao tinha mas  "+nameClass);
			for (int i = 0 ; i < handlers.length ; i++){
				Handler tmp = handlers[i];
				System.out.println("removendo handler "+tmp.toString());
				log.removeHandler(tmp);
			}
			if (fileCardTxt == null){
				try {
					fileCardTxt = new FileHandler(ParametrosLogDAO.getCaminhoLogIMP(),LogBusiness.getInstance().tamMaxArqLog(),LogBusiness.getInstance().qtdMaxArqLog(),true);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				formatterCardTxt = new SimpleFormatter();
				fileTxt.setFormatter(formatterCardTxt);
				log.addHandler(fileCardTxt);
				log.logp(Level.FINE, LoggerHelper.class.getName(), "setup()", "Testando IMP arquivo!");
			}else{
				log.addHandler(fileCardTxt);
				log.logp(Level.FINE, LoggerHelper.class.getName(), "setup()", "Usando IMP file existente!");
			}
			if (fileTxt == null){
				try {
					fileTxt = new FileHandler(ParametrosLogDAO.getCaminhoLog(),LogBusiness.getInstance().tamMaxArqLog(),LogBusiness.getInstance().qtdMaxArqLog(),true);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				formatterTxt = new SimpleFormatter();
				fileTxt.setFormatter(formatterTxt);
				log.addHandler(fileTxt);
				log.logp(Level.FINE, LoggerHelper.class.getName(), "setup()", "Testando arquivo!");
			}else{
				log.addHandler(fileTxt);
				log.logp(Level.FINE, LoggerHelper.class.getName(), "setup()", "Usando file existente!");
			}
	}
		return log;
	}


}
