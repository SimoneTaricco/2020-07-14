package it.polito.tdp.PremierLeague.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ThreadLocalRandom;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Simulator {
	
	private Integer nReporter;
	private Double soglia;
	private Graph<Team,DefaultWeightedEdge> grafo;
	private PriorityQueue<Match> queue;
	private PremierLeagueDAO dao;
	private Map<Integer,Team> idMap;
	
	private int numeroPartite;
	
	private Map<Team,Integer> reports;
	private double reporterPartite;
	private int sottoSoglia;
	
	private int T;
	
	public Simulator(Integer nReporter, Double soglia, Graph<Team, DefaultWeightedEdge> grafo, Map<Integer,Team> idMap) {
		super();
		this.dao = new PremierLeagueDAO();
		this.nReporter = nReporter;
		this.soglia = soglia;
		this.grafo = grafo;
		this.idMap = idMap;
	} 
	
	public String getReporterPartite() {
						
		return (new DecimalFormat("#.00")).format(this.reporterPartite/this.numeroPartite); // per avere precisione
	}
	
	public int getSottoSoglia() {
		return this.sottoSoglia;
	}
	
	public void init(){
		
		this.queue = new PriorityQueue<Match>();
		this.T = 0;
		this.reports = new HashMap<Team,Integer>();
		this.reporterPartite = 0;
		this.sottoSoglia = 0;
		
		for (Team t:this.grafo.vertexSet()) {
			reports.put(t, nReporter);
		}
		
		this.queue = new PriorityQueue<Match>((match1, match2) -> 
																	match1.getDate().compareTo(match2.getDate()));
		this.queue.addAll(dao.listAllMatches());
		this.numeroPartite = dao.listAllMatches().size();
	}
	
	public void run() {

		Match match;
		
		while((match = this.queue.poll())!=null) {  
			
		Team teamHome = this.idMap.get(match.getTeamHomeID());
		Team teamAway = this.idMap.get(match.getTeamAwayID());
		int reporterHome = this.reports.get(teamHome);
		int reporterAway = this.reports.get(teamAway);
		
		//System.out.println(teamHome + " " + teamAway + " " + reporterHome + " " + reporterAway);
		
		this.reporterPartite += (reporterHome+reporterAway);
		
		if(reporterHome+reporterAway<this.soglia)
			this.sottoSoglia++;
						
		if(match.resultOfTeamHome == 1) {		// vince la squadra di casa
			int random = (int)(Math.random()*100);
			if (random<=50) {
				
				List<Team> picker = new ArrayList<Team>(Graphs.predecessorListOf(grafo, teamHome)); // si sceglie un team migliore
				if (picker.size()==0) { // non esistono squadre migliori
					continue;
				} else {
					int randomNum = (int)(picker.size()* Math.random());
					Team scelto = picker.get(randomNum); // team a cui va il reporter in più
					reports.put(scelto, this.reports.get(scelto)+1);
					reports.put(teamHome, this.reports.get(scelto)-1);
				}	
			} else  // altro 50%, non succede nulla
				continue;
		}
		else if (match.resultOfTeamHome == 0) { // pareggio: non succede nulla
			continue;
		} else if(match.resultOfTeamHome == -1) {		// vince la squadra ospite
			int random = (int)(Math.random()*100);
			if (random<=20) {
			List<Team> picker = new ArrayList<Team>(Graphs.successorListOf(grafo, teamHome)); // si sceglie un team peggiore	
			if (picker.size()==0) { // non esistono squadre peggiori
				continue;
			} else {
				int randomNum = (int)(picker.size()* Math.random()); // squadra peggiore scelta a caso
				Team scelto = picker.get(randomNum); // team a cui vanno i reporter in più
				
				int numeroReporterBocciati = (int)(reporterHome*Math.random());
				reports.put(scelto, this.reports.get(scelto)+numeroReporterBocciati);
				reports.put(teamHome, this.reports.get(teamHome)-numeroReporterBocciati);
			}	
		} else  // altro 80%, non succede nulla
			continue;
		}
			 
		}					
	}
}
