package br.com.marisa.srv.geral.helper;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.security.auth.login.AccountException;
import javax.security.auth.login.FailedLoginException;

import br.com.marisa.srv.geral.enumeration.ActiveDirectoryErrorEnum;
import br.com.marisa.srv.geral.excecoes.AutenticacaoException;

/**
 * 
 * @author 
 *
 */
public class ActiveDirectoryAuthentication {

	private static final String CONTEXT_FACTORY_CLASS = "com.sun.jndi.ldap.LdapCtxFactory";

	private String ldapServerUrls[];

	private int lastLdapUrlIndex;

	private final String domainName;

	/**
	 * 
	 * @param domainName
	 */
	public ActiveDirectoryAuthentication(String domainName) {
		this.domainName = domainName.toUpperCase();

		try {
			ldapServerUrls = nsLookup(domainName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		lastLdapUrlIndex = 0;
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws AuthenticationException 
	 */
	public boolean authenticate(String username, String password) throws AutenticacaoException  {
		try{
			if (ldapServerUrls == null || ldapServerUrls.length == 0) {
					throw new AccountException("Unable to find ldap servers");
			}
			if (username == null || password == null || username.trim().length() == 0 || password.trim().length() == 0) {
				throw new FailedLoginException("Username or password is empty");
			}
			int retryCount = 0;
			int currentLdapUrlIndex = lastLdapUrlIndex;
			do {
				retryCount++;
				try {
					Hashtable<Object, Object> env = new Hashtable<Object, Object>();
					env.put(Context.INITIAL_CONTEXT_FACTORY, CONTEXT_FACTORY_CLASS);
					env.put(Context.PROVIDER_URL, ldapServerUrls[currentLdapUrlIndex]);
					env.put(Context.SECURITY_PRINCIPAL, username + "@" + domainName);
					env.put(Context.SECURITY_CREDENTIALS, password);
					DirContext ctx = new InitialDirContext(env);
					ctx.close();
					lastLdapUrlIndex = currentLdapUrlIndex;
					return true;
				} catch (CommunicationException exp) {
					// you can replace with log4j or slf4j API
					exp.printStackTrace();
					// if the exception of type communication we can assume the AD
					// is not reachable hence retry can be attempted with next
					// available AD
					if (retryCount < ldapServerUrls.length) {
						currentLdapUrlIndex++;
						if (currentLdapUrlIndex == ldapServerUrls.length) {
							currentLdapUrlIndex = 0;
						}
						continue;
					}
					return false;
				} catch (AuthenticationException aex) {
					String msgAd = "AD: ";
					String messageLdap = aex.getMessage().toUpperCase();
					System.out.println("Erro de autenticacao AD: "+messageLdap);
					aex.printStackTrace();
					if (messageLdap.contains("LDAP: ERROR CODE 49".toUpperCase())) {
						if (messageLdap.contains(ActiveDirectoryErrorEnum.INVALID_USER_OR_PASS.getStrErroAd())) {
							msgAd += ActiveDirectoryErrorEnum.INVALID_USER_OR_PASS.getDescErro();
						} else if (messageLdap.contains(ActiveDirectoryErrorEnum.USER_NOT_FOUND.getStrErroAd())) {
							msgAd += ActiveDirectoryErrorEnum.USER_NOT_FOUND.getDescErro();
						} else if (messageLdap.contains(ActiveDirectoryErrorEnum.PASSWORD_EXPIRED.getStrErroAd())) {
							msgAd += ActiveDirectoryErrorEnum.PASSWORD_EXPIRED.getDescErro();
						} else if (messageLdap.contains(ActiveDirectoryErrorEnum.USER_DISABLED.getStrErroAd())) {
							msgAd += ActiveDirectoryErrorEnum.USER_DISABLED.getDescErro();
						} else if (messageLdap.contains(ActiveDirectoryErrorEnum.USER_EXPIRED.getStrErroAd())) {
							msgAd += ActiveDirectoryErrorEnum.USER_EXPIRED.getDescErro();
						} else if (messageLdap.contains(ActiveDirectoryErrorEnum.PASSWORD_RESET.getStrErroAd())) {
							msgAd += ActiveDirectoryErrorEnum.PASSWORD_RESET.getDescErro();
						} else if (messageLdap.contains(ActiveDirectoryErrorEnum.USER_LOCKED.getStrErroAd())) {
							msgAd += ActiveDirectoryErrorEnum.USER_LOCKED.getDescErro();
						} else {
							msgAd += "Falha ao autenticar usuário no domínio";
						}
					} else {
						msgAd += "Falha de autenticação no domínio";
					}
					throw new AutenticacaoException(msgAd);
					//return false;
				} catch (Throwable throwable) {
					throwable.printStackTrace();
					return false;
				}
			} while (true);
		} catch (AccountException e) {
			e.printStackTrace();
			return false;
		} catch(FailedLoginException e1){
			e1.printStackTrace();
			return false;
		}
	}

	private static String[] nsLookup(String argDomain) throws Exception {
		try {
			Hashtable<Object, Object> env = new Hashtable<Object, Object>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
			env.put("java.naming.provider.url", "dns:");
			DirContext ctx = new InitialDirContext(env);
			Attributes attributes = ctx
					.getAttributes(String.format("_ldap._tcp.%s", argDomain), new String[] { "srv" });
			// try thrice to get the KDC servers before throwing error
			for (int i = 0; i < 3; i++) {
				Attribute a = attributes.get("srv");
				if (a != null) {
					List<String> domainServers = new ArrayList<String>();
					NamingEnumeration<?> enumeration = a.getAll();
					while (enumeration.hasMoreElements()) {
						String srvAttr = (String) enumeration.next();
						// the value are in space separated 0) priority 1)
						// weight 2) port 3) server
						String values[] = srvAttr.toString().split(" ");
						domainServers.add(String.format("ldap://%s:%s", values[3], values[2]));
					}
					String domainServersArray[] = new String[domainServers.size()];
					domainServers.toArray(domainServersArray);
					return domainServersArray;
				}
			}
			throw new Exception("Unable to find srv attribute for the domain " + argDomain);
		} catch (NamingException exp) {
			throw new Exception("Error while performing nslookup. Root Cause: " + exp.getMessage(), exp);
		}
	}

	public static void main (String args[]) {
		ActiveDirectoryAuthentication authentication = new ActiveDirectoryAuthentication("central.marisa.com.br");
		//ActiveDirectoryAuthentication authentication = new ActiveDirectoryAuthentication("homol.marisa.com.br");
		//ActiveDirectoryAuthentication authentication = new ActiveDirectoryAuthentication("128.1.1.44");
		try {
			String usuario = "levyvillar";
			String senha = "Central@14";
	        boolean authResult = authentication.authenticate(usuario, senha);
	        System.out.print("Auth: "+ authResult);
		} catch (AutenticacaoException ex) {
			System.out.println(ex.getMessage());
		} catch (Exception ex) {
			System.out.println("ERRO>>>>> " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
