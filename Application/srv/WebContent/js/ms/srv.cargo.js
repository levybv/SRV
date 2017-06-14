$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
		$('#idCargoF').numeric();
		
		$("#form").validate({
	    	rules: {},
	     	messages: {},                  
            submitHandler : function(form){

	        	$("#div-botao").hide();  
	        	$("#div-load").show(); 
				
	           	document.form.action = "cargo.do?operacao=inicio";   
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
    
		$("#botaoLimpar").click(function() {
			$("#idCargoF").val("");
			$("#descricaoF").val("");
			$("#idClasseHayF").val("");
			$("#agrupaFilialF").val("");
			$("#idGrupoRemuneracaoF").val("");
		});    
    
	    $("#botao-avancar").click(function(){
			 $("#formBusca").submit();
		});	
		
		$("#atualizar").click(function() {

		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Alterar Cargo',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Alterar Cargo',true,'Selecione um registro.',false); 
	    	} else {
	    		idCargo = lista[0].value;
	    		
				idCargoF     = $("#idCargoF").val();
				descricaoF   = $("#descricaoF").val();
				
				modal(550,390,'paginas/cargo/alteraCargo.jsp','Marisa - Alterar Cargo',true,'',true);        
			}
		});
		
		$("#incluir").click(function() {   
		
			idCargoF     = $("#idCargoF").val();
			descricaoF   = $("#descricaoF").val();
		
			modal(550,390,'paginas/cargo/incluiCargo.jsp','Marisa - Incluir Cargo',true,'',true);        
		});
		
		$('#idCargoF').focus();
});