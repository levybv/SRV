<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript" src="js/ms/srv.login.js" charset="ISO-8859-1"></script>

<form id="form" name="form" method="post">
	<div class="boxIndex" style="width:650px;">
		<div class="boxCenter">
			<br />
			<div id="" class="panel">
				<div>
					<div id="" class="barTitle">Login</div>
				</div>
				<div id="painel-interno">
					<table class="tabelaComponente" cellpadding="2" cellspacing="2">
						<tbody>
							<tr>
								<td class="labelConectado" id="labelLogin">
									Login:
								</td>
								<td class="componente">
									<input type="text" id="login" name="login" class="campo2" maxlength="30" style="width: 150px;" value="<c:out value="${login}" />" />
								</td>
							</tr>
							<tr>
								<td class="labelConectado" id="labelSenha">
									Senha:
								</td>
								<td class="componente">
									<input type="password" id="senha" name="senha" class="campo2" maxlength="30" style="width: 150px;" value="<c:out value="${senha}" />" />
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div id="botao-geral">
					<div id="botao" class="barBottom">
						<input id="botao-avancar" type="button" class="button" value="Avançar" />
						<input id="botao-altera-senha" type="button" class="buttonAtualizar120" value="Alterar Senha" />
						<span id="loadInicial" class="hideLoad">Processando... &nbsp;<img src="images/ajax-loader.gif"/>&nbsp; </span>
					</div>
				</div>
			</div>
		</div>
	</div>
	<br style="clear: both;" />
	<br style="clear: both;" />
	<br style="clear: both;" />
</form>