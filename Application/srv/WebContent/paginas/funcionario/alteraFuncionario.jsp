<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type='text/javascript' src='srvdwr/interface/FilialBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/CargoBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/FuncionarioBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>	
	
<script type="text/javascript" src="js/lib/jquery-1.4.2.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.alphanumeric.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.hotkeys-0.7.9.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.maskedinput-1.2.2.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.validate.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.tipsy.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.metadata.js"></script>
<script type="text/javascript" src="js/lib/jquery.hoverIntent.js"></script>



<script type="text/javascript" src="js/ns/neuro.1.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/ns/neuro.validate.1.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/ns/neuro.modal-1.0.js"  charset="UTF8" ></script>

<script type="text/javascript">

	$(document).ready(function() {
		$(document).bind('keydown', 'esc', function () {
			fecharModal();
		});
		
		$("#botaoCancelar").click(function(){
			fecharModal();
		});
		
		
		FuncionarioBusinessAjax.obtemFuncionario(idFuncionario,

			function (funcionarioVO) {
			
				$('#idFuncionarioF').val(funcionarioVO.idFuncionario);
				$('#idFuncionarioDIV').html(funcionarioVO.idFuncionario);
				$('#idEmpresaF').val(funcionarioVO.idEmpresa);
				$('#idFilialRHF').val(funcionarioVO.idFilialRH);
				$('#descricaoFilialRHF').val(funcionarioVO.descricaoFilialRH);
				$('#crachaF').val(funcionarioVO.cracha);
				$('#cpfFuncionarioF').val(funcionarioVO.cpfFuncionarioFormatado);  

				$('#idPromocaoElegivel').val(funcionarioVO.dataPromocaoElegivelFormatada);  
				$('#idMotivoDemissao').val(funcionarioVO.codigoMotivoDEmissao==null?"":funcionarioVO.codigoMotivoDEmissao);  
				$('#idQuantidadeAfast').val(funcionarioVO.quantidadeDiasAfastamento==null?"":funcionarioVO.quantidadeDiasAfastamento);  
				$('#idCentroCusto').val(funcionarioVO.flagIndicadorCentroCusto);  
				
				$('#nomeFuncionarioF').val(funcionarioVO.nomeFuncionario);
				if (funcionarioVO.idSituacaoRH != null) {
					$('#idSituacaoRHF').val(funcionarioVO.idSituacaoRH);
				}
				if (funcionarioVO.descricaoSituacaoRH != null) {
					$('#descricaoSituacaoRHF').val(funcionarioVO.descricaoSituacaoRH);
				}
				$('#dataSituacaoRHF').val(funcionarioVO.dataSituacaoRHFormatada);   
				$('#idEmpresaRHF').val(funcionarioVO.idEmpresaRH);
				$('#descricaoEmpresaRHF').val(funcionarioVO.descricaoEmpresaRH);
				$('#idCentroCustoF').val(funcionarioVO.idCentroCusto);
				$('#descricaoCentroCustoF').val(funcionarioVO.descricaoCentroCusto);
				if (funcionarioVO.idFuncionarioSuperior != null) {
					$('#idFuncionarioSuperiorF').val(funcionarioVO.idFuncionarioSuperior);        
				}
				if (funcionarioVO.idFuncionarioAvaliador != null) {
					$('#idFuncionarioAvaliadorF').val(funcionarioVO.idFuncionarioAvaliador);      
				}
				$('#dataAdmissaoF').val(funcionarioVO.dataAdmissaoFormatada);						  
				$('#dataDemissaoF').val(funcionarioVO.dataDemissaoFormatada);						   

				if(funcionarioVO.idSituacaoAnterior != null){
				   $('#idSituacaoAnteriorRHF').val(funcionarioVO.idSituacaoAnterior);
				}
				
			    if(funcionarioVO.descricaoSituacaoAnterior != null){
				   $('#descricaoSituacaoAnteriorRHF').val(funcionarioVO.descricaoSituacaoAnterior);
				}
										  
				$('#dataInicioSituacaoAnteriorRHF').val(funcionarioVO.dataInicioSituacaoAnteriorFormatada);						  
				$('#idQtdDiasF').val(funcionarioVO.qtdDiasTrabalhadosMes);
										   
				CargoBusinessAjax.obtemCargos( 
					function (cargos) {
						if (cargos != null) {
							var options = $('#idCargoF').attr('options');
							for (var i=0; i<cargos.length; i++) {
								var cargoVO = cargos[i];
								options[options.length] = new Option(cargoVO.descricaoCargo, cargoVO.idCargo, false, false);
							}
							$('#idCargoF').val(funcionarioVO.idCargo);
						}
				});
		
				FilialBusinessAjax.obtemTodasFiliaisAtivas( 
					function (filiais) {
						if (filiais != null) {
							var options = $('#idFilialF').attr('options');
							for (var i=0; i<filiais.length; i++) {
								var filialVO = filiais[i];
								options[options.length] = new Option(filialVO.descricao, filialVO.codFilial, false, false);
							}
							$('#idFilialF').val(funcionarioVO.idFilial);
						}
				});
		});

		
		$("#cpfFuncionarioF").mask("999.999.999-99");
		$("#idSituacaoRHF").numeric();
		$("#dataSituacaoRHF").mask("99/99/9999");  
		$("#idEmpresaRHF").numeric();
		$("#idFuncionarioSuperiorF").numeric();
		$("#idFuncionarioAvaliadorF").numeric();
		$("#dataAdmissaoF").mask("99/99/9999");
		$("#dataDemissaoF").mask("99/99/9999");
		$("#dataInicioSituacaoAnteriorRHF").mask("99/99/9999");
		$("#idPromocaoElegivel").mask("99/99/9999");
		$("#idQtdDiasF").numeric();
		$("#idSituacaoAnteriorRHF").numeric();

		jQuery.validator.addMethod('funcionarioNaoExistex', function(value, element, params) {
			dwr.engine.setAsync(false);
			if (value == "") {
				return true;
			}
			FuncionarioBusinessAjax.funcionarioExiste(value, 
				function (funcionarioEncontrado) {
					if (funcionarioEncontrado) {
						$("#funcionarioEncontrado").val("S");
						return true;
					} else {
						$("#funcionarioEncontrado").val("N");
						return false;
					} 
					
			});
			return ($("#funcionarioEncontrado").val() == "S");
		});			
		
		jQuery.validator.addMethod('dataValida', function(value, element, params) {
			var dataIniSplit = value.split('/');
			if (!validarData(dataIniSplit) && value != ''){
				return false;
			} else {
				return true;
			}
		});
		
		jQuery.validator.addMethod('cpfValido', function(value, element, params) {
			if (value != '' && !validarCPF(value)){
				return false;
			} else {
				return true;
			}
		});


		$("#detMeta").validate({
	    	rules: {
	    			idFilialF:   			{required:true},
	    			idFilialRHF:   			{required:true},
	    			descricaoFilialRHF: 	{required:true},
	    			idCargoF: 				{required:true},
	    			crachaF: 				{required:true},
	    			cpfFuncionarioF: 		{required:true, cpfValido: true},
	    			nomeFuncionarioF: 		{required:true},
	    			dataSituacaoRHF:		{dataValida:true},
	    			idEmpresaRHF: 			{required:true},
	    			descricaoEmpresaRHF: 	{required:true},
	    			idCentroCustoF: 		{required:true},
	    			descricaoCentroCustoF: 	{required:true},
	    			idFuncionarioSuperiorF: {funcionarioNaoExistex:true},
	    			idFuncionarioAvaliadorF:{funcionarioNaoExistex:true},
	    			dataInicioSituacaoAnteriorRHF: {dataValida:true},
	    			idCentroCusto:				   {required:true},
	    			dataAdmissaoF: 			{required:true, dataValida:true},
	    			dataDemissaoF: 			{dataValida:true}
	     	},
	     	messages: {
			     	idFilialF:    			{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	idFilialRHF:  			{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	descricaoFilialRHF:  	{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	idCargoF:  				{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	crachaF:  				{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	cpfFuncionarioF:  		{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
		     				                 cpfValido:    '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> CPF inválido.</span>'},
			     	nomeFuncionarioF:  		{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	dataSituacaoRHF:  		{dataValida:   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Data inválida.</span>'},
			     	idEmpresaRHF:  			{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	descricaoEmpresaRHF:  	{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	idCentroCustoF:  		{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	descricaoCentroCustoF:  {required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	idFuncionarioSuperiorF: {funcionarioNaoExistex: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Funcionário não encontrado.</span>'},
			     	idFuncionarioAvaliadorF:{funcionarioNaoExistex: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Funcionário não encontrado.</span>'},
			     	dataAdmissaoF:  		{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
					  			     	     dataValida:   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Data inválida.</span>'},
			     	dataDemissaoF:  		{dataValida:   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Data inválida.</span>'}
		         },
	         submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
		       	document.detMeta.action = "funcionario.do?operacao=alteraFuncionario";   
	            document.detMeta.submit();
	         }
    	});
    	
		//Filtro
		$("#idFuncionarioFiltro").val(idFuncionarioF);
		$("#nomeFuncionarioFiltro").val(nomeFuncionarioF);
		$("#crachaFiltro").val(crachaF);
		$("#cpfFuncionarioFiltro").val(cpfFuncionarioF);
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 580px">
	<form name="detMeta" id="detMeta" method="post"> 

		<input type='hidden' id="funcionarioEncontrado"    	name="funcionarioEncontrado"    value=""/>

		<!--Chave-->
		<input type='hidden' id="idFuncionarioF"    name="idFuncionarioF"   value=""/>
		<input type='hidden' id="idEmpresaF"  		name="idEmpresaF"    	value=""/>

		<!--Filtro-->
		<input type='hidden' id="idFuncionarioFiltro"    	name="idFuncionarioFI"     	value=""/>
		<input type='hidden' id="nomeFuncionarioFiltro"  	name="nomeFuncionarioFI"    value=""/>
		<input type='hidden' id="crachaFiltro"    			name="crachaFI"     		value=""/>
		<input type='hidden' id="cpfFuncionarioFiltro"  	name="cpfFuncionarioFI"   	value=""/>
		
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" >
						<label class="label">Matrícula:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<div id="idFuncionarioDIV"></div>
					</td>
				</tr>				
				<tr>
					<td class="componente" >
						<label class="label">Filial:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idFilialF" name="idFilialF" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>				
				<tr>
					<td class="componente" >
						<label class="label">Código Filial RH:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="idFilialRHF" name="idFilialRHF" value="" maxlength="10" size="10"  />
					</td>
				</tr>					
				<tr>
					<td class="componente" >
						<label class="label">Descrição Filial RH:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="descricaoFilialRHF" name="descricaoFilialRHF" value="" maxlength="50" size="50"  />
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Cargo:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idCargoF" name="idCargoF" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Crachá:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="crachaF" name="crachaF" value="" maxlength="20" size="20"  />
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">CPF Funcionário:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="cpfFuncionarioF" name="cpfFuncionarioF" value="" maxlength="14" size="13"  />
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Nome Funcionário:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="nomeFuncionarioF" name="nomeFuncionarioF" value="" maxlength="60" size="50"  />
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Código Situação RH:</label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="idSituacaoRHF" name="idSituacaoRHF" value="" maxlength="5" size="5"  />
					</td>
				</tr>									
				
													
				<tr>
					<td class="componente" >
						<label class="label">Descrição Situação RH:</label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="descricaoSituacaoRHF" name="descricaoSituacaoRHF" value="" maxlength="50" size="50"  />
					</td>
				</tr>									
				
													
				<tr>
					<td class="componente" >
						<label class="label">Data Situação RH:</label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="dataSituacaoRHF" name="dataSituacaoRHF" value="" maxlength="12" size="10"  />
					</td>
				</tr>									
				
													
				<tr>
					<td class="componente" >
						<label class="label">Código Empresa RH:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="idEmpresaRHF" name="idEmpresaRHF" value="" maxlength="5" size="5"  />
					</td>
				</tr>									
				<tr>
					<td class="componente" >
						<label class="label">Descrição Empresa RH:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="descricaoEmpresaRHF" name="descricaoEmpresaRHF" value="" maxlength="60" size="50"  />
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Código Centro Custo:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="idCentroCustoF" name="idCentroCustoF" value="" maxlength="40" size="40"  />
					</td>
				</tr>									
				<tr>
					<td class="componente" >
						<label class="label">Descrição Centro Custo:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="descricaoCentroCustoF" name="descricaoCentroCustoF" value="" maxlength="40" size="40"  />
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Matrícula Superior:</label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="idFuncionarioSuperiorF" name="idFuncionarioSuperiorF" value="" maxlength="11" size="10"  />
					</td>
				</tr>																
				<tr>
					<td class="componente" >
						<label class="label">Matrícula Avaliador:</label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="idFuncionarioAvaliadorF" name="idFuncionarioAvaliadorF" value="" maxlength="11" size="10"  />
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Data Admissão:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="dataAdmissaoF" name="dataAdmissaoF" value="" maxlength="12" size="10"  />
					</td>
				</tr>									
				<tr>
					<td class="componente" >
						<label class="label">Data Demissão:</label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="dataDemissaoF" name="dataDemissaoF" value="" maxlength="12" size="10"  />
					</td>
				</tr>									
				
				<tr>
					<td class="componente" >
						<label class="label">Código Situação Anterior RH:</label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="idSituacaoAnteriorRHF" name="idSituacaoAnteriorRHF" value="" maxlength="5" size="5"  />
					</td>
				</tr>
				
				<tr>
					<td class="componente" >
						<label class="label">Descrição Situação Anterior RH:</label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="descricaoSituacaoAnteriorRHF" name="descricaoSituacaoAnteriorRHF" value="" maxlength="50" size="50"  />
					</td>
				</tr>
				
				<tr>
					<td class="componente" >
						<label class="label">Data Início Situação Anterior RH:</label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="dataInicioSituacaoAnteriorRHF" name="dataInicioSituacaoAnteriorRHF" value="" maxlength="12" size="10"  />
					</td>
				</tr>
				
				<tr>
					<td class="componente" >
						<label class="label">Quantidade de Dias Trabalhados no Mês:</label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="idQtdDiasF" name="idQtdDiasF" value="" maxlength="5" size="5"  />
					</td>
				</tr>
				
								<tr>
					<td class="componente" >
						<label class="label">Data da promoção ao Elegivel:</label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="idPromocaoElegivel" name="idPromocaoElegivel" value="" maxlength="10" size="10"  />
					</td>
				</tr>


				<tr>
					<td class="componente" >
						<label class="label">Código do Motivo da demissão:</label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="idMotivoDemissao" name="idMotivoDemissao" value="" maxlength="5" size="5"  />
					</td>
				</tr>


				<tr>
					<td class="componente" >
						<label class="label">Quantidade de dias afastamento:</label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="idQuantidadeAfast" name="idQuantidadeAfast" value="" maxlength="5" size="5"  />
					</td>
				</tr>

				<tr>
					<td class="componente" >
						<label class="label">Centro de custo Vendas/Comercial:</label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="idCentroCusto" name="idCentroCusto" value="" maxlength="5" size="5"  />
					</td>
				</tr>
													
			</tbody>
		</table>
		<br/>
		<div id="botao-geral" style="width:100%;">
			<div id="botao" class="barBottom" style="width:100%;"> 			
				<div class="div-botao" style="float:right;width:100%;">
					<input id="botaoSalvar" type="submit" class="button" value="Salvar" />
					<input id="botaoCancelar" type="button" class="button" value="Cancelar" />					
				</div>
			</div>
		</div>
	</form>
</div>