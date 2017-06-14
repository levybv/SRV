<script type='text/javascript' src='srvdwr/util.js'></script>
<script type='text/javascript' src='srvdwr/interface/PerfilBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/UsuarioBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>	
	
<script type="text/javascript" src="js/lib/jquery-1.4.2.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery-1.4.2.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.hotkeys-0.7.9.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.maskedinput-1.2.2.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.validate.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.tipsy.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.metadata.js"></script>
<script type="text/javascript" src="js/lib/jquery.hoverIntent.js"></script>



<script type="text/javascript" src="js/ns/neuro.1.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/ns/neuro.validate.1.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/ns/neuro.modal-1.0.js"  charset="UTF8" ></script>

<script type="text/javascript">

	$(document).ready(function() {
		$(document).bind('keydown', 'esc', function () {
			fecharModal();
		});
		
		$("#botaoCancelar").click(function(){
			fecharModal();
		});
		
		
		jQuery.validator.addMethod('validaLoginUsuario', function(value, element, params) {
			dwr.engine.setAsync(false);
			UsuarioBusinessAjax.validaLoginUsuario(value, 
				function (loginValido) {
					if (loginValido) {
						$("#loginValido").val("S");
						return false;
					} else {
						$("#loginValido").val("N");
						return true;
					} 
			});
			return ($("#loginValido").val() == "S");
		});
		
		jQuery.validator.addMethod('validaLoginJaExistente', function(value, element, params) {
			dwr.engine.setAsync(false);
			UsuarioBusinessAjax.validaLoginJaExistente(value, $("#idUsuarioM").val(),
				function (loginValido) {
					if (loginValido) {
						$("#loginValido").val("S");
						return false;
					} else {
						$("#loginValido").val("N");
						return true;
					} 
			});
			return ($("#loginValido").val() == "S");
		});
		
		jQuery.validator.addMethod('validaMatriculaUsuario', function(value, element, params) {
			dwr.engine.setAsync(false);
			if (value == "") return true;
			UsuarioBusinessAjax.validaMatriculaUsuario(value,
				function (matriculaValida) {
					if (matriculaValida) {
						$("#matriculaValida").val("S");
					} else {
						$("#matriculaValida").val("N");
					} 
			});
			return ($("#matriculaValida").val() == "S");
		});		
		
		$("#detFilial").validate({
	    	rules: {
	    			nomeM: {required:true},
	    			matriculaM: {validaMatriculaUsuario:true},
	    			loginM: {required:true, validaLoginUsuario:true, validaLoginJaExistente:true},
	    			perfilM: {required:true}
		     	},
		     	messages: {
			     	nomeM:  {required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	matriculaM:  {validaMatriculaUsuario:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Valor inv&aacute;lido.</span>'},
			     	loginM:  {required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
			     	         validaLoginUsuario:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Login inv&aacute;lido.</span>',
			     	         validaLoginJaExistente:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Login j&aacute; existente.</span>'},
			     	perfilM:  {required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
		         },                  
	         submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
		       	document.detFilial.action = "usuario.do?operacao=incluiUsuario";   
	            document.detFilial.submit();
	         }
    	});
    	
		PerfilBusinessAjax.obtemPerfis(
			function (perfis) { 
				if (perfis != null) {
					var options = $('#perfilM').attr('options');
					for (var i=0; i<perfis.length; i++) {
						var perfilVO = perfis[i];
						options[options.length] = new Option(perfilVO.descricao, perfilVO.idPerfil, false, false);
					}
				}    	
		});
    	
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 440px;">
	<form name="detFilial" id="detFilial" method="post"> 
			
			<input type='hidden' id="idUsuarioM" name="idUsuarioM" value="">
			<input type='hidden' id="matriculaValida" name="matriculaValida" value="">
			<input type='hidden' id="loginValido" name="loginValido" value="">
			
			<table class="tabelaComponente">
				<tbody> 
					<tr>
						<td class="componente" >
							<label class="label">Nome:<span class="requerido">*</span></label> 
						</td>
						<td class="componente" >
	  						<input type="text" class="campo2" id="nomeM" name="nomeM" value="" size="40" maxlength="50" />
						</td>
					</tr>
					<tr>
						<td class="componente" >
							<label class="label">Matr&iacute;cula:<span class="requerido">*</span></label> 
						</td>
						<td class="componente" >
	  						<input type="text" class="campo2" id="matriculaM" name="matriculaM" value="" size="30" maxlength="38" />
						</td>
					</tr>					
					<tr>
						<td class="componente" >
							<label class="label">Ativo:</label> 
						</td>
						<td class="componente" >
	  						<input class="campo2" type="checkbox" id="ativoM" name="ativoM" />
						</td>
					</tr>
					<tr>
						<td class="componente" >
							<label class="label">Autenticar AD:</label> 
						</td>
						<td class="componente" >
	  						<input class="campo2" type="checkbox" id="autenticaAdM" name="autenticaAdM" checked="checked" />
						</td>
					</tr>
					<tr> 
						<td class="componente">
							<label class="label" >Login:<span class="requerido">*</span></label> 
						</td>
						<td class="componente" >
	  						<input type="text" class="campo2" id="loginM" name="loginM" value="" size="30" maxlength="30" />
						</td>
					</tr>
					<tr>
						<td class="componente">
							<label class="label" >Perfil:<span class="requerido">*</span></label> 
						</td>
						<td class="componente" >
							<select id="perfilM" name="perfilM" class="campo">
								<option value="" selected>Selecione...</option>
							</select>
						</td>
					</tr>
				</tbody>
			</table>
			<br/>
		<div id="botao-geral" style="width:100%;">
			<div id="botao" class="barBottom" style="width:100%;"> 			
				<div class="div-botao" style="float:right;width:100%;">
					<input id="botaoSalvar" type="submit" class="button" value="Salvar" />
					<input id="botaoCancelar" type="button" class="button" value="Cancelar" />					
				</div>
			</div>
		</div>
</form>
</div>