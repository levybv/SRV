package br.com.marisa.srv.logger.business;



import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;



public class LogBusiness {

	private static final LogBusiness instance = new LogBusiness();
	
	private static ArrayList<Logger> logApp = new ArrayList<Logger>();
	
	private LogBusiness (){}
	
	private static NivelLogger nvlLog =  NivelLogger.CONFIG;

	
	public static LogBusiness  getInstance() {
		return instance;
	}
	
	public static Level getNVLlog(){
		return nvlLog.getValor();
	}
	
	public void setNVLlog(int nivel){
		switch (nivel) {
		case 0:
			nvlLog = NivelLogger.SEVERE;
			break;
		case 1:
			nvlLog = NivelLogger.WARNING;
			break;
		case 2:
			nvlLog = NivelLogger.INFO;
			break;
		case 3:
			nvlLog = NivelLogger.CONFIG;
			break;
		case 4:
			nvlLog = NivelLogger.FINE;
			break;
		case 5:
			nvlLog = NivelLogger.FINER;
			break;
		case 6:
			nvlLog = NivelLogger.FINEST;
			break;
		default:
			nvlLog = NivelLogger.SEVERE;
			break;
		}
	}
	public int tamMaxArqLog(){
		return 1024 * 1024 * 10;
	}
	public int qtdMaxArqLog(){
		return 20;
	}

	/**
	 * @return the logApp
	 */
	public static ArrayList<Logger> getLogApp() {
		return LogBusiness.logApp;
	}

	/**
	 * @param logApp the logApp to set
	 */
//	public static void setLogApp(ArrayList<Logger> logApp) {
//		LogBusiness.logApp = logApp;
//	}
//	
	public static void setLoginApp(Logger log){
		LogBusiness.logApp.add(log);
	}
	
	public static void setNovoNivelAllLogsApp(){
		
		for (int index = 0 ; index < LogBusiness.logApp.size(); index++){
			LogBusiness.logApp.get(index).setLevel(LogBusiness.getNVLlog());
		}
		
	}
	
}
