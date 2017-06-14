<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type='text/javascript' src='srvdwr/util.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>	
<script type='text/javascript' src='srvdwr/interface/GerenteFilialBusinessAjax.js'></script>

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

		$("#detMeta").validate({
	    	 rules: {},
		     messages: {},
	         submitHandler : function(form){
			        $("#div-botao").hide();  
		           	$("#div-load").show();
			       	document.detMeta.action = "gerenteFilial.do?operacao=excluiGerenteFilial";
		            document.detMeta.submit();
	         }
    	});

		GerenteFilialBusinessAjax.obtemGerenteLoja(idFuncE, idFilialE,
			function (gerenteVO) {
				if (gerenteVO != null) {
					$("#idFuncE").val(gerenteVO.idFuncionario);
					$("#idFilialE").val(gerenteVO.filial.codFilial);
					$("#divMatriculaE").html(gerenteVO.idFuncionario);
					$("#divNomeE").html(gerenteVO.nomeFuncionario);
					$("#divFilialE").html(gerenteVO.filial.descricao);
					$("#divAtuacaoE").html((gerenteVO.codAtuacao!=null&&gerenteVO.codAtuacao==1)?"Definitiva":((gerenteVO.codAtuacao!=null&&gerenteVO.codAtuacao==2)?"Provisória":"-"));
					$("#dtInclusaoE").html(gerenteVO.dataInclusaoFilialFmt);
				}
		});

    });

</script>

<div id="corpoModalExcluirGerenteFilial" style="float:left;margin-left: 10px; width: 575px">
	<form name="detMeta" id="detMeta" method="post"> 
		<input type="hidden" id="idFuncE" name="idFuncE" value=""/>
		<input type="hidden" id="idFilialE" name="idFilialE" value=""/>
		<center><b>Confirmar a exclusão da loja abaixo?</b></center>
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width="25%">
					</td>
					<td class="componente" >
					</td>
				</tr>
				<tr>
					<td class="componente" width="25%">
						<label class="label">Matrícula:</label> 
					</td>
					<td class="componente" >
  						<div id="divMatriculaE"></div>
					</td>
				</tr>			
				<tr>
					<td class="componente" width="25%">
						<label class="label">Nome do Funcionário:</label> 
					</td>
					<td class="componente" >
  						<div id="divNomeE"></div>
					</td>
				</tr>			
				<tr>
					<td class="componente" width="25%">
						<label class="label">Filial:</label> 
					</td>
					<td class="componente" >
						<div id="divFilialE"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" width="25%">
						<label class="label">Atuação:</label> 
					</td>
					<td class="componente" >
						<div id="divAtuacaoE"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" width="25%">
						<label class="label">Data Inclusão:</label> 
					</td>
					<td class="componente" >
						<div id="dtInclusaoE"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" width="25%">
					</td>
					<td class="componente" >
					</td>
				</tr>
			</tbody>
		</table>
		<br/>
		<div id="botao-geral" style="width:100%;">
			<div id="botao" class="barBottom" style="width:100%;">
				<div class="div-botao" style="float:right;width:100%;">
					<input id="botaoSalvar" type="submit" class="button" value="Sim" />
					<input id="botaoCancelar" type="button" class="button" value="Não" />
				</div>
			</div>
		</div>
	</form>
</div>