$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
		
		$("#form").validate({
	    	rules: {
	    	  mes: {required:true},
	    	  ano: {required:true},
	    	  tpRelatorio: {required:true}
	    	},
	     	messages: {
	     	  mes:   {required:	  '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
	     	  ano:   {required:	  '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
	     	  tpRelatorio:   {required:	  '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
	     	},                  
	        submitHandler : function(form){
	           	document.form.action = "relatorioLoja.do?operacao=geraRelatorioLoja";   
	            document.form.submit();
			}
	    });

		
		$("#botaoLimpar").click(function() {
			$("#ano").val("");
			$("#mes").val("");
			$("#tpRelatorio").val("");
		});
		
		
		$("#importar").click(function() {   
			idFuncionarioF  		= $("#idFuncionarioF").val();
			nomeFuncionarioF   		= $("#nomeFuncionarioF").val();
			anoF   					= $("#anoF").val();
			mesF 					= $("#mesF").val();
		});		
		
		
});