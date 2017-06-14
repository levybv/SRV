package br.com.marisa.srv.util.tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.marisa.srv.relatorios.data.CelulaVO;

public class Csv
{
  public String montaTxt(List nomeCampos, List valores, List listaChaveMap)
  {
    List dados = new ArrayList();
    StringBuffer sb = new StringBuffer();
    int qtdCols = nomeCampos.size();

    Iterator it = valores.iterator();
    while (it.hasNext()) {
      Map vals = (Map)it.next();
      for (int x = 0; x < listaChaveMap.size(); x++) {
        CelulaVO celulaVO = (CelulaVO)vals.get(listaChaveMap.get(x));
        CelulaVO celulaFormatada = new CelulaVO();

        switch (celulaVO.getTipoCampo().intValue()) {
        case 91:
        case 93:
          celulaFormatada.setValorCampo(celulaVO.getValorCampo() != null ? obtemDataFormatada(celulaVO.getValorCampo()) : null);

          break;
        case 4:
          Integer inteiro = (Integer)celulaVO.getValorCampo();
          celulaFormatada.setValorCampo(celulaVO.getValorCampo() != null ? inteiro : null);

          break;
        case 12:
          String texto = (String)celulaVO.getValorCampo();
          celulaFormatada.setValorCampo(celulaVO.getValorCampo() != null ? texto : null);

          break;
        case 2:
        case 8:
          Double numero = (Double)celulaVO.getValorCampo();
          if ((numero != null) && (numero.toString().endsWith(".0")))
            celulaFormatada.setValorCampo(celulaVO.getValorCampo() != null ? numero.toString().substring(0, numero.toString().length() - 2) : null);
          else {
            celulaFormatada.setValorCampo(celulaVO.getValorCampo());
          }

          break;
        default:
          celulaVO.setValorCampo(celulaVO.getValorCampo());
        }
        dados.add(celulaFormatada.getValorCampo());
      }
      it.remove();
    }

    for (int x = 0; x < nomeCampos.size(); x++) {
      sb.append(nomeCampos.get(x).toString());
      if (x < nomeCampos.size() - 1) {
        sb.append(";");
      }
    }

    sb.append("\r\n");

    for (int x = 0; x < dados.size(); x++)
    {
      if (dados.get(x) == null)
        sb.append("nulo");
      else {
        sb.append(dados.get(x).toString());
      }
      sb.append(";");

      if (x == qtdCols - 1) {
        sb.deleteCharAt(sb.length() - 1);
        sb.append("\r\n");
        qtdCols += nomeCampos.size();
      }
    }

    return sb.toString();
  }

  public String obtemDataFormatada(Object data)
  {
    SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
    String result = out.format(data);
    return result;
  }
}