package br.com.marisa.srv.util.servlets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ArquivoDownload extends HttpServlet
{
  private static final String FILENAME = "Relatorio.xml";
  private static final int BUFSIZE = 1024;

  public void montaDownload(HttpServletRequest request, HttpServletResponse resp, ByteArrayOutputStream baos, String nomeArquivo)
    throws ServletException, IOException
  {
    int length = 0;
    ServletOutputStream op = resp.getOutputStream();

    resp.setContentType("application/octet-stream");

    resp.setContentLength(baos.size());
    if ((nomeArquivo == null) || (nomeArquivo.equals(""))) {
      nomeArquivo = "Relatorio.xml";
    }

    resp.setHeader("Content-Disposition", "attachment; filename=\"" + nomeArquivo + "\"");

    byte[] bbuf = new byte[1024];
    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

    while ((bais != null) && ((length = bais.read(bbuf)) != -1))
    {
      op.write(bbuf, 0, length);
    }

    bais.close();

    op.flush();
    op.close();
  }
}