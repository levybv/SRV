<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   

<script type='text/javascript' src='srvdwr/interface/CalendarioComercialBusinessAjax.js'></script>
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

		$("#dataFinalI").mask("99/99/9999");
		$("#dataInicialI").mask("99/99/9999");
		$("#idPeriodoI").numeric();
		$("#anoI").numeric();

		jQuery.validator.addMethod('periodoJaCadastrado', function(value, element, params) {
			dwr.engine.setAsync(false);
			var validaPeriodo = false;
			CalendarioComercialBusinessAjax.validaCalendarioComercial($("#idPeriodoI").val(), $("#anoI").val(),function (jaCadastrado) {
				validaPeriodo = jaCadastrado;
			});
			return validaPeriodo;
		});

		CalendarioComercialBusinessAjax.obtemCalendarioComercialListaMes(function (listaMes) {
			if (listaMes != null) {
				var options = $('#mesI').attr('options');
				for (var i=0; i<listaMes.length; i++) {
					var itemVO = listaMes[i];
					options[options.length] = new Option(itemVO.mesStr, itemVO.mes, false, false);
				}
			}
		});

		$("#detMeta").validate({
	    	 rules: {
	    		 idPeriodoI: {required:true},
	    		 anoI: {required:true,
	    			 	periodoJaCadastrado: true},
	    		 dataFinalI: {required:true},
	    		 dataInicialI: {required:true},
	    		 mesI: {required:true}
	    	 },
		     messages: {
		    	 idPeriodoI: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 anoI: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
				 		periodoJaCadastrado: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Ano / Período já cadastrado !</span>'},
		    	 dataFinalI: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 dataInicialI: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 mesI: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
		     },
	         submitHandler : function(form){
			        $("#div-botao").hide();  
		           	$("#div-load").show(); 
			       	document.detMeta.action = "calendarioComercial.do?operacao=incluiCalendarioComercial";   
		            document.detMeta.submit();
	         }
    	});

		$("#anoSelecionadoFiltro").val(anoSelecionadoFiltro);
		$("#idPeriodoI").focus();

	});

</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 575px;">
	<form name="detMeta" id="detMeta" method="post">
		<input type="hidden" id="anoSelecionadoFiltro" name="anoSelecionadoFiltro" value=""/>
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente"  width="20%">
						<label class="label">Período:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" id="idPeriodoI" name="idPeriodoI" value="" maxlength="2" size="4" class="campo2" />
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Ano:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<input type="text" id="anoI" name="anoI" value="" maxlength="4" size="6" class="campo2" />
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Mês de Referência:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="mesI" name="mesI" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Data Inicial:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<input type="text" id="dataInicialI" name="dataInicialI" value="" maxlength="10" size="12" class="campo2" />
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Data Final:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<input type="text" id="dataFinalI" name="dataFinalI" value="" maxlength="10" size="12" class="campo2" />
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