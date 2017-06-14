$(document).ready(function() {
	$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
		if(event.keyCode == 8)
			return false;
    });		

	$(".painel-filtro").show(100);

	$("#form").validate({
    	rules: {
    		mes:	 		{required:true},
    		ano:	 		{required:true},
    		tipo: 			{required:true},
    		arquivoUpload:	{required:true}
     	},
     	messages: {
     		mes:	 		{required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione o m&ecirc;s.</span>'},
     		ano:	 		{required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione o ano.</span>'},
     		tipo: 	 		{required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione um tipo de importa&ccedil;&atilde;o.</span>'},
     		arquivoUpload:	{required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione um arquivo.</span>'}
        },
        submitHandler : function(form){
    		$("#div-botao").hide();
           	$("#div-load").show();
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

	$(document).bind('keydown', 'esc', function () {
    	fecharTodosOsModais();
    });

	$("#botaoLimpar").click(function() {
		$("#mes").val("");
		$("#ano").val("");
		$("#tipo").val("");
		$("#arquivoUpload").val("");
		$("#mes").focus();
	});

	$("#botaoImportar").click(function() {
		if($("#form").valid()){
			document.form.action = "servlet/uploadDigitalizadoServlet";
			document.form.submit();
		}
	});
	
	$("#layoutDigitalizados").click(function() {
		modal(580,320,'paginas/digitalizado/layoutDigitalizado.jsp','Marisa - Layout Arquivos Digitalizado',true,'',true);
	});

	$("#mes").focus();
});
