package com.jackrabbit.wackrab.sqlite;

public class Db {
	
	protected final java.util.concurrent.locks.Lock lock = new java.util.concurrent.locks.ReentrantLock();
	
	public DbPlayers players;
	public DbWorlds worlds;
	public Db() {
		players = new DbPlayers();
		worlds = new DbWorlds();
	}
}
