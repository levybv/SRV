$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
		
		$("#form").validate({
	    	rules: {
	     	},
	     	messages: {
	        },                  
	        submitHandler : function(form){
	        	$("#div-botao").hide();  
	            $("#div-load").show(); 
				document.form.action = "filial.do?operacao=executarFiltro";   
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
   		
	    $("#botaoLimpar").click(function(){
			 $("#codFilial").val("");
			 $("#apenasAtivas").removeAttr("checked");
		});	   		
    
		$("#botaoIncluir").click(function(){
			
			idFilialFuncF           = $("#idFilialFunc").val();
			idFuncionarioF  		= $("#idFuncionarioF").val();
			nomeFuncionarioF   		= $("#nomeFuncionarioF").val();
			anoF   					= $("#anoF").val();
			mesF 					= $("#mesF").val();
					
			modal(350,310,'paginas/filial/incluiFilial.jsp','Marisa - Incluir Filial',true,'',true);
		});   		
    
	    $("#botao-avancar").click(function(){
			 $("#formBusca").submit();
		});	
		
		
		$("#atualizar").click(function() {   

		   	var lista = $('input[type=radio]:checked');
		
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Alterar Filial',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Alterar Filial',true,'Selecione um registro.',false); 
	    	} else {

	    		filialCod = lista[0].value.split("@")[0];
  	    		empCod = lista[0].value.split("@")[1];   
  	    		descricaoFil   = lista[0].value.split("@")[2];
  	    		flgAtivo       = lista[0].value.split("@")[3];
  	    		cnpj           = lista[0].value.split("@")[4];   
  	    		descricaoTpFil = lista[0].value.split("@")[5];
  	    		flagMeta100    = lista[0].value.split("@")[6];
  	    		tpfilialCod    = lista[0].value.split("@")[7];
  	    		dtInaug		   = lista[0].value.split("@")[8];
  	    		
  	    		//alert(filialCod+'-'+empCod+'-'+descricaoFil+'-'+flgAtivo+'-'+cnpj+'-'+descricaoTpFil+'-'+flagMeta100);   
  	    		//alert(lista[0].value.split("@"));   
  	    		
				modal(350,310,'paginas/modal/alterar-config-filial.jsp','Marisa - Alterar Filial',true,'',true);        
			}
		}); 
		
		$('#codFilial').focus();
});