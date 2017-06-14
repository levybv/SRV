<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" src="js/ms/srv.usuario.js" charset="ISO-8859-1"></script>
<script type="text/javascript" >
	var idUsuario=-1;
</script> 

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Usuários</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente"><label class="label">Código:</label>
                                	<input id="codigo" name="codigo" class="campo2" maxlength="5" style="width: 50px;" value="<c:out value='${codigo}'/>" type="text"/>
                                </td>
 	                        	<td class="componente"><label class="label">Nome:</label>
                                	<input id="nome" name="nome" class="campo2" maxlength="50" style="width: 300px;" value="<c:out value='${nome}'/>" type="text"/>
                                </td>
 	                        	<td class="componente">
 	                        		<label class="label">Ativo:</label>
                                	<select id="ativo" name="ativo" class="campo">
                                    	<option value="" selected>[TODOS]</option>
                                    	<option value="S"<c:if test="${ativo==true}">selected</c:if>>Sim</option>
                                    	<option value="N"<c:if test="${ativo==false}">selected</c:if>>Não</option>
                                    </select>
                                </td>
                            </tr> 
                        	<tr>
 	                        	<td class="componente"><label class="label">Matrícula:</label>
                                	<input id="matricula" name="matricula" class="campo2" maxlength="38" style="width: 100px;" value="<c:out value='${matricula}'/>" type="text"/>
                                </td>
 	                        	<td class="componente"><label class="label">Login:</label>
                                	<input id="login" name="login" class="campo2" maxlength="20" style="width: 120px;" value="<c:out value='${login}'/>" type="text"/>
                                </td>
 	                        	<td class="componente">
 	                        		<label class="label">Perfil:</label>
                                	<select id="perfil" name="perfil" class="campo">
                                    	<option value="" selected>[TODOS]</option>
										<c:forEach var="itemPerfil" items="${listaPerfil}">
											<option value="<c:out value='${itemPerfil.idPerfil}'/>" <c:if test="${itemPerfil.idPerfil == perfil}">selected</c:if> ><c:out value='${itemPerfil.idPerfil}' /> - <c:out value='${itemPerfil.descricao}' /></option>
										</c:forEach>
                                    </select>
                                </td>
                            </tr> 
                        	<tr>
 	                        	<td class="componente">
                                </td>
 	                        	<td class="componente">
                                </td>
 	                        	<td class="componente">
 	                        		<label class="label">Autentica&ccedil;&atilde;o AD:</label>
                                	<select id="autenticaAD" name="autenticaAD" class="campo">
                                    	<option value="" selected>[TODOS]</option>
                                    	<option value="S"<c:if test="${autenticaAD==true}">selected</c:if>>Sim</option>
                                    	<option value="N"<c:if test="${autenticaAD==false}">selected</c:if>>Não</option>
                                    </select>
                                </td>
                            </tr> 
                        </tbody>
                    </table>
                </div>
				<div id="botao-geral">
					<div id="botao" class="barBottom"> 
						<div id="div-botao">
							<input id="botaoLimpar" type="button" class="button" value="Limpar" />
							<input id="botaoConsultar" type="submit" class="button" value="Filtrar" />
						</div>
						<div id="div-load" style="display: none;">
							<img id="aguardeCargo" alt="Aguarde..." src="images/ajax-loader.gif">
						</div>
					</div>
				</div>
				<br>
				<div class="barTitle">
					Usuários 
					<input id="incluir" type="button" class="buttonAtualizar" style="float:right" value="Incluir" />
					<input id="atualizar" type="button" class="buttonAtualizar" value="Alterar" />
					<br/>
				</div>
				
				<display:table pagesize='10' id="usuarios" name="usuarios" class="table" requestURI="">     
					<display:setProperty name="paging.banner.placement" value="bottom" />   
					<display:column title="" style="cursor: pointer; text-align: center; vertical-align: middle; width:6%;"><input type="radio" name="checkbox" id="checkbox" value="${usuarios.idUsuario}"></display:column>
					<display:column property="idUsuario" title="Código" style="text-align: center; cursor: pointer; vertical-align: middle; width:6%;"/>
					<display:column property="nome" title="Nome" style="text-align: center; cursor: pointer; vertical-align: middle; " /> 
					<display:column property="matricula" title="Matrícula" style="text-align: center; cursor: pointer; vertical-align: middle; width:10%;"/>
					<display:column property="login" title="Login" style="text-align: center; cursor: pointer; vertical-align: middle; width:10%;"/>
					<display:column title="Autenticação AD" style="text-align: center; cursor: pointer; vertical-align: middle; width:6%;">
						<c:choose>
							<c:when test="${usuarios.autenticaAD == true}">Sim</c:when>
							<c:when test="${usuarios.autenticaAD == false}">Não</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</display:column>
					<display:column title="Ativo" style="text-align: center; cursor: pointer; vertical-align: middle; width:6%;">
						<c:choose>
							<c:when test="${usuarios.ativo == true}">Sim</c:when>
							<c:when test="${usuarios.ativo == false}">Não</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</display:column>
					<display:column property="descricaoPerfil" title="Perfil" style="text-align: center; cursor: pointer; vertical-align: middle;"/>
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>