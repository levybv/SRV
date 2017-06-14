
var fields;

$(document).ready(function() {	

    dwr.engine.setAsync(false);

	jQuery.validator.addMethod("confirmacaoIgualNovaSenha", function(value, element) {
		return (value == $("#novaSenha").val());
	});

	jQuery.validator.addMethod("segurancaSenha", function(value, element) {
		return (value.length >= 6);
	});

	jQuery.validator.addMethod("validaLogin", function(value, element) {
			var ret;
			UsuarioBusinessAjax.validaUsuarioSistema($("#login").val(), function (retorno) {
				if (retorno != null && retorno != "") {
					alert(retorno);
					ret = false;
				} else {
	    			ret = true;
				}
    		});
			return ret;
	});

	$("#form").validate({
    	rules: {
    		login: {required: true, validaLogin: true},
    		senha: {required: true},
    		novaSenha: {required: true, segurancaSenha: true},
    		confirmacaoNovaSenha: {required: true, confirmacaoIgualNovaSenha: true}
     	},
     	messages: {
     		login: {required: '  <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Por favor informe o seu Login.</span>',
     			 validaLogin: '  <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" />Usu&aacute;rio n&atilde;o permite altera&ccedil;&atilde;o de senha pelo SRV</span>'},
     		senha: {required: '  <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Por favor informe a sua Senha.</span>'},
     		novaSenha: {required: '  <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Por favor informe a sua Nova Senha.</span>',
     					segurancaSenha: '  <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> A senha precisa ter pelo menos 6 caracteres.</span>'},
     		confirmacaoNovaSenha: {required: '  <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Por favor informe a Confirma&ccedil;&atilde;o da sua Nova Senha.</span>',
						     	confirmacaoIgualNovaSenha: '  <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> A confirma&ccedil;&atilde;o precisa ser igual a nova senha.</span>'}
	    },
	    submitHandler : function(form){
			$("#botao-avancar").attr("disabled", "disabled");
			
			document.form.action = 'login.do?operacao=alterarSenha';
	        submitForm();	
	    }
    });
	
	$('#login').focus();
	$("#botao-avancar").click(function() {
		$("#form").submit();
	});	
	$("#botao-cancelar").click(function() {
		document.form.action = 'principal.do?operacao=iniciarSistema';
	    submitForm();
	});
});

function liberarBotao(){
	$("#botao-avancar").removeAttr("disabled"); 
}

function submitForm(){
    if ($.browser.mozilla) {
    	document.forms[0].submit();
    } else {
    	form.submit();	
    }  
}