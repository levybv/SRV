$(document).ready(function() {
	$("#form").validate({
    	rules: {
    		anoSelecionadoF: {required:true}
     	},
     	messages: {
     		anoSelecionadoF:  {required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> &Eacute; necess&aacute;rio selecionar um ano.</span>'}
        },                  
        submitHandler : function(form){

    		$("#div-botao").hide();  
           	$("#div-load").show(); 
			
           	document.form.action = "calendarioComercial.do?operacao=inicio";   
            document.form.submit();
        }
	});
	
	$("#botaoLimpar").click(function() {
		$("#anoSelecionadoF").val("");
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

	$("#atualizar").click(function() {
	   	var lista = $('input[type=radio]:checked');
		if(lista.length > 1) {
			modal(450,120,null,'Marisa - Alterar Calendário Comercial',true,'Selecione apenas um registro.',false);
		} else if(lista.length < 1) {
			modal(450,120,null,'Marisa - Alterar Calendário Comercial',true,'Selecione um registro.',false);
    	} else {
    		anoSelecionadoFiltro = $("#anoSelecionadoF").val();
    		selecionado = lista[0].value;
    		idCalComerc = selecionado.substring(0,selecionado.indexOf(";"));
    		anoCalComerc = selecionado.substring(selecionado.indexOf(";")+1);
			modal(600,250,'paginas/calendario/comercial/alteraCalendarioComercial.jsp','Marisa - Alterar Calendário Comercial',true,'',true);
		}
	});

	$("#incluir").click(function() {
		anoSelecionadoFiltro = $("#anoSelecionadoF").val();
		modal(600,250,'paginas/calendario/comercial/incluiCalendarioComercial.jsp','Marisa - Incluir Calendário Comercial',true,'',true);        
	});

	$("#excluir").click(function() {
	   	var lista = $('input[type=radio]:checked');
		if(lista.length > 1) {  
			modal(450,120,null,'Marisa - Excluir Calendário Comercial',true,'Selecione apenas um registro.',false);
		} else if(lista.length < 1) {  
			modal(450,120,null,'Marisa - Excluir Calendário Comercial',true,'Selecione um registro.',false); 
    	} else {
    		anoSelecionadoFiltro = $("#anoSelecionadoF").val();
    		selecionado = lista[0].value;
    		idCalComerc = selecionado.substring(0,selecionado.indexOf(";"));
    		anoCalComerc = selecionado.substring(selecionado.indexOf(";")+1);
    		anoSelecionadoF = $("#anoSelecionadoF").val();
			modal(600,250,'paginas/calendario/comercial/excluiCalendarioComercial.jsp','Marisa - Excluir Calendário Comercial',true,'',true);        
		}
	});

});