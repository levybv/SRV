$(document).ready(function() {
    

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
		
		$("#form").validate({
	    	 rules: {
    			periodos: {required:true},
	     	 },
	     	 messages: {
		     	periodos:    	{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
	         },
	         submitHandler : function(form){
		        $("#div-botao").hide();  
	           	//$("#div-load").show(); 
		       	document.form.action = "relatorioBonusPagamento.do?operacao=montaTelaPrincipal";   
		        document.form.submit();
	         }
    	});

		
		$("#botaoLimpar").click(function() {
			$("#periodos").val("");
			$("#status").val("");
		});
		
		
		$("#exportar").click(function() {   
		
		 document.form.action = "relatorioBonusPagamento.do?operacao=exportaRelatorioExcel";   
		 document.form.submit();
		
			
		});	
		
		var status  = $("#statusS").val();
		var periodo = $("#periodosS").val();
		
		if(status != null){
		   $("#status").val(status);
		}
		
		if(periodo != null){
		   $("#periodos").val(periodo);
		}
		
		
});