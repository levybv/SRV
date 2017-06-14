package br.com.marisa.srv.geral.helper;

/**
 * @author tiago nogueira
 * 
 */
public class HtmlHelper {
	
	
	/**
	 * Conversão para Unicode HTML
	 * 
	 * @param string
	 * @param convertToHtmlUnicodeEscapes
	 * @return
	 */
	public static String escapeMarkup(final String string, final boolean convertToHtmlUnicodeEscapes) {
		
		if (string != null) {

			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < string.length(); i++) {
				final char c = string.charAt(i);

				switch (c) {
				case '<':
					buffer.append("&lt;");
					break;

				case '>':
					buffer.append("&gt;");
					break;

				case '&':
					buffer.append("&amp;");
					break;

				case '"':
					buffer.append("&quot;");
					break;

				case '\'':
					buffer.append("&#039;");
					break;

				default:
					if (convertToHtmlUnicodeEscapes) {
						int ci = 0xffff & c;
						if (ci < 160) {
							// nothing special only 7 Bit
							buffer.append(c);
						} else {
							// Not 7 Bit use the unicode system
							buffer.append("&#");
							buffer.append(new Integer(ci).toString());
							buffer.append(';');
						}
					} else {
						buffer.append(c);
					}

					break;
				}
			}
			return buffer.toString();
		} else {
			return null;
		}
	}

	
	/**
	 * Retira HTML
	 * 
	 * @param string
	 * @return
	 */
	public static String backMarkup(String string) {
		if (string != null) {
			string = string.replaceAll("&lt;", "<");
			string = string.replaceAll("&gt;", ">");
			string = string.replaceAll("&amp;", "&");
			string = string.replaceAll("&quot;", "");
			string = string.replaceAll("&#039;", "\"");
			return   string.replaceAll("&quot;", "\'");
		} else {
			return null;
		}
	}
}