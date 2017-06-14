<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   

<script type='text/javascript' src='srvdwr/interface/CalendarioBonusBusinessAjax.js'></script>
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
		$("#anoI").numeric();

		jQuery.validator.addMethod('periodoJaCadastrado', function(value, element, params) {
			dwr.engine.setAsync(false);
			var validaPeriodo = false;
			CalendarioBonusBusinessAjax.validaCalendarioBonus($("#mesI").val(), $("#anoI").val(),function (jaCadastrado) {
				validaPeriodo = jaCadastrado;
			});
			return validaPeriodo;
		});

		jQuery.validator.addMethod('intervaloValido', function(value, element, params) {
			dwr.engine.setAsync(false);
			var valida = false;
			CalendarioBonusBusinessAjax.existeIntervaloCalendarioBonus(value, null, null, function (jaCadastrado) {
				valida = jaCadastrado;
			});
			return valida;
		});

		$("#detMeta").validate({
	    	 rules: {
	    		 mesDescrI: {required:true},
	    		 anoI: {required:true},
	    		 dataFinalI: {	required:true},
	    			 			//intervaloValido:true},
	    		 dataInicialI: {required:true},
	    			 			//intervaloValido:true},
	    		 mesI: {required:true,
	    			 	periodoJaCadastrado: true}
	    	 },
		     messages: {
		    	 mesDescrI: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 anoI: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 dataFinalI: {	required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    		 			//intervaloValido: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Intervalo de data já cadastrado! </span>'},
		    	 dataInicialI: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    		 			//intervaloValido: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Intervalo de data já cadastrado! </span>'},
		    	 mesI: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
		    		 	periodoJaCadastrado: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Ano / Mês já cadastrado !</span>'}
		     },
	         submitHandler : function(form){
			        $("#div-botao").hide();  
		           	$("#div-load").show(); 
			       	document.detMeta.action = "calendarioBonus.do?operacao=incluiCalendarioBonus";   
		            document.detMeta.submit();
	         }
    	});

		$("#anoFiltro").val(anoFiltro);
		$("#mesFiltro").val(mesFiltro);
		$("#mesDescrFiltro").val(mesDescrFiltro);
		$("#dataInicialFiltro").val(dataInicialFiltro);
		$("#dataFinalFiltro").val(dataFinalFiltro);

		$("#anoI").focus();

	});

</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 575px;">
	<form name="detMeta" id="detMeta" method="post">

		<input type="hidden" id="anoFiltro" name="anoFiltro" value=""/>
		<input type="hidden" id="mesFiltro" name="mesFiltro" value=""/>
		<input type="hidden" id="mesDescrFiltro" name="mesDescrFiltro" value=""/>
		<input type="hidden" id="dataInicialFiltro" name="dataInicialFiltro" value=""/>
		<input type="hidden" id="dataFinalFiltro" name="dataFinalFiltro" value=""/>

		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente"  width="20%">
						<label class="label">Ano:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<input type="text" id="anoI" name="anoI" value="" maxlength="4" size="6" class="campo2" />
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Mês:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="mesI" name="mesI" class="campo">
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
						<label class="label">Descrição Mês:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<input type="text" id="mesDescrI" name="mesDescrI" value="" maxlength="20" size="30" class="campo2" />
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