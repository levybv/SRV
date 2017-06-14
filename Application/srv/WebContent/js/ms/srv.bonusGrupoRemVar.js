$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
		$('#idGrupoRemVarF').numeric();
		
		$("#form").validate({
	    	rules: {},
	     	messages: {},
            submitHandler : function(form){

	        	$("#div-botao").hide();  
	        	$("#div-load").show(); 
				
	           	document.form.action = "grupoRemVarBonus.do?operacao=inicio";   
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
			$("#idGrupoRemVarF").val("");
			$("#descricaoF").val("");
		});    
    
	    $("#botao-avancar").click(function(){
			 $("#formBusca").submit();
		});	
		
		$("#processar").click(function() {

		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(570,120,null,'Marisa - Processar Desempenho do Colaborador por Grupo de Remuneração',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(570,120,null,'Marisa - Processar Desempenho do Colaborador por Grupo de Remuneração',true,'Selecione um registro.',false); 
	    	} else {
	    		
	    		var grupoRemVarSelecionado = lista[0].value;
	    		var posicao = grupoRemVarSelecionado.indexOf(";");
	    		
	    		idGrupoRemVar 			= grupoRemVarSelecionado.substring(0, posicao);
	    		descricaoGrupoRemVar	= grupoRemVarSelecionado.substring(posicao+1);
	    		
				idGrupoRemVarF     = $("#idGrupoRemVarF").val();
				descricaoF   	   = $("#descricaoF").val();
				
				modal(800,335,'paginas/grupoRemVar/processaBonusGrupoRemVar.jsp','Marisa - Processar Desempenho do Colaborador por Grupo de Remuneração',true,'',true);        
			}
		});
		
		$('#idGrupoRemVarF').focus();
});