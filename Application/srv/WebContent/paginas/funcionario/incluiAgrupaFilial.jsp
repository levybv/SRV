<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type='text/javascript' src='srvdwr/interface/CalendarioComercialBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/FilialBusinessAjax.js'></script>
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

		CalendarioComercialBusinessAjax.obtemCalendarioComercialListaMes( 
			function (periodos) {
				if (periodos != null) {
					var options = $('#idMesAF').attr('options');
					for (var i=0; i<periodos.length; i++) {
						var periodoVO = periodos[i];
						options[options.length] = new Option(periodoVO.mesStr, periodoVO.mes, false, false);
					}
				}
		});

		CalendarioComercialBusinessAjax.obtemCalendarioComercialListaAno( 
			function (periodos) {
				if (periodos != null) {
					var options = $('#idAnoAF').attr('options');
					for (var i=0; i<periodos.length; i++) {
						var periodoVO = periodos[i];
						options[options.length] = new Option(periodoVO.ano, periodoVO.ano, false, false);
					}
				}
		});

		FilialBusinessAjax.obtemTodasFiliaisAtivas( 
			function (filiais) {
				if (filiais != null) {
					var options = $('#idFilialAF').attr('options');
					for (var i=0; i<filiais.length; i++) {
						var filialVO = filiais[i];
						options[options.length] = new Option(filialVO.descricao, filialVO.codFilial, false, false);
					}
				}
		});		
		
		$("#idFuncionarioAF").numeric();
		
		jQuery.validator.addMethod('funcionarioExistex', function(value, element, params) {
			dwr.engine.setAsync(false);
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
		
		$("#detMeta").validate({
	    	rules: {
	    			idMesAF:	   			{required:true},
	    			idAnoAF:	   			{required:true},
	    			idFilialAF: 			{required:true},
	    			idFuncionarioAF: 		{required:true, funcionarioExistex:true}
		     	},
		     	messages: {
			     	idMesAF:	    		{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	idAnoAF:	    		{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	idFilialAF:  			{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	idFuncionarioAF:  		{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
		     				       funcionarioExistex:     '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Funcionário não encontrado.</span>'}
		         },
	         submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
		       	document.detMeta.action = "agrupaFilial.do?operacao=incluiAgrupamentoFilial";   
	            document.detMeta.submit();
	         }
    	});
    	
		$("#idFilialFiltro").val(idFilialF);
		$("#idFuncionarioFiltro").val(idFuncionarioF);
		$("#nomeFuncionarioFiltro").val(nomeFuncionarioF);
		$("#anoFiltro").val(anoF);
		$("#mesFiltro").val(mesF);
		    	
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 580px">
	<form name="detMeta" id="detMeta" method="post"> 
			
		<input type='hidden' id="funcionarioEncontrado" name="funcionarioEncontrado" value=""/>
			
		<!--Filtro-->
		<input type='hidden' id="idFilialFiltro"    		name="idFilialF"     			value=""/>
		<input type='hidden' id="idFuncionarioFiltro"    	name="idFuncionarioF"     		value=""/>
		<input type='hidden' id="nomeFuncionarioFiltro"  	name="nomeFuncionarioF"     	value=""/>
		<input type='hidden' id="anoFiltro"    				name="anoF"     				value=""/>
		<input type='hidden' id="mesFiltro"  				name="mesF"   					value=""/>
		
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width="20%">
						<label class="label">M&ecirc;s:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idMesAF" name="idMesAF" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" width="20%">
						<label class="label">Ano:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idAnoAF" name="idAnoAF" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Filial:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idFilialAF" name="idFilialAF" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>				
				<tr>
					<td class="componente" >
						<label class="label">Matrícula:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="idFuncionarioAF" name="idFuncionarioAF" value="" maxlength="11" size="10"  />
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