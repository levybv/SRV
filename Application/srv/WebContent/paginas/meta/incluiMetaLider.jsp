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
	$("#valorMetaInclusao").maskMoney({decimal:",", thousands:".", allowNegative:true});
	
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
					var options = $('#idLideresInclusao').attr('options');
					for (var i=0; i<lideres.length; i++) {
						var funcionarioVO = lideres[i];
						options[options.length] = new Option(funcionarioVO.nomeFuncionario,funcionarioVO.idFuncionario, false, false);
					}
				}
		});	
		MetaLiderBusinessAjax.obtemListaEquipes(
				function (equipes) {
					if (equipes != null) {
						var options = $('#idEquipeInclusao').attr('options');
						for (var i=0; i<equipes.length; i++) {
							options[options.length] = new Option(equipes[i],equipes[i], false, false);
						}
					}
		});	
		MetaLiderBusinessAjax.obtemListaIndicador(
				function (listaDadosIndicadorVO) {
					if (listaDadosIndicadorVO != null) {
						var options = $('#idIndicador').attr('options');
						for (var i=0; i<listaDadosIndicadorVO.length; i++) {
							options[options.length] = new Option(listaDadosIndicadorVO[i].descricaoIndicador,listaDadosIndicadorVO[i].idIndicador, false, false);
						}
					}
		});	
		
		MetaLiderBusinessAjax.permiteAlteracaoMesAnterior(
				function (isPermitido) {
					var mesPermissaoAlteracao = isPermitido?1:0;
					
					var meses = new Array("Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro");

					$('#mesesInclusao').append($('<option>', { 
					    value: "",
					    text :'Selecione...'
					}));


					for(var i=mesPermissaoAlteracao;i>0;i--){
						var date = new Date();
						date.setMonth(date.getMonth()-i);
						 $('#mesesInclusao').append($('<option>', { 
						        value: date.getYear()+""+date.getMonth(),
						        text :  meses[date.getMonth()]+"/"+date.getYear()
						    }));

						 if(mesSelecionado == date.getYear()+""+date.getMonth()){
							 $("#mesesInclusao").val(date.getYear()+""+date.getMonth()).attr('selected',true);
						 }
					}



					for(var i=0;i<6;i++){
						var date = new Date();
						date.setMonth(date.getMonth()+i);
						 $('#mesesInclusao').append($('<option>', { 
						        value: date.getYear()+""+date.getMonth(),
						        text : meses[date.getMonth()]+"/"+date.getYear()
						    }));
					}
					
			});
		
		jQuery.validator.addMethod("validaMetaLider", function(value, element) {
			var resp = true;
			MetaLiderBusinessAjax.validaMetaLider($('#mesesInclusao').val(),document.getElementById('idLideresInclusao').value,document.getElementById('idEquipeInclusao').value,document.getElementById('idIndicador').value,
					function (retorno) {
				resp = retorno;
			});
			return resp;
		});
		
		jQuery.validator.addMethod("validaDuplicidade", function(value, element) {
			var resp = true;
			MetaLiderBusinessAjax.validaDuplicidade($('#mesesInclusao').val(),document.getElementById('idLideresInclusao').value,document.getElementById('idEquipeInclusao').value,document.getElementById('idIndicador').value,
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
				mesesInclusao:	   	{required:true,validaDuplicidade:true},
				idLideresInclusao:	{required:true},
				idIndicador:		{required:true},
				idEquipeInclusao:   {required:true,validaMetaLider:true},
				valorMetaInclusao:  {required:true}
		},
     	messages: {
     		mesesInclusao:  	{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
     				             validaDuplicidade:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Já foi salvo para essa data essa meta com esse lider e equipe</span>'},
     		idLideresInclusao:  {required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
     		idIndicador: 		{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
     		idEquipeInclusao:  	{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
     						     validaMetaLider:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Uma outra equipe já foi utilizada por esse lider nessa data.</span>'},
     		valorMetaInclusao: 	{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
     		
		},
        submitHandler : function(form) {
        	$("#div-botaoI").hide();  
           	document.detMeta.action = "metaLider.do?operacao=inclui";  
            document.detMeta.submit();
		}
	    });
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 0px; width:100%;">
	<form name="detMeta" id="detMeta" method="post" action="metaLider.do?operacao=pesquisaRetorno"> 
			<table class="tabelaComponente" width="100%">
				<tbody> 
					<tr>
						<td class="componente" width="5%">
							<label class="label">Mês/Ano:<span class="requerido">*</span></label> 
						</td>
						<td class="componente" width="20%" style="text-align: left">
							<select id="mesesInclusao" name="mesesInclusao" class="campo"/>
						</td>
						<td class="componente" width="5%">
							<label class="label">Líder:<span class="requerido">*</span></label> 
						</td>
						<td class="componente" width="20%" colspan=3>
							<select id="idLideresInclusao" name="idLideresInclusao" class="campo">
                              	<option value="" selected>Selecione...</option>
                            </select>
						</td>
						
					</tr>					
					<tr>
						<td class="componente" width="5%">
							<label class="label">Indicador:<span class="requerido">*</span></label> 
						</td>
						<td class="componente" width="20%">
							<select id="idIndicador" name="idIndicador" class="campo">
                              	<option value="" selected>Selecione...</option>
                            </select>
						</td>
						<td class="componente" width="5%">
							<label class="label">Equipe:<span class="requerido">*</span></label> 
						</td>
						<td class="componente" width="20%">
							<select id="idEquipeInclusao" name="idEquipeInclusao" class="campo">
								<option value="" selected>Selecione...</option>
							</select>
						</td>
						<td class="componente" width="5%" >
							<label class="label">Meta:<span class="requerido">*</span></label> 
						</td>
						<td class="componente" width="20%" style="text-align: left">
	  						<input type="text" class="campo2" id="valorMetaInclusao" name="valorMetaInclusao" value="" maxlength="14" size="15"  />
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
