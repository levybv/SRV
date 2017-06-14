$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
		$('#anoF').numeric();
		
		$("#form").validate({
	    	rules: {},
	     	messages: {},                  
	        submitHandler : function(form){

	        	$("#div-botao").hide();  
	            $("#div-load").show(); 
				
	           	document.form.action = "processoPeriodo.do?operacao=inicio";   
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

	    	 if ($("#anoF").val() == "") {
		     	 modal(450,120,null,'Marisa - Períodos de Processamento',true,'Informe o ano para pesquisa.',false);
		     } else {
				 $("#form").submit();
		     }
	    
		});	
		
		$("#botaoLimpar").click(function() {
			$("#anoF").val("");
			$("#mesF").val("");
		});
		
		$("#atualizar").click(function() {

		   	var lista = $('input[type=checkbox]:checked');
		   	if(lista.length <= 0) {
				modal(450,120,null,'Marisa - Períodos de Processamento',true,'Selecione o registro.',false); 
	    	} else {

	    		//Filtro
				anoFiltro = $("#anoF").val();
				mesFiltro = $("#mesF").val();

	    		listaAtualizar = lista;

	    		modal(600,280,'paginas/processo/alteraProcessoPeriodo.jsp','Marisa - Alterar Período de Processamento',true,'',true);        
			}
		});
		
		
		$("#incluir").click(function() {   
		
    		//Filtro
			anoFiltro = $("#anoF").val();
			mesFiltro = $("#mesF").val();
	
			modal(700,180,'paginas/processo/incluiProcessoPeriodo.jsp','Marisa - Incluir Período de Processamento',true,'',true);        
		});

		$("#reprocessar").click(function() {
			modal(600,140,'paginas/processo/reprocessar.jsp','Marisa - Reprocessar Período',true,'',true);        
		});

		$('#anoF').focus();
});