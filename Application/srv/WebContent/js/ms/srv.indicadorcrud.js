$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
		$('#idIndicadorF').numeric();
		
		$("#form").validate({
	    	rules: {},
	     	messages: {},                  
	        submitHandler : function(form){

	        	$("#div-botao").hide();  
	            $("#div-load").show(); 
				
	           	document.form.action = "indicador.do?operacao=inicio";   
	            document.form.submit();
			}
	    });
	
		$("#arrow-link").click(function(){
		 	if ($(".painel-filtro").is(":visible")) {
            	  $(".painel-filtro").hide(100);
              	  $("#arrow").attr("src","images/arrow_down_48.png");
              	  $("#arrow").attr("class","arrow");   
	        } else {  
    	          $(".painel-filtro").show(100);
        	      $("#arrow").attr("src","images/arrow_up_48.png");
            }
   		});  
    
	    $("#botao-avancar").click(function(){
			 $("#formBusca").submit();
		});	
		
		$("#botaoLimpar").click(function() {
			$("#idIndicadorF").val("");
			$("#descricaoF").val("");
			$("#ativoF").val("S");
			$("#grupoIndicadorF").val("");
			$("#fonteF").val("");
			$("#diretoriaF").val("");
			$("#periodoF").val("");
		});
		
		$("#atualizar").click(function() {

		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Alterar Indicador',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Alterar Indicador',true,'Selecione um registro.',false); 
	    	} else {
	    		idIndicador = lista[0].value;
	    		altIndicador = $("#altIndicador").val();
				idIndicadorF = $("#idIndicadorF").val();
				descricaoF   = $("#descricaoF").val();
				modal(750,560, 'paginas/indicador/alteraIndicador.jsp','Marisa - Alterar Indicador',true,'',true);        
			}
		});
		
		$("#incluir").click(function() {   
    		altIndicador = $("#altIndicador").val();
			idIndicadorF = $("#idIndicadorF").val();
			descricaoF   = $("#descricaoF").val();
			modal(750,560,'paginas/indicador/incluiIndicador.jsp','Marisa - Incluir Indicador',true,'',true);        
		});
		
		$('#idIndicadorF').focus();
});