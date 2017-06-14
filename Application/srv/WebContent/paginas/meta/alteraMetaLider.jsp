<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   

<script type='text/javascript' src='srvdwr/interface/MetaLiderBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>	
	
<script type="text/javascript" src="js/lib/jquery-1.4.2.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.alphanumeric.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.hotkeys-0.7.9.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.maskedinput-1.2.2.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.maskMoney.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.validate.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.tipsy.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.metadata.js"></script>
<script type="text/javascript" src="js/lib/jquery.hoverIntent.js"></script>



<script type="text/javascript" src="js/ns/neuro.1.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/ns/neuro.validate.1.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/ns/neuro.modal-1.0.js"  charset="UTF8" ></script>

<script type="text/javascript">
	dwr.engine.setAsync(false);
	$("#valorMetaAlteracao").maskMoney({decimal:",", thousands:".", allowNegative:true});
	//var i=0;
	$('#mesesAlteracao').attr('disabled', 'disabled');
	$('#idLideresAlteracao').attr('disabled', 'disabled');
	$('#idEquipeAlteracao').attr('disabled', 'disabled');
	$('#idIndicadorAlteracao').attr('disabled', 'disabled');
	$(document).ready(function() {
		$(document).bind('keydown', 'esc', function () {
			//fecharModal();
		});
		
		$("#botaoCancelar").click(function(){
            document.detMeta.submit();
            fecharModal();
		});
		MetaLiderBusinessAjax.obtemLideres(
				function (lideres) {
					if (lideres != null) {
						var options = $('#idLideresAlteracao').attr('options');
						for (var i=0; i<lideres.length; i++) {
							var funcionarioVO = lideres[i];
							options[options.length] = new Option(funcionarioVO.nomeFuncionario,funcionarioVO.idFuncionario, false, false);
						}
					}
			});	
			MetaLiderBusinessAjax.obtemListaEquipes(
					function (equipes) {
						if (equipes != null) {
							var options = $('#idEquipeAlteracao').attr('options');
							for (var i=0; i<equipes.length; i++) {
								options[options.length] = new Option(equipes[i],equipes[i], false, false);
							}
						}
			});	
			MetaLiderBusinessAjax.obtemListaIndicador(
					function (listaDadosIndicadorVO) {
						if (listaDadosIndicadorVO != null) {
							var options = $('#idIndicadorAlteracao').attr('options');
							for (var i=0; i<listaDadosIndicadorVO.length; i++) {
								options[options.length] = new Option(listaDadosIndicadorVO[i].descricaoIndicador,listaDadosIndicadorVO[i].idIndicador, false, false);
							}
						}
			});	
			
			MetaLiderBusinessAjax.permiteAlteracaoMesAnterior(
					function (isPermitido) {
						var mesPermissaoAlteracao = isPermitido?1:0;
						
						var meses = new Array("Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro");

						$('#mesesAlteracao').append($('<option>', { 
						    value: "",
						    text :'Selecione...'
						}));


						for(var i=mesPermissaoAlteracao;i>0;i--){
							var date = new Date();
							date.setMonth(date.getMonth()-i);
							 $('#mesesAlteracao').append($('<option>', { 
							        value: date.getYear()+""+date.getMonth(),
							        text :  meses[date.getMonth()]+"/"+date.getYear()
							    }));
						}



						for(var i=0;i<5;i++){
							var date = new Date();
							date.setMonth(date.getMonth()+i);
							 $('#mesesAlteracao').append($('<option>', { 
							        value: date.getYear()+""+date.getMonth(),
							        text : meses[date.getMonth()]+"/"+date.getYear()
							    }));
						}
						
				});
			
			jQuery.validator.addMethod("validaMetaLider", function(value, element) {
				var resp = true;
				MetaLiderBusinessAjax.validaMetaLider($('#mesesAlteracao').val(),document.getElementById('idLideresAlteracao').value,document.getElementById('idEquipeAlteracao').value,document.getElementById('idIndicadorAlteracao').value,
						function (retorno) {
					resp = retorno;
				});
				return resp;
			});
			
			jQuery.validator.addMethod("validaDuplicidade", function(value, element) {
				var resp = true;
				MetaLiderBusinessAjax.validaDuplicidade($('#mesesAlteracao').val(),document.getElementById('idLideresAlteracao').value,document.getElementById('idEquipeAlteracao').value,document.getElementById('idIndicadorAlteracao').value,
						function (retorno) {
					resp = retorno;
				});
				return resp;
			});
			
			
			$("#valorMetaMF").keypress(function(){
				return mascara(this, moedaSemRS);
			});	
			$("#valorPremioFilialMF").keypress(function(){
				return mascara(this, moedaSemRS);
			});			
			
			$("#detMeta").validate({
				rules: {
					mesesAlteracao:	   	{required:true,validaDuplicidade:true},
					idLideresAlteracao:	{required:true},
					idIndicadorAlteracao:		{required:true},
					idEquipeAlteracao:   {required:true,validaMetaLider:true},
					valorMetaAlteracao:  {required:true}
			},
	     	messages: {
	     		mesesAlteracao:  	{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
	     				             validaDuplicidade:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Já foi salvo para essa data essa meta com esse lider e equipe</span>'},
	     		idLideresAlteracao:  {required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
	     		idIndicadorAlteracao: 		{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
	     		idEquipeAlteracao:  	{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
	     						     validaMetaLider:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Uma outra equipe já foi utilizada por esse lider nessa data.</span>'},
	     		valorMetaAlteracao: 	{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
	     		
			},
	        submitHandler : function(form) {
	        	$("#div-botaoI").hide();  
	           	document.detMeta.action = "metaLider.do?operacao=altualiza";  
	           	$('#mesesAlteracao').removeAttr('disabled');
	        	$('#idLideresAlteracao').removeAttr('disabled');
	        	$('#idEquipeAlteracao').removeAttr('disabled');
	        	$('#idIndicadorAlteracao').removeAttr('disabled');
	        	$('#botaoSalvar').attr('disabled', 'disabled');
	        	$('#botaoCancelar').attr('disabled', 'disabled');
	            document.detMeta.submit();
			}
		    });
			var stringDadosSelecionados = $('input[name=checkbox1]:checked', '#form').val();
			var dadoSelecionados= stringDadosSelecionados.split(';');
			$("#dadosAnteriores").val(stringDadosSelecionados);
			
			
			var alteraMes = dadoSelecionados[3];
			var aleteraAno = dadoSelecionados[2];
			$("#mesesAlteracao").val(aleteraAno+''+(parseInt(alteraMes,10)-1)).attr('selected',true);
			$("#idIndicadorAlteracao").val(dadoSelecionados[1]).attr('selected',true);
			$("#idEquipeAlteracao").val(dadoSelecionados[4]).attr('selected',true);
			$("#idLideresAlteracao").val(dadoSelecionados[0]).attr('selected',true);
			$("#valorMetaAlteracao").val(dadoSelecionados[5]);
			// seta valores dos combos
			//${listaMetas.codFuncionario};${listaMetas.codIndicador};${listaMetas.ano};${listaMetas.mes}
	});
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 0px; width:100%;">
	<form name="detMeta" id="detMeta" method="post" action="metaLider.do?operacao=pesquisaRetorno">
		<input type="hidden" value='' name='dadosAnteriores' id='dadosAnteriores'> 
			<table class="tabelaComponente" width="100%">
				<tbody> 
					<tr>
						<td class="componente" width="5%">
							<label class="label">Mês/Ano:<span class="requerido">*</span></label> 
						</td>
						<td class="componente" width="20%" style="text-align: left">
							<select id="mesesAlteracao" name="mesesAlteracao" class="campo"/>
						</td>
						<td class="componente" width="5%">
							<label class="label">Líder:<span class="requerido">*</span></label> 
						</td>
						<td class="componente" width="20%" colspan=3>
							<select id="idLideresAlteracao" name="idLideresAlteracao" class="campo">
                              	<option value="" selected>Selecione...</option>
                            </select>
						</td>
						
					</tr>					
					<tr>
						<td class="componente" width="5%">
							<label class="label">Indicador:<span class="requerido">*</span></label> 
						</td>
						<td class="componente" width="20%">
							<select id="idIndicadorAlteracao" name="idIndicadorAlteracao" class="campo">
                              	<option value="" selected>Selecione...</option>
                            </select>
						</td>
						<td class="componente" width="5%">
							<label class="label">Equipe:<span class="requerido">*</span></label> 
						</td>
						<td class="componente" width="20%">
							<select id="idEquipeAlteracao" name="idEquipeAlteracao" class="campo">
								<option value="" selected>Selecione...</option>
							</select>
						</td>
						<td class="componente" width="5%" >
							<label class="label">Meta:<span class="requerido">*</span></label> 
						</td>
						<td class="componente" width="20%" style="text-align: left">
	  						<input type="text" class="campo2" id="valorMetaAlteracao" name="valorMetaAlteracao" value="" maxlength="14" size="15"  />
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
