package br.com.marisa.srv.indicador.ppt.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import br.com.marisa.srv.filial.business.FilialBusiness;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.indicador.ppt.dao.PecaTicketDAO;
import br.com.marisa.srv.indicador.ppt.vo.PecaTicketVO;

/**
 * 
 * @author levy.villar
 *
 */
public class PecaTicketBusiness {

    private static final Logger log = Logger.getLogger(PecaTicketBusiness.class);    

    private static PecaTicketBusiness instance = new PecaTicketBusiness();

    private PecaTicketBusiness() {}

    public static final PecaTicketBusiness getInstance() {
        return instance;
    }

    /**
     * 
     * @param vo
     * @return
     * @throws SRVException
     */
	public List<PecaTicketVO> obterListaRealizadoFilialPecaTicket(PecaTicketVO vo) throws SRVException {
		PecaTicketDAO pecaTicketDAO = new PecaTicketDAO();
		try {
			return pecaTicketDAO.obterListaRealizadoFilialPecaTicket(vo);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new SRVException(ex.getMessage(), ex);
		} finally {
			pecaTicketDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param linhasArquivo
	 * @param idUsuario
	 * @return
	 * @throws SRVException
	 */
	public List<String> importaRealizadoFilialPecaTicket(List<String> linhasArquivo, Integer idUsuario) throws SRVException {

		PecaTicketDAO dao = new PecaTicketDAO();
		dao.beginTrans();

		List<String> erros = new ArrayList<String>();
		List<PecaTicketVO> listaRealizado = new ArrayList<PecaTicketVO>();

		if (ObjectHelper.isNotEmpty(linhasArquivo)) {

			int numLinha = 1;
			for (Iterator<String> it = linhasArquivo.iterator(); it.hasNext();) {
				String linha = it.next();
				
				try {
					StringTokenizer st = new StringTokenizer(linha, Constantes.PONTO_VIRGULA);
					PecaTicketVO vo = new PecaTicketVO();

					int mes = new Integer(st.nextToken());
					if (mes < 1 || mes > 12) {
						erros.add("Mês informado é inválido. Linha: " + numLinha);
						continue;
					} else {
						vo.setNumMes(mes);
					}

					vo.setNumAno(new Integer(st.nextToken()));

					int idFilial = new Integer(st.nextToken());
					if (!FilialBusiness.getInstance().obterFilial(Constantes.CODIGO_EMPRESA, idFilial)) {
						vo.setCodFilial(idFilial);
					} else {
						erros.add("Filial informada é inválida. Linha: " + numLinha);
						continue;
					}

					String valor = st.nextToken();
					if (valor.indexOf(".") > -1) {
						erros.add("Valor realizado é inválido. Linha: " + numLinha);
						continue;
					}
					vo.setNumRealizado(new Double(valor.replaceAll(",", ".")));
					vo.setCodUniRealizado(Constantes.UNIDADE_PECAS_TKT);
					vo.setCodEmpresa(Constantes.CODIGO_EMPRESA);
					vo.setIdUsuario(idUsuario);
					listaRealizado.add(vo);

				} catch (Exception ex) {
					erros.add("Erro de layout ao importar linha: " + numLinha);
				} finally {
					numLinha++;
				}
			}

			if (erros.size() == 0) {
				try {
					for (Iterator<PecaTicketVO> it = listaRealizado.iterator(); it.hasNext();) {
						PecaTicketVO pecaTicketVO = it.next();
						PecaTicketVO antigoVO = dao.obterRealizadoFilialPecaTicket(pecaTicketVO);
						if (antigoVO != null) {
							dao.incluiPecaTicketHist(antigoVO);
							dao.excluiMetaFilial(antigoVO);
						}
						dao.incluiPecaTicket(pecaTicketVO);
					}
					dao.commitTrans();
				} catch (Exception ex) {
					dao.rollbackTrans();
					throw new SRVException(ex.getMessage(), ex);
				} finally {
					dao.closeConnection();
				}
			}
		} else {
			erros.add("Nenhum registro foi enviado no arquivo!");
		}
		
		return erros;
	}
}
