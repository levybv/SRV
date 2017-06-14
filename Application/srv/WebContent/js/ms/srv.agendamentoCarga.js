$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
		$('#codigoCargaF').numeric();
		$("#anoF").numeric();
		$('#dataAgendamentoF').mask("99/99/9999");
		$('#dataUltimoProcessamentoF').mask("99/99/9999");
		
		$("#form").validate({
	    	rules: {},
	     	messages: {},                  
	        submitHandler : function(form){
	        	$("#div-botao").hide();  
	            $("#div-load").show(); 
	           	document.form.action = "agendamentoCarga.do?operacao=inicio";   
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
			$("#codigoCargaF").val("");
			$("#descricaoCargaF").val("");
			$("#dataUltimoProcessamentoF").val("");
			$("#dataAgendamentoF").val("");
			$("#anoF").val("");
			$("#mesF").val("");
			$("#flagAtivaF").val("");
		});
		
		$("#atualizar").click(function() {
		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Agendamento de Carga',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Agendamento de Carga',true,'Selecione um registro.',false); 
	    	} else {
	    		codigoCarga = lista[0].value;
				modal(600,290,'paginas/carga/alteraAgendamentoCarga.jsp','Marisa - Alterar Agendamento de Carga',true,'',true);        
			}
		});

		$("#reprocessar").click(function() {
			modal(600,140,'paginas/carga/reprocessarCarga.jsp','Marisa - Carga de Arquivos',true,'',true);        
		});

		$('#codigoCargaF').focus();
});