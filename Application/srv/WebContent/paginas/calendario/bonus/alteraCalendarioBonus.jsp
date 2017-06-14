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

		$("#dataFinalA").mask("99/99/9999");
		$("#dataInicialA").mask("99/99/9999");

		jQuery.validator.addMethod('intervaloValido', function(value, element, params) {
			dwr.engine.setAsync(false);
			var valida = false;
			CalendarioBonusBusinessAjax.existeIntervaloCalendarioBonus(value, $("#anoA").val(), $("#mesA").val(), function (jaCadastrado) {
				valida = jaCadastrado;
			});
			return valida;
		});

		CalendarioBonusBusinessAjax.obtemCalendarioBonus(mesBonus, anoBonus,
			function (returnVO) {
				if (returnVO != null) {
					$("#anoDIV").html(returnVO.ano);
					$("#anoA").val(returnVO.ano);
					$("#mesDIV").html(returnVO.mes);
					$("#mesA").val(returnVO.mes);
					$("#mesDescrA").val(returnVO.descricaoMes);
					$("#dataFinalA").val(returnVO.dataFinalStr);
					$("#dataInicialA").val(returnVO.dataInicialStr);
				}
		});

		$("#detMeta").validate({
	    	 rules: {
	    		 dataFinalA: {	required:true},
	    			 			//intervaloValido:true},
	    		 dataInicialA: {required:true},
	    			 			//intervaloValido:true},
	    		 mesDescrA: {required:true}
	    	 },
		     messages: {
		    	 dataFinalA: {	required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    		 			//intervaloValido: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Intervalo de data já cadastrado! </span>'},
		    	 dataInicialA: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    		 			//intervaloValido: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Intervalo de data já cadastrado! </span>'},
		    	 mesDescrA: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
		     },
	         submitHandler : function(form){
			        $("#div-botao").hide();  
		           	$("#div-load").show(); 
			       	document.detMeta.action = "calendarioBonus.do?operacao=alteraCalendarioBonus";   
		            document.detMeta.submit();
	         }
    	});

		$("#anoFiltro").val(anoFiltro);
		$("#mesFiltro").val(mesFiltro);
		$("#mesDescrFiltro").val(mesDescrFiltro);
		$("#dataInicialFiltro").val(dataInicialFiltro);
		$("#dataFinalFiltro").val(dataFinalFiltro);

		$("#mesA").focus();

	});

</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 575px;">
	<form name="detMeta" id="detMeta" method="post">

		<input type="hidden" id="anoFiltro" name="anoFiltro" value=""/>
		<input type="hidden" id="mesFiltro" name="mesFiltro" value=""/>
		<input type="hidden" id="mesDescrFiltro" name="mesDescrFiltro" value=""/>
		<input type="hidden" id="dataInicialFiltro" name="dataInicialFiltro" value=""/>
		<input type="hidden" id="dataFinalFiltro" name="dataFinalFiltro" value=""/>
		
		<input type="hidden" id="mesA" name="mesA" value=""/>
		<input type="hidden" id="anoA" name="anoA" value=""/>

		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente"  width="20%">
						<label class="label">Ano:</label> 
					</td>
					<td class="componente" >
						<div id="anoDIV"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Mês:</label> 
					</td>
					<td class="componente" >
						<div id="mesDIV"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Descrição Mês:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<input type="text" id="mesDescrA" name="mesDescrA" value="" maxlength="20" size="30" class="campo2" />
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Data Inicial:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<input type="text" id="dataInicialA" name="dataInicialA" value="" maxlength="10" size="12" class="campo2" />
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Data Final:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<input type="text" id="dataFinalA" name="dataFinalA" value="" maxlength="10" size="12" class="campo2" />
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