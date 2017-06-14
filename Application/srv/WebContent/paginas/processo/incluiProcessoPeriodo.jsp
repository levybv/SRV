<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type='text/javascript' src='srvdwr/interface/ProcessoPeriodoBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/ProcessoBusinessAjax.js'></script>
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
		
		ProcessoBusinessAjax.obtemProcessosAtivos( 
			function (processos) {
				if (processos != null) {
					var options = $('#idProcessoPP').attr('options');
					for (var i=0; i<processos.length; i++) {
						var processoVO = processos[i];
						options[options.length] = new Option(processoVO.descricao, processoVO.idProcesso, false, false);
					}
				}
		});
		
		$('#anoPP').numeric();
		
		jQuery.validator.addMethod('periodoJaExiste', function(value, element, params) {
			
			//Chave	    	
    		var mes = $("#mesPP").val();
    		var ano = $("#anoPP").val();
    		var codigoProcesso = $("#idProcessoPP").val();
    		
    		if (mes != null && mes != "" && ano != "" && ano != null) {
			
				dwr.engine.setAsync(false);
				ProcessoPeriodoBusinessAjax.processoPorPeriodoExiste(ano, mes, codigoProcesso,
					function (processoPeriodoEncontrado) {
						if (processoPeriodoEncontrado) {
							$("#processoPeriodoEncontrado").val("S");
							return false;
						} else {
							$("#processoPeriodoEncontrado").val("N");
							return true;
						} 
						
				});
			} else {
				return true;
			}
			return !($("#processoPeriodoEncontrado").val() == "S");
		});			
		
		$("#detMeta").validate({
	    	 rules: {
    			mesPP:   	   {required:true},
    			anoPP:     	   {required:true},
    			idProcessoPP:  {required:true, periodoJaExiste:true}
	     	 },
	     	 messages: {
		     	mesPP:    	    {required:	       '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     	anoPP:	 	    {required:	       '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     	idProcessoPP:	{required:	       '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
		     	                 periodoJaExiste:  '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Período já existe.</span>'}
	         },
	         submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
		       	document.detMeta.action = "processoPeriodo.do?operacao=incluiProcessosPeriodo";   
	            document.detMeta.submit();
	         }
    	});
    	
		$("#anoFiltro").val(anoFiltro);
		$("#mesFiltro").val(mesFiltro);
		    	
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 670px">
	<form name="detMeta" id="detMeta" method="post"> 
			
		<input type='hidden' id="processoPeriodoEncontrado" name="processoPeriodoEncontrado" value=""/>
			
			<!--Filtro-->
			<input type='hidden' id="anoFiltro" name="anoFiltro" value=""/>
			<input type='hidden' id="mesFiltro" name="mesFiltro" value=""/>
		
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width="12%">
						<label class="label">Mês:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" width="88%">
						<select id="mesPP" name="mesPP" class="campo">
                        	<option value="" selected>[SELECIONE]</option>
                    		<option value="1">Janeiro</option>
                    		<option value="2">Fevereiro</option>
							<option value="3">Março</option>
                    		<option value="4">Abril</option>
                    		<option value="5">Maio</option>
                    		<option value="6">Junho</option>
                    		<option value="7">Julho</option>
                    		<option value="8">Agosto</option>
							<option value="9">Setembro</option>
                    		<option value="10">Outubro</option>
                    		<option value="11">Novembro</option>
                    		<option value="12">Dezembro</option>
                        </select>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Ano:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
                    	<input id="anoPP" name="anoPP" value="" class="campo2" maxlength="4" style="width: 60px;" value="" type="text"/>
					</td>
				</tr>
				<tr>
					<td class="componente">
						<label class="label">Processo:<span class="requerido">*</span></label> 
					</td>
					<td class="componente">
						<select id="idProcessoPP" name="idProcessoPP" class="campo">
                        	<option value="-1" selected>Todos</option>
                        </select>
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