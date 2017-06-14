$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
		$('#idEscalaF').numeric();
		
		$("#form").validate({
	    	rules: {},
	     	messages: {},                  
	        submitHandler : function(form){

	        	$("#div-botao").hide();  
	            $("#div-load").show(); 
				
	           	document.form.action = "escala.do?operacao=inicio";   
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
    
	    $("#botaoConsultar").click(function(){
			 $("#form").submit();
		});	
		
		$("#botaoLimpar").click(function() {
			$("#idEscalaF").val("");
			$("#descricaoF").val("");
			$("#numEscalaF").val("");
		});
		
		$("#atualizar").click(function() {

		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Alterar Escala',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Alterar Escala',true,'Selecione um registro.',false); 
	    	} else {

				//Chave	    	
	    		idEscala = lista[0].value;

	    		//Filtro
				idEscalaF 	= $("#idEscalaF").val();
				descricaoF  = $("#descricaoF").val();
				
				modal(800,320,'paginas/escala/alteraEscala.jsp','Marisa - Alterar Escala',true,'',true);        
			}
		});
		
		
		$("#excluir").click(function() {

		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Excluir Escala',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Excluir Escala',true,'Selecione um registro.',false); 
	    	} else {

				//Chave	    	
	    		idEscala = lista[0].value;

	    		//Filtro
				idEscalaF 	= $("#idEscalaF").val();
				descricaoF  = $("#descricaoF").val();
				
				modal(650,250,'paginas/escala/excluiEscala.jsp','Marisa - Excluir Escala',true,'',true);        
			}
		});
				
		
		$("#incluir").click(function() {   
		
      		//Filtro
			idEscalaF 	= $("#idEscalaF").val();
			descricaoF  = $("#descricaoF").val();
		
			modal(800,320,'paginas/escala/incluiEscala.jsp','Marisa - Incluir Escala',true,'',true);        
		});

		
		$("#faixas").click(function() {   
		
		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Faixas de Escala',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Faixas de Escala',true,'Selecione um registro.',false); 
	    	} else {
	           	document.form.action = "faixaEscala.do?operacao=inicio&idEscalaF=" + lista[0].value;
	            document.form.submit();
			}
		});
		
		$('#idEscalaF').focus();
});