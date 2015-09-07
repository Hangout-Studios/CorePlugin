package com.hangout.core.utils.scoreboard;

public class DisplayBoardTags {
	
	private String owner = null;
	private String prefix = null;
	private String suffix = null;
	
	public DisplayBoardTags(String owner, String prefix, String suffix){
		this.owner = owner;
		this.prefix = prefix;
		this.suffix = suffix;
	}
	
	public void setPrefix(String s){
		this.prefix = s;
	}
	
	public void setSuffix(String s){
		this.suffix = s;
	}
	
	public void applyTo(Displayboard b){
		b.setNameTags(owner, prefix, suffix);
	}
}
