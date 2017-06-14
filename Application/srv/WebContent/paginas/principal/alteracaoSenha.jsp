<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type='text/javascript' src='srvdwr/interface/UsuarioBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>	

<script type="text/javascript" src="js/ms/srv.alteracaoSenha.js" charset="ISO-8859-1"></script>


  <form id="form" name="form" method="post">     
	 <div class="boxIndex"> 
				<div class="boxCenter">
                                <br />
                                <div id="" class="panel">
                                    <div>
                                        <div id="" class="barTitle">Alteração de Senha</div>
                                    </div>
                                    <div id="painel-interno">
                                        <table class="tabelaComponente" cellpadding="2" cellspacing="2">
                                            <tbody>
                                             	<tr>
                                                  <td class="componente" colspan="2">
														<label class="label" style="color:red;"><center>A altera&ccedil;&atilde;o de senha SOMENTE &eacute; v&aacute;lida para usu&aacute;rios que n&atilde;o autenticam pelo dom&iacute;nio (Active Directory)</center></label>
                                                  </td>
                                              	</tr>
                                             	<tr>
                                                  <td class="labelConectado" id="labelLogin">Login:</td>
                                                  <td class="componente">
														<input id="login" name="login" class="campo2" maxlength="30" style="width: 150px;" value="<c:out value="${login}" />" type="text"/>
                                                  </td>
                                              	</tr>
                                                <tr>
                                                    <td class="labelConectado" id="labelSenha">Senha Atual:</td>
                                                    <td class="componente">
                                                        <input id="senha" name="senha" class="campo2" maxlength="30" style="width: 150px;" value="" type="password"/>
                                                    </td>
                                                </tr> 
                                                <tr>
                                                    <td class="labelConectado" id="labelSenha">Nova Senha:</td>
                                                    <td class="componente">
                                                        <input id="novaSenha" name="novaSenha" class="campo2" maxlength="30" style="width: 150px;" value="" type="password"/>
                                                    </td>
                                                </tr> 
                                                <tr>
                                                    <td class="labelConectado" id="labelSenha">Confirmação Nova Senha:</td>
                                                    <td class="componente">
                                                        <input id="confirmacaoNovaSenha" name="confirmacaoNovaSenha" class="campo2" maxlength="30" style="width: 150px;" value="" type="password"/>
                                                    </td>
                                                </tr> 
                                          </tbody>
                                      </table>
                                    </div> 
                                    <div id="botao-geral">
                                          <div id="botao" class="barBottom"> 
                                            	<input id="botao-cancelar" type="button" class="button" value="Cancelar" />
                                            	<input id="botao-avancar" type="button" class="buttonAtualizar120" value="Alterar Senha" />
                                            	<span id="loadInicial" class="hideLoad">Processando... &nbsp;<img src="images/ajax-loader.gif">&nbsp;</span>   
                                          </div>
                                    </div> 
                              </div>
	</div>
	<div id="mensagemDocumentos" class="mensagemDocumento"></div> 
</div>
		<br style="clear: both;" />
		<br style="clear: both;" />
		<br style="clear: both;" />
</form>