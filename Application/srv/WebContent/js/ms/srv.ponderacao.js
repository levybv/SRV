$(document).ready(function() {

	if (exibeInclusao=="true") {
		modal(850,360, 'paginas/ponderacao/incluiPonderacao.jsp','Marisa - Incluir Ponderação',true,'',true);        
	}

	$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
		
		$("#form").validate({
	    	rules: {},
	     	messages: {},                  
	        submitHandler : function(form){

	        	$("#div-botao").hide();  
	            $("#div-load").show(); 
				
	           	document.form.action = "ponderacao.do?operacao=inicio";   
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
   		

		$("#rdoRemuneracao1").click(function(){
    		$("#idCargoF").val("");
    		$("#idGrupoRemVarF").attr("disabled", false); 
    		$("#idCargoF").attr("disabled", true); 
   		}); 

		$("#rdoRemuneracao2").click(function(){
    		$("#idGrupoRemVarF").val("");
    		$("#idGrupoRemVarF").attr("disabled", true); 
    		$("#idCargoF").attr("disabled", false); 
   		}); 

		$("#rdoIndicador1").click(function(){
    		$("#idIndicadorF").val("");
    		$("#idGrupoIndicadorF").attr("disabled", false); 
    		$("#idIndicadorF").attr("disabled", true); 
   		}); 
	    	
		$("#rdoIndicador2").click(function(){
    		$("#idGrupoIndicadorF").val("");
    		$("#idGrupoIndicadorF").attr("disabled", true); 
    		$("#idIndicadorF").attr("disabled", false); 
   		}); 
   		
        $("#rdoFilial1").click(function(){
    		$("#idDescrFiliaisF").val("");
    		$("#idDescrTipoFiliaisF").attr("disabled", false);
    		$("#idDescrFiliaisF").attr("disabled", true);
   		});	

		
        $("#rdoFilial2").click(function(){
    		$("#idDescrTipoFiliaisF").val("");
    		$("#idDescrFiliaisF").attr("disabled", false); 
    		$("#idDescrTipoFiliaisF").attr("disabled", true);
   		});
        		
   		
	    $("#botaoConsultar").click(function(){
			 $("#form").submit();
		});	
		
		$("#botaoLimpar").click(function() {
			$("#idGrupoRemVarF").val("");
			$("#idCargoF").val("");
			$("#idGrupoIndicadorF").val("");
			$("#idIndicadorF").val("");
			$("#idDescrFiliaisF").val("");
			$("#idDescrTipoFiliaisF").val("");
			$("#idDescrFiliaisF").attr("disabled", false);
			$("#idDescrTipoFiliaisF").attr("disabled", true);
    		$("#idIndicadorF").attr("disabled", false); 
    		$("#idGrupoIndicadorF").attr("disabled", true); 
    		$("#idCargoF").attr("disabled", false); 
    		$("#idGrupoRemVarF").attr("disabled", true); 
    		$("#rdoFilial2").attr("checked", "true");
    		$("#rdoRemuneracao2").attr("checked", "true");
    		$("#rdoIndicador2").attr("checked", "true");
		});
		
		$("#atualizar").click(function() {

		   	var lista = $('input[name=checkbox]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Alterar Ponderação',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Alterar Ponderação',true,'Selecione um registro.',false); 
	    	} else {

				//Chave	    	
	    		idPonderacao          = lista[0].value.split("@")[0];
	    		descrGrupoRemVar      = lista[0].value.split("@")[1];
	    		descrCargo            = lista[0].value.split("@")[2];
	    		descrGrupoIndicador   = lista[0].value.split("@")[3];
	    		descrIndicador        = lista[0].value.split("@")[4];
	    		pesoFormatado         = lista[0].value.split("@")[5];
	    		valorPremioFormatado  = lista[0].value.split("@")[6];
	    		descrTipoFilial       = lista[0].value.split("@")[7];
	    		descrFilial           = lista[0].value.split("@")[8];
	    		idFilial              = lista[0].value.split("@")[9];
	    		idTipoFilial          = lista[0].value.split("@")[10];
	    		
	    		
	    		//Filtro
				idGrupoRemVarF 		= $("#idGrupoRemVarF").val();
				idCargoF   			= $("#idCargoF").val();
				idGrupoIndicadorF   = $("#idGrupoIndicadorF").val();
				idIndicadorF   		= $("#idIndicadorF").val();
				
				
				modal(850,360,'paginas/ponderacao/alteraPonderacao.jsp','Marisa - Alterar Ponderação',true,'',true);        
			}
		});
		
		
		$("#excluir").click(function() {

		   	var lista = $('input[name=checkbox]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Excluir Ponderação',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Excluir Ponderação',true,'Selecione um registro.',false); 
	    	} else {

				//Chave	    	
	    		idPonderacao          = lista[0].value.split("@")[0];
	    		descrGrupoRemVar      = lista[0].value.split("@")[1];
	    		descrCargo            = lista[0].value.split("@")[2];
	    		descrGrupoIndicador   = lista[0].value.split("@")[3];
	    		descrIndicador        = lista[0].value.split("@")[4];
	    		pesoFormatado         = lista[0].value.split("@")[5];
	    		valorPremioFormatado  = lista[0].value.split("@")[6];
	    		descrTipoFilial       = lista[0].value.split("@")[7];
	    		descrFilial           = lista[0].value.split("@")[8];
	    		idFilial              = lista[0].value.split("@")[9];
				
				modal(700,300,'paginas/ponderacao/excluiPonderacao.jsp','Marisa - Excluir Ponderação',true,'',true);        
			}
		});
				
		
		$("#incluir").click(function() {   
		
    		//Filtro
			idGrupoRemVarF 		= $("#idGrupoRemVarF").val();
			idCargoF   			= $("#idCargoF").val();
			idGrupoIndicadorF   = $("#idGrupoIndicadorF").val();
			idIndicadorF   		= $("#idIndicadorF").val();
			idDescrFiliaisF		= $("#idDescrFiliaisF").val();
			idDescrTipoFiliaisF = $("#idDescrTipoFiliaisF").val();

			modal(850,360, 'paginas/ponderacao/incluiPonderacao.jsp','Marisa - Incluir Ponderação',true,'',true);        
		});

		$("#idDescrFiliaisF").val($("#filialS").val());
		$("#idDescrTipoFiliaisF").val($("#tipoFilialS").val());
		$("#idGrupoRemVarF").val($("#grupoRemVarS").val());
		$("#idCargoF").val($("#cargoS").val());
		$("#idGrupoIndicadorF").val($("#grupoIndicadorS").val());
		$("#idIndicadorF").val($("#indicadorS").val());

		if($("#idDescrFiliaisF").val()==""){
    		$("#idDescrFiliaisF").attr("disabled", true);
    		$("#idDescrTipoFiliaisF").attr("disabled", false);
		} else{
    		$("#idDescrFiliaisF").attr("disabled", false);
    		$("#idDescrTipoFiliaisF").attr("disabled", true);
		}
		if($("#idGrupoRemVarF").val()==""){
    		$("#idCargoF").attr("disabled", true);
    		$("#idGrupoRemVarF").attr("disabled", false);
		} else{
    		$("#idCargoF").attr("disabled", false);
    		$("#idGrupoRemVarF").attr("disabled", true);
		}
		if($("#idGrupoIndicadorF").val()==""){
    		$("#idIndicadorF").attr("disabled", true);
    		$("#idGrupoIndicadorF").attr("disabled", false);
		} else{
    		$("#idIndicadorF").attr("disabled", false);
    		$("#idGrupoIndicadorF").attr("disabled", true);
		}

		if (rdoRemuneracaoF == "1") {
    		$("#idGrupoRemVarF").attr("disabled", false); 
    		$("#idCargoF").attr("disabled", true); 
    		$("#rdoRemuneracao1").attr("checked", "true");
		} else {
    		$("#idGrupoRemVarF").attr("disabled", true); 
    		$("#idCargoF").attr("disabled", false); 
    		$("#rdoRemuneracao2").attr("checked", "true");
		}

		if (rdoIndicadorF == "1") {
    		$("#idGrupoIndicadorF").attr("disabled", false); 
    		$("#idIndicadorF").attr("disabled", true); 
    		$("#rdoIndicador1").attr("checked", "true");
		} else {
    		$("#idGrupoIndicadorF").attr("disabled", true); 
    		$("#idIndicadorF").attr("disabled", false); 
    		$("#rdoIndicador2").attr("checked", "true");
		}
		
		if (rdoFilialF == "1") {
    		$("#idDescrTipoFiliaisF").attr("disabled", false); 
    		$("#idDescrFiliaisF").attr("disabled", true); 
    		$("#rdoFilial1").attr("checked", "true");
		} else {
    		$("#idDescrTipoFiliaisF").attr("disabled", true); 
    		$("#idDescrFiliaisF").attr("disabled", false); 
    		$("#rdoFilial2").attr("checked", "true");
		}

		$('#idGrupoRemVarF').focus();
		
});
