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
				
	           	document.form.action = "acompMetaFilial.do?operacao=inicio";   
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
	    	 if ($("#mesF").val() == "" || $("#anoF").val() == "") {
		     	 modal(450,120,null,'Marisa - Acompanhamento de Metas de Filial',true,'O período (mês e ano) precisa ser informado.',false);
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
			$("#apenasNaoRealizadasF").attr('checked', false);
		});

		$("#importar").click(function() {

			acompMetaFilialF		= $("#acompMetaFilialF").val();
		
    		//Filtro
			mesF 					= $("#mesF").val();
			anoF   					= $("#anoF").val();
			idFilialF   			= $("#idFilialF").val();
			idIndicadorF   			= $("#idIndicadorF").val();
			descricaoIndicadorF   	= $("#descricaoIndicadorF").val();
			
			
			modal(580,160,'paginas/meta/importaMetaFilial.jsp','Marisa - Importar Meta de Filial',true,'',true);
		});
		
		$('#mesF').focus();
});