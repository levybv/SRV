$(document).ready(function() {

        dwr.engine.setAsync(false);

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
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

		$(".painel-filtro").show(100);
		
		$("#botaoVoltar").click(function() {
		   	document.form.action = "bonusAnualFuncionario.do?operacao=inicio";
		    document.form.submit();
		});		

		$("#aceite").click(function() {   
		   	document.form.action = "bonusAnualFuncionario.do?operacao=aceiteIndicadorFuncionario";
		    document.form.submit();
		});

		$("#imprimir").click(function() {   
		   	document.form.action = "bonusAnualFuncionario.do?operacao=imprimiIndicadores";
		    document.form.submit();
		});

		$("#simular").click(function() {   
		   	document.form.action = "bonusAnualFuncionario.do?operacao=pesquisaIndicadorFuncionario";
		   	$("#acaoTela").val("simulaBonus");
		    document.form.submit();
		});

});


function consultaDetalheCalculo(idFuncionario, idIndicador, idEmpresa, idFilial, ano, mes) {

	this.idFuncionario 	= idFuncionario;
	this.idIndicador 	= idIndicador;
	this.idEmpresa 		= idEmpresa;
	this.idFilial 		= idFilial;
	this.ano			= ano;
	this.mes 			= mes;
	
	modal(470,500,'paginas/bonus/detalheCalculo.jsp','Marisa - Detalhe do C&aacute;lculo',true,'',true);	
}