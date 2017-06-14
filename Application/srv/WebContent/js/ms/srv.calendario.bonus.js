$(document).ready(function() {

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

	$("#dataInicialF").mask("99/99/9999");
	$("#dataFinalF").mask("99/99/9999");

	$("#form").validate({
    	rules: {
     	},
     	messages: {
        },                  
        submitHandler : function(form){
    		$("#div-botao").hide();  
           	$("#div-load").show(); 
           	document.form.action = "calendarioBonus.do?operacao=inicio";   
            document.form.submit();
        }
	});
	
	$("#botaoLimpar").click(function() {
		$("#anoF").val("");
		$("#mesF").val("");
		$("#mesDescrF").val("");
		$("#dataInicialF").val("");
		$("#dataFinalF").val("");
	});
	
	$("#atualizar").click(function() {
	   	var lista = $('input[type=radio]:checked');
		if(lista.length > 1) {
			modal(450,120,null,'Marisa - Alterar Calendário Bônus',true,'Selecione apenas um registro.',false);
		} else if(lista.length < 1) {
			modal(450,120,null,'Marisa - Alterar Calendário Bônus',true,'Selecione um registro.',false);
    	} else {

    		anoFiltro = $("#anoF").val();
    		mesFiltro = $("#mesF").val();
    		mesDescrFiltro = $("#mesDescrF").val();
    		dataInicialFiltro = $("#dataInicialF").val();
    		dataFinalFiltro = $("#dataFinalF").val();

    		selecionado = lista[0].value;
    		mesBonus = selecionado.substring(0,selecionado.indexOf(";"));
    		anoBonus = selecionado.substring(selecionado.indexOf(";")+1);
			modal(600,250,'paginas/calendario/bonus/alteraCalendarioBonus.jsp','Marisa - Alterar Calendário Bônus',true,'',true);
		}
	});

	$("#incluir").click(function() {

		anoFiltro = $("#anoF").val();
		mesFiltro = $("#mesF").val();
		mesDescrFiltro = $("#mesDescrF").val();
		dataInicialFiltro = $("#dataInicialF").val();
		dataFinalFiltro = $("#dataFinalF").val();

		modal(600,250,'paginas/calendario/bonus/incluiCalendarioBonus.jsp','Marisa - Incluir Calendário Bônus',true,'',true);        
	});

	$("#excluir").click(function() {
	   	var lista = $('input[type=radio]:checked');
		if(lista.length > 1) {  
			modal(450,120,null,'Marisa - Excluir Calendário Bônus',true,'Selecione apenas um registro.',false);
		} else if(lista.length < 1) {  
			modal(450,120,null,'Marisa - Excluir Calendário Bônus',true,'Selecione um registro.',false); 
    	} else {

    		anoFiltro = $("#anoF").val();
    		mesFiltro = $("#mesF").val();
    		mesDescrFiltro = $("#mesDescrF").val();
    		dataInicialFiltro = $("#dataInicialF").val();
    		dataFinalFiltro = $("#dataFinalF").val();

    		selecionado = lista[0].value;
    		mesBonus = selecionado.substring(0,selecionado.indexOf(";"));
    		anoBonus = selecionado.substring(selecionado.indexOf(";")+1);
			modal(600,250,'paginas/calendario/bonus/excluiCalendarioBonus.jsp','Marisa - Excluir Calendário Bônus',true,'',true);        
		}
	});

});