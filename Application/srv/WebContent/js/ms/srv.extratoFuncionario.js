$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
		
		$("#form").validate({
	    	rules: {
	    		periodos:  {required:true},
	    	},
	     	messages: {
		     	periodos:  {required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione um per&iacute;odo.</span>'},
	     	},
	        submitHandler : function(form) {
	        	$("#div-botao").hide();
	            $("#div-load").show();
	           	document.form.action = "extratoFuncionario.do?operacao=pesquisaExtratoFuncionario";
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
		
		$("#botaoLimpar").click(function() {
			$("#ano").val("");
			$("#mes").val("");
			$('#mes').focus();
		});
		
		$("#botaoVoltar").click(function() {
		   	document.form.action = "extratoFuncionario.do?operacao=inicio";
		    document.form.submit();
		});		
		
		$('#mes').focus();
});


function consultaDetalheCalculo(idFuncionario, idIndicador, idEmpresa, idFilial, ano, mes) {

	this.idFuncionario 	= idFuncionario;
	this.idIndicador 	= idIndicador;
	this.idEmpresa 		= idEmpresa;
	this.idFilial 		= idFilial;
	this.ano			= ano;
	this.mes 			= mes;

	modal(470,450,'paginas/indicador/detalheCalculo.jsp','Marisa - Detalhe do Cálculo',true,'',true);	
}