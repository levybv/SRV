$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);

    	$("#inicioF").mask("99/99/9999");
    	$("#fimF").mask("99/99/9999");

		$("#form").validate({
	    	rules: {
	    	  mes: {required:true},
	    	  ano: {required:true}
	    	},
	     	messages: {
	     	  mes:   {required:	  '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
	     	  ano:   {required:	  '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
	     	},                  
	        submitHandler : function(form){
	        	RelatorioCallCenterBusinessAjax.validaPeriodo($("#inicioF").val(), $("#fimF").val(), $("#ano").val(), $("#mes").val(),
					function (msgCritica) {
	        			if (msgCritica != "") {
	        				alert(msgCritica);
	        			} else {
	    		           	document.form.action = "relatorioCallCenterRealizado.do?operacao=geraRelatorioCallCenterRealizado";   
	    		            document.form.submit();
	        			}
				});
			}
	    });

		
		$("#botaoLimpar").click(function() {
			$("#ano").val("");
			$("#mes").val("");
			$("#inicioF").val("");
			$("#fimF").val("");
			$("#ano").focus();
		});

		$("#ano").focus();
});