$("#corpoLista tr").click(function () {
    tr = $(this);
    logradouro = tr.children().children().html();
    cep = tr.children('td:eq(1)').html();
    uf = $('#cliBuscaUf').val();
    bairro = tr.children('td:eq(2)').html();
    cidade = tr.children('td:eq(3)').html();
	
	var numeroCEP = cep;
	
	while(numeroCEP.length < 9) { 
		numeroCEP = "0" + numeroCEP;
	}
	
	cep = numeroCEP;
    
    if (formCEP == 0) {
    	$('#cliResCep').val(cep);
    	configCampoCEPGenerico('cliResUf',uf);
    	configCampoCEPGenerico('cliResCid',cidade);
    	configCampoCEPGenerico('cliResBai',bairro);
    	configCampoCEPGenerico('cliResRua',logradouro);
    	
    	if(cep == ''){
    		$('#cliResCep').focus();
    	}else if(uf == ''){
    		$('#cliResUf').focus();    		
    	}else if(cidade == ''){
    		$('#cliResCid').focus();
    	}else if(bairro == ''){
    		$('#cliResBai').focus();
    	}else if(logradouro == ''){
    		$('#cliResRua').focus();
    	}else{
    		$("#cliResNum").focus();
    	}
    	
    } else if (formCEP == 1) {
	    $('#cliEmpCep').val(cep);
    	configCampoCEPGenerico('cliEmpUf',uf);
    	configCampoCEPGenerico('cliEmpCid',cidade);
    	configCampoCEPGenerico('cliEmpBai',bairro);
    	configCampoCEPGenerico('cliEmpRua',logradouro);
    	
    	if(cep == ''){
    		$('#cliEmpCep').focus();
    	}else if(uf == ''){
    		$('#cliEmpUf').focus();    		
    	}else if(cidade == ''){
    		$('#cliEmpCid').focus();
    	}else if(bairro == ''){
    		$('#cliEmpBai').focus();
    	}else if(logradouro == ''){
    		$('#cliEmpRua').focus();
    	}else{
    		$("#cliEmpNum").focus();
    	}
    }
    fecharModal();

    if($('#cliResCep').val() != '' && tipoCadastro == 'P'){		
		validarEnderecoCEPUF();
	}

});

$("#corpoLista a").keydown(function () {
	if(event.keyCode == 32){
		$("#corpoLista tr").click();
	}
});

$("#corpoListaFilial tr").click(function () {
    
	tr = $(this);
	
	$("#cliFilialRetira").val('1#' + tr.children().children().html());

	fecharModal();
});

$("#corpoListaFilial a").keydown(function () {
	if(event.keyCode == 32){
		$("#corpoListaFilial tr").click();
	}
});

$("#corpoListaCoordenador tr").click(function () {
    
	tr = $(this);
	
	$("#filial").val(tr.children().children().html());

	fecharModal();
});

$("#corpoListaCoordenador a").keydown(function () {
	if(event.keyCode == 32){
		$("#corpoListaCoordenador tr").click();
	}
});