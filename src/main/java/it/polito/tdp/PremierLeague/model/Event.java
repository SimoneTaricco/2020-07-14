package it.polito.tdp.PremierLeague.model;

public class Event implements Comparable<Event>{
	
	public Event(Team teamCasa, Team teamOspiti, Integer t) {
		super();
		this.teamCasa = teamCasa;
		this.teamOspiti = teamOspiti;
		this.t = t;
	}




	private Team teamCasa; // team seguito dal reporter
	private Team teamOspiti; // team seguito dal reporter
	private Integer t;
	


	public Integer getT() {
		return t;
	}




	public void setT(Integer t) {
		this.t = t;
	}




	public Team getTeamCasa() {
		return teamCasa;
	}




	public void setTeamCasa(Team teamCasa) {
		this.teamCasa = teamCasa;
	}




	public Team getTeamOspiti() {
		return teamOspiti;
	}




	public void setTeamOspiti(Team teamOspiti) {
		this.teamOspiti = teamOspiti;
	}




	@Override
	public int compareTo(Event other) { 
		return this.t - other.t;
	}
	
}

