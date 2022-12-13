package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.evita.model.Solicitacao;

public class testesUnitarios {

	@Test
	public void dataTest() throws ParseException {
		Date dt1 = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse("22-10-2022 10:00");
		Date dt2 = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse("22-10-2022 11:00");
		Assert.assertTrue(dt1.before(dt2));
		Assert.assertTrue(dt2.after(dt1));
		
	}
	
	@Test
	public void dataFilterTestDuasDatas() throws ParseException {
		List<Solicitacao> solicitacoes = getLista();
		Date dtInicio = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse("22-10-2022 09:00");
		Date dtFim = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse("22-10-2022 11:00");
		solicitacoes = solicitacoes.stream().filter(
				solicitacao -> (solicitacao.getInicio().after(dtInicio)|| solicitacao.getInicio().equals(dtInicio))
				&& (solicitacao.getFim().before(dtFim) || solicitacao.getFim().equals(dtFim)))
				.collect(Collectors.toList());
		
		Assert.assertTrue(solicitacoes.size() == 2);
		
	}
	
	@Test
	public void dataFilterTestDuasDatas2() throws ParseException {
		List<Solicitacao> solicitacoes = getLista();
		Date dtInicio = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse("22-10-2022 09:00");
		Date dtFim = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse("22-10-2022 10:00");
		solicitacoes = solicitacoes.stream().filter(
				solicitacao -> (solicitacao.getInicio().after(dtInicio)|| solicitacao.getInicio().equals(dtInicio))
						&& (solicitacao.getFim().before(dtFim) || solicitacao.getFim().equals(dtFim)))
				.collect(Collectors.toList());
		
		Assert.assertTrue(solicitacoes.size() == 1);
		
	}
	
	@Test
	public void dataFilterTestUmaData() throws ParseException {
		List<Solicitacao> solicitacoes = getLista();
		Date dtInicio = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse("22-10-2022 10:00");
		solicitacoes = solicitacoes.stream().filter(
				solicitacao -> (solicitacao.getInicio().after(dtInicio)|| solicitacao.getInicio().equals(dtInicio)))
				.collect(Collectors.toList());
		
		Assert.assertTrue(solicitacoes.size() == 2);
		
	}
	
	@Test
	public void dataFilterTestUmaData2() throws ParseException {
		List<Solicitacao> solicitacoes = getLista();
		Date dtFim = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse("22-10-2022 10:00");
		solicitacoes = solicitacoes.stream().filter(
				solicitacao -> (solicitacao.getFim().before(dtFim)|| solicitacao.getFim().equals(dtFim)))
				.collect(Collectors.toList());
		
		Assert.assertTrue(solicitacoes.size() == 1);
		
	}
	
	private List<Solicitacao> getLista() throws ParseException {
		Solicitacao sol1 = new Solicitacao();
		sol1.setInicio("22-10-2022 09:00");
		sol1.setFim("22-10-2022 10:00");
		Solicitacao sol2 = new Solicitacao();
		sol2.setInicio("22-10-2022 10:00");
		sol2.setFim("22-10-2022 11:00");
		Solicitacao sol3 = new Solicitacao();
		sol3.setInicio("22-10-2022 11:00");
		sol3.setFim("22-10-2022 12:00");
		return Arrays.asList(sol1,sol2,sol3);
	}
}
