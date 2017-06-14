package br.com.marisa.srv.geral.helper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.SortedMap;

import org.mozilla.universalchardet.UniversalDetector;

/**
 * @author Levy Villar
 *
 */
public class FileHelper {

	/**
	 * Identifica o charset do InputStream e caso não encontre ou não seja suportado, retorna o charset padrão da JVM.
	 * Parâmetros de configuração podem desconsiderar little-endian (LE) e big-endian (BE), útil para que a leitura 
	 * ignore código Byte Order Mark (BOM).
	 * 
	 * @param InputStream
	 * @param ignoreLE
	 * @param ignoreBE
	 * @return Charset
	 */
	public static Charset detectCharset(InputStream is, boolean ignoreLE, boolean ignoreBE) {
		
		Charset cs = Charset.defaultCharset();
		SortedMap<String, Charset> sm = Charset.availableCharsets();

		UniversalDetector detector = null;

		try {

			byte[] buf = new byte[4096];

			detector = new UniversalDetector(null);

			int nread;
			while ((nread = is.read(buf)) > 0 && !detector.isDone()) {
				detector.handleData(buf, 0, nread);
			}
			detector.dataEnd();

			String encoding = detector.getDetectedCharset();

			if (encoding != null) {

				if ( ignoreLE && encoding.endsWith("LE") ) {
					encoding = encoding.replace("LE", "");
				}
				if ( ignoreBE && encoding.endsWith("BE") ) {
					encoding = encoding.replace("BE", "");
				}

				if ( sm.containsKey(encoding) ) {
					cs = Charset.forName(encoding);
				}

			}

			detector.reset();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			detector = null;
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return cs;
	}

	/**
	 * 
	 * @param prefixo
	 * @return
	 * @throws Exception
	 */
	public static String obterPathArquivoTemporario(String prefixo, String extensao) throws Exception {
		try {
			String path = System.getProperty("java.io.tmpdir");
			if (path == null) {
				throw new Exception("Diretorio temporario indisponivel. Contate a area de TI !");
			} else {
				if ( path.endsWith("\\") ) {
					path = path.substring(0, path.length()-1)+"/";
				}
				if ( !path.endsWith("/") ) {
					path = path+"/";
				}
			}
			String pathArquivo = null;
			if (ObjectHelper.isEmpty(prefixo)) {
				pathArquivo = path + String.valueOf(new Date().getTime()) + extensao;
			} else {
				pathArquivo = path + prefixo + String.valueOf(new Date().getTime()) + extensao;
			}
			
			if ( new File(pathArquivo).exists() ) {
				pathArquivo = obterPathArquivoTemporario(prefixo, extensao);
			}
			return pathArquivo;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 
	 * @param file
	 */
	public static void apagaArquivo(File file) {
		try {
			if ( file.exists() ) {
				file.delete();
			}
		} catch (RuntimeException ex) {
			ex.printStackTrace();
		}
	}

}
