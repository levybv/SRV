$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
    	
    	EscalaBusinessAjax.obtemEscalas(
			function (escalas) {
				if (escalas != null) {
					var options = $('#idEscalaF').attr('options');
					for (var i=0; i<escalas.length; i++) {
						var escalaVO = escalas[i];
						options[options.length] = new Option(escalaVO.descricaoEscala, escalaVO.idEscala, false, false);
					}
					$('#idEscalaF').val(idEscala);
				}
		});	
		
		$("#form").validate({
	    	rules: {
	    		idEscalaF: 		{required:true}
	    	},
	     	messages: {
	     		idEscalaF:    	{required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
	     	},
	        submitHandler : function(form){

	        	$("#div-botao").hide();  
	            $("#div-load").show(); 
	            
	            /*
	            if (alteracoesPendentes) {
	            	if (!confirm("Se for realizada uma nova pesquisa as alterações serão perdidas. Deseja continuar?")) {
	            		return;
	            	}
	            }
	            */
				
	           	document.form.action = "faixaEscala.do?operacao=inicio";   
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
		});
		
		$("#atualizar").click(function() {

		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Alterar Faixa de Escala',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Alterar Faixa de Escala',true,'Selecione um registro.',false); 
	    	} else {

				//Chave	    	
	    		var chave = lista[0].value;

	    		idEscala = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		sequencial = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		idUnidadeFaixa = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		faixaInicialFormatada = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		faixaFinalFormatada = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		idUnidadeRealizado = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		realizadoFormatado = chave;	    		

	    		//Filtro
				idEscalaF 	= $("#idEscalaF").val();
				
				modal(525,255,'paginas/escala/alteraFaixaEscala.jsp','Marisa - Alterar Faixa de Escala',true,'',true);        
			}
		});
		
		
		$("#excluir").click(function() {

		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Excluir Faixa de Escala',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Excluir Faixa de Escala',true,'Selecione um registro.',false); 
	    	} else {

				//Chave	    	
	    		var chave = lista[0].value;

	    		idEscala = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		sequencial = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		idUnidadeFaixa = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		faixaInicialFormatada = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		faixaFinalFormatada = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		idUnidadeRealizado = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		realizadoFormatado = chave;	  		

	    		//Filtro
				idEscalaF 	= $("#idEscalaF").val();
				
				modal(480,200,'paginas/escala/excluiFaixaEscala.jsp','Marisa - Excluir Faixa de Escala',true,'',true);        
			}
		});
				
		
		$("#incluir").click(function() {   
		
      		//Filtro
			idEscalaF 	= $("#idEscalaF").val();
		
			modal(525,255,'paginas/escala/incluiFaixaEscala.jsp','Marisa - Incluir Faixa de Escala',true,'',true);        
		});
		
		
		$("#confirmar").click(function() {   
			if (confirm("Salvar todas as alterações das faixas dessa escala?")) {
	           	document.form.action = "faixaEscala.do?operacao=salvarAlteracoes";   
	            document.form.submit();		
			}
		});		
		
		$("#descartar").click(function() {   
			if (confirm("Descartar todas as alterações das faixas dessa escala?")) {
	           	document.form.action = "faixaEscala.do?operacao=descartarAlteracoes";   
	            document.form.submit();		
			}
		});	
		
		$("#botaoEscalas").click(function() {   
			$("#idEscalaF").val("");
           	document.form.action = "escala.do?operacao=inicio";   
            document.form.submit();		
		});			
		
		$('#idEscalaF').focus();
});