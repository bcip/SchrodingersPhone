package com.github.bcip.schrodingersphone.server;

public abstract class PreparedThread extends Thread{
    /**
     * Return the PreparedStatementSet for the Thread.
     */
	public abstract PreparedStatementSet getPreparedStatementSet();
}
