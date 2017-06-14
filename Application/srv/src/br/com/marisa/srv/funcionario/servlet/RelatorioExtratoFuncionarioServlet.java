package br.com.marisa.srv.funcionario.servlet;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class RelatorioExtratoFuncionarioServlet extends HttpServlet
{
  private static final Logger log = Logger.getLogger(RelatorioExtratoFuncionarioServlet.class);

  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    List lista = new ArrayList();

    lista = (List)request.getSession().getAttribute("funcionarios");

    if (lista.isEmpty())
      System.out.println("LISTA VAZIA");
    else
      System.out.println(lista);
  }
}