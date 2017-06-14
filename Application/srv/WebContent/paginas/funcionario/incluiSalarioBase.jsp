<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type='text/javascript' src='srvdwr/interface/CalendarioComercialBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/FuncionarioBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/SalarioBaseBusinessAjax.js'></script>
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

		CalendarioComercialBusinessAjax.obtemListaPeriodos( 
			function (periodos) {
				if (periodos != null) {
					var options = $('#idPeriodoSB').attr('options');
					for (var i=0; i<periodos.length; i++) {
						var periodoVO = periodos[i];
						options[options.length] = new Option(periodoVO.dataFormatada, periodoVO.ano + "-" + periodoVO.mes, false, false);
					}
				}
		});

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
		
		jQuery.validator.addMethod('salarioBaseNaoExiste', function(value, element, params) {
		
			if ($("#idPeriodoSB").val() == "" || $("#idFuncionarioSB").val() == "") {
				return true;
			}
			dwr.engine.setAsync(false);
			//SalarioBaseBusinessAjax.salarioBaseExiste("xxx", 111,
			SalarioBaseBusinessAjax.salarioBaseExiste($("#idPeriodoSB").val(), parseFloat($("#idFuncionarioSB").val()),
				function (salarioBaseEncontrado) {
					if (salarioBaseEncontrado) {
						$("#salarioBaseEncontrado").val("S");
						return true;
					} else {
						$("#salarioBaseEncontrado").val("N");
						return false;
					} 
			});
			return ($("#salarioBaseEncontrado").val() != "S");
		});			
		
		jQuery.validator.addMethod('salarioBaseZerado', function(value, element, params) {
			if (value == "0" || value == "0,00" || value == "R$ 0,00") {
				return false;
			}
			return true;
		});

		$("#idFuncionarioSB").numeric();
		$("#salarioBaseSB").keypress(function(){
			return mascara(this, moeda);
		});	
		
		$("#detMeta").validate({
	    	rules: {
	    			idPeriodoSB:   			{required:true, salarioBaseNaoExiste:true},
	    			idFuncionarioSB: 		{required:true, funcionarioExistex:true, salarioBaseNaoExiste:true},
	    			salarioBaseSB: 			{required:true, salarioBaseZerado:true}
		     	},
		     	messages: {
			     	idPeriodoSB:    		{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
		     				     salarioBaseNaoExiste:     '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Salário Base já existe.</span>'},
			     	idFuncionarioSB:  		{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
		     				       funcionarioExistex:     '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Funcionário não encontrado.</span>',
		     				     salarioBaseNaoExiste:     '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Salário Base já existe.</span>'},
			     	salarioBaseSB:  		{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
			     	                salarioBaseZerado:     '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Valor precisa ser superior a zero.</span>'}
		         },
	         submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
		       	document.detMeta.action = "salarioBase.do?operacao=incluiSalarioBase";   
	            document.detMeta.submit();
	         }
    	});
    	
		$("#idFuncionarioFiltro").val(idFuncionarioF);
		$("#nomeFuncionarioFiltro").val(nomeFuncionarioF);
		$("#anoFiltro").val(anoF);
		$("#mesFiltro").val(mesF);
		    	
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 450px">
	<form name="detMeta" id="detMeta" method="post"> 
			
		<input type='hidden' id="funcionarioEncontrado" name="funcionarioEncontrado" value=""/>
		<input type='hidden' id="salarioBaseEncontrado" name="salarioBaseEncontrado" value=""/>
			
		<!--Filtro-->
		<input type='hidden' id="idFuncionarioFiltro"    	name="idFuncionarioF"     		value=""/>
		<input type='hidden' id="nomeFuncionarioFiltro"  	name="nomeFuncionarioF"     	value=""/>
		<input type='hidden' id="anoFiltro"    				name="anoF"     				value=""/>
		<input type='hidden' id="mesFiltro"  				name="mesF"   					value=""/>
		
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width="25%">
						<label class="label">Período:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idPeriodoSB" name="idPeriodoSB" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Matrícula:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="idFuncionarioSB" name="idFuncionarioSB" value="" maxlength="11" size="10"  />
					</td>
				</tr>					
				<tr>
					<td class="componente" >
						<label class="label">Salário Base:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="salarioBaseSB" name="salarioBaseSB" value="" maxlength="14" size="15"  />
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