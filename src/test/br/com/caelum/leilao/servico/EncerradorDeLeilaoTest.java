package br.com.caelum.leilao.servico;

import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.infra.dao.LeilaoDao;
import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EncerradorDeLeilaoTest {

    @Test
    public void deveEncerrarLeiloesQueComecaramUmaSemanaAntes() {
        Calendar antiga = Calendar.getInstance();
        antiga.set(199, 1, 20);

        Leilao leilao1 = Leilao
                .builder()
                .descricao("Tv de plasma")
                .data(antiga)
                .build();

        Leilao leilao2 = Leilao
                .builder()
                .descricao("Geladeira")
                .data(antiga)
                .build();

        LeilaoDao daoFalso = mock(LeilaoDao.class);

        List<Leilao> leiloesAntigos = Arrays.asList(leilao1, leilao2);

        when(daoFalso
                .correntes()).thenReturn(leiloesAntigos);

        EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso);
        encerrador.encerra();

        assertEquals(2, encerrador.getTotalEncerrados());
        assertTrue(leilao1.isEncerrado());
        assertTrue(leilao2.isEncerrado());
    }

    @Test
    public void naoDeveEncerrarLeiloesPassados() {
        Calendar data = Calendar.getInstance();
        data.add(Calendar.DAY_OF_YEAR, -10);
        Leilao leilao1 = Leilao.builder()
                .descricao("Playstation 3")
                .data(data)
                .build();
        Leilao leilao2 = Leilao.builder()
                .descricao("Xbox One Pro")
                .data(Calendar.getInstance())
                .build();
        Leilao leilao3 = Leilao.builder()
                .descricao("Playstation 4")
                .data(data)
                .build();
        List<Leilao> leiloes = Arrays.asList(leilao1, leilao2, leilao3);

        LeilaoDao leilaoDao = mock(LeilaoDao.class);
        when(leilaoDao.correntes()).thenReturn(leiloes);

        List<Leilao> leiloesEncontrados = leilaoDao.correntes();
        EncerradorDeLeilao encerradorDeLeilao = new EncerradorDeLeilao(leilaoDao);
        encerradorDeLeilao.encerra();

        assertTrue(leiloesEncontrados.get(0).isEncerrado());
        assertFalse(leiloesEncontrados.get(1).isEncerrado());
        assertTrue(leiloesEncontrados.get(2).isEncerrado());;
        assertEquals(leiloesEncontrados.size(), 3);
    }

}
