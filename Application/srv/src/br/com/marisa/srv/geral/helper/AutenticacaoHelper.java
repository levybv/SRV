package br.com.marisa.srv.geral.helper;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 * Classe para realizar a criptografia de senhas
 */
public class AutenticacaoHelper {

	
    /**
     * Recebe a o texto em seu formato normal, e retorna um StringBuffer embaralhado.
     * O texto deve ter o maximo de 30 caracteres.
     */
    public static StringBuffer embaralha(String textoOri) throws Exception {
        char caracterCriptografado     = 0;
        char caracterNormal            = 0;
        int tmpRandomico               = 0;
        int numeroByte                 = 0;
        int size                       = 0;
        int tamanhoMaximo              = 60;
        StringBuffer texto             = new StringBuffer(textoOri);
        StringBuffer charsEncriptados1 = new StringBuffer();
        StringBuffer charsEncriptados2 = new StringBuffer();
        StringBuffer retorno           = new StringBuffer();


        // O texto nao deve ter mais do que 30 caracteres
        if (textoOri.length() > 30) {
            throw new Exception("Texto a ser embaralhado nao deve ter mais que 30 (trinta) caracteres.");
        }
        
        // Completa o texto com brancos até alcançar 30 caracteres
        while (texto.length() < 30) {
            texto.append(" ");
        }
        
        // O tamanho máximo da senha original deve ser metade do tamanho do campo
        tamanhoMaximo = tamanhoMaximo / 2;
        
        // Trata byte a byte o texto a ser embaralhado
        while (tamanhoMaximo > numeroByte) {
            int randomico = 0;
            caracterCriptografado = texto.charAt(numeroByte);
            caracterNormal        = caracterCriptografado;
            
            // Obtem o CENTESIMO corrente, para o calculo randomico.
            GregorianCalendar data = new GregorianCalendar();
            int milesimo           = data.get(Calendar.MILLISECOND);
            int centesimo          = milesimo - ((int)(milesimo / 100) * 100);
            
            randomico = centesimo;
            randomico = geraRandomico(numeroByte, randomico);
            
            // Armazena o retorno e busca outro numero randomico
            tmpRandomico = randomico;
            
            randomico = geraRandomico(numeroByte, randomico);
            
            // A "chave" de criptografia e obtida atraves da soma de dois valores randomicos. 
            randomico = randomico + tmpRandomico;
            
            caracterNormal = (char)(caracterNormal + randomico);
            caracterCriptografado = caracterNormal;
            
            // Appenda StringBuffer com os caracteres encriptados
            charsEncriptados1.append((char)caracterCriptografado);
         
            caracterNormal = (char)randomico;
            caracterCriptografado = caracterNormal;
            
            // Appenda StringBuffer com as "chaves"
            charsEncriptados2.append((char)caracterCriptografado);
            numeroByte++;
        }

        // O retorno é uma ordenação em pares, char, chave, char, chave.....
        size = charsEncriptados1.length();
        for (int i=0; i<size; i++) {
            retorno.append(charsEncriptados1.charAt(i)).append(charsEncriptados2.charAt(i));
        }

        // Completar 30 caracteres como espaços em branco
        while (retorno.length() < 30) {
            retorno.append(" ");
        }
        //System.out.println("texto embaralhado => " + retorno);
        return retorno;

    }
    
    
    /**
     * Gera um valor randomico
     * Recebe o numero do Byte e um numero randomico, que vão ser as sementes para gerar um novo numero
     */
    private static int geraRandomico(int numeroByte, int randomico) {
        float randomicoFloat   = 0;
        String randomicoString = "";            
        Random random          = new Random(numeroByte + randomico);
        
        // Busca um valor randomico...
        randomicoFloat = random.nextFloat();
        
        // ... que nao pode ser zero nem 1 (hum)
        while (!(randomicoFloat > 0) || !(randomicoFloat < 1)) {
            randomicoFloat = random.nextFloat();
        }

        randomicoString = String.valueOf(randomicoFloat);
        
        // Completa com zeros 
        while (randomicoString.length() < 10) {
            randomicoString = randomicoString + "0";
        }
        
        randomico = 0;
        
        // Faz uma somatória dos numeros resultandes da busca do valor randomico
        // ex.: 0.56739001 -> 5+6+7+3+9+0+0+1 = 31
        for (int i=2; i<9; i++) {
            randomico = randomico + Integer.parseInt(randomicoString.substring(i,i+1));
        }
        
        return randomico;
    }
    

    /**
     * Recebe o texto em seu formato embaralhado, e retorna seu formato normal
     */
    private static final StringBuffer desembaralha(String texto) {
    	
        char charTexto       = 0;
        int charNum          = 0;
        int numeroByte       = 0;
        int numeroByteTexto  = 0;
        int numeroByteChave  = 1;
        int tamanhoMaximo    = 0;

        String caracterCriptografado     = null;
        StringBuffer textoEmbaralhado    = new StringBuffer(texto);
        StringBuffer textoDesembaralhado = new StringBuffer(); 
        StringBuffer charsEncriptados1   = new StringBuffer();
        StringBuffer charsEncriptados2   = new StringBuffer();

        tamanhoMaximo = textoEmbaralhado.length();
        
        while ( tamanhoMaximo > numeroByteTexto ) { 
            charsEncriptados1.append(textoEmbaralhado.substring(numeroByteTexto, numeroByteTexto + 1));
            charsEncriptados2.append(textoEmbaralhado.substring(numeroByteChave, numeroByteChave + 1));

            numeroByteTexto = numeroByteTexto + 2;
            numeroByteChave = numeroByteChave + 2;
            
            //System.out.println("charsEncriptados1 => " + charsEncriptados1);
            //System.out.println("charsEncriptados2 => " + charsEncriptados2);
            
            numeroByte++;
        }
        
        for (numeroByteTexto=0; numeroByteTexto < numeroByte; numeroByteTexto++) {
            // Faz um tratamento byte a byte para a descriptografia.
            caracterCriptografado = charsEncriptados1.substring(numeroByteTexto,numeroByteTexto+1);
            
            charTexto = caracterCriptografado.charAt(0);
            charNum = charTexto;
            
            caracterCriptografado = charsEncriptados2.substring(numeroByteTexto,numeroByteTexto+1);
            charTexto = caracterCriptografado.charAt(0);
            
            if (charTexto > charNum) {
                charNum = charNum + 256;
            }
            
            charNum = charNum - charTexto;
            
            charTexto = (char) charNum;
            
            textoDesembaralhado.append((char)charTexto);
        }
        
        //System.out.println("texto desembaralhado => " + textoDesembaralhado.toString().trim());
        
        return textoDesembaralhado;
    }

    
    /**
     * Compara um texto informado com outro embaralhado para saber 
     * se correspondem ao mesmo conteúdo.
     */
    public static boolean compare(String textoInformado, String textoEmbaralhado) {
        String textoDesembaralhado = desembaralha(textoEmbaralhado).toString();
        
        return (textoInformado.trim().equals(textoDesembaralhado.trim()));
    }
    
    public static void main(String[] args) {
		try {
			
			for(int i=0; i<10; i++) {
				String senha = "111111";
				String senhaEmb = AutenticacaoHelper.embaralha(senha).toString();
				Thread.sleep(10000);
				boolean resultado = AutenticacaoHelper.compare(senha, senhaEmb);
				System.out.println(i + "-" + senha + "-" + senhaEmb + "-" + resultado);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}