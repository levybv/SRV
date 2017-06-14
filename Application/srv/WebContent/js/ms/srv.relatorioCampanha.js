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
	    	  mes: 			{required:true},
	    	  ano: 			{required:true},
	    	  campanhaF: 	{required:true}
	    	},
	     	messages: {
	     	  mes:   		{required:	  '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
	     	  ano:   		{required:	  '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
	     	  campanhaF:   	{required:	  '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
	     	},                  
	        submitHandler : function(form){
	        	RelatorioCampanhaBusinessAjax.validaPeriodo($("#inicioF").val(), $("#fimF").val(), $("#ano").val(), $("#mes").val(),
					function (msgCritica) {
	        			if (msgCritica != "") {
	        				alert(msgCritica);
	        			} else {
	    		           	document.form.action = "relatorioCampanha.do?operacao=geraRelatorioCampanha";   
	    		            document.form.submit();
	        			}
				});
			}
	    });

		RelatorioCampanhaBusinessAjax.obtemListaCampanha(function (listaCampanha) {
			if (listaCampanha != null) {
				var options = $('#campanhaF').attr('options');
				for (var i=0; i<listaCampanha.length; i++) {
					var campanhaVO = listaCampanha[i];
					options[options.length] = new Option(campanhaVO.idIndicador + " - " + campanhaVO.descricaoIndicador, campanhaVO.idIndicador, false, false);
				}
			}
		});

		$("#botaoLimpar").click(function() {
			$("#ano").val("");
			$("#mes").val("");
			$("#inicioF").val("");
			$("#fimF").val("");
			$("#campanhaF").val("");
			$("#ano").focus();
		});

		$("#ano").focus();
});