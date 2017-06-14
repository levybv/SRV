$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
		$('#idClasseHayF').numeric();
		
		$("#form").validate({
	    	rules: {},
	     	messages: {},                  
	        submitHandler : function(form){

	        	$("#div-botao").hide();  
	            $("#div-load").show(); 
				
	           	document.form.action = "classeHay.do?operacao=inicio";   
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
			$("#idClasseHayF").val("");
			$("#descricaoF").val("");
		});
		
		$("#atualizar").click(function() {

		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Alterar Classe Hay',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Alterar Classe Hay',true,'Selecione um registro.',false); 
	    	} else {
	    		idClasseHay = lista[0].value;
	    		
				idClasseHayF = $("#idClasseHayF").val();
				descricaoF   = $("#descricaoF").val();
				
				modal(520,200,'paginas/classeHay/alteraClasseHay.jsp','Marisa - Alterar Classe Hay',true,'',true);        
			}
		});
		
		$("#incluir").click(function() {   
		
			idClasseHayF = $("#idClasseHayF").val();
			descricaoF   = $("#descricaoF").val();
		
			modal(520,200,'paginas/classeHay/incluiClasseHay.jsp','Marisa - Incluir Classe Hay',true,'',true);        
		});
		
		$('#idClasseHayF').focus();
});