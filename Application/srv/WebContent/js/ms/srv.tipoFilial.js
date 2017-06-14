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
				document.form.action = "tipoFilial.do?operacao=inicio";   
	            document.form.submit();
	        }
	        	              
	    });
	    
	    
	    $("#botaoConsultar").click(function(){
	    	 /*if ($("#dscrFilial").val() == "") {
		     	 modal(450,120,null,'Marisa - Consultar Tipo Filiais',true,'Informe algum filtro de pesquisa.',false);
		     } else {*/
				 document.form.action = "tipoFilial.do?operacao=inicio";
				 document.form.submit();
		     //}
		});
	
	
		
   		
	    $("#botaoLimpar").click(function(){
			 $("#dscrFilial").val("");
		});	   		
    
	   	
	   	$("#incluir").click(function() {
		     
    		//Filtro
    		idFilialFuncF           = $("#idFilialFunc").val();
			idFuncionarioF  		= $("#idFuncionarioF").val();
			nomeFuncionarioF   		= $("#nomeFuncionarioF").val();
			anoF   					= $("#anoF").val();
			mesF 					= $("#mesF").val();
		
			modal(480,200,'paginas/tipoFilial/incluiTipoFilial.jsp','Marisa - Incluir Tipo Filial',true,'',true);        
		});
	   	
		
		$("#atualizar").click(function() {   

		   	var lista = $('input[type=radio]:checked');
		
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Alterar Tipo Filial',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Alterar Tipo Filial',true,'Selecione um registro.',false); 
	    	} else {

	    		filialCodEnc         = lista[0].value.split("@")[0];
  	    		descrTpFilialEnc     = lista[0].value.split("@")[1];
  	    	 	codUsuTipoFilialEnc  = lista[0].value.split("@")[2];
  	    	 	dataTipoFilialEnc    = lista[0].value.split("@")[3];
  	    	 	
  	    	 	$("#codTipoFilialEnc").val(filialCodEnc);
  	    	 	$("#descrTipoFilialEnc").val(descrTpFilialEnc);
  	    	 	$("#dtTipoFilialEnc").val(dataTipoFilialEnc);
  	    	 	$("#codUsuTipoFilialEnc").val(codUsuTipoFilialEnc);
  	    	 	
  	    		codTpFilial    = $("#codTipoFilialEnc").val();
                descricaoTpFil = $("#descrTipoFilialEnc").val(); 
                codUsuTpFil    = $("#codUsuTipoFilialEnc").val();
                dataTpFil      = $("#dtTipoFilialEnc").val();
                  	    	   
				modal(350,240,'paginas/modal/alterar-config-tipoFilial.jsp','Marisa - Alterar Tipo Filial',true,'',true);        
			}
		});
		
		
		$("#excluir").click(function() {   

		   	var lista = $('input[type=radio]:checked');
		
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Alterar Tipo Filial',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Alterar Tipo Filial',true,'Selecione um registro.',false); 
	    	} else {

	    		filialCodEnc         = lista[0].value.split("@")[0];
  	    		descrTpFilialEnc     = lista[0].value.split("@")[1];
  	    	 	codUsuTipoFilialEnc  = lista[0].value.split("@")[2];
  	    	 	dataTipoFilialEnc    = lista[0].value.split("@")[3];
  	    	 	
  	    	 	$("#codTipoFilialEnc").val(filialCodEnc);
  	    	 	$("#descrTipoFilialEnc").val(descrTpFilialEnc);
  	    	 	$("#dtTipoFilialEnc").val(dataTipoFilialEnc);
  	    	 	$("#codUsuTipoFilialEnc").val(codUsuTipoFilialEnc);
  	    	 	
  	    		codTpFilial    = $("#codTipoFilialEnc").val();
                descricaoTpFil = $("#descrTipoFilialEnc").val(); 
                codUsuTpFil    = $("#codUsuTipoFilialEnc").val();
                dataTpFil      = $("#dtTipoFilialEnc").val();
                  	    	   
				modal(350,240,'paginas/tipoFilial/excluiTipoFilial.jsp','Marisa - Excluir Tipo Filial',true,'',true);        
			}
		}); 
		
		$('#codFilial').focus();
});