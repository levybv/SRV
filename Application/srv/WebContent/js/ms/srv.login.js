
var fields;

$(document).ready(function() {	

	$("#form").validate({
    	rules: {
    		login: {required: true},
    		senha: {required: true}
     	},
     	messages: {
     		login: {required: '  <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Por favor informe o seu Login.</span>'},
     		senha: {required: '  <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Por favor informe a sua Senha.</span>'}
         },
         submitHandler : function(form){
			$("#botao-avancar").attr("disabled", "disabled");
			
			document.form.action = 'login.do?operacao=login';
	        submitForm();	
			
         }
        	             
    });

	$("#botao-avancar").click(function() {
		$("#form").submit();
	});

	$("#botao-altera-senha").click(function() {
		document.form.action = 'login.do?operacao=preparaAlteracaoSenha';
	    submitForm();
	});

	$('#login').focus();

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