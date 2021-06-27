package it.polito.tdp.PremierLeague.model;

public class TeamAndScore {
	
	private Team team;
	private int score;
	
	public TeamAndScore(Team team, int score) {
		super();
		this.team = team;
		this.score = score;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	

}
