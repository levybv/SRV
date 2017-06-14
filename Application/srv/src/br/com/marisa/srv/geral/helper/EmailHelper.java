package br.com.marisa.srv.geral.helper;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.parametros.Parametros;

/**
 * Classe com métodos genéricos relacionados a envio de emails
 * 
 * @author Andre
 */
public class EmailHelper {

	// Singleton
	private static final EmailHelper instance = new EmailHelper();

	private EmailHelper() {
	}

	public static EmailHelper getInstance() {
		return instance;
	}

	/**
	 * 
	 * @param remetente
	 * @param destinatario
	 * @param cc
	 * @param bcc
	 * @param assunto
	 * @param mensagem
	 * @param tipoTexto
	 * @throws SRVException
	 */
	public void sendEmail(String host, String remetente, String destinatario, String cc, String bcc, String assunto, String mensagem, String tipoTexto) throws SRVException {
		try {

			// Get system properties
			Properties properties = new Properties();

			// Setup mail server
			properties.setProperty("mail.smtp.host", host);
			// properties.put("mail.smtp.auth", "true");
			// Get a Session object
			Session session = Session.getDefaultInstance(properties, null);
			// getInstance(properties, null);
			// Get the default Session object.
			Message msg = new MimeMessage(session);
			InternetAddress from = new InternetAddress(remetente);
			msg.setFrom(from);
			if (cc != null) {
				msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));
			}
			if (bcc != null) {
				msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc, false));
			}

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario, false));
			msg.setContent(mensagem, tipoTexto == null ? "text/plain" : tipoTexto);
			msg.setSubject(assunto);
			msg.setSentDate(new Date());

			// Send message
			Transport.send(msg);
			// log.log(Level.INFO, "Sent message successfully....");
			System.out.println("Sent message successfully....");
		} catch (Exception mex) {
			mex.printStackTrace();
			throw new SRVException(mex.getMessage());
			// log.log(Level.SEVERE, "Erro no envio do email....\r\n"+
			// mex.getStackTrace());
		}
	}
}