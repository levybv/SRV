$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
		$('#anoF').numeric();
		$('#idFilialF').numeric();
		$('#idIndicadorF').numeric();
		
		$("#form").validate({
	    	rules: {},
	     	messages: {},                  
	        submitHandler : function(form){

	        	$("#div-botao").hide();  
	            $("#div-load").show(); 
				
	           	document.form.action = "metaFilial.do?operacao=inicio";   
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

	    	 if ($("#mesF").val() == "" && $("#anoF").val() == "" && $("#idFilialF").val() == "" && $("#idIndicadorF").val() == "" && $("#descricaoIndicadorF").val() == "") {
		     	 modal(450,120,null,'Marisa - Consultas Metas de Filial',true,'Informe algum filtro de pesquisa.',false);
		     } else {
				 $("#form").submit();
		     }
	    
		});	
		
		$("#botaoLimpar").click(function() {
			$("#mesF").val("");
			$("#anoF").val("");
			$("#idFilialF").val("");
			$("#idIndicadorF").val("");
			$("#descricaoIndicadorF").val("");
		});
		
		$("#atualizar").click(function() {

		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Alterar Meta de Filial',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Alterar Meta de Filial',true,'Selecione um registro.',false); 
	    	} else {

				//Chave	    	
	    		var chave = lista[0].value;

	    		idIndicador = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		idEmpresa = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		idFilial = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		ano = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		mes = chave;
	    		
	    		//Filtro
	    		
				mesF 					= $("#mesF").val();
				anoF   					= $("#anoF").val();
				idFilialF   			= $("#idFilialF").val();
				idIndicadorF   			= $("#idIndicadorF").val();
				descricaoIndicadorF   	= $("#descricaoIndicadorF").val();
				
				modal(545,250,'paginas/meta/alteraMetaFilial.jsp','Marisa - Alterar Meta de Filial',true,'',true);        
			}
		});
		
		
		$("#excluir").click(function() {

		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Excluir Meta de Filial',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Excluir Meta de Filial',true,'Selecione um registro.',false); 
	    	} else {

				//Chave	    	
	    		var chave = lista[0].value;

	    		idIndicador = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		idEmpresa = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		idFilial = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		ano = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		mes = chave;
	    		
	    		//Filtro
	    		
				mesF 					= $("#mesF").val();
				anoF   					= $("#anoF").val();
				idFilialF   			= $("#idFilialF").val();
				idIndicadorF   			= $("#idIndicadorF").val();
				descricaoIndicadorF   	= $("#descricaoIndicadorF").val();
				
				modal(545,230,'paginas/meta/excluiMetaFilial.jsp','Marisa - Excluir Meta de Filial',true,'',true);        
			}
		});
				
		
		$("#incluir").click(function() {   
		
    		//Filtro
    		
			mesF 					= $("#mesF").val();
			anoF   					= $("#anoF").val();
			idFilialF   			= $("#idFilialF").val();
			idIndicadorF   			= $("#idIndicadorF").val();
			descricaoIndicadorF   	= $("#descricaoIndicadorF").val();
		
			modal(800,320,'paginas/meta/incluiMetaFilial.jsp','Marisa - Incluir Meta de Filial',true,'',true);        
		});
		
		
		$("#importar").click(function() {   
		
    		//Filtro
    		
			mesF 					= $("#mesF").val();
			anoF   					= $("#anoF").val();
			idFilialF   			= $("#idFilialF").val();
			idIndicadorF   			= $("#idIndicadorF").val();
			descricaoIndicadorF   	= $("#descricaoIndicadorF").val();
		
			modal(580,300,'paginas/meta/importaMetaFilial.jsp','Marisa - Importar Meta de Filial',true,'',true);        
		});		
		
		$('#mesF').focus();
});