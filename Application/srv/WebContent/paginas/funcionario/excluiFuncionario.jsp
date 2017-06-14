<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
				$('#idFilialRHF').html(funcionarioVO.idFilialRH);
				$('#descricaoFilialRHF').html(funcionarioVO.descricaoFilialRH);
				$('#crachaF').html(funcionarioVO.cracha);
				$('#cpfFuncionarioF').html(funcionarioVO.cpfFuncionarioFormatado);  
				$('#nomeFuncionarioF').html(funcionarioVO.nomeFuncionario);
				$('#idSituacaoRHF').html(funcionarioVO.idSituacaoRH);
				$('#descricaoSituacaoRHF').html(funcionarioVO.descricaoSituacaoRH);
				$('#dataSituacaoRHF').html(funcionarioVO.dataSituacaoRHFormatada);   
				$('#idEmpresaRHF').html(funcionarioVO.idEmpresaRH);
				$('#descricaoEmpresaRHF').html(funcionarioVO.descricaoEmpresaRH);
				$('#idCentroCustoF').html(funcionarioVO.idCentroCusto);
				$('#descricaoCentroCustoF').html(funcionarioVO.descricaoCentroCusto);
				$('#idFuncionarioSuperiorF').html(funcionarioVO.idFuncionarioSuperior);        
				$('#idFuncionarioAvaliadorF').html(funcionarioVO.idFuncionarioAvaliador);      
				$('#dataAdmissaoF').html(funcionarioVO.dataAdmissaoFormatada);						  
				$('#dataDemissaoF').html(funcionarioVO.dataDemissaoFormatada);			
				$('#idCargoF').html(funcionarioVO.idCargo + " - " + funcionarioVO.descricaoCargo);			  
				$('#idFilialF').html(funcionarioVO.idFilial + " - " + funcionarioVO.descricaoFilial);
				
				if(funcionarioVO.idSituacaoAnterior != null){
				   $('#idSituacaoAnteriorRHF').html(funcionarioVO.idSituacaoAnterior);
				}
										  
				$('#descricaoSituacaoAnteriorRHF').html(funcionarioVO.descricaoSituacaoRH);
				$('#dataInicioSituacaoAnteriorRHF').html(funcionarioVO.dataInicioSituacaoAnteriorFormatada);						  
				$('#idQtdDiasF').html(funcionarioVO.qtdDiasTrabalhadosMes);
				
		});

		
		$("#detMeta").validate({
	    	 rules: {},
		     messages: {},
	         submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
		       	document.detMeta.action = "funcionario.do?operacao=excluiFuncionario";   
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

		<!--Chave-->
		<input type='hidden' id="idFuncionarioF"    name="idFuncionarioF"   value=""/>

		<!--Filtro-->
		<input type='hidden' id="idFuncionarioFiltro"    	name="idFuncionarioF"     	value=""/>
		<input type='hidden' id="nomeFuncionarioFiltro"  	name="nomeFuncionarioF"    	value=""/>
		<input type='hidden' id="crachaFiltro"    			name="crachaF"     			value=""/>
		<input type='hidden' id="cpfFuncionarioFiltro"  	name="cpfFuncionarioF"   	value=""/>
		
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width="30%">
						<label class="label">Filial:</label> 
					</td>
					<td class="componente" >
						<div id="idFilialF"></div>
					</td>
				</tr>				
				<tr>
					<td class="componente" >
						<label class="label">Código Filial RH:</label> 
					</td>
					<td class="componente" >
						<div id="idFilialRHF"></div>
					</td>
				</tr>					
				<tr>
					<td class="componente" >
						<label class="label">Descrição Filial RH:</label> 
					</td>
					<td class="componente" >
						<div id="descricaoFilialRHF"></div>
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Cargo:</label> 
					</td>
					<td class="componente" >
						<div id="idCargoF"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Crachá:</label> 
					</td>
					<td class="componente" >
						<div id="crachaF"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">CPF Funcionário:</label> 
					</td>
					<td class="componente" >
						<div id="cpfFuncionarioF"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Nome Funcionário:</label> 
					</td>
					<td class="componente" >
						<div id="nomeFuncionarioF"></div>
					</td>
				</tr>
				
				
				
				<tr>
					<td class="componente" >
						<label class="label">Código Situação RH:</label> 
					</td>
					<td class="componente" >
						<div id="idSituacaoRHF"></div>
					</td>
				</tr>									
			
													
				<tr>
					<td class="componente" >
						<label class="label">Descrição Situação RH:</label> 
					</td>
					<td class="componente" >
						<div id="descricaoSituacaoRHF"></div>
					</td>
				</tr>									
				<tr>
					<td class="componente" >
						<label class="label">Data Situação RH:</label> 
					</td>
					<td class="componente" >
						<div id="dataSituacaoRHF"></div>
					</td>
				</tr>									
			
													
				<tr>
					<td class="componente" >
						<label class="label">Código Empresa RH:</label> 
					</td>
					<td class="componente" >
						<div id="idEmpresaRHF"></div>
					</td>
				</tr>									
				<tr>
					<td class="componente" >
						<label class="label">Descrição Empresa RH:</label> 
					</td>
					<td class="componente" >
						<div id="descricaoEmpresaRHF"></div>
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Código Centro Custo:</label> 
					</td>
					<td class="componente" >
						<div id="idCentroCustoF"></div>
					</td>
				</tr>									
				<tr>
					<td class="componente" >
						<label class="label">Descrição Centro Custo:</label> 
					</td>
					<td class="componente" >
						<div id="descricaoCentroCustoF"></div>
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Matrícula Superior:</label> 
					</td>
					<td class="componente" >
						<div id="idFuncionarioSuperiorF"></div>
					</td>
				</tr>																
				<tr>
					<td class="componente" >
						<label class="label">Matrícula Avaliador:</label> 
					</td>
					<td class="componente" >
						<div id="idFuncionarioAvaliadorF"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Data Admissão:</label> 
					</td>
					<td class="componente" >
						<div id="dataAdmissaoF"></div>
					</td>
				</tr>									
				<tr>
					<td class="componente" >
						<label class="label">Data Demissão:</label> 
					</td>
					<td class="componente" >
						<div id="dataDemissaoF"></div>
					</td>
				</tr>									
				
				<tr>
					<td class="componente" >
						<label class="label">Código Situação Anterior RH:</label> 
					</td>
					<td class="componente" >
  						<div id="idSituacaoAnteriorRHF"/>
					</td>
				</tr>
				
				<tr>
					<td class="componente" >
						<label class="label">Descrição Situação Anterior RH:</label> 
					</td>
					<td class="componente" >
  						<div id="descricaoSituacaoAnteriorRHF" />
					</td>
				</tr>
				
				<tr>
					<td class="componente" >
						<label class="label">Data Início Situação Anterior RH:</label> 
					</td>
					<td class="componente" >
  						<div id="dataInicioSituacaoAnteriorRHF"/>
					</td>
				</tr>
				
				<tr>
					<td class="componente" >
						<label class="label">Quantidade de Dias Trabalhados no Mês:</label> 
					</td>
					<td class="componente" >
  						<div id="idQtdDiasF"/>
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