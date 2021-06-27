package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Graph<Team,DefaultWeightedEdge> grafo; 
	private Map<Integer,Team> idMap;
	private Map<Team, Integer> punteggi;
	private Simulator sim;
	
	private int sottoSoglia;
	private String reportPerPartita;

	
	public Model() {
		this.dao = new PremierLeagueDAO();
	}
	
	public void creaGrafo() { 

		idMap = new HashMap<Integer,Team>();
		for (Team o:dao.listAllTeams()) { 
			idMap.put(o.getTeamID(), o); 
		}
		
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
			
		Graphs.addAllVertices(grafo, idMap.values()); 
		
		//Map<Team,Integer> getPunteggi(Map<Integer,Team> lista)
		this.punteggi = dao.getPunteggi(idMap);
		
		/*for (Team t:punteggi.keySet()){
			System.out.println(t + " " + punteggi.get(t));
		}*/
		
		for (Team t:punteggi.keySet()) {
			for (Team t1:punteggi.keySet()) {
				if (punteggi.get(t)-punteggi.get(t1)<0) {
					Graphs.addEdge(this.grafo, t1, t, (punteggi.get(t1)-punteggi.get(t)));
				} else if (punteggi.get(t)-punteggi.get(t1)>0) {
					Graphs.addEdge(this.grafo, t, t1, (punteggi.get(t)-punteggi.get(t1)));
				} 
			}
		}	
			
	}
	
	public List<Team> vertici() {
			
		TreeMap<Integer,Team> map = new TreeMap<Integer,Team>();
			
		for (Team o:this.grafo.vertexSet()) 
			map.put(o.teamID, o);

		return new ArrayList<Team>(map.values());
	}
		
	public int numeroArchi() {
		return this.grafo.edgeSet().size();
	}
	
	
	public List<TeamAndScore> getMigliori(Team t){
		
		ArrayList<TeamAndScore> res = new ArrayList<TeamAndScore>();
		
		for (DefaultWeightedEdge e:grafo.incomingEdgesOf(t)) {
			res.add(new TeamAndScore(this.grafo.getEdgeSource(e), (int)grafo.getEdgeWeight(e)));
		}
		
		Collections.sort(res, new Comparator<TeamAndScore>() {
			public int compare(TeamAndScore o1, TeamAndScore o2) {
			if (o2.getScore() - o1.getScore() > 0) 
				return -1;
			else  			
				return 1;
			}
		}); 
		
		return res;		
	}
	
	public List<TeamAndScore> getPeggiori(Team t){
		
		ArrayList<TeamAndScore> res = new ArrayList<TeamAndScore>();
		
		for (DefaultWeightedEdge e:grafo.outgoingEdgesOf(t)) {
			res.add(new TeamAndScore(this.grafo.getEdgeTarget(e), (int)grafo.getEdgeWeight(e)));
		}
		
		Collections.sort(res, new Comparator<TeamAndScore>() {
			public int compare(TeamAndScore o1, TeamAndScore o2) {
			if (o2.getScore() - o1.getScore() > 0) 
				return -1;
			else  			
				return 1;
			}
		}); 
		
		return res;		
	}
	
	public void simula(int nReporter, double soglia) {
		
		this.sim = new Simulator(nReporter, soglia, this.grafo, this.idMap);
		sim.init();
		sim.run();
		
		this.sottoSoglia = sim.getSottoSoglia();
		this.reportPerPartita = sim.getReporterPartite();
			
	}

	public int getSottoSoglia() {
		return sottoSoglia;
	}

	public String getReportPerPartita() {
		return reportPerPartita;
	}
	
	
	
}
