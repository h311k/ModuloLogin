package br.com.senac.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.tomcat.util.codec.binary.Base64;

public class Security {

	public String encode(String valor) {
		return Base64.encodeBase64String(valor.getBytes());
	}
	
	public String decode(String valor) {
		byte[] decode = Base64.decodeBase64(valor.getBytes());
		valor = new String(decode);
		return valor;
	}
	
	/**
	 * Converte o valor inserido para chave MD5.
	 * 
	 * @param pass (Password antes da encriptacao).
	 * @return Password encriptado.
	 * 
	 */
	public String encriptaSenha(String pass) {
		MessageDigest md=null;
		String retorno="";
		try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            //logger.getLogger(UsuarioService.class.getName()).log(Level.WARN, null, ex);
        }
		md.update(pass.getBytes());
        byte[] hashMd5 = md.digest();
        for(int i=0;i<hashMd5.length;i++){
            retorno+=hashMd5[i];
        }
        retorno = retorno.replaceAll("-", "");
		return retorno;
	}
}
