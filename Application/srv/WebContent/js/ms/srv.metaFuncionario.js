$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
		$('#anoF').numeric();
		$('#idFuncionarioF').numeric();
		$('#idIndicadorF').numeric();
		
		$("#form").validate({
	    	rules: {},
	     	messages: {},                  
	        submitHandler : function(form){

	        	$("#div-botao").hide();  
	            $("#div-load").show(); 
				
	           	document.form.action = "metaFuncionario.do?operacao=inicio";   
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
	    	 if ($("#mesF").val() == "" && $("#anoF").val() == "" && $("#idFuncionarioF").val() == "" && $("#nomeFuncionarioF").val() == "" && $("#idIndicadorF").val() == "" && $("#descricaoIndicadorF").val() == "") {
		     	 modal(450,120,null,'Marisa - Consultar Metas de Funcionário',true,'Informe algum filtro de pesquisa.',false);
		     } else {
				 $("#form").submit();
		     }
		});	
		
		$("#botaoLimpar").click(function() {
			$("#mesF").val("");
			$("#anoF").val("");
			$("#idFuncionarioF").val("");
			$("#nomeFuncionarioF").val("");
			$("#idIndicadorF").val("");
			$("#descricaoIndicadorF").val("");
		});
		
		$("#atualizar").click(function() {

		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Alterar Meta de Funcionário',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Alterar Meta de Funcionário',true,'Selecione um registro.',false); 
	    	} else {

				//Chave	    	
	    		var chave = lista[0].value;

	    		idIndicador = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		idFuncionario = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		ano = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		mes = chave;
	    		
	    		//Filtro
				mesF 					= $("#mesF").val();
				anoF   					= $("#anoF").val();
				idFuncionarioF   		= $("#idFuncionarioF").val();
				nomeFuncionarioF   		= $("#nomeFuncionarioF").val();
				idIndicadorF   			= $("#idIndicadorF").val();
				descricaoIndicadorF   	= $("#descricaoIndicadorF").val();
				
				modal(545,260,'paginas/meta/alteraMetaFuncionario.jsp','Marisa - Alterar Meta de Funcionário',true,'',true);        
			}
		});
		
		
		$("#excluir").click(function() {

		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Excluir Meta de Funcionário',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Excluir Meta de Funcionário',true,'Selecione um registro.',false); 
	    	} else {

				//Chave	    	
	    		var chave = lista[0].value;

	    		idIndicador = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		idFuncionario = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		ano = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		mes = chave;
	    		
	    		//Filtro
				mesF 					= $("#mesF").val();
				anoF   					= $("#anoF").val();
				idFuncionarioF   		= $("#idFuncionarioF").val();
				nomeFuncionarioF   		= $("#nomeFuncionarioF").val();
				idIndicadorF   			= $("#idIndicadorF").val();
				descricaoIndicadorF   	= $("#descricaoIndicadorF").val();
				
				modal(545,230,'paginas/meta/excluiMetaFuncionario.jsp','Marisa - Excluir Meta de Funcionário',true,'',true);        
			}
		});
				
		
		$("#incluir").click(function() {   
		
    		//Filtro
			mesF 					= $("#mesF").val();
			anoF   					= $("#anoF").val();
			idFuncionarioF   		= $("#idFuncionarioF").val();
			nomeFuncionarioF   		= $("#nomeFuncionarioF").val();
			idIndicadorF   			= $("#idIndicadorF").val();
			descricaoIndicadorF   	= $("#descricaoIndicadorF").val();
		
			modal(800,320,'paginas/meta/incluiMetaFuncionario.jsp','Marisa - Incluir Meta de Funcionário',true,'',true);        
		});
		
		$("#importar").click(function() {   
		
    		//Filtro
			mesF 					= $("#mesF").val();
			anoF   					= $("#anoF").val();
			idFuncionarioF   		= $("#idFuncionarioF").val();
			nomeFuncionarioF   		= $("#nomeFuncionarioF").val();
			idIndicadorF   			= $("#idIndicadorF").val();
			descricaoIndicadorF   	= $("#descricaoIndicadorF").val();
		
			modal(580,160,'paginas/meta/importaMetaFuncionario.jsp','Marisa - Importar Meta de Funcionário',true,'',true);        
		});				
		
		$('#mesF').focus();
});